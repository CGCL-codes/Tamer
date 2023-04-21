package shiva.domain.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author Paulo Vitor
 * @author Roberto Su
 * 
 * @description
 *
 */
@SuppressWarnings("unchecked")
public class AttributeMapping {

    private Field attribute;

    private String attributeNameBound;

    private Collection<Annotation> validations;

    private boolean dn;

    private String dnType;

    private String dnValue;

    /**
	 * 
	 * @return the attribute
	 */
    public Field getAttribute() {
        return attribute;
    }

    /**
	 * 
	 * @param attribute the attribute to set
	 */
    public void setAttribute(Field attribute) {
        this.attribute = attribute;
    }

    /**
	 * 
	 * @return the attributeNameBound
	 */
    public String getAttributeNameBound() {
        return attributeNameBound;
    }

    /**
	 * 
	 * @param attributeNameBound the attributeNameBound to set
	 */
    public void setAttributeNameBound(String attributeNameBound) {
        this.attributeNameBound = attributeNameBound;
    }

    /**
	 * 
	 * @return the validations
	 */
    public Collection<Annotation> getValidations() {
        return validations;
    }

    /**
	 * 
	 * @param validations the validations to set
	 */
    public void setValidations(Collection<Annotation> validations) {
        this.validations = validations;
    }

    /**
	 * @return the dn
	 */
    public boolean isDn() {
        return this.dn;
    }

    /**
	 * @return the dnType
	 */
    public String getDnType() {
        return this.dnType;
    }

    /**
	 * @param dnType the dnType to set
	 */
    public void setDnType(String dnType) {
        this.dnType = dnType;
    }

    /**
	 * @return the dnValue
	 */
    public String getDnValue() {
        return this.dnValue;
    }

    /**
	 * @param dnValue the dnValue to set
	 */
    public void setDnValue(String dnValue) {
        this.dnValue = dnValue;
    }

    /**
	 * @param dn the dn to set
	 */
    public void setDn(boolean dn) {
        this.dn = dn;
    }

    /**
	 * 
	 * @return
	 */
    public boolean isValidated() {
        return this.validations != null && this.validations.size() > 0;
    }

    /**
	 * 
	 * @param clazz
	 * @return
	 */
    public boolean isValidatedBy(Class clazz) {
        if (this.validations != null && this.validations.size() > 0) {
            for (Annotation annotation : this.validations) {
                if (annotation.getClass().equals(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * 
	 * @param clazz
	 * @return
	 */
    public Annotation getValidationBy(Class clazz) {
        if (this.validations != null && this.validations.size() > 0) {
            for (Annotation annotation : this.validations) {
                if (annotation.getClass().equals(clazz)) {
                    return annotation;
                }
            }
        }
        return null;
    }

    /**
	 * 
	 * 
	 */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\tattribute: " + (attribute != null ? attribute.getName() : "NULL") + "\n");
        buffer.append("\tattributeNameBound: " + attributeNameBound + "\n");
        buffer.append("\tvalidations: \n");
        if (this.validations != null) {
            for (Annotation validation : this.validations) {
                buffer.append("\t\t-" + validation.annotationType().getCanonicalName() + " \n");
            }
        } else {
            buffer.append("\t\t-NONE");
        }
        return buffer.toString();
    }
}
