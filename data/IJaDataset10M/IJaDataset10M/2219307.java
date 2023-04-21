package org.xblackcat.rojac.service.converter;

/**
 * Converter for replacing a RSDN tag or text part with its HTML analog.
 *
 * @author xBlackCat
 */
public interface IMessageParser {

    String convert(String rsdnMessage);
}
