package br.com.napoleao.controlfin.form;

import java.util.Locale;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import br.com.napoleao.controlfin.core.business.bo.ContaBO;
import br.com.napoleao.controlfin.core.entity.ContaEntity;
import br.com.napoleao.controlfin.core.exception.BusinessException;
import br.com.napoleao.controlfin.core.persistence.dao.ContaDAO;
import br.com.napoleao.controlfin.util.DecimalFormatter;
import br.com.napoleao.controlfin.util.GUIUtil;
import com.swtdesigner.SWTResourceManager;

public class SalvarContaDialog extends Dialog {

    protected Object result;

    protected Shell shell;

    private Text txtNome;

    private Button btnSalvar;

    private ContaEntity conta;

    private Text txtSaldoInicial;

    private Label lblReais;

    private DecimalFormatter decFormatter;

    private FormattedText fmtSaldoInicial;

    /**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
    public SalvarContaDialog(Shell parent, int style) {
        super(parent, style);
        setText("Salvar Conta");
    }

    /**
	 * Open the dialog.
	 * @return the result
	 */
    public Object open() {
        createContents();
        GUIUtil.centralizeShell(shell, shell.getParent().getShell());
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
	 * Create contents of the dialog.
	 */
    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        shell.setSize(327, 121);
        shell.setText(getText());
        shell.setLayout(null);
        Label lblNome = new Label(shell, SWT.NONE);
        lblNome.setBounds(40, 13, 36, 15);
        lblNome.setText("Nome:");
        txtNome = new Text(shell, SWT.BORDER);
        txtNome.setBounds(82, 10, 233, 21);
        Label lblSaldoInicial = new Label(shell, SWT.NONE);
        lblSaldoInicial.setBounds(10, 40, 66, 15);
        if (conta == null || conta.getId() == null) lblSaldoInicial.setText("Saldo Inicial:"); else lblSaldoInicial.setText("Saldo Atual:");
        fmtSaldoInicial = new FormattedText(shell, SWT.BORDER | SWT.RIGHT);
        decFormatter = new DecimalFormatter("-###,###,###.##", "###,###,###.00", Locale.getDefault());
        fmtSaldoInicial.setFormatter(decFormatter);
        txtSaldoInicial = fmtSaldoInicial.getControl();
        txtSaldoInicial.setBounds(82, 37, 87, 21);
        btnSalvar = new Button(shell, SWT.NONE);
        btnSalvar.setBounds(236, 61, 75, 26);
        btnSalvar.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                botaoSalvarConta();
            }
        });
        btnSalvar.setImage(SWTResourceManager.getImage(SalvarContaDialog.class, "/br/com/napoleao/controlfin/form/resources/salvar.png"));
        btnSalvar.setText("Salvar");
        lblReais = new Label(shell, SWT.NONE);
        lblReais.setBounds(175, 40, 55, 15);
        lblReais.setText("reais");
        popularObjetoFormulario();
    }

    public void recuperaObjetoFormulario() {
        if (conta == null) conta = new ContaEntity();
        if (txtNome.getText() != null && !txtNome.getText().equals("")) conta.setNome(txtNome.getText());
        if (decFormatter.getValue() != null) conta.setSaldoInicial((Double) decFormatter.getValue());
    }

    public void popularObjetoFormulario() {
        if (conta != null && conta.getNome() != null) txtNome.setText(conta.getNome());
        if (conta != null && conta.getSaldoInicial() != null) fmtSaldoInicial.setValue(conta.getSaldoInicial());
    }

    public void botaoSalvarConta() {
        recuperaObjetoFormulario();
        try {
            new ContaBO().validarObjeto(conta);
        } catch (BusinessException e) {
            MessageDialog.openError(shell, "Erro ao cadastrar", e.getMessagesString());
            return;
        }
        if (conta != null && conta.getId() == null) new ContaDAO().insert(conta); else {
            new ContaDAO().update(conta);
        }
        shell.dispose();
    }

    public ContaEntity getConta() {
        return conta;
    }

    public void setConta(ContaEntity conta) {
        this.conta = conta;
    }
}
