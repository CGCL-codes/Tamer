package lelouet.datacenter.simulation.events;

import java.util.Comparator;
import lelouet.datacenter.simulation.Event;

/**
 * compare two {@link AEvent} using their date.
 * 
 * @author lelouet
 * 
 */
public class TimeComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        int ret = (int) (o1.getTime() - o2.getTime());
        return ret;
    }
}
