package com.shimari.profile;

import com.shimari.util.SimpleStack;
import com.shimari.util.Stats;
import java.util.*;
import java.text.DecimalFormat;

/**
  * A CallGraph is a view of the events in a Profile. It
  * is a tree-like structure which you can browse to learn where 
  * the time is going in your application.
  */
public class CallGraph {

    private Stats stats;

    private HashMap children;

    private static final DecimalFormat NUM_FMT = new DecimalFormat("##########.##");

    protected CallGraph(String name) {
        stats = new Stats(name);
        children = new HashMap();
    }

    private static final Comparator comparator = new Comparator() {

        public final int compare(Object a, Object b) {
            CallGraph ca = (CallGraph) a;
            CallGraph cb = (CallGraph) b;
            return (cb.stats.getTotal() - ca.stats.getTotal());
        }
    };

    public String getName() {
        return stats.getName();
    }

    /**
     * Add the data from this profile to the call graph. 
     */
    protected void addProfile(Profiler p) {
        SimpleStack cgStack = new SimpleStack();
        CallGraph cg = this;
        int depth = -1;
        Iterator i = p.getEvents();
        while (i.hasNext()) {
            ProfileEvent evt = (ProfileEvent) i.next();
            int evtDepth = evt.depth;
            int evtDuration = evt.duration;
            while (evtDepth <= depth) {
                cg = (CallGraph) cgStack.pop();
                depth--;
            }
            CallGraph child = (CallGraph) cg.children.get(evt.name);
            if (child == null) {
                child = new CallGraph(evt.name);
                cg.children.put(evt.name, child);
            }
            cgStack.push(cg);
            depth++;
            cg = child;
            cg.stats.observe(evtDuration);
        }
    }

    public CallGraph[] getChildren() {
        CallGraph[] ret = (CallGraph[]) children.values().toArray(new CallGraph[0]);
        Arrays.sort(ret, comparator);
        return ret;
    }

    public Stats getStats() {
        return stats;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        toString(buf, 0);
        return buf.toString();
    }

    public void toString(StringBuffer buf, int depth) {
        for (int i = 0; i < depth; i++) {
            buf.append("| ");
        }
        buf.append(stats.getName());
        buf.append(": ");
        buf.append(stats.getTotal());
        buf.append(" msec (");
        buf.append(stats.getCount());
        buf.append(" * ");
        buf.append(NUM_FMT.format(stats.getMean()));
        buf.append(" msec; ");
        buf.append(stats.getLow());
        buf.append("-");
        buf.append(stats.getHigh());
        buf.append(" ~");
        buf.append(NUM_FMT.format(stats.getDeviation()));
        buf.append(")\n");
        CallGraph kids[] = getChildren();
        for (int i = 0; i < kids.length; i++) {
            kids[i].toString(buf, depth + 1);
        }
    }

    public List invert() {
        Map hotSpots = new HashMap();
        invertTo(hotSpots);
        List l = new LinkedList(hotSpots.values());
        Collections.sort(l);
        return l;
    }

    private void invertTo(Map hotSpots) {
        HotSpot hotSpot = (HotSpot) hotSpots.get(stats.getName());
        if (hotSpot == null) {
            hotSpot = new HotSpot(stats.getName(), stats.getTotal(), stats.getCount());
            hotSpots.put(stats.getName(), hotSpot);
        } else {
            hotSpot.count += stats.getCount();
            hotSpot.total += stats.getTotal();
        }
        Iterator i = children.values().iterator();
        while (i.hasNext()) {
            CallGraph child = (CallGraph) i.next();
            hotSpot.total -= child.stats.getTotal();
            child.invertTo(hotSpots);
        }
    }

    public String toInvertedString() {
        List l = invert();
        Iterator i = l.iterator();
        StringBuffer buf = new StringBuffer();
        while (i.hasNext()) {
            HotSpot hotSpot = (HotSpot) i.next();
            if (hotSpot.getTotal() > 0) {
                buf.append(hotSpot.getTotal());
                buf.append("\t");
                buf.append(hotSpot.getCount());
                buf.append(" calls to ");
                buf.append(hotSpot.getName());
                buf.append("\n");
            }
        }
        return buf.toString();
    }
}
