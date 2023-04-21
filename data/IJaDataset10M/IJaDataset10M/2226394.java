package org.frontburner.util;

import java.io.File;
import java.io.IOException;
import org.log4j.Category;
import org.log4j.Priority;

/**
 * Preferences directory in the user's home directory.  Constructing a
 * DotDir object will cause a named directory in the user's home
 * directory to be checked for writing; or, if the named directory
 * does not exist, it will be created.
 * 
 * @author Marc Hedlund &lt;marc@precipice.org&gt;
 * @version $Revision: 1.1 $ 
 */
public class DotDir {

    private static Category LOG = Category.getInstance(DotDir.class.getName());

    private String dotDirName = null;

    private File dotDir = null;

    public DotDir(String dotDirName) throws IOException {
        Contract.argNotNull(dotDirName);
        if (LOG.isEnabledFor(Priority.DEBUG)) {
            LOG.debug("DotDir constructor received dotDirName " + dotDirName);
        }
        this.dotDirName = dotDirName;
        this.dotDir = getDirectory();
        if (!dotDirOkay()) {
            createDotDir();
        }
    }

    public File getDirectory() {
        if (dotDir != null) {
            return dotDir;
        } else {
            String homeDirName = System.getProperty("user.home");
            if (LOG.isEnabledFor(Priority.INFO)) {
                LOG.info("User's home directory is '" + homeDirName + "'");
            }
            return new File(homeDirName, dotDirName);
        }
    }

    private boolean dotDirOkay() {
        Contract.assertNotNull("DotDir File field is null", dotDir);
        if (!dotDir.exists()) {
            if (LOG.isEnabledFor(Priority.INFO)) {
                LOG.info("DotDir does not exist");
            }
            return false;
        } else if (!dotDir.isDirectory()) {
            if (LOG.isEnabledFor(Priority.INFO)) {
                LOG.info("DotDir exists but is not a directory");
            }
            return false;
        } else if (!dotDir.canRead()) {
            if (LOG.isEnabledFor(Priority.INFO)) {
                LOG.info("DotDir is not readable");
            }
            return false;
        } else if (!dotDir.canWrite()) {
            if (LOG.isEnabledFor(Priority.INFO)) {
                LOG.info("DotDir is not writable");
            }
            return false;
        }
        return true;
    }

    private void createDotDir() throws IOException {
        Contract.assertNotNull("DotDir File field is null", dotDir);
        if (LOG.isEnabledFor(Priority.INFO)) {
            LOG.info("Creating DotDir " + dotDir.getCanonicalPath());
        }
        dotDir.mkdir();
        if (!dotDirOkay()) {
            throw new IOException("Failed to create DotDir");
        }
    }
}
