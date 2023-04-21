package org.jw.web.rdc.view.exceptions;

import org.jw.web.rdc.exceptions.DefaultException;
import org.jw.web.rdc.exceptions.IExceptionConstants;

/**
 * @author Edwin Meira de Alc�ntara Costa
 *
 */
public class ViewException extends DefaultException implements IExceptionConstants {

    /**
	 * Atributo de servi�o � nulo ou n�o foi definido.
	 */
    public static final String SERVICE_ATTRIBUTE_INVALID = VIEW_ERROR_PREFIX + "service.attribute.null";

    /**
	 * N�o existe inst�ncia para o objeto de neg�cio (VO).
	 */
    public static final String BUSINESS_VO_INVALID = VIEW_ERROR_PREFIX + "business.vo.null";

    /**
	 * @param message
	 * @param code
	 */
    public ViewException(String message, String code) {
        super(message, code);
    }

    /**
	 * @param message
	 * @param cause
	 * @param code
	 */
    public ViewException(String message, Throwable cause, String code) {
        super(message, cause, code);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public ViewException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param message
	 */
    public ViewException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 * @param code
	 */
    public ViewException(Throwable cause, String code) {
        super(cause, code);
    }

    /**
	 * @param cause
	 */
    public ViewException(Throwable cause) {
        super(cause);
    }
}
