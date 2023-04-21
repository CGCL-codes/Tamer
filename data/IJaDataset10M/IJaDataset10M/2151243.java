package org.databene.script;

import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.converter.ConverterWrapper;

/**
 * {@link Converter} can recognize and resolve script expressions in {@link String} values,
 * forwarding values of other Java type 'as is'.<br/><br/>
 * Created: 07.08.2011 08:27:27
 * @since 0.5.9
 * @author Volker Bergmann
 */
public class ScriptConverterForObjects extends ConverterWrapper<String, Object> implements Converter<Object, Object> {

    public ScriptConverterForObjects(Context context) {
        super(new ScriptConverterForStrings(context));
    }

    public Class<Object> getSourceType() {
        return Object.class;
    }

    public Class<Object> getTargetType() {
        return Object.class;
    }

    public Object convert(Object sourceValue) throws ConversionException {
        if (sourceValue instanceof String) return realConverter.convert((String) sourceValue); else return sourceValue;
    }
}
