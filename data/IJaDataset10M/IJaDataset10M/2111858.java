package com.celiosilva.simbanc.beans;

/**
 *
 * @author celio@celiosilva.com
 */
public class ClienteFisico extends Cliente {

    private String cpf;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String getNomeCompleto() {
        return this.getNome() + " " + this.getSobrenome();
    }
}
