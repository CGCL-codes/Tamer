package com.google.zxing.negative;

import com.google.zxing.common.AbstractNegativeBlackBoxTestCase;

/**
 * This test ensures that random images with high contrast patterns do not decode as barcodes.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class FalsePositivesBlackBoxTestCase extends AbstractNegativeBlackBoxTestCase {

    public FalsePositivesBlackBoxTestCase() {
        super("test/data/blackbox/falsepositives");
        addTest(2, 0.0f);
        addTest(2, 90.0f);
        addTest(2, 180.0f);
        addTest(2, 270.0f);
    }
}
