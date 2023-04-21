package org.qsari.effectopedia.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.qsari.effectopedia.base.Describable;
import org.qsari.effectopedia.base.EffectopediaObject;
import org.qsari.effectopedia.core.objects.Pathway;
import org.qsari.effectopedia.core.objects.PathwayElement;
import org.qsari.effectopedia.data.DataSource;
import org.qsari.effectopedia.data.DataSourceMerge;
import org.qsari.effectopedia.defaults.DefaultGUISettings;
import org.qsari.effectopedia.gui.util.MultiIndexSortedList;
import org.qsari.effectopedia.gui.util.ScrollPaneSynchronizer;
import org.qsari.effectopedia.history.SourceTrace;
import org.qsari.effectopedia.system.TraceableClasses;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DataSourceEntriesTableUI extends javax.swing.JPanel implements ListSelectionListener, RowSorterListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4817235054911510497L;

    private JScrollPane jspTable;

    private JTable jtEntries;

    /**
		 * Auto-generated main method to display this JPanel inside a new JFrame.
		 */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new DataSourceEntriesTableUI());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public DataSourceEntriesTableUI() {
        super();
        eventListeners = new EventListenerList();
        initGUI();
    }

    public class LiveEntriesTableModel extends DefaultTableModel {

        private static final long serialVersionUID = 5071184128725873963L;

        public LiveEntriesTableModel() {
            super();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return CAPTIONS[columnIndex];
        }

        @Override
        public int getRowCount() {
            if (data != null) return data.getLivePathwayElements().getMap().size(); else return 0;
        }

        @Override
        public Object getValueAt(int row, int column) {
            SourceTrace st = null;
            if (row < pathwaysCount) st = pathways.get(row); else if (row < pathwaysCount + fragmentsCount) st = fragments.get(row - pathwaysCount);
            if (st != null) {
                if (column == 0) return UIResources.getIconForClass(TraceableClasses.REGISTERED.getClassByID(st.getClassID()));
                if (column == 1) {
                    EffectopediaObject eo = data.getLiveObject(st);
                    if (eo == null) {
                        if (eo instanceof PathwayElement) return ((PathwayElement) eo).getTitle(); else if (eo instanceof Pathway) return ((Pathway) eo).getTitle();
                    }
                    return null;
                }
            }
            return null;
        }

        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return ImageIcon.class; else return String.class;
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public String[] CAPTIONS = { "Icon", "Element" };
    }

    public class ObjectUnionTableModel extends DefaultTableModel {

        /**
		 * 
		 */
        private static final long serialVersionUID = -8003382903833624546L;

        public ObjectUnionTableModel() {
            super();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return CAPTIONS[columnIndex];
        }

        @Override
        public int getRowCount() {
            if (list != null) return list.getList(sortIndex).size(); else return 0;
        }

        @Override
        public Object getValueAt(int row, int column) {
            DataSourceMerge.EffectopediaObjectUnion eou = list.getList(sortIndex).get(row);
            if (eou != null) {
                if (column == 0) return UIResources.getIconForClass(sourceA ? eou.getObjectClassA() : eou.getObjectClassB());
                if (column == 1) {
                    EffectopediaObject eo = sourceA ? eou.getEffectopediaObjectA() : eou.getEffectopediaObjectB();
                    if (eo != null) {
                        if (eo instanceof PathwayElement) {
                            if ((((PathwayElement) eo).getTitle().equals("")) && (eo instanceof Describable)) return ((Describable) eo).getGenericDescription(); else return ((PathwayElement) eo).getTitle();
                        } else if (eo instanceof Pathway) return ((Pathway) eo).getTitle();
                    }
                    return null;
                }
            }
            return null;
        }

        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return ImageIcon.class; else return String.class;
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public String[] CAPTIONS = { "Icon", "Element" };
    }

    @Override
    public void valueChanged(ListSelectionEvent arg0) {
        int row = jtEntries.getSelectedRow();
        if (row == -1) return;
        if (jtEntries.getModel() instanceof ObjectUnionTableModel) {
            DataSourceMerge.EffectopediaObjectUnion eou;
            if (list != null) {
                eou = list.getList(sortIndex).get(row);
                fireEntrySelected(new EntrySelection(this, sourceA ? eou.getEffectopediaObjectA() : eou.getEffectopediaObjectB()));
                if (sychronizedTable != null) sychronizedTable.sychronizeRows(row);
            }
        } else {
            SourceTrace st = null;
            if (row < pathwaysCount) st = pathways.get(row); else if (row < pathwaysCount + fragmentsCount) st = fragments.get(row - pathwaysCount);
            fireEntrySelected(new EntrySelection(this, data.getLiveObject(st)));
        }
    }

    public Object getSelectedObject() {
        int row = jtEntries.getSelectedRow();
        if (row == -1) return null;
        if (jtEntries.getModel() instanceof ObjectUnionTableModel) {
            DataSourceMerge.EffectopediaObjectUnion eou;
            if (list != null) {
                eou = list.getList(sortIndex).get(row);
                return sourceA ? eou.getEffectopediaObjectA() : eou.getEffectopediaObjectB();
            }
        } else {
            SourceTrace st = null;
            if (row < pathwaysCount) st = pathways.get(row); else if (row < pathwaysCount + fragmentsCount) st = fragments.get(row - pathwaysCount);
            return data.getLiveObject(st);
        }
        return null;
    }

    public Object getSelectedRawObject() {
        int row = jtEntries.getSelectedRow();
        if (row == -1) return null;
        if (jtEntries.getModel() instanceof ObjectUnionTableModel) return list.getList(sortIndex).get(row); else {
            SourceTrace st = null;
            if (row < pathwaysCount) st = pathways.get(row); else if (row < pathwaysCount + fragmentsCount) st = fragments.get(row - pathwaysCount);
            return st;
        }
    }

    public int getSelectedRow() {
        return jtEntries.getSelectedRow();
    }

    public void setSelectedRow(int row) {
        int currentRow = jtEntries.getSelectedRow();
        if (currentRow != row) jtEntries.setRowSelectionInterval(row, row);
    }

    private void sychronizeRows(int row) {
        int currentRow = jtEntries.getSelectedRow();
        if (currentRow != row) jtEntries.setRowSelectionInterval(row, row);
    }

    public void selectionChanged(EntrySelection evt) {
    }

    public class EntrySelection extends EventObject {

        private static final long serialVersionUID = 1L;

        public EntrySelection(Object source, EffectopediaObject object) {
            super(source);
            this.object = object;
        }

        public final EffectopediaObject object;
    }

    public interface EntrySelectionListener extends EventListener {

        public void selectionChanged(EntrySelection evt);
    }

    public void addEntrySelectionListener(EntrySelectionListener listener) {
        eventListeners.add(EntrySelectionListener.class, listener);
    }

    public void removeEntrySelectionListener(EntrySelectionListener listener) {
        eventListeners.remove(EntrySelectionListener.class, listener);
    }

    protected void fireEntrySelected(EntrySelection evt) {
        EntrySelectionListener[] listeners = eventListeners.getListeners(EntrySelectionListener.class);
        for (int i = 0; i < listeners.length; i++) listeners[i].selectionChanged(evt);
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            this.setLayout(thisLayout);
            setPreferredSize(new Dimension(400, 300));
            {
                jspTable = new JScrollPane();
                this.add(jspTable, BorderLayout.CENTER);
                jspTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                jspTable.setPreferredSize(new java.awt.Dimension(400, 300));
                {
                    TableModel jtEntriesModel = new ObjectUnionTableModel();
                    sorter = new TableRowSorter<TableModel>(jtEntriesModel);
                    sorter.addRowSorterListener(this);
                    jtEntries = new JTable();
                    jtEntries.getSelectionModel().addListSelectionListener(this);
                    jspTable.setViewportView(jtEntries);
                    jtEntries.setModel(jtEntriesModel);
                    jtEntries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    jtEntries.getColumnModel().getColumn(0).setMaxWidth(24);
                    jtEntries.setRowHeight(18);
                    jtEntries.setBackground(Color.WHITE);
                    jtEntries.setRowSorter(sorter);
                    highlighter = new HighlightedTableCellRenderer();
                    jtEntries.setDefaultRenderer(String.class, highlighter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class HighlightedTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            TableModel model = table.getModel();
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (model instanceof ObjectUnionTableModel) {
                DataSourceMerge.EffectopediaObjectUnion eou = list.getList(sortIndex).get(row);
                if (eou != null) switch(eou.getStatus()) {
                    case DataSourceMerge.EffectopediaObjectUnion.DIFFERENT:
                        c.setBackground(DefaultGUISettings.mergeDifferentColor);
                        break;
                    case DataSourceMerge.EffectopediaObjectUnion.MORE_OF_A:
                        c.setBackground(sourceA ? DefaultGUISettings.mergeEcessColor : DefaultGUISettings.mergeAbsentColor);
                        break;
                    case DataSourceMerge.EffectopediaObjectUnion.MORE_OF_B:
                        c.setBackground(sourceA ? DefaultGUISettings.mergeAbsentColor : DefaultGUISettings.mergeEcessColor);
                        break;
                    default:
                        c.setBackground(isSelected ? DefaultGUISettings.uiSelectedColor : Color.WHITE);
                        break;
                } else c.setBackground(isSelected ? DefaultGUISettings.uiSelectedColor : Color.WHITE);
            }
            return c;
        }
    }

    public TableRowSorter<TableModel> getSorter() {
        return sorter;
    }

    public void sorterChanged(RowSorterEvent arg0) {
        List<? extends SortKey> keys = sorter.getSortKeys();
        sychronizedTable.getSorter().setSortKeys(sorter.getSortKeys());
        if (keys.size() == 1) {
            int clmn = keys.get(0).getColumn();
            sortIndex = 2 + clmn;
            if (clmn == sortCoulmn) sortClicks++; else {
                sortClicks = 0;
                sortCoulmn = clmn;
                sortIndex = 1;
            }
            if (sortClicks == 4) {
                sortClicks = 0;
                sortIndex = 1;
                sorter.setSortKeys(null);
            }
        } else {
            sortIndex = 1;
            sorter.setSortKeys(null);
        }
    }

    public void changeListMask(int listMask) {
        if (merge != null) {
            this.list = merge.getThose(listMask);
            TableModel jtEntriesModel = new ObjectUnionTableModel();
            sorter = new TableRowSorter<TableModel>(jtEntriesModel);
            sorter.addRowSorterListener(this);
            jtEntries.setRowSorter(sorter);
            jtEntries.setModel(jtEntriesModel);
            jtEntries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jtEntries.getColumnModel().getColumn(0).setMaxWidth(24);
            jtEntries.revalidate();
        }
    }

    public void loadDataSourceMerge(DataSourceMerge merge, boolean sourceA, int listMask) {
        this.merge = merge;
        this.sourceA = sourceA;
        this.list = merge.getThose(listMask);
        TableModel jtEntriesModel = new ObjectUnionTableModel();
        sorter = new TableRowSorter<TableModel>(jtEntriesModel);
        sorter.addRowSorterListener(this);
        jtEntries.setRowSorter(sorter);
        jtEntries.setModel(jtEntriesModel);
        jtEntries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtEntries.getColumnModel().getColumn(0).setMaxWidth(24);
        jtEntries.setRowHeight(24);
        jtEntries.revalidate();
    }

    public void loadDataSource(DataSource dataSource) {
        this.data = dataSource;
        if (data != null) {
            this.pathways = data.getLivePathwayElements().getPathways();
            pathwaysCount = pathways.size();
            this.fragments = data.getLivePathwayElements().getFragments();
            fragmentsCount = fragments.size();
            TableModel jtEntriesModel = new LiveEntriesTableModel();
            sorter = new TableRowSorter<TableModel>(jtEntriesModel);
            sorter.addRowSorterListener(this);
            jtEntries.setRowSorter(sorter);
            jtEntries.setModel(jtEntriesModel);
            jtEntries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jtEntries.getColumnModel().getColumn(0).setMaxWidth(24);
            jtEntries.setRowHeight(24);
            jtEntries.revalidate();
        }
    }

    public final void setSychronizedTable(DataSourceEntriesTableUI sychronizedTable) {
        this.sychronizedTable = sychronizedTable;
        ScrollPaneSynchronizer synchronizer = new ScrollPaneSynchronizer(jspTable, sychronizedTable.getTableScrollPane());
        jspTable.getVerticalScrollBar().addAdjustmentListener(synchronizer);
        jspTable.getHorizontalScrollBar().addAdjustmentListener(synchronizer);
        sychronizedTable.jspTable.getVerticalScrollBar().addAdjustmentListener(synchronizer);
        sychronizedTable.jspTable.getHorizontalScrollBar().addAdjustmentListener(synchronizer);
    }

    public JScrollPane getTableScrollPane() {
        return jspTable;
    }

    public final int getViewOptions() {
        return viewOptions;
    }

    public final void setViewOptions(int viewOptions) {
        if (this.viewOptions == viewOptions) return;
        if ((viewOptions & DataSourceUI.VO_HIGHLIGHT) != 0) {
            jtEntries.setDefaultRenderer(String.class, highlighter);
        } else {
            if (defaultRenderer != null) defaultRenderer = new DefaultTableCellRenderer();
            jtEntries.setDefaultRenderer(String.class, defaultRenderer);
        }
        if ((this.viewOptions & DataSourceUI.VO_WHICH) == (viewOptions & DataSourceUI.VO_WHICH)) return;
        switch(viewOptions & DataSourceUI.VO_WHICH) {
            case DataSourceUI.VO_CONFLICTS_ONLY:
                changeListMask(DataSourceMerge.EffectopediaObjectUnion.DIFFERENT);
                break;
            case DataSourceUI.VO_ALL_DIFERENCES:
                changeListMask(DataSourceMerge.EffectopediaObjectUnion.ALL_DIFFERENT);
                break;
            default:
                changeListMask(DataSourceMerge.EffectopediaObjectUnion.ALL);
                break;
        }
        this.viewOptions = viewOptions;
    }

    private HighlightedTableCellRenderer highlighter;

    private DefaultTableCellRenderer defaultRenderer;

    private int viewOptions = DataSourceUI.VO_HIGHLIGHT + DataSourceUI.VO_ALL;

    protected EventListenerList eventListeners;

    protected TableRowSorter<TableModel> sorter;

    private DataSource data;

    private DataSourceMerge merge;

    private boolean sourceA;

    private MultiIndexSortedList<DataSourceMerge.EffectopediaObjectUnion> list;

    private int sortIndex = 1;

    private ArrayList<SourceTrace> pathways;

    private ArrayList<SourceTrace> fragments;

    private int pathwaysCount;

    private int fragmentsCount;

    private DataSourceEntriesTableUI sychronizedTable;

    private int sortClicks = 0;

    private int sortCoulmn = -1;
}
