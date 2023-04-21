package source.model;

import java.util.ArrayList;
import source.model.type.*;

public class Army extends Unit {

    ArrayList<Unit> units;

    private Movement move;

    public Army() {
        super(UnitType.ARMY);
        units = new ArrayList<Unit>();
        move = new Movement();
    }

    public Army(UnitType u) {
        super(UnitType.ARMY);
        units = new ArrayList<Unit>();
    }

    public void add(Unit u) {
        if (u.getType() == UnitType.ARMY) add(((Army) u).toUnitArray()); else units.add(u);
        move.setRate(0.0f);
    }

    public void add(Unit[] unitsToAdd) {
        for (Unit u : unitsToAdd) {
            if (u.getType() == UnitType.ARMY) add(((Army) u).toUnitArray()); else {
                units.add(u);
            }
        }
        move.setRate(0.0f);
    }

    public int numUnitsInArmy() {
        return units.size();
    }

    public GameObject[] toGameObjectArray() {
        GameObject[] gos = new GameObject[units.size()];
        units.toArray(gos);
        return gos;
    }

    public Unit[] toUnitArray() {
        Unit[] unit = new Unit[units.size()];
        units.toArray(unit);
        return unit;
    }

    public int mvRate() {
        return (int) move.rate;
    }

    public float mvLeft() {
        return move.left;
    }

    public int getView() {
        int maxView = 0;
        for (Unit u : units) {
            if (u.getView() > maxView) maxView = u.getView();
        }
        return maxView;
    }

    public int armor() {
        int totArmor = 0;
        for (Unit u : units) {
            totArmor += u.armor();
        }
        return totArmor;
    }

    public int oDamage() {
        int totDmg = 0;
        for (Unit u : units) {
            totDmg += u.oDamage();
        }
        return totDmg;
    }

    public int dDamage() {
        int totDmg = 0;
        for (Unit u : units) {
            totDmg += u.dDamage();
        }
        return totDmg;
    }

    public int currentHealth() {
        int totHP = 0;
        for (Unit u : units) {
            totHP += u.currentHealth();
        }
        return totHP;
    }

    public int maxHealth() {
        int totHP = 0;
        for (Unit u : units) {
            totHP += u.maxHealth();
        }
        return totHP;
    }

    public void clearMovement() {
        move.setLeft(0.0f);
    }

    public boolean move(float mvPoints) {
        return move.move(mvPoints);
    }

    public void resetMovement() {
        move.reset();
    }

    private class Movement {

        private float rate, left;

        public Movement() {
            rate = findMinRate();
            left = 0;
        }

        public void setRate(float r) {
            rate = findMinRate();
        }

        public void setLeft(float f) {
            left = f;
        }

        public float getLeft() {
            return left;
        }

        public void reset() {
            left = rate;
        }

        public boolean move(float dec) {
            if (left <= 0) return false;
            left -= dec;
            return true;
        }

        public int findMinRate() {
            int lowestRate = Integer.MAX_VALUE;
            for (Unit u : units) {
                if (u.mvRate() < lowestRate) lowestRate = u.mvRate();
            }
            return lowestRate;
        }
    }

    public boolean modHealth(int modAmmount) {
        double ranNum = Math.random();
        int target = (int) (ranNum * (double) units.size());
        if (!units.get(target).modHealth(modAmmount)) units.remove(target);
        if (units.size() == 0) return false;
        return true;
    }
}
