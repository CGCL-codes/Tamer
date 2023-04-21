package uy.gub.imm.sae.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 6669794637409153811L;

    public BusinessException(String codigoError) {
        super(codigoError);
    }

    public BusinessException(String codigoError, String mensaje) {
        super(codigoError, mensaje);
    }

    public BusinessException(String codigoError, Throwable cause) {
        super(codigoError, cause);
    }

    public BusinessException(String codigoError, String message, Throwable cause) {
        super(codigoError, message, cause);
    }
}
