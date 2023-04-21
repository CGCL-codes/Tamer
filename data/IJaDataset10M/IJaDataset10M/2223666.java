package com.ibm.wala.logic;

import java.util.*;

public class EmptyTheory extends AbstractTheory {

    private static final EmptyTheory INSTANCE = new EmptyTheory();

    public static EmptyTheory singleton() {
        return INSTANCE;
    }

    private EmptyTheory() {
    }

    public Collection<? extends IFormula> getSentences() {
        return Collections.emptySet();
    }

    public IVocabulary getVocabulary() {
        return BasicVocabulary.make(Collections.<IFunction>emptySet());
    }
}
