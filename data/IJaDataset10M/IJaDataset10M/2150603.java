package org.vramework.aspectj;

import java.lang.reflect.Field;
import org.aspectj.lang.reflect.FieldSignature;
import org.vramework.commons.beanutils.VSpector;

/**
 * @author thomas.mahringer
 * 
 */
public class AspectJHelper {

    /**
   * Uses {@link VSpector} to retrieve the field, this means: <strong>Note:
   * </strong>Uses reflection to retrieve the field;
   * 
   * @param signature -
   *          The AspectJ signature.
   * @return The field belonging to the signature.
   */
    public static Field getField(FieldSignature signature) {
        Class<?> type = signature.getDeclaringType();
        Field field = VSpector.getDeclaredField(type, signature.getName(), true);
        return field;
    }

    /**
   * @param signature -
   *          The AspectJ signature.
   * @return The name of the field defined by the signature.
   */
    public static String getFieldName(FieldSignature signature) {
        return signature.getName();
    }
}
