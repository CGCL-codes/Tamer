package org.larozanam.arq.util;

public class Sucesso<Resultado> {

    private final Resultado resultado;

    private final String mensagem;

    public Sucesso(String mensagem, Resultado resultado) {
        this.resultado = resultado;
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Resultado getResultado() {
        return resultado;
    }
}
