package br.com.cinefilmes.exception;

import java.util.List;

public class ValidacaoException extends RuntimeException {

    private List<String> mensagens;

    public ValidacaoException(String string) {
        super(string);
    }

    public ValidacaoException(String string, Exception e) {
        super(string, e);
    }

    public ValidacaoException(Exception e) {
        super(e);
    }

    public ValidacaoException(List<String> mensagens) {
        this.mensagens = mensagens;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public List<String> getMensagens() {
        return mensagens;
    }
}
