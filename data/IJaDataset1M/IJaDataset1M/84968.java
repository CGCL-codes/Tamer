package net.sf.bddbddb;

/**
 * A Constant is a special kind of variable that represents a constant value.
 * 
 * @author John Whaley
 * @version $Id: Constant.java,v 1.1 2004/10/16 02:45:21 joewhaley Exp $
 */
public class Constant extends Variable {

    /**
     * Value of constant.
     */
    protected long value;

    /**
     * Create a constant with the given value.
     * 
     * @param value  value of constant
     */
    public Constant(long value) {
        super(Long.toString(value));
        this.value = value;
    }

    /**
     * Returns the value of this constant.
     * 
     * @return value
     */
    public long getValue() {
        return value;
    }
}
