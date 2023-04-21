package com.moandjiezana.mewf.validation;

import java.util.Collections;
import java.util.Iterator;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

public class MewfConstraintViolation implements ConstraintViolation<Object> {

    private final String messageTemplate;

    private final String message;

    private final Class<?> validatedClass;

    private final String property;

    public MewfConstraintViolation(String property, Class<?> validatedClass, ValidationMessages validationMessages) {
        this.property = property;
        this.messageTemplate = validationMessages.getMissingMessage();
        this.message = messageTemplate;
        this.validatedClass = validatedClass;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessageTemplate() {
        return messageTemplate;
    }

    @Override
    public Object getRootBean() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Object> getRootBeanClass() {
        return (Class<Object>) validatedClass;
    }

    @Override
    public Object getLeafBean() {
        return null;
    }

    @Override
    public Path getPropertyPath() {
        return new Path() {

            @Override
            public Iterator<Node> iterator() {
                return Collections.<Node>singletonList(new Node() {

                    @Override
                    public String getName() {
                        return property;
                    }

                    @Override
                    public boolean isInIterable() {
                        return false;
                    }

                    @Override
                    public Integer getIndex() {
                        return null;
                    }

                    @Override
                    public Object getKey() {
                        return null;
                    }
                }).iterator();
            }
        };
    }

    @Override
    public Object getInvalidValue() {
        return null;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return null;
    }
}
