package org.decisiondeck.jmcda.xws.transformer.xml;

import java.util.Collection;
import java.util.Set;
import org.decisiondeck.jmcda.exc.FunctionWithInputCheck;
import org.decisiondeck.jmcda.exc.InvalidInputException;
import org.decisiondeck.jmcda.persist.xmcda2.XMCDAAlternatives;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XAlternatives;
import org.decisiondeck.xmcda_oo.structure.Alternative;

public class ToAlternatives implements FunctionWithInputCheck<Collection<XAlternatives>, Set<Alternative>> {

    @Override
    public Set<Alternative> apply(Collection<XAlternatives> input) throws InvalidInputException {
        return input == null ? null : new XMCDAAlternatives().readAll(input);
    }
}
