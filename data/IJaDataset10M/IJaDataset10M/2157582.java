package mimosa.scheduler;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * An influence support provides the functionalities for managing listeners and firing the influence events.
 *  
 * @author Jean-Pierre Muller
 */
public class InfluenceSupport {

    private Vector<InfluenceListener> listeners = new Vector<InfluenceListener>();

    /**
    * Commentaire relatif au constructeur InfluenceSupport.
     */
    public InfluenceSupport() {
        super();
    }

    /**
     * Adds a StateChangeListener to the list.
     * @param listener The StateChangeListener to add.
     */
    public void addInfluenceListener(InfluenceListener listener) {
        listeners.add(listener);
    }

    /**
     * Fires an influence post.
     * @param sim
     * @param globalDate
     * @param evt
     */
    public void fireInfluencePost(Coordinator sim, long globalDate, Influence evt) {
        InfluenceEvent simEvt = new InfluenceEvent(sim, globalDate, evt);
        for (Iterator<InfluenceListener> e = listeners.iterator(); e.hasNext(); ) {
            e.next().eventPosted(simEvt);
        }
    }

    /**
     * Fires an influence send.
     * @param sim
     * @param globalDate
     * @param evt
     * @param evts
     */
    public void fireInfluenceSent(Coordinator sim, long globalDate, InternalInfluence evt, Collection<ExternalInfluence> evts) {
        InfluenceEvent simEvt = new InfluenceEvent(sim, globalDate, evt, evts);
        for (Iterator<InfluenceListener> e = listeners.iterator(); e.hasNext(); ) e.next().eventSent(simEvt);
    }

    /**
     * Removes a StateChangeListener from the list.
     * @param listener The StateChangeListener to remove.
     */
    public void removeInfluenceListener(InfluenceListener listener) {
        listeners.remove(listener);
    }
}
