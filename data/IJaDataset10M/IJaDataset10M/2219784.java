package free.jin;

import free.jin.event.GameListListenerManager;

/**
 * An extension of the <code>Connection</code> interface which adds support for
 * game lists.
 */
public interface GameListConnection extends Connection {

    /**
   * Returns the GameListListenerManager via which you can register and
   * unregister GameListListeners.
   */
    GameListListenerManager getGameListListenerManager();
}
