package net.sourceforge.hlm.simple.library.formulae;

import net.sourceforge.hlm.helpers.internal.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.formulae.*;
import net.sourceforge.hlm.simple.library.objects.*;

public class SimpleStructuralFormula extends SimpleFormula implements StructuralFormula {

    public SimpleStructuralFormula(Context<?> outerContext) {
        super(outerContext);
    }

    public SimpleStructuralCaseList<Formula> getCases() {
        return this.cases;
    }

    @Override
    public String toString() {
        return SimpleObjectFormatter.toString(this);
    }

    private SimpleStructuralCaseList<Formula> cases = new SimpleStructuralCaseList<Formula>(Formula.class, SimpleFormulaPlaceholder.class, this.outerContext);
}
