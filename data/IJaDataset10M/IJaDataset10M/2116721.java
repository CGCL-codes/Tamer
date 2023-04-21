package org.jl7.hl7;

import java.util.ArrayList;

/**
 * Represents an HL7 component
 * 
 * @since 0.1
 * 
 * @author henribenoit
 * 
 */
public class HL7Component {

    /**
	 * Character used to mark the end of a subcomponent and the beginning of the
	 * next subcomponent (i.e. subcomponent delimiter). Default: &
	 * 
	 * @since 0.1
	 */
    public char subcomponentSeparator;

    /**
	 * Character used to escape characters which would otherwise be interpreted.
	 * Default: \
	 * 
	 * @since 0.1
	 */
    public char escapeCharacter;

    /**
	 * List of all subcomponents in this component.
	 */
    protected ArrayList<HL7Subcomponent> subcomponents = new ArrayList<HL7Subcomponent>();

    public void setDelimiters(String value) {
        escapeCharacter = value.charAt(3);
        subcomponentSeparator = value.charAt(4);
    }

    /**
	 * Sets all delimiters defined for this component.
	 * 
	 * @param value
	 *            a string containing all delimiters defined for this component:
	 *            fieldSeparator (not used in this class) + componentSeparator
	 *            (not used in this class) + repetitionSeparator (not used in
	 *            this class) + escapeCharacter + subcomponentSeparator
	 * 
	 * @since 0.1
	 */
    public void setSubcomponents(String[] subComponents, String delimiters, boolean escapesInSubcomponents) {
        for (String subComponent : subComponents) {
            this.subcomponents.add(HL7Parser.parseSubcomponent(subComponent, delimiters, escapesInSubcomponents));
        }
    }

    /**
	 * Removes all subcomponents from this component and adds the given
	 * subcomponents to it.
	 * 
	 * @param fields
	 *            array of string representations of subcomponents to be added.
	 * 
	 * @param delimiters
	 *            a string containing all delimiters defined for this component:
	 *            fieldSeparator + componentSeparator + repetitionSeparator +
	 *            escapeCharacter + subcomponentSeparator
	 * 
	 * @param escapesInSubcomponents
	 *            whether escape characters are used or not
	 * 
	 * @since 0.1
	 */
    public void setSubcomponentsWithoutDelimiters(String hl7String) {
        this.subcomponents.add(HL7Parser.parseSubcomponentWithoutDelimiters(hl7String));
    }

    /**
	 * Returns the subcomponent at the given position.
	 * 
	 * @param index
	 *            position in the component of the subcomponent to be returned
	 * 
	 * @return the subcomponent at the given position
	 * 
	 * @since 0.1
	 */
    public HL7Subcomponent get(int index) {
        if (index < subcomponents.size()) {
            return (HL7Subcomponent) subcomponents.get(index);
        } else {
            return null;
        }
    }

    /**
	 * Returns the string representation of this component.
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * @since 0.1
	 */
    @Override
    public String toString() {
        String s = null;
        for (HL7Subcomponent subcomponent : subcomponents) {
            if (s == null) {
                s = subcomponent.toString();
            } else {
                s = s + subcomponentSeparator + subcomponent.toString();
            }
        }
        return s;
    }

    /**
	 * Returns the string representation of this component where all escape
	 * sequences have been replaced by their values.
	 * 
	 * @return the string representation of this component where all escape
	 *         sequences have been replaced by their values
	 * 
	 * @since 0.1
	 */
    public String getValue() {
        String s = null;
        for (HL7Subcomponent subcomponent : subcomponents) {
            if (s == null) {
                s = subcomponent.getValue();
            } else {
                s = s + subcomponentSeparator + subcomponent.getValue();
            }
        }
        return s;
    }
}
