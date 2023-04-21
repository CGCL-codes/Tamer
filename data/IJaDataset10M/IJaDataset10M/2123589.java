package com.trapezium.vrml;

import com.trapezium.parse.TokenEnumerator;
import com.trapezium.vrml.fields.ExposedField;
import com.trapezium.vrml.fields.NotExposedField;
import com.trapezium.vrml.fields.MFFieldValue;

/**
 *  Scene graph component representing a value.
 *
 *  Due to performance and memory, values in nearly all cases are only
 *  represented by this object when they have an associated error.  In all
 *  other cases, access to values is through the TokenEnumerator.
 *
 *  @author          Johannes N. Johannsen
 *  @version         1.0, 17 Dec 1997
 *
 *  @since           1.0
 */
public class Value extends SingleTokenElement {

    public Value(int tokenOffset) {
        super(tokenOffset);
    }

    public Value(TokenEnumerator v) {
        super(v.getNextToken());
    }
}
