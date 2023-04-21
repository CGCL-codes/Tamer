package csheets.ui.ext;

import java.awt.Graphics;
import csheets.ui.sheet.SpreadsheetTable;

/**
 * A table decorator visualizes extension-specific data by drawing on top of
 * a table's graphics context. Decorators should respect the enabled state set
 * on them.
 * @author Einar Pehrson
 */
public abstract class TableDecorator {

    /** Whether the decorator should be used when rendering, default is false */
    protected boolean enabled = false;

    /**
	 * Creates a new table decorator.
	 */
    public TableDecorator() {
    }

    /**
	 * Decorates the given graphics context, by drawing visualizations of
	 * extension-specific data on it.
	 * @param g the graphics context on which drawing should be done
	 * @param table the table displaying the spreadsheet to visualize
	 */
    public abstract void decorate(Graphics g, SpreadsheetTable table);

    /**
	 * Returns whether the decorator should be used when rendering.
	 * @return true if the decorator should be used when rendering
	 */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
	 * Sets whether the decorator should be used when rendering.
	 * @param enabled whether the decorator should be used when rendering
	 */
    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
