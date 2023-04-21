package org.geoforge.io.finder;

import javax.swing.ImageIcon;

/**
 *
 * @author bantchao
 */
public abstract class GfrFndResImgExtAbs extends GfrFndResImgAbs {

    private static final String _STR_NAME_SUFFIX_ = "external";

    protected static ImageIcon _s_getImageIcon(ClassLoader cld, String strFolderChild, String[] strsFileName, Integer itgSize) {
        return GfrFndResImgAbs._s_getImageIcon(cld, GfrFndResImgExtAbs._STR_NAME_SUFFIX_, strFolderChild, strsFileName, itgSize);
    }

    protected static ImageIcon _s_getImageIcon(ClassLoader cld, String strFolderChild, String[] strsFileName, String strBackground, Integer itgSize) {
        return GfrFndResImgAbs._s_getImageIcon(cld, GfrFndResImgExtAbs._STR_NAME_SUFFIX_, strFolderChild, strsFileName, strBackground, itgSize);
    }

    protected GfrFndResImgExtAbs() {
        super();
    }
}
