package failure.core.business.core;

import failure.core.business.unit.Unit;

/**
 *
 * @author Jonathan
 */
public class FightUnitsResult {

    private Unit atk;

    private Unit def;

    private int dmgAtk;

    private int dmgDef;

    public FightUnitsResult(Unit atk, Unit def) {
        this.atk = atk;
        this.def = def;
    }

    public void setDmgAtk(int dmgAtk) {
        this.dmgAtk = dmgAtk;
    }

    public void setDmgDef(int dmgDef) {
        this.dmgDef = dmgDef;
    }

    public Unit getAtk() {
        return atk;
    }

    public Unit getDef() {
        return def;
    }

    public int getDmgAtk() {
        return dmgAtk;
    }

    public int getDmgDef() {
        return dmgDef;
    }
}
