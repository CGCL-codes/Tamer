package com.plexobject.docusearch.service.impl;

import java.io.File;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.validator.GenericValidator;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.plexobject.docusearch.http.RestClient;
import com.plexobject.docusearch.index.IndexPolicy;
import com.plexobject.docusearch.lucene.LuceneUtils;
import com.plexobject.docusearch.metrics.Metric;
import com.plexobject.docusearch.metrics.Timer;
import com.plexobject.docusearch.query.CriteriaBuilder;
import com.plexobject.docusearch.query.LookupPolicy;
import com.plexobject.docusearch.query.Query;
import com.plexobject.docusearch.query.QueryCriteria;
import com.plexobject.docusearch.service.SuggestionService;

@Path("/suggestions")
@Component("suggestionService")
@Scope("singleton")
public class SuggestionServiceImpl extends BaseSearchServiceImpl implements SuggestionService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
    @Path("/{index}")
    @Override
    public Response autocomplete(@PathParam("index") final String index, @QueryParam("q") final String keywords, @QueryParam("format") final String format, @DefaultValue("10") @QueryParam("limit") final int limit) {
        if (GenericValidator.isBlankOrNull(index)) {
            return Response.status(RestClient.CLIENT_ERROR_BAD_REQUEST).type("text/plain").entity("index not specified").build();
        }
        if (index.contains("\"")) {
            return Response.status(RestClient.CLIENT_ERROR_BAD_REQUEST).type("text/plain").entity("index name is valid " + index + "\n").build();
        }
        if (GenericValidator.isBlankOrNull(keywords)) {
            return Response.status(RestClient.CLIENT_ERROR_BAD_REQUEST).type("text/plain").entity("keywrods not specified").build();
        }
        final Timer timer = Metric.newTimer("SearchServiceImpl.query");
        try {
            IndexPolicy indexPolicy = configRepository.getIndexPolicy(index);
            LookupPolicy lookupPolicy = configRepository.getLookupPolicy(index);
            final String wild = "";
            final QueryCriteria criteria = new CriteriaBuilder().setKeywords(keywords.trim() + wild).build();
            final File dir = new File(LuceneUtils.INDEX_DIR, index);
            Query query = getQueryImpl(dir);
            List<String> results = query.partialLookup(criteria, indexPolicy, lookupPolicy, limit);
            String response = null;
            if (format != null && format.equals("line")) {
                StringBuilder sb = new StringBuilder();
                for (String word : results) {
                    sb.append(word + "\r\n");
                }
                response = sb.toString();
            } else {
                final JSONArray resultArray = new JSONArray();
                for (String result : results) {
                    resultArray.put(result);
                }
                response = resultArray.toString();
            }
            timer.stop("Found " + results + " hits for " + keywords + " on index " + index + ", limit " + limit);
            mbean.incrementRequests();
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.error("failed to autocomplete " + index + " with " + keywords + " limit " + limit, e);
            mbean.incrementError();
            return Response.status(RestClient.SERVER_INTERNAL_ERROR).type("text/plain").entity("failed to autocomplete " + index + " with '" + keywords + "' with limit " + limit + "\n").build();
        }
    }
}
