package world.buildings;

import geometry.QuadArea;

public class Building {

    public QuadArea area;

    public int height;

    public int population;

    public boolean beenDrawn;

    public boolean beendrawn2D;

    /**
	 * Creates a logical building object, defined by its spatial positioning, 
	 * height and the population which resides in it.
	 * @param area the area which the building occupies
	 * @param height the height of the building
	 * @param population the number of residents of the building
	 */
    public Building(QuadArea area, int height, int population) {
        this.area = area;
        this.height = height;
        this.population = population;
    }

    public void beenDrawn(boolean b) {
        beenDrawn = b;
    }

    public boolean beenDrawn() {
        return beenDrawn;
    }
}
