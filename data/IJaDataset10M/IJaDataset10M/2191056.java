package com.agentfactory.clf.lang;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.agentfactory.clf.reasoner.Bindings;

public class StringTerm implements ITerm {

    private String value;

    public StringTerm(String value) {
        this.value = value;
    }

    @Override
    public ITerm apply(Bindings set) {
        return this;
    }

    @Override
    public boolean containsVariable(Variable variable) {
        return false;
    }

    @Override
    public Set<Variable> getVariables() {
        return new HashSet<Variable>();
    }

    @Override
    public boolean hasTerms() {
        return false;
    }

    @Override
    public boolean isCompatibleFunction(ITerm term) {
        return false;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public boolean isDistinctFunction(ITerm term) {
        return false;
    }

    @Override
    public boolean isNotFunctional(ITerm term) {
        return false;
    }

    @Override
    public boolean matches(ITerm term) {
        if (StringTerm.class.isInstance(term)) {
            return true;
        }
        return false;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (StringTerm.class.isInstance(object)) {
            return value.equals(((StringTerm) object).value);
        }
        return false;
    }

    @Override
    public List<ITerm> terms() {
        List<ITerm> terms = new LinkedList<ITerm>();
        return terms;
    }
}
