package org.manentia.obov;

import java.math.BigInteger;

/**
 *
 * @author rzuasti
 */
public class OTPUtil {

    /**
     * Generates a secret (seed) from any given string. You can use any string
     * you want, but it is recommended to use 20 character long strings 
     * (all characters above the 20th are ignored for seed generation).
     * NOTE: Identical strings will result in identical seeds which will result
     * in identical OTPs being generated.
     * 
     * @param baseText Base string to use for seed generation.
     * @return The generated secret (seed).
     */
    public static String generateSecret(String baseText) {
        if (baseText == null) {
            baseText = "00000000000000000000";
        } else if (baseText.length() < 20) {
            baseText = (baseText + "00000000000000000000").substring(0, 20);
        } else if (baseText.length() > 20) {
            baseText = baseText.substring(0, 20);
        }
        return new BigInteger(baseText.getBytes()).toString(16);
    }
}
