package games.strategy.triplea.oddsCalculator.zengland;

public class UnitGroup implements Cloneable {

    private OCUnit unit;

    private int numUnits;

    private int totalHp;

    public UnitGroup(final OCUnit unit, final int numUnits) {
        super();
        setUnit(unit);
        setNumUnits(numUnits);
        setTotalHp(numUnits * unit.getMaxHP());
    }

    public int getNumUnits() {
        return numUnits;
    }

    public void setNumUnits(final int numUnits) {
        this.numUnits = numUnits;
        if (this.numUnits < 0) this.numUnits = 0;
    }

    public OCUnit getUnit() {
        return unit;
    }

    public void setUnit(final OCUnit unit) {
        this.unit = unit;
    }

    public void removeExtraHp() {
        this.totalHp--;
        if (this.totalHp < 0) this.totalHp = 0;
    }

    public void removeUnit() {
        this.totalHp--;
        if (this.totalHp < 0) this.totalHp = 0;
        setNumUnits(this.numUnits - 1);
        if (this.numUnits < 0) this.numUnits = 0;
    }

    public int rollUnitsAttack(final int numBoosted) {
        int hits = 0;
        final int maxHits = unit.getMaxHits();
        final int maxRolls = unit.getMaxRolls();
        int curAttValue = unit.getAttackValue() + 1;
        for (int i = 0; i < numUnits; i++) {
            if (i == numBoosted) curAttValue = unit.getAttackValue();
            for (int j = 0; j < maxHits; j++) {
                boolean gotAHit = false;
                for (int k = 0; k < maxRolls && !gotAHit; k++) {
                    final int roll = unit.rollAttack();
                    if (roll <= curAttValue) {
                        gotAHit = true;
                    }
                }
                if (gotAHit) hits++;
            }
        }
        return hits;
    }

    public int rollUnitsAttack() {
        int hits = 0;
        final int maxHits = unit.getMaxHits();
        final int maxRolls = unit.getMaxRolls();
        for (int i = 0; i < numUnits; i++) {
            for (int j = 0; j < maxHits; j++) {
                boolean gotAHit = false;
                for (int k = 0; k < maxRolls && !gotAHit; k++) {
                    final int roll = unit.rollAttack();
                    if (roll <= unit.getAttackValue()) {
                        gotAHit = true;
                    }
                }
                if (gotAHit) hits++;
            }
        }
        return hits;
    }

    public int rollUnitsDefend() {
        int hits = 0;
        for (int i = 0; i < numUnits; i++) {
            final int roll = unit.rollDefend();
            if (roll <= unit.getDefendValue()) hits++;
        }
        return hits;
    }

    @Override
    public String toString() {
        String unitGroup = "";
        unitGroup += this.getUnit().toString();
        unitGroup += " " + this.getNumUnits();
        return unitGroup;
    }

    public static void main(final String args[]) {
        final UnitGroup ug = new UnitGroup(OCUnit.newInf(), 10);
        System.out.println(ug);
        int defHits = 0;
        int attHits = 0;
        final int rounds = 1;
        for (int i = 0; i < rounds; i++) {
            defHits += ug.rollUnitsDefend();
            attHits += ug.rollUnitsAttack(5);
        }
        System.out.println("Avg Attack hits " + (float) attHits / (float) rounds);
        System.out.println("Avg Defense hits " + (float) defHits / (float) rounds);
    }

    public int getTotalHp() {
        return totalHp;
    }

    public void setTotalHp(final int totalHp) {
        this.totalHp = totalHp;
    }

    @Override
    public Object clone() {
        Object c = null;
        c = new UnitGroup(this.getUnit(), this.getNumUnits());
        return c;
    }
}
