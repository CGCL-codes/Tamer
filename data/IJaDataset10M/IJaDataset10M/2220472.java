package mp3.reproductor.restrictions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class RestrictionMap {

    /**
     * Each entry should contain a player location, and a list of restrictions in order
     * to use that player. If any restriction isn't valid, that player shouldn't be used.
     */
    private Map<String, List<PlayerRestriction>> map;

    public RestrictionMap() {
        map = new HashMap<String, List<PlayerRestriction>>();
    }

    public synchronized Set<String> getPlayerList() {
        return map.keySet();
    }

    public synchronized void putEntry(String player, List<PlayerRestriction> lista) {
        map.put(player, lista);
    }

    public synchronized Set<String> getValidPlayerList() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("Checking valid players:\n");
        Set<String> keys = map.keySet();
        Set<String> resultSet = new HashSet<String>();
        for (String key : keys) {
            boolean valid = true;
            sb.append("\t").append(key).append(" : ");
            List<PlayerRestriction> restrictions = map.get(key);
            for (PlayerRestriction r : restrictions) {
                if (!r.isValid(key)) {
                    valid = false;
                    sb.append("[FAIL] -> ").append(r.generateStringRestriction()).append("\n");
                    break;
                }
            }
            if (valid) {
                sb.append("[ok]\n");
                resultSet.add(key);
            }
        }
        Logger.getLogger(RestrictionMap.class.getName()).info(sb.toString());
        return resultSet;
    }

    public synchronized List<PlayerRestriction> getRestrictions(String player) {
        return map.get(player);
    }
}
