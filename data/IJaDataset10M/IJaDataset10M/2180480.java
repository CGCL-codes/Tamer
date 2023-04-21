package org.plazmaforge.studio.appmanager.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.plazmaforge.framework.config.object.IPackageConfig;
import org.plazmaforge.studio.appmanager.AppManagerResources;

public class PackageEditDialog extends AbstractConfigEditDialog {

    protected Text compressionClassField;

    public PackageEditDialog(Shell parentShell, int command, IPackageConfig packageConfig) {
        super(parentShell, command);
        setObjectConfig(packageConfig);
    }

    protected int getColumnCount() {
        return 3;
    }

    protected void createFields(Composite composite) {
        createIdField(composite);
        createNameField(composite);
        createClassField(composite, false);
        Label label = new Label(composite, SWT.NONE);
        label.setText(AppManagerResources.Compression_class);
        compressionClassField = new Text(composite, SWT.BORDER);
        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.horizontalSpan = getColumnCount() - 1;
        compressionClassField.setLayoutData(gridData);
        compressionClassField.addKeyListener(validateListener);
        label = new Label(composite, SWT.NONE);
        label.setText(AppManagerResources.Enabled);
        enableField = new Button(composite, SWT.CHECK);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, getColumnCount() - 1, 1);
        enableField.setLayoutData(gridData);
    }

    protected void loadData() {
        if (getPackageConfig() == null) {
            return;
        }
        setText(idField, getPackageConfig().getId());
        setText(nameField, getPackageConfig().getName());
        setText(classField, getPackageConfig().getClassName());
        setText(compressionClassField, getPackageConfig().getCompressionClassName());
        enableField.setSelection(getPackageConfig().isEnable());
        if (!getPackageConfig().isVirtual()) {
            return;
        }
        idField.setEditable(false);
        nameField.setEditable(false);
        classField.setEditable(false);
        compressionClassField.setEditable(false);
    }

    protected void storeData() {
        getPackageConfig().setId(idField.getText());
        getPackageConfig().setName(nameField.getText());
        getPackageConfig().setClassName(classField.getText());
        getPackageConfig().setCompressionClassName(compressionClassField.getText());
        getPackageConfig().setEnable(enableField.getSelection());
        modifyObject(getPackageConfig());
    }

    protected String[] getNLSProperties() {
        return new String[] { NLSEditDialog.NLS_PROPERTY_NAME };
    }

    protected String getElementMessage() {
        return AppManagerResources.Config_Package_message;
    }

    protected void validate() {
        validateID();
    }

    protected IPackageConfig getPackageConfig() {
        return (IPackageConfig) getObjectConfig();
    }
}
