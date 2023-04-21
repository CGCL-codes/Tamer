package org.jboss.resteasy.springmvc.test.resources;

import java.util.Date;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import org.jboss.resteasy.springmvc.test.jaxb.BasicJaxbObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Path("/basic")
@Component
public class BasicResourceImpl {

    @Context
    private HttpHeaders headers;

    @Context
    private UriInfo uri;

    /**
    * really simple test
    */
    @GET
    @Produces("text/plain")
    public String getBasicString() {
        return "test";
    }

    @GET
    @Produces("application/xml")
    @Path("object")
    public BasicJaxbObject getBasicObject() {
        return new BasicJaxbObject("something", new Date());
    }

    /**
    * WOOHOO!  SpringMVC ModelAndView in action
    */
    @GET
    @Produces("application/custom")
    @Path("/custom-rep")
    public ModelAndView getCustomRepresentation() {
        return new ModelAndView("myCustomView");
    }

    /** */
    @GET
    @Produces("text/plain")
    @Path("/header")
    public String getContentTypeHeader() {
        return this.headers.getAcceptableMediaTypes().get(0).toString();
    }

    /**
    * the dao knows the path via an @Context inject value
    */
    @GET
    @Produces("text/plain")
    @Path("/url")
    public String getURL() {
        return uri.getPath();
    }
}
