package uk.ac.ebi.intact.editor.ws;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
@Path("/mi")
public interface MiExportService {

    String FORMAT_XML254 = "xml254";

    String FORMAT_MITAB25 = "tab25";

    String FORMAT_MITAB25_INTACT = "tab25-intact";

    String FORMAT_HTML = "html";

    String FORMAT_JSON = "json";

    String FORMAT_GRAPHML = "graphml";

    @GET
    @Path("/publication")
    Object exportPublication(@QueryParam("ac") String id, @DefaultValue("tab25") @QueryParam("format") String format);

    @GET
    @Path("/experiment")
    Object exportExperiment(@QueryParam("ac") String id, @DefaultValue("tab25") @QueryParam("format") String format);

    @GET
    @Path("/interaction")
    Object exportInteraction(@QueryParam("ac") String id, @DefaultValue("tab25") @QueryParam("format") String format);
}
