package org.horen.ui.editors.columns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.horen.core.HorenPlugin;
import org.horen.core.util.ClassExtensionHandler;
import org.horen.core.util.ExtensionHelper;
import org.horen.ui.resources.Resources;

/**
 * Manages the columns and column providers for a given column viewer.
 * That is storing, restoring and changing columns also for multiple 
 * instances of the workbench part.
 * 
 * @author Steffen
 */
public class ColumnManager {

    private static final String PREFERENCE_COLUMNS_SUFFIX = ".columns.";

    private static final String PREFERENCE_SORTING_COLUMN_SUFFIX = ".lastSorted";

    private static final String PREFERENCE_SORT_ORDER_SUFFIX = ".sortOrder";

    private static final String INTERNAL_KEY_COLUMN = "org.horen.key";

    private static final String EXTENSION_POINT = "org.horen.ColumnProvider";

    private static Map<String, List<ColumnManager>> m_Managers = null;

    private String m_PartID = null;

    private int m_PartOpenIndex = 0;

    private ColumnViewer m_Viewer = null;

    private ColumnViewerComparator m_LastComparator = null;

    private SelectionListener m_HeaderListener = null;

    private List<ColumnProvider> m_CurrentColumns = null;

    private List<ColumnProvider> m_MandatoryColumns = null;

    private List<ColumnProvider> m_PresetColumns = null;

    private Map<String, ColumnProvider> m_AvailableColumns = null;

    private IDefaultColumnProvider m_DefaultColumnProvider = null;

    private String m_PreferenceKey = null;

    /**
	 * Static constructor.
	 */
    static {
        m_Managers = new HashMap<String, List<ColumnManager>>();
    }

    /**
	 * Hide constructor. Intended to be used by this class only.
	 */
    private ColumnManager(String partID, ColumnViewer viewer, int openIndex) {
        m_PartID = partID;
        m_Viewer = viewer;
        m_PartOpenIndex = openIndex;
        m_HeaderListener = new ColumnHeaderListener();
        m_AvailableColumns = new HashMap<String, ColumnProvider>();
        m_MandatoryColumns = new ArrayList<ColumnProvider>();
        m_PresetColumns = new ArrayList<ColumnProvider>();
        m_PreferenceKey = m_PartID + PREFERENCE_COLUMNS_SUFFIX + m_PartOpenIndex;
        readExtensions();
    }

    /**
	 * Reads the registered extensions for the column providers.
	 */
    protected void readExtensions() {
        ExtensionHelper.getExtensionObjects(EXTENSION_POINT, new ClassExtensionHandler<ColumnProvider>(ColumnProvider.class) {

            @Override
            public void handleConfigurationElement(IConfigurationElement extension, ColumnProvider column) {
                m_AvailableColumns.put(column.getColumnID(), column);
                String attribute = extension.getAttribute("mandatory");
                if (attribute != null && Boolean.parseBoolean(attribute)) {
                    m_MandatoryColumns.add(column);
                    m_PresetColumns.add(column);
                } else {
                    attribute = extension.getAttribute("preset");
                    if (attribute != null && Boolean.parseBoolean(attribute)) {
                        m_PresetColumns.add(column);
                    }
                }
            }
        });
    }

    /**
	 * Get the instance of the column manager for the given ID. It is highly
	 * recommended to call the <code>release</code> method when the column manager
	 * is no longer in use (e.g. when disposing an editor).
	 */
    public static ColumnManager getByPartID(String partID, ColumnViewer viewer) {
        if (m_Managers.containsKey(partID) == false) {
            m_Managers.put(partID, new ArrayList<ColumnManager>());
        }
        List<ColumnManager> managers = m_Managers.get(partID);
        boolean[] inUse = new boolean[managers.size()];
        for (ColumnManager cm : managers) {
            if (cm.m_PartOpenIndex < inUse.length) {
                inUse[cm.m_PartOpenIndex] = true;
            }
        }
        int openIndex = managers.size();
        for (int i = 0; i < inUse.length; i++) {
            if (inUse[i] == false) {
                openIndex = i;
                break;
            }
        }
        ColumnManager manager = new ColumnManager(partID, viewer, openIndex);
        managers.add(manager);
        return manager;
    }

