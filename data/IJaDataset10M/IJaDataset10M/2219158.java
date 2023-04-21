package com.byterefinery.rmbench.dialogs;

import java.util.Iterator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.byterefinery.rmbench.RMBenchPlugin;
import com.byterefinery.rmbench.model.Model;
import com.byterefinery.rmbench.model.schema.Column;
import com.byterefinery.rmbench.model.schema.Key;
import com.byterefinery.rmbench.operations.ModifyKeyConstraintOperation;

/**
 * a dialog for editing keys (primary or unique) by adding or moving columns
 * 
 * @author cse
 */
public class KeyEditorDialog extends Dialog {

    private final Key key;

    private CheckboxTableHandler viewerHandler;

    private CheckboxTableViewer columnsViewer;

    private Text nameText;

    private boolean columnsChanged, nameChanged;

    private Label errorLabel;

    private Model model;

    private String oldName;

    private boolean nameError;

    public KeyEditorDialog(Shell parentShell, Key key) {
        super(parentShell);
        this.key = key;
        this.model = RMBenchPlugin.getModelManager().getModel();
        this.nameError = false;
    }

    protected Control createDialogArea(Composite container) {
        Composite parent = (Composite) super.createDialogArea(container);
        Composite nameGroup = new Composite(parent, SWT.NULL);
        nameGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        nameGroup.setLayout(new GridLayout(2, false));
        Label nameLabel = new Label(nameGroup, SWT.LEFT);
        nameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        nameLabel.setText("name:");
        Composite nameComp = new Composite(nameGroup, SWT.NULL);
        GridLayout gl = new GridLayout();
        gl.marginHeight = 0;
        nameComp.setLayout(gl);
        nameComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        nameText = new Text(nameComp, SWT.BORDER | SWT.SINGLE);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        oldName = key.getName();
        nameText.setText(oldName);
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                updateErrorLabel();
                updateButtonState();
            }
        });
        errorLabel = new Label(nameComp, SWT.NONE);
        errorLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        GridData gd = new GridData(SWT.LEFT | SWT.FILL, SWT.NONE, true, false);
        gd.widthHint = convertWidthInCharsToPixels(Math.max(Messages.ConstraintWizard_emptyNameError.length(), Messages.ConstraintWizard_nameError.length()));
        errorLabel.setLayoutData(gd);
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 30;
        layout.marginHeight = 30;
        composite.setLayout(layout);
        viewerHandler = new CheckboxTableHandler(composite);
        columnsViewer = CheckboxTableViewer.newCheckList(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.minimumWidth = convertWidthInCharsToPixels(30);
        gd.minimumHeight = convertHeightInCharsToPixels(5);
        columnsViewer.getTable().setLayoutData(gd);
        columnsViewer.setLabelProvider(new LabelProvider() {

            public String getText(Object element) {
                return ((Column) element).getName();
            }
        });
        viewerHandler.setViewer(columnsViewer, key.getTable().getColumns(), key.getColumns());
        viewerHandler.addListener(new CheckboxTableHandler.Listener() {

            public void checkMoved(Column column, int oldIndex, int newIndex) {
                updateButtonState();
            }

            public void checkCountChanged() {
                updateButtonState();
            }
        });
        return parent;
    }

    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }

    protected void updateButtonState() {
        String newName = nameText.getText();
        if (newName.length() == 0 || !viewerHandler.hasChecked() || nameError) {
            getButton(IDialogConstants.OK_ID).setEnabled(false);
            return;
        }
        boolean columnsEqual = true;
        Iterator<Column> it = viewerHandler.checkedColumns();
        int i = 0;
        while (it.hasNext() && i < key.size()) {
            Column col = it.next();
            if (col != key.getColumn(i)) {
                columnsEqual = false;
                break;
            }
            i++;
        }
        columnsChanged = !columnsEqual || i != key.size() || it.hasNext();
        nameChanged = !newName.equals(key.getName());
        getButton(IDialogConstants.OK_ID).setEnabled(columnsChanged || nameChanged);
    }

    protected void okPressed() {
        Column[] changedColumns = columnsChanged ? viewerHandler.getCheckedColumns() : null;
        String changedName = nameChanged ? nameText.getText() : null;
        ModifyKeyConstraintOperation operation = new ModifyKeyConstraintOperation(key, changedName, changedColumns);
        operation.execute(this);
        super.okPressed();
    }

    private void updateErrorLabel() {
        if (nameText.getText().length() < 1) {
            errorLabel.setText(Messages.ConstraintWizard_emptyNameError);
            return;
        }
        if (nameText.getText().equals(oldName)) {
            errorLabel.setText("");
            return;
        }
        if (model.containsConstraint(nameText.getText())) {
            errorLabel.setText(Messages.ConstraintWizard_nameError);
            nameError = true;
            return;
        }
        errorLabel.setText("");
        nameError = false;
    }

    public Column[] getKeyColumns() {
        return viewerHandler.getCheckedColumns();
    }

    public String getKeyName() {
        return nameText.getText();
    }
}
