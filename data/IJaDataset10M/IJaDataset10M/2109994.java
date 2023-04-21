package de.jassda.modules.trace.event;

import de.jassda.core.event.EventSetException;
import de.jassda.core.event.JassdaEvent;
import de.jassda.csp.*;
import java.util.Vector;

/**
 * Interface EventSet defines an event set.
 *
 *
 * @author Mark Broerkens
 * @version %I%, %G%
 */
public interface EventSet extends AnnotatedCspTreeNode, Cloneable {

    /**
     * Return a clone of this event set. Caution: this clone can return
     * a shallow copy! This means that sets refering to other (included) sets
     * will only copy the reference to these subsets (not the sets themself).
     */
    public Object clone();

    /**
    * Should return a "real" copy of the object.
    */
    public EventSet newInstance();

    /**
     * Return a textual representation of the event set.
     */
    public String toString();

    /**
     * Is the event a member of this event set when evaluting in the given context.
     * The context is used to access the current value of variables.
     *
     *
     * @param context  the context used to access the value of variables
     * @param event    the event to test membership for
     * @param flags
     *
     * @return true, if the event belongs to this set
     *
     * @throws de.jassda.core.event.EventSetException
     *
     */
    public boolean contains(Context context, JassdaEvent event, Flags flags) throws EventSetException;

    /**
	 * tests if the intersection between this
	 * eventset and the generic eventset { class=<i>&lt;classname&gt;</i> } 
	 * is empty
	 *
	 * @param classname the name that defines the generic set.
	 * @return <code>true</code> if the intersection is not empty.
	 */
    public boolean containsClass(String classname) throws EventSetException;

    /**
     * Return an optimized version of this set. Since event sets are stored
     * symbolically we sometimes can simplify the expressions used to describe
     * the set.
     *
     * @return a simplified equivalent event set
     */
    public EventSet optimize();

    /**
     * Return the description of this event set.
     */
    public Description getDescription();

    /**
     * Set the description for this event set.
     */
    public void setDescription(Description description);

    /**
     * Return the name of this event set.
     */
    public String getName();

    /**
     * Set the name for this event set.
     */
    public void setName(String name);

    /**
     * Method check
     *
     * @throws de.jassda.csp.CspException  on an error? DOC ME!
     *
     */
    public void check() throws CspException;

    public Vector getClassPatterns();
}
