package com.esri.gpt.catalog.harvest.protocols;

import com.esri.gpt.catalog.harvest.clients.HRArcGisClient;
import com.esri.gpt.catalog.harvest.clients.HRClient;
import com.esri.gpt.catalog.harvest.clients.exceptions.HRInvalidProtocolException;
import com.esri.gpt.control.webharvest.IterationContext;
import com.esri.gpt.control.webharvest.client.res.ResourceQueryBuilder;
import com.esri.gpt.framework.resource.query.QueryBuilder;

/**
 * RES harvest protocol.
 */
public class HarvestProtocolResource extends AbstractHTTPHarvestProtocol {

    @Override
    public ProtocolType getType() {
        return ProtocolType.RES;
    }

    @Override
    public HRClient getClient(String hostUrl) throws HRInvalidProtocolException {
        return new HRArcGisClient(hostUrl);
    }

    public QueryBuilder newQueryBuilder(IterationContext context, String url) {
        return new ResourceQueryBuilder(context, this, url);
    }
}
