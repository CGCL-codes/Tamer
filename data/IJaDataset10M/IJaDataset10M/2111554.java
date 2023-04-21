package org.specular.graphics.images;

import java.io.File;
import org.specular.exceptions.ImageLoadingException;
import org.specular.graphics.Block;

/**
 * This class
 * @author Joshua Mabrey
 */
public class JPEGBlockConverter {

    /**
     * Loads a JPEG format image as a SpecularImage.
     */
    public static Block[][] load(File location) throws ImageLoadingException {
        if (location.canRead()) {
            return new Block[0][0];
        } else throw new ImageLoadingException(location.getName() + " is not readable.");
    }
}
