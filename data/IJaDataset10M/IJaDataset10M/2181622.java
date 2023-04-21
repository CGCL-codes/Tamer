package de.beas.explicanto.client.rcp.workspace.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.dialogs.BaseDialog;

public class AnnotationFilterDialog extends BaseDialog {

    private AnnotationFilterControl tfc;

    private boolean ok = false;

    private int type;

    private String searchString = "";

    public AnnotationFilterDialog(Shell parentShell) {
        super(parentShell);
    }

    public String getTitleId() {
        return I18N.translate("annotation.filter.dialog.title");
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        tfc = new AnnotationFilterControl(composite, SWT.NONE);
        tfc.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        getShell().setText(getTitleId());
        tfc.setSearchString(searchString);
        tfc.setType(type);
        return composite;
    }

    protected void cancelPressed() {
        ok = false;
        super.cancelPressed();
    }

    protected void okPressed() {
        type = tfc.getType();
        searchString = tfc.getSearchString();
        ok = true;
        super.okPressed();
    }

    public boolean isOk() {
        return ok;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
