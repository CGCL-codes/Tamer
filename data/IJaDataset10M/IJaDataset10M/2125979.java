package net.woodstock.rockapi.domain.pojo.converter.text.impl;

import net.woodstock.rockapi.domain.pojo.converter.text.TextNumber;
import net.woodstock.rockapi.util.FieldInfo;
import net.woodstock.rockapi.utils.NumberUtils;
import net.woodstock.rockapi.utils.StringUtils;

class LongConverter extends TextAttributeConverterBase<Long> {

    public Long fromText(String text, FieldInfo fieldInfo) {
        try {
            Long l = null;
            if (!StringUtils.isEmpty(text)) {
                if (fieldInfo.isAnnotationPresent(TextNumber.class)) {
                    String format = fieldInfo.getAnnotation(TextNumber.class).pattern();
                    l = new Long(NumberUtils.parse(TextConverterBase.trim(text), format).longValue());
                } else {
                    l = new Long(TextConverterBase.trim(text));
                }
            }
            return l;
        } catch (Exception e) {
            throw new TextConverterException(e);
        }
    }

    public String toText(Long l, FieldInfo fieldInfo) {
        try {
            String s = "";
            if (l != null) {
                if (fieldInfo.isAnnotationPresent(TextNumber.class)) {
                    String format = fieldInfo.getAnnotation(TextNumber.class).pattern();
                    s = NumberUtils.format(l.longValue(), format);
                } else {
                    s = l.toString();
                }
            }
            return TextConverterBase.ldap(s, this.getSize(fieldInfo));
        } catch (Exception e) {
            throw new TextConverterException(e);
        }
    }
}
