package jpm.closeAction.logic;

import javax.xml.transform.Source;
import jpm.closeAction.logic.util.DamageTable;
import jpm.closeAction.logic.util.Die;
import jpm.closeAction.logic.util.DieRoll;
import jpm.closeAction.logic.vessel.Vessel;

public class Gunfire {

    Vessel source;

    Vessel target;

    ShotType shotType;

    boolean dismantling;

    DieRoll dieRoll = new DieRoll("d0");

    Damage getDamage(int reduction) {
        dieRoll.add(Die.d6);
        dieRoll.addModifier("reduction", reduction);
        return DamageTable.getDamage(dieRoll);
    }

    private static float[][] gf = new float[][] { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, .75f, .75f, .75f, .75f, .75f, .5f, .5f, 0, 0, 0 }, { 2, 2, 2, 1.5f, 1, .5f, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    private int getGF(int range) {
        range--;
        float _gf = source.getLg() * gf[0][range] + source.getMg() * gf[1][range] + source.getCr() * gf[2][range];
        return (int) Math.ceil(_gf);
    }
}
