package com.imaginaryday.ec.main.nodes;

import com.imaginaryday.ec.gp.Node;
import com.imaginaryday.ec.main.RoboNode;
import com.imaginaryday.util.Stuff;

/**
 * <b>
 * User: jlowens<br>
 * Date: Nov 9, 2006<br>
 * Time: 10:29:10 PM<br>
 * </b>
 */
public class BulletBearing extends RoboNode {

    private static final long serialVersionUID = -822469774444982365L;

    protected Node[] children() {
        return NONE;
    }

    public String getName() {
        return "bulletBearing";
    }

    public Class getInputType(int id) {
        return null;
    }

    public Class getOutputType() {
        return Number.class;
    }

    public Object evaluate() {
        double b = Stuff.clampZero(getOwner().getBulletBearing());
        assert !(Double.isNaN(b) || Double.isInfinite(b)) : "bearing is bad!";
        assert Stuff.isReasonable(b) : "unreasonable value: " + b;
        assert (Math.abs(b) >= -0.0 && Math.abs(b) <= 2 * Math.PI) : "bearing " + b + " is out of bounds!";
        double val = Stuff.modHeading(b + getOwner().getHeadingRadians());
        assert Stuff.isReasonable(val) : "unreasonable value: " + val;
        return val;
    }
}
