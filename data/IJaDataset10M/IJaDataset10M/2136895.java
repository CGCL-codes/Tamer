package net.sf.karatasi.desktop;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import net.sf.karatasi.librarian.DatabaseLibrarian;
import net.sf.karatasi.librarian.DatabaseStatus;
import org.jetbrains.annotations.NotNull;

public class DatabaseListModel extends DefaultListModel implements ChangeListener {

    /** Serial Id. */
    private static final long serialVersionUID = 3989497150468930633L;

    /** The associated Librarian instance. */
    private DatabaseLibrarian librarian = null;

    /** The controller list. It contains the active database operations controllers. */
    private final List<DatabaseActionController> controllerList = new LinkedList<DatabaseActionController>();

    /** Event listener list for the database list change events. */
    private final EventListenerList changeListenerList = new EventListenerList();

    /** Event when the database list has changed
     * created lazily, as in the Sun example.
     */
    private ChangeEvent inventoryIsChanged = null;

    /** Constructor. */
    public DatabaseListModel() {
        super();
    }

    /** Connect the model to it's data sources.
     * @param librarian the associated DatabaseLibrarian instance.
     */
    public void connectModel(@NotNull final DatabaseLibrarian librarian) {
        if (this.librarian != null) {
            this.librarian.removeDatabaseListChangeListener(this);
        }
        this.librarian = librarian;
        librarian.addDatabaseListChangeListener(this);
        this.stateChanged(new ChangeEvent(this));
    }

    /** Disconnect the model from it's data sources. */
    public void disconnectModel() {
        if (this.librarian != null) {
            this.librarian.removeDatabaseListChangeListener(this);
        }
        this.librarian = null;
    }

    /** Register an action controller.
     * @param actionController the new action controller.
     */
    public void addPanel(@NotNull final DatabaseActionController actionController) {
        controllerList.add(actionController);
        for (int n = 0; n < this.size(); n++) {
            final DatabaseListEntry entry = (DatabaseListEntry) this.elementAt(n);
            if (entry.getDbStatus().getFullName().equals(actionController.getDatabaseFullName())) {
                entry.setDatabaseLoaded(true);
                fireContentsChanged(this, n, n);
            }
        }
        fireDatabaseChangeEvent();
    }

    /** Remove an action controller.
     * @param actionController the removed action controller.
     */
    public void removePanel(@NotNull final DatabaseActionController actionController) {
        controllerList.remove(actionController);
        for (int n = 0; n < this.size(); n++) {
            final DatabaseListEntry entry = (DatabaseListEntry) this.elementAt(n);
            if (entry.getDbStatus().getFullName().equals(actionController.getDatabaseFullName())) {
                entry.setDatabaseLoaded(false);
                fireContentsChanged(this, n, n);
            }
        }
        fireDatabaseChangeEvent();
    }

    /** Get the list of action controllers.
     * @return the controller list.
     */
    public final List<DatabaseActionController> getControllerList() {
        return controllerList;
    }

    /** Get the status information of a database in the table.
     * @param idx the index in the list.
     */
    public DatabaseListEntry getDatabaseStatus(final int idx) {
        return (DatabaseListEntry) this.elementAt(idx);
    }

    /** Register a listener for database list changes.
     *
     * @param listener the new listener
     */
    public void addDatabaseListChangeListener(final ChangeListener listener) {
        changeListenerList.add(ChangeListener.class, listener);
    }

    /** Remove a listener for database list changes.
     *
     * @param listener the listener to be removed
     */
    public void removeDatabaseListChangeListener(final ChangeListener listener) {
        changeListenerList.remove(ChangeListener.class, listener);
    }

    /** Send a change event for the database list to the listeners.
     *
     */
    private void fireDatabaseChangeEvent() {
        final Object[] listeners = changeListenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ChangeListener.class) {
                if (inventoryIsChanged == null) {
                    inventoryIsChanged = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(inventoryIsChanged);
            }
        }
    }

    /** Invoked when the target of the listener has changed its state.
     * This event may be called from any thread context!
     *
     * @param event a ChangeEvent object
     */
    public void stateChanged(final ChangeEvent event) {
        try {
            final Runnable task = new Runnable() {

                public void run() {
                    updateContent();
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                task.run();
            } else {
                SwingUtilities.invokeAndWait(task);
            }
        } catch (final InterruptedException e) {
            Logger.getLogger("net.sf.japi.net.rest").log(Level.WARNING, "DatabaseListModel.stateChanged failed: ", e);
        } catch (final InvocationTargetException e) {
            Logger.getLogger("net.sf.japi.net.rest").log(Level.WARNING, "DatabaseListModel.stateChanged failed: ", e);
        }
    }

    /** Re-read the content from the model.
     * - replace status information for existing databases
     * - add new databases to the end
     * - remove obsolete databases
     */
    public void updateContent() {
        if (librarian == null) {
            return;
        }
        final List<DatabaseStatus> newStatusList = librarian.getDatabaseStatusList();
        for (int n = 0; n < this.size(); n++) {
            ((DatabaseListEntry) this.elementAt(n)).setUpdateMarker(0);
        }
        for (final DatabaseStatus newStatus : newStatusList) {
            final String newFullName = newStatus.getFullName();
            String entryFullName = null;
            int entryIdx = 0;
            int compareResult = 0;
            for (entryIdx = 0; entryIdx < this.size(); entryIdx++) {
                entryFullName = ((DatabaseListEntry) this.elementAt(entryIdx)).getDbStatus().getFullName();
                compareResult = entryFullName.compareToIgnoreCase(newFullName);
                if (compareResult >= 0) {
                    break;
                }
            }
            if (entryIdx >= this.size()) {
                final DatabaseListEntry newEntry = new DatabaseListEntry(newStatus, false, 1);
                updateLoadedFlag(newEntry);
                this.addElement(newEntry);
            } else if (entryFullName.equals(newFullName)) {
                final DatabaseListEntry newEntry = new DatabaseListEntry(newStatus, ((DatabaseListEntry) this.elementAt(entryIdx)).isLoaded(), 1);
                updateLoadedFlag(newEntry);
                this.set(entryIdx, newEntry);
                fireContentsChanged(this, entryIdx, entryIdx);
            } else {
                final DatabaseListEntry newEntry = new DatabaseListEntry(newStatus, false, 1);
                updateLoadedFlag(newEntry);
                this.add(entryIdx, newEntry);
            }
        }
        for (int n = this.size() - 1; n >= 0; n--) {
            if (((DatabaseListEntry) this.elementAt(n)).getUpdateMarker() == 0) {
                this.removeElementAt(n);
            }
        }
        fireDatabaseChangeEvent();
    }

    /** Set the loaded flag for a database list entry.
     * @param entry the database entry
     */
    private void updateLoadedFlag(final DatabaseListEntry entry) {
        for (final DatabaseActionController controller : controllerList) {
            if (entry.getDbStatus().getFullName().equals(controller.getDatabaseFullName())) {
                entry.setDatabaseLoaded(true);
            }
        }
    }
}
