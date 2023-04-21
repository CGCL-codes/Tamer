package com.vividsolutions.xdo.xsi;

/**
 * <p>
 * This represents an abstract collection of xml element definitions within a
 * Schema.
 * </p>
 * 
 * <p>
 * To avoid multiple type checks, a group mask was include, as described below.
 * </p>
 *
 * @author dzwiers
 */
public class ElementGrouping extends Identifiable {

    public static final int UNDEFINED = 0;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int ELEMENT = 1;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int GROUP = 2;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int ANY = 4;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int SEQUENCE = 8;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int CHOICE = 16;

    /**
     * ElementGrouping mask to determine the type of ElementGrouping
     * represented. This is intended to  reduce the use of the instanceof
     * operand,  increasing performance.
     */
    public static final int ALL = 32;

    public static final int UNBOUNDED = Integer.MAX_VALUE;

    protected ElementGrouping(int grouping) {
        this.grouping = grouping;
        minOccurs = maxOccurs = 1;
    }

    protected ElementGrouping(int grouping, String id, int minOccurs, int maxOccurs) {
        super(id);
        this.grouping = grouping;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    /**
     * <p>
     * The mask informing the caller as to the type of object they are
     * dealing with.
     * </p>
     */
    protected int grouping;

    /**
     * <p>
     * returns the max number of allowable occurences within the xml schema for
     * this construct.
     * </p>
     */
    protected int maxOccurs;

    /**
     * <p>
     * returns the min number of allowable occurences within the xml schema for
     * this construct.
     * </p>
     */
    protected int minOccurs;

    /**
	 * @return Returns the grouping.
	 */
    public int getGrouping() {
        return grouping;
    }

    /**
	 * @return Returns the maxOccurs.
	 */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    /**
	 * @return Returns the minOccurs.
	 */
    public int getMinOccurs() {
        return minOccurs;
    }
}
