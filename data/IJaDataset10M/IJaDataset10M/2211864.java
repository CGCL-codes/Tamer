package org.josso.selfservices.password.generator;

/**
 * A helper construct representing an element used for the generation of
 * passwords. An instance of this class is either a vowel or a consonant. Both
 * vowels and consonants can be marked as dipthongs. The marking is implemented
 * as a bit masked int. By using this class a pronounceable password is created
 * which should be easy to remember.
 * 
 * @author unrz205
 */
public class PwElement {

    protected String value;

    protected int type;

    /**
	 * Constructor of the Password Element class
	 * 
	 * @param value
	 *            the string representation of the vowel, consonant (dipthong)
	 * @param type
	 *            the type of the password element
	 */
    public PwElement(String value, int type) {
        this.value = value;
        this.type = type;
    }

    /**
	 * Returns the type of this password element
	 * 
	 * @return a bit-masked type describing the type of element
	 */
    public int getType() {
        return type;
    }

    /**
	 * Sets the type of this password element
	 * 
	 * @param type
	 */
    public void setType(int type) {
        this.type = type;
    }

    /**
	 * Returns the string representation of this password element
	 * 
	 * @return the string representation of this password element
	 */
    public String getValue() {
        return value;
    }

    /**
	 * Sets the string representation of this password element
	 * 
	 * @param value
	 */
    public void setValue(String value) {
        this.value = value;
    }
}
