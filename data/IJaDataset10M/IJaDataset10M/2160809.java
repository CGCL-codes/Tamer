package jswisstour.ranker;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import jswisstour.Game;
import jswisstour.Player;

public interface Ranker<E extends Comparable<E>> {

    Map<Player, Rank> rank(List<Player> players, Collection<Game<E>> games);
}
