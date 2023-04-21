package com.ravana.pos;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Rounding to nearest 5 cents for Australian businesses
 * @author Manjuka Soysa
 */
public class PosRounding {

    static HashMap roundingMap = new HashMap();

    static {
        roundingMap.put(1, new BigDecimal("-0.01"));
        roundingMap.put(2, new BigDecimal("-0.02"));
        roundingMap.put(3, new BigDecimal("0.02"));
        roundingMap.put(4, new BigDecimal("0.01"));
        roundingMap.put(6, new BigDecimal("-0.01"));
        roundingMap.put(7, new BigDecimal("-0.02"));
        roundingMap.put(8, new BigDecimal("0.02"));
        roundingMap.put(9, new BigDecimal("0.01"));
    }

    /** Creates a new instance of PosRounding */
    public static BigDecimal getRounding(BigDecimal bd) {
        int decimalDigit = bd.movePointRight(2).intValue() % 10;
        BigDecimal mappedVal = (BigDecimal) roundingMap.get(decimalDigit);
        if (mappedVal == null) return BigDecimal.ZERO;
        return mappedVal;
    }

    public static void main(String[] argv) {
        getRounding(new BigDecimal("5.11"));
        getRounding(new BigDecimal("25.12"));
        getRounding(new BigDecimal("54.13"));
        getRounding(new BigDecimal("5.14"));
        getRounding(new BigDecimal("345.16"));
        getRounding(new BigDecimal("25.17"));
        getRounding(new BigDecimal("54.18"));
        getRounding(new BigDecimal("5.19"));
    }
}
