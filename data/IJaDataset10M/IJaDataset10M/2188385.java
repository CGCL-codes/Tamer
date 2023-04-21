package org.unicore.ajo;

import org.unicore.Unicore;

/**
 * ResourceDependency is an ordering relationship for a DAG of resource
 * sets derived from the DAG of AbstractActions in an
 * {@link org.unicore.ajo.ActionGroup}.
 *
 * @author S. van den Berghe (fecit)
 * @author D. Snelling (fecit)
 *
 * @since AJO 3.6
 *
 * @version $Id: ResourceDependency.java,v 1.3 2004/06/06 18:37:24 svenvdb Exp $
 * 
 * @see org.unicore.ajo.TaskResourceDAG
 * 
 **/
public final class ResourceDependency extends Unicore {

    static final long serialVersionUID = 4349457533337665552L;

    private TaskResource predecessor;

    /**
         * Get the predecessor.
         *
         **/
    public TaskResource getPredecessor() {
        return predecessor;
    }

    private TaskResource successor;

    /**
         * Get the successor.
         *
         * @return The second <tt>TaskResource</tt> of the <tt>ResourceDependency</tt>.
         *
         **/
    public TaskResource getSuccessor() {
        return successor;
    }

    /**
         * Create a new ResourceDependency.
         *
         * @param pred The predecessor TaskResource
         * @param succ The successor TaskResource
         *
         **/
    public ResourceDependency(TaskResource pred, TaskResource succ) {
        predecessor = pred;
        successor = succ;
    }

    public ResourceDependency() {
        this(null, null);
    }

    /**
         * ResourceDependencies are the same if they have the same predecessor
         * and successor. 
         *
         **/
    public boolean equals(Object in) {
        return (in.getClass() == this.getClass() && ((ResourceDependency) in).getPredecessor().equals(predecessor) && ((ResourceDependency) in).getSuccessor().equals(successor));
    }

    public int hashCode() {
        return predecessor.hashCode() ^ successor.hashCode();
    }
}
