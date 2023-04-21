package fecimpl;

import java.util.logging.Logger;

/**
 * This class encapsulates the logic used to
 * select parameters for encoding/decoding
 * and file segmentation.
 **/
class SegmentationParams {

    private static Logger logger = Logger.getLogger(SegmentationParams.class.getName());

    public final int baseN;

    public final int baseK;

    public final int endN;

    public final int endK;

    public final int baseBlockSize;

    public final int endBlockSize;

    public final int baseStripeWidth;

    public final int endStripeWidth;

    public final int baseSegmentSize;

    public final int endSegmentSize;

    public final int segments;

    SegmentationParams(int baseN_, int baseK_, int endN_, int endK_, int baseBlockSize_, int endBlockSize_, int baseStripeWidth_, int endStripeWidth_, int baseSegmentSize_, int endSegmentSize_, int segments_) {
        this.baseN = baseN_;
        this.baseK = baseK_;
        this.endN = endN_;
        this.endK = endK_;
        this.baseBlockSize = baseBlockSize_;
        this.endBlockSize = endBlockSize_;
        this.baseStripeWidth = baseStripeWidth_;
        this.endStripeWidth = endStripeWidth_;
        this.baseSegmentSize = baseSegmentSize_;
        this.endSegmentSize = endSegmentSize_;
        this.segments = segments_;
    }

    SegmentationParams(long len, int redundancyNum, int redundancyDenom) {
        if ((len < 1) || (redundancyNum < 1) || (redundancyDenom < 1)) {
            throw new IllegalArgumentException("len=" + len + " redundancyNum=" + redundancyNum + " redundancyDenom=" + redundancyDenom);
        }
        int segCount = (int) (len / C_SEGLEN);
        if ((len % C_SEGLEN) != 0) {
            segCount++;
        }
        segments = segCount;
        long segLen = len;
        if (len > C_SEGLEN) {
            segLen = C_SEGLEN;
        }
        if (segLen < C_MIN_SEGLEN) {
            segLen = C_MIN_SEGLEN;
        }
        baseSegmentSize = (int) segLen;
        baseBlockSize = selectBlockSize(baseSegmentSize);
        baseK = calculateK(baseSegmentSize, baseBlockSize);
        baseN = calculateN(baseK, redundancyNum, redundancyDenom);
        baseStripeWidth = selectStripeWidth(baseSegmentSize, baseBlockSize);
        if (segments > 1) {
            segLen = len % baseSegmentSize;
            if (segLen == 0) {
                endSegmentSize = baseSegmentSize;
                endBlockSize = baseBlockSize;
                endK = baseK;
                endN = baseN;
                endStripeWidth = baseStripeWidth;
            } else {
                if (segLen > C_SEGLEN) {
                    throw new RuntimeException("assertion failure: segLen <= " + C_SEGLEN);
                }
                if (segLen < C_MIN_SEGLEN) {
                    segLen = C_MIN_SEGLEN;
                }
                endSegmentSize = (int) segLen;
                endBlockSize = selectBlockSize(endSegmentSize);
                endK = calculateK(endSegmentSize, endBlockSize);
                endN = calculateN(endK, redundancyNum, redundancyDenom);
                endStripeWidth = selectStripeWidth(endSegmentSize, endBlockSize);
            }
        } else {
            endSegmentSize = baseSegmentSize;
            endBlockSize = baseBlockSize;
            endK = baseK;
            endN = baseN;
            endStripeWidth = baseStripeWidth;
        }
    }

    public final void dump() {
        logger.fine("----------------------------------------\n" + "baseN: " + baseN + "\n" + "baseK: " + baseK + "\n" + "endN: " + endN + "\n" + "endK: " + endK + "\n" + "baseBlockSize: " + baseBlockSize + "\n" + "endBlockSize: " + endBlockSize + "\n" + "baseStripeWidth: " + baseStripeWidth + "\n" + "endStripeWidth: " + endStripeWidth + "\n" + "baseSegmentSize: " + baseSegmentSize + "\n" + "endSegmentSize: " + endSegmentSize + "\n" + "segments: " + segments + "\n" + "----------------------------------------");
    }

    private static final int selectBlockSize(long segLen) {
        int blockSize;
        if (segLen < C_1M) {
            blockSize = C_128K;
        } else if (segLen < C_32M) {
            blockSize = C_256K;
        } else if (segLen < C_64M) {
            blockSize = C_512K;
        } else {
            blockSize = C_1M;
        }
        return blockSize;
    }

    private static final int calculateK(long segLen, int blockSize) {
        int k = 0;
        if (segLen < C_SEGLEN) {
            k = (int) (segLen / blockSize);
            if ((segLen % blockSize) != 0) {
                k++;
            }
        } else {
            k = (int) (C_SEGLEN / blockSize);
        }
        return k;
    }

    private static final int calculateN(int k, int redundancyNum, int redundancyDenom) {
        if (k > 128) {
            throw new IllegalArgumentException("k > 128!");
        }
        if (((double) redundancyNum) / redundancyDenom > 0.5) {
            throw new IllegalArgumentException("Maximum allowed redundancy is 50%.");
        }
        int n = k + ((k * redundancyNum) / redundancyDenom);
        if (n == k) {
            n++;
        }
        return n;
    }

    private static final int selectStripeWidth(int segLen, int blockSize) {
        int stripeWidth = -1;
        if (segLen > C_16M) {
            int stripeCount = 1;
            while (segLen / stripeCount > C_16M) {
                stripeCount = stripeCount << 1;
            }
            stripeWidth = blockSize / stripeCount;
            if (blockSize % stripeCount != 0) {
                throw new RuntimeException("assertion failure: blockSize % stripeCount == 0");
            }
        }
        return stripeWidth;
    }

    private static final int C_128K = 128 * 1024;

    private static final int C_256K = 256 * 1024;

    private static final int C_512K = 512 * 1024;

    private static final int C_1M = 1024 * 1024;

    private static final int C_16M = 16 * 1024 * 1024;

    private static final int C_32M = 32 * 1024 * 1024;

    private static final int C_64M = 64 * 1024 * 1024;

    private static final int C_128M = 128 * 1024 * 1024;

    private static final long C_SEGLEN = C_128M;

    private static final int C_MIN_SEGLEN = 6 * C_128K;
}
