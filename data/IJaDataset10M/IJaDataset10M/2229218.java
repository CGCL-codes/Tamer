package mrusanov.fantasyruler.game.event.uievent;

import mrusanov.fantasyruler.map.TerrainUnit;
import mrusanov.fantasyruler.objects.units.Ship;

public class UnloadingUnitUIEvent implements UIEvent {

    private final Ship ship;

    private final TerrainUnit target;

    private final int size;

    public UnloadingUnitUIEvent(Ship ship, TerrainUnit target, int size) {
        super();
        this.ship = ship;
        this.target = target;
        this.size = size;
    }

    public Ship getShip() {
        return ship;
    }

    public TerrainUnit getTarget() {
        return target;
    }

    public int getSize() {
        return size;
    }
}
