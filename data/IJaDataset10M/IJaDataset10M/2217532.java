package avrora.sim.clock;

import avrora.sim.Simulator;

/**
 * The <code>DeltaQueue</code> class implements an amortized constant time delta-queue for processing of
 * scheduled events. Events are put into the queue that will fire at a given number of cycles in the future.
 * An internal delta list is maintained where each link in the list represents a set of events to be fired
 * some number of clock cycles after the previous link.
 * <p/>
 * Each delta between links is maintained to be non-zero. Thus, to insert an event X cycles in the future, at
 * most X nodes will be skipped over. Therefore, when the list is advanced over X time steps, this cost is
 * amortized to be constant.
 * <p/>
 * For each clock cycle, only the first node in the list must be checked, leading to constant time work per
 * clock cycle.
 * <p/>
 * This class allows the clock to be advanced multiple ticks at a time.
 * <p/>
 * Also, since this class is used heavily in the simulator, its performance is important and maintains an
 * internal cache of objects. Thus, it does not create garbage over its execution and never uses more space
 * than is required to store the maximum encountered simultaneous events. It does not use standard libraries,
 * casts, virtual dispatch, etc.
 */
public class DeltaQueue {

    /**
     * The <code>EventList</code> class represents a link in the list of events for a given <code>Link</code>
     * in the delta queue chain.
     */
    private static class EventList {

        Simulator.Event event;

        EventList next;

        /**
         * The constructor for the <code>EventList</code> class simply initializes the internal references to
         * the event and the next link in the chain based on the parameters passed.
         *
         * @param t a reference the event
         * @param n the next link in the chain
         */
        EventList(Simulator.Event t, EventList n) {
            event = t;
            next = n;
        }
    }

    /**
     * The <code>Link</code> class represents a link in the list of delta queue items that are being stored.
     * It contains a list of events that share the same delta.
     */
    private class Link {

        EventList events;

        Link next;

        long delta;

        Link(Simulator.Event t, long d) {
            events = newEventList(t, null);
            delta = d;
        }

        void add(Simulator.Event t) {
            events = newEventList(t, events);
        }

        void remove(Simulator.Event t) {
            EventList prev = null;
            EventList pos = events;
            while (pos != null) {
                EventList next = pos.next;
                if (pos.event == t) {
                    if (prev == null) events = pos.next; else prev.next = pos.next;
                    free(pos);
                } else {
                    prev = pos;
                }
                pos = next;
            }
        }

        void fire() {
            for (EventList pos = events; pos != null; pos = pos.next) {
                pos.event.fire();
            }
        }
    }

    /**
     * The <code>head</code> field stores a reference to the head of the delta queue, which represents the
     * event that is nearest in the future.
     */
    protected Link head;

    /**
     * The <code>freeLinks</code> field stores a reference to any free links that have become unused during
     * the processing of events. A free list is used to prevent garbage from accumulating.
     */
    protected Link freeLinks;

    /**
     * The <code>freeEventLists</code> field stores a reference to any free event links that have become
     * unused during the processing of events. A free list is used to prevent garbage from accumulating.
     */
    protected EventList freeEventLists;

    /**
     * The <code>count</code> field stores the total number of cycles that this queue has been advanced, i.e.
     * the sum of all <code>advance()</code> calls.
     */
    protected long count;

    /**
     * The <code>add</code> method adds an event to be executed in the future.
     *
     * @param t      the event to add
     * @param cycles the number of clock cycles in the future
     */
    public void insertEvent(Simulator.Event t, long cycles) {
        if (head == null) {
            head = newLink(t, cycles, null);
            return;
        }
        Link prev = null;
        Link pos = head;
        while (pos != null && cycles > pos.delta) {
            cycles -= pos.delta;
            prev = pos;
            pos = pos.next;
        }
        if (pos == null) {
            insertAfter(prev, t, cycles, null);
        } else if (cycles == pos.delta) {
            pos.add(t);
        } else {
            insertAfter(prev, t, cycles, pos);
        }
    }

    private void insertAfter(Link prev, Simulator.Event t, long cycles, Link next) {
        if (prev != null) prev.next = newLink(t, cycles, next); else head = newLink(t, cycles, next);
    }

    /**
     * The <code>remove</code> method removes all occurrences of the specified event within the delta queue.
     *
     * @param e the event to remove
     */
    public void removeEvent(Simulator.Event e) {
        if (head == null) return;
        Link prev = null;
        Link pos = head;
        while (pos != null) {
            Link next = pos.next;
            pos.remove(e);
            if (pos.events == null) {
                if (prev == null) head = pos.next; else prev.next = pos.next;
                if (pos.next != null) {
                    pos.next.delta += pos.delta;
                }
                free(pos);
            } else {
                prev = pos;
            }
            pos = next;
        }
    }

    /**
     * The <code>advance</code> method advances timesteps through the queue by the specified number of clock
     * cycles, processing any events.
     *
     * @param cycles the number of clock cycles to advance
     */
    public void advance(long cycles) {
        if (head == null) {
            count += cycles;
            return;
        }
        if (head.delta > cycles) {
            count += cycles;
            head.delta -= cycles;
            return;
        }
        advanceSlow(cycles);
    }

    /**
     * The <code>skipAhead()</code> method skips ahead to the next event in the queue and fires it.
     */
    public void skipAhead() {
        if (head == null) {
            count++;
            return;
        }
        Link h = head;
        count += h.delta;
        head = h.next;
        h.fire();
        free(h);
    }

    private void advanceSlow(long cycles) {
        while (head != null && cycles > 0) {
            Link pos = head;
            Link next = pos.next;
            long delta = pos.delta;
            long leftover = cycles - delta;
            if (leftover < 0) {
                count += cycles;
                pos.delta = -leftover;
                return;
            }
            count += delta;
            head = next;
            pos.fire();
            free(pos);
            cycles = leftover;
        }
        count += cycles;
    }

    /**
     * The <code>getHeadDelta()</code> method gets the number of clock cycles until the first event will
     * fire.
     *
     * @return the number of clock cycles until the first event will fire
     */
    public long getFirstEventTime() {
        if (head != null) return head.delta;
        return -1;
    }

    /**
     * The <code>getCount()</code> gets the total cumulative count of all the <code>advance()</code> calls on
     * this delta queue.
     *
     * @return the total number of cycles this queue has been advanced
     */
    public long getCount() {
        return count;
    }

    private void free(Link l) {
        l.next = freeLinks;
        freeLinks = l;
        if (l.events != null) {
            free(l.events);
            l.events = null;
        }
    }

    private void free(EventList l) {
        l.event = null;
        l.next = freeEventLists;
        freeEventLists = l;
    }

    private Link newLink(Simulator.Event t, long cycles, Link next) {
        Link l;
        if (freeLinks == null) l = new Link(t, cycles); else {
            l = freeLinks;
            freeLinks = freeLinks.next;
            l.delta = cycles;
            l.add(t);
        }
        if (next != null) {
            next.delta -= cycles;
        }
        l.next = next;
        return l;
    }

    private EventList newEventList(Simulator.Event t, EventList next) {
        EventList l;
        if (freeEventLists == null) {
            l = new EventList(t, next);
        } else {
            l = freeEventLists;
            freeEventLists = freeEventLists.next;
            l.next = next;
            l.event = t;
        }
        return l;
    }
}
