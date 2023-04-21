package org.tockit.relations.operations;

import org.tockit.relations.model.Relation;
import org.tockit.relations.operations.util.AbstractUnaryRelationOperation;

public class IdentityOperation<D> extends AbstractUnaryRelationOperation<D> {

    @Override
    public Relation<D> doApply(Relation<D> input) {
        return input;
    }

    public String getName() {
        return "Identity";
    }
}
