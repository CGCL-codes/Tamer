package org.velma.filter;

import java.util.ArrayList;
import java.util.BitSet;
import javax.swing.event.EventListenerList;
import org.velma.event.FilterEvent;
import org.velma.event.FilterListener;

/**
 * A class representing a collection filters that are all applicable to a
 * particular alignment. Its primary task is to determine the overall subset
 * defined by multiple applicable filters. It does this by calculating the
 * intersection of all of the subsets defined by each individual filter.
 * 
 * @author Andy Walsh
 * 
 */
public class FilterSet implements FilterListener {

    private ArrayList<Filter> filters;

    private ArrayList<Character> operations;

    protected EventListenerList listenerList;

    protected FilterEvent filterEvent = null;

    public FilterSet() {
        filters = new ArrayList<Filter>();
        operations = new ArrayList<Character>();
        listenerList = new EventListenerList();
    }

    public BitSet getIncludedSet() {
        if (filters.size() == 0) return null;
        BitSet included = filters.get(0).getIncluded();
        for (int i = 1; i < filters.size(); i++) {
            char op = operations.get(i - 1);
            switch(op) {
                case 'a':
                    included.and(filters.get(i).getIncluded());
                    break;
                case 'o':
                    included.or(filters.get(i).getIncluded());
                    break;
                default:
                    included.xor(filters.get(i).getIncluded());
                    break;
            }
        }
        return included;
    }

    public void addFilter(Filter f) {
        filters.add(f);
        f.addFilterListener(this);
    }

    public void addFilter(Filter f, char op) {
        filters.add(f);
        operations.add(op);
        f.addFilterListener(this);
    }

    public void addOperation(char op) {
        operations.add(op);
    }

    public void setOperation(int index, char op) {
        operations.set(index, op);
        fireFilterEvent(FilterEvent.POSITION_FILTER_EVENT);
    }

    public int size() {
        return filters.size();
    }

    public void filterChanged(FilterEvent e) {
        fireFilterEvent(e.getEventType());
    }

    public void addFilterListener(FilterListener l) {
        listenerList.add(FilterListener.class, l);
    }

    private void fireFilterEvent(int eventType) {
        FilterListener listeners[] = listenerList.getListeners(FilterListener.class);
        if (filterEvent == null || filterEvent.getSource() == null) filterEvent = new FilterEvent(this, eventType);
        filterEvent.setEventType(eventType);
        for (FilterListener l : listeners) l.filterChanged(filterEvent);
    }

    public void removeFilterListener(FilterListener l) {
        listenerList.remove(FilterListener.class, l);
    }
}
