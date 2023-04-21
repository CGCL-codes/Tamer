package com.imaginaryday.ec.main.nodes;

import com.imaginaryday.ec.gp.Node;
import com.imaginaryday.ec.main.RoboNode;
import com.imaginaryday.util.Stuff;

/**
 * <b>
 * User: jlowens<br>
 * Date: Nov 9, 2006<br>
 * Time: 11:04:31 PM<br>
 * </b>
 */
public class EnemyEnergy extends RoboNode {

    private static final long serialVersionUID = 8914134630683512656L;

    protected Node[] children() {
        return NONE;
    }

    public String getName() {
        return "enemyEnergy";
    }

    public Class getInputType(int id) {
        return null;
    }

    public Class getOutputType() {
        return Number.class;
    }

    public Object evaluate() {
        double energy = getOwner().getEnemyEnergy();
        assert !(Double.isNaN(energy) || Double.isInfinite(energy)) : "energy was bad!";
        assert Stuff.isReasonable(energy) : "unreasonable value: " + energy;
        return energy;
    }
}
