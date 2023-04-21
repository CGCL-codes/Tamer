package net.java.nioserver.utils;

import java.nio.ByteBuffer;

/**
 * @author Leonid Shlyapnikov
 */
public class BlockingByteBufferPool extends BlockingPool<ByteBuffer> {

    public BlockingByteBufferPool(int poolCapacity, int bufferCapacity, boolean direct, boolean debug) {
        super(poolCapacity, createResourceFactory(bufferCapacity, direct), debug);
    }

    private static ResourceFactory<ByteBuffer> createResourceFactory(final int bufferCapacity, final boolean direct) {
        return new ResourceFactory<ByteBuffer>() {

            public ByteBuffer create() {
                return (direct ? ByteBuffer.allocateDirect(bufferCapacity) : ByteBuffer.allocate(bufferCapacity));
            }
        };
    }
}
