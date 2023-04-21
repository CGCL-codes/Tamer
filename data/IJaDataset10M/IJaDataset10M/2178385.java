package de.grogra.ray2.radiosity;

/**
 * This class contains the FormFactor value.
 * 
 * @author Ralf Kopsch
 */
public class FormFactor {

    /** The Formfactor value */
    private float value;

    /**
	 * Default Constructor.
	 */
    public FormFactor() {
        value = 0.0f;
    }

    /**
	 * This Constructor sets the given FormFactor value.
	 * @param ff The value to set.
	 */
    public FormFactor(float ff) {
        value = ff;
    }

    /**
	 * Sets the FormFactor value.
	 * @param ff The value to set.
	 */
    public void setValue(float ff) {
        value = ff;
    }

    /**
	 * Returns the FormFactor value.
	 * @return Returns the FormFactor value.
	 */
    public float getValue() {
        return value;
    }

    /**
	 * Adds the given value to the FormFactor value
	 * @param ff The Value to add.
	 */
    public void add(float ff) {
        value += ff;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
