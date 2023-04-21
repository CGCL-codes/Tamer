package com.berd.core.web.converter;

import ognl.DefaultTypeConverter;
import java.util.Map;
import com.berd.core.utils.CurrencyConverterUtils;

public class CurrencyConverter extends DefaultTypeConverter {

    private Object format(String source) {
        if (source.lastIndexOf(".") == 0) return ("0" + source);
        return source;
    }

    public Object convertValue(Map ognlContext, Object value, Class toType) {
        CurrencyConverterUtils cc = new CurrencyConverterUtils();
        Object result = null;
        if (toType == Float.class) {
            result = cc.convert(Float.class, ((Object[]) value)[0], true);
        } else if (toType == Double.class) {
            result = cc.convert(Double.class, ((Object[]) value)[0], true);
        } else if (toType == String.class) {
            if ("0.0".equals(value.toString())) result = "0.00"; else result = format(cc.convert(String.class, Double.parseDouble(value.toString()), true).toString());
        }
        return result;
    }
}
