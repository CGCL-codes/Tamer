package org.basex.query.up.primitives;

import org.basex.data.Data;
import org.basex.query.item.DBNode;
import org.basex.query.item.Nod;
import org.basex.query.iter.NodIter;

/**
 * Insert before primitive.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-10, ISC License
 * @author Lukas Kircher
 */
public final class InsertBefore extends NodeCopy {

    /**
   * Constructor.
   * @param n target node
   * @param copy copy of nodes to be inserted
   */
    public InsertBefore(final Nod n, final NodIter copy) {
        super(n, copy);
    }

    @Override
    public void apply(final int add) {
        if (md == null) return;
        final DBNode n = (DBNode) node;
        final Data d = n.data;
        final int pre = n.pre;
        d.insert(pre, d.parent(pre, Nod.kind(node.type)), md);
        mergeTexts(d, pre + md.meta.size - 1, pre + md.meta.size);
    }

    @Override
    public void merge(final UpdatePrimitive p) {
        c.add(((NodeCopy) p).c.getFirst());
    }

    @Override
    public PrimitiveType type() {
        return PrimitiveType.INSERTBEFORE;
    }
}
