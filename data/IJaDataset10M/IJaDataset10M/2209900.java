package org.arastreju.http.resources;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.arastreju.api.ontology.Namespace;
import org.arastreju.api.ontology.NamespaceService;
import org.arastreju.http.OntologySummary;
import de.lichtflut.infra.exceptions.NoLongerSupportedException;

/**
 * HTTP Resource for ontologies/extracts.
 * 
 * Created: 03.03.2009
 *
 * @author Oliver Tigges
 */
@Path("/ontologies")
@Produces("application/xml")
public class OntologyResource extends BaseResource {

    @GET
    public List<OntologySummary> index() {
        List<OntologySummary> summaries = new ArrayList<OntologySummary>();
        NamespaceService service = gate().lookupNamespaceService();
        List<Namespace> namespaces = service.findNamespaces();
        for (Namespace ns : namespaces) {
            summaries.add(new OntologySummary(ns));
        }
        return summaries;
    }

    @GET
    @Path("/{id}")
    public String getOntologyById(@PathParam("id") int id, @QueryParam(value = "revision") int revParam) {
        throw new NoLongerSupportedException();
    }
}
