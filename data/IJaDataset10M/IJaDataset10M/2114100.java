package filters;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author stylesuxx@gmail.com
 */
public class Mp3Filter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        if (new File(dir, name).isDirectory()) {
            return false;
        }
        name = name.toLowerCase();
        return name.endsWith(".mp3");
    }
}
