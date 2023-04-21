package org.etexascode.transceiver.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class HelloService {

    @GET
    @Produces("text/plain")
    public String getHello() {
        return "Hello World!!! This is Jersey + Jetty!";
    }
}
