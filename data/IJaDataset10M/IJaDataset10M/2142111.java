package uima.taes.interestingness.graphHelpers;

import java.util.TreeMap;
import java.util.TreeSet;

public class MapSetHelper {

    protected TreeMap<Object, Integer> count = new TreeMap<Object, Integer>();

    protected TreeMap<Object, Integer> interesting = new TreeMap<Object, Integer>();

    protected TreeMap<Object, TreeSet<Object>> map = new TreeMap<Object, TreeSet<Object>>();

    public static int INTERESTING = 0;

    public static int VISITED = 1;

    public void mark(Object object, int type) {
        TreeMap<Object, Integer> stat = null;
        if (type == VISITED) stat = count; else stat = interesting;
        if (stat.get(object) == null) {
            stat.put(object, 1);
        } else {
            int value = (Integer) stat.get(object);
            stat.put(object, value + 1);
        }
    }

    public double getInterestingness(Object object) {
        Integer visited = (Integer) count.get(object);
        Integer interestingness = (Integer) interesting.get(object);
        if (visited == null) return 0.5;
        if (interestingness == null) return 0.0;
        return (double) interestingness.doubleValue() / (double) visited.doubleValue();
    }

    public void addNode(Object key, Object value) {
        TreeSet<Object> set = map.get(key);
        if (set == null) {
            set = new TreeSet<Object>();
            map.put(key, set);
        }
        set.add(value);
    }

    public boolean areConnected(Object object1, Object object2) {
        TreeSet<Object> entities1 = map.get(object1);
        TreeSet<Object> entities2 = map.get(object2);
        if (entities1 == null || entities2 == null) return false;
        for (Object i : entities1) {
            if (entities2.contains(i)) return true;
        }
        return false;
    }
}
