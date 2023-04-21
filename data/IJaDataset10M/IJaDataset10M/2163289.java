package com.sptci.rwt.webui.model;

import nextapp.echo2.app.Component;
import nextapp.echo2.app.Grid;
import nextapp.echo2.app.Label;
import echopointng.GroupBox;
import com.sptci.ReflectionUtility;
import com.sptci.echo2.Application;
import com.sptci.echo2.Configuration;
import com.sptci.echo2.Utilities;
import com.sptci.rwt.ColumnMetaData;
import com.sptci.rwt.MetaData;
import com.sptci.rwt.KeyMetaData;

/**
 * An abstract view component for representing instances of {@link
 * com.sptci.rwt.KeyMetaData}.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-10-07
 * @version $Id: KeyView.java 2 2007-10-19 21:06:36Z rakesh.vidyadharan $
 */
public abstract class KeyView extends AbstractView {

    /**
   * Create the component used to display the details of the columns that
   * are included in the foreign key.
   *
   * @see #createCollectionLabels
   * @return The component that displays the column information.
   */
    protected Component createColumnDetails() {
        KeyMetaData kmd = getMetaData();
        int size = kmd.getColumns().size();
        Grid grid = new Grid(size + 1);
        createCollectionLabels("name", kmd, grid);
        createCollectionLabels("type", kmd, grid);
        createCollectionLabels("defaultValue", kmd, grid);
        createCollectionLabels("size", kmd, grid);
        createCollectionLabels("nullable", kmd, grid);
        GroupBox box = new GroupBox(Configuration.getString(this, "column.title"));
        box.add(grid);
        return box;
    }

    /**
   * Create standard {@link nextapp.echo2.app.Label} components that
   * represent the name of the specified field and the value in the
   * specified model.  Over-loaded to process the
   * java.util.Collection&lt;ColumnMetaData&gt; and create a
   * multi-dimensional grid.
   *
   * @param name The name of the field.
   * @param kmd The model object.
   * @param component The container component to which the labels are to
   *   be added.
   */
    protected void createCollectionLabels(final String name, final KeyMetaData kmd, final Component component) {
        int size = kmd.getColumns().size();
        final String method = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        component.add(Utilities.createLabel(getClass().getName(), name, "Title.Label"));
        for (ColumnMetaData cmd : kmd.getColumns()) {
            try {
                Object object = ReflectionUtility.execute(cmd, method);
                component.add(new Label(String.valueOf(object)));
            } catch (Throwable t) {
                processFatalException(method, ColumnMetaData.class.getName(), t);
            }
        }
    }

    /**
   * Abstract method to return the model object for this view.
   *
   * @return The meta data object.
   */
    protected abstract KeyMetaData getMetaData();
}
