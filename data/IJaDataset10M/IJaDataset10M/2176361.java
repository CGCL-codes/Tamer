package pcgen.core.utils;

import pcgen.util.HashMapToList;
import java.util.List;

/**
 * @author Tom Parker <thpr@sourceforge.net>
 *
 * This encapsulates a MapToList in a typesafe way (prior to java 1.5 having the
 * ability to do that with typed Collections)
 */
public class ListKeyMapToList {

    private final HashMapToList map = new HashMapToList();

    public ListKeyMapToList() {
    }

    public void addAllLists(ListKeyMapToList lcs) {
        map.addAllLists(lcs.map);
    }

    public void addAllToListFor(ListKey key, List list) {
        map.addAllToListFor(key, list);
    }

    public void addToListFor(ListKey key, Object value) {
        map.addToListFor(key, value);
    }

    public boolean containsListFor(ListKey key) {
        return map.containsListFor(key);
    }

    public List getListFor(ListKey key) {
        return map.getListFor(key);
    }

    public Object getElementInList(ListKey key, int i) {
        return map.getElementInList(key, i);
    }

    public void initializeListFor(ListKey key) {
        map.initializeListFor(key);
    }

    public boolean removeFromListFor(ListKey key, Object value) {
        return map.removeFromListFor(key, value);
    }

    public List removeListFor(ListKey key) {
        return map.removeListFor(key);
    }

    public int sizeOfListFor(ListKey key) {
        return map.sizeOfListFor(key);
    }

    public boolean containsInList(ListKey key, String value) {
        return map.containsInList(key, value);
    }
}
