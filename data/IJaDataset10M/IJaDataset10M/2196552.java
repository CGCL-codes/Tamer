package net.sf.karatasi.desktop;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import net.sf.japi.codecs.Codecs;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.karatasi.codecs.DatabaseExporter;
import net.sf.karatasi.codecs.DatabaseImporter;
import net.sf.karatasi.database.Database;
import org.jetbrains.annotations.NotNull;

/** This is the database list of the GUI.
 * It is responsible to present the list of databases including their status on the user interface,
 * and to provide the user with an interface to select database(s) and an action to be performed with this database(s).
 * It implements the JList interface for selection and an event interface for notification if an update is required.
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 *
 */
public class DatabaseList extends JList implements DatabaseListInterface {

    /** Serial ID */
    private static final long serialVersionUID = -2112491782876138903L;

    /** The ActionBuilder. */
    private final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder(GuiMain.class);

    /** The main panel for the action screens. */
    private final GuiMainPanel mainPanel;

    /** The data model of the list. */
    private DatabaseListModel dataModel = null;

    /** The controller of this view. */
    private DatabaseListController listController = null;

    /** Constructor.
     *
     * @param mainPanel the panel to add the action panels.
     */
    public DatabaseList(@NotNull final GuiMainPanel mainPanel) {
        super();
        this.mainPanel = mainPanel;
        this.setSelectionModel(new MySelectionModel());
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        final ListCellRenderer databaseListCellRenderer = new DatabaseListCellRenderer();
        this.setCellRenderer(databaseListCellRenderer);
        this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        this.setSelectedIndex(0);
    }

    /** Set the data model for the list.
     * @param dataModel the model
     */
    public void setDataModel(final DatabaseListModel dataModel) {
        this.dataModel = dataModel;
        super.setModel(dataModel);
    }

    /** Set the controller of this view.
     * @param controller the controller.
     */
    public void setController(final DatabaseListController controller) {
        this.listController = controller;
    }

