package org.nightlabs.jfire.base.ui.prop.edit.blockbased;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.nightlabs.base.ui.composite.XComposite;
import org.nightlabs.jfire.base.ui.prop.edit.AbstractDataFieldEditor;
import org.nightlabs.jfire.base.ui.prop.edit.AbstractDataFieldEditorFactory;
import org.nightlabs.jfire.base.ui.prop.edit.DataFieldEditor;
import org.nightlabs.jfire.base.ui.prop.edit.fieldbased.FieldBasedEditor;
import org.nightlabs.jfire.base.ui.resource.Messages;
import org.nightlabs.jfire.prop.IStruct;
import org.nightlabs.jfire.prop.datafield.PhoneNumberDataField;
import org.nightlabs.util.NLLocale;

public class PhoneNumberDataFieldEditor extends AbstractDataFieldEditor<PhoneNumberDataField> {

    public PhoneNumberDataFieldEditor(IStruct struct, PhoneNumberDataField data) {
        super(struct, data);
    }

    public static class PhoneNumberDataFieldEditorFactory extends AbstractDataFieldEditorFactory<PhoneNumberDataField> {

        @Override
        public DataFieldEditor<PhoneNumberDataField> createPropDataFieldEditor(IStruct struct, PhoneNumberDataField data) {
            return new PhoneNumberDataFieldEditor(struct, data);
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public String[] getEditorTypes() {
            return new String[] { ExpandableBlocksEditor.EDITORTYPE_BLOCK_BASED_EXPANDABLE, FieldBasedEditor.EDITORTYPE_FIELD_BASED };
        }

        @Override
        public Class<PhoneNumberDataField> getPropDataFieldType() {
            return PhoneNumberDataField.class;
        }
    }

    PhoneNumberDataFieldEditorComposite comp;

    @Override
    public Control createControl(Composite parent) {
        comp = new PhoneNumberDataFieldEditorComposite(parent, this);
        return comp;
    }

    @Override
    public void doRefresh() {
        PhoneNumberDataField dataField = getDataField();
        comp.countryCodeTextBox.setText(dataField.getCountryCode() != null ? dataField.getCountryCode() : "");
        comp.areaCodeTextBox.setText(dataField.getAreaCode() != null ? dataField.getAreaCode() : "");
        comp.localNumberTextBox.setText(dataField.getLocalNumber() != null ? dataField.getLocalNumber() : "");
        comp.compGroup.setText(getStructField().getName().getText(NLLocale.getDefault().getLanguage()));
    }

    public Control getControl() {
        return comp;
    }

    public void updatePropertySet() {
        PhoneNumberDataField dataField = getDataField();
        dataField.setCountryCode(comp.countryCodeTextBox.getText());
        dataField.setAreaCode(comp.areaCodeTextBox.getText());
        dataField.setLocalNumber(comp.localNumberTextBox.getText());
    }
}

class PhoneNumberDataFieldEditorComposite extends XComposite {

    private PhoneNumberDataFieldEditor phoneNumberDataFieldEditor;

    protected Text countryCodeTextBox;

    protected Text areaCodeTextBox;

    protected Text localNumberTextBox;

    protected Group compGroup;

    public PhoneNumberDataFieldEditorComposite(Composite parent, PhoneNumberDataFieldEditor editor) {
        super(parent, SWT.NONE, LayoutMode.LEFT_RIGHT_WRAPPER, LayoutDataMode.GRID_DATA_HORIZONTAL, 1);
        this.phoneNumberDataFieldEditor = editor;
        getGridLayout().horizontalSpacing = 0;
        getGridLayout().marginLeft = 0;
        getGridLayout().marginRight = 0;
        compGroup = new Group(this, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.numColumns = 6;
        gl.horizontalSpacing = 1;
        gl.marginLeft = 0;
        gl.marginRight = 0;
        compGroup.setLayout(gl);
        XComposite.setLayoutDataMode(LayoutDataMode.GRID_DATA_HORIZONTAL, compGroup);
        new Label(compGroup, SWT.NONE).setText("+ ");
        countryCodeTextBox = new Text(compGroup, getBorderStyle());
        GridData gd = new GridData();
        gd.widthHint = 30;
        countryCodeTextBox.setLayoutData(gd);
        gd = new GridData();
        gd.widthHint = 60;
        new Label(compGroup, SWT.NONE).setText(" - ");
        areaCodeTextBox = new Text(compGroup, getBorderStyle());
        areaCodeTextBox.setLayoutData(gd);
        new Label(compGroup, SWT.NONE).setText(" - ");
        localNumberTextBox = new Text(compGroup, getBorderStyle());
        XComposite.setLayoutDataMode(LayoutDataMode.GRID_DATA_HORIZONTAL, localNumberTextBox);
        ModifyListener modifyListener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                phoneNumberDataFieldEditor.setChanged(true);
            }
        };
        countryCodeTextBox.addModifyListener(modifyListener);
        areaCodeTextBox.addModifyListener(modifyListener);
        localNumberTextBox.addModifyListener(modifyListener);
    }
}
