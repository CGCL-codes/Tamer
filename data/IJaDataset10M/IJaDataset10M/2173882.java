package br.usp.ime.protoc.client;

import br.usp.ime.protoc.pessoa.Parente;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CadastroParente extends Composite {

    private Grid grid;

    private TextBox textBox;

    private TextBox textBox_1;

    private TextBox textBox_2;

    private TextBox textBox_3;

    private TextBox textBox_4;

    public CadastroParente() {
        final VerticalPanel verticalPanel = new VerticalPanel();
        initWidget(verticalPanel);
        verticalPanel.setBorderWidth(1);
        final Label dadosCadastraisPacienteLabel = new Label("Inclus�o de Parentes");
        verticalPanel.add(dadosCadastraisPacienteLabel);
        dadosCadastraisPacienteLabel.setStylePrimaryName("gwt-Label-Title");
        dadosCadastraisPacienteLabel.setStyleName("gwt-Label-Title");
        grid = new Grid();
        verticalPanel.add(grid);
        grid.resize(6, 2);
        final Label idLabel = new Label("ID:");
        grid.setWidget(0, 0, idLabel);
        final Label cpfLabel = new Label("CPF:");
        grid.setWidget(2, 0, cpfLabel);
        final Label dataDeNascimentoLabel = new Label("Data de Nascimento:");
        grid.setWidget(3, 0, dataDeNascimentoLabel);
        textBox = new TextBox();
        grid.setWidget(0, 1, textBox);
        grid.getCellFormatter().setWidth(0, 1, "200");
        textBox.setWidth("100%");
        textBox_1 = new TextBox();
        grid.setWidget(1, 1, textBox_1);
        textBox_1.setWidth("100%");
        textBox_2 = new TextBox();
        grid.setWidget(3, 1, textBox_2);
        textBox_2.setWidth("100%");
        textBox_3 = new TextBox();
        grid.setWidget(2, 1, textBox_3);
        textBox_3.setWidth("100%");
        final Label nomeLabel = new Label("Nome:");
        grid.setWidget(1, 0, nomeLabel);
        final Label emailLabel = new Label("E-mail:");
        grid.setWidget(4, 0, emailLabel);
        textBox_4 = new TextBox();
        grid.setWidget(4, 1, textBox_4);
        textBox_4.setWidth("100%");
        final Button salvarButton = new Button();
        grid.setWidget(5, 1, salvarButton);
        salvarButton.addClickListener(new ClickListener() {

            public void onClick(final Widget sender) {
                incluirParente();
            }
        });
        salvarButton.setText("Salvar");
        final Button limparButton = new Button();
        grid.setWidget(5, 0, limparButton);
        limparButton.addClickListener(new ClickListener() {

            public void onClick(final Widget sender) {
                limpaCampos();
            }
        });
        grid.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        limparButton.setText("Limpar");
    }

    private void incluirParente() {
        Parente parente = new Parente();
        parente.setId(Integer.parseInt(textBox.getText()));
        parente.setNome(textBox_1.getText());
        parente.setCpf(textBox_2.getText());
        parente.setDataNascimento(textBox_3.getText());
        parente.setEmail(textBox_4.getText());
        ParenteService.Util.getInstance().incluirParente(parente, new AsyncCallback() {

            public void onSuccess(Object result) {
                Boolean resultado = (Boolean) result;
                if (resultado) {
                    final Label mensagem = new Label("Inclusao com Sucesso");
                    limpaCampos();
                    ProtocProman.simplePanelMsg.add(mensagem);
                    ConsultaParente.getParentes();
                } else {
                    final Label mensagem = new Label("Problemas na inclusao do Paciente");
                    ProtocProman.simplePanelMsg.add(mensagem);
                }
            }

            public void onFailure(Throwable caught) {
                final Label mensagem = new Label("Problemas na inclusao do Parente - " + caught.getMessage() + "\n" + caught.getCause());
                ProtocProman.simplePanelMsg.add(mensagem);
            }
        });
    }

    private void limpaCampos() {
        textBox.setText("");
        textBox_1.setText("");
        textBox_2.setText("");
        textBox_3.setText("");
        textBox_4.setText("");
    }
}
