package com.thyante.thelibrarian.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import com.thyante.thelibrarian.dialogs.EditListDialog;
import com.thyante.thelibrarian.util.I18n;

/**
 * @author Matthias-M. Christen
 *
 */
public class MultiCheckList extends Composite {

    private static final String ATTR_SELECTED = "selected";

    /**
	 * The list of items
	 */
    protected Map<String, Boolean> m_mapItems;

    /**
	 * Flag indicating whether the list could be modified by the user at runtime
	 */
    private boolean m_bCanModifyList;

    /**
	 * The table viewer displaying the check list items
	 */
    private CheckboxTableViewer m_table;

    /**
	 * The composite that will contain the hyperlinks to add/edit items
	 */
    private Composite m_cmpAdd;

    /**
	 * Listener list
	 */
    private List<IMultiCheckListListener> m_listListeners;

    /**
	 * @param cmpParent
	 * @param nStyle
	 */
    public MultiCheckList(Composite cmpParent, int nStyle) {
        this(cmpParent, nStyle, false);
    }

    /**
	 * @param cmpParent
	 * @param nStyle
	 * @param bCanModifyList
	 */
    public MultiCheckList(Composite cmpParent, int nStyle, boolean bCanModifyList) {
        super(cmpParent, nStyle);
        m_bCanModifyList = bCanModifyList;
        m_mapItems = new TreeMap<String, Boolean>();
        m_listListeners = new LinkedList<IMultiCheckListListener>();
        createUI();
    }

    /**
	 * Sets the items that are displayed in the table.
	 * @param mapItems A map with the item texts as keys and booleans as
	 * 	values indicating whether the corresponding item is checked or not
	 */
    public void setItems(Map<String, Boolean> mapItems) {
        m_mapItems.clear();
        if (mapItems != null) for (String s : mapItems.keySet()) m_mapItems.put(s, mapItems.get(s));
        updateTable();
    }

    public void setItems(Iterable<String> listItems) {
        m_mapItems.clear();
        for (String strKey : listItems) m_mapItems.put(strKey, false);
        updateTable();
    }

    /**
	 * Returns the item map. The keys of the map are the items' texts,
	 * while the map values are booleans that indicate whether the item
	 * is checked or not.
	 * @return The item map
	 */
    public Map<String, Boolean> getItems() {
        updateMap();
        return m_mapItems;
    }

    /**
	 * Selects all the items in the list <code>listSelectedItems</code>.
	 * If an item contained in <code>listSelectedItems</code> doesn't exist yet
	 * in the current item list, it will be added to the list.
	 * @param listSelectedItems The list of items to select
	 */
    public void setSelectedItems(List<String> listSelectedItems) {
        for (String strKey : m_mapItems.keySet()) m_mapItems.put(strKey, false);
        if (listSelectedItems != null) for (String s : listSelectedItems) m_mapItems.put(s, true);
        updateTable();
    }

    /**
	 * Returns the list of all currently selected items.
	 * @return The list of selected items
	 */
    public List<String> getSelectedItems() {
        updateMap();
        List<String> l = new ArrayList<String>(m_mapItems == null ? 0 : m_mapItems.size());
        if (m_mapItems != null) for (Map.Entry<String, Boolean> entry : m_mapItems.entrySet()) if (entry.getValue()) l.add(entry.getKey());
        return l;
    }

    /**
	 * Creates the UI controls.
	 */
    protected void createUI() {
        GridLayout layout = new GridLayout(1, true);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        setLayout(layout);
        createTable();
        if (m_bCanModifyList) createModifyControls();
    }

