package com.feature50.clarity.table;

import org.apache.log4j.Logger;
import java.text.Format;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import com.feature50.util.ReflectionUtils;
import com.feature50.util.ArrayUtils;
import com.feature50.clarity.ReflectionException;

/**
 * Used by {@link ClarityTableModel} to describe a column in a table.
 *
 */
public class ColumnDescriptor {

    private static final Logger logger = Logger.getLogger(ColumnDescriptor.class);

    private String name;

    private ColumnValueGetter getter;

    private ColumnValueSetter setter;

    private Class clazz;

    private ValuePath path;

    public ColumnDescriptor(ValuePath path) {
        this.name = ReflectionUtils.getFormattedNameForField(path.getField());
        this.path = path;
        getter = new ValueGetterSetter(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnValueGetter getGetter() {
        return getter;
    }

    public ColumnValueSetter getSetter() {
        return setter;
    }

    public void setGetter(ColumnValueGetter getter) {
        this.getter = getter;
    }

    public void setSetter(final ColumnValueSetter setter) {
        this.setter = setter;
    }

    public static class ValueGetterSetter implements ColumnValueGetter, ColumnValueSetter {

        private ValuePath path;

        public ValueGetterSetter(ValuePath path) {
            this.path = path;
        }

        public Object getValue(Object object) {
            try {
                Object fieldInstance = path.getFieldInstance(object);
                if (path.getField().equals(".")) {
                    return fieldInstance;
                } else {
                    return ReflectionUtils.invokeGetter(fieldInstance, path.getField());
                }
            } catch (Exception e) {
                logger.error(String.format("Couldn't retrieve value described by '%1$s' from the instance of type '%2$s'", path.getDescription(), (object == null) ? "null" : object.getClass()), e);
                return "";
            }
        }

        public void setValue(Object object, Object value) throws ReflectionException {
            try {
                Object fieldInstance = object;
                if (!ArrayUtils.isNullOrEmpty(path.getPaths())) {
                    for (int i = 0; i < path.getPaths().length; i++) {
                        String p = path.getPaths()[i];
                        Object indexedProperty = ReflectionUtils.invokeGetter(fieldInstance, p);
                        fieldInstance = ArrayUtils.getIndex(indexedProperty, path.getIndices()[i]);
                    }
                }
                ReflectionUtils.invokeSetter(fieldInstance, path.getField(), value);
            } catch (Exception e) {
                String type = (value == null) ? "null" : value.getClass().toString();
                logger.error(String.format("Couldn't set value described by '%1$s' of using an instance of type '%2$s' on the instance of type '%3$s'", path.getDescription(), type, object.getClass()), e);
                throw new ReflectionException(e);
            }
        }
    }

    public Class getColumnClass() {
        return clazz;
    }

    public void setColumnClass(Class clazz) {
        this.clazz = clazz;
    }

    public ValuePath getPath() {
        return path;
    }
}
