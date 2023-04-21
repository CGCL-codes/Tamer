package org.webthree.dictionary.integrity;

import java.util.Locale;
import org.webthree.dictionary.Domain;
import org.webthree.dictionary.expression.ExpressionContext;
import org.webthree.dictionary.integrity.ConversionResult;
import org.webthree.dictionary.integrity.Convertor;

/**
 * @author michael.gerzabek@gmx.net
 * 
 */
public class LongConvertor implements Convertor {

    public String convertToString(Object value, Domain domain, ExpressionContext context, Locale locale) {
        return null;
    }

    public ConversionResult convertFromString(String value, Domain domain, Locale locale) {
        return null;
    }

    public Class getName() {
        return null;
    }
}
