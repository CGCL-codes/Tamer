package com.gft.larozanam.client.editores;

import com.gft.larozanam.client.componentes.CCheckBox;
import com.gft.larozanam.client.componentes.CDateBox;
import com.gft.larozanam.client.componentes.CTextBox;
import com.gft.larozanam.client.componentes.CTextArea;
import com.gft.larozanam.shared.entidades.EnfermagemIdoso;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class EnfermagemIdosoEditor extends Composite implements Editor<EnfermagemIdoso> {

    private static EnfermagemIdosoEditorUiBinder uiBinder = GWT.create(EnfermagemIdosoEditorUiBinder.class);

    interface EnfermagemIdosoEditorUiBinder extends UiBinder<Widget, EnfermagemIdosoEditor> {
    }

    public EnfermagemIdosoEditor() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    CDateBox dataInternacao;

    @UiField
    CTextBox quarto;

    @UiField
    CTextBox leito;

    @UiField
    CTextBox peso;

    @UiField
    CTextArea habitos;

    @UiField
    CTextArea motivoInternacao;

    @UiField
    CTextBox procedencia;

    @UiField
    CTextBox etnia;

    @UiField
    CTextBox medicamentos;

    @UiField
    CCheckBox pressaoAlta;

    @UiField
    CCheckBox fumante;

    @UiField
    CCheckBox independente;

    public EnfermagemIdosoEditor(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
