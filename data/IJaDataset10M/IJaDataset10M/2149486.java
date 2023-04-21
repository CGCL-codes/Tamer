package au.edu.uq.itee.eresearch.dimer.webapp.app.controller;

import java.net.URI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path(BaseResource.aggregationPathSuffix + "{path:/.*}")
public class AggregationResource extends BaseResource {

    @PathParam("path")
    private String path;

    @GET
    @Produces({ "text/html", "application/rdf+xml" })
    public Response getHTML() {
        return Response.seeOther(URI.create(getViewContext().getAppURL(path))).build();
    }
}
