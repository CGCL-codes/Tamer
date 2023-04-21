package org.isurf.spmbl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * Responsible for handing errors which occur during the execution of workflow. ErrorHandler will attempt to execute the defined error strategy 
 * for the particular Exception which was thrown. Client objects are responsible for session management.
 */
public class ErrorHandler {

    public static final String DEFAULT_ERROR_PROCESS = "org.isurf.spmbl.exceptions.LoggingErrorStrategy";

    public static final int DEFAULT_ERROR_RETRIES = 5;

    public static final String DEFAUT_ERROR_HANDLER = "DEFAUT_ERROR_HANDLER";

    private static Logger logger = Logger.getLogger(ErrorHandler.class);

    private Map<String, ErrorStrategy> errorStrategies;

    private String client;

    private boolean rethrowExceptions;

    private ErrorStrategy defaultStrategy;

    private Map<String, Integer> strategyExecutions = new HashMap<String, Integer>();

    /**
	 * Constructs an {@link ErrorHandler}.
	 * 
	 * @param client The name of the object invoking the {@link ErrorHandler}.
	  */
    public ErrorHandler(String client) {
        logger.info("ErrorHandler: client = " + client);
        this.client = client;
    }

    /**
	 * Constructs an {@link ErrorHandler}.
	 * 
	 * @param client The name of the object invoking the {@link ErrorHandler}.
	  * @param errorProcess The ID the ruleflow represent the error strategy.
	 */
    public ErrorHandler(String client, Map<String, ErrorStrategy> errorStrategies) {
        logger.info("ErrorHandler: client = " + client + "; errorStrategies = " + errorStrategies);
        this.client = client;
        this.errorStrategies = errorStrategies;
    }

    /**
	 * Handles the exception by invoking the defined error process. Clients are responsible for disposing of the
	 * session.
	 * 
	 * @param errorMessage
	 * @param cause
	 */
    public void handle(Serializable subject, String errorMessage, Throwable cause, StatefulKnowledgeSession session) {
        logger.info("handle: invoking error strategy for client " + client + ": error caused by " + errorMessage, cause);
        ErrorStrategy strategy = getStrategy(cause);
        String strategyType = cause.getClass().getName();
        strategy.execute(subject, errorMessage, cause, session, 0);
    }

    private ErrorStrategy getStrategy(Throwable cause) {
        ErrorStrategy strategy = null;
        if (errorStrategies != null) {
            strategy = errorStrategies.get(cause.getClass().getName());
        }
        if (strategy == null) {
            if (defaultStrategy == null) {
                defaultStrategy = new ErrorStrategy(DEFAULT_ERROR_PROCESS, 5, client);
            }
            strategy = defaultStrategy;
        }
        return strategy;
    }
}
