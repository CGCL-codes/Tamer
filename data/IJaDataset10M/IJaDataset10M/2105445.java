package migool.util;

import java.io.File;
import java.io.FileFilter;

/**
 * 
 * @author Denis Migol
 * 
 */
public final class FileUtil {

    /**
	 * Don't let anyone instantiate this class.
	 */
    private FileUtil() {
    }

    /**
	 * file.isDirectory()
	 */
    public static final FileFilter DIRECTORY_FILE_FILTER = new FileFilter() {

        @Override
        public boolean accept(final File file) {
            return file.isDirectory();
        }
    };

    /**
	 * !file.isDirectory()
	 */
    public static final FileFilter NOT_DIRECTORY_FILE_FILTER = new FileFilter() {

        @Override
        public boolean accept(final File file) {
            return !file.isDirectory();
        }
    };

    /**
	 * 
	 * @return
	 */
    public static File getCwd() {
        return new File("");
    }

    /**
	 * 
	 * @param dir
	 */
    public static void mkDir(final File dir) {
        if (dir != null) {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
        }
    }

    /**
	 * 
	 * @param dir
	 * @return
	 */
    public static boolean isDirectoryExists(final File dir) {
        return (dir == null) ? false : (dir.isDirectory() && dir.exists()) ? true : false;
    }

    /**
	 * 
	 * @param file
	 * @return
	 */
    public static boolean isFileExists(final File file) {
        return (file == null) ? false : file.isFile() && file.exists();
    }

    /**
	 * 
	 * @param file
	 */
    public static void delete(final File file) {
        if (file != null) {
            if (file.isDirectory()) {
                for (final File f : file.listFiles()) {
                    delete(f);
                }
            }
            file.delete();
        }
    }
}
