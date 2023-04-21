package org.kablink.teaming.remoting.rest.resource;

import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.kablink.teaming.rest.model.Team;
import org.kablink.teaming.rest.model.User;

@Path("/users")
public class UsersResource {

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<User> getUsers(@QueryParam("offset") Integer offset, @QueryParam("maxcount") Integer maxCount) {
        return null;
    }

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void createUser() {
    }

    @GET
    @Path("byemail")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<User> getUsersByEmail(@QueryParam("email_address") String emailAddress, @QueryParam("email_type") String emailType) {
        return null;
    }

    @GET
    @Path("user")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User getUserByName(@QueryParam("name") String name) {
        return null;
    }

    @GET
    @Path("user/{id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User getUser(@PathParam("id") Long id) {
        return null;
    }

    @PUT
    @Path("user/{id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void updateUser(@PathParam("id") long id) {
    }

    @DELETE
    @Path("user/{id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void deleteUser(@PathParam("id") long id) {
    }

    @PUT
    @Path("user/{id}/personal_workspace")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void addPersonalWorkspace(@PathParam("id") long id) {
    }

    @POST
    @Path("user/{id}/password")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void changePassword(@PathParam("id") long id) {
    }

    @GET
    @Path("user/{id}/favorites")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void getFavorites(@PathParam("id") long id) {
    }

    @GET
    @Path("user/{id}/teams")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public List<Team> getTeams(@PathParam("id") long id) {
        return null;
    }
}
