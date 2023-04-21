package org.freeworld.prilibext.swing;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import org.freeworld.prilib.column.ColumnSchema;
import org.freeworld.prilib.column.ColumnSchemaListener;
import org.freeworld.prilib.column.WeakColumnSchemaAdapter;
import org.freeworld.prilib.impl.base.NullTable;
import org.freeworld.prilib.impl.display.DisplayColumnSchema;
import org.freeworld.prilib.impl.display.DisplayTable;
import org.freeworld.prilib.table.Table;
import org.freeworld.prilib.util.PriTableEnabled;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 * <p>This is a TableColumnModel implementation that has been tested as a
 * suitable backend data model for a JXTable implementation. Any component that
 * used TableColumnModel will probably work with this, but since there is lack
 * of testing, there may be bugs not detected through JXTable that manifest
 * itself in other component types.</p>
 * 
 * TODO: Inherit this off PrilibTableColumnModel and only override the methods
 * that differ between the two as most of them share boiler plates
 * 
 * @author dchemko
 */
public class PrilibTableColumnModelExt extends DefaultTableColumnModelExt implements PriTableEnabled {

    private static final long serialVersionUID = 2484759351902514904L;

    private Table table = null;

    private List<TableColumn> columnTables = new ArrayList<TableColumn>();

    private transient ColumnSchemaListener schemaListenerA = null;

    /**
    * Because modifying the column schema may change the underlying model index
    * of the columns being rendered, we have to re-adjust the modex indexes
    * when the model changes to line up properly.
    */
    protected void fixColumns() {
        for (TableColumn tableColumn : columnTables) {
            if (tableColumn.getModelIndex() != table.getColumnIndex((String) tableColumn.getIdentifier())) tableColumn.setModelIndex(table.getColumnIndex((String) tableColumn.getIdentifier()));
        }
    }

    /**
    * <p>Constructes a table model without creating the back-end links into a
    * prilib Table. This column model will not be ready to receive UI events
    * until after the {@link #setTable(Table)} has been invoked, assigning this
    * interface to a real pirlib Table.</p>
    */
    public PrilibTableColumnModelExt() {
        super();
    }

    /**
    * <p>Constructes a table model and links it to a  back-end prilib table.
    * After returning, this model is ready to be used by UI components.</p>
    * @param table - The prilib table that will be the data model for this
    * interface
    */
    public PrilibTableColumnModelExt(Table table) {
        super();
        setTable(table);
    }

    @Override
    public void setTable(Table table) {
        if (this.table != null) {
            table.removeColumnSchemaListener(getSchemaListener());
            for (int i = getColumnCount() - 1; i > -1; i--) removeColumn(getColumn(i));
            columnTables.clear();
        }
        this.table = table;
        if (table != null) {
            String[] colNames = table.getColumnNames();
            for (int i = 0; i < table.getColumnCount(); i++) {
                TableColumn column = null;
                if (table.hasBinding(DisplayTable.class)) column = getTableColumnForSchema(i, table.getBinding(DisplayTable.class).getColumnSchema(colNames[i])); else column = getTableColumnForSchema(i, table.getColumnSchema(colNames[i]));
                addColumn(column);
                columnTables.add(column);
                fireColumnAdded(table, colNames[i]);
            }
            table.addColumnSchemaListener(new WeakColumnSchemaAdapter(table, getSchemaListener()));
        }
    }

    @Override
    public Table getTable() {
        if (table == null) return NullTable.getInstance();
        return table;
    }

