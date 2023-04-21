package ch.trackedbean.binding.components;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import ch.simpleel.*;
import ch.trackedbean.binding.action.*;
import ch.trackedbean.binding.components.renderers.*;
import ch.trackedbean.binding.mapping.*;
import ch.trackedbean.binding.util.*;
import ch.trackedbean.loaders.*;

/**
 * Panel holding a table and it's mapping.<br>
 * Use {@link #getPopupManager()} to configure the popup.<br>
 * Use {@link #getMapping()} for special mappings (so those not covered by the add methods).<br>
 * If you need to add selection dependent {@link ISelectionAction}s then have a look at {@link TableSelectionActionHandler}.<br>
 * 
 * @author M. Hautle
 * @param <T> The bean type
 */
public class TablePanel<T> extends JPanel {

    /** The table show on this panel */
    private final JTable table;

    /** Flag indicating if the backgroundcolor should be adapted according to the status of the represented cell. */
    private final boolean showStatus;

    /** Default action which will be executed when a double click occurs. */
    private ISelectionAction<T> defaultAction;

    /** The popup manager. */
    private LocalPopupManager popupManager = new LocalPopupManager();

    /** The mapping of this table panel */
    private TableMapping<T> mapping;

    /**
     * Default constructor
     * 
     * @param readOnly True if the table binding is just read only
     * @param showStatus True if the cellbackground should be adapted according to the cell state
     */
    public TablePanel(boolean readOnly, boolean showStatus) {
        this(new JTable(), readOnly, showStatus);
    }

    /**
     * Constructor with specified table
     * 
     * @param table The table to use
     * @param readOnly True if the table binding is just read only
     * @param showStatus True if the cellbackground should be adapted according to the cell state
     */
    public TablePanel(final JTable table, boolean readOnly, boolean showStatus) {
        this.table = table;
        this.showStatus = showStatus;
        mapping = new TableMapping<T>(table, readOnly);
        if (showStatus) table.setDefaultRenderer(Object.class, new DefaultPropertyCellRenderer(mapping, true));
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (defaultAction == null || e.getClickCount() < 2) return;
                if (TablePanel.this.table.getSelectedRowCount() != 1) return;
                final T row = mapping.getSelectedRow();
                if (defaultAction.selectionChanged(Collections.singletonList(row))) defaultAction.actionPerformed(new ActionEvent(TablePanel.this, 0, "Default Action"));
            }
        });
        final JScrollPane pane = new JScrollPane(table);
        final MouseListener popupListener = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (!e.isPopupTrigger()) return;
                final JPopupMenu popup = popupManager.getMenu(mapping.getSelectedRows());
                if (popup.getComponentCount() > 0) popup.show(e.getComponent(), e.getX(), e.getY());
            }
        };
        pane.addMouseListener(popupListener);
        table.addMouseListener(popupListener);
        setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);
    }

    /**
     * Returns the table of this panel.
     * 
     * @return The table.
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Returns the {@link TableMapping} of this panel.
     * 
     * @return The mapping
     */
    public TableMapping<T> getMapping() {
        return mapping;
    }

    /**
     * Adds a column binding
     * 
     * @param label The column label
     * @param path The path to the property shown in this column
     * @param type The type of this column
     * @return The {@link ColumnBindingInfos} object for further configuration of the binding
     * @throws IllegalStateException if the table is already bound
     * @see ch.trackedbean.binding.mapping.TableMapping#addColumn(java.lang.String, java.lang.String, java.lang.Class,boolean)
     */
    public ColumnBindingInfos addColumn(final String label, final String path, final Class<?> type) {
        return mapping.addColumn(label, path, type, true);
    }

    /**
     * Adds a column binding
     * 
     * @param label The column label
     * @param path The path to the property shown in this column
     * @return The {@link ColumnBindingInfos} object for further configuration of the binding
     * @throws IllegalStateException if the table is already bound
     * @see ch.trackedbean.binding.mapping.TableMapping#addColumn(java.lang.String, java.lang.String,boolean)
     */
    public ColumnBindingInfos addColumn(final String label, final String path) {
        return mapping.addColumn(label, path, true);
    }

    /**
     * Adds a column binding.<br>
     * The passed el string is used to extract the display values out of the provided row objects.<br>
     * This call sets simply an {@link SimpleELTableCellRenderer} on the represented column.
     * 
     * @param label The column label
     * @param property The property shown in this column
     * @param el An string containing el expression escaped in <code>${el}</code> blocks
     * @return The {@link ColumnBindingInfos} object for further configuration of the binding
     * @throws IllegalStateException if the table is already bound
     * @see ch.trackedbean.binding.mapping.TableMapping#addColumn(java.lang.String, java.lang.String, boolean)
     */
    public ColumnBindingInfos addColumn(final String label, final String property, String el) {
        final ColumnBindingInfos bind = mapping.addColumn(label, property, false);
        bind.setRenderer(new SimpleELTableCellRenderer(el, "", getMapping(), showStatus));
        return bind;
    }

    /**
     * Adds a column binding with a {@link JComboBox} using a {@link SimpleELListRenderer} with the given el expression.<br>
     * The combobox content list will be loaded using {@link DataManager#getList(Class, String, Map)}.
     * 
     * @param label The column label
     * @param path The path to the property shown in this column
     * @param el The simple el expression to use by the combobox renderer - see {@link ELExpression}
     * @param type The content type of the combobox list
     * @param subType Additional discriminator specifying the "subtype" of the combobox content list (may be null)
     * @param settings Additional settings for the combobox content load (may be null)
     * @return The {@link BindingInfos} object for further configuration of the binding
     * @throws IllegalStateException if the table is already bound
     */
    public ColumnBindingInfos addComboBoxColumn(final String label, final String path, String el, Class<?> type, Object subType, Map settings) {
        return mapping.addComboBoxColumn(label, path, el, type, subType, settings, showStatus);
    }

    /**
     * @return Returns the popupManager.
     */
    public LocalPopupManager getPopupManager() {
        return popupManager;
    }

    /**
     * Sets the table model
     * 
     * @param list The list used as tablemodel
     * @see ch.trackedbean.binding.mapping.TableMapping#setModel(java.util.List)
     */
    public void setModel(final List<T> list) {
        mapping.setModel(list);
    }

    /**
     * @return The current tablemodel (may be null)
     * @see ch.trackedbean.binding.mapping.TableMapping#getModel()
     */
    public List<T> getModel() {
        return mapping.getModel();
    }

    /**
     * Returns the default action of the table (so the action which will be executed when a double click occours)
     * 
     * @return the defaultAction or null
     */
    public ISelectionAction getDefaultAction() {
        return defaultAction;
    }

    /**
     * Sets the default action of the table (so the action which will be executed when a double click occours)
     * 
     * @param defaultAction the defaultAction to set or null
     */
    public void setDefaultAction(ISelectionAction<T> defaultAction) {
        this.defaultAction = defaultAction;
    }
}
