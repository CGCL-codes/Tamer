package com.tensegrity.palobrowser.editors.dimensioneditor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.progress.IProgressService;
import org.palo.api.Attribute;
import org.palo.api.Dimension;
import org.palo.api.Element;
import com.tensegrity.palobrowser.dialogs.AttributeFieldRenameDialog;
import com.tensegrity.palobrowser.dialogs.DeleteConfirmDialog;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.AliasEvent;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.AliasListener;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.AliasManager;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.AttributeFieldWrapper;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.IAttributeCubeWrapper;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.IAttributeDataWrapper;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.IAttributeFieldWrapper;
import com.tensegrity.palobrowser.managedlist.ManagedList;
import com.tensegrity.palobrowser.managedlist.ManagedListContentProvider;
import com.tensegrity.palobrowser.managedlist.ManagedListEvent;
import com.tensegrity.palobrowser.managedlist.ManagedListListener;
import com.tensegrity.palobrowser.util.Assertion;
import com.tensegrity.palobrowser.wizards.addattr.AddAttributeFieldWizard;
import com.tensegrity.palobrowser.wizards.addattr.AddAttributeFieldWizardContext;

/**
 * {@<describe>}
 * <p>
 * This class implements the DimensionEditors Attribute Tab functionality.
 * </p>
 * {@</describe>}
 * 
 * @author AndreasEbbert
 * @version $Id: DimensionEditorAttributeTab.java,v 1.1 2006/11/27 18:23:51
 *          AndreasEbbert Exp $
 */
class DimensionEditorAttributeTab implements ITableViewerEditorTab, SelectionListener, ManagedListListener {

    private static final boolean USE_TABLE_CURSOR = true;

    private boolean dirty;

    private final DimensionEditor editor;

    private final Dimension dimension;

    final IAttributeCubeWrapper attrCubeWrapper;

    private Button addAttrDimensionButton;

    private Button removeAttrDimensionButton;

    private Button renameAttrFieldButton;

    private Button move2frontButton;

    private Table table;

    private TableViewer attrViewer;

    private final CTabItem attrTabItem;

    private AttributeTabAttributeCubeListener attrCubeListener = new AttributeTabAttributeCubeListener();

    private int selCol = -1;

    private CustomTableCursorState cState;

    private CustomTableCursor cursor;

