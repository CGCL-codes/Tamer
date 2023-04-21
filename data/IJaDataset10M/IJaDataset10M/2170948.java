package fi.arcusys.acj.model.udm;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * An implementation of {@link Preference} for storing
 * <code>Boolean</code> values.
 * 
 * @author mikko
 * @version 1.0 $Rev$
 */
@Entity
@DiscriminatorValue("BOOL")
public class BooleanValue extends PreferenceValue {

    private static final long serialVersionUID = 1L;

    private Boolean boolValue;

    public BooleanValue() {
        super();
    }

    public BooleanValue(Boolean value) {
        this.boolValue = value;
    }

    @Override
    @Transient
    public Object getValueObject() {
        return getBooleanValue();
    }

    @Override
    @Transient
    public void setValueObject(Object value) {
        if (null == value) {
            setBooleanValue(null);
        } else if (value instanceof Boolean) {
            setBooleanValue((Boolean) value);
        } else if (value instanceof Number) {
            setBooleanValue(Boolean.valueOf(((Number) value).intValue() != 0));
        } else if (value instanceof Character) {
            char ch = ((Character) value).charValue();
            ch = Character.toLowerCase(ch);
            setBooleanValue(ch == 'y' || ch == 't');
        } else {
            String s = value.toString();
            if (null == s) {
                setBooleanValue(null);
            } else {
                s = s.trim();
                if (0 == s.length()) {
                    setBooleanValue(null);
                } else {
                    setBooleanValue(Boolean.valueOf(s));
                }
            }
        }
    }

    /**
	 * The current value.
	 * @return the current value
	 */
    public Boolean getBooleanValue() {
        return boolValue;
    }

    /**
	 * Set the current value. 
	 * @param value the new value
	 */
    public void setBooleanValue(Boolean value) {
        this.boolValue = value;
    }
}
