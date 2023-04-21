package net.sourceforge.arbaro.tree;

import net.sourceforge.arbaro.transformation.*;

/**
 * Segments with helical curving or nonlinearly changing radius
 * can be broken into subsegments. Normal segments consist of only
 * one subsegment.
 * 
 * @author Wolfram Diestel
 */
public class Subsegment {

    public Vector pos;

    public double height;

    public double rad;

    public Subsegment(Vector p, double r, double h) {
        pos = p;
        rad = r;
        height = h;
    }

    public boolean traverseStem(StemTraversal traversal) throws TraversalException {
        return traversal.visitSubsegment(this);
    }
}
