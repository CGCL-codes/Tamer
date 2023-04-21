package freedbimporter.util;

import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Enumerates (walks) files.
 * <p>
 *
 * @version      2.1 by 2.12.2009
 * @author       Copyright 2004 <a href="MAILTO:freedb2mysql@freedb2mysql.de">Christian Kruggel</a> - freedbimporter and all it&acute;s parts are free software and destributed under <a href="http://www.gnu.org/licenses/gpl-2.0.txt" target="_blank">GNU General Public License</a>
 */
public class FileWalker implements Enumeration<File> {

    File startFile = null;

    File nextFile = null;

    int nextFileIndex = 0;

    boolean sort = false;

    File[] nextFiles = null;

    FileWalker nextFileWalker = null;

    public FileWalker(File file, boolean sortDirs) {
        startFile = new File(file.getAbsolutePath());
        if (file.isFile()) nextFile = file; else {
            nextFiles = file.listFiles();
            if (sortDirs) {
                this.sort = true;
                Arrays.sort(nextFiles);
            }
        }
    }

    public boolean hasMoreElements() {
        return ((nextFile != null) || (nextFiles != null));
    }

    /**
    public boolean hasMoreFiles() {
        return ((nextFile != null) || (nextFiles != null));
    }

    public Object nextElement() {
        return nextFile();
    }
    **/
    public File nextElement() {
        if (!hasMoreElements()) throw new NoSuchElementException("No more files to enumerat from " + startFile.getName());
        if (nextFile != null) {
            File result = nextFile;
            nextFile = null;
            return result;
        }
        if (nextFileIndex == nextFiles.length) {
            nextFileWalker = null;
            nextFiles = null;
            nextFile = null;
            return startFile;
        }
        if (nextFileWalker == null) nextFileWalker = new FileWalker(nextFiles[nextFileIndex], sort);
        if (nextFileWalker.hasMoreElements()) return nextFileWalker.nextElement(); else {
            nextFileWalker = null;
            nextFileIndex++;
            return nextElement();
        }
    }
}