    /**
	 * Releases the manager. This is meaningful because the open index of the next
	 * calls to <code>getByPartID</code> depends on the column managers in use.
	 * 
	 * @param manager the manager to release
	 */
    public static void release(ColumnManager manager) {
        if (m_Managers.containsKey(manager.m_PartID) == true) {
            m_Managers.get(manager.m_PartID).remove(manager);
        }
    }

    /**
	 * Returns the available columns that are registered to the column
	 * manager.
	 */
    public List<ColumnProvider> getAvailableColumns() {
        Collection<ColumnProvider> values = m_AvailableColumns.values();
        return Arrays.asList(values.toArray(new ColumnProvider[values.size()]));
    }

    /**
	 * Removes the previous visible columns of the column viewer and adds
	 * the given columns. 
	 * 
	 * @param columns Columns to show
	 */
    public void applyColumns(List<ColumnProvider> columns) {
        m_CurrentColumns = columns;
        if (m_Viewer != null && m_Viewer.getControl() != null && m_Viewer.getControl().isDisposed() == false) {
            Item[] prev_columns = getViewerColumns();
            for (int i = 0; i < prev_columns.length; i++) {
                if (prev_columns[i] instanceof TableColumn) {
                    ((TableColumn) prev_columns[i]).removeSelectionListener(m_HeaderListener);
                } else if (prev_columns[i] instanceof TreeColumn) {
                    ((TreeColumn) prev_columns[i]).removeSelectionListener(m_HeaderListener);
                }
                prev_columns[i].dispose();
            }
            for (ColumnProvider col : columns) {
                ViewerColumn vc = createViewerColumn(col);
                vc.setLabelProvider(col);
                vc.setEditingSupport(col.getEditingSupport(m_Viewer));
            }
            m_Viewer.refresh();
        }
    }

    /**
	 * @return The columns of the viewer as <code>Item</code> widgets
	 * because there is no base of table and tree columns
	 */
    private Item[] getViewerColumns() {
        Control widget = m_Viewer.getControl();
        Item[] prev_columns = new Item[0];
        if (widget instanceof Table) {
            prev_columns = ((Table) widget).getColumns();
        } else if (widget instanceof Tree) {
            prev_columns = ((Tree) widget).getColumns();
        }
        return prev_columns;
    }

    /**
	 * Creates a viewer column for the specified column provider. Only the 
	 * specialized viewer columns have the methods to initialize the viewer
	 * column.
	 * 
	 * @param column Column provider to create the column for
	 * @return The created viewer column
	 */
    protected ViewerColumn createViewerColumn(ColumnProvider column) {
        ViewerColumn c = null;
        if (m_Viewer instanceof TableViewer) {
            TableViewerColumn tc = new TableViewerColumn((TableViewer) m_Viewer, SWT.NONE);
            tc.getColumn().setText(Resources.getDefaultBundle().getString(column.getColumnID()));
            tc.getColumn().setMoveable(false);
            tc.getColumn().setWidth(column.getDefaultColumnWidth());
            tc.getColumn().addSelectionListener(m_HeaderListener);
            tc.getColumn().setData(INTERNAL_KEY_COLUMN, column);
            c = tc;
        } else if (m_Viewer instanceof TreeViewer) {
            TreeViewerColumn tc = new TreeViewerColumn((TreeViewer) m_Viewer, SWT.NONE);
            tc.getColumn().setText(Resources.getDefaultBundle().getString(column.getColumnID()));
            tc.getColumn().setMoveable(false);
            tc.getColumn().setWidth(column.getDefaultColumnWidth());
            tc.getColumn().addSelectionListener(m_HeaderListener);
            tc.getColumn().setData(INTERNAL_KEY_COLUMN, column);
            c = tc;
        }
        return c;
    }

    /**
	 * @return The currently used columns in the column viewer.
	 */
    public List<ColumnProvider> getCurrentColumns() {
        return (m_CurrentColumns == null) ? getAvailableColumns() : m_CurrentColumns;
    }

    /** 
	 * @return The columns that have to be displayed. E.g. the name column
	 * should always be visible.
	 */
    public List<ColumnProvider> getMandatoryColumns() {
        return m_MandatoryColumns;
    }

    /**
	 * @return The columns that should be displayed on first start up.
	 */
    public List<ColumnProvider> getPresetColumns() {
        return m_PresetColumns;
    }

