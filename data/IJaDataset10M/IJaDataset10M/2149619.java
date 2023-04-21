package etp.client.gui;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import etp.client.Recurso;

public class RecursoForm extends PopupPanel {

    /**
	 * Constante que indica o modo em que o formul�rio de projeto foi 
	 * aberto.
	 */
    public static final int MODE_EDIT = 0;

    /**
	 * Constante que indica o modo em que o formul�rio de projeto foi 
	 * aberto.
	 */
    public static final int MODE_NEW = 1;

    /**
	 * R�tulo do campo para entrada do nome do projeto.
	 */
    private Label lblNomeRecurso = new Label("Nome:");

    /**
	 * R�tulo do campo para selecionar o respons�vel pelo projeto
	 */
    private Label lblEmail = new Label("Email:");

    /**
	 * R�tulo para apresenta��o do formul�rio
	 */
    private Label lblNomeForm = new Label("Cadastrar Recurso");

    /**
	 * R�tulo para exibir erros no preenchimento do nome do projeto.
	 */
    private Label lblErro = new Label();

    /**
	 * Senha
	 */
    private Label lblPassword = new Label("senha:");

    private Label lblConfirm = new Label("confirmacao:");

    /**
	 * Caixa de texto para entrada do nome do projelto.
	 */
    private TextBox txtNomeRecurso = new TextBox();

    /**
	 * Lista box para sele��o do respons�vel pelo projeto
	 */
    private TextBox txtEmail = new TextBox();

    private PasswordTextBox txtPassword = new PasswordTextBox();

    private PasswordTextBox txtConfirm = new PasswordTextBox();

    /**
	 * Bot�o para confirmar o cadastro.
	 */
    private Button btnOk = new Button("Cadastrar");

    /**
	 * Bot�o para cancelar o cadastro
	 */
    private Button btnCancelar = new Button("Cancelar");

    /**
	 * Painel principal do formul�rio.
	 */
    private FlexTable pnlForm = new FlexTable();

    /**
	 * Lista de objetos que cont�m o c�digo para realizar o cadastro.
	 */
    private List<FormRecursoHandler> cadastroHandlers = new ArrayList<FormRecursoHandler>();

    /**
	 * Indica o modo em que o formul�rio est� operando.
	 */
    private int mode = RecursoForm.MODE_NEW;

    /**
	 * Objeto que cont�m os dados que s�o exibidos ou configurados neste 
	 * formul�rio.
	 */
    private Recurso model;

    /**
	 * Construtor principal
	 */
    public RecursoForm() {
        this.getElement().setAttribute("style", "padding: 20");
        this.lblNomeForm.addStyleName("etp-ApontamentoFormTitle");
        this.lblErro.addStyleName("ErrorLabel");
        pnlForm.setWidget(0, 0, this.lblNomeForm);
        pnlForm.setWidget(1, 0, this.lblErro);
        pnlForm.setWidget(2, 0, this.lblNomeRecurso);
        pnlForm.setWidget(2, 1, this.txtNomeRecurso);
        pnlForm.setWidget(3, 0, this.lblEmail);
        pnlForm.setWidget(3, 1, this.txtEmail);
        pnlForm.setWidget(4, 0, this.lblPassword);
        pnlForm.setWidget(4, 1, this.txtPassword);
        pnlForm.setWidget(5, 0, this.lblConfirm);
        pnlForm.setWidget(5, 1, this.txtConfirm);
        FlowPanel pnlButtons = new FlowPanel();
        pnlButtons.add(btnCancelar);
        pnlButtons.add(btnOk);
        pnlForm.setWidget(6, 0, pnlButtons);
        ClickHandler handlerBtn = new ActionButtons();
        this.btnCancelar.addClickHandler(handlerBtn);
        this.btnOk.addClickHandler(handlerBtn);
        this.btnCancelar.getElement().setAttribute("style", "margin-right:10");
        pnlForm.getFlexCellFormatter().setColSpan(0, 0, 2);
        pnlForm.getFlexCellFormatter().setColSpan(1, 0, 2);
        pnlForm.getFlexCellFormatter().setColSpan(0, 0, 2);
        pnlForm.getFlexCellFormatter().setHeight(0, 0, "60");
        pnlForm.getFlexCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
        pnlForm.getFlexCellFormatter().setColSpan(6, 0, 3);
        pnlForm.getFlexCellFormatter().setHeight(6, 0, "60");
        pnlForm.getFlexCellFormatter().setAlignment(6, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_BOTTOM);
        pnlForm.getFlexCellFormatter().setHeight(6, 0, "60");
        this.add(pnlForm);
        this.setModel(new Recurso());
    }

