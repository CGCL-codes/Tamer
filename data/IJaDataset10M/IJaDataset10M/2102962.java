package com.open_squad.openplan.views;

import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;
import org.hibernate.Session;
import com.open_squad.openplan.model.Lot;
import com.open_squad.openplan.utils.HibernateUtil;
import com.open_squad.openplan.utils.LotManager;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

/**
 * @author Administrator
 *
 */
public class PlanningView extends ViewPart {

    public static final String ID = "com.open_squad.openplan.views.PlanningView";

    private FormToolkit toolkit;

    private ScrolledForm form;

    private Table lotTable = null;

    private TableViewer lotTableViewer = null;

    private Button btnGroup = null;

    private Button btnSplit = null;

    private Button btnAssign = null;

    /**
	 * 
	 */
    public PlanningView() {
    }

    @Override
    public void createPartControl(final Composite parent) {
        final ViewPart part = this;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 3;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        form.setText("Lot management");
        lotTable = new Table(form.getBody(), SWT.FULL_SELECTION | SWT.MULTI);
        lotTable.setHeaderVisible(true);
        lotTable.setLayoutData(gridData);
        lotTable.setLinesVisible(true);
        btnGroup = toolkit.createButton(form.getBody(), "Group", SWT.PUSH);
        btnGroup.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {

            public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
                LotManager manager = new LotManager(parent.getShell());
                manager.onGroup(lotTableViewer.getSelection());
                lotTableViewer.setInput(getLotTableInput());
                lotTableViewer.refresh();
            }
        });
        btnSplit = toolkit.createButton(form.getBody(), "Split", SWT.PUSH);
        btnSplit.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {

            public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
                LotManager manager = new LotManager(parent.getShell());
                manager.onSplit(lotTableViewer.getSelection());
                lotTableViewer.setInput(getLotTableInput());
                lotTableViewer.refresh();
            }
        });
        btnAssign = toolkit.createButton(form.getBody(), "Assign", SWT.PUSH);
        btnAssign.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {

            public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
                LotManager manager = new LotManager(parent.getShell());
                manager.onAssign(lotTableViewer.getSelection());
                lotTableViewer.setInput(getLotTableInput());
                lotTableViewer.refresh();
            }
        });
        String[] columnNames = new String[] { "Client", "Order", "Material", "Color", "Quantity" };
        int[] columnWidths = new int[] { 100, 100, 100, 100, 100 };
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn tableColumn = new TableColumn(lotTable, SWT.None);
            tableColumn.setText(columnNames[i]);
            tableColumn.setWidth(columnWidths[i]);
        }
        lotTableViewer = new TableViewer(lotTable);
        lotTableViewer.setContentProvider(new IStructuredContentProvider() {

            public Object[] getElements(Object inputElement) {
                return ((List) inputElement).toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
        lotTableViewer.setLabelProvider(new LotTableLabelProvider());
        lotTableViewer.setInput(getLotTableInput());
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.makeColumnsEqualWidth = true;
        form.getBody().setLayout(layout);
    }

    private List getLotTableInput() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List orderDetails = session.createQuery("from Lot where id_machine is null").list();
        return orderDetails;
    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

    @Override
    public void dispose() {
        toolkit.dispose();
        super.dispose();
    }
}
