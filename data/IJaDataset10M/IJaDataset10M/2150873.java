package br.com.helbert.financeira.apresentacao.form.teste;

import org.apache.struts.action.ActionForm;

@SuppressWarnings("serial")
public class TesteForm extends ActionForm {

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
