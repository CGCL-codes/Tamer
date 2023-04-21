package com.intel.gpe.clients.api.workflow;

import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.intel.util.xml.Namespaces;

/**
 * The complex action for performing destruction of a WSRF resource.
 * 
 * The action generates an invocation of WS-ResourceLifetime Destroy
 * operation.
 * 
 * @author Alexander Lukichev
 * @version $Id: Destroy.java,v 1.5 2006/02/17 08:15:37 serduk Exp $
 */
public abstract class Destroy implements Action {

    private Namespaces namespaces;

    private PartnerLink resource;

    private Variable destroyRequest;

    private Variable destroyResponse;

    /**
     * Create the destroy action.
     * @param resource The partner link to the resource to be destroyed
     * @param namespaces The namespaces
     */
    public Destroy(PartnerLink resource, Namespaces namespaces) {
        this.namespaces = namespaces;
        this.resource = resource;
        String id = genId();
        destroyRequest = new Variable("destroy" + id + "Request", "DestroyRequest", "");
        destroyResponse = new Variable("destroy" + id + "Response", "DestroyResponse", "");
    }

    protected abstract VariableValue getDestroyResponseValue();

    protected abstract VariableValue getDestroyRequestValue();

    protected abstract QName getDestroyPortType();

    public void emitCode(Scope scope, Element target) throws Exception {
        scope.addVariable(destroyRequest, getDestroyRequestValue());
        scope.addVariable(destroyResponse, getDestroyResponseValue());
        Document doc = target.getOwnerDocument();
        target.appendChild(BPELPrimitives.createInvocation(doc, getDestroyPortType(), "Destroy", resource, destroyRequest, destroyResponse, InvocationParameters.getNoDelegationParameters(), namespaces));
    }

    private static int id = 0;

    private static String genId() {
        return Integer.toString(++id);
    }
}
