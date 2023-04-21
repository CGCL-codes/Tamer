package util.tail;

import java.io.File;
import java.io.FilenameFilter;

public abstract class FileFinder {

    /**
	 * 
	 * @param directory
	 * @param pattern I.e. ULOG.
	 * @return
	 */
    public static synchronized File[] beginsWith(File directory, final String pattern) {
        return directory.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.length() < pattern.length()) {
                    return false;
                } else {
                    return name.substring(0, pattern.length()).equals(pattern);
                }
            }
        });
    }

    public static synchronized File latest(File[] files) {
        if (files.length == 0) {
            return null;
        }
        File file = files[0];
        for (int i = 1; i < files.length; i++) {
            if (files[i].lastModified() > file.lastModified()) {
                file = files[i];
            }
        }
        return file;
    }
}
