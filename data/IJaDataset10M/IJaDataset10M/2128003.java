package de.dfki.lt.signalproc.util;

/**
 * @author Marc Schr&ouml;der
 *
 */
public class BlockwiseDoubleDataSource extends BufferedDoubleDataSource {

    protected int blockSize;

    /**
     * @param inputSource
     */
    public BlockwiseDoubleDataSource(DoubleDataSource inputSource, int blockSize) {
        super(inputSource);
        this.blockSize = blockSize;
    }

    /**
     * Attempt to get more data from the input source. If less than this can be read,
     * the possible amount will be read, but canReadMore() will return false afterwards.
     * @param minLength the amount of data to get from the input source
     * @return true if the requested amount could be read, false if none or less data could be read.
     */
    protected boolean readIntoBuffer(int minLength) {
        if (bufferSpaceLeft() < minLength) {
            increaseBufferSize(minLength + currentlyInBuffer());
        } else if (buf.length - writePos < minLength) {
            compact();
        }
        int readSum = 0;
        while (readSum < minLength && hasMoreData()) {
            prepareBlock();
            int blockSize = getBlockSize();
            if (buf.length < writePos + blockSize) {
                increaseBufferSize(writePos + blockSize);
            }
            int read = readBlock(buf, writePos);
            writePos += read;
            readSum += read;
        }
        if (dataProcessor != null) {
            dataProcessor.applyInline(buf, writePos - readSum, readSum);
        }
        return readSum >= minLength;
    }

    /**
     * Provide the size of the next block. This implementation returns the fixed
     * blocksize given in the constructor.
     * Subclasses may want to override this method.
     *
     */
    protected int getBlockSize() {
        return blockSize;
    }

    /**
     * Prepare a block of data for output. This method is called before readBlock() is called.
     * This implementation does nothing.
     * Subclasses will want to override this method.
     *
     */
    protected void prepareBlock() {
    }

    /**
     * Read a block of data. This method is called after prepareBlock() is called. 
     * This implementation simply reads getBlockSize()
     * data from the inputSource given in the constructor.
     * Subclasses will want to override this method.
     * @param target
     * @param pos
     * @return number of values written into target from position pos
     */
    protected int readBlock(double[] target, int pos) {
        return inputSource.getData(target, pos, getBlockSize());
    }
}
