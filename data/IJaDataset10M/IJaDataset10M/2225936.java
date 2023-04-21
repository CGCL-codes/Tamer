package org.kwantu.m2.model.ui;

import java.math.BigDecimal;

/**
 *
 * Matcher values of type money(java.math.BigDecimal).
 * @author siviwe
 */
public class MoneyValueMatcher extends RestrictionTypeMatcher {

    @Override
    boolean contains(Object valueToMatch, String restrictionValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    boolean startsWith(Object valueToMatch, String restrictionValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    boolean equals(Object valueToMatch, String restrictionValue) {
        boolean result = false;
        if (valueToMatch != null) {
            if (valueToMatch.toString().equals(restrictionValue)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    boolean greaterThan(Object valueToMatch, String restrictionValue) {
        boolean result = false;
        if (valueToMatch != null) {
            BigDecimal valueOfRestriction = new BigDecimal(restrictionValue);
            BigDecimal matchValue = (BigDecimal) valueToMatch;
            if (matchValue.compareTo(valueOfRestriction) == 1) {
                result = true;
            }
        }
        return result;
    }

    @Override
    boolean lessThan(Object valueToMatch, String restrictionValue) {
        boolean result = false;
        if (valueToMatch != null) {
            BigDecimal valueOfRestriction = new BigDecimal(restrictionValue);
            BigDecimal matchValue = (BigDecimal) valueToMatch;
            if (matchValue.compareTo(valueOfRestriction) == -1) {
                result = true;
            }
        }
        return result;
    }
}
