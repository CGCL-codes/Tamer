package cookxml.cookswing.helper;

import java.awt.*;
import cookxml.core.interfaces.Helper;

/**
 * Helper class for constructing an Image object.
 *
 * @cxhelper java.awt.Image
 *
 * @author Heng Yuan
 * @version $Id: ImageHelper.java 215 2007-06-06 03:59:41Z coconut $
 * @since CookSwing 1.0
 */
public class ImageHelper implements Helper {

    /** The image resource. */
    public Image src;

    public Object getFinalObject() {
        return src;
    }
}
