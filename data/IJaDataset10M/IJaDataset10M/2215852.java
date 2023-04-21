package org.wct;

/**
 * This class implements a LayoutManager like the AWT GridLayout.
 * see the AWT GridLayout class for usage.
 * @author  juliano
 * @version 0.1
 */
public class GridLayout implements LayoutManager {

    /** Holds value of property rows. */
    private int rows;

    /** Utility field used by bound properties. */
    private java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    /** Holds value of property columns. */
    private int columns;

    /**
     * Holds the components
     */
    private java.util.ArrayList components = new java.util.ArrayList();

    /** Creates a GridLayout with one row and any number of columns */
    public GridLayout() {
        setRows(1);
        setColumns(0);
    }

    /**
     * Creates a GridLayout with the specified number of rows and columns. One of the arguments may be zero.
     * @exception IllegalArgumentException if both arguments are zero or if one of them  is negative
     */
    public GridLayout(int rows, int columns) {
        if (rows == 0 && columns == 0) throw new IllegalArgumentException("It is illegal to set both rows and cols to zero.");
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * Adds the specified component to this Layout Manager with the specified constraints
     */
    public void addLayoutComponent(Component comp, java.lang.Object constraints) {
        components.add(comp);
    }

    /**
     * Return the UIClassID if the object, used to look up the reference to the UI implementation object
     */
    public String getUIClassID() {
        return "GridLayoutUI";
    }

    /**
     * Removees the specified component from this container
     */
    public void removeLayoutComponent(Component comp) {
        components.remove(comp);
    }

    /** Add a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    /** Getter for property rows.
     * @return Value of property rows.
     */
    public int getRows() {
        return rows;
    }

    /** Setter for property rows.
     * @param rows New value of property rows.
     */
    public void setRows(int rows) {
        if (rows < 0) throw new IllegalArgumentException("Illegal negative number of rows supplied: " + rows);
        if (rows == 0 && getColumns() == 0) throw new IllegalArgumentException("It is illegal to set both rows and columns to zero.");
        int oldRows = this.rows;
        this.rows = rows;
        propertyChangeSupport.firePropertyChange("rows", new Integer(oldRows), new Integer(rows));
    }

    /** Getter for property columns.
     * @return Value of property columns.
     */
    public int getColumns() {
        return columns;
    }

    /** Setter for property columns.
     * @param columns New value of property columns.
     */
    public void setColumns(int columns) {
        if (columns < 0) throw new IllegalArgumentException("Illegal number of columns supplied: " + columns);
        if (columns == 0 && getRows() == 0) throw new IllegalArgumentException("It is illegal to set both columns and rows to zero");
        int oldColumns = this.columns;
        this.columns = columns;
        propertyChangeSupport.firePropertyChange("columns", new Integer(oldColumns), new Integer(columns));
    }

    /**
     * Returns the component list in the added order
     **/
    public java.util.List getComponents() {
        return components;
    }
}
