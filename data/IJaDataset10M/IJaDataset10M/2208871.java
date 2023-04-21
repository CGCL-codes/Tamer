package net.sf.component.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.component.config.ConfigHelper;
import net.sf.component.table.orderable.OrderButtonsComposite;
import net.sf.util.StringUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * 调用方法如下：
 * <pre>
 		final BindedTableViewer bindedTableViewer = new BindedTableViewer(shell, SWT.NONE);
		bindedTableViewer.setModel( model);
		bindedTableViewer.setRowCreateRemovable( false);
		bindedTableViewer.setRowsOrderable( false);
		bindedTableViewer.setColsOrderable( true);
		bindedTableViewer.create();
 * </pre>
 * 其中：
 *  
 * <li>Model 提供本Viewr 所有的信息，
 * <li>RowCreateRemovable 决定是否有增加和删除按钮，缺省为true。
 * <li>RowsOrderable 指是否有行移动的按钮，缺省为 true。 
 * <li>ColsOrderable 指是否有列排序和是否显示的按钮，缺省为 false。
 * <br>
 * 最后一定要调用 create()方法来创建对应的component <br>
 * $Id: BindedTableViewer.java 844 2005-09-12 03:18:14Z yflei $
 * Created Date: 2005-7-15
 * @author SimonLei
 */
public class BindedTableViewer extends Composite implements IBindedTableViewer {

