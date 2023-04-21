package mmf.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import mmf.api.IModel;
import mmf.api.IPrimaryKey;
import mmf.api.IPrimaryKeyGetter;

/**
 * PrimaryKey per Reflection extrahieren
 * 
 * @author Frank R�ber
 * @version $Revision$ $Date$
 */
public class ReflectionPrimaryKeyGetter<TModel extends IModel, TKey extends IPrimaryKey> implements IPrimaryKeyGetter<TModel, TKey> {

    private Field pkField;

    private Method pkGetter;

    public ReflectionPrimaryKeyGetter(Field pkField) {
        this.pkField = pkField;
        this.pkField.setAccessible(true);
    }

    public ReflectionPrimaryKeyGetter(Method pkGetter) {
        this.pkGetter = pkGetter;
        this.pkGetter.setAccessible(true);
    }

    public TKey getPrimaryKey(TModel target) {
        try {
            if (pkGetter != null) return (TKey) pkGetter.invoke(target);
            if (pkField != null) return (TKey) pkField.get(target);
            throw new AssertionError(toString() + ": Neither field nor getter found.");
        } catch (IllegalArgumentException exc) {
            throw new RuntimeException(target.getClass().getName(), exc);
        } catch (IllegalAccessException exc) {
            throw new RuntimeException(target.getClass().getName(), exc);
        } catch (InvocationTargetException exc) {
            throw new RuntimeException(target.getClass().getName(), exc);
        }
    }
}