    /**
	 * Sets the given default column provider to the column manager and to all of the
	 * column providers managed.
	 */
    public void setDefaultColumnProvider(IDefaultColumnProvider defaultColumnProvider) {
        m_DefaultColumnProvider = defaultColumnProvider;
        for (ColumnProvider cp : m_AvailableColumns.values()) {
            cp.setDefaultProvider(defaultColumnProvider);
        }
    }

    /**
	 * @return The previously set default column provider or null if not yet set.
	 */
    public IDefaultColumnProvider getDefaultColumnProvider() {
        return m_DefaultColumnProvider;
    }

    /**
	 * Stores the currently shown columns of the viewer in the preference
	 * store using the part ID as part of the preference key.
	 */
    public void storeColumns() {
        IPreferenceStore store = HorenPlugin.getInstance().getPreferenceStore();
        StringBuffer value = new StringBuffer();
        Map<ColumnProvider, Integer> widths = getColumnWidths();
        for (ColumnProvider column : m_CurrentColumns) {
            value.append(column.getColumnID());
            value.append("#");
            value.append(widths.containsKey(column) ? widths.get(column).intValue() : -1);
            value.append("|");
        }
        store.setValue(m_PreferenceKey, value.toString());
        String sortingColumnID = "";
        int sortOrder = ColumnViewerComparator.ORDER_ASCENDING;
        ColumnViewerComparator comparator = m_LastComparator;
        if (comparator != null) {
            ColumnProvider column = comparator.getColumnProvider();
            sortOrder = comparator.getSortOrder();
            if (column != null) {
                sortingColumnID = column.getColumnID();
            }
        }
        store.setValue(m_PreferenceKey + PREFERENCE_SORTING_COLUMN_SUFFIX, sortingColumnID);
        store.setValue(m_PreferenceKey + PREFERENCE_SORT_ORDER_SUFFIX, sortOrder);
    }

    /**
	 * @return A mapping of columns and their current width. If there is no key
	 * available for a column provider then the column is actually not visible.
	 */
    protected Map<ColumnProvider, Integer> getColumnWidths() {
        Map<ColumnProvider, Integer> result = new HashMap<ColumnProvider, Integer>();
        Item[] columns = getViewerColumns();
        for (int i = 0; i < columns.length; i++) {
            ColumnProvider column = (ColumnProvider) columns[i].getData(INTERNAL_KEY_COLUMN);
            if (column != null) {
                if (columns[i] instanceof TableColumn) {
                    result.put(column, ((TableColumn) columns[i]).getWidth());
                } else if (columns[i] instanceof TreeColumn) {
                    result.put(column, ((TreeColumn) columns[i]).getWidth());
                }
            }
        }
        return result;
    }

    /**
	 * Restores the columns from the preference store to the viewer.
	 */
    public void restoreColumns() {
        IPreferenceStore store = HorenPlugin.getInstance().getPreferenceStore();
        String value = store.getString(m_PreferenceKey);
        String[] columns = value.split("\\|");
        List<ColumnProvider> restoredColumns = new ArrayList<ColumnProvider>();
        Map<ColumnProvider, Integer> widths = new HashMap<ColumnProvider, Integer>();
        for (int i = 0; i < columns.length; i++) {
            String[] props = columns[i].split("#");
            if (props.length > 0 && m_AvailableColumns.containsKey(props[0])) {
                ColumnProvider restored = m_AvailableColumns.get(props[0]);
                restoredColumns.add(restored);
                if (props.length > 1) {
                    widths.put(restored, Integer.valueOf(props[1]));
                }
            }
        }
        if (restoredColumns.size() <= 0) {
            restoredColumns = getPresetColumns();
        }
        applyColumns(restoredColumns);
        setColumnWidths(widths);
        int sortOrder = store.getInt(m_PreferenceKey + PREFERENCE_SORT_ORDER_SUFFIX);
        sortOrder = (store.contains(m_PreferenceKey + PREFERENCE_SORT_ORDER_SUFFIX) == false) ? ColumnViewerComparator.ORDER_ASCENDING : sortOrder;
        ColumnProvider sortingColumnProvider = null;
        String sortingColumn = store.getString(m_PreferenceKey + PREFERENCE_SORTING_COLUMN_SUFFIX);
        if (m_AvailableColumns.containsKey(sortingColumn)) {
            sortingColumnProvider = m_AvailableColumns.get(sortingColumn);
        } else if (restoredColumns.size() > 0) {
            sortingColumnProvider = restoredColumns.get(0);
            sortOrder = ColumnViewerComparator.ORDER_ASCENDING;
        }
        if (sortingColumnProvider != null) {
            setSortingColumn(sortingColumnProvider, sortOrder);
        }
    }

