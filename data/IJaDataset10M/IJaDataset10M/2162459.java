package org.vikamine.gui.subgroup.editors.tuningtable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import net.sf.xframe.swing.JXTable;
import net.sf.xframe.swing.table.ColumnGroup;
import net.sf.xframe.swing.table.ColumnGroupHeader;
import net.sf.xframe.swing.table.JXTableColumnModel;
import net.sf.xframe.swing.table.JXTableHeader;
import net.sf.xframe.swing.table.KTable;
import org.vikamine.app.DMManager;
import org.vikamine.app.VIKAMINE;
import org.vikamine.gui.subgroup.view.workspace.SGPopupContextMenuFactory;
import org.vikamine.gui.subgroup.visualization.VisualizationUtils;
import org.vikamine.gui.util.AttributeDNDContainer;
import org.vikamine.gui.util.DefaultDragSourceListener;
import org.vikamine.gui.util.DefaultTransferable;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.data.NominalAttribute;
import org.vikamine.kernel.data.Value;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.target.SGTarget;

public class TuningTable extends JXTable {

    private static final long serialVersionUID = -4301700197722048014L;

    public static final int NUMBER_COLUMN_WIDTH = 18;

    public static final int VALUES_Abbreviation_COLUMN_WIDTH = 24;

    public static final int VALUES_COLUMN_WIDTH = 26;

    public static final int COMPUTATION_COLUMN_WIDTH = 38;

    private final TuningTableModel tableModel;

    private TuningTableController controller;

    private final transient SGPopupContextMenuFactory popupFactory;

    private final int width;

    class TableHeadContextMenuListener extends MouseAdapter {

        TuningTable tuningTable;

