package com.google.zxing.oned.rss.expanded.decoders;

import org.junit.Test;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
public class AI01_3X0X_1X_DecoderTest extends AbstractDecoderTest {

    private static final String header_310x_11 = "..XXX...";

    private static final String header_320x_11 = "..XXX..X";

    private static final String header_310x_13 = "..XXX.X.";

    private static final String header_320x_13 = "..XXX.XX";

    private static final String header_310x_15 = "..XXXX..";

    private static final String header_320x_15 = "..XXXX.X";

    private static final String header_310x_17 = "..XXXXX.";

    private static final String header_320x_17 = "..XXXXXX";

    @Test
    public void test01_310X_1X_endDate() throws Exception {
        String data = header_310x_11 + compressedGtin_900123456798908 + compressed20bitWeight_1750 + compressedDate_End;
        String expected = "(01)90012345678908(3100)001750";
        assertCorrectBinaryString(data, expected);
    }

    @Test
    public void test01_310X_11_1() throws Exception {
        String data = header_310x_11 + compressedGtin_900123456798908 + compressed20bitWeight_1750 + compressedDate_March_12th_2010;
        String expected = "(01)90012345678908(3100)001750(11)100312";
        assertCorrectBinaryString(data, expected);
    }

    @Test
    public void test01_320X_11_1() throws Exception {
        String data = header_320x_11 + compressedGtin_900123456798908 + compressed20bitWeight_1750 + compressedDate_March_12th_2010;
        String expected = "(01)90012345678908(3200)001750(11)100312";
        assertCorrectBinaryString(data, expected);
    }

    @Test
    public void test01_310X_13_1() throws Exception {
        String data = header_310x_13 + compressedGtin_900123456798908 + compressed20bitWeight_1750 + compressedDate_March_12th_2010;
        String expected = "(01)90012345678908(3100)001750(13)100312";
        assertCorrectBinaryString(data, expected);
    }

    @Test
    public void test01_320X_13_1() throws Exception {
        String data = header_320x_13 + compressedGtin_900123456798908 + compressed20bitWeight_1750 + compressedDate_March_12th_2010;
        String expected = "(01)90012345678908(3200)001750(13)100312";
        assertCorrectBinaryString(data, expected);
    }

    @Test
    public void test01_310X_15_1() throws Exception {
        String data = header_310x_15 + compressedGtin_900123456798908 + compressed20bitWeight_1750 + compressedDate_March_12th_2010;
        String expected = "(01)90012345678908(3100)001750(15)100312";
        assertCorrectBinaryString(data, expected);
    }

    @Test
    public void test01_320X_15_1() throws Exception {
        String data = header_320x_15 + compressedGtin_900123456798908 + compressed20bitWeight_1750 + compressedDate_March_12th_2010;
        String expected = "(01)90012345678908(3200)001750(15)100312";
        assertCorrectBinaryString(data, expected);
    }

    @Test
    public void test01_310X_17_1() throws Exception {
        String data = header_310x_17 + compressedGtin_900123456798908 + compressed20bitWeight_1750 + compressedDate_March_12th_2010;
        String expected = "(01)90012345678908(3100)001750(17)100312";
        assertCorrectBinaryString(data, expected);
    }

    @Test
    public void test01_320X_17_1() throws Exception {
        String data = header_320x_17 + compressedGtin_900123456798908 + compressed20bitWeight_1750 + compressedDate_March_12th_2010;
        String expected = "(01)90012345678908(3200)001750(17)100312";
        assertCorrectBinaryString(data, expected);
    }
}
