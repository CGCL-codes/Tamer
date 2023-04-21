package it.pronetics.madstore.server.jaxrs.atom.pub.impl;

import it.pronetics.madstore.common.AtomConstants;
import it.pronetics.madstore.repository.util.PagingList;
import it.pronetics.madstore.server.HttpConstants;
import it.pronetics.madstore.server.jaxrs.atom.impl.AbstractResourceHandler;
import it.pronetics.madstore.server.jaxrs.atom.pub.CollectionResourceHandler;
import it.pronetics.madstore.server.jaxrs.atom.resolver.ResourceName;
import it.pronetics.madstore.server.jaxrs.atom.resolver.ResourceUriFor;
import java.net.URL;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Collection;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link it.pronetics.madstore.server.jaxrs.atom.pub.CollectionResourceHandler} implementation based on
 * JBoss Resteasy and Abdera atom model.
 * 
 * @author Sergio Bossa
 */
@Path("/")
public class DefaultCollectionResourceHandler extends AbstractResourceHandler implements CollectionResourceHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCollectionResourceHandler.class);

    private String collectionKey;

    private int maxNumberOfEntries;

    private int pageNumberOfEntries;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @ResourceUriFor(resource = ResourceName.COLLECTION)
    @GET
    @Path("/{collectionKey}")
    @Produces(AtomConstants.ATOM_MEDIA_TYPE)
    public Response getCollectionResource() {
        try {
            int max = maxNumberOfEntries;
            int offset = (pageNumberOfEntries - 1) * max;
            Collection collectionModel = readCollectionFromRepository(collectionKey);
            Factory abderaFactory = Abdera.getInstance().getFactory();
            Feed feed = abderaFactory.newFeed();
            PagingList<Entry> entries = readEntriesFromRepository(collectionKey, offset, max);
            configureFeed(feed, entries, collectionModel);
            Response response = buildOkResponse(feed);
            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @PathParam("collectionKey")
    public void setCollectionKey(String collectionKey) {
        this.collectionKey = collectionKey;
    }

    @QueryParam(HttpConstants.MAX_PARAMETER)
    @DefaultValue("10")
    public void setMaxNumberOfEntries(int maxNumberOfEntries) {
        this.maxNumberOfEntries = maxNumberOfEntries;
    }

    @QueryParam(HttpConstants.PAGE_PARAMETER)
    @DefaultValue("1")
    public void setPageNumberOfEntries(int pageNumberOfEntries) {
        this.pageNumberOfEntries = pageNumberOfEntries;
    }

    private void configureFeed(Feed feed, PagingList<Entry> entries, Collection collectionModel) throws Exception {
        URL selfUrl = resourceResolver.resolveResourceUriFor(ResourceName.COLLECTION, uriInfo.getBaseUri().toString(), collectionKey);
        URL nextUrl = UriBuilder.fromUri(selfUrl.toURI()).queryParam(HttpConstants.PAGE_PARAMETER, new Integer(pageNumberOfEntries + 1)).queryParam(HttpConstants.MAX_PARAMETER, maxNumberOfEntries).build().toURL();
        URL prevUrl = UriBuilder.fromUri(selfUrl.toURI()).queryParam(HttpConstants.PAGE_PARAMETER, new Integer(pageNumberOfEntries - 1)).queryParam(HttpConstants.MAX_PARAMETER, maxNumberOfEntries).build().toURL();
        String id = resourceResolver.resolveResourceIdFor(uriInfo.getBaseUri().toString(), ResourceName.COLLECTION, collectionKey);
        feed.setId(id);
        feed.addLink(selfUrl.toString(), "self");
        feed.setTitle(collectionModel.getTitle());
        feed.addAuthor(Abdera.getInstance().getFactory().newAuthor().getText());
        for (Entry entry : entries) {
            feed.addEntry(entry);
        }
        if (entries.size() > 0) {
            int currentLastResult = ((pageNumberOfEntries - 1) * maxNumberOfEntries) + entries.size();
            if (currentLastResult < entries.getTotal()) {
                feed.addLink(nextUrl.toString(), "next");
            }
            if (pageNumberOfEntries > 1) {
                feed.addLink(prevUrl.toString(), "previous");
            }
        }
    }
}
