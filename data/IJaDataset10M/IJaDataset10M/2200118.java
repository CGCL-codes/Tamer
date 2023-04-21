package org.gcreator.pineapple.pinec.parser.tree;

/**
 *
 * @author luis
 */
public abstract class Leaf {

    public int line;

    public int col;

    public Leaf optimize() {
        return this;
    }
}
