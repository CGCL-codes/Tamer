package gear.widgets.form.matrix;

import gear.widgets.form.GWForm;
import gear.widgets.items.ImageItem;
import gear.widgets.items.GridItem;
import gear.widgets.theme.Theme;
import javax.microedition.lcdui.Graphics;

/**
 * Scrollable grid widget
 * Contains a set of ImageItems and handles the rendering and scrolling activities.
 * @author Paolo Burelli
 *
 */
public class GWGrid extends GWForm {

    /** The number of rows */
    private int rows;

    /** The number of columns */
    private int cols;

    /** Scrollbar width */
    protected int scrollbarWidht = 6;

    /**
	 * GWGrid default constructor (3x3)
	 */
    public GWGrid() {
        this(3, 3);
    }

    /**
	 * GWGrid constructor
	 * @param rows number of maximum rows
	 * @param cols number of maximum columns
	 */
    public GWGrid(int rows, int cols) {
        super();
        this.rows = rows;
        this.cols = cols;
        setStretchIcons(true);
        applyTheme(Theme.getDefaultTheme());
    }

    /**
	 * Adds a text Item
	 * @param label the item's label
	 * @param imageRes the item's image
	 * @return the text button
	 */
    public GridItem addItem(String label, String imageRes) {
        return (GridItem) addItem(new GridItem(label, imageRes));
    }

    /**
	 * Adds a text Item
	 * @param label the item's label
	 * @param imageRes the item's image
	 * @param customData a custom object associated to the icon
	 * @return the text button
	 */
    public GridItem addItem(String label, String imageRes, Object customData) {
        GridItem ti = new GridItem(label, imageRes);
        ti.setCustomData(customData);
        return (GridItem) addItem(ti);
    }

    /**
	 * Sets item's properties
	 * 
	 * @param index item's index
	 * @param label the label (if null is label is not set)
	 * @param imageRes the image res (if null image is not set)
	 * 
	 */
    public void setItem(int index, String label, String imageRes) {
        if (imageRes != null) ((ImageItem) getItem(index)).setImage(imageRes);
        if (label != null && GridItem.class.isInstance(getItem(index))) ((GridItem) getItem(index)).setLabel(label);
    }

    /**
	 * Adds an new image item to the form.
	 * @param newItem the new item
	 * @return the image item
	 */
    public ImageItem addItem(ImageItem newItem) {
        super.addItem(newItem);
        if ((getRows() * getCols()) < getItemsCount()) {
            setCols(this.calculateCols());
            setRows(this.calculateRows());
        }
        return newItem;
    }

    /**
	 * Calculates rows.
	 * @return number of rows
	 */
    private int calculateRows() {
        double ratio;
        ratio = Math.sqrt((double) (getWidth() * getHeight()) / ((double) getItemsCount()));
        int xcount = (int) Math.ceil((((double) getWidth()) / ratio));
        int ycount = (int) Math.ceil((((double) getHeight()) / ratio));
        if (xcount <= 0) xcount = 1;
        if (ycount <= 0) ycount = 1;
        while ((xcount * ycount) < getItemsCount()) {
            if (getWidth() > getHeight()) xcount++; else ycount++;
        }
        if (ycount > rows) ycount = rows;
        return ycount;
    }

    /**
	 * Calculates columns.
	 * @return number of columns
	 */
    private int calculateCols() {
        double ratio;
        ratio = Math.sqrt((double) (getWidth() * getHeight()) / ((double) getItemsCount()));
        int xcount = (int) Math.ceil((((double) getWidth()) / ratio));
        int ycount = (int) Math.ceil((((double) getHeight()) / ratio));
        if (xcount <= 0) xcount = 1;
        if (ycount <= 0) ycount = 1;
        while ((xcount * ycount) < getItemsCount()) {
            if (getWidth() > getHeight()) xcount++; else ycount++;
        }
        if (xcount > cols) xcount = cols;
        return xcount;
    }

    /**
	 * Returns the number of rows
	 * @return the number of rows
	 */
    public int getRows() {
        return rows;
    }

    /**
	 * Sets the number of rows
	 * @param rows the number of rows
	 */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
	 * Returns the number of columns
	 * @return the number of columns
	 */
    public int getCols() {
        return cols;
    }

    /**
	 * Sets the number of columns
	 * @param cols the number of columns
	 */
    public void setCols(int cols) {
        this.cols = cols;
    }

    public void contentPaint(Graphics gc, int sx, int sy, int width, int height) {
        if (getItemsCount() > getCols() * getRows()) {
            int handleSize = height / 10;
            gc.setColor(0x777777);
            gc.fillRect(sx + width - scrollbarWidht, sy, scrollbarWidht, height);
            gc.setColor(getSelectedBorder());
            gc.fillRect(sx + width - scrollbarWidht, sy + (int) ((height - (handleSize + 3)) * ((float) getSelectedIndex() / (getItemsCount() - 1))) + 1, scrollbarWidht, handleSize + 2);
            gc.setColor(getSelectedBackground());
            gc.fillRect(sx + width - (scrollbarWidht - 1), sy + (int) ((height - (handleSize + 3)) * ((float) getSelectedIndex() / (getItemsCount() - 1))) + 2, scrollbarWidht - 2, handleSize);
            width -= scrollbarWidht;
        }
        int box_width = (width - sx) / getCols();
        int box_height = (height - sy) / getRows();
        ImageItem item;
        int currentRow = getSelectedIndex() / cols;
        int minRow = currentRow - (rows / 2);
        if (minRow < 0) minRow = 0;
        int minIndex = minRow * cols;
        if (minIndex + (rows * cols) > getItemsCount()) minIndex = getItemsCount() - (rows * cols);
        int index = minIndex;
        if (getItemsCount() > 0) {
            for (int y = 0; y < getRows(); y++) {
                for (int x = 0; x < getCols(); x++) {
                    if (index < getItemsCount()) {
                        item = (ImageItem) getItem(index++);
                        item.paint(gc, sx + x * box_width, sy + y * box_height, box_width, box_height);
                    }
                }
            }
        }
    }

    protected void downArrowPressed() {
        if (getItemsCount() > 0) {
            int idx = getSelectedIndex();
            if (idx + getCols() < getItemsCount()) {
                setSelectedIndex(idx + getCols());
            } else {
                setSelectedIndex(idx % getCols());
            }
        }
    }

    protected void leftArrowPressed() {
        if (getItemsCount() > 0) {
            int idx = getSelectedIndex();
            if (idx > 0) {
                setSelectedIndex(idx - 1);
            } else {
                setSelectedIndex(getItemsCount() - 1);
            }
        }
    }

    protected void rightArrowPressed() {
        if (getItemsCount() > 0) {
            int idx = getSelectedIndex();
            if (idx < getItemsCount() - 1) {
                setSelectedIndex(idx + 1);
            } else {
                setSelectedIndex(0);
            }
        }
    }

    protected void upArrowPressed() {
        if (getItemsCount() > 0) {
            int idx = getSelectedIndex();
            if (idx > 0) {
                if (idx >= getCols()) {
                    setSelectedIndex(idx - getCols());
                } else {
                    setSelectedIndex(getItemsCount() - 1);
                }
            } else {
                setSelectedIndex(getItemsCount() - 1);
            }
        }
    }
}
