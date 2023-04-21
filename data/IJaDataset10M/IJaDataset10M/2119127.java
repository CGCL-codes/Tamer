package internal.civilization.city;

import internal.civilization.Civilization;
import internal.grid.cell.Cell;

public class Citizen {

    protected int production = 1;

    protected int commerce = 1;

    protected int food = 1;

    protected int morale = 1;

    protected int research = 50;

    private Cell currentCell;

    public Citizen(Cell cell) {
        this.currentCell = cell;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public int getProduction() {
        return production;
    }

    public int getCommerce() {
        return commerce;
    }

    public int getFood() {
        return food;
    }

    public int getMorale() {
        return morale;
    }

    public int getResearch() {
        return research;
    }

    public Citizen getCopy(Civilization civ) {
        Citizen output = new Citizen(civ.getGameGrid().getCell(currentCell.getCellID()));
        output.setCommerce(commerce);
        output.setFood(food);
        output.setMorale(morale);
        output.setProduction(production);
        output.setResearch(research);
        return output;
    }

    private void setProduction(int production) {
        this.production = production;
    }

    private void setCommerce(int commerce) {
        this.commerce = commerce;
    }

    private void setFood(int food) {
        this.food = food;
    }

    private void setMorale(int morale) {
        this.morale = morale;
    }

    private void setResearch(int research) {
        this.research = research;
    }
}
