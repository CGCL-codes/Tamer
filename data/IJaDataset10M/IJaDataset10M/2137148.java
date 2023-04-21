package org.nightlabs.util;

/**
 * This {@link ParameterCoder} allows the following characters additional to
 * {@link ParameterCoderMinusHex}:
 * <code>'_' '.' '+'</code>
 * 
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 */
public class ParameterCoderMinusHexExt extends ParameterCoderMinusHex {

    private static char[] extraAllowedChars = new char[] { '_', '.', '+' };

    public ParameterCoderMinusHexExt() {
    }

    @Override
    protected boolean literalAllowed(int c) {
        boolean superResult = super.literalAllowed(c);
        if (superResult) return true;
        for (char check : extraAllowedChars) {
            if (check == c) return true;
        }
        return false;
    }
}
