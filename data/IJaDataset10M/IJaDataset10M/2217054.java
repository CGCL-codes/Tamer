package com.w20e.socrates.rendering;

import java.util.Locale;
import java.util.Map;
import com.w20e.socrates.data.TypeChecker;
import com.w20e.socrates.expression.Undef;
import com.w20e.socrates.expression.XObject;

public class Select extends SelectionControl {

    /**
	 * Serialization id.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Construct select type.
	 */
    public Select(String id) {
        super(id);
        setType("select");
    }

    /**
	 * Allow multiple choices?
	 */
    private boolean multiple = false;

    /**
	 * Set the appearance to full.
	 */
    public void setFullAppearance() {
        setProperty("appearance", "FULL");
    }

    /**
	 * Set the appearance to minimal.
	 */
    public void setMinimalAppearance() {
        setProperty("appearance", "MINIMAL");
    }

    /**
	 * Set the appearance to compact;
	 */
    public void setCompactAppearance() {
        setProperty("appearance", "COMPACT");
    }

    /**
	 * Does the select allow multiple choice?
	 * 
	 * @return multiple or no.
	 */
    public boolean isMultiple() {
        return this.multiple;
    }

    /**
	 * Set multiple choice.
	 * 
	 * @param multiple
	 */
    public void setMultiple(final boolean newMultiple) {
        this.multiple = newMultiple;
    }

    public XObject processInput(Map<String, Object> data, Class type) {
        Object val = data.get(getId());
        if (val == null) {
            return Undef.UNDEF;
        }
        if (isMultiple()) {
            return null;
        }
        return TypeChecker.evaluate(type, val);
    }

    /**
	 * Return vocabulary value.
	 */
    public Object getDisplayValue(XObject value, Locale locale) {
        if (value == null || value instanceof Undef) {
            return "";
        }
        try {
            return getOptionLabel(value.toString());
        } catch (Exception e) {
            return "";
        }
    }
}
