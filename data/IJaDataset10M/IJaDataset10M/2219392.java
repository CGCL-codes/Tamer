package il.ac.biu.cs.grossmm.impl.activeData;

import il.ac.biu.cs.grossmm.api.data.Root;
import il.ac.biu.cs.grossmm.api.flow.NoSuchSubscription;
import il.ac.biu.cs.grossmm.api.flow.NodeInspector;
import il.ac.biu.cs.grossmm.api.flow.Status;
import il.ac.biu.cs.grossmm.api.flow.Subscriber;
import il.ac.biu.cs.grossmm.api.flow.SubscriptionPoint;
import il.ac.biu.cs.grossmm.api.flow.Track;
import il.ac.biu.cs.grossmm.api.keys.Key;
import il.ac.biu.cs.grossmm.api.keys.KeyPattern;

public class SubscriptionPointImpl<N> implements SubscriptionPoint<N> {

    final SubscriptionDispatcher<N> dispatcher;

    final Subscriber<N> subscriber;

    final KeyPattern pattern;

    final DataLayer<N> dataLayer;

    SubscriptionPointImpl(DataLayer<N> dataLayer, Subscriber<N> subscriber, KeyPattern pattern) {
        this.dispatcher = new SubscriptionDispatcher<N>(this);
        this.subscriber = subscriber;
        this.pattern = pattern;
        this.dataLayer = dataLayer;
    }

    public Status subscribe(Key key, Track track) {
        SubscriptionImpl<N> s = dataLayer.tryCached(key, subscriber);
        if (s != null) {
            if (s.getStatus() == Status.ACTIVE) {
                subscriber.activated(key, s.getRoot());
            } else {
                try {
                    subscriber.deactivated(key, s.getStatus());
                } catch (NoSuchSubscription e) {
                    s.remove(subscriber);
                    e.printStackTrace();
                    return new Status.Error(e.getMessage());
                }
            }
            return s.getStatus();
        }
        return dispatcher.subscribe(key, track);
    }

    public void unsubscribe(Key key) {
        dataLayer.unsubscribe(key, subscriber);
    }

    public Root<N> getRoot(Key key) {
        return dataLayer.getRoot(key);
    }

    public KeyPattern getPattern() {
        return pattern;
    }

    public NodeInspector readLock(Root<N> root) {
        return ((il.ac.biu.cs.grossmm.impl.activeData.Root<N>) root).readLock();
    }

    public void readUnlock(Root<N> root) {
        ((il.ac.biu.cs.grossmm.impl.activeData.Root<N>) root).readLock();
    }

    public Root<N> fetch(Key key) {
        throw new RuntimeException("Not implemented");
    }

    public void remove() {
        dataLayer.removeSubscriptionPoint(this);
    }

    public Status getSubscriptionState(Key key) {
        SubscriptionImpl<N> s = dataLayer.subscriptions.get(key);
        if (s == null) return null;
        return s.getStatus();
    }

    @Override
    public String toString() {
        return "SP(" + dataLayer.nodeType + ") " + subscriber + " " + pattern + " ";
    }
}
