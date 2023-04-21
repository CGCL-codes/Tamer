package com.byterefinery.rmbench.dialogs;

import java.util.Properties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The Dialog that shows the current license details in a table
 * 
 * @author cse
 */
public class LicenseDetailsDialog extends Dialog {

    private TableViewer viewer;

    private Properties details;

    public LicenseDetailsDialog(Shell parentShell, Properties details) {
        super(parentShell);
        this.details = details;
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.LicenseDetailsDialog_Title);
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite((Composite) super.createDialogArea(parent), SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        viewer = new TableViewer(composite, SWT.FULL_SELECTION);
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setLabelProvider(new DetailLabelProvider());
        Table table = viewer.getTable();
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = convertHeightInCharsToPixels(7);
        table.setLayoutData(gd);
        table.setHeaderVisible(true);
        TableColumn tc = new TableColumn(table, SWT.NONE);
        tc.setText(Messages.LicenseDetailsDialog_Col_Key);
        tc.setWidth(120);
        tc = new TableColumn(table, SWT.NONE);
        tc.setText(Messages.LicenseDetailsDialog_Col_Value);
        tc.setWidth(120);
        tc = new TableColumn(table, SWT.NONE);
        tc.setWidth(20);
        viewer.setInput(details.keySet());
        return composite;
    }

    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    private class DetailLabelProvider implements ITableLabelProvider {

        public String getColumnText(Object element, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return (String) element;
                case 1:
                    return details.getProperty((String) element);
                default:
                    return null;
            }
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
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
    }
}
