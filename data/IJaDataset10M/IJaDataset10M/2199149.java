package com.intel.gpe.clients.api.workflow;

import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.intel.util.xml.Namespaces;

/**
 * The set of methods for emitting BPEL primitives
 * 
 * @author Alexander Lukichev
 * @version $Id: BPELPrimitives.java,v 1.9 2006/02/17 08:15:37 serduk Exp $
 */
public class BPELPrimitives {

    public static final String ASSIGN = "assign";

    public static final String COPY = "copy";

    public static final String FROM = "from";

    public static final String TO = "to";

    public static final String INVOKE = "invoke";

    public static final String WHILE = "while";

    public static final String PICK = "pick";

    public static final String ON_MESSAGE = "onMessage";

    public static final String CASE = "case";

    public static final String OTHERWISE = "otherwise";

    public static final String SWITCH = "switch";

    public static final String THROW = "throw";

    public static final String PARAMETER = "parameter";

    public static final String SCOPE = "scope";

    public static final String FAULT_HANDLERS = "faultHandlers";

    public static final String SEQUENCE = "sequence";

    public static final String CATCH = "catch";

    public static final String FLOW = "flow";

    public static Element createAssignment(Document doc, Variable from, Variable to) {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, ASSIGN);
        Element copy = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, COPY);
        Element efrom = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, FROM);
        efrom.setAttribute("variable", from.getName());
        efrom.setAttribute("part", from.getPart());
        if (from.getQuery() != null) {
            efrom.setAttribute("query", from.getQuery());
        }
        Element eto = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, TO);
        eto.setAttribute("variable", to.getName());
        eto.setAttribute("part", to.getPart());
        if (to.getQuery() != null) {
            eto.setAttribute("query", to.getQuery());
        }
        copy.appendChild(efrom);
        copy.appendChild(eto);
        result.appendChild(copy);
        return result;
    }

    public static Element createAssignment(Document doc, Variable from, PartnerLink to) {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, ASSIGN);
        Element copy = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, COPY);
        Element efrom = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, FROM);
        efrom.setAttribute("variable", from.getName());
        efrom.setAttribute("part", from.getPart());
        if (from.getQuery() != null) {
            efrom.setAttribute("query", from.getQuery());
        }
        Element eto = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, TO);
        eto.setAttribute("partnerLink", to.getName());
        copy.appendChild(efrom);
        copy.appendChild(eto);
        result.appendChild(copy);
        return result;
    }

    public static Element createAssignment(Document doc, PartnerLink from, boolean myRole, Variable to) {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, ASSIGN);
        Element copy = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, COPY);
        Element efrom = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, FROM);
        efrom.setAttribute("partnerLink", from.getName());
        efrom.setAttribute("endpointReference", myRole ? "myRole" : "partnerRole");
        Element eto = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, TO);
        eto.setAttribute("variable", to.getName());
        eto.setAttribute("part", to.getPart());
        if (to.getQuery() != null) {
            eto.setAttribute("query", to.getQuery());
        }
        copy.appendChild(efrom);
        copy.appendChild(eto);
        result.appendChild(copy);
        return result;
    }

    public static Element createAssignment(Document doc, PartnerLink from, boolean myRole, PartnerLink to) {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, ASSIGN);
        Element copy = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, COPY);
        Element efrom = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, FROM);
        efrom.setAttribute("partnerLink", from.getName());
        efrom.setAttribute("endpointReference", myRole ? "myRole" : "partnerRole");
        Element eto = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, TO);
        eto.setAttribute("partnerLink", to.getName());
        copy.appendChild(efrom);
        copy.appendChild(eto);
        result.appendChild(copy);
        return result;
    }

    public static Node createAssignment(Document doc, String expr, Variable to) {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, ASSIGN);
        Element copy = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, COPY);
        Element efrom = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, FROM);
        efrom.setAttribute("expression", expr);
        Element eto = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, TO);
        eto.setAttribute("variable", to.getName());
        eto.setAttribute("part", to.getPart());
        if (to.getQuery() != null) {
            eto.setAttribute("query", to.getQuery());
        }
        copy.appendChild(efrom);
        copy.appendChild(eto);
        result.appendChild(copy);
        return result;
    }

    public static Element createInvocation(Document doc, QName portType, String operation, PartnerLink partnerLink, Variable input, Variable output, Map parameters, Namespaces namespaces) throws Exception {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, INVOKE);
        String portTypeStr = namespaces.getStringFromQName(portType);
        result.setAttribute("portType", portTypeStr);
        result.setAttribute("partnerLink", partnerLink.getName());
        result.setAttribute("operation", operation);
        result.setAttribute("inputVariable", input.getName());
        result.setAttribute("outputVariable", output.getName());
        Iterator pkey = parameters.keySet().iterator();
        while (pkey.hasNext()) {
            String name = (String) pkey.next();
            String value = (String) parameters.get(name);
            Element parameter = doc.createElementNS(WorkflowConstants.Workflow.NAMESPACE, PARAMETER);
            parameter.setAttribute("name", name);
            parameter.setAttribute("value", value);
            result.appendChild(parameter);
        }
        return result;
    }

    public static Element createPick(Document doc) {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, PICK);
        return result;
    }

    public static Element createOnMessage(Document doc, QName portType, String operation, PartnerLink partnerLink, Variable variable, Namespaces namespaces) throws Exception {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, ON_MESSAGE);
        String portTypeStr = namespaces.getStringFromQName(portType);
        result.setAttribute("portType", portTypeStr);
        result.setAttribute("partnerLink", partnerLink.getName());
        result.setAttribute("operation", operation);
        result.setAttribute("variable", variable.getName());
        return result;
    }

    public static Element createWhile(Document doc, String condition, Action action, Scope scope) throws Exception {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, WHILE);
        result.setAttribute("condition", condition);
        action.emitCode(scope, result);
        return result;
    }

    public static Element createCase(Document doc, String condition, Action action, Scope scope) throws Exception {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, CASE);
        result.setAttribute("condition", condition);
        action.emitCode(scope, result);
        return result;
    }

    public static Element createOtherwise(Document doc, Action action, Scope scope) throws Exception {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, OTHERWISE);
        action.emitCode(scope, result);
        return result;
    }

    public static Element createSwitch(Document doc) throws Exception {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, SWITCH);
        return result;
    }

    public static Element createThrow(Document doc, QName fault) {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, THROW);
        result.setAttribute("xmlns:fault", fault.getNamespaceURI());
        result.setAttribute("faultName", "fault:" + fault.getLocalPart());
        return result;
    }

    public static Element createScope(Document doc) {
        return doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, SCOPE);
    }

    public static Element createFaultHandlers(Document doc) {
        return doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, FAULT_HANDLERS);
    }

    public static Element createSequence(Document doc) {
        return doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, SEQUENCE);
    }

    public static Element createCatch(Document doc, QName fault) {
        Element result = doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, CATCH);
        if (fault != null) {
            result.setAttribute("xmlns:fault", fault.getNamespaceURI());
            result.setAttribute("faultName", "fault:" + fault.getLocalPart());
        }
        return result;
    }

    public static Element createFlow(Document doc) {
        return doc.createElementNS(WorkflowConstants.Workflow.BPWS_NAMESPACE, FLOW);
    }
}
