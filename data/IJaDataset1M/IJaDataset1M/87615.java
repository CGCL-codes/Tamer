package org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.IAlgorithmParameterInputArea;

public class UnspecifiedIntInputArea implements IAlgorithmParameterInputArea {

    private Text valueText;

    public UnspecifiedIntInputArea(Composite parent, String description) {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        Label descriptionLabel = new Label(parent, SWT.NONE);
        descriptionLabel.setText("Enter " + description + ":");
        descriptionLabel.setLayoutData(gridData);
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.BEGINNING;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.widthHint = 150;
        gridData1.verticalAlignment = GridData.CENTER;
        valueText = new Text(parent, SWT.BORDER);
        valueText.setLayoutData(gridData1);
        valueText.addVerifyListener(new VerifyListener() {

            public void verifyText(VerifyEvent e) {
                if (e.character != SWT.BS && e.character != SWT.DEL) {
                    System.err.println("Entering: " + e.text);
                    try {
                        Integer.valueOf(e.text);
                        if (page != null) {
                            page.setPageComplete(true);
                        }
                    } catch (NumberFormatException exc) {
                        e.doit = false;
                    }
                }
            }
        });
        parent.setLayout(new GridLayout());
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData separatorGridData = new GridData();
        separatorGridData.horizontalAlignment = GridData.FILL;
        separatorGridData.grabExcessHorizontalSpace = true;
        separator.setLayoutData(separatorGridData);
    }

    public Object getValue() {
        if (valueText != null) {
            return Integer.valueOf(valueText.getText()).intValue();
        } else {
            return null;
        }
    }

    public void setValue(Object value) {
    }

    private WizardPage page;

    public void setWizardPage(WizardPage page) {
        this.page = page;
    }
}
