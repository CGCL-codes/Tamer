package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.common.AbstractBlackBoxTestCase;

/**
 * @author Sean Owen
 */
public final class Code39BlackBox1TestCase extends AbstractBlackBoxTestCase {

    public Code39BlackBox1TestCase() {
        super("test/data/blackbox/code39-1", new MultiFormatReader(), BarcodeFormat.CODE_39);
        addTest(4, 4, 0.0f);
        addTest(4, 4, 180.0f);
    }
}
