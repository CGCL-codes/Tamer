package org.kablink.teaming.remoting.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.kablink.teaming.rest.model.Subscription;

@Path("/subscription/{user_id}/{entity_type}/{entity_id}")
public class SubscriptionResource {

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Subscription getSubscription(@PathParam("user_id") long userId, @PathParam("entity_type") int entityType, @PathParam("entity_id") long entityId) {
        return null;
    }

    @PUT
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response putSubscription(@PathParam("user_id") long userId, @PathParam("entity_type") int entityType, @PathParam("entity_id") long entityId) {
        return null;
    }
}
