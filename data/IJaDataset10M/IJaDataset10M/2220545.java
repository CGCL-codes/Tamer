package de.emoo.ui.swing.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import de.emoo.ui.swing.table.model.EmooTableModel;
import de.emoo.ui.swing.table.model.LocationTableModel;
import de.emoo.util.EmooException;

/**
 * <b>LocationView</b>:
 * 
 * @author gandalf
 * @version $Id: LocationView.java,v 1.3 2004/08/14 11:39:04 tbuchloh Exp $
 */
final class LocationView extends View {

    private LocationTableModel _tableModel;

    /**
     * creates a new LocationView
     * @param locations are the locations to display.
     */
    public LocationView(Set locations) {
        super(new Navigator("Files"));
        _tableModel = new LocationTableModel();
        _tableModel.setRows(locations);
        getDataTable().setModel(_tableModel);
    }

    /**
     * Overridden!
     * @see de.emoo.ui.swing.EmooComponent#save()
     */
    public void save() throws EmooException {
    }

    /**
     * Overridden!
     * @see de.emoo.ui.swing.main.View#getActions()
     */
    protected Collection getActions() {
        return new ArrayList();
    }

    /**
     * Overridden!
     * @see de.emoo.ui.swing.main.View#getModel()
     */
    protected EmooTableModel getModel() {
        return _tableModel;
    }
}
