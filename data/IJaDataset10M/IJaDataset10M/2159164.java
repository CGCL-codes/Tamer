package br.com.visualmidia.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import br.com.visualmidia.GD;
import br.com.visualmidia.ui.preferences.composite.SuportComposite;

/**
 * @author  Lucas
 */
public class UserAndPasswordDialog extends TitleAreaDialog {

    private SuportComposite suportComposite;

    private Composite composite;

    private GD gd;

    public UserAndPasswordDialog(Shell shell) {
        super(shell);
        gd = GD.getInstance();
    }

    protected Control createContents(Composite parent) {
        super.createContents(parent);
        setTitle("Dados de Autentica��o");
        setMessage("Informe o Usu�rio e Senha para Conex�o");
        return parent;
    }

    protected Control createDialogArea(Composite parent) {
        composite = (Composite) super.createDialogArea(parent);
        suportComposite = new SuportComposite(composite, SWT.NONE);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 200;
        suportComposite.setLayoutData(gridData);
        return dialogArea;
    }

    @Override
    protected void buttonPressed(int arg0) {
        if (arg0 == IDialogConstants.OK_ID) {
            gd.set("username", suportComposite.getUsername());
            gd.set("password", suportComposite.getPassword());
        }
        close();
    }
}
