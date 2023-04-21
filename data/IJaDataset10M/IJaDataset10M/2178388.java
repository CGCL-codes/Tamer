package universe.server.database;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/** This class represnts a file filter for Universe Theme Files. */
public class ThemeFileFilter extends FileFilter {

    private int scheme;

    public ThemeFileFilter(int scheme) {
        this.scheme = scheme;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            String extension = s.substring(i + 1).toLowerCase();
            if (extension.equals("ut")) {
                String tmp = s.substring(0, 3);
                try {
                    int tmpi = Integer.parseInt(tmp);
                    if (tmpi == scheme) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return false;
    }

    public String getDescription() {
        return "Universe Theme Files (*.ut)";
    }
}
