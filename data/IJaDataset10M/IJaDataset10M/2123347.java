package wood.model.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import static lawu.util.iterator.Iterators.*;
import lawu.util.Pair;
import lawu.util.iterator.UniversalIterator;
import wood.model.map.Map.Tile;

/**
 * This returns a set of visible tiles based on a central location... used
 * for Fog of War
 * 
 * @author Nikolas Wolfe
 */
public class VisibleTilesIterator {

    private final ArrayList<Tile> arr;

    public VisibleTilesIterator(int range, Tile startTile) {
        arr = new ArrayList<Tile>();
        Queue<Pair<Tile, Integer>> q = new LinkedList<Pair<Tile, Integer>>();
        q.add(new Pair<Tile, Integer>(startTile, 0));
        arr.add(startTile);
        do {
            Pair<Tile, Integer> cur = q.poll();
            if (cur.getSecond() == range) continue;
            for (Tile cand : cur.getFirst().getAdjacentTiles()) if (!arr.contains(cand)) {
                arr.add(cand);
                q.add(new Pair<Tile, Integer>(cand, cur.getSecond() + 1));
            }
        } while (!q.isEmpty());
    }

    public UniversalIterator<Tile> getTiles() {
        return iterator(arr);
    }
}
