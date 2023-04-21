package org.jwaim.core.interfaces;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.jwaim.core.ModuleCommunicationLine;
import org.jwaim.core.logger.JWAIMLogger;

/**
 * This is abstract class which should be used as base for all the exception-filters in this
 * monitoring system. ExceptionFilters are filters which are run after request has finished to
 * analyze any exception which was not captured at the application layer
 */
public abstract class ExceptionModule extends Module {

    protected ExceptionModule(ModuleCommunicationLine communicationLine, JWAIMLogger logger) {
        super(communicationLine, logger);
    }

    /**
	 * Performs filtering to given response
	 * @param request request object
	 * @param exception exception which was thrown
	 * @param attributes attributes set by other filters
	 * @return Exception which was left after this handling/analysis/masking or null of no further need to throw exception
	 */
    public abstract Exception filter(HttpServletRequest request, Exception exception, Map<String, Object> attributes);

    @Override
    public void init(Properties propertiesConfiguration) throws IOException {
        super.init(propertiesConfiguration);
    }

    @Override
    public final boolean isExceptionModule() {
        return true;
    }
}
