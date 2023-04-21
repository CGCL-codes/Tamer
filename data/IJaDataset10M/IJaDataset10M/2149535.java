package nz.org.venice.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Image filter for dialog boxes.
 * 
 * @author Mark Hummel
 
 */
public class ImageFilter extends FileFilter {

    public static final String jpeg = "jpeg";

    public static final String jpg = "jpg";

    public static final String gif = "gif";

    public static final String tiff = "tiff";

    public static final String tif = "tif";

    public static final String png = "png";

    public static final String bmp = "bmp";

    public boolean accept(File f) {
        boolean rv = false;
        if (f.isDirectory()) {
            return false;
        }
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(tiff) || extension.equals(tif) || extension.equals(gif) || extension.equals(jpeg) || extension.equals(jpg) || extension.equals(bmp) || extension.equals(png)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf(".");
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public String getDescription() {
        return Locale.getString("GRAPH_EXPORT_DIALOG");
    }
}
