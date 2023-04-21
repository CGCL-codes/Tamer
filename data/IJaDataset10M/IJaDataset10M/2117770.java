package org.plazmaforge.bsolution.contact.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.plazmaforge.framework.client.PWT;
import org.plazmaforge.framework.client.swt.controls.XComboEdit;
import org.plazmaforge.framework.client.swt.controls.XTextField;
import org.plazmaforge.framework.client.swt.forms.AbstractEditForm;

/** 
 * @author Oleh Hapon
 * $Id: LocalityEditForm.java,v 1.2 2010/04/28 06:31:05 ohapon Exp $
 */
public class LocalityEditForm extends AbstractEditForm {

    private Label countryLabel;

    private Label regionLabel;

    private Label nameLabel;

    private XComboEdit countryField;

    private XComboEdit regionField;

    private XTextField nameField;

    private XComboEdit localityTypeField;

    /**
     * Create the form
     * @param parent
     * @param style
     */
    public LocalityEditForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("LocalityEditForm.title"));
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginBottom = 10;
        gridLayout.marginTop = 10;
        gridLayout.numColumns = 3;
        setLayout(gridLayout);
        countryLabel = new Label(this, SWT.NONE);
        countryLabel.setText(Messages.getString("LocalityEditForm.countryLabel.text"));
        countryField = new XComboEdit(this, SWT.BORDER, PWT.POPUP_BUTTON | PWT.VIEW_BUTTON | PWT.EDIT_BUTTON);
        countryField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        regionLabel = new Label(this, SWT.NONE);
        regionLabel.setText(Messages.getString("LocalityEditForm.regionLabel.text"));
        regionField = new XComboEdit(this, SWT.BORDER, PWT.POPUP_BUTTON | PWT.VIEW_BUTTON | PWT.EDIT_BUTTON | PWT.DELETE_BUTTON);
        regionField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        nameLabel = new Label(this, SWT.NONE);
        nameLabel.setText(Messages.getString("LocalityEditForm.nameLabel.text"));
        localityTypeField = new XComboEdit(this, SWT.BORDER);
        localityTypeField.setLayoutData(new GridData());
        GridData gridData;
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 300;
        nameField = new XTextField(this, SWT.BORDER);
        nameField.setLayoutData(gridData);
        nameField.setTextLimit(50);
        this.setSize(new Point(574, 298));
    }

    protected void bindControls() {
        bindControl(countryField, "country", countryLabel, REQUIRED);
        bindControl(regionField, "region");
        bindControl(localityTypeField, "localityType", Messages.getString("LocalityEditForm.localityTypeLabel"), REQUIRED);
        bindControl(nameField, "name", nameLabel, REQUIRED);
    }
}
