package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GridFieldComposer - Lays out fields in a vertical style having the following
 * attributes:
 * <ol>
 * <li>Only one field exists on a row
 * <li>Field labels are placed to the left of the fields
 * </ol>
 * @author jpk
 */
public class GridFieldComposer extends AbstractFieldComposer {

    /**
	 * Styles - (field.css)
	 * @author jpk
	 */
    static final class Styles {

        /**
		 * Style applied to the grid containing the fields.
		 */
        public static final String FIELD_GRID = "fgrid";

        public static final String CELL_LABEL = "cell-lbl";

        public static final String CELL_FIELD = "cell-fld";
    }

    /**
	 * The root canvas panel for this field canvas implementation.
	 */
    private Grid grid;

    private int rowIndex = -1;

    /**
	 * Constructor
	 */
    public GridFieldComposer() {
        super();
    }

    @Override
    public void setCanvas(final Panel canvas) {
        if (this.canvas != null && this.canvas == canvas) return;
        super.setCanvas(canvas);
        grid = new Grid(0, 2);
        grid.addStyleName(Styles.FIELD_GRID);
        rowIndex = -1;
        canvas.add(grid);
    }

    @Override
    public void add(final FieldLabel fldLbl, final Widget w) {
        grid.resizeRows(++rowIndex + 1);
        if (fldLbl != null) {
            grid.getCellFormatter().setStyleName(rowIndex, 0, Styles.CELL_LABEL);
            grid.setWidget(rowIndex, 0, fldLbl);
        }
        grid.setWidget(rowIndex, 1, w);
        grid.getCellFormatter().setStyleName(rowIndex, 1, Styles.CELL_FIELD);
    }

    @Override
    public void addWidget(final Widget w) {
        add(null, w);
    }

    @Override
    public void addWidget(final String label, final Widget w) {
        add(label == null ? null : new FieldLabel(label), w);
    }

    @Override
    public void addField(final IFieldWidget<?> field) {
        add(field.getFieldLabel(), field.getWidget());
    }
}
