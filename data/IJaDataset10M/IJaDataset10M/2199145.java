package org.openjf.exception;

/**
 * Exceção levantada quando não houver permissão para o usuário acessar 
 * um determinado recurso
 */
public class BoardPermissionException extends BoardRuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5185011112209254999L;

    public BoardPermissionException() {
    }

    public BoardPermissionException(String message) {
        super(message);
    }

    public BoardPermissionException(Throwable cause) {
        super(cause);
    }

    public BoardPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoardPermissionException(String message, Object args[], Throwable cause) {
        super(message, args, cause);
    }

    public BoardPermissionException(String message, Object arg0, Throwable cause) {
        super(message, new Object[] { arg0 }, cause);
    }

    public BoardPermissionException(String message, Object arg0, Object arg1, Throwable cause) {
        super(message, new Object[] { arg0, arg1 }, cause);
    }

    public BoardPermissionException(String message, Object arg0, Object arg1, Object arg2, Throwable cause) {
        super(message, new Object[] { arg0, arg1, arg2 }, cause);
    }

    public BoardPermissionException(String message, Object arg0, Object arg1, Object arg2, Object arg3, Throwable cause) {
        super(message, new Object[] { arg0, arg1, arg2, arg3 }, cause);
    }

    public BoardPermissionException(String message, Object args[]) {
        super(message, args, null);
    }

    public BoardPermissionException(String message, Object arg0) {
        super(message, new Object[] { arg0 });
    }

    public BoardPermissionException(String message, Object arg0, Object arg1) {
        super(message, new Object[] { arg0, arg1 });
    }

    public BoardPermissionException(String message, Object arg0, Object arg1, Object arg2) {
        super(message, new Object[] { arg0, arg1, arg2 });
    }

    public BoardPermissionException(String message, Object arg0, Object arg1, Object arg2, Object arg3) {
        super(message, new Object[] { arg0, arg1, arg2, arg3 });
    }
}
