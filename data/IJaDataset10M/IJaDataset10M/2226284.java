package org.xblackcat.rojac.service.options.converter;

/**
 * @author xBlackCat
 */
public class ByteConverter extends AScalarConverter<Byte> {

    public Byte convert(String s) {
        try {
            if (s != null) {
                return Byte.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
