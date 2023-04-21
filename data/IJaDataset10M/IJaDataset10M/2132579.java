package com.googlecode.kipler.reasoning;

import java.util.ArrayList;
import java.util.List;
import com.googlecode.kipler.syntax.concept.Concept;
import com.googlecode.kipler.syntax.concept.ConceptName;

public class InconsistentLiterals {

    private List<ConceptName> literals = new ArrayList<ConceptName>();

    private Concept interpolant;

    public boolean add(ConceptName e) {
        return literals.add(e);
    }

    public Concept getInterpolant() {
        return interpolant;
    }

    public void setInterpolant(Concept interpolant) {
        this.interpolant = interpolant;
    }

    public List<ConceptName> getLiterals() {
        return literals;
    }
}
