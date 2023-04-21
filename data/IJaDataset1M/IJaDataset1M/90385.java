package de.grogra.graph.impl;

import de.grogra.persistence.*;

final class GraphXAApplier extends TransactionApplier implements GraphTransaction.Consumer {

    public void addEdgeBits(Node source, Node target, int mask) {
        if (Transaction.isNotApplying(transaction)) {
            throw new IllegalStateException("GraphThreadState.addEdges may only be invoked " + "while applying a transaction");
        }
        source.getOrCreateEdgeTo(target).addEdgeBits(mask, transaction);
    }

    public void removeEdgeBits(Node source, Node target, int mask) {
        if (Transaction.isNotApplying(transaction)) {
            throw new IllegalStateException("GraphThreadState.removeEdges may only be invoked " + "while applying a transaction");
        }
        source.removeEdgeBitsTo(target, mask, transaction);
    }
}
