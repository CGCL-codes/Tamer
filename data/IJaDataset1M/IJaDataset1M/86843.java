package com.google.gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.google.gson.JsonParseException;
import com.google.gson.Primitives;

/**
 * Handles type conversion from some object to some primitive (or primitive
 * wrapper instance).
 * 
 * @author Joel Leitch
 */
final class PrimitiveTypeAdapter {

    @SuppressWarnings("unchecked")
    public <T> T adaptType(Object from, Class<T> to) {
        Class<?> aClass = Primitives.wrap(to);
        if (Primitives.isWrapperType(aClass)) {
            if (aClass == Character.class) {
                String value = from.toString();
                if (value.length() == 1) {
                    return (T) (Character) from.toString().charAt(0);
                } else {
                    throw new JsonParseException("The value: " + value + " contains more than a character.");
                }
            }
            try {
                Constructor<?> constructor = aClass.getConstructor(String.class);
                return (T) constructor.newInstance(from.toString());
            } catch (NoSuchMethodException e) {
                throw new JsonParseException(e);
            } catch (IllegalAccessException e) {
                throw new JsonParseException(e);
            } catch (InvocationTargetException e) {
                throw new JsonParseException(e);
            } catch (InstantiationException e) {
                throw new JsonParseException(e);
            }
        } else if (Enum.class.isAssignableFrom(to)) {
            try {
                Method valuesMethod = to.getMethod("valueOf", String.class);
                return (T) valuesMethod.invoke(null, from.toString());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new JsonParseException("Can not adapt type " + from.getClass() + " to " + to);
        }
    }
}
