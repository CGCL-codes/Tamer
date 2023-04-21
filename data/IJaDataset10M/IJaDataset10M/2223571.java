package com.google.code.tinypmclient.internal.resource;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.ClientResponse;
import com.google.code.tinypmclient.UserStory;

public interface UserStories {

    @GET
    @Path("userstory/{id}")
    @Produces("application/xml")
    ClientResponse<UserStory> getUserStory(@PathParam("id") int id);

    @POST
    @Path("project/{projectId}/userstories")
    @Consumes("application/xml")
    Response.Status createUserStory(@PathParam("projectId") int projectId, UserStory userStory);

    @POST
    @Path("project/{projectId}/iteration/current/userstories")
    @Consumes("application/xml")
    Response.Status createUserStoryInCurrentIteration(@PathParam("projectId") int projectId, UserStory userStory);

    @DELETE
    @Path("userstory/{id}")
    @Consumes("application/xml")
    Response.Status deleteUserStory(@PathParam("id") int id);

    @PUT
    @Path("userstory/{id}")
    @Consumes("application/xml")
    Response.Status updateUserStory(@PathParam("id") int id, UserStory userStory);

    @GET
    @Path("project/{projectId}/userstories")
    @Produces("application/xml")
    ClientResponse<List<UserStory>> getAllUserStories(@PathParam("projectId") int projectId);
}
