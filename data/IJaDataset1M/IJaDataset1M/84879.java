package passreminder;

import java.io.File;
import java.io.FileFilter;

public class IconChoiceFilter implements FileFilter {

    public boolean accept(File file) {
        return file.getName().endsWith(".gif") || file.getName().endsWith(".png");
    }
}
