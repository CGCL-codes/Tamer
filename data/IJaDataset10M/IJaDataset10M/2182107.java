package com.thoughtworks.yav.validator;

import com.thoughtworks.yav.annotation.Conditional;
import com.thoughtworks.yav.ValidationContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Warren Oliver
 */
public class ConditionalValidator implements FieldValidator {

    private FieldValidator fieldValidator;

    public ConditionalValidator(FieldValidator fieldValidator) {
        this.fieldValidator = fieldValidator;
    }

    public boolean appliesTo(Class<? extends Object> targetClass, Field field) {
        return true;
    }

    public void validate(Object target, Field field) {
        if (continueValidation(target, field)) {
            fieldValidator.validate(target, field);
        }
    }

    private boolean continueValidation(Object target, Field field) {
        if (!field.isAnnotationPresent(Conditional.class)) {
            return true;
        }
        Boolean result = Boolean.TRUE;
        Conditional conditional = field.getAnnotation(Conditional.class);
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (method.getName().equals(conditional.method())) {
                try {
                    result = (Boolean) method.invoke(target, null);
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
                return result;
            }
        }
        return result;
    }
}
