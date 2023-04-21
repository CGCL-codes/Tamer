package edu.mit.osidimpl.agent.shared;

/**
 *  <p>
 *  Implements a TypeIterator for the agent implementations. 
 *  Iterators are a once pass through sequence of Types.
 *  </p><p>
 *  CVS $Id: TypeIterator.java,v 1.2 2006/04/13 20:14:59 tom Exp $
 *  </p>
 *
 *  @author  Tom Coppeto
 *  @version $OSID: 2.0$ $Revision: 1.2 $
 *  @see     org.osid.shared.Type
 *  @see     org.osid.shared.TypeIterator
 */
public class TypeIterator implements org.osid.shared.TypeIterator {

    private java.util.Vector vector = new java.util.Vector();

    private int i = 0;

    /**
     *  Constructs a <code>TypeIterator</code>.
     *
     *  @param vector contains a vector of types 
     *  @throws org.osid.shared.SharedException
     */
    public TypeIterator(java.util.Vector vector) throws org.osid.shared.SharedException {
        this.vector = vector;
    }

    /**
     *  Test if there are more types left in this iterator.
     *
     *  @return <code>true</code> if there are more types
     *  @throws org.osid.shared.SharedException
     */
    public boolean hasNextType() throws org.osid.shared.SharedException {
        return (i < this.vector.size());
    }

    /**
     *  Gets the next <code>Type</code> in this Iterator
     *
     *  @return the next <code>Type</code>
     *  @throws org.osid.shared.SharedException An exception
     *         with one of the following messages defined in
     *         org.osid.agent.AgentException may be thrown:
     *         {@link
     *         org.osid.agent.AgentException#NO_MORE_ITERATOR_ELEMENTS
     *         NO_MORE_ITERATOR_ELEMENTS}
     */
    public org.osid.shared.Type nextType() throws org.osid.shared.SharedException {
        if (i < this.vector.size()) {
            return ((org.osid.shared.Type) this.vector.elementAt(i++));
        } else {
            throw new org.osid.shared.SharedException(org.osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
