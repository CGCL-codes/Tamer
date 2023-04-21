package org.jia.ptrack.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EntityIdAndClass {

    private String typeName;

    private int entityId;

    EntityIdAndClass() {
    }

    public EntityIdAndClass(Object entity) {
        this.typeName = entity.getClass().getName();
        this.entityId = getId(entity);
    }

    private int getId(Object entity) {
        try {
            Method m = entity.getClass().getMethod("getId", new Class[0]);
            return (Integer) m.invoke(entity, new Object[0]);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return entityId;
    }

    public String getTypeName() {
        return typeName;
    }
}
