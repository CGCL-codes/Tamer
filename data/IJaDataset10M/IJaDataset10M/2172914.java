package CH.ifa.draw.util.collections.jdk12;

import CH.ifa.draw.util.CollectionsFactory;
import java.util.*;

/**
 * @author  Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class CollectionsFactoryJDK12 extends CollectionsFactory {

    public CollectionsFactoryJDK12() {
    }

    public List createList() {
        return new ArrayList();
    }

    public List createList(Collection initList) {
        return new ArrayList(initList);
    }

    public List createList(int initSize) {
        return new ArrayList(initSize);
    }

    public Map createMap() {
        return new Hashtable();
    }

    public Map createMap(Map initMap) {
        return new Hashtable(initMap);
    }

    public Set createSet() {
        return new HashSet();
    }

    public Set createSet(Set initSet) {
        return new HashSet(initSet);
    }
}
