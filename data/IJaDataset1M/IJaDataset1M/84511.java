package net.sf.ij_plugins.ij.utils;

import ij.ImagePlus;
import ij.io.Opener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Jarek Sacha
 * @version $Revision: 1.1 $
 */
public class ImageJIOUtils {

    private ImageJIOUtils() {
    }

    ;

    /**
     * Wrapper for {@link ij.io.Opener#openImage(java.lang.String)} that throws {@link IOException} in case of errors.
     *
     * @param fileName name of the image file to open.
     * @return opened image.
     * @throws IOException in case of problems locating or reading the requested file as an ImageJ supported image
     *                     file.
     */
    public static ImagePlus read(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }
        Opener opener = new Opener();
        ImagePlus imp = opener.openImage(file.getAbsolutePath());
        if (imp == null) {
            throw new IOException("Error opening image file: " + file.getAbsolutePath());
        }
        return imp;
    }
}
