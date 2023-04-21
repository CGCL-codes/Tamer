package net.sf.opendf.profiler.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * @author jornj
 */
public class TimedQueue {

    public void add(Time time, Object v) {
        if (revQ.containsKey(v)) {
            this.remove(v);
        }
        List entries;
        if (!q.containsKey(time)) {
            entries = new ArrayList();
            q.put(time, entries);
        } else {
            entries = (List) q.get(time);
        }
        entries.add(v);
        revQ.put(v, time);
    }

    public void remove(Object v) {
        if (revQ.containsKey(v)) {
            Object t = revQ.get(v);
            revQ.remove(v);
            assert q.containsKey(t);
            List entries = (List) q.get(t);
            assert entries.contains(v);
            entries.remove(v);
            if (entries.isEmpty()) {
                q.remove(t);
            }
        }
    }

    public boolean isEmpty() {
        return revQ.isEmpty();
    }

    public int size() {
        return revQ.keySet().size();
    }

    public Time nextTimeStamp() {
        if (this.isEmpty()) throw new RuntimeException("Timed queue is empty---no next time stamp.");
        return (Time) q.firstKey();
    }

    public Object dequeueNext() {
        if (this.isEmpty()) throw new RuntimeException("Timed queue is empty---cannot dequeue next entry.");
        Object t = q.firstKey();
        List a = (List) q.get(t);
        Object v = a.remove(0);
        if (a.isEmpty()) {
            q.remove(t);
        }
        revQ.remove(v);
        return v;
    }

    private SortedMap q = new TreeMap();

    private Map revQ = new HashMap();
}
