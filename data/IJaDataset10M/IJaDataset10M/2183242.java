package com.gft.larozanam.client.widgets;

import com.gft.larozanam.client.componentes.CGrid;
import com.gft.larozanam.client.componentes.CListBox;
import com.gft.larozanam.client.componentes.CTextBox;
import com.gft.larozanam.client.menus.Factory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class EnfermeiroView extends Composite {

    @UiField
    CTextBox rg;

    @UiField
    CTextBox nome;

    @UiField
    CListBox<String> situacao;

    @UiField
    CGrid enfermeiroGrid;

    private static EnfermeiroViewUiBinder uiBinder = GWT.create(EnfermeiroViewUiBinder.class);

    interface EnfermeiroViewUiBinder extends UiBinder<Widget, EnfermeiroView> {
    }

    public static Factory factory() {
        return new Factory() {

            @Override
            public Widget create() {
                return new EnfermeiroView();
            }
        };
    }

    public EnfermeiroView() {
        initWidget(uiBinder.createAndBindUi(this));
        CListBoxEvent();
    }

    public void CListBoxEvent() {
        situacao.addItem("Ativos");
        situacao.addItem("Inativos");
        situacao.addItem("Todos");
        situacao.setValue("");
    }

    @UiHandler("pesquisar")
    void onPesquisar(ClickEvent event) {
    }
}
