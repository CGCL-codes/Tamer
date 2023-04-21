package com.g2inc.scap.library.domain.oval;

/**
 * Represents a variable restriction in an oval variable.
 */
public class VariableRestriction extends OvalVariableChild {

    public VariableRestriction(OvalDefinitionsDocument parentDocument) {
        super(parentDocument);
    }

    /**
	 * Get the value.
	 * 
	 * @return String
	 */
    public String getValue() {
        return element.getValue();
    }

    /**
	 * Set the value.
	 * 
	 * @param value
	 */
    public void setValue(String value) {
        element.setText(value);
    }

    /**
	 * Get the operation.
	 * 
	 * @return String
	 */
    public String getOperation() {
        String ret = "equals";
        String operation = element.getAttributeValue("operation");
        if (operation != null && operation.length() > 0) {
            ret = operation;
        }
        return ret;
    }

    /**
	 * Set the operation.
	 * 
	 * @param operation
	 */
    public void setOperation(String operation) {
        element.setAttribute("operation", operation);
    }

    @Override
    public String toString() {
        return "restriction: oper=" + getOperation() + ", value=" + getValue();
    }

    @Override
    public boolean equals(Object o) {
        boolean ret = false;
        if (o == null) {
            return ret;
        }
        if (!(o instanceof VariableRestriction)) {
            return ret;
        }
        VariableRestriction other = (VariableRestriction) o;
        if (getElementName() != null) {
            if (!getElementName().equals(other.getElementName())) {
                return ret;
            }
        }
        if (getOperation() != null) {
            if (!getOperation().equals(other.getOperation())) {
                return ret;
            }
        }
        if (getValue() != null) {
            if (!getValue().equals(other.getValue())) {
                return ret;
            }
        }
        ret = true;
        return ret;
    }

    /**
     * This method determines if this object is a duplicate of another object.
     * 
     * @param other
     * @return boolean
     */
    @Override
    public boolean isDuplicateOf(Object other) {
        if (!(super.isDuplicateOf(other))) {
            return false;
        }
        if (!(other instanceof VariableRestriction)) {
            return false;
        }
        VariableRestriction other2 = (VariableRestriction) other;
        if (!(areStringsEqualOrBothNull(other2.getOperation(), this.getOperation()))) {
            return false;
        }
        if (!(areStringsEqualOrBothNull(other2.getValue(), this.getValue()))) {
            return false;
        }
        return true;
    }
}
