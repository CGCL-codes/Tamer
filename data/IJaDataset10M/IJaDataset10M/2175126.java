package com.ontotext.ordi.sar.server.handlers;

import java.util.Map;
import com.ontotext.ordi.sar.exception.SARException;

public interface EntityHandler {

    public static final String PARAM_OPERATION = "EntityHandler.operation";

    public static final String PARAM_ORDI_CONNECTION = "EntityHandler.TConnection";

    public static final String PARAM_ORDI_SOURCE = "EntityHandler.TSource";

    public static final String PARAM_PIPELINE = "EntityHandler.pipeline";

    public static final String PARAM_CONFIG = "EntityHandler.config";

    public static final String PARAM_STORAGE = "EntityHandler.storage";

    public static final String PARAM_TFACTORY = "EntityHandler.TFactory";

    public static final String OPERATION_STORE = "operation.store";

    public static final String OPERATION_LOAD = "operation.load";

    /**
	 * Entity was not handled.
	 */
    public static final int NOT_HANDLED = 0;

    /**
	 * Entity was handled; break the pipeline walk-trough.
	 */
    public static final int HANDLED_BREAK = 1;

    /**
	 * Entity was handled; continue the pipeline walk-trough.
	 */
    public static final int HANDLED_CONTINUE = 2;

    /**
	 * Handles (if possible) the entity supplied. Somewhat similar to the "visit" method from the visitor pattern.
	 *  
	 * @param entity	The entity to be handled.
	 * @param context	Arguments representing the context of the operation.
	 * @return 			The outcome of the operation. Currently, supported are  {@link #NOT_HANDLED NOT_HANDLED},
	 * 		{@link #HANDLED_BREAK HANDLED_BREAK} and {@link #HANDLED_CONTINUE HANDLED_CONTINUE}.
	 */
    int handle(Object entity, Map<Object, Object> context) throws SARException;
}
