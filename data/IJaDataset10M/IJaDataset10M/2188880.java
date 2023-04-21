package pl.org.minions.stigma.server.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import pl.org.minions.utils.collections.SuperSet;

/**
 * Special collection for queuing objects to be "polled"
 * when turn number is proper. Every object can be enqueued
 * only once, re-adding it to queue will move it to
 * different turn. All adding, re-adding and polling
 * operations will be made in (average) constant time -
 * collection works as hash map merged with linked list for
 * speeding up operations. Size is NOT optimized.
 * @param <T>
 *            queued object
 */
public class TurnScheduleQueue<T extends TurnSchedulable> {

    private static class ListElement<T> {

        private ListElement<T> next;

        private int turnNumber;

        private Set<T> managerSet = new HashSet<T>();

        public ListElement(int turnNumber) {
            this.turnNumber = turnNumber;
        }

        public void add(T manager) {
            managerSet.add(manager);
        }

        public void detach() {
            next = null;
        }

        public Set<T> getObjectSet() {
            return managerSet;
        }

        public int getTurnNumber() {
            return turnNumber;
        }

        public ListElement<T> next() {
            return next;
        }

        public void remove(T manager) {
            managerSet.remove(manager);
        }

        public void setNext(ListElement<T> next) {
            this.next = next;
        }
    }

    private ListElement<T> head;

    private ListElement<T> tail;

    private Map<Integer, ListElement<T>> elementMap = new HashMap<Integer, ListElement<T>>();

    private ListElement<T> addTurn(int turnNumber) {
        if (head == null) {
            head = tail = new ListElement<T>(turnNumber);
            elementMap.put(turnNumber, tail);
            return head;
        }
        if (turnNumber < head.getTurnNumber()) {
            for (int i = head.getTurnNumber() - 1; i >= turnNumber; --i) {
                ListElement<T> oldHead = head;
                head = new ListElement<T>(i);
                head.setNext(oldHead);
                elementMap.put(i, head);
            }
            return head;
        }
        for (int i = tail.getTurnNumber() + 1; i <= turnNumber; ++i) {
            ListElement<T> ret = new ListElement<T>(i);
            elementMap.put(i, ret);
            tail.setNext(ret);
            tail = ret;
        }
        return tail;
    }

    /**
     * Enqueues object for given turn number. Removes from
     * previous (if any) turn assignment.
     * @param turnNumber
     *            turn number for which manager should be
     *            enqueued
     * @param object
     *            object to enqueue
     */
    public void enqueue(int turnNumber, T object) {
        int lastQueuedTurn = object.getLastQueuedTurn();
        if (lastQueuedTurn > 0) {
            ListElement<T> el = elementMap.get(lastQueuedTurn);
            if (el != null) el.remove(object);
        }
        ListElement<T> el = elementMap.get(turnNumber);
        if (el == null) el = addTurn(turnNumber);
        el.add(object);
        object.setLastQueuedTurn(turnNumber);
    }

    /**
     * Polls for objects scheduled for given turn.
     * @param turnNumber
     *            turn in which objects should be ready
     * @return collection of ready objects
     */
    public Collection<T> poll(int turnNumber) {
        if (head == null) return null;
        SuperSet<T> ret = null;
        while (head != null && head.getTurnNumber() <= turnNumber) {
            if (ret == null) ret = new SuperSet<T>();
            ret.addAll(head.getObjectSet());
            ListElement<T> oldHead = head;
            head = head.next();
            oldHead.detach();
            elementMap.remove(oldHead.getTurnNumber());
        }
        if (head == null) tail = null;
        return ret;
    }
}
