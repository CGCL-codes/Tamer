package com.customwars.client.action.unit;

import com.customwars.client.SFX;
import com.customwars.client.action.DirectAction;
import com.customwars.client.controller.CursorController;
import com.customwars.client.model.game.Game;
import com.customwars.client.model.gameobject.Locatable;
import com.customwars.client.model.gameobject.Unit;
import com.customwars.client.model.map.Location;
import com.customwars.client.ui.renderer.MapRenderer;
import com.customwars.client.ui.state.InGameContext;
import org.apache.log4j.Logger;

/**
 * Select the last unit on selectTile and make it the active unit in the game. This is the first action
 * for a unit.
 *
 * @author stefan
 */
public class SelectAction extends DirectAction {

    private static final Logger logger = Logger.getLogger(SelectAction.class);

    private InGameContext inGameContext;

    private MapRenderer mapRenderer;

    private CursorController cursorControl;

    private Game game;

    private final Location selectTile;

    public SelectAction(Location selectTile) {
        super("Select");
        this.selectTile = selectTile;
    }

    public void init(InGameContext inGameContext) {
        this.inGameContext = inGameContext;
        this.game = inGameContext.getObj(Game.class);
        this.mapRenderer = inGameContext.getObj(MapRenderer.class);
        this.cursorControl = inGameContext.getObj(CursorController.class);
    }

    protected void invokeAction() {
        Locatable locatable = selectTile.getLastLocatable();
        selectUnit((Unit) locatable);
        SFX.playSound("select");
        if (cursorControl.isTraversing()) {
            cursorControl.stopCursorTraversal();
        }
        inGameContext.setInputMode(InGameContext.INPUT_MODE.UNIT_SELECT);
    }

    private void selectUnit(Unit unit) {
        logger.debug("Selecting " + unit);
        game.setActiveUnit(unit);
        game.getMap().buildMovementZone(unit);
        mapRenderer.setActiveUnit(unit);
        mapRenderer.removeZones();
        mapRenderer.showMoveZone();
        mapRenderer.showArrows(true);
    }

    public void undo() {
        deselectActiveUnit();
        inGameContext.setInputMode(InGameContext.INPUT_MODE.DEFAULT);
    }

    private void deselectActiveUnit() {
        game.setActiveUnit(null);
        mapRenderer.setActiveUnit(null);
        mapRenderer.removeZones();
        mapRenderer.showArrows(false);
        cursorControl.moveCursor(selectTile);
    }
}
