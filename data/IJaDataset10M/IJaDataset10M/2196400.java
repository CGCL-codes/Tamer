package org.xblackcat.rojac.service.options.converter;

/**
 * @author xBlackCat
 */
public class ShortConverter extends AScalarConverter<Short> {

    public Short convert(String s) {
        try {
            if (s != null) {
                return Short.decode(s);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
