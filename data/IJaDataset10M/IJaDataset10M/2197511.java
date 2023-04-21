package uk.ac.cam.caret.minibix.metadata.impl;

import java.util.*;
import uk.ac.cam.caret.minibix.metadata.api.*;

public class Conversion {

    private Defaults def;

    private boolean extract;

    private List<String> terms = new ArrayList<String>();

    Conversion(Defaults def, boolean extract) {
        this.def = def;
        this.extract = extract;
    }

    public Defaults getDefaults() {
        return def;
    }

    public boolean doExtract() {
        return extract;
    }

    public String[] getTerms() {
        return terms.toArray(new String[0]);
    }

    public void addTerm(String in) {
        terms.add(in);
    }
}
