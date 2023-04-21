package wsim.logic.reference;

/**
 * @author admin
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class HitTable {

    private int hull = 0;

    private int crew = 0;

    private int gun = 0;

    private int rigging = 0;

    private int criticalPower = 0;

    private boolean criticalTarget = false;

    public HitTable(int hull, int gun, int rigging, int crew) {
        this.hull = hull;
        this.crew = crew;
        this.gun = gun;
        this.rigging = rigging;
    }

    public HitTable(int power, boolean hull, int roll) {
        this.criticalPower = power + roll;
        this.criticalTarget = hull;
    }

    public HitTable() {
    }

    public int getCrew() {
        return this.crew;
    }

    public int getGun() {
        return this.gun;
    }

    public int getHull() {
        return this.hull;
    }

    public int getRigging() {
        return this.rigging;
    }

    public int getCriticalPower() {
        return this.criticalPower;
    }

    public boolean isCriticalTarget() {
        return this.criticalTarget;
    }
}
