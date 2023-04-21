package server.gameplay;

import java.awt.Point;
import java.util.List;

/**
 * house class, where population grows.
 * 
 * @author Torbj�rn
 * 
 */
public class House implements Buildings {

    private int buildLevel;

    private float timeMultiplier = 0.075f;

    private long reduceTime;

    public House() {
        this((int) 0);
    }

    public House(int level) {
        this.buildLevel = level;
        reduceTime = System.currentTimeMillis();
    }

    public float growth(float curRes) {
        switch(buildLevel) {
            case 0:
                if (curRes <= 20f) return 0.198f * timeMultiplier;
                return 0f;
            case 1:
                if (curRes <= 40f) return 0.296f * timeMultiplier;
                return 0f;
            case 2:
                if (curRes <= 60f) return 0.444f * timeMultiplier;
                return 0f;
            case 3:
                if (curRes <= 80f) return 0.667f * timeMultiplier;
                return 0f;
            case 4:
                if (curRes <= 100f) return 1 * timeMultiplier;
                return 0f;
            default:
                return 0f;
        }
    }

    public int getLevel() {
        return buildLevel;
    }

    public int upgradeCost() {
        switch(buildLevel) {
            case 0:
                return 5;
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 999;
            default:
                return 99999;
        }
    }

    public boolean isUpgradeable(float res) {
        return (buildLevel < 4) && res >= upgradeCost();
    }

    public void addLevel() {
        buildLevel++;
    }

    /**
	 * 
	 * @param attackMorale
	 *            attackers morale (0-5)
	 * @param ownMorale
	 *            own morale (0-5)
	 * @param attackArmory
	 *            attackers number of armory's
	 * @return true if successful defense
	 */
    public float defend(int attackMorale, int ownMorale, int attackArmory) {
        float reduce = (((float) (attackMorale - ownMorale)) / 5f) + ((float) (attackArmory - buildLevel) / 4f);
        if (reduce < 0) {
            reduce = Math.abs(1 / (reduce - 1));
        } else {
            reduce++;
        }
        return reduce;
    }

    public void setBuildLevel(int n) {
        buildLevel = n;
    }

    public House clone() {
        return new House(buildLevel);
    }

    public House clone(int level) {
        return new House(level);
    }

    public void reduceLevel() {
        if (System.currentTimeMillis() - reduceTime > 100 && buildLevel > 0) {
            buildLevel--;
            reduceTime = System.currentTimeMillis();
        }
    }

    public void InRange(Point pos, List<Unit> units, ServerPlayerPackage owner) {
    }
}
