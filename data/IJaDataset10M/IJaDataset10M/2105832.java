package net.sf.karatasi.databaseoperations;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.AbstractListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import net.sf.karatasi.KaratasiPreferences;
import net.sf.karatasi.desktop.KaratasiDesktopPreferences;
import net.sf.karatasi.librarian.DatabaseLibrarian;
import net.sf.karatasi.librarian.DatabaseStatus;
import net.sf.karatasi.util.SwingSynchronizer;
import net.sf.karatasi.viewmodels.ReorderingListener;
import org.jetbrains.annotations.NotNull;

public class DatabaseListModel extends AbstractListModel implements ChangeListener, ReorderingListener {

    /** Serial Id. */
    private static final long serialVersionUID = 3989497150468930633L;

    /** The Preferences to store the database list. */
    private final Preferences prefs = KaratasiDesktopPreferences.create();

    /** The associated Librarian instance. */
    private DatabaseLibrarian librarian = null;

    /** The list of databases. */
    private final AbstractList<DatabaseListEntry> databaseList = new Vector<DatabaseListEntry>();

    private final List<AbstractDatabaseActionController> controllerList = new LinkedList<AbstractDatabaseActionController>();

    /** The reference list for database ordering, it gets loaded from the preferences. */
    private String[] databaseNamesOrderList = null;

    /** Event listener list for the database list change events. */
    private final EventListenerList changeListenerList = new EventListenerList();

    /** Event when the database list has changed
     * created lazily, as in the Sun example.
     */
    private ChangeEvent inventoryIsChanged = null;

    /** Constructor. */
    public DatabaseListModel() {
        super();
        readDatabaseReferenceListFromPrefs();
    }

