package com.curlap.orb.servlet;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * ExceptionContent
 */
public class ExceptionContent implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private final String exceptionName;

    private final String message;

    private final ExceptionContent cause;

    private final List<Object> exceptionFields;

    public ExceptionContent(Throwable throwable) throws InstanceManagementException {
        exceptionName = throwable.getClass().getName();
        message = throwable.getMessage();
        try {
            exceptionFields = getFieldValues(throwable);
        } catch (IllegalAccessException e) {
            throw new InstanceManagementException();
        }
        cause = (throwable.getCause() != null ? new ExceptionContent(throwable.getCause()) : null);
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public String getMessage() {
        return message;
    }

    public ExceptionContent getCause() {
        return cause;
    }

    public List<Object> getExceptionFields() {
        return exceptionFields;
    }

    private List<Object> getFieldValues(Object obj) throws IllegalAccessException {
        if (obj instanceof java.lang.reflect.InvocationTargetException) return null;
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        List<Object> fieldList = new ArrayList<Object>();
        for (Field field : fields) {
            int modifier = field.getModifiers();
            if (!Modifier.isTransient(modifier) && !Modifier.isStatic(modifier)) {
                field.setAccessible(true);
                fieldList.add(field.get(obj));
                field.setAccessible(false);
            }
        }
        return fieldList;
    }
}