    private final class ContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object inputElement) {
            return model.getRows().toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    private Table table;

    private IBindedTableModel model;

    private boolean rowCreateRemovable = true;

    private boolean rowsOrderable = true;

    private boolean colsOrderable = false;

    private TableViewer tableViewer;

    private OrderButtonsComposite orderButtonsComposite;

    private TableCursor tableCursor;

    public BindedTableViewer(Composite parent, int style) {
        super(parent, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        setLayout(gridLayout);
        tableViewer = new TableViewer(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        final GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 6;
        table.setLayoutData(gridData);
    }

    public void create() {
        if (rowCreateRemovable) initCreateRemoveButtons();
        if (rowsOrderable) {
            orderButtonsComposite = new OrderButtonsComposite(this, SWT.NONE, this, model);
            tableViewer.addSelectionChangedListener(orderButtonsComposite);
        }
        if (colsOrderable) initColsOrderButton();
        List<String> propertyNames = new ArrayList<String>();
        List<CellEditor> cellEditors = new ArrayList<CellEditor>();
        Listener sortedListener = model.getSortedListener(this);
        Map<String, String> headerSize = ConfigHelper.getMapProperty(model.createRow().getClass().getSimpleName().toLowerCase() + ".headersize");
        for (int i = 0; i < model.getColumnCount(); i++) {
            BindedTableColumn column = model.getColumn(i);
            if (!column.isVisible()) continue;
            TableColumn tableColumn = new TableColumn(table, column.getStyle());
            tableColumn.setText(column.getShowName());
            tableColumn.setWidth(headerSize.get(column.getName()) == null ? column.getWidth() : StringUtil.parseInt(headerSize.get(column.getName())));
            tableColumn.setData(column.getName());
            tableColumn.addListener(SWT.Selection, sortedListener);
            propertyNames.add(column.getName());
            CellEditor editor = column.getEditor();
            if (editor.getControl() == null) {
                editor.create(table);
                if (editor instanceof ComboBoxCellEditor) {
                    ComboBoxCellEditor comboEditor = (ComboBoxCellEditor) editor;
                    comboEditor.setItems(comboEditor.getItems());
                }
            }
            cellEditors.add(editor);
        }
        if (propertyNames.size() == 0) return;
        tableViewer.setContentProvider(new ContentProvider());
        tableViewer.setLabelProvider(model.getLabelProvider(propertyNames));
        tableViewer.setColumnProperties((String[]) propertyNames.toArray(new String[propertyNames.size()]));
        tableViewer.setCellModifier(model.getCellModifier(this));
        tableViewer.setCellEditors((CellEditor[]) cellEditors.toArray(new CellEditor[cellEditors.size()]));
        TableViewerEditor.create(tableViewer, new ColumnViewerEditorActivationStrategy(tableViewer), ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);
        tableViewer.setInput(model.getRows());
        if (model.isEditable()) {
            tableCursor = TableUtils.installTableCursor(tableViewer);
            TableUtils.installTabNavigation(tableViewer, tableCursor);
        }
    }

    private void initColsOrderButton() {
        final Button colsOrderButton = new Button(this, SWT.NONE);
        colsOrderButton.setText("列属性设置...");
        colsOrderButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ColumnsOrderDialog dlg = new ColumnsOrderDialog(getShell());
                dlg.setModel(model);
                int retValue = dlg.open();
                if (retValue == ColumnsOrderDialog.OK) {
                    model.setColumns(dlg.getColumns());
                    columnChanges();
                }
            }
        });
    }

    public void setRowCreateRemovable(boolean enable) {
        this.rowCreateRemovable = enable;
    }

    public void setRowsOrderable(boolean enable) {
        this.rowsOrderable = enable;
    }

    private void initCreateRemoveButtons() {
        final Button rowCreateButton = new Button(this, SWT.NONE);
        rowCreateButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                model.addRow(model.createRow());
                refresh(false);
            }
        });
        rowCreateButton.setText("增加");
        final Button rowRemoveButton = new Button(this, SWT.NONE);
        rowRemoveButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                int index = table.getSelectionIndex();
                if (index < 0 || index > table.getItemCount() - 1) return;
                if (MessageDialog.openConfirm(getShell(), "是否真的删除？", "确定删除选中记录？")) {
                    model.removeRow(index);
                    refresh(false);
                }
            }
        });
        rowRemoveButton.setEnabled(false);
        rowRemoveButton.setText("删除");
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent e) {
                if (table.getSelectionCount() == 0) {
                    rowCreateButton.setEnabled(true);
                    rowRemoveButton.setEnabled(false);
                } else rowRemoveButton.setEnabled(true);
            }
        });
    }

    public void dispose() {
        super.dispose();
    }

    protected void checkSubclass() {
    }

    public int getSelectionIndex() {
        return table.getSelectionIndex();
    }

    public Object getSelectedItem() {
        if (table.getSelectionIndex() == -1) return null;
        return model.getRows().get(table.getSelectionIndex());
    }

    public int getItemCount() {
        return table.getItemCount();
    }

    public void refresh(boolean updateLabels) {
        tableViewer.refresh(updateLabels);
        if (orderButtonsComposite != null) orderButtonsComposite.selectionChanged(null);
    }

    public IBindedTableModel getModel() {
        return model;
    }

    public void setModel(IBindedTableModel model) {
        this.model = model;
    }

    public boolean isColsOrderable() {
        return colsOrderable;
    }

    public void setColsOrderable(boolean colsOrderable) {
        this.colsOrderable = colsOrderable;
    }

    public boolean isRowCreateRemovable() {
        return rowCreateRemovable;
    }

    public boolean isRowsOrderable() {
        return rowsOrderable;
    }

    /**
	 * 当有列发生变化，或者是列当中的ComboBox所拥有的Item发生变化，就调用这个方法。
	 */
    public void columnChanges() {
        table.clearAll();
        int counts = table.getColumnCount();
        for (int i = counts - 1; i >= 0; i--) {
            TableColumn column = table.getColumn(i);
            column.dispose();
        }
        create();
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

    public void search(String property, String name) {
        int searchedIndex = model.searchPropertyIndex(property, name, table.getSelectionIndex() + 1);
        if (searchedIndex != -1) table.setSelection(searchedIndex);
    }

    public void dataReordered() {
        refresh(false);
    }

    public TableCursor getTableCursor() {
        return tableCursor;
    }

    public Table getTable() {
        return table;
    }
}
