package net.sourceforge.hlm.impl.library.formulae;

import net.sourceforge.hlm.impl.library.contexts.*;
import net.sourceforge.hlm.impl.library.terms.element.*;
import net.sourceforge.hlm.impl.library.terms.set.*;
import net.sourceforge.hlm.library.formulae.*;
import net.sourceforge.hlm.library.terms.element.*;
import net.sourceforge.hlm.library.terms.set.*;
import net.sourceforge.hlm.util.storage.*;

public class ElementFormulaImpl extends RelationFormulaImpl<ElementTerm, SetTerm> implements ElementFormula {

    public ElementFormulaImpl(StoredObject storedObject, ContextImpl outerContext) {
        super(storedObject, outerContext);
    }

    public ElementTermPlaceholderImpl getLeftTerm() {
        if (this.leftTerm == null) {
            this.leftTerm = new ElementTermPlaceholderImpl(this.storedObject, 0, this.outerContext);
        }
        return this.leftTerm;
    }

    public SetTermPlaceholderImpl getRightTerm() {
        if (this.rightTerm == null) {
            this.rightTerm = new SetTermPlaceholderImpl(this.storedObject, 1, this.outerContext);
        }
        return this.rightTerm;
    }

    private ElementTermPlaceholderImpl leftTerm;

    private SetTermPlaceholderImpl rightTerm;
}
