package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.common.AbstractBlackBoxTestCase;

/**
 * @author Sean Owen
 */
public final class CodabarBlackBox1TestCase extends AbstractBlackBoxTestCase {

    public CodabarBlackBox1TestCase() {
        super("test/data/blackbox/codabar-1", new MultiFormatReader(), BarcodeFormat.CODABAR);
        addTest(11, 11, 0.0f);
        addTest(11, 11, 180.0f);
    }
}
