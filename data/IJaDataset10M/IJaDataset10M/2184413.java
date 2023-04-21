package net.sourceforge.hlm.impl.library.proofs;

import net.sourceforge.hlm.generic.annotations.*;
import net.sourceforge.hlm.impl.library.contexts.*;
import net.sourceforge.hlm.library.proofs.*;
import net.sourceforge.hlm.util.storage.*;

public class SubstituteStepImpl extends IntermediateStepImpl implements SubstituteStep {

    public SubstituteStepImpl(StoredObject storedObject, ContextImpl outerContext) {
        super(storedObject, outerContext);
    }

    public SourcePlaceholderImpl getSource() {
        if (this.source == null) {
            this.source = new SourcePlaceholderImpl(FIRST_CUSTOM_CHILD);
        }
        return this.source;
    }

    public RelationSideReferenceImpl getRelationSide() {
        if (this.relationSide == null) {
            this.relationSide = new RelationSideReferenceImpl(ProveSingleStepImpl.FIRST_CUSTOM_INT);
        }
        return this.relationSide;
    }

    private SourcePlaceholderImpl source;

    private RelationSideReferenceImpl relationSide;

    class SourcePlaceholderImpl extends ContextPlaceholderImpl<IndependentIntermediateStep> {

        SourcePlaceholderImpl(int index) {
            super(SubstituteStepImpl.this.storedObject, index, SubstituteStepImpl.this.outerContext);
        }

        public Class<IndependentIntermediateStep> getType() {
            return IndependentIntermediateStep.class;
        }

        @Override
        protected short getTypeID() {
            return Id.CONTEXT_ITEM;
        }

        @Override
        protected IndependentIntermediateStep createWrapper(StoredObject storedObject) {
            return (IndependentIntermediateStep) ContextItemImpl.createWrapper(storedObject, SubstituteStepImpl.this.outerContext, 0, null);
        }
    }
}