    /** Overloaded mouse event to handle double click.
     *
     * @param event the mouse event.
     */
    @Override
    protected void processMouseEvent(final MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_PRESSED && event.getButton() == MouseEvent.BUTTON1) {
            final int index = locationToIndex(event.getPoint());
            final DatabaseListEntry entry = (DatabaseListEntry) getModel().getElementAt(index);
            if (entry.isLoaded()) {
                for (final DatabaseActionController controller : dataModel.getControllerList()) {
                    if (entry.getDbStatus().getFullName().equals(controller.getDatabaseFullName())) {
                        mainPanel.showPanel(controller.getPanel());
                        break;
                    }
                }
            }
        }
        if (event.getID() == MouseEvent.MOUSE_PRESSED && event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() >= 2) {
            final int index = locationToIndex(event.getPoint());
            if (index < 0) {
                return;
            }
            if (this.isSelectedIndex(index)) {
                if (listController != null) {
                    listController.doViewOneDatabase((DatabaseListEntry) getModel().getElementAt(index));
                    return;
                }
            }
        }
        super.processMouseEvent(event);
    }

    /** Overloaded key event to handle the return key.
    *
    * @param event the key event.
    */
    @Override
    protected void processKeyEvent(final KeyEvent event) {
        if (event.getID() == KeyEvent.KEY_PRESSED && event.getKeyCode() == KeyEvent.VK_ENTER) {
            final Object[] ob = getSelectedValues();
            if (ob.length == 1 && listController != null) {
                listController.doViewOneDatabase((DatabaseListEntry) ob[0]);
                return;
            }
        }
        super.processKeyEvent(event);
    }

    /** A specialized selection model.
     * Implements selection toggling (http://java.sun.com/products/jfc/tsc/tech_topics/jlist_1/jlist.html).
     */
    private static class MySelectionModel extends DefaultListSelectionModel {

        /** Serial Id. */
        private static final long serialVersionUID = 8878216126325290446L;

        /** Ignore multiple calls for same gesture. */
        private boolean gestureStarted = false;

        /** Overloaded to handle selection toggling.
         * @see DefaultListSelectionModel#setSelectionInterval(index0,index1).
         */
        @Override
        public void setSelectionInterval(final int index0, final int index1) {
            if (index0 == index1 && isSelectedIndex(index0) && !gestureStarted) {
                super.removeSelectionInterval(index0, index1);
            } else {
                super.setSelectionInterval(index0, index1);
            }
            gestureStarted = true;
        }

        /** Overloaded to track gesture changes.
         * @see DefaultListSelectionModel#setValueIsAdjusting(isAdjusting).
         */
        @Override
        public void setValueIsAdjusting(final boolean isAdjusting) {
            if (!isAdjusting) {
                gestureStarted = false;
            }
        }
    }

    /** Get the list of selected indices.
     * @return a table of indices
     */
    @Override
    public int[] getSelectedIndices() {
        return super.getSelectedIndices();
    }

    /** Clear the selection of the list. */
    @Override
    public void clearSelection() {
        super.clearSelection();
    }

    /** Add a database action panel.
     * @param panel the panel of the action controller.
     * @param title the panel title string.
     */
    public void addPanel(@NotNull final JPanel panel, @NotNull final String title) {
        mainPanel.addDatabaseActionPanel(panel, title);
    }

    /** Clean up panel after an action has finished.
     * @param panel the panel of the action controller.
     */
    public void removePanel(@NotNull final JPanel panel) {
        mainPanel.removePanel(panel);
    }

    /** Update the title of the panel.
     * @param panel the panel of the action controller.
     * @param newTitle the new panel title.
     */
    public void updatePanelTitle(@NotNull final Component panel, @NotNull final String newTitle) {
        mainPanel.updatePanelTitle(panel, newTitle);
    }

    /** Display a warning message panel.
     * @param titleSelector the selector string of tile of the panel.
     * @param  message the message string
     */
    public void showWarningDialog(@NotNull final String titleSelector, @NotNull final String message) {
        JOptionPane.showMessageDialog(this.getParent(), message, actionBuilder.getString(titleSelector), JOptionPane.WARNING_MESSAGE);
    }

    /** Display a confirmation dialog.
     * @param titleSelector the selector string of tile of the panel.
     * @param message the message string
     * @param optionType the type of the confirmation options
     * @returns the selection {@see JOptionPane.showConfirmDialog}.
     */
    public int showConfirmationDialog(@NotNull final String titleSelector, @NotNull final String message, final int optionType) {
        return JOptionPane.showConfirmDialog(this.getParent(), message, actionBuilder.getString(titleSelector), optionType, JOptionPane.QUESTION_MESSAGE);
    }

    /** Display a dialog to enter a new database name (for renaming)
     * @param oldName
     * @returns the new name
     */
    public String showNewDatabaseNameDialog(@NotNull final String oldName) {
        return (String) JOptionPane.showInputDialog(this, actionBuilder.getString("renameDatabase.enter.name"), actionBuilder.getString("renameDatabase.title"), JOptionPane.PLAIN_MESSAGE, null, null, oldName);
    }

    /** Select a file or directory.
     * @param chooseImportFile true if we are selecting files for import, if false for export.
     * @param multiSelect true if multiple files selection shall be supported
     * @param databaseCodecs the codecs for exporting.
     * @param fileName preset filename.
     * @return the selection (file and filter) or null if nothing has been selected.
     */
    @SuppressWarnings("unchecked")
    public DatabaseListInterface.FileSystemSelection selectFromFilesystem(final boolean chooseImportFile, final boolean multiSelect, @NotNull final Object databaseCodecs, final String fileName) {
        JFileChooser fileChooser;
        if (chooseImportFile == FilesystemChooser.CHOOSE_EXPORT) {
            fileChooser = new FilesystemChooser<DatabaseExporter>(chooseImportFile, multiSelect, (Codecs<Database, DatabaseExporter>) databaseCodecs);
        } else {
            fileChooser = new FilesystemChooser<DatabaseImporter>(chooseImportFile, multiSelect, (Codecs<Database, DatabaseImporter>) databaseCodecs);
        }
        if (fileName != null) {
            fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), fileName));
        }
        int result;
        if (chooseImportFile == FilesystemChooser.CHOOSE_EXPORT) {
            result = fileChooser.showSaveDialog(this.getParent());
        } else {
            result = fileChooser.showOpenDialog(this.getParent());
        }
        if (result == JFileChooser.APPROVE_OPTION) {
            final FileFilter selectedFilter = fileChooser.getFileFilter();
            final File selectedFile;
            if (multiSelect) {
                selectedFile = fileChooser.getCurrentDirectory();
            } else {
                selectedFile = fileChooser.getSelectedFile();
            }
            return new DatabaseListInterface.FileSystemSelection(selectedFile, selectedFilter);
        }
        return null;
    }
}
