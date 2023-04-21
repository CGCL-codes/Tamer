package br.gov.component.demoiselle.common.pojo.extension;

import java.lang.reflect.Field;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Support class to be used by PojoExtensionAspect and PojoExtension in order to
 * provide customized equals(), hashCode(), toString(), and toLog() methods on
 * the POJO entity.
 * 
 * @author CETEC/CTJEE
 * 
 * @see IPojoExtension
 * @see java.lang.Object#equals(java.lang.Object)
 * @see java.lang.Object#hashCode()
 * @see java.lang.Object#toString()
 */
public class PojoExtensionManager {

    private Object that;

    /**
	 * Class constructor.
	 * 
	 * @param that	the reference to an object
	 * @param superObject	the reference to the object's parent
	 */
    public PojoExtensionManager(Object that) {
        this.that = that;
    }

    /**
	 * Returns true whether the specified object is considered equals to the
	 * current reference.
	 * 
	 * @param obj	the object to compare with
	 * @param superResult	the result of super.equals() method
	 * @return a boolean
	 */
    public boolean executeEquals(Object obj, boolean superResult) {
        if (that == obj) return true;
        if (obj == null || that.getClass() != obj.getClass()) return false;
        EqualsBuilder builder = new EqualsBuilder();
        boolean hasAnnotation = false;
        for (Field field : that.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EqualsField.class)) {
                hasAnnotation = true;
                try {
                    field.setAccessible(true);
                    builder.append(field.get(that), field.get(obj));
                } catch (SecurityException e) {
                    throw new PojoExtensionException("Could not access field " + field.getName() + " in class " + obj.getClass().getSimpleName(), e);
                } catch (Exception e) {
                    throw new PojoExtensionException("Error checking object equality to another reference", e);
                }
            }
        }
        if (hasAnnotation) return builder.isEquals(); else {
            return superResult;
        }
    }

    /**
	 * Returns a hash code for the given object resulting of its key attributes.
	 * 
	 * @param superResult	the result of super.hashCode() method
	 * @return an integer
	 */
    public int executeHashCode(int superResult) {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean hasAnnotation = false;
        for (Field field : that.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EqualsField.class)) {
                hasAnnotation = true;
                try {
                    field.setAccessible(true);
                    builder.append(field.get(that));
                } catch (Exception e) {
                    throw new PojoExtensionException("Error calculating object hash code", e);
                }
            }
        }
        if (hasAnnotation) return builder.toHashCode(); else return superResult;
    }

    /**
	 * Returns the object string representation for debugging purposes.
	 * 
	 * @param superResult	the result of super.toString() method
	 * @return	a string
	 */
    public String executeToString(String superResult) {
        ToStringBuilder builder = new ToStringBuilder(that, ToStringStyle.SHORT_PREFIX_STYLE);
        boolean hasAnnotation = false;
        for (Field field : that.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Description.class)) {
                for (OutputType type : ((Description) field.getAnnotation(Description.class)).methods()) {
                    if (type == OutputType.TO_STRING) {
                        hasAnnotation = true;
                        try {
                            field.setAccessible(true);
                            builder.append(field.getName(), field.get(that));
                        } catch (Exception e) {
                            builder.append("null");
                        }
                    }
                }
            }
        }
        if (hasAnnotation) return builder.toString(); else return superResult;
    }

    /**
	 * Returns the object string representation for logging purposes.
	 * 
	 * @param superResult	the result of super.toLog() method
	 * @return	a string
	 */
    public String executeToLog(String superResult) {
        ToStringBuilder builder = new ToStringBuilder(that, ToLogStyle.LOG_STYLE);
        boolean hasAnnotation = false;
        for (Field field : that.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Description.class)) {
                for (OutputType type : ((Description) field.getAnnotation(Description.class)).methods()) {
                    if (type == OutputType.TO_LOG) {
                        hasAnnotation = true;
                        try {
                            field.setAccessible(true);
                            builder.append(field.getName(), field.get(that));
                        } catch (Exception e) {
                            builder.append("null");
                        }
                    }
                }
            }
        }
        if (hasAnnotation) return builder.toString(); else return superResult;
    }
}
