package org.webguitoolkit.ui.controls.util.conversion;

import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverterPrecise;

/**
 * <pre>
 * Converter for positive numbers (Double)
 * </pre>
 */
public class PositiveNumberConverterPrecise extends NumberConverterPrecise {

    public static IConverter pn0 = new PositiveNumberConverterPrecise("###0");

    public static IConverter pn1 = new PositiveNumberConverterPrecise("###0.0");

    public static IConverter pn2 = new PositiveNumberConverterPrecise("###0.00");

    public PositiveNumberConverterPrecise(String pattern) {
        super(pattern);
    }

    public Object parse(String textRep) throws ConversionException {
        Double d = (Double) super.parse(textRep);
        if (d.doubleValue() < 0) {
            throw new ConversionException(TextService.getString("converter.PositiveNumberConverter.message@The entered Number must be positive."));
        }
        return d;
    }
}
