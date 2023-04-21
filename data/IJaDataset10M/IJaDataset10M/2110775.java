package org.apache.commons.io.filefilter;

import java.io.File;

/**
 * This filter accepts <code>File</code>s that are files (not directories).
 * <p>
 * For example, here is how to print out a list of the real files
 * within the current directory:
 *
 * <pre>
 * File dir = new File(".");
 * String[] files = dir.list( FileFileFilter.FILE );
 * for ( int i = 0; i &lt; files.length; i++ ) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 *
 * @since Commons IO 1.3
 * @version $Revision: 155419 $ $Date: 2006-08-27 23:39:07 -0700 (Sun, 27 Aug 2006) $
 */
public class FileFileFilter extends AbstractFileFilter {

    /** Singleton instance of file filter */
    public static final IOFileFilter FILE = new FileFileFilter();

    /**
     * Restrictive consructor.
     */
    protected FileFileFilter() {
    }

    /**
     * Checks to see if the file is a file.
     *
     * @param file  the File to check
     * @return true if the file is a file
     */
    public boolean accept(File file) {
        return file.isFile();
    }
}
