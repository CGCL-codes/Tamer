package org.gwt.advanced.client;

import org.gwt.advanced.client.datamodel.DataModelCallbackHandler;
import org.gwt.advanced.client.datamodel.Editable;
import org.gwt.advanced.client.datamodel.GridDataModel;
import org.gwt.advanced.client.ui.widget.EditableGrid;
import org.gwt.advanced.client.ui.widget.GridPanel;

/**
 * This class is a demo of data handler for detail grid.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 */
public class DetailGridModelHandler implements DataModelCallbackHandler {

    private GridPanel panel;

    private GridPanel parent;

    public DetailGridModelHandler(GridPanel panel, GridPanel parent) {
        this.panel = panel;
        this.parent = parent;
    }

    public void synchronize(GridDataModel model) {
        panel.lock();
        try {
            EditableGrid grid = parent.getGrid();
            Object[] data = grid.getModel().getRowData(grid.getCurrentRow());
            GridDataModel newModel = DemoModelFactory.createEployeesModel((Long) data[data.length - 1]);
            ((Editable) model).update(newModel.getData());
        } finally {
            panel.unlock();
        }
    }
}