    DimensionEditorAttributeTab(DimensionEditor dEditor, FormToolkit toolkit, final CTabFolder tabfolder) {
        this.editor = dEditor;
        this.dimension = ((DimensionEditorInput) editor.getEditorInput()).getDimension();
        Assertion.assertNotNull(dimension);
        this.attrCubeWrapper = AliasManager.getInstance().getAttributeCubeWrapper(dimension);
        attrTabItem = new CTabItem(tabfolder, SWT.NONE);
        attrTabItem.setText(DimensionEditorMessages.getString("DimensionEditor.Atributes"));
        Composite c = toolkit.createComposite(tabfolder, SWT.NONE);
        toolkit.paintBordersFor(c);
        c.setLayout(new GridLayout(2, false));
        createAttributeContent(toolkit, c);
        attrTabItem.setControl(c);
        attrViewer.getTable().addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                switch(e.keyCode) {
                    case SWT.F2:
                        startEditSelectedItem(2);
                        e.doit = false;
                        break;
                    case 's':
                        if (e.stateMask == SWT.CONTROL && editor.isDirty()) {
                            editor.getEditorSite().getPage().saveEditor(editor, true);
                        }
                        e.doit = false;
                        break;
                }
            }
        });
    }

    void save() {
        storeCursorState();
        if (!dirty) return;
        IWorkbench wb = PlatformUI.getWorkbench();
        IProgressService ps = wb.getProgressService();
        try {
            ps.busyCursorWhile(new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    AliasManager.getInstance().saveAttributeCube(attrCubeWrapper, monitor);
                }
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dirty = false;
    }

    private void storeCursorState() {
        if (!USE_TABLE_CURSOR) return;
        cState = new CustomTableCursorState(attrViewer, cursor);
    }

    private void restoreCursorState() {
        if (!USE_TABLE_CURSOR || cState == null) return;
        cursor.setVisible(true);
        cursor.setSelection(cState.getRow(), cState.getCol());
    }

    public CTabItem getCTabItem() {
        return attrTabItem;
    }

    public TableViewer getTableViewer() {
        return attrViewer;
    }

    public void processInput() {
        ManagedList elementList = new ManagedList();
        table.removeAll();
        TableColumn[] tCol = table.getColumns();
        final int[] storedColOrder = table.getColumnOrder();
        for (int i = 2; i < tCol.length; i++) {
            tCol[i].dispose();
        }
        final List elCols = attrCubeWrapper.getAttrFields();
        List propList = new LinkedList();
        propList.add(getColPropertyName(0));
        propList.add(getColPropertyName(1));
        List cEditorsList = new LinkedList();
        cEditorsList.add(new TextCellEditor(attrViewer.getTable()));
        cEditorsList.add(new TextCellEditor(attrViewer.getTable()));
        for (int i = 0; i < elCols.size(); i++) {
            IAttributeFieldWrapper actWrapper = (IAttributeFieldWrapper) elCols.get(i);
            TableColumn column = new TableColumn(table, SWT.LEFT);
            column.setMoveable(true);
            column.setResizable(true);
            column.setText(actWrapper.getName());
            column.setWidth(160);
            propList.add(getColPropertyName(i + 2));
            TextCellEditor textEditor = new TextCellEditor(attrViewer.getTable());
            if (USE_TABLE_CURSOR) textEditor.addListener(cursor);
            cEditorsList.add(textEditor);
            elementList.add(actWrapper);
        }
        attrViewer.setColumnProperties((String[]) propList.toArray(new String[propList.size()]));
        attrViewer.setCellEditors((CellEditor[]) cEditorsList.toArray(new CellEditor[cEditorsList.size()]));
        if (table.getColumnCount() > 2 && table.getColumnCount() == storedColOrder.length) table.setColumnOrder(storedColOrder);
        attrViewer.setInput(elementList);
        elementList.addListener(this);
        restoreCursorState();
    }

    public int getElementIndexOfObject(Object obj) {
        return -1;
    }

    public Object getElementAtIndex(int index) {
        return attrViewer.getElementAt(index);
    }

    public void dispose() {
        AliasManager.getInstance().removeAttributeCubeListener(attrCubeListener);
        if (cursor != null) cursor.dispose();
        attrViewer.getTable().dispose();
    }

    private void createAttributeContent(FormToolkit toolkit, Composite content) {
        table = toolkit.createTable(content, SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.MULTI);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        {
            TableColumn column = new TableColumn(table, SWT.LEFT, 0);
            column.setText(DimensionEditorMessages.getString("DimensionEditor.ColumnPos"));
            column.setWidth(DimensionEditorElementTab.FIRST_COL_WIDTH);
        }
        {
            TableColumn column = new TableColumn(table, SWT.LEFT, 1);
            column.setText(DimensionEditorMessages.getString("DimensionEditor.ColumnElName"));
            column.setWidth(140);
        }
        Composite buttonPanel = toolkit.createComposite(content);
        buttonPanel.setLayout(new GridLayout(1, false));
        buttonPanel.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        addAttrDimensionButton = toolkit.createButton(buttonPanel, DimensionEditorMessages.getString("DimensionEditor.AddCaption"), SWT.PUSH);
        addAttrDimensionButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        addAttrDimensionButton.addSelectionListener(this);
        addAttrDimensionButton.setEnabled(true);
        removeAttrDimensionButton = toolkit.createButton(buttonPanel, DimensionEditorMessages.getString("DimensionEditor.RemoveCaption"), SWT.PUSH);
        removeAttrDimensionButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        removeAttrDimensionButton.addSelectionListener(this);
        removeAttrDimensionButton.setEnabled(!true);
        renameAttrFieldButton = toolkit.createButton(buttonPanel, DimensionEditorMessages.getString("DimensionEditorAttr.RenDim"), SWT.PUSH);
        renameAttrFieldButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        renameAttrFieldButton.addSelectionListener(this);
        renameAttrFieldButton.setEnabled(false);
        move2frontButton = toolkit.createButton(buttonPanel, DimensionEditorMessages.getString("DimensionEditorAttr.MoveToFront"), SWT.PUSH);
        move2frontButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        move2frontButton.addSelectionListener(this);
        move2frontButton.setEnabled(false);
        attrViewer = new TableViewer(table);
        attrViewer.setLabelProvider(new AttributeCubeWrapperLabelProvider(attrViewer, dimension));
        attrViewer.setContentProvider(new AttributeCubeContentProvider(editor, attrViewer));
        attrViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                rehashButtonsEnableState();
            }
        });
        setupTableEditing();
        AliasManager.getInstance().addAttributeCubeListener(attrCubeListener);
        rehashButtonsEnableState();
    }

    private void rehashButtonsEnableState() {
        removeAttrDimensionButton.setEnabled(attrCubeWrapper != null && attrCubeWrapper.getAttrFields().size() > 0);
        move2frontButton.setEnabled(attrCubeWrapper != null && attrCubeWrapper.getAttrFields().size() > 1);
        renameAttrFieldButton.setEnabled(!dirty);
    }

    private void doAttributeFieldAdd() {
        ManagedList inputList = ((ManagedListContentProvider) attrViewer.getContentProvider()).getManagedList();
        final String[] existingAttrNames = getExistingAttrNames();
        final AddAttributeFieldWizard wizard = new AddAttributeFieldWizard(existingAttrNames, editor.getOutputQueue());
        wizard.init(editor.getEditorSite().getWorkbenchWindow().getWorkbench(), new StructuredSelection());
        WizardDialog dialog = new WizardDialog(editor.getEditorSite().getShell(), wizard);
        dialog.create();
        if (Window.OK == dialog.open()) {
            editor.setDirty(true);
            dirty = true;
            final String name = wizard.getContext().getName();
            TableColumn column = new TableColumn(table, SWT.LEFT);
            column.setText(name);
            column.setWidth(160);
            List propList = new LinkedList(Arrays.asList(attrViewer.getColumnProperties()));
            propList.add(getColPropertyName(propList.size()));
            attrViewer.setColumnProperties((String[]) propList.toArray(new String[propList.size()]));
            List cEditorsList = new LinkedList(Arrays.asList(attrViewer.getCellEditors()));
            TextCellEditor textEditor = new TextCellEditor(attrViewer.getTable());
            if (USE_TABLE_CURSOR) textEditor.addListener(cursor);
            cEditorsList.add(textEditor);
            attrViewer.setCellEditors((CellEditor[]) cEditorsList.toArray(new CellEditor[cEditorsList.size()]));
            wrapper = null;
            final IWorkbench wb = PlatformUI.getWorkbench();
            final IProgressService ps = wb.getProgressService();
            try {
                if (!wizard.getContext().isInitWithOriginalValues()) {
                    ps.busyCursorWhile(new IRunnableWithProgress() {

                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            wrapper = AliasManager.getInstance().addAttributeField(attrCubeWrapper, name, false, monitor);
                        }
                    });
                } else if (wizard.getContext().isInitWithOriginalValues()) {
                    if (wizard.getContext().getInitMode() == AddAttributeFieldWizardContext.INIT_EL_NAMES) {
                        ps.busyCursorWhile(new IRunnableWithProgress() {

                            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                                wrapper = AliasManager.getInstance().addAttributeField(attrCubeWrapper, name, true, monitor);
                            }
                        });
                    } else {
                        ps.busyCursorWhile(new IRunnableWithProgress() {

                            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                                wrapper = AliasManager.getInstance().addAttributeField(attrCubeWrapper, name, wizard.getContext().getInitAliasName(), monitor);
                            }
                        });
                    }
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inputList.add(wrapper);
            attrViewer.refresh();
            rehashButtonsEnableState();
        }
    }

    private IAttributeFieldWrapper wrapper;

    private String[] getExistingAttrNames() {
        List ret = new ArrayList();
        Dimension refDim = attrCubeWrapper.getRefDimension();
        Attribute[] rootAttr = refDim.getAttributes();
        Attribute aliasAttr = null;
        for (int i = 0; rootAttr != null && i < rootAttr.length; i++) {
            if (rootAttr[i].getName().equalsIgnoreCase("Alias")) {
                aliasAttr = rootAttr[i];
                break;
            }
        }
        if (aliasAttr != null) {
            for (Attribute a : aliasAttr.getChildren()) {
                if (a.getType() == Attribute.TYPE_STRING) {
                    ret.add(a.getName().trim());
                }
            }
        }
        return (String[]) ret.toArray(new String[0]);
    }

    private void doAttributeFieldRemove() {
        List attrCols = attrCubeWrapper.getAttrFields();
        final List attrFieldList = new ArrayList();
        for (int i = 0; i < attrCols.size(); i++) {
            attrFieldList.add(((IAttributeFieldWrapper) attrCols.get(i)).getName());
        }
        Collections.sort(attrFieldList);
        DeleteConfirmDialog dialog = new DeleteConfirmDialog(editor.getEditorSite().getShell(), attrFieldList, DimensionEditorMessages.getString("DimensionEditorAttr.RemDim"), DimensionEditorMessages.getString("DimensionEditorAttr.RemDim.desc"), DimensionEditorMessages.getString("DimensionEditorAttr.RemDim.label"));
        dialog.create();
        if (Window.OK != dialog.open()) return;
        final Object[] itemsPre = dialog.getCheckedItems();
        final List itemsPreList = Arrays.asList(itemsPre);
        final TableColumn[] cols = table.getColumns();
        for (int i = 2; i < cols.length; i++) {
            if (itemsPreList.contains(cols[i].getText())) {
                cols[i].dispose();
            }
        }
        for (int i = 0; i < itemsPre.length; i++) {
            final IAttributeFieldWrapper adwDel = attrCubeWrapper.getAttrField(itemsPre[i].toString());
            ManagedList inputList = ((ManagedListContentProvider) attrViewer.getContentProvider()).getManagedList();
            inputList.remove(adwDel);
            attrViewer.refresh();
            AliasManager.getInstance().removeAttributeField(attrCubeWrapper, adwDel);
        }
        editor.setDirty(true);
        dirty = true;
        rehashButtonsEnableState();
    }

    private void setupTableEditing() {
        attrViewer.setColumnProperties(new String[] { getColPropertyName(0) });
        final CellEditor cellEditors[] = new CellEditor[] { new TextCellEditor(attrViewer.getTable()) };
        attrViewer.setCellEditors(cellEditors);
        attrViewer.setCellModifier(new CellModifier());
        if (USE_TABLE_CURSOR) {
            CustomTableCursorKeyHandler crListener = new CustomTableCursorKeyHandler(SWT.CR) {

                protected void handle() {
                    TableItem row = cursor.getRow();
                    int rowIndex = attrViewer.getTable().indexOf(row);
                    int colIndex = cursor.getColumn();
                    if (rowIndex < attrViewer.getTable().getItems().length - 1) {
                        cursor.setRowColumn(Math.max(0, rowIndex + 1), colIndex, true);
                    }
                }
            };
            cursor = new CustomTableCursor(attrViewer, new int[] { 0, 1 }, new CustomTableCursorKeyHandler[] { crListener });
        }
        table.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                Rectangle clientArea = table.getClientArea();
                Point pt = new Point(event.x, event.y);
                int index = table.getTopIndex();
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    TableItem item = table.getItem(index);
                    for (int i = 0; i < table.getColumns().length; i++) {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(pt)) {
                            selCol = i;
                            rehashButtonsEnableState();
                        }
                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }
                    if (!visible) return;
                    index++;
                }
            }
        });
    }

    private void doAttributeFieldRename() {
        if (selCol < 2) return;
        final String oldFieldName = attrViewer.getTable().getColumn(selCol).getText();
        String[] exAttrNames = getExistingAttrNames();
        AttributeFieldRenameDialog dlg = new AttributeFieldRenameDialog(editor.getEditorSite().getShell(), oldFieldName, exAttrNames);
        final int ret = dlg.open();
        if (ret == Window.CANCEL) return;
        final String newName = dlg.getRenamedFieldName();
        AliasManager.getInstance().renameAttributeField(attrCubeWrapper, oldFieldName, newName);
        attrViewer.getTable().getColumn(selCol).setText(newName);
        editor.setDirty(true);
        dirty = true;
        rehashButtonsEnableState();
    }

    public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == addAttrDimensionButton) {
            doAttributeFieldAdd();
        } else if (e.getSource() == removeAttrDimensionButton) {
            doAttributeFieldRemove();
        } else if (e.getSource() == renameAttrFieldButton) {
            doAttributeFieldRename();
        } else if (e.getSource() == move2frontButton) {
            doMoveColumnsToFront();
        }
    }

    private void doMoveColumnsToFront() {
        List attrCols = attrCubeWrapper.getAttrFields();
        final List attrFieldList = new ArrayList();
        for (int i = 0; i < attrCols.size(); i++) {
            attrFieldList.add(((IAttributeFieldWrapper) attrCols.get(i)).getName());
        }
        Collections.sort(attrFieldList);
        DeleteConfirmDialog dialog = new DeleteConfirmDialog(editor.getEditorSite().getShell(), attrFieldList, DimensionEditorMessages.getString("DimensionEditorAttr.MoveToFront"), DimensionEditorMessages.getString("DimensionEditorAttr.MoveToFront.desc"), DimensionEditorMessages.getString("DimensionEditorAttr.MoveToFront.label"));
        dialog.create();
        if (Window.OK != dialog.open()) return;
        final List itemsList = Arrays.asList(dialog.getCheckedItems());
        int[] order = attrViewer.getTable().getColumnOrder();
        for (Iterator iter = itemsList.iterator(); iter.hasNext(); ) {
            final int actCol = colname2col((String) iter.next());
            Assertion.assertTrue(actCol != -1);
            order = moveColumnToFront(order, actCol);
        }
        attrViewer.getTable().setColumnOrder(order);
    }

    private int[] moveColumnToFront(int[] order, int col) {
        for (int i = col2vis(col); i > 2; i--) {
            order[i] = order[i - 1];
        }
        order[2] = col;
        return order;
    }

    private int col2vis(int col) {
        final int[] order = attrViewer.getTable().getColumnOrder();
        for (int i = 0; i < order.length; i++) {
            if (order[i] == col) return i;
        }
        return col;
    }

    private int colname2col(String name) {
        for (int i = 0; i < attrViewer.getTable().getColumnCount(); i++) {
            if (attrViewer.getTable().getColumn(i).getText().equals(name)) return i;
        }
        return -1;
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }

    private void startEditSelectedItem(int column) {
        ISelection selection = attrViewer.getSelection();
        if (!(selection instanceof IStructuredSelection)) return;
        IStructuredSelection sselection = (IStructuredSelection) selection;
        if (sselection.size() < 1) return;
        attrViewer.editElement(sselection.getFirstElement(), column);
    }

    public void listChanged(ManagedListEvent event) {
        switch(event.getType()) {
            case ManagedListEvent.ITEMS_ADDED:
            case ManagedListEvent.ITEMS_REMOVED:
            case ManagedListEvent.ITEMS_CHANGED:
        }
    }

    private String getColPropertyName(int col) {
        return "c" + col;
    }

    private int getColFromPropertyName(String propName) {
        return Integer.parseInt(propName.substring(1));
    }

    private class CellModifier implements ICellModifier {

        public boolean canModify(Object element, String property) {
            return property != null && (!property.equals("c0")) && (!property.equals("c1"));
        }

        public Object getValue(Object element, String property) {
            final IAttributeDataWrapper[] els = (IAttributeDataWrapper[]) element;
            final int col = getColFromPropertyName(property) - 2;
            final String elName = els[col].getName();
            return elName;
        }

        public void modify(Object element, String property, Object value) {
            if (element instanceof Item) {
                element = ((Item) element).getData();
            }
            if (value == null) value = "";
            if (!(element instanceof IAttributeDataWrapper[])) return;
            final IAttributeDataWrapper[] els = (IAttributeDataWrapper[]) element;
            final IAttributeDataWrapper adew = (IAttributeDataWrapper) els[getColFromPropertyName(property) - 2];
            final String newName = value.toString();
            AliasManager.getInstance().renameAttributeFieldElement(adew, newName);
            if (adew.isDirty()) {
                editor.setDirty(true);
                dirty = true;
                attrViewer.refresh();
            }
        }
    }

    void addDataWrapperForElementToInputList(Element el) {
        ManagedList inputList = (ManagedList) attrViewer.getInput();
        for (Iterator iter = inputList.iterator(); iter.hasNext(); ) {
            AttributeFieldWrapper col = (AttributeFieldWrapper) iter.next();
            IAttributeDataWrapper adw = col.addDataWrapper(el);
        }
        if (!attrViewer.getTable().isDisposed()) attrViewer.refresh();
    }

    void removeDataWrapperForElementFromInputList(Element el) {
        ManagedList inputList = (ManagedList) attrViewer.getInput();
        for (Iterator iter = inputList.iterator(); iter.hasNext(); ) {
            AttributeFieldWrapper col = (AttributeFieldWrapper) iter.next();
            col.removeDataWrapper(el);
        }
        attrViewer.refresh();
    }

    private class AttributeTabAttributeCubeListener implements AliasListener {

        public void attrCubeChanged(AliasEvent e) {
        }

        public void elementAdded(AliasEvent e) {
            final Element element = e.getElement();
            if (!element.getDimension().equals(attrCubeWrapper.getRefDimension())) return;
            addDataWrapperForElementToInputList(element);
            AttributeCubeWrapperLabelProvider lProv = (AttributeCubeWrapperLabelProvider) attrViewer.getLabelProvider();
            lProv.addToIndexMap(element);
        }

        public void elementRemoved(AliasEvent e) {
            final Element element = e.getElement();
            final Dimension dim = element.getDimension();
            if (!dim.equals(attrCubeWrapper.getRefDimension())) return;
            removeDataWrapperForElementFromInputList(element);
        }
    }
}
