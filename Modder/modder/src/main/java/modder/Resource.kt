package modder

import java.io.File
import java.net.URLDecoder
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

/* 
* class that handles reading resources file from jar
* because reading file directly will throw an error
* 
* so it must be read as an input stream
* https://stackoverflow.com/a/20389418/14073678
*/
class Resource {
    /*
     * [resourceFile]: path to resource file, must start with '/'
     *
     * this function cannot be static, because it needs to use [javaClass]
     * (it cannot be declared in companion/static)
     */

    fun CopyResourceFile(resourceFile: String, destFile: String) {
        val _in = javaClass.getResourceAsStream(resourceFile)!!
        val outputPath = Paths.get(destFile)
        System.out.printf("Copying resources file %s to %s\n", resourceFile, destFile)
        Files.copy(_in, outputPath, StandardCopyOption.REPLACE_EXISTING)
    }

    companion object {

        fun GetFile(classLoader: ClassLoader, resourceFile: String): File {
            // need to decode the path, because for some reason,
            // getResource().getFile replace space with %20
            // and it will cause File.exist() to fail
            // what a mess

            // decoding the url seems to fix it
            // https://stackoverflow.com/questions/31133361/how-to-get-file-from-resources-when-blank-space-is-in-path
            // https://stackoverflow.com/a/12125969/14073678
            val filePath = URLDecoder.decode(
                    classLoader.getResource(resourceFile).file,
                    "UTF-8")
            return File(filePath)
        }
    }
}
