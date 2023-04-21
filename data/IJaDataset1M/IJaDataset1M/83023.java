package checker.event;

import checker.FileID;

/**
 * A processing event for LicenseChecker.
 * Used to track processing events and notify the GUI about them
 * (ie. progress indicator).
 * 
 * @author Veli-Jussi Raitila
 *
 */
public class LicenseProcessEvent {

    private FileID file;

    private int fileIndex;

    private int fileCount;

    public LicenseProcessEvent(FileID f, int fi, int fc) {
        file = f;
        fileIndex = fi;
        fileCount = fc;
    }

    public FileID getFile() {
        return file;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getFileIndex() {
        return fileIndex;
    }
}
