package org.plazmaforge.bsolution.personality.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.plazmaforge.framework.client.PWT;
import org.plazmaforge.framework.client.swt.controls.XCheckBox;
import org.plazmaforge.framework.client.swt.controls.XComboEdit;

/** 
 * @author Oleh Hapon
 * $Id: PersonLanguageEditForm.java,v 1.2 2010/04/28 06:31:02 ohapon Exp $
 */
public class PersonLanguageEditForm extends AbstractPersonableEdit {

    private Label languageLabel;

    private Label easyLabel;

    private XComboEdit languageField;

    private XCheckBox easyField;

    /**
     * Create the form
     * @param parent
     * @param style
     */
    public PersonLanguageEditForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("PersonLanguageEditForm.title"));
        GridData gridData;
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginBottom = 10;
        gridLayout.marginTop = 10;
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        languageLabel = new Label(this, SWT.NONE);
        languageLabel.setText(Messages.getString("PersonLanguageEditForm.languageLabel.text"));
        languageField = new XComboEdit(this, SWT.BORDER, PWT.VIEW_BUTTON);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 350;
        languageField.setLayoutData(gridData);
        easyLabel = new Label(this, SWT.NONE);
        easyLabel.setText(Messages.getString("PersonLanguageEditForm.easyLabel.text"));
        easyField = new XCheckBox(this, SWT.NONE);
        gridData = new GridData();
        easyField.setLayoutData(gridData);
        this.setSize(new Point(470, 108));
    }

    protected void bindControls() {
        bindControl(languageField, "language", languageLabel, REQUIRED);
        bindControl(easyField, "easy", Boolean.TYPE);
    }
}
