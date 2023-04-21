package com.marketfarm.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.marketfarm.client.CachePagina.Pagina;
import com.marketfarm.client.footer.Footer;
import com.marketfarm.client.service.AbstractAsynCallback;
import com.marketfarm.client.service.UsuarioService;
import com.marketfarm.client.service.UsuarioServiceAsync;
import com.marketfarm.shared.Utils;
import com.marketfarm.shared.entity.Usuario;

public class NovoUsuario extends Composite {

    private PasswordTextBox tbSenha;

    private TextBox tbNick;

    private TextBox tbEmail;

    private PasswordTextBox tbRepitaASenha;

    private Label lblErroNick;

    private Label lblErroEmail;

    private Label lblErroSenha;

    private Label lblErroRepitaSenha;

    private UsuarioServiceAsync service = GWT.create(UsuarioService.class);

    public NovoUsuario() {
        FlowPanel flowPanel = new FlowPanel();
        initWidget(flowPanel);
        flowPanel.setSize("846px", "");
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        flowPanel.add(verticalPanel);
        verticalPanel.setSize("", "");
        Header header = new Header();
        verticalPanel.add(header);
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setSpacing(10);
        verticalPanel.add(horizontalPanel);
        DecoratorPanel decoratorPanel_1 = new DecoratorPanel();
        horizontalPanel.add(decoratorPanel_1);
        VerticalPanel verticalPanel_3 = new VerticalPanel();
        decoratorPanel_1.setWidget(verticalPanel_3);
        verticalPanel_3.setWidth("798px");
        verticalPanel_3.setSpacing(10);
        Label lblPreenchaOsDados = new Label("Por favor preencha os dados, todos os campos são obrigatórios.");
        lblPreenchaOsDados.setStyleName("font-16");
        verticalPanel_3.add(lblPreenchaOsDados);
        HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
        horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel_3.add(horizontalPanel_1);
        Label lblEmail = new Label("Email:");
        horizontalPanel_1.add(lblEmail);
        lblEmail.setWidth("100px");
        tbEmail = new TextBox();
        lblErroEmail = new Label("Esse email já está cadastrado");
        lblErroEmail.setVisible(false);
        tbEmail.addBlurHandler(new BlurHandler() {

            public void onBlur(BlurEvent event) {
                TextBox tb = ((TextBox) event.getSource());
                if (validaCampoBranco(tb, lblErroEmail)) {
                    service.emailInexistente(tb.getText(), new AbstractAsynCallback<Boolean>() {

                        @Override
                        public void onSuccess(Boolean result) {
                            if (result) {
                                lblErroEmail.setVisible(false);
                            } else {
                                lblErroEmail.setVisible(true);
                                lblErroEmail.setText("Esse email já está cadastrado");
                            }
                        }
                    });
                }
            }
        });
        tbEmail.addFocusHandler(new FocusHandler() {

            public void onFocus(FocusEvent event) {
                lblErroEmail.setVisible(false);
            }
        });
        horizontalPanel_1.add(tbEmail);
        lblErroEmail.setStyleName("padding-left10");
        lblErroEmail.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel_1.add(lblErroEmail);
        HorizontalPanel horizontalPanel_5 = new HorizontalPanel();
        horizontalPanel_5.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel_3.add(horizontalPanel_5);
        Label lblNick = new Label("Usuário:");
        horizontalPanel_5.add(lblNick);
        lblNick.setWidth("100px");
        tbNick = new TextBox();
        lblErroNick = new Label("Esse nick já está cadastrado");
        lblErroNick.setVisible(false);
        tbNick.addBlurHandler(new BlurHandler() {

            public void onBlur(BlurEvent event) {
                TextBox tb = ((TextBox) event.getSource());
                if (validaCampoBranco(tb, lblErroNick)) {
                    service.nickInexistente(tb.getText(), new AbstractAsynCallback<Boolean>() {

                        @Override
                        public void onSuccess(Boolean result) {
                            if (result) {
                                lblErroNick.setVisible(false);
                            } else {
                                lblErroNick.setVisible(true);
                                lblErroNick.setText("Esse nick já está cadastrado");
                            }
                        }
                    });
                }
            }
        });
        tbNick.addFocusHandler(new FocusHandler() {

            public void onFocus(FocusEvent event) {
                lblErroNick.setVisible(false);
            }
        });
        horizontalPanel_5.add(tbNick);
        lblErroNick.setStyleName("padding-left10");
        lblErroNick.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel_5.add(lblErroNick);
        HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
        horizontalPanel_3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel_3.add(horizontalPanel_3);
        Label lblSenha = new Label("Senha:");
        horizontalPanel_3.add(lblSenha);
        lblSenha.setWidth("100px");
        lblErroSenha = new Label("Esse campo não pode ficar em branco");
        lblErroSenha.setVisible(false);
        tbSenha = new PasswordTextBox();
        tbSenha.addBlurHandler(new BlurHandler() {

            public void onBlur(BlurEvent event) {
                TextBox tb = ((TextBox) event.getSource());
                if (validaCampoBranco(tb, lblErroSenha)) lblErroSenha.setVisible(false);
            }
        });
        tbSenha.addFocusHandler(new FocusHandler() {

            public void onFocus(FocusEvent event) {
                lblErroSenha.setVisible(false);
            }
        });
        horizontalPanel_3.add(tbSenha);
        lblErroSenha.setStyleName("padding-left10");
        horizontalPanel_3.add(lblErroSenha);
        HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
        horizontalPanel_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel_3.add(horizontalPanel_2);
        Label lblRepitaASenha = new Label("Repita a Senha:");
        horizontalPanel_2.add(lblRepitaASenha);
        lblRepitaASenha.setWidth("100px");
        lblErroRepitaSenha = new Label("As senhas não são iguais");
        lblErroRepitaSenha.setVisible(false);
        tbRepitaASenha = new PasswordTextBox();
        tbRepitaASenha.addBlurHandler(new BlurHandler() {

            public void onBlur(BlurEvent event) {
                TextBox tb = ((TextBox) event.getSource());
                if (validaCampoBranco(tb, lblErroRepitaSenha)) {
                    if (validaRepitaSenha(tb, lblErroRepitaSenha)) lblErroRepitaSenha.setVisible(false);
                }
            }
        });
        tbRepitaASenha.addFocusHandler(new FocusHandler() {

            public void onFocus(FocusEvent event) {
                lblErroRepitaSenha.setVisible(false);
            }
        });
        horizontalPanel_2.add(tbRepitaASenha);
        lblErroRepitaSenha.setStyleName("padding-left10");
        horizontalPanel_2.add(lblErroRepitaSenha);
        HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
        horizontalPanel_4.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        verticalPanel_3.add(horizontalPanel_4);
        horizontalPanel_4.setWidth("261px");
        final Button btnCriarNovoUsurio = new Button("Criar Novo Usuário");
        btnCriarNovoUsurio.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                boolean parar = false;
                btnCriarNovoUsurio.setEnabled(false);
                if (!validaCampoBranco(tbEmail, lblErroEmail)) parar = true;
                if (!validaCampoBranco(tbNick, lblErroNick)) parar = true;
                if (!validaCampoBranco(tbSenha, lblErroSenha)) parar = true;
                if (!validaCampoBranco(tbRepitaASenha, lblErroRepitaSenha)) parar = true;
                if (!validaRepitaSenha(tbRepitaASenha, lblErroRepitaSenha)) parar = true;
                if (parar) {
                    btnCriarNovoUsurio.setEnabled(true);
                    return;
                }
                Usuario usuario = new Usuario();
                usuario.setEmail(tbEmail.getText());
                usuario.setNick(tbNick.getText());
                usuario.setSenha(tbSenha.getText());
                service.novoUsuario(usuario, new AbstractAsynCallback<Void>() {

                    @Override
                    public void onSuccess(Void result) {
                        btnCriarNovoUsurio.setEnabled(true);
                        Window.alert("Usuário criado com sucesso");
                        History.newItem(Pagina.LOGIN.toString());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                        btnCriarNovoUsurio.setEnabled(true);
                    }
                });
            }
        });
        horizontalPanel_4.add(btnCriarNovoUsurio);
        Footer footer = new Footer();
        verticalPanel.add(footer);
    }

    private boolean validaCampoBranco(TextBox tb, Label label) {
        if (Utils.isEmpty(tb.getText())) {
            label.setVisible(true);
            label.setText("Esse campo não pode ficar em branco");
            return false;
        }
        return true;
    }

    private boolean validaRepitaSenha(TextBox tb, Label label) {
        if (!tb.getText().equals(tbSenha.getText())) {
            label.setVisible(true);
            label.setText("As senhas não iguais");
            return false;
        }
        return true;
    }
}
