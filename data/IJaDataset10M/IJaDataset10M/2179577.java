package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.wbsax.StringOpaqueValue;

/**
 * A class which marks {@link StringOpaqueValue} for use with rendering access
 * keys.
 */
public class AccesskeyOpaqueValue extends StringOpaqueValue {

    public String toString() {
        return "[AccesskeyOpaqueValue:" + getValue() + "]";
    }
}
