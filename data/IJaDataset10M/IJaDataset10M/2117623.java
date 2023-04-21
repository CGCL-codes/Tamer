package br.uff.javaavancado.exception.model;

import java.util.List;

public class AplicacaoException extends Exception {

    private static final long serialVersionUID = 1L;

    private List<String> mensagens;

    public AplicacaoException() {
    }

    public AplicacaoException(String msg) {
        super(msg);
    }

    public AplicacaoException(Throwable t) {
        super(t);
    }

    public AplicacaoException(List<String> mensagens) {
        this.mensagens = mensagens;
    }

    public List<String> getMensagens() {
        return mensagens;
    }
}