    /**
	 * Sets the given column widths to the viewer columns.
	 * @param widths a mapping of columns and their width
	 */
    protected void setColumnWidths(Map<ColumnProvider, Integer> widths) {
        Item[] columns = getViewerColumns();
        for (Item column : columns) {
            ColumnProvider cprov = (ColumnProvider) column.getData(INTERNAL_KEY_COLUMN);
            if (widths.containsKey(cprov)) {
                int width = widths.get(cprov).intValue();
                if (column instanceof TableColumn) {
                    ((TableColumn) column).setWidth(width);
                } else if (column instanceof TreeColumn) {
                    ((TreeColumn) column).setWidth(width);
                }
            }
        }
    }

    /**
	 * Sets the column to the viewer that will be used for sorting.
	 * 
	 * @param column the column to take comparator for sort
	 * @param switchSortOrder true to switch between ascending or descending
	 * sort order depending on current sort order, or false to set to
	 * ascending order
	 */
    public void setSortingColumn(ColumnProvider column, boolean switchSortOrder) {
        int sortOrder = ColumnViewerComparator.ORDER_ASCENDING;
        if (switchSortOrder) {
            ColumnViewerComparator cvc = m_LastComparator;
            if (cvc != null && cvc.getColumnProvider() == column) {
                int previousSortOrder = cvc.getSortOrder();
                sortOrder = previousSortOrder == ColumnViewerComparator.ORDER_ASCENDING ? ColumnViewerComparator.ORDER_DESCENDING : ColumnViewerComparator.ORDER_ASCENDING;
            }
        }
        setSortingColumn(column, sortOrder);
    }

    /**
	 * Sets the column to the viewer that will be used for sorting.
	 * 
	 * @param column the column to take comparator for sort
	 * @param sortOrder the sorting order to apply
	 * @see ColumnViewerComparator#ORDER_ASCENDING
	 * @see ColumnViewerComparator#ORDER_DESCENDING
	 */
    public void setSortingColumn(ColumnProvider column, int sortOrder) {
        m_LastComparator = new ColumnViewerComparator(column, sortOrder);
        m_Viewer.setComparator(m_LastComparator);
        Item sortingColumn = getSortingColumn();
        if (m_Viewer instanceof TableViewer) {
            ((TableViewer) m_Viewer).getTable().setSortColumn((TableColumn) sortingColumn);
            ((TableViewer) m_Viewer).getTable().setSortDirection(sortOrder);
        } else if (m_Viewer instanceof TreeViewer) {
            ((TreeViewer) m_Viewer).getTree().setSortColumn((TreeColumn) sortingColumn);
            ((TreeViewer) m_Viewer).getTree().setSortDirection(sortOrder);
        }
    }

    /**
	 * @return The column that the viewer is actually sorted with. Note
	 * that the return type is a <code>Item</code> widget because there
	 * is no base of table and tree columns. The return value may is
	 * null if no sorting is done at the moment.
	 */
    private Item getSortingColumn() {
        ColumnProvider sortingColumnProvider = getSortingColumnProvider();
        if (sortingColumnProvider != null) {
            Item[] columns = getViewerColumns();
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].getData(INTERNAL_KEY_COLUMN) == sortingColumnProvider) {
                    return columns[i];
                }
            }
        }
        return null;
    }

    /**
	 * @return The column provider that the viewer is actually sorted or
	 * null if there is no sorting at the moment.
	 */
    private ColumnProvider getSortingColumnProvider() {
        if (m_LastComparator != null) {
            return m_LastComparator.getColumnProvider();
        }
        return null;
    }

    private class ColumnHeaderListener implements SelectionListener {

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            Object source = e.getSource();
            ColumnProvider selected = null;
            if (source instanceof TableColumn) {
                selected = (ColumnProvider) ((TableColumn) source).getData(INTERNAL_KEY_COLUMN);
            } else if (source instanceof TreeColumn) {
                selected = (ColumnProvider) ((TreeColumn) source).getData(INTERNAL_KEY_COLUMN);
            }
            if (selected != null) {
                setSortingColumn(selected, true);
            }
        }
    }
}
