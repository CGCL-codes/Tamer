package org.snsmeet.zxing.qrcode.detector;

import org.snsmeet.zxing.ResultPoint;

/**
 * <p>Encapsulates a finder pattern, which are the three square patterns found in
 * the corners of QR Codes. It also encapsulates a count of similar finder patterns,
 * as a convenience to the finder's bookkeeping.</p>
 *
 * @author Sean Owen
 */
public final class FinderPattern extends ResultPoint {

    private final float estimatedModuleSize;

    private int count;

    FinderPattern(float posX, float posY, float estimatedModuleSize) {
        super(posX, posY);
        this.estimatedModuleSize = estimatedModuleSize;
        this.count = 1;
    }

    public float getEstimatedModuleSize() {
        return estimatedModuleSize;
    }

    int getCount() {
        return count;
    }

    void incrementCount() {
        this.count++;
    }

    /**
   * <p>Determines if this finder pattern "about equals" a finder pattern at the stated
   * position and size -- meaning, it is at nearly the same center with nearly the same size.</p>
   */
    boolean aboutEquals(float moduleSize, float i, float j) {
        if (Math.abs(i - getY()) <= moduleSize && Math.abs(j - getX()) <= moduleSize) {
            float moduleSizeDiff = Math.abs(moduleSize - estimatedModuleSize);
            return moduleSizeDiff <= 1.0f || moduleSizeDiff / estimatedModuleSize <= 1.0f;
        }
        return false;
    }
}
