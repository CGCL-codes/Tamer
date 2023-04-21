package net.sourceforge.sql.function.string;

import net.sourceforge.sql.function.SqlFunctionException;

/**
 *Returns the ASCII code value of the leftmost character of a an expression
 * 
 * @author ekonstantinov
 * 
 */
public class AsciiFunction {

    /**
	 * 
	 * @param ch
	 *            {@code Character} parameter to retirn an ASCII code
	 * @return {@code Integer} value representing an ASCII code
	 */
    public static Integer execute(Character ch) {
        return ((int) ch.charValue());
    }

    /**
	 * 
	 * @param str
	 *            {@code String} parameter to return an ASCII code
	 * @return {@code Integer} value representing an ASCII code for the leftmost
	 *         character of an expression
	 */
    public static Integer execute(String str) {
        if (str.length() < 1) {
            throw new SqlFunctionException("Illegal string argument. Length must be at least one character.");
        }
        return execute(str.charAt(0));
    }
}
