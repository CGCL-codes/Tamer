package restful.servlet;

import javax.ws.rs.*;

@Path("/{root}")
public class ExtendedFolderResource extends FolderResource {

    @POST
    public String createExtendedFolder() {
        return "createExtendedFolder";
    }

    @DELETE
    @Path("{name:.*}")
    public String deleteExtendedFolder() {
        return "deleteExtendedFolder";
    }

    @GET
    @Path("{name:.*}")
    public String readExtendedFolder() {
        return "readExtendedFolder";
    }

    @PUT
    @Path("{name:.*}")
    public String updateExtendedFolder() {
        return "updateExtendedFolder";
    }
}
