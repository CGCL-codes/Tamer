package cross.event;

import java.util.LinkedHashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * EventSource of Type V, where V can be any Object produced by the EventSource.
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 * @param <V>
 */
public class EventSource<V> implements IEventSource<V> {

    private final LinkedHashSet<IListener<IEvent<V>>> listenerMap;

    private ExecutorService es = Executors.newCachedThreadPool();

    public EventSource(int nThreads) {
        this();
        this.es = Executors.newFixedThreadPool(nThreads);
    }

    public EventSource() {
        this.listenerMap = new LinkedHashSet<IListener<IEvent<V>>>();
    }

    public void addListener(final IListener<IEvent<V>> l) {
        if (this.listenerMap.contains(l)) {
        } else {
            this.listenerMap.add(l);
        }
    }

    public void fireEvent(final IEvent<V> e) {
        for (final IListener<IEvent<V>> lst : this.listenerMap) {
            es.submit(new Runnable() {

                public void run() {
                    lst.listen(e);
                }
            });
        }
    }

    public void removeListener(final IListener<IEvent<V>> l) {
        if (this.listenerMap.contains(l)) {
            this.listenerMap.remove(l);
        }
    }
}
