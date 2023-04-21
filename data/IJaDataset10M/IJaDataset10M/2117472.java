package com.customwars.client.controller;

import com.customwars.client.SFX;
import com.customwars.client.action.ActionFactory;
import com.customwars.client.action.CWAction;
import com.customwars.client.action.ShowPopupMenuAction;
import com.customwars.client.action.unit.SelectAction;
import com.customwars.client.action.unit.ShowAttackZoneAction;
import com.customwars.client.model.game.Player;
import com.customwars.client.model.gameobject.City;
import com.customwars.client.model.gameobject.Unit;
import com.customwars.client.model.map.Location;
import com.customwars.client.model.map.Tile;
import com.customwars.client.ui.PopupMenu;
import com.customwars.client.ui.renderer.MapRenderer;
import com.customwars.client.ui.state.InGameContext;
import org.apache.log4j.Logger;

/**
 * Allows a human to control a unit
 * by using a gui
 *
 * @author stefan
 */
public class HumanUnitController extends UnitController {

    private static final Logger logger = Logger.getLogger(HumanUnitController.class);

    private final InGameContext inGameContext;

    private final MapRenderer mapRenderer;

    private PopupMenu menu;

    public HumanUnitController(Unit unit, InGameContext gameContext) {
        super(unit, gameContext);
        this.inGameContext = gameContext;
        this.mapRenderer = gameContext.getObj(MapRenderer.class);
    }

    public void handleAPress() {
        Tile selected = mapRenderer.getCursorLocation();
        Tile to = inGameContext.getClick(2);
        if (inGameContext.isUnitDropMode()) {
            dropUnit(selected);
        } else if (inGameContext.isUnitAttackMode()) {
            attack(selected, to);
        } else if (inGameContext.isRocketLaunchMode()) {
            launchRocket(selected, to);
        } else if (inGameContext.isUnitFlareMode() && unit.getAttackZone().contains(selected)) {
            fireFlare(selected, to);
        } else if (canSelect(selected)) {
            select(selected);
        } else if (canShowMenu(selected)) {
            showContextMenu(selected);
        } else {
            cancelPressed();
        }
    }

    private void dropUnit(Tile selected) {
        int unitInTransportIndex = menu.getCurrentItem();
        Unit unitInTransport = inGameContext.getUnitInTransport(unitInTransportIndex);
        inGameContext.addDropLocation(selected, unitInTransport);
        this.menu = new UnitMenuBuilder(this, unit, inGameContext, selected).getMenu();
        showMenu(selected);
    }

    private void attack(Tile selected, Location to) {
        if (canAttackUnit(selected)) {
            attackUnit(selected, to);
        } else if (canAttackCity(selected)) {
            attackCity(selected, to);
        }
    }

    private void attackUnit(Location selected, Location to) {
        Unit defender = map.getUnitOn(selected);
        CWAction attackAction = ActionFactory.buildUnitVsUnitAttackAction(unit, defender, to);
        inGameContext.doAction(attackAction);
    }

    private void attackCity(Location selected, Location to) {
        City city = map.getCityOn(selected);
        CWAction attackAction = ActionFactory.buildUnitVsCityAttackAction(unit, city, to);
        inGameContext.doAction(attackAction);
    }

    private void launchRocket(Location selected, Location to) {
        City city = map.getCityOn(inGameContext.getClick(2));
        CWAction launchRocket = ActionFactory.buildLaunchRocketAction(unit, city, selected);
        inGameContext.doAction(launchRocket);
    }

    private void fireFlare(Location selected, Location to) {
        CWAction fireFlare = ActionFactory.buildFireFlareAction(unit, to, selected);
        inGameContext.doAction(fireFlare);
    }

    private void select(Tile selected) {
        inGameContext.clearClickHistory();
        inGameContext.clearUndoHistory();
        inGameContext.registerClick(1, selected);
        inGameContext.doAction(new SelectAction(selected));
    }

    private boolean canShowMenu(Tile selected) {
        Unit activeUnit = game.getActiveUnit();
        return activeUnit != null && activeUnit.isWithinMoveZone(selected);
    }

    private void showContextMenu(Tile selected) {
        inGameContext.registerClick(2, selected);
        this.menu = new UnitMenuBuilder(this, unit, inGameContext, selected).getMenu();
        showMenu(selected);
    }

    private void showMenu(Tile selected) {
        if (menu.atLeastHasOneItem()) {
            inGameContext.doAction(new ShowPopupMenuAction(menu, selected));
        } else {
            logger.warn("No menu items to show");
        }
    }

    private void cancelPressed() {
        SFX.playSound("cancel");
        inGameContext.undo();
    }

    public void handleBPress() {
        Tile cursorLocation = mapRenderer.getCursorLocation();
        Unit selectedUnit = map.getUnitOn(cursorLocation);
        Player activePlayer = game.getActivePlayer();
        if (isUnitVisibleTo(activePlayer) && isUnitOn(cursorLocation) && selectedUnit.canFire()) {
            showAttackZone(selectedUnit);
        }
    }

    private void showAttackZone(Unit selectedUnit) {
        inGameContext.doAction(new ShowAttackZoneAction(selectedUnit));
    }
}
