package com.taliasplayground.convert;

import java.nio.charset.Charset;
import org.testng.annotations.Test;
import com.taliasplayground.convert.CannotConvertException;
import com.taliasplayground.convert.CharsetConverter;
import com.taliasplayground.lang.Assert;

/**
 * @author David M. Sledge
 *
 */
public class CharsetConverterTests {

    @Test
    public void testConvert() throws CannotConvertException {
        CharsetConverter converter = new CharsetConverter();
        Object charset = converter.convert("UTF-8", Charset.class);
        Assert.notNullArg(charset);
        Assert.notNullArg(converter.convert(charset, String.class));
    }
}
