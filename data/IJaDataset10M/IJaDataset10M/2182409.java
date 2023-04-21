package jmud.jgp;

import jgp.predicate.UnaryPredicate;
import jmud.Named;

public class NameMatcher implements UnaryPredicate {

    private String theName = null;

    public NameMatcher(String name) {
        theName = name;
    }

    public void setName(String name) {
        theName = name;
    }

    public boolean execute(Object obj) {
        return ((Named) obj).hasName(theName);
    }
}
