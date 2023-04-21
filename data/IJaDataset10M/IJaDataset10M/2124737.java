package xml.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import xml.ElementList;

/**
 * The <code>ElementListLabel</code> object is used to create a converter
 * that can be used.
 * 
 * 
 * @author Niall Gallagher
 */
public class ElementListLabel implements Label {

    private ElementList label;

    private Field field;

    private Class type;

    private Class item;

    public ElementListLabel(Field field, ElementList type) {
        this.type = field.getClass();
        this.item = type.type();
        this.field = field;
        this.label = label;
    }

    public Converter getConverter(Source root) throws Exception {
        return new CompositeList(root, type, item);
    }

    public Annotation getAnnotation() {
        return label;
    }

    public Class getType() {
        return type;
    }

    public Field getField() {
        return field;
    }
}
