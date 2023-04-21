package com.tensegrity.palojava;

/**
 * The <code>CellInfo</code> interface is a representation of a single cell
 * within a palo <code>Cube</code>. Each cell has a value of type 
 * {@link #TYPE_NUMERIC} or {@link #TYPE_STRING} and a path specified by its
 * coordinate.
 * 
 * @author ArndHouben
 * @version $Id: CellInfo.java,v 1.3 2007/12/17 13:15:53 ArndHouben Exp $
 */
public interface CellInfo extends PaloInfo {

    public static final int SPLASH_MODE_DISABLED = 0;

    public static final int SPLASH_MODE_DEFAULT = 1;

    public static final int SPLASH_MODE_ADD = 2;

    public static final int SPLASH_MODE_SET = 3;

    public static final int SPLASH_MODE_UNKNOWN = 4;

    /** type for numeric cell value */
    public static final int TYPE_NUMERIC = 1;

    /** type for textual cell value */
    public static final int TYPE_STRING = 2;

    /** signals an cell error */
    public static final int TYPE_ERROR = 99;

    /**
	 * Checks if a cell at current set coordinate exists.
	 * @return <code>true</code> if a cell exists at current coordinate,
	 * <code>false</code> otherwise
	 */
    public boolean exists();

    /**
	 * Returns the cell value as {@link Double} or {@link String} depending on
	 * the cell type
	 * @return cell value
	 */
    public Object getValue();

    /**
	 * Returns the ids of the <code>Element</code>s which build up the path to
	 * this cell. <b>NOTE:</b> this is optional and can return <code>null</code>
	 * if no path was set. 
	 * @return
	 */
    public String[] getCoordinate();

    /**
	 * Returns the type of this cell. The type is one of the predefined cell 
	 * type constants.
	 * @return the cell type.
	 */
    public int getType();

    /**
	 * Returns the identifier of the rule which is attached to this cell or
	 * <code>null</code> if this cell has no rule. 
	 * @return the rule id or <code>null</code>
	 */
    public String getRule();

    /**
	 * Sets the identifier of the rule to use for this cell
	 * @param id the identifier of the rule to use for this cell
	 */
    public void setRule(String id);
}
