package com.tenline.pinecone.platform.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * @author Bill
 *
 */
@Path("/api/channel")
public interface ChannelService {

    /**
	 * 
	 * @param subject
	 * @param response
	 */
    @GET
    @Path("/subscribe/{subject}")
    public void subscribe(@PathParam("subject") String subject, @Context HttpServletRequest request, @Context HttpServletResponse response);

    /**
	 * 
	 * @param subject
	 * @param request
	 * @return
	 */
    @POST
    @Path("/publish/{subject}")
    public Response publish(@PathParam("subject") String subject, @Context HttpServletRequest request, @Context HttpServletResponse response);
}
