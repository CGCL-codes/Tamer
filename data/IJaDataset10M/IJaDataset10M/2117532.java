package seventhsense.gui.basicscenario;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import seventhsense.data.file.FileReference;
import seventhsense.data.listenerlist.IListenerList;
import seventhsense.data.scenario.sound.AbstractSoundItem;
import seventhsense.gui.table.DisabledCheckboxCellRenderer;
import seventhsense.gui.table.FileReferenceCellRenderer;
import seventhsense.gui.table.TristateTableRowSorter;

/**
 * Abstract class for basic scenario table showing subitems
 * @author Parallan, Drag-On
 *
 * @param <E> type of item
 */
public abstract class AbstractBasicScenarioTablePanel<E extends AbstractSoundItem<E>> extends JPanel {

    /**
	 * Default serial version
	 */
    private static final long serialVersionUID = 1L;

    protected final JTable _table;

    protected final TristateTableRowSorter<TableModel> _rowSorter;

    private final JButton _buttonAdd;

    private final JButton _buttonDelete;

    private final JButton _buttonUp;

    private final JButton _buttonDown;

    protected final AbstractBasicScenarioTableModel<E> _tableModel;

    protected final AbstractBasicScenarioTransferHandler<E> _transferHandler;

    /**
	 * Creates the basic scenario table panel
	 * 
	 * @param tableModel model for the table
	 * @param transferHandler transfer handler for the table
	 */
    public AbstractBasicScenarioTablePanel(final AbstractBasicScenarioTableModel<E> tableModel, final AbstractBasicScenarioTransferHandler<E> transferHandler) {
        super();
        _tableModel = tableModel;
        _transferHandler = transferHandler;
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);
        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        final GridBagConstraints gbc_toolBar = new GridBagConstraints();
        gbc_toolBar.fill = GridBagConstraints.BOTH;
        gbc_toolBar.insets = new Insets(0, 0, 5, 0);
        gbc_toolBar.gridx = 0;
        gbc_toolBar.gridy = 0;
        add(toolBar, gbc_toolBar);
        _buttonAdd = new JButton("");
        _buttonAdd.setEnabled(false);
        _buttonAdd.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                AbstractBasicScenarioTablePanel.this.onAddAction();
            }
        });
        _buttonAdd.setToolTipText("Add new item");
        toolBar.add(_buttonAdd);
        _buttonAdd.setIcon(new ImageIcon(AbstractBasicScenarioTablePanel.class.getResource("/seventhsense/resources/Add_20.png")));
        _buttonDelete = new JButton("");
        _buttonDelete.setEnabled(false);
        _buttonDelete.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                AbstractBasicScenarioTablePanel.this.onDeleteAction();
            }
        });
        _buttonDelete.setToolTipText("Delete selected item");
        _buttonDelete.setIcon(new ImageIcon(AbstractBasicScenarioTablePanel.class.getResource("/seventhsense/resources/Delete_20.png")));
        toolBar.add(_buttonDelete);
        _buttonUp = new JButton("");
        _buttonUp.setEnabled(false);
        _buttonUp.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                AbstractBasicScenarioTablePanel.this.onUpAction();
            }
        });
        _buttonUp.setIcon(new ImageIcon(AbstractBasicScenarioTablePanel.class.getResource("/seventhsense/resources/Up_20.png")));
        _buttonUp.setToolTipText("Move selected item up");
        toolBar.add(_buttonUp);
        _buttonDown = new JButton("");
        _buttonDown.setEnabled(false);
        _buttonDown.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                AbstractBasicScenarioTablePanel.this.onDownAction();
            }
        });
        _buttonDown.setIcon(new ImageIcon(AbstractBasicScenarioTablePanel.class.getResource("/seventhsense/resources/Down_20.png")));
        _buttonDown.setToolTipText("Move selected item down");
        toolBar.add(_buttonDown);
        final JScrollPane scrollPaneTable = new JScrollPane();
        final GridBagConstraints gbc_scrollPaneTable = new GridBagConstraints();
        gbc_scrollPaneTable.fill = GridBagConstraints.BOTH;
        gbc_scrollPaneTable.gridx = 0;
        gbc_scrollPaneTable.gridy = 1;
        add(scrollPaneTable, gbc_scrollPaneTable);
        _table = new JTable();
        _table.setDefaultRenderer(Boolean.class, new DisabledCheckboxCellRenderer());
        _table.setDefaultRenderer(FileReference.class, new FileReferenceCellRenderer());
        _table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                AbstractBasicScenarioTablePanel.this.tableSelectionChanged();
            }
        });
        _rowSorter = new TristateTableRowSorter<TableModel>();
        _table.setModel(_tableModel);
        _table.setRowSorter(_rowSorter);
        _table.setModel(_tableModel);
        _rowSorter.setModel(_tableModel);
        _table.setDragEnabled(true);
        _table.setTransferHandler(_transferHandler);
        _table.setDropMode(DropMode.INSERT_ROWS);
        scrollPaneTable.setViewportView(_table);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        _buttonAdd.setEnabled(enabled);
        _buttonDelete.setEnabled(enabled && (_table.getSelectedRowCount() != 0));
        _buttonUp.setEnabled(enabled && (_table.getSelectedRowCount() != 0));
        _buttonDown.setEnabled(enabled && (_table.getSelectedRowCount() != 0));
    }

    /**
	 * adds a table listener
	 * @param listener listener to add
	 */
    public void addTableSelectionListener(final ListSelectionListener listener) {
        _table.getSelectionModel().addListSelectionListener(listener);
    }

    /**
	 * removes a table listener
	 * @param listener listener to remove
	 */
    public void removeTableSelectionListener(final ListSelectionListener listener) {
        _table.getSelectionModel().removeListSelectionListener(listener);
    }

    /**
	 * Event.
	 */
    protected void tableSelectionChanged() {
        _buttonDelete.setEnabled(this.isEnabled() && (_table.getSelectedRowCount() != 0));
        _buttonUp.setEnabled(this.isEnabled() && (_table.getSelectedRowCount() != 0));
        _buttonDown.setEnabled(this.isEnabled() && (_table.getSelectedRowCount() != 0));
    }

    /**
	 * Sets the model
	 * @param data model
	 */
    public void setModel(final IListenerList<E> data) {
        _tableModel.setModel(data);
        if (data == null) {
            setEnabled(false);
            _transferHandler.setModel(null);
        } else {
            setEnabled(true);
            _transferHandler.setModel(data);
        }
    }

    /**
	 * Returns the selected item
	 * 
	 * @return selected item or null
	 */
    public E getSelectedItem() {
        final int selectedRow = _table.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        } else {
            return _tableModel.getModel().get(_table.convertRowIndexToModel(selectedRow));
        }
    }

    /**
	 * Sets the selected item
	 * 
	 * @param item selected item
	 */
    public void setSelectedItem(final E item) {
        if (item == null) {
            _table.getSelectionModel().clearSelection();
        } else {
            final int selectedRow = _tableModel.getModel().indexOf(item);
            _table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
        }
    }

    /**
	 * Event.
	 */
    protected abstract void onAddAction();

    /**
	 * Event.
	 */
    protected abstract void onDeleteAction();

    /**
	 * Event.
	 */
    private void onUpAction() {
        final E selectedItem = getSelectedItem();
        if (selectedItem != null) {
            final int oldIndex = _tableModel.getModel().indexOf(selectedItem);
            if (oldIndex > 0) {
                _tableModel.getModel().remove(selectedItem);
                _tableModel.getModel().add(oldIndex - 1, selectedItem);
                setSelectedItem(selectedItem);
            }
        }
    }

    /**
	 * Event.
	 */
    private void onDownAction() {
        final E selectedItem = getSelectedItem();
        if (selectedItem != null) {
            final int oldIndex = _tableModel.getModel().indexOf(selectedItem);
            if (oldIndex < _tableModel.getModel().size() - 1) {
                _tableModel.getModel().remove(selectedItem);
                _tableModel.getModel().add(oldIndex + 1, selectedItem);
                setSelectedItem(selectedItem);
            }
        }
    }
}
