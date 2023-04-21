package com.tgjorgoski.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class VerticalGridLayout implements LayoutManager, java.io.Serializable {

    private int rowHeight, colWidth;

    /**
	 * This is the horizontal gap (in pixels) which specifies the space between
	 * columns. They can be changed at any time. This should be a non-negative
	 * integer.
	 * 
	 * @serial
	 * @see #getHgap()
	 * @see #setHgap(int)
	 */
    int hgap;

    /**
	 * This is the vertical gap (in pixels) which specifies the space between
	 * rows. They can be changed at any time. This should be a non negative
	 * integer.
	 * 
	 * @serial
	 * @see #getVgap()
	 * @see #setVgap(int)
	 */
    int vgap;

    /**
	 * This is the number of rows specified for the grid. The number of rows can
	 * be changed at any time. This should be a non negative integer, where '0'
	 * means 'any number' meaning that the number of Rows in that dimension
	 * depends on the other dimension.
	 * 
	 * @serial
	 * @see #getRows()
	 * @see #setRows(int)
	 */
    int rows;

    /**
	 * This is the number of columns specified for the grid. The number of
	 * columns can be changed at any time. This should be a non negative
	 * integer, where '0' means 'any number' meaning that the number of Columns
	 * in that dimension depends on the other dimension.
	 * 
	 * @serial
	 * @see #getColumns()
	 * @see #setColumns(int)
	 */
    int cols;

    /**
	 * Creates a grid layout with the specified number of rows and columns. All
	 * components in the layout are given equal size.
	 * <p>
	 * In addition, the horizontal and vertical gaps are set to the specified
	 * values. Horizontal gaps are placed between each of the columns. Vertical
	 * gaps are placed between each of the rows.
	 * <p>
	 * One, but not both, of <code>rows</code> and <code>cols</code> can be
	 * zero, which means that any number of objects can be placed in a row or in
	 * a column.
	 * <p>
	 * All <code>GridLayout</code> constructors defer to this one.
	 * 
	 * @param rows
	 *            the rows, with the value zero meaning any number of rows
	 * @param cols
	 *            the columns, with the value zero meaning any number of columns
	 * @param hgap
	 *            the horizontal gap
	 * @param vgap
	 *            the vertical gap
	 * @exception IllegalArgumentException
	 *                if the value of both <code>rows</code> and
	 *                <code>cols</code> is set to zero
	 */
    public VerticalGridLayout(int rows, int cols, int hgap, int vgap, int rowHeight, int colWidth) {
        if ((rows == 0) && (cols == 0)) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.rows = rows;
        this.cols = cols;
        this.hgap = hgap;
        this.vgap = vgap;
        this.rowHeight = rowHeight;
        this.colWidth = colWidth;
    }

    /**
	 * Gets the number of rows in this layout.
	 * 
	 * @return the number of rows in this layout
	 * @since JDK1.1
	 */
    public int getRows() {
        return rows;
    }

    /**
	 * Sets the number of rows in this layout to the specified value.
	 * 
	 * @param rows
	 *            the number of rows in this layout
	 * @exception IllegalArgumentException
	 *                if the value of both <code>rows</code> and
	 *                <code>cols</code> is set to zero
	 * @since JDK1.1
	 */
    public void setRows(int rows) {
        if ((rows == 0) && (this.cols == 0)) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.rows = rows;
    }

    /**
	 * Gets the number of columns in this layout.
	 * 
	 * @return the number of columns in this layout
	 * @since JDK1.1
	 */
    public int getColumns() {
        return cols;
    }

    /**
	 * Sets the number of columns in this layout to the specified value. Setting
	 * the number of columns has no affect on the layout if the number of rows
	 * specified by a constructor or by the <tt>setRows</tt> method is
	 * non-zero. In that case, the number of columns displayed in the layout is
	 * determined by the total number of components and the number of rows
	 * specified.
	 * 
	 * @param cols
	 *            the number of columns in this layout
	 * @exception IllegalArgumentException
	 *                if the value of both <code>rows</code> and
	 *                <code>cols</code> is set to zero
	 * @since JDK1.1
	 */
    public void setColumns(int cols) {
        if ((cols == 0) && (this.rows == 0)) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.cols = cols;
    }

    /**
	 * Gets the horizontal gap between components.
	 * 
	 * @return the horizontal gap between components
	 * @since JDK1.1
	 */
    public int getHgap() {
        return hgap;
    }

    /**
	 * Sets the horizontal gap between components to the specified value.
	 * 
	 * @param hgap
	 *            the horizontal gap between components
	 * @since JDK1.1
	 */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
	 * Gets the vertical gap between components.
	 * 
	 * @return the vertical gap between components
	 * @since JDK1.1
	 */
    public int getVgap() {
        return vgap;
    }

    /**
	 * Sets the vertical gap between components to the specified value.
	 * 
	 * @param vgap
	 *            the vertical gap between components
	 * @since JDK1.1
	 */
    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    /**
	 * Adds the specified component with the specified name to the layout.
	 * 
	 * @param name
	 *            the name of the component
	 * @param comp
	 *            the component to be added
	 */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
	 * Removes the specified component from the layout.
	 * 
	 * @param comp
	 *            the component to be removed
	 */
    public void removeLayoutComponent(Component comp) {
    }

    /**
	 * Determines the preferred size of the container argument using this grid
	 * layout.
	 * <p>
	 * The preferred width of a grid layout is the largest preferred width of
	 * all of the components in the container times the number of columns, plus
	 * the horizontal padding times the number of columns minus one, plus the
	 * left and right insets of the target container.
	 * <p>
	 * The preferred height of a grid layout is the largest preferred height of
	 * all of the components in the container times the number of rows, plus the
	 * vertical padding times the number of rows minus one, plus the top and
	 * bottom insets of the target container.
	 * 
	 * @param parent
	 *            the container in which to do the layout
	 * @return the preferred dimensions to lay out the subcomponents of the
	 *         specified container
	 * @see java.awt.GridLayout#minimumLayoutSize
	 * @see java.awt.Container#getPreferredSize()
	 */
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = rows;
            int ncols = cols;
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            int w = colWidth;
            int h = rowHeight;
            return new Dimension(insets.left + insets.right + ncols * w + (ncols - 1) * hgap, insets.top + insets.bottom + nrows * h + (nrows - 1) * vgap);
        }
    }

    /**
	 * Determines the minimum size of the container argument using this grid
	 * layout.
	 * <p>
	 * The minimum width of a grid layout is the largest minimum width of all of
	 * the components in the container times the number of columns, plus the
	 * horizontal padding times the number of columns minus one, plus the left
	 * and right insets of the target container.
	 * <p>
	 * The minimum height of a grid layout is the largest minimum height of all
	 * of the components in the container times the number of rows, plus the
	 * vertical padding times the number of rows minus one, plus the top and
	 * bottom insets of the target container.
	 * 
	 * @param parent
	 *            the container in which to do the layout
	 * @return the minimum dimensions needed to lay out the subcomponents of the
	 *         specified container
	 * @see java.awt.GridLayout#preferredLayoutSize
	 * @see java.awt.Container#doLayout
	 */
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = rows;
            int ncols = cols;
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            int w = colWidth;
            int h = rowHeight;
            return new Dimension(insets.left + insets.right + ncols * w + (ncols - 1) * hgap, insets.top + insets.bottom + nrows * h + (nrows - 1) * vgap);
        }
    }

    /**
	 * Lays out the specified container using this layout.
	 * <p>
	 * This method reshapes the components in the specified target container in
	 * order to satisfy the constraints of the <code>GridLayout</code> object.
	 * <p>
	 * The grid layout manager determines the size of individual components by
	 * dividing the free space in the container into equal-sized portions
	 * according to the number of rows and columns in the layout. The
	 * container's free space equals the container's size minus any insets and
	 * any specified horizontal or vertical gap. All components in a grid layout
	 * are given the same size.
	 * 
	 * @param parent
	 *            the container in which to do the layout
	 * @see java.awt.Container
	 * @see java.awt.Container#doLayout
	 */
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = rows;
            int ncols = cols;
            boolean ltr = parent.getComponentOrientation().isLeftToRight();
            if (ncomponents == 0) {
                return;
            }
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            int w = getColWidth();
            int h = getRowHeight();
            for (int c = 0, x = insets.left; c < ncols; c++, x += w + hgap) {
                for (int r = 0, y = insets.top; r < nrows; r++, y += h + vgap) {
                    int i = c * nrows + r;
                    if (i < ncomponents) {
                        Dimension dim = parent.getComponent(i).getPreferredSize();
                        parent.getComponent(i).setBounds(x, y, Math.min(dim.width, w), Math.min(dim.height, h));
                    }
                }
            }
        }
    }

    /**
	 * Returns the string representation of this grid layout's values.
	 * 
	 * @return a string representation of this grid layout
	 */
    public String toString() {
        return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + ",rows=" + rows + ",cols=" + cols + "]";
    }

    public int getColWidth() {
        return colWidth;
    }

    public void setColWidth(int colWidth) {
        this.colWidth = colWidth;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowWidth) {
        this.rowHeight = rowWidth;
    }
}
