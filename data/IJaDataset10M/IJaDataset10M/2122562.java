package org.brandao.brutos;

/**
 * Lançada quando ocorre algum problema na configuração e execução
 * da aplicação.
 * 
 * @author Afonso Brandao
 */
public class BrutosException extends RuntimeException {

    public BrutosException() {
        super();
    }

    public BrutosException(String message) {
        super(message);
    }

    public BrutosException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrutosException(Throwable cause) {
        super(cause);
    }
}
