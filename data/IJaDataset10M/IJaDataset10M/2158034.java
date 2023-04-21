package net.sourceforge.hlm.simple.library.terms.element;

import net.sourceforge.hlm.helpers.internal.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.terms.element.*;
import net.sourceforge.hlm.simple.library.objects.*;

public class SimpleStructuralElementTerm extends SimpleElementTerm implements StructuralElementTerm {

    public SimpleStructuralElementTerm(Context<?> outerContext) {
        super(outerContext);
    }

    public SimpleStructuralCaseList<ElementTerm> getCases() {
        return this.cases;
    }

    @Override
    public String toString() {
        return SimpleObjectFormatter.toString(this);
    }

    private SimpleStructuralCaseList<ElementTerm> cases = new SimpleStructuralCaseList<ElementTerm>(ElementTerm.class, SimpleElementTermPlaceholder.class, this.outerContext);
}
