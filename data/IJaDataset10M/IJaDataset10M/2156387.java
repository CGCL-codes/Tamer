package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link TelParsedResult}.
 *
 * @author Sean Owen
 */
public final class TelParsedResultTestCase extends Assert {

    @Test
    public void testTel() {
        doTest("tel:+15551212", "+15551212", null);
        doTest("tel:2125551212", "2125551212", null);
    }

    private static void doTest(String contents, String number, String title) {
        Result fakeResult = new Result(contents, null, null, BarcodeFormat.QR_CODE);
        ParsedResult result = ResultParser.parseResult(fakeResult);
        assertSame(ParsedResultType.TEL, result.getType());
        TelParsedResult telResult = (TelParsedResult) result;
        assertEquals(number, telResult.getNumber());
        assertEquals(title, telResult.getTitle());
        assertEquals("tel:" + number, telResult.getTelURI());
    }
}
