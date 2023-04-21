package com.sptci.rwt.webui.model;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.Grid;
import echopointng.GroupBox;
import com.sptci.echo2.Configuration;
import com.sptci.rwt.LimitsMetaData;

/**
 * A view component used to display the information contained in
 * {@link com.sptci.rwt.LimitsMetaData}.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-10-07
 * @version $Id: LimitsView.java 2 2007-10-19 21:06:36Z rakesh.vidyadharan $
 */
public class LimitsView extends AbstractView {

    /** The meta data object whose details are to be displayed. */
    private final LimitsMetaData metaData;

    /**
   * Create a new instance of the view using the specified model object.
   *
   * @param metaData The {@link #metaData} model object to use.
   */
    public LimitsView(final LimitsMetaData metaData) {
        this.metaData = metaData;
    }

    /**
   * Lifecycle method used to initialise component when added to a
   * container hierarchy.
   *
   * @see #createDetails
   */
    @Override
    public void init() {
        removeAll();
        add(createDetails());
    }

    /**
   * Create the component used to display the default limits enforced by
   * the database engine.
   *
   * @see #createLabels
   * @return The component that displays the limits information.
   */
    protected Component createDetails() {
        Grid grid = new Grid();
        createLabels("characterLength", metaData, grid);
        createLabels("columnNameLength", metaData, grid);
        createLabels("cursorNameLength", metaData, grid);
        createLabels("procedureNameLength", metaData, grid);
        createLabels("schemaNameLength", metaData, grid);
        createLabels("tableNameLength", metaData, grid);
        createLabels("userNameLength", metaData, grid);
        createLabels("columnsInIndex", metaData, grid);
        createLabels("columnsInSelect", metaData, grid);
        createLabels("columnsInOrderBy", metaData, grid);
        createLabels("columnsInGroupBy", metaData, grid);
        createLabels("tablesInSelect", metaData, grid);
        createLabels("connections", metaData, grid);
        createLabels("indexLength", metaData, grid);
        createLabels("rowSize", metaData, grid);
        createLabels("statementLength", metaData, grid);
        createLabels("statements", metaData, grid);
        createLabels("columnsInTable", metaData, grid);
        GroupBox box = new GroupBox(Configuration.getString(this, "title"));
        box.add(grid);
        return box;
    }
}
