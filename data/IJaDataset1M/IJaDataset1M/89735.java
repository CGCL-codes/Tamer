package com.imaginaryday.ec.main.nodes;

import com.imaginaryday.ec.gp.Node;
import com.imaginaryday.ec.main.RoboNode;
import com.imaginaryday.util.VectorUtils;
import org.jscience.mathematics.vectors.Vector;
import org.jscience.mathematics.vectors.VectorFloat64;
import java.util.Random;

/**
 * <b>
 * User: jlowens<br>
 * Date: Nov 12, 2006<br>
 * Time: 5:40:19 PM<br>
 * </b>
 */
public class VectorConstant extends RoboNode {

    private static final Random rand = new Random();

    VectorFloat64 val;

    private static final long serialVersionUID = 7848819907141991753L;

    public VectorConstant(double x, double y) {
        this.val = VectorFloat64.valueOf(x, y);
    }

    public VectorConstant(VectorFloat64 val) {
        this.val = val;
    }

    public VectorConstant() {
        this.val = VectorUtils.randomVector();
    }

    @Override
    public <T extends Node> T copy() {
        return (T) new VectorConstant(val);
    }

    protected Node[] children() {
        return NONE;
    }

    public String getName() {
        return "vectorConst";
    }

    public Class getInputType(int id) {
        return null;
    }

    public Class getOutputType() {
        return Vector.class;
    }

    @Override
    public Object evaluate() {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    protected String getConstructorParam() {
        return Double.toString(val.getValue(0)) + "," + Double.toString(val.getValue(1));
    }
}
