package com.cooldatasoft.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.cooldatasoft.data.Data;

@Path("/")
public class EchoService {

    @GET
    @Path("/echo/{message}")
    @Produces({ MediaType.APPLICATION_JSON, "application/*+json" })
    public Response echoService(@PathParam("message") String message) {
        Data d = new Data();
        d.setText(message);
        return Response.status(200).entity(d).build();
    }

    @GET
    @Path("favicon.ico")
    public void favicon() {
    }
}
