package net.woodstock.rockapi.pojo.converter.text.internal;

import net.woodstock.rockapi.pojo.converter.ConverterException;
import net.woodstock.rockapi.pojo.converter.text.TextNumber;
import net.woodstock.rockapi.util.FieldInfo;
import net.woodstock.rockapi.utils.NumberUtils;
import net.woodstock.rockapi.utils.StringUtils;

class ByteConverter extends TextAttributeConverterBase<Byte> {

    public Byte fromText(String text, FieldInfo fieldInfo) {
        try {
            Byte b = null;
            if (!StringUtils.isEmpty(text)) {
                if (fieldInfo.isAnnotationPresent(TextNumber.class)) {
                    String format = fieldInfo.getAnnotation(TextNumber.class).pattern();
                    b = new Byte(NumberUtils.parse(TextConverterBase.trim(text), format).byteValue());
                } else {
                    b = new Byte(TextConverterBase.trim(text));
                }
            }
            return b;
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }

    public String toText(Byte b, FieldInfo fieldInfo) {
        try {
            String s = "";
            if (b != null) {
                if (fieldInfo.isAnnotationPresent(TextNumber.class)) {
                    String format = fieldInfo.getAnnotation(TextNumber.class).pattern();
                    s = NumberUtils.format(b.byteValue(), format);
                } else {
                    s = b.toString();
                }
            }
            return TextConverterBase.ldap(s, this.getSize(fieldInfo));
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }
}
