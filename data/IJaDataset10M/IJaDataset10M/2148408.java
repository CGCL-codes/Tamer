package de.lema.client.editor.permission;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import de.lema.bo.LemaSecurityRole;
import de.lema.bo.LemaUserPermission;
import de.lema.client.eclipse.i18n.Messages;
import de.lema.client.editor.template.LemaBoEditor;
import de.lema.client.view.permission.LemaUserPermissionView;
import de.lema.transfer.AsyncCallback;
import de.lema.transfer.container.BosLadenRequest;
import de.lema.transfer.container.BosLadenResponse;

public class LemaUserPermissionEditor extends LemaBoEditor<LemaUserPermission, LemaUserPermissionEditorInput> {

    public static final String ID = "de.lema.client.editor.permission.LemaUserPermissionEditor";

    private Label lblUsername;

    private Text textUsername;

    private Label lblPassword;

    private Text textPassword;

    private Label lblRolle;

    private Combo comboRolle;

    private ComboViewer comboViewerRolle;

    public LemaUserPermissionEditor() {
        super(LemaUserPermissionView.ID);
    }

    @Override
    public void createPartControl(final Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        lblUsername = new Label(parent, SWT.NONE);
        lblUsername.setText(Messages.LemaUserPermissionEditor_username);
        textUsername = new Text(parent, SWT.BORDER);
        textUsername.addVerifyListener(new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                e.text = e.text.toUpperCase();
            }
        });
        textUsername.setEditable(false);
        textUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        lblPassword = new Label(parent, SWT.NONE);
        lblPassword.setText(Messages.LemaUserPermissionEditor_password);
        textPassword = new Text(parent, SWT.BORDER | SWT.PASSWORD);
        textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        lblRolle = new Label(parent, SWT.NONE);
        lblRolle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblRolle.setText(Messages.LemaUserPermissionEditor_rolle);
        comboViewerRolle = new ComboViewer(parent, SWT.READ_ONLY);
        comboRolle = comboViewerRolle.getCombo();
        comboRolle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        comboViewerRolle.setContentProvider(new ArrayContentProvider());
        setEnabledDisabled();
        requestSecurityRoles();
    }

    private void requestSecurityRoles() {
        getServerAccess().requestAsync(getSite().getShell(), new BosLadenRequest<LemaSecurityRole>(LemaSecurityRole.class), new AsyncCallback<BosLadenResponse<LemaSecurityRole>>() {

            public void onFailure(final Throwable caught) {
                setEnabledDisabled();
            }

            public void onSuccess(final BosLadenResponse<LemaSecurityRole> result) {
                comboViewerRolle.setInput(result.getList());
                postInit();
            }
        });
    }

    @Override
    protected void setEnabledDisabled() {
        if (!comboRolle.isDisposed()) {
            comboRolle.setEnabled(isEdit());
            textPassword.setEditable(isEdit());
            textUsername.setEditable(!isExists() && isEdit());
        }
    }

    @Override
    public void setName() {
        setPartName("Berechtigung " + (getBo().getUsername() != null ? getBo().getUsername() : "<Neu>"));
    }

    @Override
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        IObservableValue textUsernameObserveTextObserveWidget = SWTObservables.observeText(textUsername, SWT.Modify);
        IObservableValue usernameObserveValue = PojoObservables.observeValue(getBo(), "username");
        bindingContext.bindValue(textUsernameObserveTextObserveWidget, usernameObserveValue, null, null);
        IObservableValue textPasswordObserveTextObserveWidget = SWTObservables.observeText(textPassword, SWT.Modify);
        IObservableValue passwordObserveValue = PojoObservables.observeValue(getBo(), "password");
        bindingContext.bindValue(textPasswordObserveTextObserveWidget, passwordObserveValue, null, null);
        IObservableValue comboViewerObserveSingleSelection = ViewersObservables.observeSingleSelection(comboViewerRolle);
        IObservableValue enumObserveValue = PojoObservables.observeValue(getBo(), "role");
        bindingContext.bindValue(comboViewerObserveSingleSelection, enumObserveValue, null, null);
        return bindingContext;
    }

    @Override
    public Label getLabelError() {
        return null;
    }

    @Override
    public boolean validate() {
        return getBo().getUsername() != null && getBo().getUsername().length() > 0 && getBo().getRole() != null;
    }
}
