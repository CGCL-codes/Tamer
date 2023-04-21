package net.jadoth.util.bytes;

public interface BinaryProtocol {

    public int getHeaderSize();

    public void addLastSegmentHeader(byte[] chunk);

    public void addPartialSegmentHeader(byte[] chunk);

    public boolean isCommit(byte[] chunk);

    public boolean isAbort(byte[] chunk);

    public class Implementation implements BinaryProtocol {

        static final int HEADER_LENGTH = 1;

        static final byte HEADER_BIT_ENDIAN = 1 << 0;

        static final byte HEADER_BIT_MORE = 1 << 1;

        static final byte HEADER_BIT_ABORT = 1 << 2;

        static final byte HEADER_BIT_EXTEND = (byte) (1 << 7);

        static final boolean IS_BIG_ENDIAN = java.nio.ByteOrder.nativeOrder() == java.nio.ByteOrder.BIG_ENDIAN;

        static final byte HEADER_BASE = IS_BIG_ENDIAN ? HEADER_BIT_ENDIAN : 0;

        static final byte HEADER_LAST = (byte) (HEADER_BASE | 0);

        static final byte HEADER_MORE = (byte) (HEADER_BASE | HEADER_BIT_MORE);

        private static void internalAddLastSegmentHeader(final byte[] chunk) {
            chunk[0] = HEADER_LAST;
        }

        private static void internalAddBeforeLastSegmentHeader(final byte[] chunk) {
            chunk[0] = HEADER_MORE;
        }

        @Override
        public int getHeaderSize() {
            return HEADER_LENGTH;
        }

        @Override
        public void addLastSegmentHeader(final byte[] chunk) {
            internalAddLastSegmentHeader(chunk);
        }

        @Override
        public void addPartialSegmentHeader(final byte[] chunk) {
            internalAddBeforeLastSegmentHeader(chunk);
        }

        @Override
        public boolean isCommit(final byte[] chunk) {
            return (chunk[0] & HEADER_BIT_MORE) == 0;
        }

        @Override
        public boolean isAbort(final byte[] chunk) {
            return (chunk[0] & HEADER_BIT_ABORT) != 0;
        }
    }
}
