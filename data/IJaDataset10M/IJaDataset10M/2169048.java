package org.horen.ui.panes.database;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.horen.core.HorenPlugin;
import org.horen.core.db.DataBase;
import org.horen.ui.dialogs.WarningDialog;
import org.horen.ui.editors.columns.ColumnManager;
import org.horen.ui.editors.columns.ColumnProvider;
import org.horen.ui.panes.ObjectPropertyPane;
import org.horen.ui.panes.database.data.PaneSettings;
import org.horen.ui.util.NonEmptyInputValidator;
import org.horen.ui.util.SelectionHelper;
import org.horen.ui.util.ViewerHelper;

/**
 * Abstract base class for all panes shown for a database object. The general
 * purpose of this class is to reduce code duplication.
 * 
 * @author Steffen
 */
public abstract class DatabasePane extends ObjectPropertyPane<DataBase> {

    protected static final String KEY_COLUMN_ITEM_NAME = "Panes.Database.Column.itemName";

    private boolean m_Changed = false;

    private TableViewer m_ItemViewer = null;

    private ColumnManager m_ColumnManager = null;

    private Button m_AddButton = null;

    private Button m_RemoveButton = null;

    private Button m_EditButton = null;

    private PaneSettings m_Settings = null;

    /**
	 * Constructor.
	 */
    public DatabasePane() {
        super();
        m_Settings = new PaneSettings();
        configurePane(m_Settings);
    }

    /**
	 * Allows implementors to set flags and keys for the behavior of the
	 * pane.
	 * 
	 * @param settings settings to change
	 */
    protected abstract void configurePane(PaneSettings settings);

    /**
	 * @return The pane settings configured by the pane. You should not modify
	 * the returned object.
	 */
    protected PaneSettings getSettings() {
        return m_Settings;
    }

