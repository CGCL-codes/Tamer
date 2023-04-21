package com.ravana.pos;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Manjuka Soysa
 */
public class DecimalUtils {

    public static BigDecimal getScaledDecimal(double d) {
        return (new BigDecimal(d)).setScale(2, RoundingMode.HALF_EVEN);
    }
}
