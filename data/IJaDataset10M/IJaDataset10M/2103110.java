package net.sf.logsaw.ui.propertyPages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.logsaw.core.field.ALogEntryField;
import net.sf.logsaw.core.field.model.DateLogEntryField;
import net.sf.logsaw.core.logresource.ILogResource;
import net.sf.logsaw.ui.Messages;
import net.sf.logsaw.ui.editors.ILogViewEditor;
import net.sf.logsaw.ui.editors.LogViewEditorColumnConfiguration;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * @author Philipp Nanz
 */
public class ColumnsPropertyPage extends PropertyPage {

    private Button moveUpButton;

    private Button moveDownButton;

    private CheckboxTableViewer tableViewer;

    private WritableList model;

    private LogViewEditorColumnConfiguration config;

    @Override
    protected Control createContents(Composite parent) {
        Composite root = new Composite(parent, SWT.NONE);
        root.setLayout(new GridLayout(2, false));
        tableViewer = CheckboxTableViewer.newCheckList(root, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        tableViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection sel = (IStructuredSelection) event.getSelection();
                boolean moveUp = false;
                boolean moveDown = false;
                if (!sel.isEmpty()) {
                    int idx = model.indexOf(sel.getFirstElement());
                    moveUp = idx > 0;
                    moveDown = idx < model.size() - 1;
                }
                moveUpButton.setEnabled(moveUp);
                moveDownButton.setEnabled(moveDown);
            }
        });
        ILogResource log = (ILogResource) getElement().getAdapter(ILogResource.class);
        model = new WritableList(log.getDialect().getFieldProvider().getAllFields(), ALogEntryField.class);
        ViewerSupport.bind(tableViewer, model, PojoProperties.values(new String[] { "label" }));
        Composite buttonArea = new Composite(root, SWT.NONE);
        buttonArea.setLayout(new GridLayout());
        GridData gridData = new GridData();
        gridData.verticalAlignment = SWT.BEGINNING;
        buttonArea.setLayoutData(gridData);
        Button selectAllButton = new Button(buttonArea, SWT.PUSH);
        selectAllButton.setText(Messages.ColumnsPropertyPage_label_selectAll);
        selectAllButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tableViewer.setAllChecked(true);
            }
        });
        selectAllButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
        Button deselectAllButton = new Button(buttonArea, SWT.PUSH);
        deselectAllButton.setText(Messages.ColumnsPropertyPage_label_deselectAll);
        deselectAllButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tableViewer.setAllChecked(false);
            }
        });
        deselectAllButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
        moveUpButton = new Button(buttonArea, SWT.PUSH);
        moveUpButton.setText(Messages.ColumnsPropertyPage_label_moveUp);
        moveUpButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection sel = (IStructuredSelection) tableViewer.getSelection();
                int idx = model.indexOf(sel.getFirstElement());
                model.move(idx, idx - 1);
            }
        });
        moveUpButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
        moveDownButton = new Button(buttonArea, SWT.PUSH);
        moveDownButton.setText(Messages.ColumnsPropertyPage_label_moveDown);
        moveDownButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection sel = (IStructuredSelection) tableViewer.getSelection();
                int idx = model.indexOf(sel.getFirstElement());
                model.move(idx, idx + 1);
            }
        });
        moveDownButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
        tableViewer.setSelection(StructuredSelection.EMPTY);
        lockTimestampField();
        loadOrderAndCheckState();
        return root;
    }

    @Override
    protected void performDefaults() {
        ILogResource log = (ILogResource) getElement().getAdapter(ILogResource.class);
        List<ALogEntryField<?, ?>> defaultFields = log.getDialect().getFieldProvider().getDefaultFields();
        sort(log, defaultFields);
        super.performDefaults();
    }

    @Override
    public boolean performOk() {
        saveOrderAndCheckState();
        return super.performOk();
    }

    private void sort(ILogResource log, List<ALogEntryField<?, ?>> sortedFields) {
        List<ALogEntryField<?, ?>> allFields = log.getDialect().getFieldProvider().getAllFields();
        for (int i = 0; i < sortedFields.size(); i++) {
            ALogEntryField<?, ?> fld = sortedFields.get(i);
            int idx = model.indexOf(fld);
            model.move(idx, i);
            tableViewer.setChecked(fld, true);
            allFields.remove(fld);
        }
        for (ALogEntryField<?, ?> fld : allFields) {
            tableViewer.setChecked(fld, false);
        }
    }

    private void lockTimestampField() {
        ILogResource log = (ILogResource) getElement().getAdapter(ILogResource.class);
        final DateLogEntryField timestampFld = log.getDialect().getFieldProvider().getTimestampField();
        tableViewer.setGrayed(timestampFld, true);
        tableViewer.setChecked(timestampFld, true);
        tableViewer.getTable().addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if ((event.detail == SWT.CHECK) && (event.item instanceof TableItem) && ((TableItem) event.item).getData().equals(timestampFld)) {
                    ((TableItem) event.item).setChecked(true);
                }
            }
        });
    }

    private void loadOrderAndCheckState() {
        ILogResource log = (ILogResource) getElement().getAdapter(ILogResource.class);
        config = new LogViewEditorColumnConfiguration(log);
        sort(log, config.getFields());
    }

    @SuppressWarnings("unchecked")
    private void saveOrderAndCheckState() {
        List<ALogEntryField<?, ?>> list = new ArrayList<ALogEntryField<?, ?>>(model);
        Iterator<ALogEntryField<?, ?>> it = list.iterator();
        while (it.hasNext()) {
            if (!tableViewer.getChecked(it.next())) {
                it.remove();
            }
        }
        if (config == null) {
            return;
        }
        config.update(list, new int[list.size()]);
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorReference[] editorRefs = page.findEditors((IEditorInput) getElement().getAdapter(IEditorInput.class), null, IWorkbenchPage.MATCH_INPUT);
        for (int i = 0; i < editorRefs.length; i++) {
            IEditorPart editorPart = editorRefs[i].getEditor(false);
            ILogViewEditor editor = editorPart != null ? (ILogViewEditor) editorPart.getAdapter(ILogViewEditor.class) : null;
            if (editor != null) {
                editor.setColumnConfig(config);
            }
        }
    }
}
