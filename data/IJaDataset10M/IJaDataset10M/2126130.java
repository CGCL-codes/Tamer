package com.celiosilva.simbanc.teste;

import java.util.Date;

/**
 * @author celio@celiosilva.com
 */
public class TiposDeVariaveis {

    static Object objeto;

    static int TIPO_CLIENTE;

    Object objetoInstancia;

    Date data;

    final String mensagemErro;

    public TiposDeVariaveis() {
        this.mensagemErro = "Alguma mensagem de erro aqui";
    }

    public void depositarDinheiro(double total) {
        int numeroDepositosOcorridos = 0;
    }
}
