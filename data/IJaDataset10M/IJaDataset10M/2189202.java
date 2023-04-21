package com.celiosilva.simbanc.business.util;

import com.celiosilva.simbanc.controller.util.Mensagem;

/**
 *
 * @author celio@celiosilva.com
 */
public class Sucesso implements Mensagem {

    private final String mensagem;

    public Sucesso(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return this.mensagem;
    }

    @Override
    public String getMensagem() {
        return this.mensagem;
    }
}
