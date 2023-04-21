package org.ufacekit.ui.swing.jface.viewers.internal;

import org.ufacekit.ui.swing.jface.viewers.internal.swt.graphics.Color;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.graphics.Font;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.graphics.Image;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.widgets.Control;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.widgets.Item;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.widgets.Widget;

/**
 * The ViewerCell is the JFace representation of a cell entry in a ViewerRow.
 * @param <ModelElement>
 *
 * @since 3.3
 *
 */
public class ViewerCell<ModelElement> {

    private int columnIndex;

    private ViewerRow<ModelElement> row;

    private ModelElement element;

    /**
	 * Constant denoting the cell above current one (value is 1).
	 */
    public static int ABOVE = 1;

    /**
	 * Constant denoting the cell below current one (value is 2).
	 */
    public static int BELOW = 1 << 1;

    /**
	 * Constant denoting the cell to the left of the current one (value is 4).
	 */
    public static int LEFT = 1 << 2;

    /**
	 * Constant denoting the cell to the right of the current one (value is 8).
	 */
    public static int RIGHT = 1 << 3;

    /**
	 * Create a new instance of the receiver on the row.
	 *
	 * @param row
	 * @param columnIndex
	 * @param element
	 */
    ViewerCell(ViewerRow<ModelElement> row, int columnIndex, ModelElement element) {
        this.row = row;
        this.columnIndex = columnIndex;
        this.element = element;
    }

    /**
	 * Get the index of the cell.
	 *
	 * @return the index
	 */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
	 * Get the element this row represents.
	 *
	 * @return {@link Object}
	 */
    public ModelElement getElement() {
        if (element != null) {
            return element;
        }
        if (row != null) {
            return row.getElement();
        }
        return null;
    }

    /**
	 * Return the text for the cell.
	 *
	 * @return {@link String}
	 */
    public String getText() {
        return row.getText(columnIndex);
    }

    /**
	 * Return the Image for the cell.
	 *
	 * @return {@link Image} or <code>null</code>
	 */
    public Image getImage() {
        return row.getImage(columnIndex);
    }

    /**
	 * Set the background color of the cell.
	 *
	 * @param background
	 */
    public void setBackground(Color background) {
        row.setBackground(columnIndex, background);
    }

    /**
	 * Set the foreground color of the cell.
	 *
	 * @param foreground
	 */
    public void setForeground(Color foreground) {
        row.setForeground(columnIndex, foreground);
    }

    /**
	 * Set the font of the cell.
	 *
	 * @param font
	 */
    public void setFont(Font font) {
        row.setFont(columnIndex, font);
    }

    /**
	 * Set the text for the cell.
	 *
	 * @param text
	 */
    public void setText(String text) {
        row.setText(columnIndex, text);
    }

    /**
	 * Set the Image for the cell.
	 *
	 * @param image
	 */
    public void setImage(Image image) {
        row.setImage(columnIndex, image);
    }

    /**
	 * Set the columnIndex.
	 *
	 * @param column
	 */
    void setColumn(int column) {
        columnIndex = column;
    }

    /**
	 * Set the row to rowItem and the columnIndex to column.
	 *
	 * @param rowItem
	 * @param column
	 * @param element
	 */
    void update(ViewerRow<ModelElement> rowItem, int column, ModelElement element) {
        row = rowItem;
        columnIndex = column;
        this.element = element;
    }

    /**
	 * Return the item for the receiver.
	 *
	 * @return {@link Item}
	 */
    public Widget getItem() {
        return row.getItem();
    }

    /**
	 * Get the control for this cell.
	 *
	 * @return {@link Control}
	 */
    public Control getControl() {
        return row.getControl();
    }

    /**
	 * @return the row
	 */
    public ViewerRow<ModelElement> getViewerRow() {
        return row;
    }

    /**
	 * Gets the foreground color of the cell.
	 *
	 * @return the foreground of the cell or <code>null</code> for the default foreground
	 *
	 * @since 3.4
	 */
    public Color getForeground() {
        return row.getForeground(columnIndex);
    }

    /**
	 * Gets the background color of the cell.
	 *
	 * @return the background of the cell or <code>null</code> for the default background
	 *
	 * @since 3.4
	 */
    public Color getBackground() {
        return row.getBackground(columnIndex);
    }

    /**
	 * Gets the font of the cell.
	 *
	 * @return the font of the cell or <code>null</code> for the default font
	 *
	 * @since 3.4
	 */
    public Font getFont() {
        return row.getFont(columnIndex);
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + columnIndex;
        result = prime * result + ((row == null) ? 0 : row.hashCode());
        return result;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ViewerCell<ModelElement> other = (ViewerCell<ModelElement>) obj;
        if (columnIndex != other.columnIndex) return false;
        if (row == null) {
            if (other.row != null) return false;
        } else if (!row.equals(other.row)) return false;
        return true;
    }
}
