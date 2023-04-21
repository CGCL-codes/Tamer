package org.simpleframework.tapestry;

import org.apache.tapestry5.internal.TapestryAppInitializer;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.slf4j.Logger;

class ApplicationProvider {

    private final SymbolProvider provider;

    private final String name;

    private final Logger logger;

    public ApplicationProvider(ApplicationContext context) {
        this.provider = context.getSymbolProvider();
        this.logger = context.getLogger();
        this.name = context.getName();
    }

    public TapestryAppInitializer getInitializer() {
        return new TapestryAppInitializer(logger, provider, name, "simple");
    }
}
