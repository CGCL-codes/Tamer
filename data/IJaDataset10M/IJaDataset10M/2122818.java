package org.nomicron.suber.module;

import org.nomicron.suber.model.object.Link;
import org.nomicron.suber.model.object.Player;
import org.nomicron.suber.model.object.Turn;
import java.util.List;

/**
 * Module for the game state.
 */
public class GameStateModule extends SuberModule {

    /**
     * Get the sidebar links associated with this module based on the specified parameters.
     *
     * @param inTurn   Turn to get links for
     * @param inPlayer Player who is currently logged in
     * @return List of links for the side bar.
     */
    @Override
    public List<Link> getLinks(Turn inTurn, Player inPlayer) throws Exception {
        return getGameState().getLinkList();
    }
}
