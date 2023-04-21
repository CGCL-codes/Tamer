package de.ui.sushi.metadata.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import de.ui.sushi.metadata.Cardinality;
import de.ui.sushi.metadata.Item;
import de.ui.sushi.metadata.Schema;
import de.ui.sushi.metadata.Type;

public class OptionItem<T> extends Item<T> {

    public static <T> OptionItem<T> create(Schema metadata, Field field) {
        String name;
        Class<?> type;
        Class<?> fieldType;
        name = field.getName();
        type = field.getDeclaringClass();
        fieldType = field.getType();
        return new OptionItem<T>(name, fieldType, metadata.type(fieldType), lookup(type, "get" + name), lookup(type, "set" + name), field);
    }

    private final Method getter;

    private final Method setter;

    public OptionItem(String name, Class<?> typeRaw, Type type, Method getter, Method setter, AnnotatedElement definition) {
        super(name, Cardinality.OPTION, type, definition);
        if (typeRaw.isPrimitive()) {
            throw new IllegalArgumentException("primitive type is not allowed for options: " + typeRaw);
        }
        checkSetter(typeRaw, setter);
        checkGetter(typeRaw, getter);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public Collection<T> get(Object src) {
        T result;
        result = (T) invoke(getter, src);
        return result == null ? new ArrayList<T>() : Collections.singletonList(result);
    }

    @Override
    public void set(Object dest, Collection<T> values) {
        Object value;
        switch(values.size()) {
            case 0:
                value = null;
                break;
            case 1:
                value = values.iterator().next();
                break;
            default:
                throw new IllegalArgumentException();
        }
        invoke(setter, dest, value);
    }
}
