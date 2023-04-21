package de.beas.explicanto.client.rcp.dialogs;

import java.util.List;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import de.beas.explicanto.client.Resources;
import de.beas.explicanto.client.rcp.viewerproviders.OrderListContentProvider;
import de.beas.explicanto.client.rcp.viewerproviders.OrderListLabelProvider;

/**
 * 
 * Dialof for changing the order of lessons or units
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class OrderChangeDialog extends BaseDialog {

    private static final int DLG_WIDTH = 300;

    private static final int DLG_HIGHT = 300;

    private static final int BUTTON_WIDTH = 30;

    private ListViewer listViewer;

    private Button upButton;

    private Button downButton;

    private List elements;

    protected Object selectedElement;

    public OrderChangeDialog(Shell parentShell, List elements) {
        super(parentShell);
        this.elements = elements;
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        ((GridData) composite.getLayoutData()).widthHint = DLG_WIDTH;
        ((GridData) composite.getLayoutData()).heightHint = DLG_HIGHT;
        ((GridLayout) composite.getLayout()).numColumns = 2;
        getShell().setText(translate("dialog.orderChange.title"));
        Label textLine = new Label(composite, SWT.NONE);
        textLine.setText(translate("dialog.orderChange.textLine"));
        GridData gd = new GridData();
        gd.horizontalSpan = 2;
        textLine.setLayoutData(gd);
        listViewer = new ListViewer(composite, SWT.BORDER | SWT.SINGLE);
        listViewer.getList().setLayoutData(new GridData(GridData.FILL_BOTH));
        listViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection sel = (IStructuredSelection) event.getSelection();
                selectedElement = sel.getFirstElement();
                refresh();
            }
        });
        listViewer.setContentProvider(new OrderListContentProvider());
        listViewer.setLabelProvider(new OrderListLabelProvider());
        Composite buttonsComp = new Composite(composite, SWT.NONE);
        GridData buttGd = new GridData();
        buttGd.verticalAlignment = GridData.CENTER;
        buttonsComp.setLayoutData(buttGd);
        buttonsComp.setLayout(new GridLayout());
        ((GridLayout) buttonsComp.getLayout()).verticalSpacing = 30;
        upButton = new Button(buttonsComp, SWT.PUSH);
        upButton.setLayoutData(new GridData(BUTTON_WIDTH, BUTTON_WIDTH));
        upButton.setImage(Resources.getImage("rcp/resources/images/itemUp.png"));
        upButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                upButtonPressed();
            }
        });
        downButton = new Button(buttonsComp, SWT.PUSH);
        downButton.setLayoutData(new GridData(BUTTON_WIDTH, BUTTON_WIDTH));
        downButton.setImage(Resources.getImage("rcp/resources/images/itemDown.png"));
        downButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                downButtonPressed();
            }
        });
        listViewer.setInput(elements);
        select(elements.get(0));
        return composite;
    }

    public void refresh() {
        upButton.setEnabled(true);
        downButton.setEnabled(true);
        if (elements.size() == 1) {
            upButton.setEnabled(false);
            downButton.setEnabled(false);
        } else {
            if (elements.indexOf(selectedElement) == 0) upButton.setEnabled(false);
            if (elements.indexOf(selectedElement) == elements.size() - 1) downButton.setEnabled(false);
        }
    }

    public void select(Object node) {
        listViewer.setSelection(new StructuredSelection(node));
    }

    protected void downButtonPressed() {
        int pos = elements.indexOf(selectedElement);
        elements.remove(pos);
        elements.add(pos + 1, selectedElement);
        refresh();
        listViewer.refresh();
    }

    protected void upButtonPressed() {
        int pos = elements.indexOf(selectedElement);
        elements.remove(pos);
        elements.add(pos - 1, selectedElement);
        refresh();
        listViewer.refresh();
    }
}
