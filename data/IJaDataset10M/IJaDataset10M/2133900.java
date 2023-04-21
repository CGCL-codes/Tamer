package org.gamegineer.table.internal.ui.model;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentLegal;
import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.gamegineer.table.internal.ui.Loggers;

/**
 * The top-level model.
 */
@ThreadSafe
public final class MainModel {

    /** The collection of main model listeners. */
    private final CopyOnWriteArrayList<IMainModelListener> listeners_;

    /** The preferences model. */
    private final PreferencesModel preferencesModel_;

    /** The table model. */
    private final TableModel tableModel_;

    /**
     * Initializes a new instance of the {@code MainModel} class.
     */
    public MainModel() {
        listeners_ = new CopyOnWriteArrayList<IMainModelListener>();
        preferencesModel_ = new PreferencesModel();
        tableModel_ = new TableModel();
        tableModel_.addTableModelListener(new TableModelListener());
    }

    /**
     * Adds the specified main model listener to this main model.
     * 
     * @param listener
     *        The main model listener; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code listener} is already a registered main model listener.
     * @throws java.lang.NullPointerException
     *         If {@code listener} is {@code null}.
     */
    public void addMainModelListener(final IMainModelListener listener) {
        assertArgumentNotNull(listener, "listener");
        assertArgumentLegal(listeners_.addIfAbsent(listener), "listener", NonNlsMessages.MainModel_addMainModelListener_listener_registered);
    }

    /**
     * Fires a main model state changed event.
     */
    private void fireMainModelStateChanged() {
        final MainModelEvent event = new MainModelEvent(this);
        for (final IMainModelListener listener : listeners_) {
            try {
                listener.mainModelStateChanged(event);
            } catch (final RuntimeException e) {
                Loggers.getDefaultLogger().log(Level.SEVERE, NonNlsMessages.MainModel_mainModelStateChanged_unexpectedException, e);
            }
        }
    }

    public PreferencesModel getPreferencesModel() {
        return preferencesModel_;
    }

    public TableModel getTableModel() {
        return tableModel_;
    }

    /**
     * Loads the main model from persistent storage.
     */
    public void load() {
        preferencesModel_.load();
    }

    /**
     * Opens a new empty table.
     */
    public void openTable() {
        tableModel_.open();
    }

    /**
     * Opens an existing table from the specified file.
     * 
     * @param file
     *        The file from which the table will be opened; must not be
     *        {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code file} is {@code null}.
     * @throws org.gamegineer.table.internal.ui.model.ModelException
     *         If an error occurs while opening the file.
     */
    public void openTable(final File file) throws ModelException {
        assertArgumentNotNull(file, "file");
        try {
            tableModel_.open(file);
        } catch (final ModelException e) {
            preferencesModel_.getFileHistoryPreferences().removeFile(file);
            throw e;
        }
        preferencesModel_.getFileHistoryPreferences().addFile(file);
    }

    /**
     * Removes the specified main model listener from this main model.
     * 
     * @param listener
     *        The main model listener; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code listener} is not a registered main model listener.
     * @throws java.lang.NullPointerException
     *         If {@code listener} is {@code null}.
     */
    public void removeMainModelListener(final IMainModelListener listener) {
        assertArgumentNotNull(listener, "listener");
        assertArgumentLegal(listeners_.remove(listener), "listener", NonNlsMessages.MainModel_removeMainModelListener_listener_notRegistered);
    }

    /**
     * Stores the main model to persistent storage.
     */
    public void save() {
        preferencesModel_.save();
    }

    /**
     * Saves the current table to the specified file.
     * 
     * @param file
     *        The file to which the table will be saved; must not be
     *        {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code file} is {@code null}.
     * @throws org.gamegineer.table.internal.ui.model.ModelException
     *         If an error occurs while saving the file.
     */
    public void saveTable(final File file) throws ModelException {
        assertArgumentNotNull(file, "file");
        try {
            tableModel_.save(file);
        } catch (final ModelException e) {
            preferencesModel_.getFileHistoryPreferences().removeFile(file);
            throw e;
        }
        preferencesModel_.getFileHistoryPreferences().addFile(file);
    }

    /**
     * A table model listener for the main model.
     */
    @Immutable
    private final class TableModelListener extends org.gamegineer.table.internal.ui.model.TableModelListener {

        /**
         * Initializes a new instance of the {@code TableModelListener} class.
         */
        TableModelListener() {
        }

        @Override
        @SuppressWarnings("synthetic-access")
        public void tableChanged(final TableModelEvent event) {
            assertArgumentNotNull(event, "event");
            fireMainModelStateChanged();
        }

        @Override
        @SuppressWarnings("synthetic-access")
        public void tableModelDirtyFlagChanged(final TableModelEvent event) {
            assertArgumentNotNull(event, "event");
            fireMainModelStateChanged();
        }

        @Override
        @SuppressWarnings("synthetic-access")
        public void tableModelFileChanged(final TableModelEvent event) {
            assertArgumentNotNull(event, "event");
            fireMainModelStateChanged();
        }

        @Override
        @SuppressWarnings("synthetic-access")
        public void tableModelFocusChanged(final TableModelEvent event) {
            assertArgumentNotNull(event, "event");
            fireMainModelStateChanged();
        }
    }
}
