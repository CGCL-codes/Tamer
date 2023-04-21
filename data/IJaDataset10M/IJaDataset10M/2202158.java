package org.makagiga.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

/**
 * @since 2.0
 */
public abstract class AbstractListTableModel<T> extends AbstractTableModel implements Iterable<T>, Lockable, UI.EventsControl {

    private boolean eventsEnabled = true;

    private boolean locked;

    private boolean undoInProgress;

    private ColumnInfo[] columnInfo;

    private int columnCount;

    private List<T> backend;

    private UndoableEditSupport undoSupport;

    /**
	 * Constructs a new model with @c ArrayList as the backend.
	 */
    public AbstractListTableModel(final int columnCount) {
        this(columnCount, new ArrayList<T>(100));
    }

    /**
	 * @throws NullPointerException If @p backend is @c null
	 */
    public AbstractListTableModel(final int columnCount, final List<T> backend) {
        this.columnCount = columnCount;
        this.backend = TK.checkNull(backend, "backend");
    }

    /**
	 * @throws IllegalArgumentException If @p columnInfo length is zero
	 * @throws NullPointerException If @p columnInfo is @c null
	 * 
	 * @since 2.2
	 */
    public AbstractListTableModel(final ColumnInfo... columnInfo) {
        this(new ArrayList<T>(100), columnInfo);
    }

    /**
	 * @throws IllegalArgumentException If @p columnInfo length is zero
	 * @throws NullPointerException If @p backend or @p columnInfo is @c null
	 * 
	 * @since 2.4
	 */
    public AbstractListTableModel(final List<T> backend, final ColumnInfo... columnInfo) {
        this(columnInfo.length, backend);
        setColumnInfo(columnInfo);
    }

    public void addRow(final T row) {
        backend.add(row);
        if (eventsEnabled) {
            int i = backend.size() - 1;
            T copy = createCopyForUndo(row);
            fireTableRowsInserted(i, i);
            if (copy != null) fireUndoableEditHappened(new InsertUndo(copy, i));
        }
    }

    public void addUndoableEditListener(final UndoableEditListener l) {
        if (undoSupport == null) undoSupport = new UndoableEditSupport();
        undoSupport.addUndoableEditListener(l);
    }

    /**
	 * @since 3.0
	 */
    public UndoableEditListener[] getUndoableEditListeners() {
        if (undoSupport != null) return undoSupport.getUndoableEditListeners();
        return MUndoManager.EMPTY_UNDOABLE_EDIT_LISTENER_ARRAY;
    }

    public void removeUndoableEditListener(final UndoableEditListener l) {
        if (undoSupport != null) undoSupport.removeUndoableEditListener(l);
    }

    public void clear() {
        if (!backend.isEmpty()) {
            int size = backend.size();
            List<T> copy = eventsEnabled ? createCopyForUndo(0, size) : null;
            backend.clear();
            if (eventsEnabled) {
                fireTableRowsDeleted(0, size - 1);
                if (copy != null) fireUndoableEditHappened(new RemoveUndo(copy, 0));
            }
        }
    }

    @Override
    public Class<?> getColumnClass(final int column) {
        return (columnInfo == null) ? super.getColumnClass(column) : columnInfo[column].getColumnClass();
    }

    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(final int column) {
        return (columnInfo == null) ? super.getColumnName(column) : columnInfo[column].getName();
    }

    public boolean getEventsEnabled() {
        return eventsEnabled;
    }

    public void setEventsEnabled(final boolean value) {
        eventsEnabled = value;
    }

    public T getRowAt(final int index) {
        return backend.get(index);
    }

    public int getRowCount() {
        return backend.size();
    }

    public List<T> getRows() {
        return backend;
    }

    public void insertRow(final int index, final T row) {
        backend.add(index, row);
        if (eventsEnabled) {
            T copy = createCopyForUndo(row);
            fireTableRowsInserted(index, index);
            if (copy != null) fireUndoableEditHappened(new InsertUndo(copy, index));
        }
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        if (locked) return false;
        return (columnInfo == null) ? true : columnInfo[column].isEditable();
    }

    public boolean isEmpty() {
        return backend.isEmpty();
    }

    public void removeRow(final int index) {
        List<T> copy = eventsEnabled ? createCopyForUndo(index, 1) : null;
        backend.remove(index);
        if (eventsEnabled) {
            fireTableRowsDeleted(index, index);
            if (copy != null) fireUndoableEditHappened(new RemoveUndo(copy, index));
        }
    }