    /** Clear the database name order reference list. */
    public void clearDatabaseNameOrderReferenceList() {
        databaseNamesOrderList = null;
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

    /** The panel for a database was opened.
     * @param dbFullName the database full name
     */
    public void panelOpened(final String dbFullName) {
        for (int n = 0; n < databaseList.size(); n++) {
            final DatabaseListEntry entry = databaseList.get(n);
            if (entry.getDbName().equals(dbFullName)) {
                entry.setDatabaseLoaded(true);
                fireContentsChanged(this, n, n);
            }
        }
        fireDatabaseChangeEvent();
    }

    /** The panel for a database was closed.
     * @param dbFullName the database full name
     */
    public void panelClosed(final String dbFullName) {
        for (int n = 0; n < databaseList.size(); n++) {
            final DatabaseListEntry entry = databaseList.get(n);
            if (entry.getDbName().equals(dbFullName)) {
                entry.setDatabaseLoaded(false);
                fireContentsChanged(this, n, n);
            }
        }
        fireDatabaseChangeEvent();
    }

    /** Get the list of action controllers.
     * @return the controller list.
     */
    public final List<AbstractDatabaseActionController> getControllerList() {
        return controllerList;
    }

    /** Get a database entry from the model.
     * @param idx the index in the list.
     */
    public DatabaseListEntry getEntry(final int idx) {
        return databaseList.get(idx);
    }

    /** Get a database entry from the model.
     * @param name the database full name.
     * @return the entry / null if not found.
     */
    private DatabaseListEntry getEntry(@NotNull final String name) {
        final int size = databaseList.size();
        for (int idx = 0; idx < size; idx++) {
            final DatabaseListEntry entry = databaseList.get(idx);
            if (entry.getDbName().equals(name)) {
                return entry;
            }
        }
        return null;
    }

    /** Get the name of a database in the table.
     * @param idx the index in the list.
     */
    public String getDatabaseName(final int idx) {
        return getEntry(idx).getDbName();
    }

    /** Resolve a list of indices to a list of database names.
     * @param indices the list of indices
     * @return the list of database names
     */
    @NotNull
    public List<String> getDbNames(@NotNull final int[] indices) {
        final List<String> names = new ArrayList<String>(indices.length);
        for (int n = 0; n < indices.length; n++) {
            names.add(getDatabaseName(indices[n]));
        }
        return names;
    }

    /** Query if the referenced database is loaded into the right panel.
     * @param name the database full name
     * @return true if database is loaded / false if not.
     */
    public boolean isLoaded(@NotNull final String name) {
        final DatabaseListEntry entry = getEntry(name);
        if (entry == null) {
            return false;
        }
        return entry.isLoaded();
    }

    /** Query if the referenced database is loaded into the right panel.
     * @param idx idx the index in the list.
     * @return true if database is loaded / false if not.
     */
    public boolean isLoaded(final int idx) {
        return getEntry(idx).isLoaded();
    }

    /** Query if the referenced database is operational (healthy and of the correct version).
     * @param name the database full name
     * @return true if database is operational / false if not.
     */
    public boolean isOperational(@NotNull final String name) {
        final DatabaseListEntry entry = getEntry(name);
        if (entry == null) {
            return false;
        }
        return entry.getDbStatus().isOperational();
    }

    /** Save the database list to the persistent preferences.
     */
    private void saveDatabaseListToPrefs() {
        String prevString = "";
        for (int n = 0; n < databaseList.size(); n++) {
            try {
                prevString = prevString + " " + URLEncoder.encode(databaseList.get(n).getDbName(), "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                Logger.getLogger("net.sf.japi.net.rest").warning("url encoding for UTF-8 not supported, no database ordering persistence");
                return;
            }
        }
        prefs.put(KaratasiPreferences.KEEP_KEY_DATABASE_LIST, prevString);
    }

    /** Read the database names reference list from the preferences.
     * @throws UnsupportedEncodingException */
    private void readDatabaseReferenceListFromPrefs() {
        final String prefString = prefs.get(KaratasiPreferences.KEEP_KEY_DATABASE_LIST, "");
        databaseNamesOrderList = prefString.split(" ");
        for (int n = 0; n < databaseNamesOrderList.length; n++) {
            try {
                databaseNamesOrderList[n] = URLDecoder.decode(databaseNamesOrderList[n], "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                Logger.getLogger("net.sf.japi.net.rest").warning("url encoding for UTF-8 not supported, no database ordering persistence");
            }
        }
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
        final Runnable task = new Runnable() {

            public void run() {
                updateContent();
            }
        };
        SwingSynchronizer.runSynchronized(task);
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
        for (int n = 0; n < databaseList.size(); n++) {
            databaseList.get(n).setUpdateMarker(0);
        }
        for (final DatabaseStatus newStatus : newStatusList) {
            final String newFullName = newStatus.getFullName();
            String entryFullName = null;
            int entryIdx = 0;
            for (entryIdx = 0; entryIdx < databaseList.size(); entryIdx++) {
                entryFullName = this.getDatabaseName(entryIdx);
                if (entryFullName.compareTo(newFullName) == 0) {
                    break;
                }
            }
            if (entryIdx >= databaseList.size()) {
                final DatabaseListEntry newEntry = new DatabaseListEntry(newStatus, false, 1);
                updateLoadedFlag(newEntry);
                databaseList.add(newEntry);
                fireIntervalAdded(this, databaseList.size() - 1, databaseList.size() - 1);
            } else if (entryFullName.equals(newFullName)) {
                final DatabaseListEntry newEntry = new DatabaseListEntry(newStatus, this.isLoaded(entryIdx), 1);
                updateLoadedFlag(newEntry);
                databaseList.set(entryIdx, newEntry);
                fireContentsChanged(this, entryIdx, entryIdx);
            } else {
                final DatabaseListEntry newEntry = new DatabaseListEntry(newStatus, false, 1);
                updateLoadedFlag(newEntry);
                databaseList.add(entryIdx, newEntry);
                fireIntervalAdded(this, entryIdx, entryIdx);
            }
        }
        for (int n = databaseList.size() - 1; n >= 0; n--) {
            if (databaseList.get(n).getUpdateMarker() == 0) {
                databaseList.remove(n);
                fireIntervalRemoved(this, n, n);
            }
        }
        if (databaseNamesOrderList != null) {
            int toIdx = 0;
            for (int refIdx = 0; refIdx < databaseNamesOrderList.length; refIdx++) {
                for (int thisIdx = toIdx; thisIdx < databaseList.size(); thisIdx++) {
                    if (databaseNamesOrderList[refIdx].equals(this.getDatabaseName(thisIdx))) {
                        if (toIdx != thisIdx) {
                            databaseList.add(toIdx, databaseList.remove(thisIdx));
                            fireContentsChanged(this, toIdx, thisIdx);
                        }
                        toIdx++;
                        break;
                    }
                }
            }
        }
        saveDatabaseListToPrefs();
        fireDatabaseChangeEvent();
    }

    /** Set the loaded flag for a database list entry if there exists a controller associated to this entry.
     * The association goes via the database full name.
     * @param entry the database entry
     */
    private void updateLoadedFlag(final DatabaseListEntry entry) {
        final String fullName = entry.getDbName();
        if (controllerExistsFor(fullName)) {
            entry.setDatabaseLoaded(true);
        }
    }

    /** Determine whether the database is in use by a database action controller.
     * @param fullName of the database.
     * @return true if a database action controller exists for the database / false if not.
     */
    private boolean controllerExistsFor(final String fullName) {
        boolean isLoaded = false;
        for (final AbstractDatabaseActionController controller : getControllerList()) {
            if (fullName.equals(controller.getDatabaseFullName())) {
                isLoaded = true;
            }
        }
        return isLoaded;
    }

    /** ReorderingListener: Move a row to a new position
     * @param index original position
     * @param offset the offset of the new position
     * @return if true the operation was successful, else the row wasn't moved
     */
    public boolean moveRow(final int index, final int offset) {
        if (offset == 0) {
            return false;
        }
        final int newIndex = index + offset;
        if (newIndex < 0 || newIndex >= databaseList.size()) {
            return false;
        }
        databaseList.add(newIndex, databaseList.remove(index));
        saveDatabaseListToPrefs();
        databaseNamesOrderList = null;
        fireContentsChanged(this, index, newIndex);
        return true;
    }

    /** Get an object from the list.
     * @param index the index of the element in the database list
     * @return the database list entry as an Object.
     */
    public Object getElementAt(final int index) {
        return databaseList.get(index);
    }

    /** Get the entry count
     * @return the count
     */
    public int getSize() {
        return databaseList.size();
    }
}
