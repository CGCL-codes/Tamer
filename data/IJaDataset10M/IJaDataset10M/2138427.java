package vn.ducquoc.cxf;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoRest {

    private static final Logger LOG = LoggerFactory.getLogger(TodoRest.class);

    private TodoManager _backendService = new TodoManager();

    @GET
    @Path("/todo")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TodoItem> getTodoItems() {
        LOG.error("111 ");
        return _backendService.findAll();
    }

    @GET
    @Path("/todo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TodoItem getTodoItem(@PathParam("id") Long id) {
        LOG.error("222 ");
        TodoItem retval = _backendService.findById(id);
        return retval;
    }

    @POST
    @Path("/todo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TodoItem createTodoItem(TodoItem request) {
        _backendService.save(request);
        return request;
    }

    @POST
    @Path("/todo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, "multipart/mixed" })
    public Response createMultipartTodoItem(@FormParam("id") String id, @FormParam("importantLevel") String importantLevel, @FormParam("note") String note) {
        try {
            Long.parseLong(String.valueOf(id));
        } catch (NumberFormatException ex) {
            return Response.status(200).entity(getTodoItems()).build();
        }
        TodoItem request = _backendService.findById(Long.valueOf(id));
        if (request == null) {
            request = new TodoItem();
            request.setId(99L);
        }
        request.setImportantLevel("IMPORTANT");
        request.setNote("borrow some $$$ to survive this winter");
        _backendService.save(request);
        return Response.status(201).entity(request).build();
    }
}
