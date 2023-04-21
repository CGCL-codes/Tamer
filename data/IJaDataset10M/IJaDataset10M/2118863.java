package voldemort.lucene;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.lucene.store.IndexInput;
import voldemort.client.StoreClient;
import voldemort.lucene.locking.SegmentReadLocker;

/** Responsible for reading from <code>VoldemortDirectory</code>
 *
 * @author W.J. Blackburn
 * @author Sanne Grinovero
 * @author Davide Di Somma
 */
public class VoldemortSegmentedIndexInput extends IndexInput {

    private static Logger log = Logger.getLogger(VoldemortSegmentedIndexInput.class);

    private final StoreClient client;

    private final FileKey fileKey;

    private final int segmentSize;

    private final SegmentReadLocker readLocks;

    private final String filename;

    private final long fileLength;

    private int currentBufferSize;

    private byte[] buffer;

    private int bufferPosition;

    private int currentLoadedSegment = -1;

    private boolean isClone;

    public VoldemortSegmentedIndexInput(StoreClient client, FileKey fileKey, FileMetadata fileMetadata, SegmentReadLocker readLocks) throws FileNotFoundException {
        this.client = client;
        this.fileKey = fileKey;
        this.segmentSize = fileMetadata.getBufferSize();
        this.fileLength = fileMetadata.getSize();
        this.readLocks = readLocks;
        this.filename = fileKey.getFileName();
        if (log.isDebugEnabled()) {
            log.debug(String.format("Opened new VoldemortSegmentedIndexInput for file:%s in index: %s ", filename, fileKey.getIndexName()));
        }
    }

    @Override
    public byte readByte() throws IOException {
        if (bufferPosition >= currentBufferSize) {
            nextSegment();
            bufferPosition = 0;
        }
        return buffer[bufferPosition++];
    }

    @Override
    public void readBytes(byte[] b, int offset, int len) throws IOException {
        if (buffer == null) {
            nextSegment();
        }
        while (len > 0) {
            int bytesToCopy = Math.min(currentBufferSize - bufferPosition, len);
            System.arraycopy(buffer, bufferPosition, b, offset, bytesToCopy);
            offset += bytesToCopy;
            len -= bytesToCopy;
            bufferPosition += bytesToCopy;
            if (bufferPosition >= currentBufferSize && len > 0) {
                nextSegment();
                bufferPosition = 0;
            }
        }
    }

    @Override
    public void close() throws IOException {
        currentBufferSize = 0;
        bufferPosition = 0;
        currentLoadedSegment = -1;
        buffer = null;
        if (isClone) {
            return;
        }
        readLocks.deleteOrReleaseReadLock(filename);
        if (log.isDebugEnabled()) {
            log.debug(String.format("Closed IndexInput for file:%s in index: %s ", filename, fileKey.getIndexName()));
        }
    }

    @Override
    public long getFilePointer() {
        return ((long) currentLoadedSegment) * segmentSize + bufferPosition;
    }

    @Override
    public void seek(long pos) throws IOException {
        bufferPosition = (int) (pos % segmentSize);
        int targetSegment = (int) (pos / segmentSize);
        if (targetSegment != currentLoadedSegment) {
            currentLoadedSegment = targetSegment;
            setBufferToCurrentChunkIfPossible();
        }
    }

    @Override
    public long length() {
        return this.fileLength;
    }

    private void nextSegment() throws IOException {
        currentLoadedSegment++;
        setBufferToCurrentSegment();
    }

    private void setBufferToCurrentSegment() throws IOException {
        SegmentKey key = new SegmentKey(fileKey.getIndexName(), filename, currentLoadedSegment);
        buffer = (byte[]) client.getValue(key.toStore());
        if (buffer == null) {
            throw new IOException("Read past EOF: Chunk value could not be found for key " + key);
        }
        currentBufferSize = buffer.length;
    }

    private void setBufferToCurrentChunkIfPossible() throws IOException {
        SegmentKey key = new SegmentKey(fileKey.getIndexName(), filename, currentLoadedSegment);
        buffer = (byte[]) client.getValue(key.toStore());
        if (buffer == null) {
            currentLoadedSegment--;
            bufferPosition = segmentSize;
        } else {
            currentBufferSize = buffer.length;
        }
    }

    @Override
    public Object clone() {
        VoldemortSegmentedIndexInput clone = (VoldemortSegmentedIndexInput) super.clone();
        clone.isClone = true;
        return clone;
    }
}
