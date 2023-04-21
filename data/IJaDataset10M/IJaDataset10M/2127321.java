package foucault.gui;

import java.lang.reflect.Field;
import foucault.utils.Callback;

public class ReflectionAlignIntModel extends AlignComponent.Model {

    private final Object object;

    private Field field;

    private final Callback callback;

    public ReflectionAlignIntModel(Object object, String fieldName, Callback callback) {
        this.object = object;
        try {
            this.field = object.getClass().getField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.callback = callback;
    }

    @Override
    public double get() {
        try {
            return ((Integer) field.get(object));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(double v) {
        try {
            field.set(object, (int) v);
            callback.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
