package edu.ucdavis.genomics.metabolomics.util.statistics.replacement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import org.apache.log4j.Logger;
import edu.ucdavis.genomics.metabolomics.util.BasicObject;

/**
 * method needed to spawn new threads based on my bad design of the replaceable
 * algorithm the best would be to actually rewrite it
 * 
 * @author wohlgemuth
 */
public abstract class AbstractThreadableReplaceable extends BasicObject implements ThreadableReplaceable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public synchronized ZeroReplaceable spawn() {
        try {
            getLogger().trace("internal clone method whith mighty reflections...");
            getLogger().trace("create instance of: " + this.getClass().getName());
            ZeroReplaceable replace = this.getClass().newInstance();
            Class current = replace.getClass();
            Class my = this.getClass();
            boolean clazz = true;
            while (clazz) {
                replaceField(current, my, replace);
                current = current.getSuperclass();
                my = my.getSuperclass();
                if (current == Object.class) {
                    getLogger().trace("break...");
                    clazz = false;
                }
            }
            return replace;
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass());
            logger.error(e.getMessage(), e);
            throw new InternalError(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void replaceField(Class clazz, Class my, Object object) throws NoSuchFieldException, IllegalAccessException, InstantiationException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
        for (Field field : my.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(this);
            Field newField = clazz.getDeclaredField((field.getName()));
            if (Modifier.isStatic(newField.getModifiers()) == false) {
                getLogger().trace("setting value for: " + newField.getName());
                newField.setAccessible(true);
                if (value instanceof Cloneable) {
                    getLogger().trace("clonable object found, so we clone it..." + value.getClass());
                    Method method = value.getClass().getDeclaredMethod("clone");
                    method.setAccessible(true);
                    value = method.invoke(value, null);
                }
                if (value instanceof Collection) {
                    if (((Collection) value).isEmpty()) {
                        getLogger().trace("empty collection found, so we reinitialize it...");
                        value = value.getClass().newInstance();
                    }
                } else if (value instanceof Map) {
                    if (((Map) value).isEmpty()) {
                        getLogger().trace("empty map found, so we reinitialize it...");
                        value = value.getClass().newInstance();
                    }
                }
                newField.set(object, value);
            }
        }
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CloneNotSupportedException(e.getMessage());
        }
    }

    public Logger getLogger() {
        return Logger.getLogger("replace");
    }
}
