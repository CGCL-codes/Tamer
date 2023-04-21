package civquest.util.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MultiHashMap<E, F> {

    private Map<E, Map<F, Integer>> map = new HashMap<E, Map<F, Integer>>();

    private boolean useMultiplicity = false;

    private boolean debugOn = false;

    private boolean weakValues = false;

    public MultiHashMap(boolean useMultiplicity, boolean weakValues, boolean debugOn) {
        this.useMultiplicity = useMultiplicity;
        this.weakValues = weakValues;
        this.debugOn = debugOn;
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(E key) {
        return map.containsKey(key);
    }

    public boolean containsPair(E key, F value) {
        return map.containsKey(key) && map.get(key).containsKey(value);
    }

    public void put(E key, F value) {
        put(key, value, 1);
    }

    private void put(E key, F value, int addMultiplicity) {
        if (!(map.containsKey(key))) {
            if (weakValues) {
                map.put(key, new WeakHashMap<F, Integer>());
            } else {
                map.put(key, new HashMap<F, Integer>());
            }
        }
        Map<F, Integer> valueMap = map.get(key);
        if (useMultiplicity) {
            if (valueMap.containsKey(value)) {
                int multiplicity = valueMap.get(value);
                multiplicity += addMultiplicity;
                valueMap.put(value, multiplicity);
            } else {
                valueMap.put(value, addMultiplicity);
            }
        } else {
            valueMap.put(value, 1);
        }
    }

    public void addAll(MultiHashMap<E, F> other) {
        Iterator<E> keyIterator = other.getKeyIterator();
        while (keyIterator.hasNext()) {
            E key = keyIterator.next();
            Iterator<F> valueIterator = other.get(key);
            while (valueIterator.hasNext()) {
                F currValue = valueIterator.next();
                put(key, currValue, other.getMultiplicity(key, currValue));
            }
        }
    }

    public void remove(E key, F value) {
        remove(key, value, 1);
    }

    private void remove(E key, F value, int removeMultiplicity) {
        if (map.containsKey(key)) {
            Map<F, Integer> valueMap = map.get(key);
            if (valueMap.containsKey(value)) {
                int oldMultiplicity = valueMap.get(value);
                int newMultiplicity = oldMultiplicity - removeMultiplicity;
                if (newMultiplicity > 0) {
                    valueMap.put(value, newMultiplicity);
                } else if (newMultiplicity == 0) {
                    valueMap.remove(value);
                    if (valueMap.isEmpty()) {
                        map.remove(key);
                    }
                } else {
                    valueMap.remove(value);
                    if (valueMap.isEmpty()) {
                        map.remove(key);
                    }
                    print("MultiHashMap says: When removing " + key + " --> " + value + " with multiplicity " + removeMultiplicity + ": Resulting multiplicity is " + newMultiplicity + " < 0!");
                }
            } else {
                print("MultiHashMap.remove says: Key " + key + " found, but " + "not value " + value + "!");
            }
        } else {
            print("MultiHashMap.remove says: Key " + key + " was not there.");
        }
    }

    public void removeAll(MultiHashMap<E, F> other) {
        Iterator<E> keyIterator = other.getKeyIterator();
        while (keyIterator.hasNext()) {
            E key = keyIterator.next();
            Iterator<F> valueIterator = other.get(key);
            while (valueIterator.hasNext()) {
                F currValue = valueIterator.next();
                remove(key, currValue, other.getMultiplicity(key, currValue));
            }
        }
    }

    public Iterator<F> get(E key) {
        return (map.containsKey(key) ? map.get(key).keySet().iterator() : null);
    }

    private int getMultiplicity(E key, F value) {
        return map.get(key).get(value);
    }

    public Iterator<E> getKeyIterator() {
        return map.keySet().iterator();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n");
        Iterator<E> keyIterator = getKeyIterator();
        while (keyIterator.hasNext()) {
            E currKey = keyIterator.next();
            buffer.append("( " + currKey + " --> ");
            Iterator<F> valueIterator = get(currKey);
            while (valueIterator.hasNext()) {
                F currValue = valueIterator.next();
                buffer.append(currValue + (useMultiplicity ? "(" + getMultiplicity(currKey, currValue) + "), " : ", "));
            }
            buffer.append(")\n");
        }
        return buffer.toString();
    }

    private void print(String message) {
        if (debugOn) {
            System.out.println(message);
        }
    }
}
