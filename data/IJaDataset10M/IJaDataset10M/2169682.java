package com.thyante.thelibrarian.components;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class MultiCheckCombo implements KeyListener, IMultilinePopupEditorListener, IMultiCheckListListener {

    /**
	 * Style constant indicating that the list of the multi check combo is
	 * editable by the user
	 */
    public static final int EDITABLE_LIST = 0x40000000;

    /**
	 * The composite holding all the member controls
	 */
    protected Composite m_cmpComposite;

    /**
	 * The text control
	 */
    protected Text m_text;

    /**
	 * The list of items
	 */
    protected List<String> m_listItems;

    /**
	 * The list of selected items
	 */
    protected List<String> m_listSelectedItems;

    /**
	 * The popup window containing the editor
	 */
    private MultiCheckComboDialog m_popup;

    /**
	 * Indicates whether the popup dialog is currently shown or hidden
	 */
    private boolean m_bIsPopupOpen;

    /**
	 * Flag indicating whether the list could be edited by the user
	 */
    protected boolean m_bIsListEditable;

    /**
	 * List of listeners that have to be added to the popup once the popup is create
	 */
    protected List<IMultilinePopupEditorListener> m_listPopupListenersToAdd;

    /**
	 * List of multi check list listeners
	 */
    private List<IMultiCheckListListener> m_listMultiCheckListListenersToAdd;

    /**
	 * A runnable that, when run, clears the {@link MultiCheckCombo#m_bIsPopupOpen} flag, i.e.
	 * sets it to <code>false</code>
	 */
    private Runnable m_runClearOpenFlag;

    /**
	 * Creates the control without creating the UI representation.
	 */
    public MultiCheckCombo() {
        m_cmpComposite = null;
        m_text = null;
        m_listItems = null;
        m_listSelectedItems = null;
        m_popup = null;
        m_bIsPopupOpen = false;
        m_listPopupListenersToAdd = null;
        m_listMultiCheckListListenersToAdd = null;
    }

    /**
	 * Creates the multi-check combo control.
	 * @param cmpParent The parent composite
	 * @param nStyle The style applied to the composite embedding the basic UI controls
	 *	If the bit {@link MultiCheckCombo#EDITABLE_LIST} is set,
	 * 	the list is made editable for the user
	 */
    public MultiCheckCombo(Composite cmpParent, int nStyle) {
        this();
        createUI(cmpParent, nStyle);
    }

    /**
	 * Creates the component's UI.
	 * @param cmpParent The parent composite
	 * @param nStyle The style. If the bit {@link MultiCheckCombo#EDITABLE_LIST} is set,
	 * 	the list is made editable for the user
	 * @return The control that represents the UI
	 */
    public Composite createUI(Composite cmpParent, int nStyle) {
        m_bIsListEditable = (nStyle & EDITABLE_LIST) != 0;
        nStyle &= ~EDITABLE_LIST;
        m_cmpComposite = new Composite(cmpParent, nStyle);
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        m_cmpComposite.setLayout(layout);
        m_text = new Text(m_cmpComposite, SWT.NONE);
        m_text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true));
        m_text.setEditable(false);
        m_text.addKeyListener(this);
        m_text.setBackground(m_text.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        Button btn = new Button(m_cmpComposite, SWT.ARROW | SWT.DOWN);
        btn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!m_bIsPopupOpen) showDropDown();
            }
        });
        return m_cmpComposite;
    }

    /**
	 * Returns the control displaying the text.
	 * @return The control
	 */
    public Control getControl() {
        return m_text;
    }

    /**
	 * Sets the lines.
	 * @param listLines
	 */
    public void setItems(List<String> listItems) {
        m_listItems = listItems;
    }

    /**
	 * Returns the lines.
	 * @return the text lines contained in the component
	 */
    public List<String> getItems() {
        return m_listItems;
    }

    /**
	 * Sets the selected items.
	 * @param listSelectedItems The list of items to select. If <code>null</code> all items
	 * 	are deselected.
	 * @see MultiCheckCombo#selectAllItems()
	 */
    public void setSelectedItems(List<String> listSelectedItems) {
        m_listSelectedItems = listSelectedItems;
        StringBuffer sb = new StringBuffer();
        if (listSelectedItems != null) for (String strItem : listSelectedItems) {
            sb.append(strItem);
            sb.append(", ");
        }
        m_text.setText(sb.length() == 0 ? "" : sb.substring(0, sb.length() - 2));
    }

    /**
	 * Selects all the items that are in the list.
	 * <p>To deselect all items, use
	 * <pre>
	 * 	setSelectedItems (null);
	 * </pre>
	 * 
	 * @see MultiCheckCombo#setSelectedItems(List)
	 */
    public void selectAllItems() {
        m_listSelectedItems.clear();
        m_listSelectedItems.addAll(m_listItems);
        setSelectedItems(m_listSelectedItems);
    }

    /**
	 * Returns the list of all currently selected items.
	 * @return The list of selected items
	 */
    public List<String> getSelectedItems() {
        return m_listSelectedItems;
    }

    /**
	 * Sets the custom data object.
	 * @param objData The data to attach to the component
	 * @see Control#setData(Object)
	 */
    public void setData(Object objData) {
        m_cmpComposite.setData(objData);
    }

    /**
	 * Returns the custom data attached to the component.
	 * @return The custom data attached to the component
	 * @see Control#getData()
	 */
    public Object getData() {
        return m_cmpComposite.getData();
    }

    /**
	 * Shows the drop down popup.
	 */
    public void showDropDown() {
        if (m_popup == null) {
            m_popup = new MultiCheckComboDialog(m_cmpComposite, m_bIsListEditable);
            m_popup.addPopupListener(this);
            m_popup.addMultiCheckListListener(this);
        }
        if (m_listPopupListenersToAdd != null) {
            for (IMultilinePopupEditorListener listener : m_listPopupListenersToAdd) m_popup.addPopupListener(listener);
            m_listPopupListenersToAdd.clear();
        }
        if (m_listMultiCheckListListenersToAdd != null) {
            for (IMultiCheckListListener listener : m_listMultiCheckListListenersToAdd) m_popup.addMultiCheckListListener(listener);
            m_listMultiCheckListListenersToAdd.clear();
        }
        m_bIsPopupOpen = true;
        m_popup.setItems(m_listItems, m_listSelectedItems);
        m_popup.open();
    }

    public void addPopupListener(IMultilinePopupEditorListener listener) {
        if (m_popup != null) m_popup.addPopupListener(listener); else {
            if (m_listPopupListenersToAdd == null) m_listPopupListenersToAdd = new LinkedList<IMultilinePopupEditorListener>();
            m_listPopupListenersToAdd.add(listener);
        }
    }

    public void removePopupListener(IMultilinePopupEditorListener listener) {
        if (m_popup != null) m_popup.removePopupListener(listener);
        if (m_listPopupListenersToAdd != null) m_listPopupListenersToAdd.remove(listener);
    }

    public void addMultiCheckListListener(IMultiCheckListListener listener) {
        if (m_popup != null) m_popup.addMultiCheckListListener(listener); else {
            if (m_listMultiCheckListListenersToAdd == null) m_listMultiCheckListListenersToAdd = new LinkedList<IMultiCheckListListener>();
            m_listMultiCheckListListenersToAdd.add(listener);
        }
    }

    public void removeMultiCheckListListener(IMultiCheckListListener listener) {
        if (m_popup != null) m_popup.removeMultiCheckListListener(listener);
        if (m_listMultiCheckListListenersToAdd != null) m_listMultiCheckListListenersToAdd.remove(listener);
    }

    public void keyPressed(KeyEvent e) {
        if (e.keyCode == SWT.F4 || (e.keyCode == SWT.ARROW_DOWN)) showDropDown(); else if ((e.keyCode == '\r' || e.keyCode == '\n') && m_popup.getShell() != null && !m_popup.getShell().isDisposed()) {
            m_popup.close();
            m_bIsPopupOpen = false;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void onPopupOpen() {
    }

    public void onPopupClose(List<String> listLines) {
        setSelectedItems(listLines);
        if (m_runClearOpenFlag == null) {
            m_runClearOpenFlag = new Runnable() {

                public void run() {
                    m_bIsPopupOpen = false;
                }
            };
        }
        m_text.getDisplay().timerExec(100, m_runClearOpenFlag);
    }

    public void onEndEditing() {
    }

    public void onEnterPressed() {
    }

    public void onListElementsChanged(List<ItemChange<String>> listChanges) {
        for (ItemChange<String> change : listChanges) {
            if (change.getType() == ItemChange.ChangeType.ADDED || change.getType() == ItemChange.ChangeType.VALUE_CHANGED) m_listItems.add(change.getNewValue());
            if (change.getType() == ItemChange.ChangeType.REMOVED || change.getType() == ItemChange.ChangeType.VALUE_CHANGED) m_listItems.remove(change.getOldValue());
        }
    }

    public void onStartEditing() {
    }
}
