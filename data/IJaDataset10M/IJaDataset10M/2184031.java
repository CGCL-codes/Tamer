package net.sf.istcontract.wsimport.model.wsdl;

import com.sun.istack.Nullable;
import com.sun.istack.NotNull;
import javax.jws.WebParam.Mode;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import net.sf.istcontract.wsimport.api.model.ParameterBinding;
import net.sf.istcontract.wsimport.api.model.wsdl.*;
import java.util.*;

/**
 * Implementation of {@link WSDLBoundOperation}
 *
 * @author Vivek Pandey
 */
public final class WSDLBoundOperationImpl extends AbstractExtensibleImpl implements WSDLBoundOperation {

    private final QName name;

    private final Map<String, ParameterBinding> inputParts;

    private final Map<String, ParameterBinding> outputParts;

    private final Map<String, ParameterBinding> faultParts;

    private final Map<String, String> inputMimeTypes;

    private final Map<String, String> outputMimeTypes;

    private final Map<String, String> faultMimeTypes;

    private boolean explicitInputSOAPBodyParts = false;

    private boolean explicitOutputSOAPBodyParts = false;

    private boolean explicitFaultSOAPBodyParts = false;

    private Boolean emptyInputBody;

    private Boolean emptyOutputBody;

    private Boolean emptyFaultBody;

    private final Map<String, WSDLPartImpl> inParts;

    private final Map<String, WSDLPartImpl> outParts;

    private final Map<String, WSDLPartImpl> fltParts;

    private final List<WSDLBoundFaultImpl> wsdlBoundFaults;

    private WSDLOperationImpl operation;

    private String soapAction;

    private ANONYMOUS anonymous;

    private final WSDLBoundPortTypeImpl owner;

    /**
     *
     * @param name wsdl:operation name qualified value
     */
    public WSDLBoundOperationImpl(XMLStreamReader xsr, WSDLBoundPortTypeImpl owner, QName name) {
        super(xsr);
        this.name = name;
        inputParts = new HashMap<String, ParameterBinding>();
        outputParts = new HashMap<String, ParameterBinding>();
        faultParts = new HashMap<String, ParameterBinding>();
        inputMimeTypes = new HashMap<String, String>();
        outputMimeTypes = new HashMap<String, String>();
        faultMimeTypes = new HashMap<String, String>();
        inParts = new HashMap<String, WSDLPartImpl>();
        outParts = new HashMap<String, WSDLPartImpl>();
        fltParts = new HashMap<String, WSDLPartImpl>();
        wsdlBoundFaults = new ArrayList<WSDLBoundFaultImpl>();
        this.owner = owner;
    }

    public QName getName() {
        return name;
    }

