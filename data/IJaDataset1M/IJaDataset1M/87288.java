package net.sourceforge.combean.base;

import net.sourceforge.combean.interfaces.base.IndexMapping;

/**
 * A linear mapping f(x) = cx + t.
 * 
 * @author schickin
 *
 */
public class LinearMapping implements IndexMapping {

    private int scale = 1;

    private int offset = 0;

    /**
     * Constructor.
     * 
     * @param scale the scaling factor
     * @param offset the constant, additive offset
     */
    public LinearMapping(int scale, int offset) {
        this.scale = scale;
        this.offset = offset;
    }

    public int mapIndex(int origIndex) {
        return this.scale * origIndex + this.offset;
    }
}
