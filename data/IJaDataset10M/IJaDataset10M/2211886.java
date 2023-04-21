package uk.org.ogsadai.rest.files;

import java.io.File;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * REST resource for a file. This assumes that the REST components are
 * running in a servlet container which provides a 
 * {@link javax.servlet.ServletContext} which Jersey can access.
 *
 * @author The OGSA-DAI Project Team.
 */
public class RESTFile {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010.";

    /** File. */
    private File mFile;

    /**
     * Constructor.
     * 
     * @param file
     *     File.
     */
    public RESTFile(File file) {
        mFile = file;
    }

    /**
     * Gets file contents.
     * 
     * @return file contents.
     */
    @GET
    @Produces("*/*")
    public Response getFile(@Context ServletContext context) {
        String mimeType = context.getMimeType(mFile.toString());
        return Response.ok(mFile, mimeType).build();
    }

    /**
     * Deletes file.
     */
    @DELETE
    public void deleteFile() {
        mFile.delete();
    }
}
