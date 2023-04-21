package com.softserveinc.train.locfind.rest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.softserveinc.train.locfind.entity.Location;
import com.softserveinc.train.locfind.entity.Sample;
import com.softserveinc.train.locfind.entity.User;

@Service("sampleService")
@Transactional(propagation = Propagation.REQUIRED)
@Path("/sampleservice")
@Produces({ "application/json" })
public class SampleService {

    @PersistenceContext(unitName = "locfind")
    private transient EntityManager em;

    @Transactional(readOnly = true)
    @POST
    @Path("/sample")
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response getSample(@FormParam("cmd") String cmd, @FormParam("email") String email, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName) {
        return Response.ok(new SampleResult(new Sample("Hello from CXF"))).build();
    }

    @GET
    @Path("/repopulate")
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response repopulate() {
        em.createQuery("delete from Location").executeUpdate();
        em.createQuery("delete from User").executeUpdate();
        User user1 = new User("jdoe", "John", "Doe", "jdoe@example.com");
        User user2 = new User("pbull", "Pitt", "Bull", "pbull@example.com");
        em.persist(user1);
        em.persist(user2);
        em.persist(new Location(10d, 15d, "my", "home", user1));
        em.persist(new Location(30d, 20d, "my", "office", user1));
        em.persist(new Location(15d, 12d, "food", "PizzaCap", null));
        return Response.ok().build();
    }

    @GET
    @Path("/testcookie")
    @Produces("text/plain")
    public String getTestCookieValue(@CookieParam("testcookie") String cookieValue) {
        return cookieValue;
    }
}
