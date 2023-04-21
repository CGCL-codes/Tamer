package org.middleheaven.global.gov;

import java.io.Serializable;

/**
 * Abstracts a National Document Identification 
 * This immutable object contains characters that can be interpreted as 
 * a document identification of some sort, i.e. Social Security Number, 
 * Passport , etc...
 * <code>NDI</code> provides methods to help the number's analysis and validation 
 *
 *@see http://en.wikipedia.org/wiki/National_identification_number
 */
public class NDI implements CharSequence, Serializable {

    private static final long serialVersionUID = 6015951818470234429L;

    private String sequence;

    public NDI(char... chars) {
        this.sequence = new String(chars);
    }

    public NDI(CharSequence other) {
        this.sequence = other.toString();
    }

    public NDI subGroup(int beginIndex, int length) {
        return new NDI(this.subSequence(beginIndex, beginIndex + length));
    }

    /**
	 * Converts each character to an <code>int</code>. If the conversion
	 * cannot be performed an exception is thrown. 
	 * @return the value of the NDI as a <code>int</code> array
	 * @throws NumberFormatException if it is not possible to convert a character
	 */
    public final int[] asIntArray() throws NumberFormatException {
        int[] array = new int[sequence.length()];
        for (int i = 0; i < sequence.length(); i++) {
            array[i] = Integer.parseInt(sequence.substring(i, i + 1));
        }
        return array;
    }

    public Number asNumber() {
        try {
            return new Long(this.sequence);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean equals(String other) {
        return this.sequence.equals(other);
    }

    public boolean equals(Object other) {
        return other instanceof NDI && equals((NDI) other);
    }

    public boolean equals(NDI other) {
        return this.sequence.equals(other.sequence);
    }

    public int hashCode() {
        return this.sequence.hashCode();
    }

    @Override
    public char charAt(int index) {
        return sequence.charAt(index);
    }

    @Override
    public int length() {
        return sequence.length();
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return sequence.subSequence(beginIndex, endIndex);
    }

    public String toString() {
        return sequence;
    }
}
