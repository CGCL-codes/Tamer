package net.sourceforge.hlm.simple.library.terms.symbol;

import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.terms.symbol.*;
import net.sourceforge.hlm.simple.library.context.*;

public class SimpleSymbolTermPlaceholder extends SimpleContextPlaceholder<SymbolTerm> {

    public SimpleSymbolTermPlaceholder(Context<?> outerContext) {
        super(SymbolTerm.class, outerContext);
    }

    @Override
    protected <A extends SymbolTerm> A create(Class<A> type) {
        return (A) SimpleSymbolTerm.create(type, this.outerContext);
    }
}
