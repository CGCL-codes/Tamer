package org.nodal.storage.memory;

import org.nodal.model.Node;
import org.nodal.model.TxnOp;
import org.nodal.model.Operator;
import org.nodal.model.OpClone;
import org.nodal.model.OpCloned;

class memOpClone implements OpClone {

    Node context;

    Node src;

    memOpClone(Node ctx, Node from) {
        context = ctx;
        src = from;
    }

    public Node context() {
        return context;
    }

    public Node source() {
        return src;
    }

    static class myTxnOp extends memTxnOp {

        memOpClone operator;

        Operator inverse;

        myTxnOp(memOpClone op, Node n) {
            super(n);
            operator = op;
            inverse = null;
        }

        public Operator op() {
            return operator;
        }

        public Operator inverseOp() {
            if (inverse == null) inverse = new memOpCloned(null, subj, operator.src);
            return inverse;
        }

        public boolean undo() {
            return true;
        }
    }

    public TxnOp apply(Node n) {
        return new myTxnOp(this, n);
    }
}

;
