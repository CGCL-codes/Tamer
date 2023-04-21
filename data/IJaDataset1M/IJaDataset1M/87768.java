package org.nakedobjects.runtime.logging;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.TriggeringEventEvaluator;

public class FileSnapshotAppender extends SnapshotAppender {

    private static final Logger LOG = Logger.getLogger(FileSnapshotAppender.class);

    private String directoryPath;

    private String extension;

    private String fileName = "log-snapshot-";

    public FileSnapshotAppender(final TriggeringEventEvaluator evaluator) {
        super(evaluator);
    }

    public FileSnapshotAppender() {
        super();
    }

    public synchronized String getDirectory() {
        return directoryPath;
    }

    public synchronized String getExtension() {
        return extension;
    }

    public synchronized String getFileName() {
        return fileName;
    }

    public synchronized void setDirectory(final String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public synchronized void setExtension(final String extension) {
        this.extension = extension;
    }

    public synchronized void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    protected synchronized void writeSnapshot(final String message, final String details) {
        SnapshotWriter s;
        try {
            final String contentType = layout.getContentType();
            final String fileExtension = isEmpty(extension) ? contentType.substring(contentType.indexOf('/') + 1) : extension;
            s = new SnapshotWriter(directoryPath, fileName, fileExtension, message);
            s.appendLog(details);
            s.close();
        } catch (final FileNotFoundException e) {
            LOG.error("failed to open log file", e);
        } catch (final IOException e) {
            LOG.error("failed to write log file", e);
        }
    }

    private boolean isEmpty(String extension2) {
        return extension2 == null || extension2.length() == 0;
    }
}
