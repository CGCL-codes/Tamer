package org.unicore;

/**
 * The Identifier of a Fifo
 *
 * @see org.unicore.ajo.Fifo
 *
 * @author S. van den Berghe (fecit)
 * @author D. Snelling (fecit)
 *
 * @since AJO 4.0
 *
 * @version $Id: FifoIdentifier.java,v 1.2 2004/05/26 16:31:45 svenvdb Exp $
 * 
 **/
public final class FifoIdentifier extends Identifier {

    public FifoIdentifier() {
        super("");
    }

    /**
     * Create a new Fifo Identifier.
     *
     * @param name The name of the Fifo Identifier
     *
     **/
    public FifoIdentifier(String name) {
        super(name);
    }

    /**
     * Create a new Fifo Identifier with the given value.
     *
     * @param name The name of the Fifo Identifier
     * @param value The value for this Identifier.
     *
     **/
    public FifoIdentifier(String name, int value) {
        super(name, value);
    }

    static final long serialVersionUID = 0;
}
