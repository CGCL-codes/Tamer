package edu.cmu.ece.agora.core;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;

public final class ByteGatheringAsyncQueue extends AbstractAsyncQueue<ByteGatheringAsyncQueue.ByteArrayPart, byte[]> {

    public static class ByteArrayPart {

        public final byte[] ba;

        public final int off;

        public final int len;

        public ByteArrayPart(byte[] ba, int off, int len) {
            this.ba = ba;
            this.off = off;
            this.len = len;
        }
    }

    private final ByteArrayOutputStream baos;

    public ByteGatheringAsyncQueue(Executor exec) {
        super(exec);
        this.baos = new ByteArrayOutputStream();
    }

    @Override
    protected void offerNoBlock(ByteArrayPart item) {
        baos.write(item.ba, item.off, item.len);
    }

    @Override
    protected byte[] pollNoBlock() {
        if (baos.size() == 0) return null;
        byte[] ret = baos.toByteArray();
        baos.reset();
        return ret;
    }
}
