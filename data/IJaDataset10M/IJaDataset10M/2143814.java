package org.jcvi.trace.sanger.chromatogram.ztr.data;

import java.nio.ByteBuffer;

/**
 * <code>AbstractDeltaData</code> is an abstract
 * implementation of the Delta encoded Data formats.
 * The Delta formats store the differences between successive
 * bytes instead of the actual values.  Different implementations
 * of <code>AbstractDeltaData</code> are used for the different
 * sizes of the encoded values.
 * @author dkatzel
 *
 *
 */
public abstract class AbstractDeltaData implements Data {

    /**
     * 
    * {@inheritDoc}
     */
    @Override
    public byte[] parseData(byte[] data) {
        int level = data[1];
        int startPosition = 2 + getPaddingSize();
        ByteBuffer compressed = ByteBuffer.allocate(data.length - startPosition);
        compressed.put(data, startPosition, data.length - startPosition);
        compressed.flip();
        ByteBuffer unCompressedData = ByteBuffer.allocate(compressed.capacity());
        getDeltaStrategy(level).unCompress(compressed, unCompressedData);
        return unCompressedData.array();
    }

    /**
     * Retrieves which DeltaStrategy to use.
     * @param level
     * @return
     */
    protected abstract DeltaStrategy getDeltaStrategy(int level);

    /**
     * Some implementations may have additional
     * padding between the format byte and
     * when the actual data starts.  Usually this
     * is to needed to make the total length
     * of the data section divisible. Implementations
     * may override this method to return a different
     * padding size.
     * @return <code>0</code>
     */
    protected int getPaddingSize() {
        return 0;
    }
}
