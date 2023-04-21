package com.android.monkeyrunner.adb;

import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import junit.framework.TestCase;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Unit Tests for AdbMonkeyDevice.
 */
public class AdbMonkeyDeviceTest extends TestCase {

    private static String MULTILINE_RESULT = "\r\n" + "Test results for InstrumentationTestRunner=.\r\n" + "Time: 2.242\r\n" + "\r\n" + "OK (1 test)";

    private static String getResource(String resName) throws IOException {
        URL resource = Resources.getResource(AdbMonkeyDeviceTest.class, resName);
        List<String> lines = Resources.readLines(resource, Charset.defaultCharset());
        return Joiner.on("\r\n").join(lines);
    }

    public void testSimpleResultParse() throws IOException {
        String result = getResource("instrument_result.txt");
        Map<String, Object> convertedResult = AdbMonkeyDevice.convertInstrumentResult(result);
        assertEquals("one", convertedResult.get("result1"));
        assertEquals("two", convertedResult.get("result2"));
    }

    public void testMultilineResultParse() throws IOException {
        String result = getResource("multiline_instrument_result.txt");
        Map<String, Object> convertedResult = AdbMonkeyDevice.convertInstrumentResult(result);
        assertEquals(MULTILINE_RESULT, convertedResult.get("stream"));
    }
}
