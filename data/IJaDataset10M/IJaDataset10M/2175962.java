package org.archive.ws.jmxadaptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.JMException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import mx4j.tools.adaptor.http.HttpCommandProcessorAdaptor;
import mx4j.tools.adaptor.http.HttpInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * InvokeOperationCommandProcessor, processes a request for unregistering an
 * MBean
 * 
 * @version $Revision: 1.3 $
 */
public class InvokeOperationCommandProcessor extends JmxCommandProcessor {

    public InvokeOperationCommandProcessor() {
    }

    public Document executeRequest(HttpInputStream in) throws IOException, JMException {
        Document document = builder.newDocument();
        Element operationElement = document.createElement("Operation");
        document.appendChild(operationElement);
        String operationVariable = in.getVariable("operation");
        if (operationVariable == null || operationVariable.equals("")) {
            operationElement.setAttribute("result", "error");
            operationElement.setAttribute("errorMsg", "Incorrect parameters in the request");
            return document;
        }
        operationElement.setAttribute("pattern", nameQuerier.patternByHttpVars(in).toString());
        operationElement.setAttribute("operation", operationVariable);
        List types = new ArrayList();
        List values = new ArrayList();
        boolean unmatchedParameters = parseArguments(in, document, operationElement, types, values);
        if (unmatchedParameters) {
            operationElement.setAttribute("result", "error");
            operationElement.setAttribute("errorMsg", "count of parameter types doesn't match count of parameter values");
            return document;
        }
        Set<ObjectName> names = nameQuerier.namesByHttpVars(in);
        for (ObjectName objectName : names) {
            Element node = document.createElement("MBeanOperation");
            operationElement.appendChild(node);
            node.setAttribute("objectname", objectName.toString());
            MBeanInfo info = server.getMBeanInfo(objectName);
            node.setAttribute("classname", info.getClassName());
            node.setAttribute("description", info.getDescription());
            invoke(document, node, operationVariable, types, values, objectName);
        }
        return document;
    }

    protected void invoke(Document document, Element operationElement, String operationVariable, List types, List values, ObjectName objectName) throws InstanceNotFoundException, IntrospectionException, ReflectionException {
        if (server.isRegistered(objectName)) {
            MBeanInfo info = server.getMBeanInfo(objectName);
            MBeanOperationInfo[] operations = info.getOperations();
            boolean match = false;
            if (operations != null) {
                for (int j = 0; j < operations.length; j++) {
                    if (operations[j].getName().equals(operationVariable)) {
                        MBeanParameterInfo[] parameters = operations[j].getSignature();
                        if (parameters.length != types.size()) {
                            continue;
                        }
                        Iterator k = types.iterator();
                        boolean signatureMatch = true;
                        for (int p = 0; p < types.size(); p++) {
                            if (!parameters[p].getType().equals(k.next())) {
                                signatureMatch = false;
                                break;
                            }
                        }
                        match = signatureMatch;
                    }
                    if (match) {
                        break;
                    }
                }
            }
            if (!match) {
                operationElement.setAttribute("result", "error");
                operationElement.setAttribute("errorMsg", "Operation singature has no match in the MBean");
            } else {
                try {
                    Object[] params = values.toArray();
                    String[] signature = new String[types.size()];
                    types.toArray(signature);
                    Object returnValue = server.invoke(objectName, operationVariable, params, signature);
                    operationElement.setAttribute("result", "success");
                    if (returnValue != null) {
                        operationElement.setAttribute("returnclass", returnValue.getClass().getName());
                        operationElement.appendChild(objectToNode(document, returnValue));
                    } else {
                        operationElement.setAttribute("returnclass", null);
                        operationElement.setAttribute("return", null);
                    }
                } catch (Exception e) {
                    operationElement.setAttribute("result", "error");
                    operationElement.setAttribute("errorMsg", e.getMessage());
                }
            }
        } else {
            if (objectName != null) {
                operationElement.setAttribute("result", "error");
                operationElement.setAttribute("errorMsg", new StringBuffer("MBean ").append(objectName).append(" not registered").toString());
            }
        }
    }

    private boolean parseArguments(HttpInputStream in, Document document, Element operationElement, List types, List values) {
        int i = 0;
        boolean unmatchedParameters = false;
        boolean valid = false;
        do {
            String parameterType = in.getVariable("type" + i);
            String parameterValue = in.getVariable("value" + i);
            valid = (parameterType != null && parameterValue != null);
            if (valid) {
                types.add(parameterType);
                Object value = null;
                try {
                    value = CommandProcessorUtil.createParameterValue(parameterType, parameterValue);
                } catch (Exception e) {
                    operationElement.setAttribute("result", "error");
                    operationElement.setAttribute("errorMsg", "Parameter " + i + ": " + parameterValue + " cannot be converted to type " + parameterType);
                    unmatchedParameters = true;
                }
                if (value != null) {
                    values.add(value);
                }
            }
            if (parameterType == null ^ parameterValue == null) {
                unmatchedParameters = true;
                break;
            }
            i++;
        } while (valid);
        return unmatchedParameters;
    }
}
