package net.sf.julie.grammar.modules;

import net.sf.julie.AbstractInterpretable;
import net.sf.julie.Interpretable;
import net.sf.julie.SchemeSystem;
import net.sf.julie.grammar.Symbol;

public class UseSyntax extends AbstractInterpretable {

    public UseSyntax(SchemeSystem environment) {
        super(environment);
    }

    @Override
    public Interpretable interpret() {
        return null;
    }

    public void addSymbol(Symbol value) {
    }
}