    public Iterator<T> iterator() {
        return backend.iterator();
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(final boolean value) {
        locked = value;
    }

    protected T createCopyForUndo(final T original) {
        return original;
    }

    protected void fireUndoableEditHappened(final UndoableEdit edit) {
        if (undoInProgress) return;
        if (undoSupport != null) undoSupport.postEdit(edit);
    }

    /**
	 * @since 3.0
	 */
    protected void setColumnInfo(final ColumnInfo... columnInfo) {
        if (columnInfo.length == 0) throw new IllegalArgumentException("Empty \"columnInfo\" array");
        this.columnCount = columnInfo.length;
        this.columnInfo = new ColumnInfo[columnInfo.length];
        for (int i = 0; i < columnInfo.length; i++) {
            this.columnInfo[i] = columnInfo[i];
            this.columnInfo[i].setModelIndex(i);
        }
    }

    private List<T> createCopyForUndo(final int startRow, final int count) {
        List<T> result = new ArrayList<T>(count);
        int index = startRow;
        for (int i = 0; i < count; i++) {
            result.add(createCopyForUndo(backend.get(index)));
            index++;
        }
        return result;
    }

    private void doChange(final RedoAction action) {
        try {
            undoInProgress = true;
            action.redo();
        } catch (Exception exception) {
            MLogger.exception(exception);
            throw new CannotRedoException();
        } finally {
            undoInProgress = false;
        }
    }

    private void doChange(final UndoAction action) {
        try {
            undoInProgress = true;
            action.undo();
        } catch (Exception exception) {
            MLogger.exception(exception);
            throw new CannotUndoException();
        } finally {
            undoInProgress = false;
        }
    }

    /**
	 * @since 2.2
	 */
    public static class ColumnInfo extends TableColumn {

        private boolean editable;

        private Class<?> columnClass;

        private String name;

        public ColumnInfo(final String name) {
            this(name, Object.class, true);
        }

        public ColumnInfo(final String name, final boolean editable) {
            this(name, Object.class, editable);
        }

        public ColumnInfo(final String name, final Class<?> columnClass) {
            this(name, columnClass, true);
        }

        public ColumnInfo(final String name, final Class<?> columnClass, final boolean editable) {
            this.name = name;
            this.columnClass = TK.checkNull(columnClass, "columnClass");
            this.editable = editable;
        }

        public Class<?> getColumnClass() {
            return columnClass;
        }

        public String getName() {
            return name;
        }

        public boolean isEditable() {
            return editable;
        }
    }

    public final class ChangeUndo extends AbstractUndoableEdit {

        private int row;

        private T after;

        private T before;

        public ChangeUndo(final T before, final T after, final int row) {
            this.before = before;
            this.after = after;
            this.row = row;
        }

        @Override
        public void redo() {
            super.redo();
            AbstractListTableModel.this.doChange(new RedoAction() {

                public void redo() {
                    AbstractListTableModel.this.backend.set(row, after);
                    AbstractListTableModel.this.fireTableRowsUpdated(row, row);
                }
            });
        }

        @Override
        public void undo() {
            super.undo();
            AbstractListTableModel.this.doChange(new UndoAction() {

                public void undo() {
                    AbstractListTableModel.this.backend.set(row, before);
                    AbstractListTableModel.this.fireTableRowsUpdated(row, row);
                }
            });
        }
    }

    public final class InsertUndo extends AbstractUndoableEdit {

        private int row;

        private T data;

        public InsertUndo(final T data, final int row) {
            this.data = data;
            this.row = row;
        }

        @Override
        public void redo() {
            super.redo();
            AbstractListTableModel.this.doChange(new RedoAction() {

                public void redo() {
                    AbstractListTableModel.this.insertRow(row, data);
                }
            });
        }

        @Override
        public void undo() {
            super.undo();
            AbstractListTableModel.this.doChange(new UndoAction() {

                public void undo() {
                    AbstractListTableModel.this.removeRow(row);
                }
            });
        }
    }

    public final class RemoveUndo extends AbstractUndoableEdit {

        private int row;

        private List<T> data;

        public RemoveUndo(final List<T> data, final int row) {
            this.data = data;
            this.row = row;
        }

        @Override
        public void redo() {
            super.redo();
            AbstractListTableModel.this.doChange(new RedoAction() {

                public void redo() {
                    int count = data.size();
                    for (int i = row + count - 1; i >= row; --i) AbstractListTableModel.this.removeRow(i);
                }
            });
        }

        @Override
        public void undo() {
            super.undo();
            AbstractListTableModel.this.doChange(new UndoAction() {

                public void undo() {
                    int insertRow = row;
                    for (T i : data) {
                        AbstractListTableModel.this.insertRow(insertRow, i);
                        insertRow++;
                    }
                }
            });
        }
    }

    private static interface RedoAction {

        public void redo();
    }

    private static interface UndoAction {

        public void undo();
    }
}
