package net.sourceforge.hlm.simple.library.parameters;

import net.sourceforge.hlm.helpers.internal.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.parameters.*;
import net.sourceforge.hlm.simple.library.formulae.*;

public class SimpleConstraintParameter extends SimpleParameter implements ConstraintParameter {

    public SimpleConstraintParameter(Context<?> outerContext, ParameterList list) {
        super(outerContext, list);
    }

    public SimpleFormulaPlaceholder getFormula() {
        return this.formula;
    }

    @Override
    public String toString() {
        return SimpleObjectFormatter.toString(this);
    }

    private SimpleFormulaPlaceholder formula = new SimpleFormulaPlaceholder(this.outerContext);
}
