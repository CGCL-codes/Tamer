package de.ui.sushi.metadata.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import de.ui.sushi.fs.IO;
import de.ui.sushi.metadata.ComplexType;
import de.ui.sushi.metadata.Item;
import de.ui.sushi.metadata.Schema;
import de.ui.sushi.metadata.simpletypes.NodeType;

public class ReflectSchema extends Schema {

    public ReflectSchema() {
    }

    public ReflectSchema(IO io) {
        this();
        add(new NodeType(this, io));
    }

    @Override
    public void complex(ComplexType type) {
        Class<?> fieldType;
        Item<?> item;
        for (Field field : type.getType().getDeclaredFields()) {
            fieldType = field.getType();
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                item = null;
            } else if (field.isSynthetic()) {
                item = null;
            } else if (fieldType.isArray()) {
                item = new ArrayItem<Object>(field, type(fieldType.getComponentType()));
            } else if (Collection.class.isAssignableFrom(fieldType)) {
                item = new CollectionItem(field, type(Object.class));
            } else {
                item = new ValueItem<Object>(field, type(fieldType));
            }
            if (item != null) {
                type.items().add(item);
            }
        }
    }
}
