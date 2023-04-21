package com.safi.workshop.sqlexplorer.dialogs;

import java.util.Properties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import com.safi.workshop.part.AsteriskDiagramEditorPlugin;
import com.safi.workshop.sqlexplorer.wizard.PropertyEditorTable;

public class PropertyEditorDialog extends Dialog {

    private Properties properties;

    private Properties propCopy;

    /**
   * Create the dialog
   * 
   * @param parentShell
   */
    public PropertyEditorDialog(Shell parentShell) {
        this(parentShell, new Properties());
    }

    public PropertyEditorDialog(Shell parentShell, Properties properties) {
        super(parentShell);
        this.properties = properties;
    }

    /**
   * Create contents of the dialog
   * 
   * @param parent
   */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        propCopy = (Properties) properties.clone();
        final PropertyEditorTable propertyEditorTable = new PropertyEditorTable(container, SWT.NONE, propCopy);
        propertyEditorTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        return container;
    }

    /**
   * Create contents of the button bar
   * 
   * @param parent
   */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
   * Return the initial size of the dialog
   */
    @Override
    protected Point getInitialSize() {
        return new Point(340, 375);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setImage(AsteriskDiagramEditorPlugin.getDefault().getBundledImage("icons/common_tab.gif"));
        newShell.setText("Connection Properties Editor");
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected void okPressed() {
        super.okPressed();
        properties.clear();
        properties.putAll(propCopy);
    }
}
