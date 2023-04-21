package joelib.io.types;

import java.awt.Image;
import java.io.IOException;
import java.io.OutputStream;
import joelib.io.SimpleImageWriter;

/**
 * Writer for a Portable Network Graphics (PNG) image.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.11 $, $Date: 2004/08/31 14:23:23 $
 */
public class PNG extends SimpleImageWriter {

    private static final String description = "Portable Network Graphics (PNG) image";

    private static final String[] extensions = new String[] { "png" };

    public String outputDescription() {
        return description;
    }

    public String[] outputFileExtensions() {
        return extensions;
    }

    public boolean writeImage(Image image, OutputStream os) throws IOException {
        return (true);
    }
}
