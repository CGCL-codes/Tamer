package org.nakedobjects.utility.logging;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;

public class FileSnapshotAppender extends SnapshotAppender {

    private static final Logger LOG = Logger.getLogger(FileSnapshotAppender.class);

    private String directoryPath;

    private String extension;

    private String fileName = "log-snapshot-";

    public String getDirectory() {
        return directoryPath;
    }

    public String getExtension() {
        return extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setDirectory(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    protected synchronized void writeSnapshot(String message, String details) {
        SnapshotWriter s;
        try {
            s = new SnapshotWriter(directoryPath, fileName, extension, message);
            s.appendLog(details);
            s.close();
        } catch (FileNotFoundException e) {
            LOG.info("failed to open log file", e);
        } catch (IOException e) {
            LOG.info("failed to write log file", e);
        }
    }
}
