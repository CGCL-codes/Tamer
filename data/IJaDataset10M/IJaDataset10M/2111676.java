package org.gbif.checklistbank.ws.client;

import org.gbif.api.paging.Pageable;
import org.gbif.api.paging.PagingRequest;
import org.gbif.api.paging.PagingResponse;
import org.gbif.checklistbank.api.model.Reference;
import org.gbif.checklistbank.ws.util.Constants;
import java.util.LinkedList;
import java.util.List;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.gbif.api.paging.PagingConstants.PARAM_LIMIT;
import static org.gbif.api.paging.PagingConstants.PARAM_OFFSET;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceWsClientTest {

    @Mock
    private WebResource resource;

    private ReferenceWsClient client;

    private Pageable page;

    @Before
    public void setUp() {
        this.client = new ReferenceWsClient(resource);
        this.page = new PagingRequest(0l, 1);
    }

    @Test
    public void testListByUsage() {
        int key = 1111;
        when(resource.path(eq(Constants.NAME_USAGE_PATH + "/" + String.valueOf(key) + "/" + Constants.REFERENCES_PATH))).thenReturn(resource);
        when(resource.queryParam(PARAM_LIMIT, String.valueOf(page.getLimit()))).thenReturn(resource);
        when(resource.queryParam(PARAM_OFFSET, String.valueOf(page.getOffset()))).thenReturn(resource);
        List<Reference> references = new LinkedList<Reference>();
        Reference reference = new Reference();
        reference.setKey(1234);
        reference.setUsageKey(key);
        reference.setCitation("Citation string");
        references.add(reference);
        PagingResponse<Reference> response = new PagingResponse<Reference>(page, 1L, references);
        when(resource.get(client.getTPage())).thenReturn(response);
        client.listByUsage(key, page);
    }
}
