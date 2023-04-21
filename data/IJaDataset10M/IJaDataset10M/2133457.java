package org.callbackparams.wrap.legacy;

/**
 * @author Henrik Kaipe
 */
class WrapperToStringTemplate extends WrapperTemplate {

    public String toString() {
        return String.valueOf(wrappedValue());
    }
}
