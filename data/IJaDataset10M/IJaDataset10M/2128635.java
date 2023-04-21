package org.aoplib4j.modularity;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation for the boundary violation. This class only logs 
 * the violation using the Java logging API.
 * 
 * @author Adrian Citu
 *
 */
public final class LogViolationCallback extends BoundaryViolationCallback {

    /**
     * The logger used to log the boundary violations.
     */
    private static final Logger LOGGER = Logger.getLogger(LogViolationCallback.class.getName());

    /**
     * The method will extract the information from the 
     * {@link ViolationInformation} and will log it.
     * 
     * {@inheritDoc}
     */
    @Override
    public void boundaryViolation(final ViolationInformation info) {
        StringBuffer errorMessage = new StringBuffer();
        errorMessage.append("It is forbidden to call the method ").append(info.getCalledClassName()).append("#").append(info.getCalledMethodName()).append(" from the class ").append(info.getCallerClassName());
        LOGGER.log(Level.WARNING, errorMessage.toString());
    }
}
