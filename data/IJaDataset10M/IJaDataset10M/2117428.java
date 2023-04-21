package com.agentfactory.logic.update.lang;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.agentfactory.logic.update.reasoner.Bindings;

public class Function implements ITerm, Iterable<ITerm> {

    private String functor;

    private List<ITerm> terms;

    public Function(String functor) {
        this(functor, new LinkedList<ITerm>());
    }

    public Function(String functor, List<ITerm> terms) {
        this.functor = functor;
        this.terms = terms;
    }

    @Override
    public ITerm apply(Bindings set) {
        List<ITerm> list = new LinkedList<ITerm>();
        for (ITerm term : terms) {
            list.add(term.apply(set));
        }
        return new Function(functor, list);
    }

    public String functor() {
        return functor;
    }

    public boolean isConstant() {
        return terms.isEmpty();
    }

    @Override
    public Iterator<ITerm> iterator() {
        return terms.iterator();
    }

    public ITerm termAt(int i) {
        return terms.get(i);
    }

    public int size() {
        return terms.size();
    }

    public boolean isDistinctFunction(ITerm term) {
        if (Function.class.isInstance(term)) {
            Function function = (Function) term;
            return !functor.equals(function.functor);
        }
        return false;
    }

    public boolean isCompatibleFunction(ITerm term) {
        if (Function.class.isInstance(term)) {
            Function function = (Function) term;
            return functor.equals(function.functor) && (terms.size() == function.terms.size());
        }
        return false;
    }

    public boolean isNotFunctional(ITerm term) {
        if (Variable.class.isInstance(term)) {
            return containsVariable((Variable) term);
        }
        return false;
    }

    public boolean containsVariable(Variable variable) {
        for (ITerm term : terms) {
            if (term.equals(variable)) return true;
            if (term.containsVariable(variable)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (Function.class.isInstance(obj)) {
            Function function = (Function) obj;
            if (!functor.equals(function.functor)) return false;
            if (terms.size() != function.size()) return false;
            for (int i = 0; i < terms.size(); i++) {
                if (!terms.get(i).equals(function.terms.get(i))) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean hasTerms() {
        return !terms.isEmpty();
    }

    @Override
    public Set<Variable> getVariables() {
        Set<Variable> set = new HashSet<Variable>();
        for (ITerm term : terms) {
            set.addAll(term.getVariables());
        }
        return set;
    }

    public boolean matches(ITerm term) {
        if (Variable.class.isInstance(term)) {
            return true;
        }
        if (!Function.class.isInstance(term)) {
            return false;
        }
        Function function = (Function) term;
        if (!functor.equals(function.functor)) return false;
        if (terms.size() != function.terms.size()) return false;
        for (int i = 0; i < terms.size(); i++) {
            if (!terms.get(i).matches(function.terms.get(i))) return false;
        }
        return true;
    }
}