    /**
	 * Creates the widget with that the user can set / change the priority
	 * models of a database.
	 */
    @Override
    public void createControl(Composite parent) {
        Composite content = new Composite(parent, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        content.setLayout(new GridLayout(1, false));
        PlatformUI.getWorkbench().getHelpSystem().setHelp(content, getContextHelpID());
        createItemListArea(content);
        m_ColumnManager = ColumnManager.getByPartID("BasePriorityPane", m_ItemViewer);
        configureColumns(m_ItemViewer, m_ColumnManager);
        createItemArea(content);
        m_ItemViewer.getTable().select(0);
        setControl(content);
    }

    /**
	 * @return The identification of the context help of this pane.
	 */
    protected abstract String getContextHelpID();

    /**
	 * Creates a composite with a label, a list and buttons for item
	 * manipulation.
	 * 
	 * @param parent composite to add widgets to
	 */
    private void createItemListArea(Composite parent) {
        Composite content = new Composite(parent, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, m_Settings.viewerGrabExtraSpace));
        GridLayout layout = new GridLayout(3, false);
        layout.marginWidth = layout.marginHeight = 0;
        content.setLayout(layout);
        GridData data;
        Label label = new Label(content, SWT.NONE);
        label.setText(m_Settings.itemListLabel);
        label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        m_ItemViewer = new TableViewer(content, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        m_ItemViewer.setContentProvider(new ArrayContentProvider());
        m_ItemViewer.setInput(getItemList());
        m_ItemViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                handleItemSelection();
            }
        });
        data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.widthHint = 100;
        if (m_Settings.viewerGrabExtraSpace == false) {
            data.heightHint = 60;
        }
        m_ItemViewer.getControl().setLayoutData(data);
        if (m_Settings.editOnDoubleClick) {
            ViewerHelper.enableDoubleClickEditing(m_ItemViewer);
        }
        Composite buttons = new Composite(content, SWT.NONE);
        buttons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        layout = new GridLayout(1, false);
        layout.marginWidth = layout.marginHeight = 0;
        buttons.setLayout(layout);
        createItemButtons(buttons);
    }

    /**
	 * Creates the buttons that modify the items in the item list.
	 * Default implementation creates buttons for add, remove and
	 * edit name operation.
	 * 
	 * @param buttonArea parent composite for buttons
	 */
    protected void createItemButtons(Composite buttonArea) {
        SelectionListener listener = new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.getSource().equals(m_AddButton)) {
                    addItem();
                } else if (e.getSource().equals(m_RemoveButton)) {
                    removeItem();
                } else if (e.getSource().equals(m_EditButton)) {
                    editItem();
                }
            }
        };
        m_AddButton = new Button(buttonArea, SWT.PUSH);
        m_AddButton.setImage(HorenPlugin.imageDescriptorFromPlugin(HorenPlugin.PLUGIN_ID, "res/images/views/properties/plus_gruen2_16x16.png").createImage());
        m_AddButton.addSelectionListener(listener);
        m_AddButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        m_RemoveButton = new Button(buttonArea, SWT.PUSH);
        m_RemoveButton.setImage(HorenPlugin.imageDescriptorFromPlugin(HorenPlugin.PLUGIN_ID, "res/images/views/properties/kreuz_rot2_16x16.png").createImage());
        m_RemoveButton.addSelectionListener(listener);
        m_RemoveButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        if (m_Settings.showEditButton) {
            m_EditButton = new Button(buttonArea, SWT.PUSH);
            m_EditButton.setImage(HorenPlugin.imageDescriptorFromPlugin(HorenPlugin.PLUGIN_ID, "res/images/views/properties/edit.png").createImage());
            m_EditButton.addSelectionListener(listener);
            m_EditButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        }
    }

    /**
	 * (see base)
	 */
    @Override
    public void dispose() {
        if (m_ColumnManager != null) {
            ColumnManager.release(m_ColumnManager);
        }
        super.dispose();
    }

    /**
	 * Handles the post selection changed event of the item viewer.
	 */
    private void handleItemSelection() {
        DatabaseItem selected = SelectionHelper.getFirstSelectedObject(m_ItemViewer.getSelection(), DatabaseItem.class);
        m_RemoveButton.setEnabled(selected != null);
        handleItemSelection(selected);
    }

    /**
	 * Handles the selection of an item from the item list and allows
	 * implementors to update the item area. The default implementation
	 * does nothing.
	 * 
	 * @param item item that was selected or null if selection was removed
	 */
    protected void handleItemSelection(DatabaseItem item) {
    }

    /**
	 * Handles the selection of the remove button. That is asking for
	 * remove, the remove itself and the refreshing of item viewer.
	 */
    private void removeItem() {
        boolean remove = true;
        if (m_Settings.removeWarning) {
            WarningDialog dlg = new WarningDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), m_Settings.removeWarningTitle, m_Settings.removeWarningMessage, true, m_Settings.removeWarningPreferenceKey);
            remove = dlg.open() == WarningDialog.YES;
        }
        if (remove) {
            Set<? extends DatabaseItem> selectedItems = SelectionHelper.getSelectedObjects(m_ItemViewer.getSelection(), DatabaseItem.class);
            removeItems(selectedItems);
            m_ItemViewer.setSelection(new StructuredSelection());
            m_ItemViewer.refresh();
            handleContentChanged();
        }
    }

    /**
	 * Removes the given items from the internal data structure.
	 * 
	 * @param items items to remove
	 */
    protected abstract void removeItems(Set<? extends DatabaseItem> items);

    /**
	 * Handles the selection of the edit button. That is invoking the
	 * <code>editItem(DatabaseItem)</code> method to edit the item.
	 */
    private void editItem() {
        DatabaseItem selected = SelectionHelper.getFirstSelectedObject(m_ItemViewer.getSelection(), DatabaseItem.class);
        if (selected != null) {
            editItem(selected);
        }
    }

    /**
	 * Edits the given item. That is by default editing the item in-place of
	 * the item viewer.
	 * 
	 * @param item the item to edit
	 */
    protected void editItem(DatabaseItem item) {
        m_ItemViewer.editElement(item, m_Settings.editNewItemColumnIndex);
    }

    /**
	 * Handles the selection of the add button. That is asking for
	 * a new name, add the new item and the refreshing of item viewer. 
	 */
    private void addItem() {
        String newName = null;
        if (m_Settings.askForNewName) {
            InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), m_Settings.newNameTitle, m_Settings.newNameMessage, getNewItemName(m_Settings.defaultItemName, getItemList()), new NonEmptyInputValidator());
            if (dlg.open() != IDialogConstants.OK_ID) {
                return;
            }
            newName = dlg.getValue();
        }
        DatabaseItem newItem = addItem(newName);
        if (newItem != null) {
            m_ItemViewer.refresh();
            m_ItemViewer.setSelection(new StructuredSelection(newItem));
            if (m_Settings.askForNewName == false) {
                getItemViewer().editElement(newItem, m_Settings.editNewItemColumnIndex);
            }
            handleContentChanged();
        }
    }

    /**
	 * Adds an item with the given name to the internal data structure.
	 * 
	 * @param name the name of the new item or null if no name was
	 * entered for the item
	 * @return the item added or null if item could not be created
	 */
    protected abstract DatabaseItem addItem(String name);

    /**
	 * @return The internal data structure of the items as list.
	 */
    protected abstract List<? extends DatabaseItem> getItemList();

    /**
	 * Returns the table viewer that is used to show the list of items
	 * in the pane.
	 */
    protected TableViewer getItemViewer() {
        return m_ItemViewer;
    }

    /**
	 * Creates the area where a selected item of the list can be
	 * changed. The given parent composite contains the item list,
	 * so implementors can just add their widgets directly under
	 * it and assume that GridLayout is set with one column.
	 * 
	 * @param parent composite to add widgets to
	 */
    protected void createItemArea(Composite parent) {
    }

    /**
	 * Implementors should configure the columns for the item list.
	 * The default implementation adds the name column to the column
	 * manager. The column header is not visible by default.
	 * 
	 * @param list column viewer to configure the columns for
	 * @param manager the column manager to use for configuring
	 */
    protected void configureColumns(TableViewer list, ColumnManager manager) {
        ColumnProvider nameColumn = new ItemNameColumn();
        m_ColumnManager.applyColumns(Arrays.asList(nameColumn));
        m_ColumnManager.setSortingColumn(nameColumn, false);
    }

    /**
	 * Returns a name for a new database object that is unique, i.e. not contained
	 * in the given items list.
	 */
    public String getNewItemName(String newName, List<? extends DatabaseItem> items) {
        String defaultName = newName;
        boolean isExisting = false;
        int i = 1;
        do {
            isExisting = false;
            for (DatabaseItem item : items) {
                if (item.name.equals(defaultName)) {
                    isExisting = true;
                    defaultName = newName + "(" + i + ")";
                    i++;
                    break;
                }
            }
        } while (isExisting);
        return defaultName;
    }

    /**
	 * Marks the pane as changed.
	 */
    protected void handleContentChanged() {
        if (m_Changed == false) {
            m_Changed = true;
            handlePropertyChanged();
        }
    }

    /**
	 * Marks the pane as unchanged. It is intended to call this method
	 * when a button like "restore defaults" is pressed, so any changed
	 * data got lost and the pane changed state has to be updated now.
	 */
    protected void handleContentReset() {
        if (m_Changed == true) {
            m_Changed = false;
            handlePropertyChanged();
        }
    }

    /**
	 * (see base)
	 */
    @Override
    protected boolean checkPropertyChanged() {
        return m_Changed;
    }

    /**
	 * (see base)
	 */
    @Override
    public void setInput(DataBase inputObject) {
        m_Changed = false;
        refreshItems();
        super.setInput(inputObject);
    }

    /**
	 * Updates the item list from the database. Implementors should refresh
	 * their internal data structure and call the super method to refresh
	 * the item viewer.
	 */
    protected void refreshItems() {
        m_ItemViewer.setSelection(new StructuredSelection());
        m_ItemViewer.refresh();
    }

    /**
	 * (see base)
	 */
    @Override
    public void performApply(DataBase inputObject) {
        m_Changed = false;
        super.performApply(inputObject);
    }

    protected class DatabaseItem {

        public String name;

        public DatabaseItem(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    protected class ItemNameColumn extends ColumnProvider {

        @Override
        public String getColumnID() {
            return KEY_COLUMN_ITEM_NAME;
        }

        @Override
        public Object getValue(Object element) {
            if (element instanceof DatabaseItem) {
                DatabaseItem item = (DatabaseItem) element;
                return item.name;
            }
            return "";
        }

        @Override
        public Class<?> getValueClass() {
            return String.class;
        }

        @Override
        public int getDefaultColumnWidth() {
            return 150;
        }

        /**
		 * Adds support for editing the base priority name via a simple text
		 * widget.
		 */
        @Override
        public EditingSupport getEditingSupport(final ColumnViewer viewer) {
            EditingSupport e = new EditingSupport(viewer) {

                private String m_PreviousValue = null;

                @Override
                protected boolean canEdit(Object element) {
                    return element instanceof DatabaseItem;
                }

                @Override
                protected CellEditor getCellEditor(Object element) {
                    TextCellEditor cellEditor = new TextCellEditor((Composite) viewer.getControl());
                    return cellEditor;
                }

                @Override
                protected Object getValue(Object element) {
                    String value = ((DatabaseItem) element).name;
                    m_PreviousValue = value;
                    return value;
                }

                @Override
                protected void setValue(Object element, Object value) {
                    if (value instanceof String) {
                        ((DatabaseItem) element).name = (String) value;
                        ViewerHelper.reinsertObject(getViewer(), element);
                        if (((String) value).equals(m_PreviousValue) == false) {
                            handleContentChanged();
                        }
                    }
                    m_PreviousValue = null;
                }
            };
            return e;
        }
    }
}
