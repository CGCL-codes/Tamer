package com.migniot.streamy.application.view;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import com.migniot.streamy.application.ApplicationPlugin;
import com.migniot.streamy.core.Check;

/**
 * The startup check view.
 */
public class StartupView extends ViewPart implements ITableLabelProvider {

    /**
	 * The view id.
	 */
    public static String VIEW_ID = "com.migniot.streamy.application.StartupView";

    /**
	 * The table viewer.
	 */
    private TableViewer tableViewer;

    /**
	 * The parent composite.
	 */
    private Composite parent;

    /**
	 * Create the table
	 *
	 * @param parent
	 *            the parent control
	 * @see IWorkbenchPart
	 */
    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        this.parent = parent;
        TableViewerColumn column = null;
        this.tableViewer = new TableViewer(parent, SWT.V_SCROLL);
        column = new TableViewerColumn(this.tableViewer, SWT.NONE);
        column.getColumn().setText("Category");
        column.getColumn().setWidth(64);
        column.getColumn().setResizable(false);
        column = new TableViewerColumn(this.tableViewer, SWT.NONE);
        column.getColumn().setText("Message");
        column.getColumn().setWidth(512);
        column.getColumn().setResizable(true);
        column = new TableViewerColumn(this.tableViewer, SWT.NONE);
        column.getColumn().setText("Status");
        column.getColumn().setWidth(64);
        column.getColumn().setResizable(false);
        this.tableViewer.setLabelProvider(this);
        Table table = this.tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    /**
	 * Create the progress monitor for startup tasks.
	 *
	 * @return The progress monitor for startup tasks.
	 */
    public ProgressMonitorPart createMonitor() {
        ProgressMonitorPart part = new ProgressMonitorPart(parent, parent.getLayout());
        part.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        parent.layout();
        return part;
    }

    /**
	 * {@inheritDoc}
	 */
    public void setFocus() {
        this.tableViewer.getTable().setFocus();
    }

    /**
	 * {@inheritDoc}
	 */
    public Image getColumnImage(Object element, int columnIndex) {
        String concept = null;
        if (columnIndex == 0) {
            concept = ((Check) element).getCategory().name();
        } else if (columnIndex == 2) {
            concept = ((Check) element).getStatus().name();
        }
        if (concept != null) {
            return ApplicationPlugin.getImageDescriptor(new StringBuilder("icons/").append(concept.toLowerCase()).append(".png").toString()).createImage();
        }
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    public String getColumnText(Object element, int columnIndex) {
        if (columnIndex == 1) {
            return ((Check) element).getMessage();
        }
        return null;
    }

    /**
	 * Add a check.
	 *
	 * @param check
	 *            The check
	 */
    public void addCheck(Check check) {
        this.tableViewer.add(check);
    }

    /**
	 * {@inheritDoc}
	 */
    public void addListener(ILabelProviderListener listener) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void removeListener(ILabelProviderListener listener) {
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }
}
