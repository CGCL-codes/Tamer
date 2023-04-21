package net.sf.metarbe.example.wc;

import net.sf.metarbe.RuleConstraint;

public class NumberMatch implements RuleConstraint {

    public boolean match(Object aValue) {
        try {
            Double.parseDouble(String.valueOf(aValue));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
