package uk.gov.dti.og.fox.webservices;

import java.util.HashMap;
import java.util.Map;
import uk.gov.dti.og.fox.Theme;
import uk.gov.dti.og.fox.XFUtil;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.dom.DOMList;
import uk.gov.dti.og.fox.ex.ExInternal;

/**
 * WSDL Binding builder for SOAP. Fills in the implementation-specific
 * parts of the WSDL, based on the operations that we're providing in
 * the port specification.
 */
public class WSDLBindingBuilderSOAP extends WSDLBindingBuilder {

    public static final String NAMESPACE_URI = "http://schemas.xmlsoap.org/wsdl/soap/";

    public static final String SOAP_TRANSPORT = "http://schemas.xmlsoap.org/soap/http";

    public static final String SOAP_BODY_USE = "literal";

    /**
   * Creates a valid WSDL binding based on the PortType provided, using the
   * specified namespace. This method makes the assumption that the PortType
   * provided does not have namespace prefixes on WSDL elements.
   * @param pNsPrefix the namespace prefix to use
   * @param pPortTypeDOM the port type on which to base the binding
   * @param pHintBaseURI the base URI to use when generating operation hints
   * @param pTargetNsPrefix the prefix to use when referencing the binding
   * @param pTargetNsURI the target namespace to use in the binding
   * @param pOperationToBindingStyleMap a map of operation names to binding style
   * @param pBindingStyle the style of the binding and which operations to include
   * @return a WSDL binding as a DOM
   */
    public DOM createWSDLBinding(String pNsPrefix, DOM pPortTypeDOM, String pHintBaseURI, String pTargetNsPrefix, String pTargetNsURI, Map pOperationToBindingStyleMap, String pBindingStyle) {
        if (pPortTypeDOM == null) {
            throw new ExInternal("Null PortType passed to WSDLBindingBuilderSOAP.createWSDLBinding");
        }
        if (XFUtil.isNull(pHintBaseURI)) {
            throw new ExInternal("No hint base URI passed to WSDLBindingBuilderSOAP.createWSDLBinding ");
        }
        String lPortTypeName = pPortTypeDOM.getAttr("name");
        if (XFUtil.isNull(lPortTypeName)) {
            throw new ExInternal("Null PortType name found in WSDLBindingBuilderSOAP.createWSDLBinding");
        }
        DOM lBindingDOM = DOM.createDocument("binding");
        lBindingDOM.setAttr("name", lPortTypeName + "Binding");
        lBindingDOM.setAttr("type", (XFUtil.isNull(pTargetNsPrefix) ? "" : pTargetNsPrefix + ":") + lPortTypeName);
        DOM lSOAPBinding = lBindingDOM.addElem(pNsPrefix + ":binding");
        lSOAPBinding.setAttr("transport", SOAP_TRANSPORT);
        lSOAPBinding.setAttr("style", pBindingStyle);
        DOMList lOperations = pPortTypeDOM.getUL("./operation");
        OPERATION_LOOP: for (int i = 0; i < lOperations.getLength(); i++) {
            DOM lWSDLOperation = lOperations.item(i);
            String lOperationName = lWSDLOperation.getAttr("name");
            if (XFUtil.isNull(lOperationName)) {
                throw new ExInternal("Null or empty name attribute found in WSDL operation when processing WSDLBindingBuilderSOAP.createWSDLBinding");
            }
            String lBindingStyle = (String) pOperationToBindingStyleMap.get(lOperationName);
            if (XFUtil.isNull(lBindingStyle)) {
                throw new ExInternal("No binding style found for operation '" + lOperationName + "' in WSDLBindingBuilderSOAP.createWSDLBinding");
            }
            DOM lBindingOperation = lBindingDOM.addElem("operation");
            lBindingOperation.setAttr("name", lOperationName);
            DOM lSOAPOperation = lBindingOperation.addElem(pNsPrefix + ":operation");
            lSOAPOperation.setAttr("soapAction", pHintBaseURI + "/" + lOperationName);
            lSOAPOperation.setAttr("style", lBindingStyle);
            DOM lWSDLInput = lWSDLOperation.get1EOrNull("input");
            if (lWSDLInput != null) {
                generateWSDLOperationChildNode(pNsPrefix, lWSDLInput, pTargetNsURI, lBindingStyle).moveToParent(lBindingOperation);
            }
            DOM lWSDLOutut = lWSDLOperation.get1EOrNull("output");
            if (lWSDLOutut != null) {
                generateWSDLOperationChildNode(pNsPrefix, lWSDLOutut, pTargetNsURI, lBindingStyle).moveToParent(lBindingOperation);
            }
        }
        return lBindingDOM;
    }

    /**
   * Reusable method to create the child nodes of an operation.
   * @param pNsPrefix the base prefix to use
   * @param pPortTypeOperationChildNode the prototype operation to work from
   * @param pTargetNsURI the target namespace to use
   * @param pBindingStyle the operation style to determine requirement of namespace
   * @return a generated SOAP operation child node
   */
    private DOM generateWSDLOperationChildNode(String pNsPrefix, DOM pPortTypeOperationChildNode, String pTargetNsURI, String pBindingStyle) {
        String lNameAttr = pPortTypeOperationChildNode.getAttr("name");
        if (XFUtil.isNull(lNameAttr)) {
            throw new ExInternal("Null or empty name attribute found in WSDL input when processing WSDLBindingBuilderSOAP.createWSDLBinding");
        }
        DOM lOpChildNode = pPortTypeOperationChildNode.createUnconnectedElement(pPortTypeOperationChildNode.getLocalName());
        lOpChildNode.setAttr("name", lNameAttr);
        DOM lSOAPBody = lOpChildNode.addElem(pNsPrefix + ":body");
        lSOAPBody.setAttr("use", SOAP_BODY_USE);
        if (pBindingStyle.equals(BINDING_STYLE_RPC)) {
            lSOAPBody.setAttr("namespace", pTargetNsURI);
        }
        return lOpChildNode;
    }
}
