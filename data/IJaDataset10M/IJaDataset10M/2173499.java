package de.intarsys.pdf.cos;

import java.util.HashSet;
import java.util.Set;

/**
 * Adapter implementation for visiting a COS object structure, including
 * indirect references.
 * <p>
 * Every object in the potential cyclic data structure is visited exactly once.
 * 
 */
public class COSObjectWalkerDeep extends COSObjectWalkerShallow {

    private Set visited;

    private boolean swap;

    public COSObjectWalkerDeep() {
        this(true);
    }

    public COSObjectWalkerDeep(boolean swap) {
        visited = new HashSet();
        this.swap = swap;
    }

    public Set getVisited() {
        return visited;
    }

    public Object visitFromIndirectObject(COSIndirectObject io) throws COSVisitorException {
        if (getVisited().contains(io)) {
            return null;
        }
        getVisited().add(io);
        if (swap || !io.isSwapped()) {
            io.dereference().accept(this);
        }
        return null;
    }
}
