package com.trapezium.vrml;

import com.trapezium.parse.TokenEnumerator;

/**
 *  Scene graph component representing a LeftBracket.
 *
 *  Optimization is to only store these when there is an error.  This
 *  optimization is not complete, done in some places.  When this
 *  optimization is complete, LeftBracket can be replaced by ErrorElement.
 *
 *  @author          Johannes N. Johannsen
 *  @version         1.0, 5 Nov 1997
 *
 *  @since           1.0
 */
public class LeftBracket extends SingleTokenElement {

    public LeftBracket(int tokenOffset, TokenEnumerator v) {
        super(tokenOffset);
        if (!v.sameAs(tokenOffset, "[")) {
            setError("Expected '[' at this point");
        }
    }

    public static boolean isValid(int tokenOffset, TokenEnumerator v) {
        return (v.sameAs(tokenOffset, "["));
    }
}