    /**
    * <p>Notifies interested model listeners that a new column was added</p>
    * 
    * @param table - The root table generating the event
    * @param columnName - The name of the column being added
    */
    protected void fireColumnAdded(Table table, String columnName) {
        int index;
        try {
            index = getColumnIndex(columnName);
        } catch (IllegalArgumentException e) {
            return;
        }
        final TableColumnModelEvent event = new TableColumnModelEvent(this, -1, index);
        try {
            SwingHelper.testInvokeAndWait(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < getListeners(TableColumnModelListener.class).length; i++) {
                        try {
                            getListeners(TableColumnModelListener.class)[i].columnAdded(event);
                        } catch (Throwable t) {
                        }
                    }
                }
            });
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
    * <p>Notifies interested model listeners that a column was removed</p>
    * 
    * @param table - The root table generating the event
    * @param columnName - The name of the column being removed
    * @param columnIndex - The index of where the column used to exist
    */
    protected void fireColumnRemoved(Table table, int columnIndex, String columnName) {
        final TableColumnModelEvent event = new TableColumnModelEvent(this, columnIndex, -1);
        try {
            SwingHelper.testInvokeAndWait(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < getListeners(TableColumnModelListener.class).length; i++) getListeners(TableColumnModelListener.class)[i].columnRemoved(event);
                }
            });
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private TableColumn getTableColumnForSchema(int modelIndex, ColumnSchema schema) {
        TableColumnExt retr = new TableColumnExt();
        retr.setModelIndex(modelIndex);
        if (schema instanceof DisplayColumnSchema) {
            DisplayColumnSchema displaySchema = (DisplayColumnSchema) schema;
            retr.setHeaderValue(displaySchema.getDisplayName());
            retr.setVisible(!displaySchema.isHidden());
            if (displaySchema.getTableCellRenderer() != null) retr.setCellRenderer(displaySchema.getTableCellRenderer()); else if (PriRegistry.getTableCellRendererClass(displaySchema.getType()) != null) {
                try {
                    retr.setCellRenderer(PriRegistry.getTableCellRendererClass(displaySchema.getType()).newInstance());
                } catch (Throwable t) {
                }
            }
            if (displaySchema.getTableCellEditor() != null) retr.setCellEditor(displaySchema.getTableCellEditor()); else if (PriRegistry.getTableCellRendererClass(displaySchema.getType()) != null) {
                try {
                    retr.setCellEditor(PriRegistry.getTableCellEditorClass(displaySchema.getType()).newInstance());
                } catch (Throwable t) {
                }
            }
            if (displaySchema.getCharacterWidth() != DisplayColumnSchema.WIDTH_NO_LIMIT) {
                retr.setPreferredWidth(displaySchema.getCharacterWidth() * 7);
                retr.setWidth(displaySchema.getCharacterWidth() * 7);
                retr.setMinWidth(displaySchema.getCharacterWidth() * 7);
            } else if (displaySchema.getPixelWidth() != DisplayColumnSchema.WIDTH_NO_LIMIT) {
                retr.setPreferredWidth(displaySchema.getPixelWidth());
                retr.setWidth(displaySchema.getPixelWidth());
                retr.setMinWidth(displaySchema.getPixelWidth());
            }
        } else retr.setHeaderValue(schema.getName());
        retr.setModelIndex(modelIndex);
        retr.setIdentifier(schema.getName());
        return retr;
    }

    private ColumnSchemaListener getSchemaListener() {
        if (schemaListenerA == null) {
            schemaListenerA = new ColumnSchemaListener() {

                @Override
                public void columnAdded(String name, ColumnSchema schema) {
                    int requestedColumnIndex = table.getColumnIndex(name);
                    TableColumn column = getTableColumnForSchema(requestedColumnIndex, table.getColumnSchema(name));
                    columnTables.add(requestedColumnIndex, column);
                    boolean hasColumnNow = false;
                    for (int i = 0; i < getColumnCount(); i++) if (getColumn(i).getIdentifier().equals(name)) {
                        hasColumnNow = true;
                        break;
                    }
                    if (!hasColumnNow) {
                        if (!(column instanceof TableColumnExt) || ((TableColumnExt) column).isVisible()) {
                            addColumn(column);
                        }
                    }
                    fixColumns();
                }

                @Override
                public void columnRemoved(String name, ColumnSchema oldSchema) {
                    for (int i = 0; i < columnTables.size(); i++) {
                        if (columnTables.get(i).getIdentifier().equals(name)) {
                            int oldIdx = -1;
                            boolean hidden = columnTables.get(i) instanceof TableColumnExt && !((TableColumnExt) columnTables.get(i)).isVisible();
                            if (!hidden) oldIdx = getColumnIndex(name);
                            if (oldIdx > -1) {
                                removeColumn(columnTables.get(i));
                                columnTables.remove(i);
                            }
                            if (!hidden) fireColumnRemoved(table, oldIdx, name);
                            return;
                        }
                    }
                    fixColumns();
                }

                @Override
                public void columnUpdated(String name, ColumnSchema oldSchema, ColumnSchema newSchema) {
                    if (!oldSchema.getName().equals(newSchema.getName())) {
                        getColumnExt(oldSchema.getName()).setIdentifier(newSchema.getName());
                    }
                    fixColumns();
                }
            };
        }
        return schemaListenerA;
    }
}
