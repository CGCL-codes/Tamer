package net.slashie.expedition.ui.console;

import net.slashie.expedition.town.Building;
import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.libjcsi.textcomponents.ListItem;
import net.slashie.libjcsi.textcomponents.MenuItem;

public class BuildingMenuItem implements MenuItem, ListItem {

    private Building building;

    private int quantity;

    public BuildingMenuItem(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }

    public String getRow() {
        return "  " + building.getLongDescription();
    }

    public String getMenuDescription() {
        return "[" + quantity + "] " + building.getDescription() + " (" + (building.getWoodCost() * quantity) + " wood, " + (building.getBuildTimeCost() * quantity) + " workpower)";
    }

    public String getGroupClassifier() {
        return "DITTO";
    }

    public void add() {
        quantity++;
    }

    public void remove() {
        if (quantity > 0) quantity--;
    }

    public int getQuantity() {
        return quantity;
    }

    public char getMenuChar() {
        return ' ';
    }

    public int getMenuColor() {
        return ConsoleSystemInterface.BLUE;
    }

    public char getIndex() {
        return getMenuChar();
    }

    public int getIndexColor() {
        return getMenuColor();
    }
}
