package net.woodstock.rockapi.domain.pojo.converter.text.impl;

import net.woodstock.rockapi.domain.Pojo;
import net.woodstock.rockapi.domain.message.DomainMessage;
import net.woodstock.rockapi.domain.pojo.converter.text.TextCollection;
import net.woodstock.rockapi.domain.pojo.converter.text.TextField;
import net.woodstock.rockapi.domain.pojo.converter.text.TextIgnore;
import net.woodstock.rockapi.util.BeanInfo;
import net.woodstock.rockapi.util.FieldInfo;

class PojoConverter extends TextAttributeConverterBase<Pojo> {

    @SuppressWarnings("unchecked")
    public Pojo fromText(String text, FieldInfo fieldInfo) {
        try {
            Pojo pojo = (Pojo) fieldInfo.getFieldType().newInstance();
            BeanInfo beanInfo = BeanInfo.getBeanInfo(pojo.getClass());
            for (FieldInfo f : beanInfo.getFieldsInfo()) {
                if (f.isAnnotationPresent(TextIgnore.class)) {
                    continue;
                }
                TextField textField = f.getAnnotation(TextField.class);
                int size = textField.size();
                String s = text.substring(0, size);
                text = text.substring(size);
                TextAttributeConverter converter = TextConverterBase.getAttributeConverter(f.getFieldType());
                Object value = converter.fromText(s, f);
                f.setFieldValue(pojo, value);
            }
            return pojo;
        } catch (Exception e) {
            throw new TextConverterException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Pojo fromText(Class<? extends Pojo> clazz, String text) {
        try {
            Pojo pojo = clazz.newInstance();
            BeanInfo beanInfo = BeanInfo.getBeanInfo(pojo.getClass());
            for (FieldInfo fieldInfo : beanInfo.getFieldsInfo()) {
                if (fieldInfo.isAnnotationPresent(TextIgnore.class)) {
                    continue;
                }
                int size = -1;
                if (fieldInfo.isAnnotationPresent(TextField.class)) {
                    TextField textField = fieldInfo.getAnnotation(TextField.class);
                    size = textField.size();
                }
                if ((size == -1) && (!fieldInfo.isAnnotationPresent(TextCollection.class))) {
                    throw new TextConverterException(DomainMessage.getMessage(TextAttributeConverterBase.MESSAGE_ERROR_CONVERTER_SIZE, fieldInfo.getFieldName()));
                }
                String s = "";
                if (size != -1) {
                    s = text.substring(0, size);
                    text = text.substring(size);
                } else {
                    s = text.substring(0);
                    text = null;
                }
                TextAttributeConverter converter = TextConverterBase.getAttributeConverter(fieldInfo.getFieldType());
                Object value = converter.fromText(s, fieldInfo);
                fieldInfo.setFieldValue(pojo, value);
            }
            return pojo;
        } catch (Exception e) {
            throw new TextConverterException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public String toText(Pojo p) {
        try {
            StringBuilder builder = new StringBuilder();
            BeanInfo beanInfo = BeanInfo.getBeanInfo(p.getClass());
            for (FieldInfo fieldInfo : beanInfo.getFieldsInfo()) {
                if (fieldInfo.isAnnotationPresent(TextIgnore.class)) {
                    continue;
                }
                Object value = fieldInfo.getFieldValue(p);
                TextAttributeConverter attributeConverter = TextConverterBase.getNullAttributeConverter();
                if (value != null) {
                    attributeConverter = TextConverterBase.getAttributeConverter(value.getClass());
                }
                String s = attributeConverter.toText(value, fieldInfo);
                builder.append(s);
            }
            return builder.toString();
        } catch (Exception e) {
            throw new TextConverterException(e);
        }
    }

    public String toText(Pojo p, FieldInfo fieldInfo) {
        try {
            return TextConverterBase.ldap(this.toText(p), this.getSize(fieldInfo));
        } catch (Exception e) {
            throw new TextConverterException(e);
        }
    }
}
