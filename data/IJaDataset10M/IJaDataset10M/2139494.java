package librerias;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author joel
 */
public class XFiltroImagenes extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.gif) || extension.equals(Utils.jpeg) || extension.equals(Utils.jpg) || extension.equals(Utils.png)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "*.png,*.gif,*.jpg,*.jpeg";
    }
}
