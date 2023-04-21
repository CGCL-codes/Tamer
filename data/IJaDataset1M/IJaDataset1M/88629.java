package org.webguitoolkit.ui.controls.util.conversion;

import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverter;

/**
 * <pre>
 * Converter for positive numbers (Float)
 * </pre>
 */
public class PositiveNumberConverter extends NumberConverter {

    public static IConverter pn0 = new PositiveNumberConverter("###0");

    public static IConverter pn1 = new PositiveNumberConverter("###0.0");

    public static IConverter pn2 = new PositiveNumberConverter("###0.00");

    public PositiveNumberConverter(String pattern) {
        super(pattern);
    }

    public Object parse(String textRep) throws ConversionException {
        Float f = (Float) super.parse(textRep);
        if (f.floatValue() < 0) {
            throw new ConversionException(TextService.getString("converter.PositiveNumberConverter.message@The entered Number must be positive."));
        }
        return f;
    }
}
