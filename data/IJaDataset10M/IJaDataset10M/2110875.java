package br.gov.demoiselle.eclipse.main.ui.dialogs;

import java.util.HashSet;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import br.gov.demoiselle.eclipse.main.Activator;

public class ProjectSelectionDialog extends SelectionStatusDialog {

    private HashSet<IProject> fProjectsWithSpecifics;

    private TableViewer fTableViewer;

    private static final int SIZING_SELECTION_WIDGET_HEIGHT = 250;

    private static final int SIZING_SELECTION_WIDGET_WIDTH = 300;

    public ProjectSelectionDialog(Shell parent, HashSet<IProject> projectsWithSpecifics) {
        this(parent);
        this.fProjectsWithSpecifics = projectsWithSpecifics;
    }

    public ProjectSelectionDialog(Shell parent) {
        super(parent);
    }

    protected void computeResult() {
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        Font font = parent.getFont();
        composite.setFont(font);
        createMessageArea(composite);
        fTableViewer = new TableViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                doSelectionChanged(((IStructuredSelection) event.getSelection()).toArray());
            }
        });
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                okPressed();
            }
        });
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
        data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
        fTableViewer.getTable().setLayoutData(data);
        fTableViewer.setLabelProvider(new WorkbenchLabelProvider());
        fTableViewer.setContentProvider(new StructuredContentProvider(fProjectsWithSpecifics));
        fTableViewer.setComparator(new ProjectComparator());
        fTableViewer.getControl().setFont(font);
        fTableViewer.setInput(fProjectsWithSpecifics.iterator().next());
        doSelectionChanged(new Object[0]);
        Dialog.applyDialogFont(composite);
        return composite;
    }

    private void doSelectionChanged(Object[] array) {
        if (array.length != 1) {
            updateStatus(new Status(IStatus.ERROR, Activator.PLUGIN_ID, ""));
            setSelectionResult(null);
        } else {
            updateStatus(new Status(IStatus.OK, Activator.PLUGIN_ID, ""));
            setSelectionResult(array);
        }
    }

    public IProject getFirstResult() {
        return (IProject) super.getFirstResult();
    }
}
