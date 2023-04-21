package de.dgrid.bisgrid.secure.proxy.wsdl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import org.codehaus.xfire.wsdl.WSDLWriter;
import org.codehaus.xfire.wsdl11.DefinitionWSDL;

/**
 * The CopyWSDLWriter reads a WSDL of an URL (WorkflowURL) and a
 * adds the SOAP action string.
 * 
 * This is done by adding the operations of the {@link Definition} of BPEL
 * Workflow to the PortType of the Workflow Service.
 * 
 * @author Christoph Hohenwarter - 19.11.2009
 * 
 */
public class CopyWSDLWriter implements WSDLWriter {

    private DefinitionWSDL combinedDefinitionWSDL;

    private Definition serviceDef;

    @SuppressWarnings("unused")
    private Map<String, String> importNamespaces;

    protected WSDLFactory wsdlFactory;

    protected ExtensionRegistry extensionRegistry;

    public CopyWSDLWriter(String workflowURL) throws Exception {
        wsdlFactory = WSDLFactory.newInstance();
        extensionRegistry = wsdlFactory.newPopulatedExtensionRegistry();
        copyWSDL(workflowURL);
    }

    /**
	 * create the new WSDL by just copying the WSDL and adding the SOAP-action string
	 * 
	 * @param wsdlURL
	 *            URL of the wsdl-file to copy
	 * @throws Exception
	 */
    private void copyWSDL(String wsdlURL) throws Exception {
        WSDLReader reader = wsdlFactory.newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", true);
        reader.setFeature("javax.wsdl.importDocuments", true);
        String workflowEndpointURI = wsdlURL;
        de.dgrid.bisgrid.secure.proxy.wsdl.WSDLReader bisgridReader = new de.dgrid.bisgrid.secure.proxy.wsdl.WSDLReader();
        Definition defWorkflow = bisgridReader.readWSDL(workflowEndpointURI + "?wsdl");
        Iterator<?> defWorkflowImports = defWorkflow.getImports().keySet().iterator();
        String baseURL = workflowEndpointURI.substring(0, workflowEndpointURI.lastIndexOf("/"));
        while (defWorkflowImports.hasNext()) {
            String namespace = (String) defWorkflowImports.next();
            List<?> importsForNS = defWorkflow.getImports(namespace);
            for (Object imp : importsForNS) {
                String location = ((Import) imp).getLocationURI();
                System.out.println(location);
                if (location != null && !location.startsWith("http")) {
                    String newLocation = baseURL + "/" + location;
                    ((Import) imp).setLocationURI(newLocation);
                }
            }
        }
        this.importNamespaces = new java.util.Hashtable<String, String>();
        this.importNamespaces = bisgridReader.getImportNamespaces();
        combinedDefinitionWSDL = new DefinitionWSDL(defWorkflow);
    }

    public void write(OutputStream out) throws IOException {
        combinedDefinitionWSDL.write(out);
    }

    public Definition getServiceDef() {
        return serviceDef;
    }
}
