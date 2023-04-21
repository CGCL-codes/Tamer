package org.unitils.io.conversion.impl;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertEquals;

/**
 * @author Jeroen Horemans
 * @author Tim Ducheyne
 * @author Thomas De Rycke
 * @since 3.3
 */
public class StringConversionStrategyTest {

    private StringConversionStrategy conversion = new StringConversionStrategy();

    private String input = "€é*ù¨´ù]:~e;[=+";

    @Test
    public void validEncoding() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes("utf-8"));
        String result = conversion.convertContent(inputStream, "utf-8");
        assertEquals(input, result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void invalidEncoding() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        conversion.convertContent(inputStream, "xxxx");
    }
}
