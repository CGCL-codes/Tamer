package com.tenline.pinecone.platform.web.service;

import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.tenline.pinecone.platform.model.Category;

/**
 * @author Bill
 *
 */
@Path("/api/category")
public interface CategoryService extends AbstractService {

    /**
	 * 
	 * @param category
	 * @return
	 */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Category create(Category category);

    /**
	 * 
	 * @param category
	 * @return
	 */
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Category update(Category category);

    /**
	 * 
	 * @param filter
	 * @return
	 */
    @GET
    @Path("/show/{filter}")
    @Produces(MediaType.APPLICATION_JSON)
    Collection<Category> show(@PathParam("filter") String filter);
}
