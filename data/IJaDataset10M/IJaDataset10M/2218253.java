package fr.uha.ensisa.ir.walther.milcityblue.core.buildings;

import fr.uha.ensisa.ir.walther.milcityblue.core.Building;
import fr.uha.ensisa.ir.walther.milcityblue.core.Cell;
import fr.uha.ensisa.ir.walther.milcityblue.core.Config;
import fr.uha.ensisa.ir.walther.milcityblue.core.Size;
import fr.uha.ensisa.ir.walther.milcityblue.core.Weapon;

public class AirField extends Building {

    public AirField(int x, int y, int r) {
        super();
        this.cells.push(new Cell(Cell.CELL_AIRFIELD_1, x, y));
        this.cells.push(new Cell(Cell.CELL_AIRFIELD_2, x, y + 1));
        this.cells.push(new Cell(Cell.CELL_AIRFIELD_3, x, y + 2));
    }

    public int getConstructionCost() {
        return Config.CONSTRUCTION_COST_AIRFIELD;
    }

    public int getConstructionTime() {
        return Config.CONSTRUCTION_TIME_AIRFIELD;
    }

    public int getEnergy() {
        return Config.ENERGY_AIRFIELD;
    }

    public int getProductionTime() {
        return Config.PRODUCTION_TIME_AIRCRAFT;
    }

    public int getProductionType() {
        return Weapon.WEAPON_AIRCRAFT;
    }

    public Size getSize() {
        return new Size(Building.SIZE_AIRFIELD_X, Building.SIZE_AIRFIELD_Y);
    }

    public int getType() {
        return Building.BUILDING_AIRFIELD;
    }
}
