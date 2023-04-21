package org.speakmon.babble.dcc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author  speakmon
 */
public final class DccFileInfo {

    /** Holds value of property fileStartingPosition. */
    private long fileStartingPosition;

    /** Holds value of property bytesTransferred. */
    private long bytesTransferred;

    /** Holds value of property completeFileSize. */
    private long completeFileSize;

    private File file;

    private RandomAccessFile randomAccessFile;

    private FileChannel fileChannel;

    private ByteBuffer fileByteBuffer;

    private long lastAckValue;

    /** Creates a new instance of DccFileInfo */
    public DccFileInfo(File file, long completeFileSize) {
        this.file = file;
        this.completeFileSize = completeFileSize;
        fileStartingPosition = 0;
        bytesTransferred = 0;
    }

    public DccFileInfo(File file) {
        this.file = file;
    }

    public DccFileInfo(String filename) {
        this.file = new File(filename);
    }

    /** Getter for property fileStartingPosition.
     * @return Value of property fileStartingPosition.
     *
     */
    public long getFileStartingPosition() {
        return this.fileStartingPosition;
    }

    /** Getter for property bytesTransferred.
     * @return Value of property bytesTransferred.
     *
     */
    public long getBytesTransferred() {
        return this.bytesTransferred;
    }

    /** Getter for property completeFileSize.
     * @return Value of property completeFileSize.
     *
     */
    public long getCompleteFileSize() {
        return this.completeFileSize;
    }

    /** Getter for property dccFileName.
     * @return Value of property dccFileName.
     *
     */
    public String getDccFileName() {
        return file.getName();
    }

    protected void addBytesTransferred(int additionalBytes) {
        synchronized (this) {
            bytesTransferred += additionalBytes;
        }
    }

    protected boolean acceptPositionMatches(long position) {
        return position == fileStartingPosition;
    }

    protected void gotoWritePosition() throws IOException {
        randomAccessFile.seek(fileStartingPosition + 1);
    }

    protected void gotoReadPosition() throws IOException {
        randomAccessFile.seek(fileStartingPosition);
    }

    protected boolean isResumePositionValid(long position) {
        return position > 1 && position < file.length();
    }

    protected boolean canResume() {
        return true;
    }

    protected void setResumeToFileSize() {
        fileStartingPosition = file.length();
    }

    protected void setResumePosition(long resumePosition) {
        fileStartingPosition = resumePosition;
        bytesTransferred = fileStartingPosition;
    }

    protected long currentFilePosition() throws IOException {
        return fileChannel.position();
    }

    protected boolean allBytesTransferred() {
        if (completeFileSize == 0) {
            return false;
        } else {
            return (fileStartingPosition + bytesTransferred) == completeFileSize;
        }
    }

    protected void closeFile() throws IOException {
        if (file != null) {
            randomAccessFile.close();
        }
    }

    protected void openForRead() throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(file, "r");
    }

    protected void openForWrite() throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(file, "rws");
    }

    protected boolean shouldResume() {
        return file.length() > 0 && canResume();
    }

    protected boolean acksFinished(long ack) {
        boolean done = (ack == bytesTransferred || ack == lastAckValue);
        lastAckValue = ack;
        return done;
    }
}
