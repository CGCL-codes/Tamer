package net.entropysoft.transmorph.converters;

import java.net.URL;
import net.entropysoft.transmorph.ConversionContext;
import net.entropysoft.transmorph.ConverterException;
import net.entropysoft.transmorph.type.TypeReference;

/**
 * Converter used when destination is a String. It uses toString() method on the
 * source object
 * 
 * @author Cedric Chabanois (cchabanois at gmail.com)
 * 
 */
public class ObjectToString extends AbstractConverter {

    public static final Class<?>[] ALL_SOURCE_CLASSES = new Class[] { Object.class };

    private Class<?>[] handledSourceClasses = ALL_SOURCE_CLASSES;

    private boolean failIfDefaultObjectToString = true;

    public ObjectToString() {
        this.useObjectPool = false;
    }

    public Class<?>[] getHandledSourceClasses() {
        return handledSourceClasses;
    }

    public void setHandledSourceClasses(Class<?>[] handledSourceClasses) {
        this.handledSourceClasses = handledSourceClasses;
    }

    public void setFailIfDefaultObjectToString(boolean failIfDefaultObjectToString) {
        this.failIfDefaultObjectToString = failIfDefaultObjectToString;
    }

    public boolean isFailIfDefaultObjectToString() {
        return failIfDefaultObjectToString;
    }

    public Object doConvert(ConversionContext context, Object sourceObject, TypeReference<?> destinationType) throws ConverterException {
        if (sourceObject == null) {
            return null;
        }
        String result = sourceObject.toString();
        if (failIfDefaultObjectToString && isDefaultObjectToString(sourceObject, result)) {
            throw new ConverterException("Cannot convert to string : toString() method has not been overridden");
        }
        return result;
    }

    protected boolean isDefaultObjectToString(Object sourceObject, String str) {
        if (sourceObject instanceof URL) {
            return false;
        }
        return str.equals(getDefaultObjectToString(sourceObject));
    }

    protected boolean canHandleDestinationType(TypeReference<?> destinationType) {
        return destinationType.isType(String.class);
    }

    protected boolean canHandleSourceObject(Object sourceObject) {
        if (sourceObject == null) {
            return true;
        }
        for (Class<?> handledClass : handledSourceClasses) {
            if (handledClass.isAssignableFrom(sourceObject.getClass())) {
                return true;
            }
        }
        return false;
    }

    private String getDefaultObjectToString(Object object) {
        return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
    }
}
