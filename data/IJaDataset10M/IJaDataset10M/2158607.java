package com.teste.jpaUiBinder.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.teste.jpaUiBinder.client.business.FuncionarioBusiness;
import com.teste.jpaUiBinder.client.business.FuncionarioBusinessAsync;
import com.teste.jpaUiBinder.shared.entidades.Funcionario;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;

public class FuncionarioView extends Composite implements Editor<Funcionario> {

    private static FuncionarioViewUiBinder uiBinder = GWT.create(FuncionarioViewUiBinder.class);

    interface FuncionarioViewUiBinder extends UiBinder<Widget, FuncionarioView> {
    }

    private final FuncionarioBusinessAsync funcAssync = GWT.create(FuncionarioBusiness.class);

    interface Driver extends SimpleBeanEditorDriver<Funcionario, FuncionarioView> {
    }

    Driver driver = GWT.create(Driver.class);

    @Path("nome")
    @UiField
    TextBox nome;

    @Path("idade")
    @UiField
    IntegerBox idade;

    public FuncionarioView() {
        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);
        Funcionario func = new Funcionario();
        driver.edit(func);
    }

    @UiHandler("salvar")
    void onSalvar(ClickEvent event) {
        Funcionario func = driver.flush();
        funcAssync.cadastrar(func, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                Window.alert("Sucesso");
                driver.edit(new Funcionario());
            }

            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }
        });
    }
}
