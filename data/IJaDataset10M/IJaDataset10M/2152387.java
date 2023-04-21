package org.exmaralda.partitureditor.partiture.editActions;

import org.exmaralda.partitureditor.partiture.*;

/**
 *
 * @author  thomas
 */
public class SearchInEventsAction extends org.exmaralda.partitureditor.partiture.AbstractTableAction {

    public org.exmaralda.partitureditor.search.FindDialog dialog;

    /** Creates a new instance of SearchInEventsAction */
    public SearchInEventsAction(PartitureTableWithActions t, javax.swing.ImageIcon icon) {
        super("Search in events...", icon, t);
        dialog = new org.exmaralda.partitureditor.search.FindDialog(table.parent, false, table.externalKeyboardPaths, table.generalPurposeFontName);
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        System.out.println("searchInEventsAction!");
        table.commitEdit(true);
        searchInEvents();
    }

    private void searchInEvents() {
        dialog.setTranscription(table.getModel().getTranscription());
        dialog.addSearchResultListener(table);
        dialog.show();
    }
}
