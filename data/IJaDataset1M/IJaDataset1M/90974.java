package org.dhs.echobase.jcomics.gui;

import org.dhs.echobase.jcomics.data.*;
import org.dhs.echobase.widgets.gui.SortedListModel;
import java.util.*;
import javax.swing.*;

/**
 * Dialog for editing Locations.
 */
public class LocationDialog extends QuicklistDialog {

    /**
     * Constructs the dialog.
     *
     * @param owner The JComicsFrame the owns this dialog.
     * @param title The title of the dialog.
     * @param comics The comics data collection.
     */
    public LocationDialog(JComicsFrame owner, String title, Comics comics) {
        super(owner, title, comics);
        setPanelTitle("Locations");
        initList();
    }

    /**
     * Event handler for the Delete button.
     */
    public void eventDelete() {
        if (intCurrent.intValue() != 0) {
            dbComics.removeLocation(getSelectedValue());
            remove(getSelectedValue());
        }
    }

    /**
     * Event handler for the Save button.
     */
    public void eventSave() {
        if (intCurrent.intValue() == 0) {
            QuicklistItem loc = dbComics.createLocation(txtName.getText());
            add(loc);
        } else {
            QuicklistItem qli = (QuicklistItem) lstQuicklist.getSelectedValue();
            qli.setName(txtName.getText());
            dbComics.updateLocation(qli);
            refresh();
        }
    }

    /**
     * Inits the list from the data collection.
     */
    private void initList() {
        Iterator itLocations = dbComics.getLocationIterator();
        SortedListModel dlm = new SortedListModel();
        while (itLocations.hasNext()) {
            dlm.add(itLocations.next());
        }
        setListModel(dlm);
    }
}
