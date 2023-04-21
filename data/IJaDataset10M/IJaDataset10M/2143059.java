package org.soa4all.lpml.bpel;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.openrdf.repository.RepositoryException;
import eu.soa4all.wsl4j.ModellingException;
import eu.soa4all.wsl4j.rpc.MSMService;
import eu.soa4all.wsl4j.rpc.MessagePart;
import eu.soa4all.wsl4j.rpc.RPCOperation;

/**
 * @author Adrian Mos
 * 
 */
public class IServeDataProxy {

    public String getServiceName() {
        return serviceName;
    }

    public URI getDefinition() {
        return definition;
    }

    public String getInputLoweringSchema() {
        return inputLoweringSchema;
    }

    public String getOutputLiftingSchema() {
        return outputLiftingSchema;
    }

    private String serviceName = "";

    private URI definition = null;

    private String inputLoweringSchema = null;

    private String outputLiftingSchema = null;

    /**
	 * creates the object with the URI to iServe where data is stored
	 * @throws URISyntaxException
	 * @throws ModellingException
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public IServeDataProxy(URI uri) throws RepositoryException, IOException, ModellingException, URISyntaxException {
        MSMService service = MSMService.createFromIServe(uri, true);
        serviceName = service.getURI().toString().substring(service.getURI().toString().indexOf("(") + 1, service.getURI().toString().indexOf(")"));
        definition = new URI(service.getDefinitions().next().toString());
        RPCOperation operation = service.getOperations().next();
        org.ontoware.rdf2go.model.node.URI inputLoweringSchemaURI = ((MessagePart) operation.getInputDescription()).getDirectParts().next().getDirectParts().next().getLoweringSchema();
        if (null != inputLoweringSchemaURI) inputLoweringSchema = inputLoweringSchemaURI.toString();
        org.ontoware.rdf2go.model.node.URI outputLiftingSchemaURI = ((MessagePart) operation.getOutputDescription()).getDirectParts().next().getDirectParts().next().getLiftingSchema();
        if (null != outputLiftingSchemaURI) outputLiftingSchema = outputLiftingSchemaURI.toString();
    }
}
