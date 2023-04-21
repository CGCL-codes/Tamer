package freedbimporter.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Writes plain text to a textfile.
 * <p>
 * uses {@link RawTextReader#UTF8}-encoding
 *
 * @version      2.2 by 12.12.2009
 * @author       Copyright 2004 <a href="MAILTO:freedb2mysql@freedb2mysql.de">Christian Kruggel</a> - freedbimporter and all it&acute;s parts are free software and destributed under <a href="http://www.gnu.org/licenses/gpl-2.0.txt" target="_blank">GNU General Public License</a>
 */
public class RawTextLogger implements Logger {

    public static int BUFFER_SIZE = 4096 * 16;

    boolean append = false;

    boolean keepOpened = true;

    boolean isClosed = true;

    char[] logBuffer = null;

    int bufferFilledUpToPosition = 0;

    File logFile = null;

    OutputStreamWriter logStream = null;

    long charsWritten = 0;

    public RawTextLogger(File fileToLogTo, boolean append, boolean keepOpened) throws IOException {
        logFile = fileToLogTo;
        logBuffer = new char[BUFFER_SIZE];
        this.keepOpened = keepOpened;
        this.append = append;
        open();
    }

    public RawTextLogger(File fileToLogTo) throws IOException {
        this(fileToLogTo, false, true);
    }

    /**
     * gibt an, ob zwei RawTextLogger gleich sind.
     * <p>
     * Zwei RawTextLogger sind dann und nur dann gleich, wenn ihre {@link #getLoggerName() Dateinamen} gleich sind.
     * Beim Vergleich der Namen wird <i>nicht</i> zwischen Gro&szlig;- und Kleinschreibung unterschieden.
     * <p>
     * @return <code>true</code>, falls die Dateinamen der RawTextLogger gleich sind, sonst <code>false</code>
     */
    public boolean equals(Object compare) {
        if (compare == null) return false;
        if (compare == this) return true;
        if (getClass().equals(compare.getClass())) {
            RawTextLogger compareLogger = (RawTextLogger) compare;
            if (getLoggerName().equalsIgnoreCase(compareLogger.getLoggerName())) return true; else return false;
        } else return false;
    }

    public String getLoggerName() {
        return logFile.getAbsolutePath();
    }

    public void print(String string) {
        for (int lesenImEingabeString = 0; lesenImEingabeString < string.length(); lesenImEingabeString++) storeCharInBuffer(string.charAt(lesenImEingabeString));
    }

    public void println(String string) {
        print(string);
        print(System.getProperty("line.separator"));
        charsWritten = charsWritten - System.getProperty("line.separator").length();
    }

    protected void storeCharInBuffer(char zuSchreibendesZeichen) {
        charsWritten++;
        if (bufferFilledUpToPosition == (logBuffer.length - 1)) flush();
        logBuffer[bufferFilledUpToPosition] = zuSchreibendesZeichen;
        bufferFilledUpToPosition++;
    }

    public boolean isFlushed() {
        return (bufferFilledUpToPosition == 0);
    }

    /**
     * flushes internal buffer to filesystem.
     * <p>
     * Might lead into some closing and re-opening that is
     * <a href="http://256.com/gray/docs/misc/java_bad_file_descriptor_close_bug.shtml" target="_blank">not recommended</a>.
     */
    public boolean flush() {
        if (isFlushed()) return true;
        synchronized (this) {
            try {
                if (isClosed) open();
                logStream.write(logBuffer, 0, bufferFilledUpToPosition);
                logStream.flush();
                bufferFilledUpToPosition = 0;
            } catch (IOException e) {
                System.err.println(getLoggerName() + ".flush() - c - " + e);
            } finally {
                if (!keepOpened) try {
                    logStream.close();
                    isClosed = true;
                } catch (IOException e) {
                    System.err.println(getLoggerName() + ".flush() - f - " + e);
                }
            }
            notifyAll();
        }
        return isFlushed();
    }

    public boolean renameTo(String newFileName) {
        if (!isClosed) try {
            close();
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
            return false;
        }
        File newFile = new File(logFile.getParentFile().getAbsolutePath() + File.separatorChar + newFileName);
        boolean succeeded = logFile.renameTo(newFile);
        if (succeeded) logFile = newFile;
        return succeeded;
    }

    public long getNumberOfCharsWritten() {
        return charsWritten;
    }

    protected void open() throws IOException {
        logStream = new OutputStreamWriter(new FileOutputStream(logFile, append), RawTextReader.UTF8);
        isClosed = false;
    }

    public void close() throws IOException {
        if (!isClosed) {
            while (!flush()) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    System.err.println(getLoggerName() + ".close() - " + e);
                }
            }
            logStream.close();
            isClosed = true;
        }
    }

    public void finalize() {
        try {
            close();
        } catch (IOException e) {
            System.err.println(this + ".finalize() - " + e);
        }
        try {
            super.finalize();
        } catch (Throwable t) {
            System.err.println(super.toString() + ".finalize() - " + t);
        }
    }

    public static RawTextLogger getRawTextLogger(String name) {
        if (name == null) return null;
        name = name.trim();
        if (name.length() == 0) return null;
        try {
            return new RawTextLogger(new File(name));
        } catch (IOException iE) {
            System.err.println(iE.getMessage());
            return null;
        }
    }
}
