package com.plexobject.docusearch.service.impl;

import java.io.File;
import java.util.Collection;
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
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.plexobject.docusearch.converter.Converters;
import com.plexobject.docusearch.http.RestClient;
import com.plexobject.docusearch.index.IndexPolicy;
import com.plexobject.docusearch.lucene.LuceneUtils;
import com.plexobject.docusearch.metrics.Metric;
import com.plexobject.docusearch.metrics.Timer;
import com.plexobject.docusearch.query.CriteriaBuilder;
import com.plexobject.docusearch.query.Query;
import com.plexobject.docusearch.query.QueryCriteria;
import com.plexobject.docusearch.query.QueryPolicy;
import com.plexobject.docusearch.query.RankedTerm;
import com.plexobject.docusearch.service.SearchAdminService;

@Path("/admin")
@Component("searchAdminService")
@Scope("singleton")
public class SearchAdminServiceImpl extends BaseSearchServiceImpl implements SearchAdminService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ MediaType.WILDCARD })
    @Path("/explain/search/{index}")
    @Override
    public Response explainSearch(@PathParam("index") final String index, @QueryParam("owner") final String owner, @QueryParam("q") final String keywords, @QueryParam("zipCode") final String zipCode, @QueryParam("city") final String city, @QueryParam("state") final String state, @QueryParam("country") final String country, @QueryParam("region") final String region, @DefaultValue("50") @QueryParam("radius") final float radius, @QueryParam("sort") final String sortBy, @DefaultValue("true") @QueryParam("asc") final boolean sortAscending, @DefaultValue("0") @QueryParam("start") final int start, @DefaultValue("20") @QueryParam("limit") final int limit) {
        if (GenericValidator.isBlankOrNull(index)) {
            return Response.status(RestClient.CLIENT_ERROR_BAD_REQUEST).type("text/plain").entity("index not specified").build();
        }
        if (index.contains("\"")) {
            return Response.status(RestClient.CLIENT_ERROR_BAD_REQUEST).type("text/plain").entity("index name is valid " + index + "\n").build();
        }
        if (GenericValidator.isBlankOrNull(keywords)) {
            return Response.status(RestClient.CLIENT_ERROR_BAD_REQUEST).type("text/plain").entity("keywrods not specified").build();
        }
        final Timer timer = Metric.newTimer("SearchServiceImpl.explain");
        try {
            IndexPolicy indexPolicy = configRepository.getIndexPolicy(index);
            QueryPolicy queryPolicy = configRepository.getQueryPolicy(index);
            final CriteriaBuilder criteriaBuilder = new CriteriaBuilder().setKeywords(keywords).setOwner(owner);
            if (!GenericValidator.isBlankOrNull(zipCode)) {
                criteriaBuilder.setZipcode(zipCode);
                double[] latLongs = spatialLookup.getLatitudeAndLongitude(zipCode);
                criteriaBuilder.setLatitude(latLongs[0]);
                criteriaBuilder.setLongitude(latLongs[1]);
            }
            criteriaBuilder.setCity(city);
            criteriaBuilder.setState(state);
            criteriaBuilder.setCountry(country);
            criteriaBuilder.setRegion(region);
            criteriaBuilder.setRadius(radius);
            criteriaBuilder.setSortBy(sortBy, sortAscending);
            final QueryCriteria criteria = criteriaBuilder.build();
            final File dir = new File(LuceneUtils.INDEX_DIR, index);
            final Query query = getQueryImpl(dir);
            final Collection<String> results = query.explainSearch(criteria, indexPolicy, queryPolicy, start, limit);
            final JSONArray response = new JSONArray();
            for (String result : results) {
                response.put(result);
            }
            timer.stop("Explanationfor " + keywords + " on index " + index + ", start " + start + ", limit " + limit);
            mbean.incrementRequests();
            return Response.ok(response.toString()).build();
        } catch (Exception e) {
            LOGGER.error("failed to query " + index + " with " + keywords + " from " + start + "/" + limit, e);
            mbean.incrementError();
            return Response.status(RestClient.SERVER_INTERNAL_ERROR).type("text/plain").entity("failed to query " + index + " with " + keywords + " from " + start + "/" + limit + "\n").build();
        }
    }

    /**
     * 
     * @param index
     * @param numTerms
     * @return JSONArray with top ranking terms
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ MediaType.TEXT_HTML, MediaType.APPLICATION_JSON })
    @Path("/rank/{index}")
    @Override
    public Response getTopRankingTerms(@PathParam("index") final String index, @DefaultValue("200") @QueryParam("limit") final int numTerms) {
        if (GenericValidator.isBlankOrNull(index)) {
            return Response.status(RestClient.CLIENT_ERROR_BAD_REQUEST).type("text/plain").entity("index not specified").build();
        }
        if (index.contains("\"")) {
            return Response.status(RestClient.CLIENT_ERROR_BAD_REQUEST).type("text/plain").entity("index name is valid " + index + "\n").build();
        }
        final Timer timer = Metric.newTimer("SearchServiceImpl.ranking");
        try {
            QueryPolicy policy = configRepository.getQueryPolicy(index);
            final File dir = new File(LuceneUtils.INDEX_DIR, index);
            Query query = getQueryImpl(dir);
            Collection<RankedTerm> results = query.getTopRankingTerms(policy, numTerms);
            JSONArray response = new JSONArray();
            for (RankedTerm result : results) {
                JSONObject jsonDoc = Converters.getInstance().getConverter(RankedTerm.class, JSONObject.class).convert(result);
                response.put(jsonDoc);
            }
            timer.stop();
            mbean.incrementRequests();
            return Response.ok(response.toString()).build();
        } catch (Exception e) {
            LOGGER.error("failed to get top ranks for " + index, e);
            mbean.incrementError();
            return Response.status(RestClient.SERVER_INTERNAL_ERROR).type("text/plain").entity("failed to top rank for " + index + "\n").build();
        }
    }
}
