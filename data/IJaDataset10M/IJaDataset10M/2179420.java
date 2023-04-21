package org.gm.jrgen;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.sf.jasperreports.engine.JRRuntimeException;
import org.gm.jrgen.metadata.ClassMetadata;
import org.gm.jrgen.metadata.FieldMetadata;

public class ReflectionHelper {

    /**
	 * Return true if the clazz is a collection
	 * 
	 * @param clazz
	 * @return
	 */
    public static boolean isCollection(Class<?> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    /**
	 * Return true if the clazz is a collection, a map or a set
	 * 
	 * @param clazz
	 * @return
	 */
    public static boolean isCollectionMapSet(Class<?> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Set.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    /**
	 * Return true if the clazz is a collection
	 * 
	 * @param clazz
	 * @return
	 */
    public static boolean isMap(Class<?> clazz) {
        if (Map.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    /**
	 * Return true if the clazz is a primitive type
	 * 
	 * @param clazz
	 * @return
	 */
    public static boolean isJRType(Class<?> clazz) {
        if (Double.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (String.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Float.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Long.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Short.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Integer.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    public static boolean isCollectionOfCollection(FieldMetadata ischemaFieldToRender) throws JRRuntimeException {
        ClassMetadata embType = ischemaFieldToRender.getEmbeddedType();
        if (embType == null) {
            throw new JRRuntimeException("Cannot find embedded type definition for the field " + ischemaFieldToRender.getName());
        }
        if (embType.isAssignableAs(Collection.class)) {
            return true;
        }
        return false;
    }
}
