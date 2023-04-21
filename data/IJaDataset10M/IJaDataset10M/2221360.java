package org.impalaframework.facade;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.bootstrap.ContextLocationResolver;
import org.impalaframework.bootstrap.SimpleContextLocationResolver;
import org.impalaframework.bootstrap.SimpleLocationsRetriever;
import org.impalaframework.config.SimplePropertiesLoader;
import org.impalaframework.util.InstantiationUtils;

public class BootstrappingOperationFacade extends BaseOperationsFacade {

    private static final Log logger = LogFactory.getLog(BootstrappingOperationFacade.class);

    @Override
    protected List<String> getBootstrapContextLocations() {
        final String defaultResourceName = getDefaultResourceName();
        final String string = getContextLocationResolverClassName();
        SimpleLocationsRetriever retriever = new SimpleLocationsRetriever(getContextLocationResolver(string), new SimplePropertiesLoader(defaultResourceName));
        return retriever.getContextLocations();
    }

    protected String getContextLocationResolverClassName() {
        return SimpleContextLocationResolver.class.getName();
    }

    private ContextLocationResolver getContextLocationResolver(String className) {
        ContextLocationResolver c = InstantiationUtils.instantiate(className);
        logger.info("Using " + ContextLocationResolver.class.getSimpleName() + ": " + c.getClass().getName());
        return c;
    }

    protected String getDefaultResourceName() {
        return "impala-interactive.properties";
    }
}
