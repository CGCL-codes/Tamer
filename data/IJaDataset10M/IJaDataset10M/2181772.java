package awilkins.objectmodel.properties.impl.objectmodel;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import awilkins.objectmodel.properties.IllegalPropertyAccessException;

/**
 * FieldProperty
 *
 * @author Antony
 */
public class FieldProperty extends MemberProperty {

    private Field field;

    /**
   *
   */
    public FieldProperty(Field member) {
        super();
        if (!isBeanField(member)) {
            throw new IllegalArgumentException("not a bean field");
        }
        field = member;
    }

    public Field getField() {
        return field;
    }

    public Member getMember() {
        return getField();
    }

    public Class getType() {
        return getField().getType();
    }

    /**
   * @see awilkins.objectmodel.properties.Property#getValue(java.lang.Object)
   */
    public Object getValue(Object object) throws IllegalPropertyAccessException {
        if (!isReadable()) {
            throw new IllegalStateException("Not readable");
        }
        try {
            getField().setAccessible(true);
            return getField().get(object);
        } catch (IllegalArgumentException e) {
            throw new IllegalPropertyAccessException("while accessing " + this, e);
        } catch (IllegalAccessException e) {
            throw new IllegalPropertyAccessException("while accessing " + this, e);
        }
    }

    /**
   * @see awilkins.objectmodel.properties.Property#isReadable()
   */
    public boolean isReadable() {
        return true;
    }

    /**
   * @see awilkins.objectmodel.properties.Property#isWritable()
   */
    public boolean isWritable() {
        return !Modifier.isFinal(getField().getModifiers());
    }

    /**
   * @see awilkins.objectmodel.properties.Property#setValue(java.lang.Object, java.lang.Object)
   */
    public void setValue(Object object, Object value) throws IllegalPropertyAccessException {
        if (!isWritable()) {
            throw new IllegalStateException("Not writable");
        }
        try {
            getField().setAccessible(true);
            getField().set(object, value);
        } catch (IllegalArgumentException e) {
            throw new IllegalPropertyAccessException("while accessing " + this, e);
        } catch (IllegalAccessException e) {
            throw new IllegalPropertyAccessException("while accessing " + this, e);
        }
    }

    /**
   * @param m
   * @return
   */
    public static boolean isBeanField(Field m) {
        return !Modifier.isStatic(m.getModifiers());
    }
}
