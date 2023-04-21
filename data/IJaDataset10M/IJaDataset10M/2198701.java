package org.gamegineer.client.core.system;

import java.util.Collection;
import org.gamegineer.game.ui.system.IGameSystemUi;

/**
 * A source of game system user interfaces.
 * 
 * <p>
 * This interface is intended to be implemented but not extended by clients.
 * </p>
 */
public interface IGameSystemUiSource {

    public Collection<IGameSystemUi> getGameSystemUis();
}
