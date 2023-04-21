package br.com.xp.galera.exception;

public class UsuarioInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public UsuarioInvalidoException() {
        super();
    }

    public UsuarioInvalidoException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public UsuarioInvalidoException(String arg0) {
        super(arg0);
    }

    public UsuarioInvalidoException(Throwable arg0) {
        super(arg0);
    }
}
