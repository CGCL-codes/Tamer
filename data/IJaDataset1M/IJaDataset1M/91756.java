package com.softserveinc.train.locfind.rest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.softserveinc.train.locfind.entity.Location;

@Service("locationService")
@Transactional(propagation = Propagation.REQUIRED)
@Path("/locationservice")
@Produces({ "application/json" })
public class LocationService {

    @PersistenceContext(unitName = "locfind")
    private transient EntityManager em;

    @Transactional(readOnly = true)
    @GET
    @Path("/location")
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response getLocation(@FormParam("id") long id) {
        Location location = em.find(Location.class, id);
        if (location != null) {
            return Response.ok(location).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