    /**
	 * Creates the table control.
	 */
    protected void createTable() {
        m_table = CheckboxTableViewer.newCheckList(this, SWT.NONE);
        m_table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        m_table.setContentProvider(new IStructuredContentProvider() {

            public Object[] getElements(Object inputElement) {
                Object[] rgElements = new Object[m_mapItems.size()];
                int i = 0;
                for (String s : m_mapItems.keySet()) rgElements[i++] = s;
                return rgElements;
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
        m_table.setLabelProvider(new ILabelProvider() {

            public Image getImage(Object element) {
                return null;
            }

            public String getText(Object element) {
                return element instanceof String ? (String) element : element.toString();
            }

            public void addListener(ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void removeListener(ILabelProviderListener listener) {
            }
        });
    }

    /**
	 * Creates the add/edit hyperlinks below the table.
	 */
    protected void createModifyControls() {
        Label lblSeparator = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
        lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        m_cmpAdd = new Composite(this, SWT.NONE);
        m_cmpAdd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout layoutAdd = new GridLayout(2, false);
        layoutAdd.marginWidth = 0;
        layoutAdd.marginHeight = 0;
        m_cmpAdd.setLayout(layoutAdd);
        final Hyperlink linkAdd = new Hyperlink(m_cmpAdd, SWT.NONE);
        linkAdd.setText(I18n.xl8("Add"));
        linkAdd.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                InputDialog dlg = new InputDialog(getShell(), I18n.xl8("Add Item"), I18n.xl8("Please enter the item you want to add"), "", null);
                if (dlg.open() == Window.OK) {
                    m_mapItems.put(dlg.getValue(), false);
                    updateTable();
                    List<ItemChange<String>> listChanges = new ArrayList<ItemChange<String>>(1);
                    listChanges.add(new ItemChange<String>(null, dlg.getValue(), ItemChange.ChangeType.ADDED));
                    fireListElementsChangedEvent(listChanges);
                }
            }
        });
        final Hyperlink linkEdit = new Hyperlink(m_cmpAdd, SWT.NONE);
        linkEdit.setText(I18n.xl8("Edit"));
        linkEdit.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                List<AttributedText> list = new LinkedList<AttributedText>();
                Map<AttributedText, String> mapOldValues = new HashMap<AttributedText, String>();
                for (String s : m_mapItems.keySet()) {
                    AttributedText at = new AttributedText(s, ATTR_SELECTED, m_mapItems.get(s));
                    list.add(at);
                    mapOldValues.put(at, s);
                }
                EditListDialog dlg = new EditListDialog(getShell(), I18n.xl8("Edit Values"), list, true, "");
                if (dlg.open() == Dialog.OK) {
                    List<ItemChange<String>> listChanges = new LinkedList<ItemChange<String>>();
                    m_mapItems.clear();
                    for (AttributedText at : dlg.getList()) if (!at.isEmpty()) {
                        Boolean bIsSelected = (Boolean) at.getAttribute(ATTR_SELECTED);
                        m_mapItems.put(at.getText(), bIsSelected == null ? false : bIsSelected);
                        String strOldValue = mapOldValues.get(at);
                        if (strOldValue == null) {
                            listChanges.add(new ItemChange<String>(null, at.getText(), ItemChange.ChangeType.ADDED));
                        } else {
                            if (!strOldValue.equals(at.getText())) listChanges.add(new ItemChange<String>(strOldValue, at.getText(), ItemChange.ChangeType.VALUE_CHANGED));
                        }
                        mapOldValues.remove(at);
                    }
                    for (AttributedText at : mapOldValues.keySet()) listChanges.add(new ItemChange<String>(at.getText(), null, ItemChange.ChangeType.REMOVED));
                    if (listChanges.size() > 0) fireListElementsChangedEvent(listChanges);
                    updateTable();
                }
            }
        });
    }

    /**
	 * Returns the desired height of the list control.
	 * @return The preferred height
	 */
    public int getHeight() {
        Table table = m_table.getTable();
        int nTableHeight = table.getItemHeight() * m_mapItems.size();
        Point ptAdd = m_cmpAdd.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        return nTableHeight + ptAdd.y + 10;
    }

    /**
	 * Updates the internal map with the values from the UI. 
	 */
    protected void updateMap() {
        for (String strItem : m_mapItems.keySet()) m_mapItems.put(strItem, m_table.getChecked(strItem));
    }

    /**
	 * Updates the UI.
	 */
    protected void updateTable() {
        if (m_table == null || m_table.getTable().isDisposed()) return;
        m_table.setInput(m_mapItems);
        for (String s : m_mapItems.keySet()) m_table.setChecked(s, m_mapItems.get(s));
    }

    /**
	 * Adds a listener that is notified whenever the list of items changes.
	 * @param listener The listener to add to the listener list
	 */
    public void addMultiCheckListListener(IMultiCheckListListener listener) {
        if (!m_listListeners.contains(listener)) m_listListeners.add(listener);
    }

    /**
	 * Removes the listener <code>listener</code> from the list of listeners.
	 * @param listener The listener to remove
	 */
    public void removeMultiCheckListListener(IMultiCheckListListener listener) {
        m_listListeners.remove(listener);
    }

    protected void fireListElementsChangedEvent(List<ItemChange<String>> listChanges) {
        for (IMultiCheckListListener l : m_listListeners) l.onListElementsChanged(listChanges);
    }

    /**
	 * Shows or hides the vertical scrollbar.
	 * @param bIsScrollbarVisible Flag determining whether the scrollbars are
	 * 	shown or hidden
	 */
    public void setScrollbarVisible(boolean bIsScrollbarVisible) {
        ScrollBar bar = m_table.getTable().getVerticalBar();
        if (bar != null) bar.setVisible(bIsScrollbarVisible);
    }

    public static void main(String[] args) {
        ApplicationWindow wnd = new ApplicationWindow(null) {

            @Override
            protected Control createContents(Composite parent) {
                Composite c = (Composite) super.createContents(parent);
                c.setLayout(new GridLayout(1, true));
                MultiCheckList l = new MultiCheckList(c, SWT.BORDER, true);
                l.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
                Map<String, Boolean> m = new HashMap<String, Boolean>();
                m.put("BeeBee Ashdene", false);
                m.put("Cassie69er Teardrop", true);
                m.put("Fere Ansar", true);
                m.put("Marianna Barbarossa", false);
                l.setItems(m);
                return c;
            }
        };
        wnd.setBlockOnOpen(true);
        wnd.open();
    }
}
