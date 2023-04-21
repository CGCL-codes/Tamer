package com.thoughtworks.yav.validator;

import com.thoughtworks.yav.ValidationContext;
import com.thoughtworks.yav.annotation.Validatable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author Warren Oliver
 */
public class NestedCollectionValidator extends AbstractAnnotationValidator<Annotation, Collection> implements FieldValidator {

    public NestedCollectionValidator(ValidationContext context) {
        super(context);
    }

    protected void validate(String fieldName, Annotation annotation, Collection target) {
        int index = 0;
        for (Object object : target) {
            getContext().validate(object, fieldName + "[" + index + "]");
            index++;
        }
    }

    protected boolean appliesTo(Class<? extends Object> targetClass, Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }
}
