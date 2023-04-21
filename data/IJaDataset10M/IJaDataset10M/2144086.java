package com.siemens.ct.exi.datatype;

import com.siemens.ct.exi.exceptions.UnknownElementException;

public interface RestrictedCharacterSet {

    /**
	 * Retrieves the character with given code(-point).
	 * 
	 * @param code
	 * @return char or
	 * @throws UnknownElementException
	 *             code unknown
	 */
    public char getCharacter(int code) throws UnknownElementException;

    /**
	 * Returns code for given character or <code>NOT_FOUND</code> == -1 for
	 * invalid char.
	 * 
	 * @param c
	 *            character of interest
	 * @return code(-point)
	 */
    public int getCode(char c);

    /**
	 * Returns the number of entries for the set.
	 * 
	 * @return number of entries
	 */
    public int size();

    /**
	 * Returns the number of bits to encode codes for the set.
	 * <p>
	 * codingLength = ceil( log2(N + 1) ) and N is the number of characters in
	 * the restricted character set.
	 * </p>
	 * 
	 * @see RestrictedCharacterSet#size
	 * @return number of entries
	 */
    public int getCodingLength();
}
