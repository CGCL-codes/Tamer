package intellibitz.sted.io;

import intellibitz.sted.event.IStatusEventSource;
import intellibitz.sted.event.IStatusListener;
import intellibitz.sted.event.StatusEvent;
import intellibitz.sted.event.ThreadEventSourceBase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class FileReaderThread extends ThreadEventSourceBase implements IStatusEventSource {

    private File file;

    private static final Logger logger = Logger.getLogger("intellibitz.sted.io.FileReaderThread");

    private IStatusListener statusListener;

    private StatusEvent statusEvent;

    public FileReaderThread() {
        super();
        statusEvent = new StatusEvent(this);
    }

    public FileReaderThread(File file) {
        this();
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public IStatusListener getStatusListener() {
        return statusListener;
    }

    public void setStatusListener(IStatusListener statusListener) {
        this.statusListener = statusListener;
    }

    public StatusEvent getStatusEvent() {
        return statusEvent;
    }

    public void setStatusEvent(StatusEvent statusEvent) {
        this.statusEvent = statusEvent;
    }

    public void run() {
        fireThreadRunStarted();
        logger.entering(getClass().getName(), "run");
        try {
            if (file == null) {
                setMessage("File is null");
                fireThreadRunFailed();
            } else {
                setResult(getFileContents(file));
                fireThreadRunFinished();
            }
        } catch (FileNotFoundException e) {
            setMessage("Cannot Read File - File not found: " + e.getMessage());
            logger.throwing(getClass().getName(), "run", e);
            fireThreadRunFailed();
        } catch (IOException e) {
            setMessage("Cannot Read File - IOException: " + e.getMessage());
            logger.throwing(getClass().getName(), "run", e);
            fireThreadRunFailed();
        }
        logger.exiting(getClass().getName(), "run");
    }

    private String getFileContents(File file) throws IOException {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            final int sz = (int) file.length();
            final char[] cbuf = new char[sz];
            if (sz > 0) {
                bufferedReader = new BufferedReader(fileReader, sz);
                setProgressMaximum(sz);
                int count = 0;
                int len = 100;
                if (len > sz - count) {
                    len = sz - count;
                }
                int offset = count;
                logger.finest("File Size: " + sz);
                logger.finest("File Offset: " + offset);
                logger.finest("File Length to be read: " + len);
                while (bufferedReader.read(cbuf, offset, len) > 0) {
                    count += len;
                    if (len > sz - count) {
                        len = sz - count;
                    }
                    offset = count;
                    setProgress(count);
                    fireThreadRunning();
                }
            }
            return String.valueOf(cbuf);
        } finally {
            if (bufferedReader != null) {
                fileReader.close();
                bufferedReader.close();
            }
        }
    }

    public void fireStatusPosted() {
        statusListener.statusPosted(getStatusEvent());
    }

    public void addStatusListener(IStatusListener statusListener) {
        setStatusListener(statusListener);
    }
}
