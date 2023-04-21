package net.sourceforge.x360mediaserve.plugins.wicketUI.impl.wicket;

import net.sourceforge.x360mediaserve.plugins.wicketUI.osgi.X360MSServices;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X360MSWicketUIFilter extends WicketFilter {

    Logger logger = LoggerFactory.getLogger(X360MSWicketUIFilter.class);

    X360MSServices x360MSServices;

    public X360MSWicketUIFilter(X360MSServices wicketUIConfig) {
        super();
        this.x360MSServices = wicketUIConfig;
    }

    @Override
    protected IWebApplicationFactory getApplicationFactory() {
        logger.info("Getting factory");
        return new X360MSWicketUIFactory(x360MSServices);
    }
}
