package com.agentfactory.logic.update.lang;

import java.util.List;
import java.util.Set;
import com.agentfactory.logic.update.reasoner.Bindings;

public interface IFormula {

    public IFormula apply(Bindings set);

    public String type();

    public Set<Variable> getVariables();

    public List<IFormula> asList();

    public boolean isTrue();

    public boolean isFalse();

    public boolean matches(IFormula formula);
}
