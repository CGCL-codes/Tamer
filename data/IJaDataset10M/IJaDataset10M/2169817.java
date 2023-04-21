package entagged.tageditor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import entagged.tageditor.models.Navigator;
import entagged.tageditor.models.TagEditorTableModel;

/**
 * The represented action performs the opening of the current selected object in
 * the registered table. <br>
 * The method {@link entagged.tageditor.models.Navigator#browseInto(File)}is
 * called which will identify the target and perform one of the following
 * actions: <br>
 * <li>Reload the current folder</li>
 * <li>Goto parent folder</li>
 * <li>Open folder</li>
 * 
 * @author Rapha�l Slinckx
 */
public final class BrowseIntoAction extends AbstractAction {

    /**
     * Stores the tablemodel of {@link #table}for retrieving the {@link File}
     * instance of the selection.
     */
    private TagEditorTableModel model;

    /**
     * Navigator which performs the browsing.
     */
    private Navigator navi;

    /**
     * Table of that the selection is retrieved.
     */
    private JTable table;

    /**
     * Creates an instance.
     * 
     * @param navigator
     *                  The navigator which performs directory changes.
     * @param table
     *                  Table to look for the selection.
     * @param editorTableModel
     *                  To retrieve the file instance.
     */
    public BrowseIntoAction(Navigator navigator, JTable table, TagEditorTableModel editorTableModel) {
        this.navi = navigator;
        this.table = table;
        this.model = editorTableModel;
    }

    /**
     * (overridden)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0 || row >= table.getRowCount()) return;
        File dest = model.getFileAt(row);
        if (dest != null && dest.isDirectory() && dest.canRead()) {
            navi.browseInto(dest);
        }
    }
}
