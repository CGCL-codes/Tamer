package com.entelience.probe;

import java.io.File;
import com.entelience.util.DateHelper;
import com.entelience.util.FileHelper;
import com.entelience.util.NumberHelper;

/**
 * Manage a directory that contains all files we have failures for.
 *
 * Note that this is much like Archive but it doesn't ever delete files.
 *
 * Normally the errors directory should be empty, if it does fill up FileProbeContainer
 * will simply stop working.
 *
 * @see Archive
 */
public class ErrorsArchive {

    private static org.apache.log4j.Logger _logger = com.entelience.util.Logs.getProbeLogger();

    private final File archiveRoot;

    private final File archiveNow;

    private final boolean trialRun;

    private long currentSize = 0l;

    /**
     * Use given directory for an errors archive.
     *
     * @param trialRun if true, don't actually do anything.
     * @param directory path to directory we want to store the errors in.
     */
    protected ErrorsArchive(boolean trialRun, String directory) throws Exception {
        this.trialRun = trialRun;
        archiveRoot = new File(directory);
        archiveNow = new File(directory + File.separator + DateHelper.filenameString(DateHelper.now()));
        if (!trialRun) {
            if (!archiveRoot.exists()) {
                _logger.debug("Making directory [" + archiveRoot.getAbsolutePath() + "] because it doesn't already exist");
                if (!archiveRoot.mkdirs()) {
                    throw new Exception("Unable to make directory [" + archiveRoot.getAbsolutePath() + "]");
                }
            }
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        if (currentSize != 0l) sb.append(" currentSize=").append(currentSize);
        if (trialRun) sb.append(" trialRun");
        sb.append(" archiveRoot=").append(archiveRoot.getAbsolutePath());
        sb.append(" archiveNow=").append(archiveNow.getAbsolutePath());
        if (maxSize > 0l) sb.append(" maxSize=").append(maxSize);
        return sb.toString();
    }

    private long maxSize = 0l;

    /**
     * Set a size limit for this directory.
     */
    protected void setMaxSize(long bytes) {
        this.maxSize = bytes;
    }

    protected long getMaxSize() {
        return maxSize;
    }

    /**
     * Handle archiving a file.  
     * @return true if the file is archived
     */
    protected boolean archive(LocalFileState processed) throws Exception {
        if (trialRun) return false;
        if (!archiveNow.exists()) {
            _logger.debug("Making archive directory [" + archiveNow.getAbsolutePath() + "] because it doesn't already exist");
            if (!archiveNow.mkdir()) {
                throw new Exception("Unable to make archive directory [" + archiveNow.getAbsolutePath() + "]");
            }
        }
        File f = processed.file;
        size();
        try {
            File a = new File(archiveNow.getAbsolutePath() + File.separator + processed.filename);
            if (a.exists()) {
                _logger.warn("Error: target archive file " + a.getAbsolutePath() + " already exists.");
                return false;
            }
            if (f.exists() && f.isFile()) {
                if (FileHelper.moveTo(f.getAbsolutePath(), a.getAbsolutePath())) {
                    currentSize += NumberHelper.roundUpFileSize(a.length());
                    return true;
                }
                return false;
            } else {
                _logger.debug("File " + processed.filename + " does not exist or is not a regular file");
                return false;
            }
        } catch (Exception e) {
            _logger.warn("During archive: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Just calculate current size of the archive directory contents
     */
    protected long size() throws Exception {
        if (currentSize == 0l) {
            File root[] = archiveRoot.listFiles();
            size(root);
        }
        return currentSize;
    }

    /**
     * Recurse file list to calculate size.
     */
    private void size(File dir[]) throws Exception {
        if (dir == null) return;
        for (int i = 0; i < dir.length; ++i) {
            File c = dir[i];
            String name = c.getName();
            if (".".equals(name) || "..".equals(name)) continue;
            if (archiveRoot.equals(c)) continue;
            if (c.isFile()) {
                currentSize += NumberHelper.roundUpFileSize(c.length());
            } else if (c.isDirectory()) {
                File list[] = c.listFiles();
                size(list);
            }
        }
    }
}
