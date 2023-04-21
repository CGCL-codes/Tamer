package gtkwire.widget;

import gtkwire.*;
import gtkwire.widget.utils.GtkAttachOptions;

/**
*Widget for packing widgets in regular patterns.
*/
public class GtkTable extends GtkContainer {

    /**
	*Value used to communicate internally with native lib.
	*/
    private static final int ROW_SPACING = 0;

    /**
	*Value used to communicate internally with native lib.
	*/
    private static final int COLUMN_SPACING = 1;

    /**
	*Value used to communicate internally with native lib.
	*/
    private static final int ALL_ROWS_SPACING = 2;

    /**
	*Value used to communicate internally with native lib.
	*/
    private static final int ALL_COLUMNS_SPACING = 3;

    /**
	*Value used to communicate internally with native lib.
	*/
    private static final int ATTACH_FULL = 0;

    /**
	*Value used to communicate internally with native lib.
	*/
    private static final int ATTACH_DEFAULT = 1;

    private int rows;

    private int columns;

    private boolean homogenous;

    public GtkTable(int rows, int columns, boolean homogenous) {
        super();
        this.widgetType = WT_GtkTable;
        this.rows = rows;
        this.columns = columns;
        this.homogenous = homogenous;
        gtkCreate();
    }

    public GtkTable(String name, int rows, int columns, boolean homogenous, GladeKey key) {
        super(name, key);
        this.widgetType = WT_GtkTable;
    }

    protected String[] getCreateData() {
        String[] msg = { Integer.toString(rows), Integer.toString(columns), Boolean.toString(homogenous) };
        return msg;
    }

    /**
	*If you need to change a table's size after it has been created, this function allows you to do so.
	*/
    public void resize(int rows, int columns) {
        int[] data = { rows, columns };
        app().sendCommand(name, GTKWireCommand.RESIZE, data);
    }

    /**
	* Adds a widget to a table. The values used for the GtkAttachOptions 
	* are <code>GtkAttachOptions.EXPAND | GtkAttachOptions.FILL <code/>, 
	* and the padding is set to 0.
	*/
    public void attach(GtkWidget child, int left_attach, int right_attach, int top_attach, int bottom_attach) {
        attach(child, left_attach, right_attach, top_attach, bottom_attach, GtkAttachOptions.EXPAND | GtkAttachOptions.FILL, GtkAttachOptions.EXPAND | GtkAttachOptions.FILL, 0, 0);
    }

    public void attach(GtkWidget child, int left_attach, int right_attach, int top_attach, int bottom_attach, int xoptions, int yoptions) {
        attach(child, left_attach, right_attach, top_attach, bottom_attach, xoptions, yoptions, 0, 0);
    }

    /**
	*Adds a widget to a table. The number of 'cells' that a widget will occupy is specified by 
	*left_attach, right_attach, top_attach and bottom_attach. 
	*/
    public void attach(GtkWidget child, int left_attach, int right_attach, int top_attach, int bottom_attach, int xoptions, int yoptions, int xpadding, int ypadding) {
        String[] dataStr = { child.getName() };
        int[] dataInt = { left_attach, right_attach, top_attach, bottom_attach, xoptions, yoptions, xpadding, ypadding };
        app().sendCommand(name, GTKWireCommand.ATTACH, dataStr, dataInt);
    }

    /**
	*Changes the space between a given table row and the subsequent row.
	*/
    public void setRowSpacing(int spacing, int row) {
        int[] data = { ROW_SPACING, spacing, row };
        app().sendCommand(name, GTKWireCommand.SET_SPACING, data);
    }

    /**
	*Changes the space between a given table column and the subsequent column.
	*/
    public void setColumnSpacing(int spacing, int column) {
        int[] data = { COLUMN_SPACING, spacing, column };
        app().sendCommand(name, GTKWireCommand.SET_SPACING, data);
    }

    /**
	*Sets the space between every row in table equal to spacing.
	*/
    public void setAllRowsSpacing(int spacing) {
        int[] data = { ALL_ROWS_SPACING, spacing };
        app().sendCommand(name, GTKWireCommand.SET_SPACING, data);
    }

    /**
	*Sets the space between every column in table equal to spacing.
	*/
    public void setAllColumnsSpacing(int spacing) {
        int[] data = { ALL_COLUMNS_SPACING, spacing };
        app().sendCommand(name, GTKWireCommand.SET_SPACING, data);
    }

    public void setHomogenous(boolean b) {
        app().sendCommand(name, GTKWireCommand.SET_HOMOGENOUS, b);
    }
}
