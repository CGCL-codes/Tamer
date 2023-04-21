package com.odontosis.view.popup;

import java.util.Arrays;
import java.util.List;
import com.odontosis.entidade.SituacaoPaciente;
import com.odontosis.view.OdontosisForm;

@SuppressWarnings("serial")
public class FormPacientePesquisa extends OdontosisForm {

    private String nomePaciente;

    private String numeroPaciente;

    private String situacaoPaciente;

    private String maxResultado = "50";

    public List<SituacaoPaciente> getColecaoSituacaoPaciente() {
        return Arrays.asList(SituacaoPaciente.values());
    }

    public String getMaxResultado() {
        return maxResultado;
    }

    public void setMaxResultado(String maxResultado) {
        this.maxResultado = maxResultado;
    }

    public String getNumeroPaciente() {
        return numeroPaciente;
    }

    public void setNumeroPaciente(String numeroPaciente) {
        this.numeroPaciente = numeroPaciente;
    }

    public String getSituacaoPaciente() {
        return situacaoPaciente;
    }

    public void setSituacaoPaciente(String situacaoPaciente) {
        this.situacaoPaciente = situacaoPaciente;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }
}