    public String getSOAPAction() {
        return soapAction;
    }

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction != null ? soapAction : "";
    }

    public WSDLPartImpl getPart(String partName, Mode mode) {
        if (mode == Mode.IN) {
            return inParts.get(partName);
        } else if (mode == Mode.OUT) {
            return outParts.get(partName);
        }
        return null;
    }

    public void addPart(WSDLPartImpl part, Mode mode) {
        if (mode == Mode.IN) inParts.put(part.getName(), part); else if (mode == Mode.OUT) outParts.put(part.getName(), part);
    }

    /**
     * Map of wsdl:input part name and the binding as {@link ParameterBinding}
     *
     * @return empty Map if there is no parts
     */
    public Map<String, ParameterBinding> getInputParts() {
        return inputParts;
    }

    /**
     * Map of wsdl:output part name and the binding as {@link ParameterBinding}
     *
     * @return empty Map if there is no parts
     */
    public Map<String, ParameterBinding> getOutputParts() {
        return outputParts;
    }

    /**
     * Map of wsdl:fault part name and the binding as {@link ParameterBinding}
     *
     * @return empty Map if there is no parts
     */
    public Map<String, ParameterBinding> getFaultParts() {
        return faultParts;
    }

    public Map<String, WSDLPart> getInParts() {
        return Collections.<String, WSDLPart>unmodifiableMap(inParts);
    }

    public Map<String, WSDLPart> getOutParts() {
        return Collections.<String, WSDLPart>unmodifiableMap(outParts);
    }

    @NotNull
    public List<WSDLBoundFaultImpl> getFaults() {
        return wsdlBoundFaults;
    }

    public void addFault(@NotNull WSDLBoundFaultImpl fault) {
        wsdlBoundFaults.add(fault);
    }

    /**
     * Map of mime:content@part and the mime type from mime:content@type for wsdl:output
     *
     * @return empty Map if there is no parts
     */
    public Map<String, String> getInputMimeTypes() {
        return inputMimeTypes;
    }

    /**
     * Map of mime:content@part and the mime type from mime:content@type for wsdl:output
     *
     * @return empty Map if there is no parts
     */
    public Map<String, String> getOutputMimeTypes() {
        return outputMimeTypes;
    }

    /**
     * Map of mime:content@part and the mime type from mime:content@type for wsdl:fault
     *
     * @return empty Map if there is no parts
     */
    public Map<String, String> getFaultMimeTypes() {
        return faultMimeTypes;
    }

    /**
     * Gets {@link ParameterBinding} for a given wsdl part in wsdl:input
     *
     * @param part Name of wsdl:part, must be non-null
     * @return null if the part is not found.
     */
    public ParameterBinding getInputBinding(String part) {
        if (emptyInputBody == null) {
            if (inputParts.get(" ") != null) emptyInputBody = true; else emptyInputBody = false;
        }
        ParameterBinding block = inputParts.get(part);
        if (block == null) {
            if (explicitInputSOAPBodyParts || emptyInputBody) return ParameterBinding.UNBOUND;
            return ParameterBinding.BODY;
        }
        return block;
    }

    /**
     * Gets {@link ParameterBinding} for a given wsdl part in wsdl:output
     *
     * @param part Name of wsdl:part, must be non-null
     * @return null if the part is not found.
     */
    public ParameterBinding getOutputBinding(String part) {
        if (emptyOutputBody == null) {
            if (outputParts.get(" ") != null) emptyOutputBody = true; else emptyOutputBody = false;
        }
        ParameterBinding block = outputParts.get(part);
        if (block == null) {
            if (explicitOutputSOAPBodyParts || emptyOutputBody) return ParameterBinding.UNBOUND;
            return ParameterBinding.BODY;
        }
        return block;
    }

    /**
     * Gets {@link ParameterBinding} for a given wsdl part in wsdl:fault
     *
     * @param part Name of wsdl:part, must be non-null
     * @return null if the part is not found.
     */
    public ParameterBinding getFaultBinding(String part) {
        if (emptyFaultBody == null) {
            if (faultParts.get(" ") != null) emptyFaultBody = true; else emptyFaultBody = false;
        }
        ParameterBinding block = faultParts.get(part);
        if (block == null) {
            if (explicitFaultSOAPBodyParts || emptyFaultBody) return ParameterBinding.UNBOUND;
            return ParameterBinding.BODY;
        }
        return block;
    }

    /**
     * Gets the MIME type for a given wsdl part in wsdl:input
     *
     * @param part Name of wsdl:part, must be non-null
     * @return null if the part is not found.
     */
    public String getMimeTypeForInputPart(String part) {
        return inputMimeTypes.get(part);
    }

    /**
     * Gets the MIME type for a given wsdl part in wsdl:output
     *
     * @param part Name of wsdl:part, must be non-null
     * @return null if the part is not found.
     */
    public String getMimeTypeForOutputPart(String part) {
        return outputMimeTypes.get(part);
    }

    /**
     * Gets the MIME type for a given wsdl part in wsdl:fault
     *
     * @param part Name of wsdl:part, must be non-null
     * @return null if the part is not found.
     */
    public String getMimeTypeForFaultPart(String part) {
        return faultMimeTypes.get(part);
    }

    public WSDLOperationImpl getOperation() {
        return operation;
    }

    public WSDLBoundPortType getBoundPortType() {
        return owner;
    }

    public void setInputExplicitBodyParts(boolean b) {
        explicitInputSOAPBodyParts = b;
    }

    public void setOutputExplicitBodyParts(boolean b) {
        explicitOutputSOAPBodyParts = b;
    }

    public void setFaultExplicitBodyParts(boolean b) {
        explicitFaultSOAPBodyParts = b;
    }

    private Style style = Style.DOCUMENT;

    public void setStyle(Style style) {
        this.style = style;
    }

    @Nullable
    public QName getPayloadName() {
        if (style.equals(Style.RPC)) {
            return name;
        } else {
            if (emptyPayload) return null;
            if (payloadName != null) return payloadName;
            QName inMsgName = operation.getInput().getMessage().getName();
            WSDLMessageImpl message = messages.get(inMsgName);
            for (WSDLPartImpl part : message.parts()) {
                ParameterBinding binding = getInputBinding(part.getName());
                if (binding.isBody()) {
                    payloadName = part.getDescriptor().name();
                    return payloadName;
                }
            }
            emptyPayload = true;
        }
        return null;
    }

    @Nullable
    public QName getReqPayloadName() {
        if (emptyRequestPayload) return null;
        if (requestPayloadName != null) return requestPayloadName;
        if (style.equals(Style.RPC)) {
            String ns = getRequestNamespace() != null ? getRequestNamespace() : name.getNamespaceURI();
            requestPayloadName = new QName(ns, name.getLocalPart());
            return requestPayloadName;
        } else {
            QName inMsgName = operation.getInput().getMessage().getName();
            WSDLMessageImpl message = messages.get(inMsgName);
            for (WSDLPartImpl part : message.parts()) {
                ParameterBinding binding = getInputBinding(part.getName());
                if (binding.isBody()) {
                    requestPayloadName = part.getDescriptor().name();
                    return requestPayloadName;
                }
            }
            emptyRequestPayload = true;
        }
        return null;
    }

    @Nullable
    public QName getResPayloadName() {
        if (emptyResponsePayload) return null;
        if (responsePayloadName != null) return responsePayloadName;
        if (style.equals(Style.RPC)) {
            String ns = getResponseNamespace() != null ? getResponseNamespace() : name.getNamespaceURI();
            responsePayloadName = new QName(ns, name.getLocalPart() + "Response");
            return responsePayloadName;
        } else {
            QName outMsgName = operation.getOutput().getMessage().getName();
            WSDLMessageImpl message = messages.get(outMsgName);
            for (WSDLPartImpl part : message.parts()) {
                ParameterBinding binding = getOutputBinding(part.getName());
                if (binding.isBody()) {
                    responsePayloadName = part.getDescriptor().name();
                    return responsePayloadName;
                }
            }
            emptyResponsePayload = true;
        }
        return null;
    }

    private String reqNamespace;

    private String respNamespace;

    /**
     * For rpclit gives namespace value on soapbinding:body@namespace
     *
     * @return   non-null for rpclit and null for doclit
     * @see net.sf.istcontract.wsimport.model.RuntimeModeler#processRpcMethod(net.sf.istcontract.wsimport.model.JavaMethodImpl, String, javax.jws.WebMethod, String, java.lang.reflect.Method, javax.jws.WebService)
     */
    public String getRequestNamespace() {
        return (reqNamespace != null) ? reqNamespace : name.getNamespaceURI();
    }

    public void setRequestNamespace(String ns) {
        reqNamespace = ns;
    }

    /**
     * For rpclit gives namespace value on soapbinding:body@namespace
     *
     * @return   non-null for rpclit and null for doclit
     *      * @see net.sf.istcontract.wsimport.model.RuntimeModeler#processRpcMethod(net.sf.istcontract.wsimport.model.JavaMethod, String, javax.jws.WebMethod, String, java.lang.reflect.Method, javax.jws.WebService)
     */
    public String getResponseNamespace() {
        return (respNamespace != null) ? respNamespace : name.getNamespaceURI();
    }

    public void setResponseNamespace(String ns) {
        respNamespace = ns;
    }

    WSDLBoundPortTypeImpl getOwner() {
        return owner;
    }

    private QName payloadName;

    private QName requestPayloadName;

    private QName responsePayloadName;

    private boolean emptyPayload;

    private boolean emptyRequestPayload;

    private boolean emptyResponsePayload;

    private Map<QName, WSDLMessageImpl> messages;

    void freeze(WSDLModelImpl parent) {
        messages = parent.getMessages();
        operation = owner.getPortType().get(name.getLocalPart());
        for (WSDLBoundFaultImpl bf : wsdlBoundFaults) {
            bf.freeze(this);
        }
    }

    public void setAnonymous(ANONYMOUS anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * @inheritDoc
     */
    public ANONYMOUS getAnonymous() {
        return anonymous;
    }
}
