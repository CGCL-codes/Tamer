package br.com.q10.service.exception;

/**
 *
 * @author rafael
 */
public class UsuarioNaoAutorizadoException extends RuntimeException {

    public UsuarioNaoAutorizadoException(Throwable thrwbl) {
        super(thrwbl);
    }

    public UsuarioNaoAutorizadoException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public UsuarioNaoAutorizadoException(String string) {
        super(string);
    }

    public UsuarioNaoAutorizadoException() {
    }
}
