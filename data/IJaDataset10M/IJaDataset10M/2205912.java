package backend.filter;

import java.util.Iterator;
import java.util.Vector;
import backend.AbstractONDEXPlugin;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXGraph;
import backend.core.AbstractRelation;
import backend.core.ONDEXView;
import backend.core.security.Session;
import backend.event.FilterEvent;
import backend.event.FilterListener;
import backend.event.type.EventType;

public abstract class AbstractONDEXFilter implements AbstractONDEXPlugin<FilterArguments> {

    private Vector<FilterListener> listeners = new Vector<FilterListener>();

    protected Session s;

    protected FilterArguments args;

    public abstract void copyResultsToNewGraph(AbstractONDEXGraph exportGraph);

    /**
	 * Constructor for AbstractONDEXFilter with given session.
	 * 
	 * @param s - session context
	 */
    public AbstractONDEXFilter(Session s) {
        this.s = s;
    }

    public void setArguments(FilterArguments fa) {
        args = fa;
    }

    public FilterArguments getArguments() {
        return args;
    }

    /**
	 * Adds a filter listener to the list.
	 * 
	 * @param l - FilterListener
	 */
    public void addFilterListener(FilterListener l) {
        listeners.add(l);
    }

    /**
	 * Removes a filter listener from the list.
	 * 
	 * @param l - FilterListener
	 */
    public void removeFilterListener(FilterListener l) {
        listeners.remove(l);
    }

    /**
	 * Returns the list of filter listeners.
	 * 
	 * @return list of FilterListeners
	 */
    public FilterListener[] getFilterListeners() {
        return listeners.toArray(new FilterListener[0]);
    }

    /**
	 * Notify all listeners that have registered with this class.
	 * 
	 * @param eventName - name of event
	 */
    public void fireEventOccurred(EventType e) {
        if (listeners.size() > 0) {
            FilterEvent pe = new FilterEvent(this, e);
            Iterator<FilterListener> it = listeners.iterator();
            while (it.hasNext()) {
                it.next().eventOccurred(pe);
            }
        }
    }

    /**
	 * 
	 * @return relations that are visible post filter
	 */
    public abstract ONDEXView<AbstractRelation> getVisibleRelations();

    /**
	 * 
	 * @return concepts that are visible post filter
	 */
    public abstract ONDEXView<AbstractConcept> getVisibleConcepts();
}
