package net.sourceforge.hlm.impl.library.objects.lists;

import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.generic.annotations.*;
import net.sourceforge.hlm.generic.exceptions.*;
import net.sourceforge.hlm.impl.*;
import net.sourceforge.hlm.impl.generic.*;
import net.sourceforge.hlm.impl.library.contexts.*;
import net.sourceforge.hlm.impl.library.proofs.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.formulae.*;
import net.sourceforge.hlm.library.objects.*;
import net.sourceforge.hlm.library.proofs.*;
import net.sourceforge.hlm.util.*;
import net.sourceforge.hlm.util.storage.*;

public abstract class EquivalenceListImpl<T> extends PlaceholderListImpl<T, ContextPlaceholder<T>> implements EquivalenceList<T> {

    public EquivalenceListImpl(StoredObject parent, int startIndex, ContextImpl outerContext) {
        super(parent, startIndex + 1);
        this.outerContext = outerContext;
    }

    public EquivalenceProofListImpl getEquivalenceProofs() {
        if (this.equivalenceProofs == null) {
            this.equivalenceProofs = new EquivalenceProofListImpl(this.parent.createChildPlaceholder(this.startIndex - 1, Id.LIST, Id.EQUIVALENCE_PROOF), 0);
        }
        return this.equivalenceProofs;
    }

    protected Indirection<Formula> getImplicitGoal(EquivalenceProof<T> proof) {
        return null;
    }

    protected abstract short getSourceTargetTypeID();

    protected abstract T createSourceTargetWrapper(StoredObject storedObject);

    private EquivalenceProofListImpl equivalenceProofs;

    protected ContextImpl outerContext;

    class EquivalenceProofListImpl extends FixedListImpl<EquivalenceProof<T>> {

        EquivalenceProofListImpl(StoredObject parent, int startIndex) {
            super(parent, startIndex);
        }

        public Class<EquivalenceProofImpl> getItemType() {
            return EquivalenceProofImpl.class;
        }

        @Override
        protected short getTypeID() {
            return Id.EQUIVALENCE_PROOF;
        }

        @Override
        protected EquivalenceProofImpl createWrapper(StoredObject storedObject) {
            return new EquivalenceProofImpl(storedObject);
        }
    }

    class EquivalenceProofImpl extends HLMObjectImpl implements EquivalenceProof<T> {

        EquivalenceProofImpl(StoredObject storedObject) {
            super(storedObject);
        }

        public Class<Proof> getType() {
            return Proof.class;
        }

        public Reference<T> getSource() {
            return new SourceTargetReferenceImpl(0);
        }

        public int getSourceIndex() {
            return this.getSourceTargetIndex(0);
        }

        public Reference<T> getTarget() {
            return new SourceTargetReferenceImpl(1);
        }

        public int getTargetIndex() {
            return this.getSourceTargetIndex(1);
        }

        private int getSourceTargetIndex(int referenceIndex) {
            StoredObject object = this.storedObject.getReference(referenceIndex);
            if (object != null) {
                StoredObject parent = EquivalenceListImpl.this.parent;
                int count = parent.getChildCount();
                for (int index = EquivalenceListImpl.this.startIndex; index < count; index++) {
                    if (object.equals(parent.getChild(index))) {
                        return index - EquivalenceListImpl.this.startIndex;
                    }
                }
            }
            return -1;
        }

        public ProofImpl get() {
            StoredObject proof = this.storedObject.getChild(0, Id.PROOF, SubId.NONE);
            if (proof == null) {
                return null;
            } else {
                return this.createWrapper(proof);
            }
        }

        public ProofImpl fill() throws DependencyException, AlreadyFilledException {
            if (!this.isEmpty()) {
                throw new AlreadyFilledException(Translator.format("placeholder already filled"));
            }
            return this.createWrapper(this.storedObject.createChild(0, Id.PROOF, SubId.NONE));
        }

        public void clear() throws DependencyException {
            this.storedObject.setChildCount(0);
        }

        public boolean isEmpty() {
            return (this.storedObject.getChild(0) == null);
        }

        private ProofImpl createWrapper(StoredObject storedObject) {
            return new ProofImpl(storedObject, EquivalenceListImpl.this.outerContext, EquivalenceListImpl.this.getImplicitGoal(this));
        }

        class SourceTargetReferenceImpl extends ReferenceImpl<T> {

            SourceTargetReferenceImpl(int index) {
                super(EquivalenceProofImpl.this.storedObject, index);
            }

            public Class<? extends T> getType() {
                return EquivalenceListImpl.this.getContentType();
            }

            @Override
            protected short getTypeID() {
                return EquivalenceListImpl.this.getSourceTargetTypeID();
            }

            @Override
            protected T createWrapper(StoredObject storedObject) {
                return EquivalenceListImpl.this.createSourceTargetWrapper(storedObject);
            }
        }
    }
}
