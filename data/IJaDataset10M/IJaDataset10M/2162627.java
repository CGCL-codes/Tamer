package easyaccept.util.bean.convert;

import java.lang.reflect.Array;
import org.apache.commons.beanutils.Converter;
import easyaccept.util.bean.BeanHelperUtil;

/**
 * Faz a conver��o de Objetos em String. Retorna um trim() da string
 * 
 */
@SuppressWarnings("unchecked")
public class StringConverter implements Converter {

    private org.apache.commons.beanutils.converters.StringConverter stringConverter;

    public StringConverter() {
        super();
        stringConverter = new org.apache.commons.beanutils.converters.StringConverter();
    }

    public Object convert(Class type, Object value) {
        if ((value != null) && value.getClass().isArray()) {
            if (Array.getLength(value) == 0) {
                return null;
            } else {
                value = Array.get(value, 0);
            }
        }
        if ((value == null) || (value.toString().trim().length() <= 0)) {
            return ((String) null);
        }
        Converter converter = BeanHelperUtil.lookup(value.getClass());
        if (converter instanceof ConverterToString) {
            ConverterToString converterToString = (ConverterToString) converter;
            return converterToString.convertToString(value);
        }
        String resp = (String) stringConverter.convert(type, value);
        return resp.trim().replace("\r", "");
    }
}
