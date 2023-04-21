package com.intel.gpe.clients.api.workflow;

import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.intel.util.xml.Namespaces;

/**
 * The complex action for performing GetResourceProperty request.
 * 
 * The action generates an invocation of WS-ResourceProperties GetResourceProperty
 * operation.
 * 
 * @author Alexander Lukichev
 * @version $Id: GetResourceProperty.java,v 1.5 2006/02/17 08:15:37 serduk Exp $
 */
public abstract class GetResourceProperty implements Action {

    private PartnerLink resource;

    private Variable getResourcePropertyRequest;

    private Variable getResourcePropertyResponse;

    private Namespaces namespaces;

    private QName name;

    /**
     * Create the GetResourceProperty action.
     * 
     * @param name The name of the property
     * @param resource The partner link to the resource
     * @param to The variable where to store the response
     * @param namespaces The namespaces
     */
    public GetResourceProperty(QName name, PartnerLink resource, Variable to, Namespaces namespaces) {
        this.resource = resource;
        getResourcePropertyResponse = to;
        this.namespaces = namespaces;
        this.name = name;
        getResourcePropertyRequest = new Variable("getResourcePropertyRequest" + genId(), "GetResourcePropertyRequest", "");
    }

    public abstract VariableValue getGetResourcePropertyRequestValue(QName name);

    public abstract QName getGetResourcePropertyPortType();

    public void emitCode(Scope scope, Element target) throws Exception {
        scope.addVariable(getResourcePropertyRequest, getGetResourcePropertyRequestValue(name));
        Document doc = target.getOwnerDocument();
        target.appendChild(BPELPrimitives.createInvocation(doc, getGetResourcePropertyPortType(), "GetResourceProperty", resource, getResourcePropertyRequest, getResourcePropertyResponse, InvocationParameters.getNoDelegationParameters(), namespaces));
    }

    private static int idCounter = 0;

    private static String genId() {
        idCounter++;
        return Integer.toString(idCounter);
    }
}
