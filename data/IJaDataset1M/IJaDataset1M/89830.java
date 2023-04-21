package net.sourceforge.hlm.simple.library.terms.element;

import net.sourceforge.hlm.helpers.internal.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.objects.constructions.*;
import net.sourceforge.hlm.library.terms.element.*;
import net.sourceforge.hlm.simple.library.objects.*;

public class SimpleConstructorTerm extends SimpleElementTerm implements ConstructorTerm {

    public SimpleConstructorTerm(Context<?> outerContext) {
        super(outerContext);
    }

    public SimpleMathObjectReference<Construction> getConstruction() {
        return this.construction;
    }

    public SimpleMathObjectReference<Constructor> getConstructor() {
        return this.constructor;
    }

    @Override
    public String toString() {
        return SimpleObjectFormatter.toString(this);
    }

    private SimpleMathObjectReference<Construction> construction = new SimpleMathObjectReference<Construction>(Construction.class, this.outerContext);

    private SimpleMathObjectReference<Constructor> constructor = new SimpleMathObjectReference<Constructor>(Constructor.class, this.outerContext);
}
