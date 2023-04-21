package com.beam.validator.propertyValidator;

import java.util.Date;
import com.beam.util.ObjectUtil;
import com.beam.util.Reflect;
import com.beam.validator.ValidType;

/**
 * 
 * �����������ж�С�ڵ���
 * 
 * @author beam
 * 2010-12-24  ����03:02:35
 */
public class LessEquals extends Rule {

    private Object other;

    public LessEquals(String propertyName, ValidType type, String errorMessage) {
        super(propertyName, type, errorMessage);
    }

    public LessEquals(String propertyName, ValidType type, Object other, String errorMessage) {
        super(propertyName, type, errorMessage);
        this.other = other;
    }

    @Override
    public boolean validate(Object obj) throws ValidateException {
        Object value;
        try {
            value = Reflect.getValueByFieldName(obj, name);
        } catch (Exception e) {
            throw new ValidateException();
        }
        if (ObjectUtil.isEmpty(value)) return true;
        switch(type) {
            case NUMBER:
                return number(value);
            case DATE:
                return date(value);
        }
        return false;
    }

    private boolean number(Object value) {
        return ((Number) other).doubleValue() >= ((Number) value).doubleValue();
    }

    private boolean date(Object value) {
        return ((Date) other).compareTo((Date) value) >= 0;
    }
}
