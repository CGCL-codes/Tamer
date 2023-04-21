package edu.rice.cs.cunit.record.graph;

import java.util.*;
import java.util.List;
import java.io.PrintWriter;

/**
 * Creates and analyzes the wait graph.
 *
 * @author Mathias Ricken
 */
public class WaitGraph {

    /**
     * Map for fast lookup. Key is the unique ID, value is a MapEntry.
     */
    private HashMap<Long, MapEntry> _map;

    /**
     * List of list with cycles.
     */
    private List<List<ThreadInfo>> _cycles;

    /**
     * Helper class containing ThreadInfo and data for breadth-first search.
     */
    private static class MapEntry {

        public static enum Color {

            WHITE, GRAY, BLACK
        }

        private ThreadInfo _threadInfo;

        private Color _color;

        private long _distance;

        private Long _parentId;

        public MapEntry(ThreadInfo threadInfo, Color color, long distance, Long parentId) {
            _threadInfo = threadInfo;
            _color = color;
            _distance = distance;
            _parentId = parentId;
        }

        public Long getParentId() {
            return _parentId;
        }

        public void setParentId(Long parentId) {
            _parentId = parentId;
        }

        public ThreadInfo getThreadInfo() {
            return _threadInfo;
        }

        public void setThreadInfo(ThreadInfo threadInfo) {
            _threadInfo = threadInfo;
        }

        public Color getColor() {
            return _color;
        }

        public void setColor(Color color) {
            _color = color;
        }

        public long getDistance() {
            return _distance;
        }

        public void setDistance(long distance) {
            _distance = distance;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ");
            sb.append(_threadInfo.toString());
            if (_threadInfo != null) {
                sb.append(_threadInfo.toStringVerbose());
            }
            sb.append(", color ");
            sb.append(_color);
            return sb.toString();
        }
    }

    /**
     * Creates a new wait graph.
     * @param threads collection of threads to analyze
     * @param writer writer for output
     */
    public WaitGraph(Map<Long, ThreadInfo> threads, PrintWriter writer) {
        _cycles = new ArrayList<List<ThreadInfo>>();
        _map = new HashMap<Long, MapEntry>();
        for (ThreadInfo ti : threads.values()) {
            _map.put(ti.getThreadID(), new MapEntry(ti, MapEntry.Color.WHITE, 0, null));
        }
        for (ThreadInfo ti : threads.values()) {
            MapEntry me = _map.get(ti.getThreadID());
            if (me.getColor() == MapEntry.Color.WHITE) {
                bfs(ti, threads);
            }
        }
        if (_cycles.size() > 0) {
            writer.println("Deadlocks found:");
            int num = 1;
            for (List<ThreadInfo> cycle : _cycles) {
                writer.println("\tCycle " + (num++));
                for (ThreadInfo ti : cycle) {
                    writer.println("\t\tThread " + ti.toStringVerbose());
                }
            }
        }
    }

    /**
     * Returns list of list with cycles.
     * @return list of list with cycles
     */
    public List<List<ThreadInfo>> getCycles() {
        return _cycles;
    }

    /**
     * Performs a breadth-first search, starting with the specified thread.
     * @param ti thread to start with
     * @param threads map of threads to analyze
     */
    private void bfs(ThreadInfo ti, Map<Long, ThreadInfo> threads) {
        MapEntry me = _map.get(ti.getThreadID());
        me.setColor(MapEntry.Color.GRAY);
        PriorityQueue<MapEntry> q = new PriorityQueue<MapEntry>(10, new Comparator<MapEntry>() {

            public int compare(MapEntry o1, MapEntry o2) {
                return (o1.getDistance() < o2.getDistance()) ? -1 : ((o1.getDistance() == o2.getDistance()) ? 0 : -1);
            }
        });
        q.add(me);
        while (q.size() > 0) {
            me = q.poll();
            if (me.getThreadInfo().getContendedLockID() != null) {
                long contendedLockID = me.getThreadInfo().getContendedLockID();
                ThreadInfo owningThread = null;
                for (ThreadInfo otherThread : threads.values()) {
                    if (otherThread.getOwnedLockIDs().contains(contendedLockID)) {
                        owningThread = otherThread;
                        break;
                    }
                }
                if (owningThread != null) {
                    MapEntry owner = _map.get(owningThread.getThreadID());
                    if (owner.getColor() == MapEntry.Color.WHITE) {
                        owner.setColor(MapEntry.Color.GRAY);
                        owner.setDistance(me.getDistance() + 1);
                        owner.setParentId(me.getThreadInfo().getThreadID());
                        q.add(owner);
                    } else {
                        ArrayList<ThreadInfo> cycle = new ArrayList<ThreadInfo>();
                        cycle.add(owner.getThreadInfo());
                        MapEntry parent = me;
                        while ((parent != null) && (parent != owner)) {
                            cycle.add(parent.getThreadInfo());
                            parent = _map.get(parent.getParentId());
                        }
                        _cycles.add(cycle);
                    }
                }
            }
        }
    }
}
