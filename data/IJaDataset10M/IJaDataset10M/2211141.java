package net.sourceforge.hlm.impl.library.terms.element;

import net.sourceforge.hlm.helpers.internal.*;
import net.sourceforge.hlm.impl.library.contexts.*;
import net.sourceforge.hlm.impl.library.objects.*;
import net.sourceforge.hlm.library.objects.operators.*;
import net.sourceforge.hlm.library.terms.element.*;
import net.sourceforge.hlm.util.storage.*;

public class OperatorTermImpl extends ElementTermImpl implements OperatorTerm {

    public OperatorTermImpl(StoredObject storedObject, ContextImpl outerContext) {
        super(storedObject, outerContext);
    }

    public MathObjectReferenceImpl<Operator> getOperator() {
        if (this.operator == null) {
            this.operator = new MathObjectReferenceImpl<Operator>(Operator.class, this.storedObject, this.outerContext);
        }
        return this.operator;
    }

    @Override
    public String toString() {
        return SimpleObjectFormatter.toString(this);
    }

    private MathObjectReferenceImpl<Operator> operator;
}
