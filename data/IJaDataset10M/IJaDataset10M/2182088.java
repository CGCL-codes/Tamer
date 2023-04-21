package com.jacum.cms.rcp.ui.dialogs;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddHyperLinkDialog extends Dialog {

    private String linkTextString;

    private String targetString;

    private Text linkText;

    private Text targetText;

    public AddHyperLinkDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Shell shell = parent.getShell();
        shell.setText("Hyperlink");
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new GridLayout(2, false));
        Label label = new Label(composite, SWT.READ_ONLY);
        label.setText("Link text : ");
        linkText = new Text(composite, SWT.BORDER);
        linkText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if (linkTextString != null) {
            linkText.setText(linkTextString);
        }
        label = new Label(composite, SWT.NONE);
        label.setText("Link target : ");
        targetText = new Text(composite, SWT.BORDER);
        if (targetString != null) {
            targetText.setText(targetString);
        }
        targetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return composite;
    }

    @Override
    protected void okPressed() {
        linkTextString = linkText.getText();
        targetString = targetText.getText();
        if (linkTextString.trim() != StringUtils.EMPTY && targetString.trim() != StringUtils.EMPTY) {
            super.okPressed();
        }
    }

    public String getLinkTextString() {
        return linkTextString;
    }

    public void setLinkTextString(String linkTextString) {
        this.linkTextString = linkTextString;
    }

    public String getTargetString() {
        return targetString;
    }

    public void setTargetString(String targetString) {
        this.targetString = targetString;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(300, 150);
    }
}
