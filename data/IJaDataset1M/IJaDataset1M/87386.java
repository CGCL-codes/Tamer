package irc;

import java.awt.*;

/**
 * Null image loader. This class always fail to load image.
 */
public class NullImageLoader implements ImageLoader {

    public Image getImage(String source) {
        return null;
    }
}
