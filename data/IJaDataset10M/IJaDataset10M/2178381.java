package com.usoog.tdcore.pathfinder;

import com.usoog.commons.gamecore.gamegrid.GameGrid;
import com.usoog.commons.gamecore.pathfinder.Pathfinder;
import com.usoog.tdcore.creep.Creep;
import com.usoog.tdcore.creep.CreepData;
import com.usoog.tdcore.gamemanager.TDGameManager;
import com.usoog.tdcore.gamestate.TDGameState;
import com.usoog.tdcore.player.TDPlayer;
import com.usoog.tdcore.tile.TDTile;
import com.usoog.tdcore.tower.Tower;

/**
 * A TDPathfinder handles how a creep moves over the board.
 * This can be implemented by a fixed path in games like
 *	HexTD, or a variable path in games like GemTD.
 *
 * @author Jimmy Axenhus
 * @author Hylke van der Schaaf
 * @param <C> The Creep type to use.
 * @param <CD> The data-type of the creeps used in this game, see {@link com.usoog.tdcore.creep.Creep#init(com.usoog.tdcore.gamestate.TDGameState, com.usoog.tdcore.creep.CreepData)  Creep}.
 * @param <To> The Tower type to use.
 * @param <Ti> The TDTile type to use.
 * @param <P> The TDPlayer type to use.
 * @param <Gr> The TDGameGrid type to use.
 * @param <GM> The TDGameManager type to use.
 * @param <GS> The TDGameState type to use.
 */
public interface TDPathfinder<C extends Creep<C, CD, To, Ti, P, Gr, GM, GS>, CD extends CreepData, To extends Tower<C, CD, To, Ti, P, Gr, GM, GS>, Ti extends TDTile<C, CD, To, Ti, P, Gr, GM, GS>, P extends TDPlayer<C, CD, To, Ti, P, Gr, GM, GS>, Gr extends GameGrid<Ti, P, Gr, GM, GS>, GM extends TDGameManager<C, CD, To, Ti, P, Gr, GM, GS>, GS extends TDGameState<C, CD, To, Ti, P, Gr, GM, GS>> extends Pathfinder<Ti, P, Gr, GM, GS> {

    /**
	 * The owner of a TDPathfinder is the TDPlayer that can create creeps that use
	 * this TDPathfinder to find their target.
	 *
	 * @return The TDPlayer that owns this TDPathfinder.
	 */
    public P getOwner();

    /**
	 * The target of a TDPathfinder is the TDPlayer that is that target of the
	 * creeps that use this TDPathfinder.
	 *
	 * @return The TDPlayer that is targeted by this TDPathfinder.
	 */
    public P getTarget();
}
