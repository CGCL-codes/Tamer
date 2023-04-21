package org.cobertura4j2me.reporting.html.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class CopyFiles {

    public static void copy(File outputDir, File logo) throws IOException {
        File cssOutputDir = new File(outputDir, "css");
        File imagesOutputDir = new File(outputDir, "images");
        File jsOutputDir = new File(outputDir, "js");
        outputDir.mkdirs();
        cssOutputDir.mkdir();
        imagesOutputDir.mkdir();
        jsOutputDir.mkdir();
        copyResourceFromJar("help.css", cssOutputDir);
        copyResourceFromJar("main.css", cssOutputDir);
        copyResourceFromJar("sortabletable.css", cssOutputDir);
        copyResourceFromJar("source-viewer.css", cssOutputDir);
        copyResourceFromJar("tooltip.css", cssOutputDir);
        copyResourceFromJar("blank.png", imagesOutputDir);
        copyResourceFromJar("downsimple.png", imagesOutputDir);
        copyResourceFromJar("upsimple.png", imagesOutputDir);
        copyResourceFromJar("percentagesorttype.js", jsOutputDir);
        copyResourceFromJar("popup.js", jsOutputDir);
        copyResourceFromJar("sortabletable.js", jsOutputDir);
        copyResourceFromJar("stringbuilder.js", jsOutputDir);
        copyResourceFromJar("help.html", outputDir);
        if (logo != null && logo.exists()) {
            copyFile(new FileInputStream(logo), imagesOutputDir, logo.getName());
        }
    }

    /**
	 * Copy a file from the jar to a directory on the local machine.
	 *
	 * @param resourceName The name of the file in the jar.  This file
	 *        must exist the same package as this method.
	 * @param directory The directory to copy the jar to.
	 * @throws IOException If the file could not be read from the
	 *         jar or written to the disk.
	 */
    private static void copyResourceFromJar(String resourceName, File directory) throws IOException {
        InputStream in = CopyFiles.class.getResourceAsStream(resourceName);
        copyFile(in, directory, resourceName);
    }

    /**
	 * Copies the content of the given input stream to the file <tt>name<tt>
	 * in directory <tt>directory</tt>
	 * 
	 * @param in
	 * @param directory
	 * @param name
	 * 
	 * @throws IOException
	 */
    private static void copyFile(InputStream in, File directory, String name) throws IOException {
        int n;
        byte[] buf = new byte[1024];
        FileOutputStream out = null;
        directory.mkdirs();
        try {
            if (in == null) throw new IllegalArgumentException("Resource " + name + " does not exist in this package.");
            out = new FileOutputStream(new File(directory, name));
            while ((n = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, n);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
