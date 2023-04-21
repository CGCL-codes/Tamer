package org.softsmithy.lib.swing;

import javax.swing.Icon;

/**
 *
 * @author  puce
 */
public abstract class AbstractCellRenderer<T> implements CellRenderer<T> {

    /** Holds value of property horizontalAlignment. */
    private final HorizontalAlignment horizontalAlignment;

    public AbstractCellRenderer() {
        this(HorizontalAlignment.LEADING);
    }

    /** Creates a new instance of LeadingCellRenderer */
    public AbstractCellRenderer(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    /** Getter for property horizontalAlignment.
     * @return Value of property horizontalAlignment.
     *
     */
    @Override
    public HorizontalAlignment getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    @Override
    public Icon getIcon(T value) {
        return null;
    }
}
