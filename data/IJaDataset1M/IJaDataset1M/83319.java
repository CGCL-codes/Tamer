package de.mpiwg.vspace.modules.editor.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;
import de.mpiwg.vspace.modules.editor.tables.SlideTableLabelProvider;

public class TwoListsSelectionDialog extends SelectionDialog {

    TableViewer elementTable = null;

    TableViewer selectionTable = null;

    List<EObject> elements = new ArrayList<EObject>();

    List<EObject> selectedElements = new ArrayList<EObject>();

    Button addButton = null;

    Button removeButton = null;

    Button upButton = null;

    Button downButton = null;

    public TwoListsSelectionDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected void configureShell(Shell shell) {
        shell.setSize(870, 400);
        super.configureShell(shell);
    }

    @Override
    protected Composite createDialogArea(Composite composite) {
        Composite area = (Composite) super.createDialogArea(composite);
        Label label = new Label(area, SWT.NONE);
        label.setText("Select slides");
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Composite compo = new Composite(area, SWT.NONE);
        compo.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout gridlayout = new GridLayout();
        gridlayout.numColumns = 3;
        compo.setLayout(gridlayout);
        elementTable = new TableViewer(compo, SWT.MULTI);
        GridData table1Data = new GridData(GridData.FILL_BOTH);
        table1Data.widthHint = 370;
        elementTable.getTable().setLayoutData(table1Data);
        elementTable.setLabelProvider(new SlideTableLabelProvider());
        elementTable.add(elements.toArray());
        elementTable.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent e) {
                addItemToSelectionTable();
            }
        });
        Composite buttonCompo = new Composite(compo, SWT.NONE);
        GridLayout buttonLayout = new GridLayout();
        buttonCompo.setLayout(buttonLayout);
        createButtons(buttonCompo);
        selectionTable = new TableViewer(compo, SWT.MULTI);
        GridData table2Data = new GridData(GridData.FILL_BOTH);
        table2Data.widthHint = 370;
        selectionTable.getTable().setLayoutData(table2Data);
        selectionTable.setLabelProvider(new SlideTableLabelProvider());
        selectionTable.add(selectedElements.toArray());
        selectionTable.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent e) {
                removeItemFromSelectionTable();
            }
        });
        return area;
    }

    private void createButtons(Composite composite) {
        addButton = new Button(composite, SWT.NONE);
        addButton.setText("Add ->");
        addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        addButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                addItemToSelectionTable();
            }
        });
        removeButton = new Button(composite, SWT.NONE);
        removeButton.setText("<- Remove");
        removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        removeButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                removeItemFromSelectionTable();
            }
        });
        Label spacing = new Label(composite, SWT.NONE);
        spacing.setLayoutData(new GridData());
        upButton = new Button(composite, SWT.NONE);
        upButton.setText("up");
        upButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        upButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ISelection selection = selectionTable.getSelection();
                if (selection instanceof StructuredSelection) {
                    if ((((StructuredSelection) selection) != null) && ((StructuredSelection) selection).size() > 0) {
                        Object next = ((StructuredSelection) selection).getFirstElement();
                        int index = 0;
                        for (index = 0; index < selectionTable.getTable().getItemCount(); index++) {
                            Object o = selectionTable.getElementAt(index);
                            if ((o == next) && selectionTable.getTable().isSelected(index)) break;
                        }
                        if (index > 0) {
                            selectionTable.remove(next);
                            selectionTable.insert(next, index - 1);
                            selectionTable.getTable().deselectAll();
                            selectionTable.getTable().select(index - 1);
                        }
                    }
                }
            }
        });
        downButton = new Button(composite, SWT.NONE);
        downButton.setText("down");
        downButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        downButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ISelection selection = selectionTable.getSelection();
                if (selection instanceof StructuredSelection) {
                    if ((((StructuredSelection) selection) != null) && ((StructuredSelection) selection).size() > 0) {
                        Object next = ((StructuredSelection) selection).getFirstElement();
                        int index = 0;
                        for (index = 0; index < selectionTable.getTable().getItemCount(); index++) {
                            Object o = selectionTable.getElementAt(index);
                            if ((o == next) && selectionTable.getTable().isSelected(index)) break;
                        }
                        if (index < selectionTable.getTable().getItemCount() - 1) {
                            selectionTable.remove(next);
                            selectionTable.insert(next, index + 1);
                            selectionTable.getTable().deselectAll();
                            selectionTable.getTable().select(index + 1);
                        }
                    }
                }
            }
        });
    }

    /**
	 * Method for setting the list with the selectable items and the list
	 * with the already selected items.
	 * @param elements Elements that are selectable.
	 * @param selectedElements Elements that are selected (a subset of elements)
	 */
    public void setLists(EList<?> elements, List<?> selectedElements) {
        if (elements == null) return;
        for (Object elem : elements) {
            if (elem instanceof EObject) {
                this.elements.add((EObject) elem);
            }
        }
        if (selectedElements == null) return;
        for (Object elem : selectedElements) {
            if (elem instanceof EObject) {
                this.selectedElements.add((EObject) elem);
                this.elements.remove((EObject) elem);
            }
        }
    }

    private void addItemToSelectionTable() {
        ISelection selection = elementTable.getSelection();
        if (selection instanceof StructuredSelection) {
            Iterator it = ((StructuredSelection) selection).iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                selectionTable.add(obj);
                elementTable.remove(obj);
            }
        }
    }

    private void removeItemFromSelectionTable() {
        ISelection selection = selectionTable.getSelection();
        if (selection instanceof StructuredSelection) {
            Iterator it = ((StructuredSelection) selection).iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                selectionTable.remove(obj);
                elementTable.add(obj);
            }
        }
    }

    @Override
    protected void okPressed() {
        List result = new ArrayList();
        for (int i = 0; i < selectionTable.getTable().getItemCount(); i++) {
            result.add(selectionTable.getElementAt(i));
        }
        setResult(result);
        super.okPressed();
    }
}