    /**
	 * Retorna o modelo de dados deste formul�rio.
	 * 
	 * @return objeto que encapsula os dados de um projeto
	 */
    public Recurso getModel() {
        return model;
    }

    /**
	 * Seta um modelo para o projeto.
	 * 
	 * ATEN��O: setar este modelo n�o atualiza a interface com os dados, para 
	 * isto veja o m�todo updateFromModel.
	 * 
	 * @param model
	 */
    public void setModel(Recurso model) {
        this.model = model;
    }

    /**
	 *	Atualiza os dados inseridos no formul�rio no modelo.
	 */
    public void updateModel() {
        this.model.setNome(this.txtNomeRecurso.getText());
        this.model.setEmail(this.txtEmail.getText());
        this.model.setPassword(this.txtPassword.getText());
    }

    /**
	 * Atualiza o formul�rio com os dados presentes no modelo
	 */
    public void updateFromModel() {
        this.txtNomeRecurso.setText(model.getNome());
        this.txtEmail.setText(model.getEmail());
    }

    public void addCadastroHandler(FormRecursoHandler handler) {
        this.cadastroHandlers.add(handler);
    }

    public boolean removeCadastroHandler(FormRecursoHandler handler) {
        return this.cadastroHandlers.remove(handler);
    }

    public void edit(Recurso prj) {
        this.setModel(prj);
        this.updateFromModel();
        this.txtNomeRecurso.setEnabled(true);
        this.txtEmail.setEnabled(false);
        this.btnOk.setText("Confirmar");
        this.mode = RecursoForm.MODE_EDIT;
        this.show();
    }

    public void novo() {
        this.setModel(new Recurso());
        this.txtNomeRecurso.setEnabled(true);
        this.txtEmail.setEnabled(true);
        this.btnOk.setText("Cadastrar");
        this.mode = RecursoForm.MODE_NEW;
        this.show();
    }

    public void clean() {
        this.txtNomeRecurso.setText("");
        this.txtEmail.setText("");
        this.lblErro.setText("");
        this.txtPassword.setText("");
        this.txtConfirm.setText("");
    }

    protected void fireCadastroEvent() {
        this.updateModel();
        for (FormRecursoHandler h : this.cadastroHandlers) {
            h.cadastrarRecurso(this.getModel());
        }
    }

    protected void fireEdicaoEvent() {
        this.updateModel();
        for (FormRecursoHandler h : this.cadastroHandlers) {
            h.editarRecurso(this.getModel());
        }
    }

    protected boolean validar() {
        if (this.txtNomeRecurso.getText().equals("")) {
            this.lblErro.setText("Nome é obrigatório");
            return false;
        }
        if (!this.validaEmail()) {
            return false;
        }
        if (this.txtPassword.getText().equals("")) {
            this.lblErro.setText("Senha não definida.");
            return false;
        }
        if (!this.txtPassword.getText().equals(this.txtConfirm.getText())) {
            this.lblErro.setText("Senha não confere com a confirmacao");
            return false;
        }
        this.lblErro.setText("");
        return true;
    }

    protected boolean validaEmail() {
        if (this.txtEmail.getText().equals("")) {
            this.lblErro.setText("Email não pode ser vazio.");
            return false;
        }
        int indexArromba = this.txtEmail.getText().indexOf("@");
        if (indexArromba == -1) {
            this.lblErro.setText("Email inválido.");
            return false;
        }
        String dominio = this.txtEmail.getText().substring(indexArromba);
        if (dominio.length() <= 1) {
            this.lblErro.setText("Domínio inválido.");
            return false;
        }
        String nome = this.txtEmail.getText().substring(0, indexArromba);
        if (nome.length() == 0) {
            this.lblErro.setText("Nome de usuário inválido.");
            return false;
        }
        return true;
    }

    protected class ActionButtons implements ClickHandler {

        public void onClick(ClickEvent event) {
            if (event.getSource() == RecursoForm.this.btnCancelar) {
                RecursoForm.this.clean();
                RecursoForm.this.hide();
            } else if (event.getSource() == RecursoForm.this.btnOk) {
                if (RecursoForm.this.validar()) {
                    if (RecursoForm.this.mode == RecursoForm.MODE_EDIT) {
                        RecursoForm.this.fireEdicaoEvent();
                    } else {
                        RecursoForm.this.fireCadastroEvent();
                    }
                    RecursoForm.this.clean();
                    RecursoForm.this.hide();
                }
            }
        }
    }

    public interface FormRecursoHandler {

        public void cadastrarRecurso(Recurso p);

        public void editarRecurso(Recurso p);
    }
}
