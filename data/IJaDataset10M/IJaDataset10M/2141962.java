package org.fonteditor.utilities.resources;

import org.fonteditor.utilities.general.For;

/**
 * A class containing static methods which load images -
 * including images within jar files...
 * <p>
 * @author Tim Tyler
 * @version 1.0
 */
public class ResourceLoader {

    private static final boolean RESOURCES_ZIPPED = false;

    /**
   * Get an image.
   * <p>
   * Loads a specified file, either from the currect directory,
   * Or from inside the relevant jar file, whichever is appropriate.
   */
    public static byte[] getByteArray(String archive, String dir, String leaf) {
        For.get(archive);
        byte[] array;
        try {
            if (RESOURCES_ZIPPED) {
                array = ZipLoader.getByteArray(archive, leaf);
            } else {
                array = FileLoader.getByteArray(dir + leaf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return array;
    }
}
