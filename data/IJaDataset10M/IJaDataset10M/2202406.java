package org.yass.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.Library;
import org.yass.domain.User;
import org.yass.rest.representations.LibraryRepresentation;
import org.yass.util.DiskScannerThread;

/**
 * @author Sven Duzont
 * 
 */
@Path("/libraries")
public class LibraryResource extends YassResource {

    /**
     *
     */
    public static final Log LOG = LogFactory.getLog(LibraryResource.class);

    /**
	 * 
	 * @param userId
	 * @return
	 */
    @GET
    public Response getLibraries() {
        final User user = retrieveUser();
        final Collection<Library> libs = user.getLibraries();
        if (libs.isEmpty()) failNotFound("User have no libraries");
        return Response.ok("Found Libraries").build();
    }

    /**
	 * 
	 * @param userId
	 * @return
	 */
    @GET
    @Path("{libraryId}")
    public LibraryRepresentation getLibrary(@PathParam("libraryId") final int libraryId) {
        return new LibraryRepresentation(retrieveLibrary(libraryId));
    }

    /**
	 * 
	 * @param userId
	 * @return
	 * @throws URISyntaxException
	 */
    @POST
    public Response postLibrary(@FormParam("path") final String path) throws URISyntaxException {
        final User user = retrieveUser();
        final Library library = new Library();
        library.setPath(path);
        library.setUser(user);
        LIBRARY_DAO.save(library);
        user.setLibrary(library);
        USER_DAO.save(user);
        new DiskScannerThread(library.getId()).start();
        LOG.info("Saved Library id:" + library.getId());
        return Response.created(new URI("/libraries/" + library.getId())).build();
    }
}
