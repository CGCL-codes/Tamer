package org.spantus.extractor;

import org.spantus.core.FrameValues;

public class FrameValuesTestUtils {

    public static FrameValues convert(Integer[] intArr) {
        FrameValues rtn = new FrameValues();
        rtn.setSampleRate(1D);
        for (Integer i1 : intArr) {
            rtn.add(i1.doubleValue());
        }
        return rtn;
    }

    public static FrameValues convertInt(Integer... intArr) {
        FrameValues rtn = new FrameValues();
        for (Integer i1 : intArr) {
            rtn.add(i1.doubleValue());
        }
        return rtn;
    }

    public static FrameValues generateFrameValues(int size) {
        FrameValues rtn = new FrameValues();
        for (int i = 0; i < size; i++) {
            rtn.add((double) i);
        }
        return rtn;
    }

    public static ExtractorConfig createExtractorConfig() {
        ExtractorConfig config = new ExtractorConfig();
        config.setSampleRate(8000D);
        return config;
    }
}
