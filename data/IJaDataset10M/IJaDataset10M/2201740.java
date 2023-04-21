package org.mariella.rcp.databinding;

import java.text.Format;
import java.text.ParseException;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;

public class FormatConverterBuilder extends ConverterBuilder {

    Format format;

    public FormatConverterBuilder(Format format) {
        this.format = format;
    }

    @Override
    public IConverter buildFromModelConverter(VBindingDomain domain) {
        return new Converter(domain.getType(), String.class) {

            public Object convert(Object fromObject) {
                if (fromObject == null) {
                    return "";
                } else {
                    return format.format(fromObject);
                }
            }
        };
    }

    @Override
    public IConverter buildToModelConverter(VBindingDomain domain) {
        return new Converter(String.class, domain.getType()) {

            public Object convert(Object fromObject) {
                String string = (String) fromObject;
                if (string == null || string.length() == 0) {
                    return null;
                } else {
                    try {
                        return format.parseObject((String) fromObject);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }
}
