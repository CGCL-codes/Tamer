package civquest;

import java.util.Vector;

/** much is missing here still.
 * 
 * Changes:
 *   -produced: The amount of production points produced so far. When produced reaches a buildings (or something else) requiredPoints
 *              it produces and sets produces to zero again.
 *   - Added a check to make sure you don't add the same building twice.
 *   - Added removeBuilding()
 *   - Added discontent, content and happy citizens to the total population.
 *
 * Needed changes:
 *   - Need a super class (abstract) for all items that can be produced in towns, so far that accounts for buildings and units.
 *     Since the variable requiredPoints, Civilization and town are common for a lot of things it would be logic to create an abstract class.
 */
public class Town {

    private String name;

    private Vector buildings, units;

    private boolean townContent;

    private int scientist, business, entertainers, discontent, content, happy;

    private int trade, production, food, stock, produced;

    private int pollution;

    private int xpos, ypos;

    public Town(String name, int xpos, int ypos) {
        this.name = name;
        this.xpos = xpos;
        this.ypos = ypos;
        buildings = new Vector();
        units = new Vector();
    }

    public void addUnit(Unit unit) {
        units.addElement(unit);
    }

    public void addBuilding(Building building) {
        if (!buildings.contains(building)) buildings.addElement(building);
    }

    public void removeBuilding(Building building) {
        buildings.removeElement(building);
    }

    public int getTotalPopulation() {
        return scientist + business + entertainers + discontent + content + happy;
    }
}
