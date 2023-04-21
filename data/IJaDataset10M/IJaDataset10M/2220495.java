package com.iver.cit.gvsig.fmap.layers;

/**
 * 
 * @author jaume
 *
 */
public interface IFMapWMSDimension {

    public static int SINGLE_VALUE = 0;

    public static int MULTIPLE_VALUE = 1;

    public static int INTERVAL = 2;

    /**
     * Return the dimension's name. This value is the value that will be used in
     * a GetMap request.
     * 
     * @return String containing the name of this dimension.
     */
    public String getName();

    /**
     * Return the unit used by this dimension.
     * @return
     */
    public String getUnit();

    /**
     * Returns the unit symbol (i.e. 'm', 's', or 'l' for meters, seconds, or liters respectively) 
     * @return
     */
    public String getUnitSymbol();

    /**
     * This method returns the <b>lowest</b> value of this dimension if this dimension is
     * specified as an interval or as a set of values, or the value specified if it
     * was a single value. 
     * @return String containing the coded value.
     */
    public String getLowLimit();

    /**
     * This method returns the <b>highest</b> value of this dimension if this dimension is
     * specified as an interval or as a set of values, or the value specified if it
     * was a single value. 
     * @return String containing the coded value.
     */
    public String getHighLimit();

    /**
     * This method returns the resolution supported by this dimension. This
     * means the step lenght between two consecutive points along the
     * dimension's axis. 
     * @return String containing the coded value, or null if no value for resolution.
     * @deprecated
     */
    public String getResolution();

    /**
     * Checks if the value represented as string is a valid value by checking
     * if the dimensions supports it. It should be true if one of the following is
     * true:
     * <p>
     * <ol> 
     * <li>
     *  The dimension <b>supports nearest values</b> and <b>the value is greather
     *  or  equal than the low limit</b> and <b>less or equal than the high limit</b>.  
     *  </li>
     *  <li>
     *  The value matches in one of the points defined by the low and high limits, and
     *  the resolution value.
     *  </li>
     * </ol>
     * </p>
     * @param value
     * @return
     */
    public boolean isValidValue(String value);

    /**
     * Return the value of the String passed in the dimension's unit-natural type.
     * @param value
     * @return
     */
    public Object valueOf(String value) throws IllegalArgumentException;

    /**
     * Returns the value that would be at the position passed as argument.
     * @param pos
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    public String valueAt(int pos) throws ArrayIndexOutOfBoundsException;

    /**
     * The amount of positions that this dimension contains. 
     * @return -1 if the dimension is not recognized, the amount otherwise
     */
    public int valueCount();

    /**
     * Returns the expression describing this WMS Dimension
     */
    public String getExpression();

    /**
     * Sets the expression describing this WMS Dimension
     * @throws IllegalArgumentException
     */
    public void setExpression(String expr);

    /**
	 * Returns the type of the dimension expression.<br>
	 * Possible values are:
	 * <ol>
	 * 	<li>
	 * 		<b>IFMapWMSDimension.SINGLE_VALUE</b>
	 * 		<b>IFMapWMSDimension.MULTIPLE_VALUE</b>
	 * 		<b>IFMapWMSDimension.INTERVAL</b>
	 * 	</li>
	 * </ol>
	 * @return int
	 */
    public int getType();

    /**
     * Analyzes and establishes the starting values for this dimension. No operation of this
     * dimension can be called before the dimension has been compiled.
     * 
     * @throws IllegalArgumentException
     */
    public void compile() throws IllegalArgumentException;
}
