package org.chessworks.common.javatools.events;

import java.util.Collection;
import java.util.Comparator;
import java.util.EventObject;
import java.util.TreeSet;

/**
 * 
 * @author Doug Bateman ("DuckStorm")
 */
public class PriorityListenerSupport<E extends EventObject> extends ListenerSupport<E> {

    private static final long serialVersionUID = -7255589580672985253L;

    private static class PrioritizedEventListener<E extends EventObject> implements EventListener<E> {

        public final int priority;

        public final EventListener<E> target;

        public PrioritizedEventListener(int priority, EventListener<E> target) {
            this.priority = priority;
            this.target = target;
        }

        @Override
        public boolean handleEvent(E event) {
            return target.handleEvent(event);
        }
    }

    private static class PriorityComparator<E extends EventObject> implements Comparator<EventListener<? super E>> {

        @Override
        public int compare(EventListener<? super E> o1, EventListener<? super E> o2) {
            int priority1;
            int priority2;
            EventListener<?> target1;
            EventListener<?> target2;
            if (o1 instanceof PrioritizedEventListener<?>) {
                PrioritizedEventListener<?> p1 = (PrioritizedEventListener<?>) o1;
                priority1 = p1.priority;
                target1 = p1.target;
            } else {
                priority1 = 0;
                target1 = o1;
            }
            if (o1 instanceof PrioritizedEventListener<?>) {
                PrioritizedEventListener<?> p2 = (PrioritizedEventListener<?>) o2;
                priority2 = p2.priority;
                target2 = p2.target;
            } else {
                priority2 = 0;
                target2 = o1;
            }
            if (target1.equals(target2)) return 0; else return priority1 - priority2;
        }
    }

    public PriorityListenerSupport(PriorityComparator<E> compare) {
        super(new TreeSet<EventListener<? super E>>(compare));
    }

    public boolean add(EventListener<? super E> e) {
        if (contains(e)) {
        }
        return super.add(e);
    }

    public boolean addAll(Collection<? extends EventListener<? super E>> c) {
        return super.addAll(c);
    }
}
