package de.carne.fs.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * <code>FileScannerInput</code> implementation used to access data backuped by the decode cache.
 */
class DecodedFileScannerInput extends NestedFileScannerInput {

    private final DecodeCache.MapEntry cacheEntry;

    /**
	 * Construct <code>DecodedFileScannerInput</code>.
	 * 
	 * @param parent The <code>FileScannerInput</code> providing the encoded data.
	 * @param name The name of the <code>DecodedFileScannerInput</code>.
	 * @param cacheEntry The cache entry to access the decoded data.
	 * @param e A possible I/O exception that occurred during decoding.
	 */
    public DecodedFileScannerInput(FileScannerInput parent, String name, DecodeCache.MapEntry cacheEntry, IOException e) {
        super(parent, name);
        this.cacheEntry = cacheEntry;
        recordIOError(e);
    }

    @Override
    public long size() {
        return this.cacheEntry.size();
    }

    @Override
    public int readAt(long position, ByteBuffer buffer) throws IOException {
        return this.cacheEntry.readAt(position, buffer);
    }

    @Override
    public long transferTo(long position, long count, WritableByteChannel target) throws IOException {
        return this.cacheEntry.transferTo(position, count, target);
    }
}
