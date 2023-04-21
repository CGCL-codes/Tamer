package com.esri.gpt.control.webharvest.client.waf;

import com.esri.gpt.catalog.harvest.protocols.HarvestProtocolWaf;
import com.esri.gpt.control.webharvest.IterationContext;
import com.esri.gpt.control.webharvest.common.CommonCapabilities;
import com.esri.gpt.framework.resource.api.Native;
import com.esri.gpt.framework.resource.query.Capabilities;
import com.esri.gpt.framework.resource.query.Criteria;
import com.esri.gpt.framework.resource.query.Query;
import com.esri.gpt.framework.resource.query.QueryBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WAF query builder.
 */
public class WafQueryBuilder implements QueryBuilder {

    /** logger */
    private static final Logger LOGGER = Logger.getLogger(WafQuery.class.getCanonicalName());

    /** capabilities */
    private static final Capabilities capabilities = new WafCommonCapabilities();

    /** iteration context */
    private IterationContext context;

    /** service info */
    private WafInfo info;

    /**
 * Creates instance of the builder.
 * @param context iteration context
 * @param protocol protocol
 * @param url url
 */
    public WafQueryBuilder(IterationContext context, HarvestProtocolWaf protocol, String url) {
        if (context == null) throw new IllegalArgumentException("No context provided.");
        this.context = context;
        this.info = new WafInfo(url, protocol.getUserName(), protocol.getUserPassword());
    }

    @Override
    public Capabilities getCapabilities() {
        return capabilities;
    }

    @Override
    public Query newQuery(Criteria crt) {
        WafProxy proxy = new WafProxy(info, crt);
        Query q = new WafQuery(context, info, proxy, crt);
        LOGGER.log(Level.FINER, "Query created: {0}", q);
        return q;
    }

    @Override
    public Native getNativeResource() {
        return null;
    }

    /**
 * WAF capabilities.
 */
    private static class WafCommonCapabilities extends CommonCapabilities {

        @Override
        public boolean canQueryFromDate() {
            return true;
        }

        @Override
        public boolean canQueryToDate() {
            return true;
        }
    }
}
