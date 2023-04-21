package reconcile.general;

import java.io.File;
import java.io.FileFilter;

/**
 * A central place to keep file filters
 * 
 * @author <a href="mailto:buttler1@llnl.gov">David Buttler</a>
 * 
 */
public class Filters {

    /**
 * @author <a href="mailto:buttler1@llnl.gov">David Buttler</a>
 * 
 */
    static class NamedFileFilter implements FileFilter {

        private String mName;

        /**
 * @param name
 */
        public NamedFileFilter(String name) {
            mName = name;
        }

        public boolean accept(File pathname) {
            if (pathname.isDirectory() || pathname.getName().equals(mName)) return true;
            return false;
        }
    }

    /**
 * @author <a href="mailto:hysom@llnl.gov">David Hysom</a>
 * 
 */
    static class FileFileFilter implements FileFilter {

        /**
 * @param name
 */
        public FileFileFilter() {
        }

        public boolean accept(java.io.File pathname) {
            if (pathname.isDirectory() || pathname.isFile()) return true;
            return false;
        }
    }

    /**
 * returns true on directories and files
 */
    public static final FileFilter isFileFileFilter = createIsFile();

    public static FileFilter createIsFile() {
        return new FileFileFilter();
    }

    /**
 * returns true on directories and files named 'raw.txt'
 */
    public static final FileFilter rawFileFilter = create("raw.txt");

    /**
 * Create a file filter that returns true on all directories and on files with the given name
 */
    public static FileFilter create(String name) {
        return new NamedFileFilter(name);
    }
}
