package org.granite.generator;

import java.lang.reflect.Field;

public class GField {

    private final Field field;

    private final boolean readable;

    private final boolean writable;

    public GField(Field field, boolean readable, boolean writable) {
        this.field = field;
        this.readable = readable;
        this.writable = writable;
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return field.getName();
    }

    public String getType() {
        return field.getType().getSimpleName();
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }
}
