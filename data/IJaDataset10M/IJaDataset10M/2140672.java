package net.sf.nebulacards.comm;

import net.sf.nebulacards.main.*;
import java.util.*;

/**
 * Manage a pool of games.  This class allows for sharing of games between
 * different protocol servers and for easily finding games that need more
 * players.
 * <p><b>This class depends on version 1.2 or higher of Java.</b>
 * @author James Ranson
 * @version 0.8
 */
public class GameRunnerPool {

    private LinkedList m_games = new LinkedList();

    private int m_limit = -1;

    private GameRunnerProvider m_provider;

    /**
	 * Constructor.
	 * @param provider A provider of new games (used when adding a player
	 * and all existing games are full).
	 */
    public GameRunnerPool(GameRunnerProvider provider) {
        m_provider = provider;
    }

    /**
	 * Add a new game to the pool.
	 */
    public synchronized void add(GameRunner gr) {
        m_games.add(gr);
    }

    /**
	 * Limit the number of games that may be stored in this pool.
	 * Currently stored games will not be effected, even if the new limit
	 * is below the number of stored games.
	 * @param n The maximum number of games, or negative if there is no maximum.
	 */
    public void setLimit(int n) {
        m_limit = n;
    }

    /**
	 * Get the maximum number of games.
	 * @return The maximum, or negative if there is no maximum.
	 */
    public int getLimit() {
        return m_limit;
    }

    /**
	 * Join a player to the first available game.
	 * @param target A communication link to the player.
	 * @param name The player's name.
	 * @param startNew Should we try to create a new game if all current
	 * games are full.  Even if this is true, failure may occur if the
	 * pool is full.
	 * @return The game runner to which the player was added, or <i>null</i>
	 * if no spots were available.
	 */
    public synchronized GameRunner joinAny(NebulaUi target, String name, boolean startNew) {
        ListIterator li = m_games.listIterator();
        GameRunner result = null;
        while (li.hasNext()) {
            GameRunner gr = (GameRunner) li.next();
            if (!gr.isAlive()) {
                li.remove();
            } else if (gr.howMany() == 0) {
                gr.interrupt();
            } else if (gr.add(target, name) >= 0) {
                result = gr;
                break;
            }
        }
        if (result == null && startNew && (m_games.size() < m_limit || m_limit < 0)) {
            GameRunner gr = m_provider.provideGameRunner();
            add(gr);
            if (gr.add(target, name) >= 0) {
                result = gr;
            }
        }
        return result;
    }
}
