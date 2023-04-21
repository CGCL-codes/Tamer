package org.kablink.teaming.remoting.rest.v1.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.kablink.teaming.rest.v1.model.Tag;

@Path("/tag/{id}")
public class TagResource extends AbstractResource {

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Tag getTag(@PathParam("id") String id) {
        return null;
    }

    @DELETE
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void deleteTag(@PathParam("id") String id) {
    }
}
