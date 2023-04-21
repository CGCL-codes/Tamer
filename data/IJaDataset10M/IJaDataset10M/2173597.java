package de.schwarzrot.ui.image;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import de.schwarzrot.system.support.FileUtils;

/**
 * a filter component for {@code JFileChooser} to select image files only
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 */
public class ImageFilter extends FileFilter {

    public static final String EXT_BMP = "bmp";

    public static final String EXT_TIFF = "tiff";

    public static final String EXT_TIF = "tif";

    public static final String EXT_GIF = "gif";

    public static final String EXT_JPG = "jpg";

    public static final String EXT_JPEG = "jpeg";

    public static final String EXT_PNG = "png";

    @Override
    public boolean accept(File aFile) {
        if (aFile.isDirectory()) {
            return true;
        }
        String xt = FileUtils.getExtension(aFile);
        if (xt != null) {
            if (xt.equals(EXT_TIFF) || xt.equals(EXT_TIF) || xt.equals(EXT_GIF) || xt.equals(EXT_BMP) || xt.equals(EXT_JPEG) || xt.equals(EXT_JPG) || xt.equals(EXT_PNG)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "bmp, gif, tiff, jpeg and png";
    }
}
