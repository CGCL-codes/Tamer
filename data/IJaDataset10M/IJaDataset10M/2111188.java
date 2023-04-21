package net.sf.lightbound.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sf.lightbound.Request;
import net.sf.lightbound.annotations.BeanProperty;
import net.sf.lightbound.annotations.BeanPropertyDefinitions;
import net.sf.lightbound.annotations.StorageScope;
import net.sf.lightbound.events.Event;
import net.sf.lightbound.exceptions.BeanInterfaceException;
import net.sf.lightbound.util.LightBoundUtil;

public class BeanProperties {

    private final String propertyName;

    private final BeanProperty annotation;

    private Field field;

    private Method readMethod;

    private boolean readUsesRequest;

    private Method writeMethod;

    private boolean writeUsesRequest;

    public BeanProperties(Field field, Method readMethod, boolean readUsesRequest, Method writeMethod, boolean writeUsesRequest, String propertyName, BeanProperty annotation) {
        this.field = field;
        if (this.field != null) {
            this.field.setAccessible(true);
        }
        this.propertyName = propertyName;
        this.annotation = annotation;
        this.readMethod = readMethod;
        if (readMethod != null) {
            readMethod.setAccessible(true);
        }
        this.writeMethod = writeMethod;
        if (writeMethod != null) {
            writeMethod.setAccessible(true);
        }
        this.readUsesRequest = readUsesRequest;
        this.writeUsesRequest = writeUsesRequest;
    }

    public BeanProperty getBeanPropertyAnnotation() {
        return annotation;
    }

    public String getExternalName() {
        if (annotation == null) {
            return null;
        }
        String externalName = annotation.id();
        if (BeanPropertyDefinitions.UNDEFINED_VALUE.equals(externalName)) {
            return null;
        }
        return externalName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public StorageScope getStorageScope() {
        if (annotation == null) {
            return null;
        }
        return annotation.storageScope();
    }

    public boolean isDirectFieldAccess() {
        if (annotation == null) {
            return false;
        }
        return annotation.directFieldAccess();
    }

    public Class<?> getSetValueClass() {
        if (getField() != null) {
            return getField().getType();
        }
        if (getSetter() != null) {
            return getSetter().getParameterTypes()[0];
        }
        return null;
    }

    public void setValue(Object target, Object value, Request request) throws BeanInterfaceException {
        try {
            if (writeMethod != null) {
                if (writeUsesRequest) {
                    writeMethod.invoke(target, value, request);
                } else {
                    writeMethod.invoke(target, value);
                }
            } else if (field != null) {
                field.set(target, value);
            } else {
                throw new BeanInterfaceException("can't find a way to write the " + "property value for " + target);
            }
        } catch (IllegalArgumentException e) {
            throw new BeanInterfaceException(getModificationErrorMessage(e, value), e);
        } catch (IllegalAccessException e) {
            throw new BeanInterfaceException(getModificationErrorMessage(e, value), e);
        } catch (InvocationTargetException e) {
            Event.throwCauseIfEvent(e);
            throw new BeanInterfaceException(getModificationErrorMessage(e.getCause(), value), e.getCause());
        }
    }

    public Object getValue(Object target, Request request) throws BeanInterfaceException {
        try {
            if (readMethod == null) {
                if (field == null) {
                    return null;
                }
                return field.get(target);
            }
            if (readUsesRequest) {
                return readMethod.invoke(target, request);
            }
            return readMethod.invoke(target);
        } catch (IllegalArgumentException e) {
            throw new BeanInterfaceException(getReadErrorMessage(e), e);
        } catch (IllegalAccessException e) {
            throw new BeanInterfaceException(getReadErrorMessage(e), e);
        } catch (InvocationTargetException e) {
            Event.throwCauseIfEvent(e);
            throw new BeanInterfaceException(getReadErrorMessage(e.getCause()), e.getCause());
        }
    }

    public Method getGetter() {
        return readMethod;
    }

    public Method getSetter() {
        return writeMethod;
    }

    public Field getField() {
        return field;
    }

    public Object getFieldValue(Object target) throws BeanInterfaceException {
        if (field == null) {
            return null;
        }
        try {
            return field.get(target);
        } catch (IllegalArgumentException e) {
            throw new BeanInterfaceException(getReadErrorMessage(e), e);
        } catch (IllegalAccessException e) {
            throw new BeanInterfaceException(getReadErrorMessage(e), e);
        }
    }

    public void setFieldValue(Object target, Object value) throws BeanInterfaceException {
        if (field == null) {
            return;
        }
        try {
            field.set(target, value);
        } catch (IllegalArgumentException e) {
            throw new BeanInterfaceException(getModificationErrorMessage(e, value), e);
        } catch (IllegalAccessException e) {
            throw new BeanInterfaceException(getModificationErrorMessage(e, value), e);
        }
    }

    private String getModificationErrorMessage(Throwable cause, Object value) {
        return getErrorMessage(cause, "modification") + ": cannot be set to '" + value + "'";
    }

    private String getReadErrorMessage(Throwable cause) {
        return getErrorMessage(cause, "reading");
    }

    private String getErrorMessage(Throwable cause, String action) {
        return "value " + action + " exception: " + LightBoundUtil.getShortExceptionDescription(cause) + " for property " + propertyName;
    }

    public boolean isReadUsesRequest() {
        return readUsesRequest;
    }

    public void setReadUsesRequest(boolean readUsesRequest) {
        this.readUsesRequest = readUsesRequest;
    }

    public boolean isWriteUsesRequest() {
        return writeUsesRequest;
    }

    public void setWriteUsesRequest(boolean writeUsesRequest) {
        this.writeUsesRequest = writeUsesRequest;
    }

    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }
}
