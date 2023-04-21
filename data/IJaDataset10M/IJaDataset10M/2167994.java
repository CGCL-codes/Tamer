package org.gbif.checklistbank.ws.client;

import org.gbif.api.paging.PagingResponse;
import org.gbif.checklistbank.api.model.Distribution;
import org.gbif.checklistbank.api.service.DistributionService;
import org.gbif.checklistbank.ws.client.guice.NameUsageWs;
import org.gbif.checklistbank.ws.util.Constants;
import com.google.inject.Inject;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

/**
 * Client-side implementation to the DistributionService.
 * TODO: the verbatim model class has to be used once available
 */
public class DistributionWsClient extends NameUsageComponentBaseWsClient<Distribution, Distribution> implements DistributionService {

    @Inject
    public DistributionWsClient(@NameUsageWs WebResource resource) {
        super(Distribution.class, Distribution.class, new GenericType<PagingResponse<Distribution>>() {
        }, new GenericType<PagingResponse<Distribution>>() {
        }, resource, Constants.DISTRIBUTIONS_PATH);
    }
}
