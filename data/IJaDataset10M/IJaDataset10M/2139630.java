package meraner81.jets.creating.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * MapFileFilter for opening .map files.
 * 
 * @author Martin Meraner
 *
 */
public class MapFileFilter extends FileFilter implements java.io.FileFilter {

    private static final String MAP = ".map";

    private static final String Q3_MAP = "Q3 map";

    @Override
    public boolean accept(File f) {
        return checkExtension(f) || f.isDirectory();
    }

    public boolean checkExtension(File f) {
        return (f.getName().toLowerCase().endsWith(MAP));
    }

    @Override
    public String getDescription() {
        return Q3_MAP;
    }
}
