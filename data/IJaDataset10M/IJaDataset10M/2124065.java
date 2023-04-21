package starcraft.gamemodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import starcraft.gamemodel.Player;
import starcraft.gamemodel.references.PlanetRef;
import starcraft.gamemodel.references.PlayerRef;

/**
 * Tools for randomly drawing and shuffling (Planets, Cards).
 */
public class RandomTools {

    /**
	 * Draws planets for each player.
	 */
    public static Map<PlayerRef, List<Planet>> drawPlanets(List<Player> players, int planetsPerPlayer, Map<PlanetRef, Planet> allPlanets) {
        HashMap<PlayerRef, List<Planet>> result = new HashMap<PlayerRef, List<Planet>>();
        List<Planet> toDrawFrom = new ArrayList<Planet>(allPlanets.values());
        for (int i = 0; i < planetsPerPlayer; i++) {
            if (toDrawFrom.isEmpty()) {
                throw new RuntimeException("No planets to draw from.");
            }
            for (Player player : players) {
                List<Planet> list = result.get(player.getID());
                if (list == null) {
                    list = new ArrayList<Planet>();
                    result.put(player.getID(), list);
                }
                int index = (int) Math.round(Math.random() * (toDrawFrom.size() - 1));
                Planet drawn = toDrawFrom.remove(index);
                list.add(drawn);
            }
        }
        return result;
    }

    /**
	 * Shuffles the lsit.
	 * Included in this class to keep all 'random functions' together.  
	 */
    public static void shuffle(List<?> list) {
        if (list != null) Collections.shuffle(list);
    }
}
