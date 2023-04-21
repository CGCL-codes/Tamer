package net.sourceforge.hlm.impl.library.formulae;

import net.sourceforge.hlm.impl.library.contexts.*;
import net.sourceforge.hlm.library.formulae.*;
import net.sourceforge.hlm.util.storage.*;

public class ConjunctionFormulaImpl extends EnumerationFormulaImpl implements ConjunctionFormula {

    public ConjunctionFormulaImpl(StoredObject storedObject, ContextImpl outerContext) {
        super(storedObject, outerContext);
    }
}
