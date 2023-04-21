package com.celiosilva.simbanc.controller.agencia;

import com.celiosilva.simbanc.view.AgenciaView;

/**
 *
 * @author celio@celiosilva.com
 */
public class VisualizarAgenciaController extends AgenciaView {

    @Override
    protected String getTitulo() {
        return "Visualizar Agência";
    }

    @Override
    protected void setEstadoInicial() {
        this.btnDesativar.setEnabled(false);
        this.btnReativar.setEnabled(false);
        this.btnSalvar.setEnabled(false);
        this.txtCNPJ.setEditable(false);
        this.txtEndereco.setEditable(false);
        this.txtNome.setEditable(false);
        this.cbxAgenciaAtiva.setEnabled(false);
    }
}