        TableHeadContextMenuListener(TuningTable table) {
            this.tuningTable = table;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) handleEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) handleEvent(e);
        }

        private void handleEvent(MouseEvent e) {
            ColumnGroupHeader header = (ColumnGroupHeader) e.getComponent();
            int columnIndex = header.columnAtPoint(e.getPoint());
            int modelColumnIndex = header.getTable().convertColumnIndexToModel(columnIndex);
            if (isColumnRemovable(modelColumnIndex)) {
                TableColumn tc = tuningTable.getColumnModel().getColumn(modelColumnIndex);
                ColumnGroup cg = null;
                Enumeration enu = header.getColumnGroups(tc);
                while (enu.hasMoreElements()) {
                    cg = (ColumnGroup) enu.nextElement();
                }
                if (cg != null) {
                    JPopupMenu menu = createTableHeadContextMenu(cg.getHeaderValue() + "", modelColumnIndex);
                    if (menu == null) {
                        return;
                    }
                    menu.show((Component) e.getSource(), e.getX(), e.getY());
                }
            }
        }
    }

    class DropTargetListener extends DropTargetAdapter {

        public void drop(DropTargetDropEvent event) {
            try {
                if (event.isDataFlavorSupported(DefaultTransferable.SG_FLAVOR)) {
                    HashMap<SGTarget, List<SG>> sgMap = new HashMap<SGTarget, List<SG>>();
                    Transferable transferable = event.getTransferable();
                    List sgList = (List) transferable.getTransferData(DefaultTransferable.SG_FLAVOR);
                    if (sgList != null && sgList.size() > 0) {
                        for (Iterator iter = sgList.iterator(); iter.hasNext(); ) {
                            SG sg = (SG) iter.next();
                            SGTarget target = sg.getTarget();
                            List<SG> sgsWithtarget = sgMap.get(target);
                            if (sgsWithtarget == null) {
                                sgsWithtarget = new ArrayList<SG>();
                            }
                            sgsWithtarget.add(sg);
                            sgMap.put(target, sgsWithtarget);
                        }
                        controller.addSGsToTables(sgMap);
                        event.dropComplete(true);
                    } else event.dropComplete(false);
                } else if (event.isDataFlavorSupported(DefaultTransferable.ATTRIBUTE_DND_FLAVOR)) {
                    Transferable transferable = event.getTransferable();
                    Object obj = transferable.getTransferData(DefaultTransferable.ATTRIBUTE_DND_FLAVOR);
                    if (obj instanceof AttributeDNDContainer) {
                        Attribute dmAttribute = ((AttributeDNDContainer) obj).getAttribute();
                        if (addDMAttribute(dmAttribute)) event.dropComplete(true); else event.dropComplete(false);
                    } else if (obj instanceof List) {
                        List dmAttributesList = (List) obj;
                        if (dmAttributesList.size() == 1) {
                            Object transferred = dmAttributesList.iterator().next();
                            Attribute dmAttribute;
                            if (transferred instanceof Attribute) {
                                dmAttribute = (Attribute) transferred;
                            } else if (transferred instanceof AttributeDNDContainer) {
                                dmAttribute = ((AttributeDNDContainer) transferred).getAttribute();
                            } else {
                                Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "drop", new UnsupportedOperationException("Attribute type " + transferred + " not supported in drop."));
                                return;
                            }
                            if (addDMAttribute(dmAttribute)) event.dropComplete(true); else event.dropComplete(false);
                        }
                    } else event.dropComplete(false);
                } else if (event.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    Object id = event.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    if (id != null) {
                        Attribute attr = DMManager.getInstance().getOntology().getAttribute((String) id);
                        if (attr != null) {
                            if (addDMAttribute(attr)) event.dropComplete(true); else event.dropComplete(false);
                        } else {
                            event.dropComplete(false);
                        }
                    } else {
                        event.dropComplete(false);
                    }
                } else {
                    event.dropComplete(false);
                }
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "drop", e);
            }
        }

        public boolean dmAttributeAlreadyExists(Attribute dmAttribute) {
            AssociatedTableHeads assTableHeads = tableModel.getAssociatedTableHeads();
            for (Iterator iter = assTableHeads.iterator(); iter.hasNext(); ) {
                AssociatedTableHead assTableHead = (AssociatedTableHead) iter.next();
                if (dmAttribute != null && dmAttribute.equals(assTableHead.getSelector().getAttribute())) return true;
            }
            return false;
        }

        public boolean addDMAttribute(Attribute dmAttribute) {
            if (!dmAttributeAlreadyExists(dmAttribute) && dmAttribute.isNominal()) {
                try {
                    tableModel.addDMAttribute((NominalAttribute) dmAttribute);
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "add DMAttribute", e);
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
    }

    class DragTargetListener implements DragGestureListener {

        public void dragGestureRecognized(DragGestureEvent event) {
            List<SG> interactionSGs = tableModel.getInteractionSGs();
            int selectedRow = TuningTable.this.getSelectedRow();
            if (interactionSGs == null || interactionSGs.size() == 0 || selectedRow < 0) {
                return;
            }
            DefaultTransferable transferable = new DefaultTransferable();
            List sgList = new ArrayList(1);
            SG sg = interactionSGs.get(selectedRow);
            if (sg != null) sgList.add(sg.clone());
            if (!sgList.isEmpty()) transferable.put(DefaultTransferable.SG_FLAVOR, sgList);
            event.startDrag(DragSource.DefaultMoveDrop, transferable, new DefaultDragSourceListener());
        }
    }

    public class TableMouseListener extends MouseAdapter {

        private void handleEvent(MouseEvent e) {
            if (!e.isPopupTrigger()) {
                return;
            }
            int row = rowAtPoint(e.getPoint());
            int column = columnAtPoint(e.getPoint());
            if (row == -1 || column == -1) {
                return;
            }
            if (!isRowSelected(row)) {
                if (e.isControlDown()) {
                    getSelectionModel().addSelectionInterval(row, row);
                } else {
                    getSelectionModel().setSelectionInterval(row, row);
                }
            }
            JPopupMenu menu = createContextMenu(tableModel.getSelectedSGs(getSelectedRows()));
            if (menu == null) {
                return;
            }
            menu.show((Component) e.getSource(), e.getX(), e.getY());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && !e.isShiftDown() && !e.isControlDown()) {
                tableModel.modifyInteractionSG(getSelectedRow(), getSelectedColumn());
                controller.setActiveTable(TuningTable.this);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            handleEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            handleEvent(e);
        }
    }

    private class RemoveAction extends AbstractAction {

        /**
	 * 
	 */
        private static final long serialVersionUID = -9117004270759482018L;

        public RemoveAction() {
            super(VIKAMINE.I18N.getString("vikamine.subgroupTuningTable.remove"));
        }

        public void actionPerformed(ActionEvent e) {
            tableModel.removeRows(getSelectedRows());
        }
    }

    public TuningTable(final TuningTableModel tableModel) {
        super(tableModel);
        TuningTableColumnGroupHeader interactionLockedTableColumnHeader = new TuningTableColumnGroupHeader(this, getLockedTable().getColumnModel(), TuningTableColumnGroupHeader.LOCKED_TYPE);
        TuningTableColumnGroupHeader interactionScrollTableColumnHeader = new TuningTableColumnGroupHeader(this, getScrollTable().getColumnModel(), TuningTableColumnGroupHeader.SCROLL_TYPE);
        JXTableHeader headerTest = new JXTableHeader(getColumnModel(), interactionLockedTableColumnHeader, interactionScrollTableColumnHeader);
        headerTest.setTable(this);
        setTableHeader(headerTest);
        this.tableModel = tableModel;
        popupFactory = new SGPopupContextMenuFactory() {

            @Override
            public JPopupMenu createSGsContextMenu(List values) {
                JPopupMenu menu = new JPopupMenu();
                menu.add(new SelectAsCurrentSGAction(values));
                menu.addSeparator();
                menu.add(new RemoveAction());
                menu.addSeparator();
                addCommonSGsContextMenuItems(values, menu, true);
                menu.addSeparator();
                VisualizationUtils.addVisualizationActions(menu, values);
                return menu;
            }
        };
        getLockedTable().setDefaultRenderer(Object.class, new TuningTableCellRenderer(TuningTableCellRenderer.LOCKED_TYPE));
        getScrollTable().setDefaultRenderer(Object.class, new TuningTableCellRenderer(TuningTableCellRenderer.SCROLL_TYPE));
        TableModelListener[] listener = tableModel.getListeners(TableModelListener.class);
        for (int i = 0; i < listener.length; i++) {
            if (listener[i] instanceof KTable) tableModel.removeTableModelListener(listener[i]);
        }
        tableModel.addTableModelListener(this);
        JXTableColumnModel columnModel = (JXTableColumnModel) getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(NUMBER_COLUMN_WIDTH);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setResizingAllowed(true);
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        setFrozenColumns(1);
        addMouseListener(new TableMouseListener());
        SortableTableHeaderRenderer headerRenderer = new SortableTableHeaderRenderer(tableModel);
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setHeaderRenderer(headerRenderer);
        }
        JXTableHeader header = getTableHeader();
        SortableTableHeaderListener headerListener = new SortableTableHeaderListener(tableModel, headerRenderer);
        header.addMouseListener(headerListener);
        header.addMouseMotionListener(headerListener);
        header.addMouseListener(new TableHeadContextMenuListener(this));
        new DropTarget(this, new DropTargetListener());
        new DragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, new DragTargetListener());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        width = getPreferredScrollableViewportSize().width;
        updateTableHeight();
    }

    protected JPopupMenu createContextMenu(List values) {
        for (Iterator iter = values.iterator(); iter.hasNext(); ) {
            if (!(iter.next() instanceof SG)) {
                iter.remove();
            }
        }
        if (values.isEmpty()) {
            return null;
        }
        return popupFactory.createSGsContextMenu(values);
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        int firstRow = e.getFirstRow();
        int type = e.getType();
        boolean delete = type == TableModelEvent.DELETE;
        boolean update = type == TableModelEvent.UPDATE;
        TuningTableModelEvent ev = null;
        boolean rowSelection = false;
        boolean columnDeleted = false;
        boolean columnInserted = false;
        boolean columnUpdated = false;
        if (e instanceof TuningTableModelEvent) {
            ev = (TuningTableModelEvent) e;
            int extendedType = ev.getExtendedType();
            rowSelection = extendedType == TuningTableModelEvent.EXTENDED_TYPE_ROW_SELECTION;
            columnDeleted = extendedType == TuningTableModelEvent.EXTENDED_TYPE_COLUMN_DELETED;
            columnInserted = extendedType == TuningTableModelEvent.EXTENDED_TYPE_COLUMN_INSERTED;
            columnUpdated = extendedType == TuningTableModelEvent.EXTENDED_TYPE_COLUMN_UPDATED;
        } else {
            return;
        }
        if (update && (columnDeleted || columnInserted || columnUpdated)) {
            removeAllScrollColumns();
            createAllScrollColumns();
        }
        int selectedRow = getSelectedRow();
        int selectedColumn = getSelectedColumn();
        getScrollTable().tableChanged(e);
        TableModelEvent event = new TableModelEvent((TableModel) e.getSource(), e.getFirstRow(), e.getLastRow(), e.getColumn() - getFrozenColumns(), e.getType());
        getLockedTable().tableChanged(event);
        SortableTableHeaderRenderer headerRenderer = new SortableTableHeaderRenderer(tableModel);
        for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
            getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        if (rowSelection) {
            List<Integer> newSortedIndices = ev.getNewSortedIndices();
            if (selectedRow != -1) {
                int newIndex = newSortedIndices.get(selectedRow);
                setRowSelectionInterval(newIndex, newIndex);
            }
        } else {
            if (tableModel.getRowCount() > 0) if (selectedRow != -1 && !(delete && firstRow == selectedRow) && selectedRow < getRowCount()) setRowSelectionInterval(selectedRow, selectedRow);
            if (selectedColumn != -1) setColumnSelectionInterval(selectedColumn, selectedColumn);
        }
        updateTableHeight();
    }

    private void createAllScrollColumns() {
        JXTableColumnModel columnModel = (JXTableColumnModel) getColumnModel();
        TuningTableModel tm = (TuningTableModel) getModel();
        String[] computationHeads = new String[tm.getStatComponents().size()];
        for (int i = 0; i < computationHeads.length; i++) {
            computationHeads[i] = tm.getStatComponents().get(i).getDescription();
        }
        AssociatedTableHeads assTableHeads = tm.getAssociatedTableHeads();
        if (tableModel.isShowAbbreviationHeads()) {
            for (Iterator iter = assTableHeads.iterator(); iter.hasNext(); ) {
                AssociatedTableHead abbTableHead = (AssociatedTableHead) iter.next();
                addGroupedColumn(abbTableHead.getSelectorAbbreviation(), abbTableHead.getValuesAbbreviation(), VALUES_Abbreviation_COLUMN_WIDTH);
            }
        } else {
            for (Iterator iter = assTableHeads.iterator(); iter.hasNext(); ) {
                AssociatedTableHead assTableHead = (AssociatedTableHead) iter.next();
                LinkedList<String> valueStrings = new LinkedList<String>();
                for (Iterator iterator = assTableHead.getValues().iterator(); iterator.hasNext(); ) {
                    Value value = (Value) iterator.next();
                    valueStrings.add(value.getDescription());
                }
                addGroupedColumn(assTableHead.getSelector().getAttribute().getDescription(), valueStrings, VALUES_COLUMN_WIDTH);
            }
        }
        for (int i = 0; i < computationHeads.length; i++) {
            columnModel.addColumn(createColumn(columnModel.getColumnCount(), COMPUTATION_COLUMN_WIDTH, computationHeads[i]));
        }
    }

    private void addGroupedColumn(String mainHead, List<String> subHeads, int columnWidth) {
        JXTableHeader header = getTableHeader();
        TableColumnModel columnModel = getColumnModel();
        int oldColumnCount = columnModel.getColumnCount();
        ColumnGroup groupedColumn = new ColumnGroup(mainHead);
        groupedColumn.setHeaderRenderer(new SortableTableHeaderRenderer(tableModel));
        for (int i = 0; i < subHeads.size(); i++) {
            String subHead = subHeads.get(i);
            columnModel.addColumn(createColumn(oldColumnCount + i, columnWidth, subHead));
            groupedColumn.add(columnModel.getColumn(oldColumnCount + i));
        }
        header.addColumnGroup(groupedColumn);
    }

    private TableColumn createColumn(final int modelIndex, final int width, final String header) {
        final TableColumn column = new TableColumn(modelIndex, width);
        column.setHeaderValue(header);
        return column;
    }

    private void removeAllScrollColumns() {
        JXTableColumnModel columnModel = (JXTableColumnModel) getColumnModel();
        List allColumns = Util.enumerationToList(columnModel.getColumns());
        for (Iterator iter = allColumns.iterator(); iter.hasNext(); ) {
            TableColumn currentColumn = (TableColumn) iter.next();
            if (currentColumn.getModelIndex() != 0) columnModel.removeColumn(currentColumn);
        }
    }

    public void addRow() {
        tableModel.addRow();
        updateTableHeight();
    }

    public void copyAndAddRow(int row) {
        tableModel.copyAndAddRow(row);
        updateTableHeight();
    }

    public boolean isAbbreviationHeads() {
        return tableModel.isShowAbbreviationHeads();
    }

    public void setAbbreviationHeads(boolean isAbbreviationHeads) {
        tableModel.setShowAbbreviationHeads(isAbbreviationHeads);
    }

    public void removeRow(int row) {
        tableModel.removeRow(row);
        updateTableHeight();
    }

    public boolean isAutoSort() {
        return tableModel.isAutoSort();
    }

    public void setAutoSort(boolean isAutoSort) {
        tableModel.setAutoSort(isAutoSort);
    }

    public boolean isColumnRemovable(int col) {
        return tableModel.isColumnRemovable(col);
    }

    private JPopupMenu createTableHeadContextMenu(final String columnName, final int columnIndex) {
        JPopupMenu menu = new JPopupMenu();
        menu.add(new AbstractAction(VIKAMINE.I18N.getString("vikamine.subgroupTuningTable.removeColumn_") + " " + columnName) {

            private static final long serialVersionUID = -8867956191343994811L;

            public void actionPerformed(ActionEvent e) {
                tableModel.removeDMAttributeAtSelectedColumn(columnIndex);
            }
        });
        return menu;
    }

    public void addSingleFactors() {
        tableModel.addSingleFactors();
        updateTableHeight();
    }

    public void clearTable() {
        tableModel.clearTable();
        updateTableHeight();
    }

    public void setController(TuningTableController controller) {
        this.controller = controller;
    }

    public void updateHeaders() {
        tableModel.updateHeaders();
    }

    private void updateTableHeight() {
        setPreferredScrollableViewportSize(new Dimension(width, tableModel.getRowCount() * getRowHeight()));
    }
}
