package org.plazmaforge.bsolution.base.client.swt.dialogs;

import java.util.List;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/** 
 * @author Oleh Hapon
 * $Id: PropertyEditDialog.java,v 1.3 2010/07/06 14:36:25 ohapon Exp $
 */
public class PropertyEditDialog extends Dialog {

    private Text propertyField;

    private Text valueField;

    private PropertyElement element;

    private List<PropertyElement> data;

    private String mode;

    public PropertyEditDialog(Shell parentShell, String mode, PropertyElement element) {
        super(parentShell);
        if (element == null) {
            throw new IllegalArgumentException("PropertyElement is null");
        }
        if (element.getName() == null) {
            throw new IllegalArgumentException("Name of PropertyElement is null");
        }
        if (element.getValue() == null) {
            throw new IllegalArgumentException("Value of PropertyElement is null");
        }
        this.mode = mode;
        this.element = element;
    }

    public List<PropertyElement> getData() {
        return data;
    }

    public void setData(List<PropertyElement> data) {
        this.data = data;
    }

    protected Control createDialogArea(Composite parent) {
        Composite parentComposite = (Composite) super.createDialogArea(parent);
        Composite composite = new Composite(parentComposite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        GridData gridData = null;
        Label nameLabel = new Label(composite, SWT.NONE);
        nameLabel.setText(Messages.getString("ConfigPanel.properties.property"));
        propertyField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 300;
        propertyField.setLayoutData(gridData);
        Label valueLabel = new Label(composite, SWT.NONE);
        valueLabel.setText(Messages.getString("ConfigPanel.properties.value"));
        valueField = new Text(composite, SWT.BORDER);
        valueField.setLayoutData(gridData);
        updateControls();
        return parentComposite;
    }

    protected void updateControls() {
        propertyField.setText(element.getName());
        valueField.setText(element.getValue());
    }

    protected void updateData() {
        element.setName(propertyField.getText());
        element.setValue(valueField.getText());
    }

    protected void okPressed() {
        if (!isValid()) {
            return;
        }
        updateData();
        if ("ADD".equals(mode)) {
            getData().add(element);
        }
        super.okPressed();
    }

    protected boolean isValid() {
        StringBuffer buf = null;
        String text = propertyField.getText();
        if (text.trim().length() == 0) {
            if (buf == null) {
                buf = new StringBuffer();
            }
            buf.append("Property name is empty.");
        }
        text = valueField.getText();
        if (text.trim().length() == 0) {
            if (buf == null) {
                buf = new StringBuffer();
            } else {
                buf.append("\n");
            }
            buf.append("Value is empty.");
        }
        String error = null;
        if (buf != null) {
            error = buf.toString().trim();
        }
        if (error == null || error.length() == 0) {
            return true;
        }
        MessageDialog.openError(getShell(), "Error", error);
        return false;
    }
}
