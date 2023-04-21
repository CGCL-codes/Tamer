package mrusanov.fantasyruler.game.tasks;

import mrusanov.fantasyruler.game.event.uievent.UIEvent;
import mrusanov.fantasyruler.game.event.uievent.UnloadingUnitUIEvent;
import mrusanov.fantasyruler.game.mvc.gamefield.controller.GameFieldController;
import mrusanov.fantasyruler.objects.units.Ship;

public class UnitUnloadingTask extends AbstractGameFieldTask {

    private final Ship ship;

    private final int size;

    public UnitUnloadingTask(GameFieldController gameFieldController, Ship ship, int size) {
        super(gameFieldController);
        this.ship = ship;
        this.size = size;
    }

    @Override
    protected UIEvent buildUIEvent(int x, int y) {
        return new UnloadingUnitUIEvent(ship, getTerrainUnitByPixelCoords(x, y), size);
    }
}
