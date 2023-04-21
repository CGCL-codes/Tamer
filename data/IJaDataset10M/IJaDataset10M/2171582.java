package playground.fabrice.secondloc;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;
import org.matsim.gbl.Gbl;

public class MentalMap {

    private TreeMap<String, HashSet<CoolPlace>> places = new TreeMap<String, HashSet<CoolPlace>>();

    void learn(CoolPlace place) {
        String type = place.activity.getType();
        HashSet<CoolPlace> sp = places.get(type);
        if (sp == null) {
            sp = new HashSet<CoolPlace>();
            places.put(type, sp);
        }
        sp.add(place);
    }

    public CoolPlace getRandomCoolPlace() {
        Vector<CoolPlace> v = new Vector<CoolPlace>();
        for (HashSet<CoolPlace> tree : places.values()) v.addAll(tree);
        return v.get(Gbl.random.nextInt(v.size()));
    }

    public CoolPlace getRandomCoolPlace(String activityType) {
        HashSet<CoolPlace> sp = places.get(activityType);
        if (sp == null) return null;
        Vector<CoolPlace> v = new Vector<CoolPlace>();
        v.addAll(places.get(activityType));
        return v.get(Gbl.random.nextInt(v.size()));
    }
}
