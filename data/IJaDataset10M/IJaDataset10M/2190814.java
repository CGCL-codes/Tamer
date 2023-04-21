package org.tripcom.ws.wsdl2rdf;

import java.io.File;
import java.net.URI;
import java.util.UUID;
import javax.xml.namespace.QName;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.internal.wsdl20.extensions.PopulatedExtensionRegistry;
import org.apache.woden.wsdl20.Binding;
import org.apache.woden.wsdl20.BindingFault;
import org.apache.woden.wsdl20.BindingFaultReference;
import org.apache.woden.wsdl20.BindingMessageReference;
import org.apache.woden.wsdl20.BindingOperation;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.ElementDeclaration;
import org.apache.woden.wsdl20.Endpoint;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceFault;
import org.apache.woden.wsdl20.InterfaceFaultReference;
import org.apache.woden.wsdl20.InterfaceMessageReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.Service;
import org.apache.woden.wsdl20.TypeDefinition;
import org.apache.woden.wsdl20.WSDLComponent;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.extensions.InterfaceOperationExtensions;
import org.apache.woden.wsdl20.extensions.http.HTTPAuthenticationScheme;
import org.apache.woden.wsdl20.extensions.http.HTTPBindingExtensions;
import org.apache.woden.wsdl20.extensions.http.HTTPBindingFaultExtensions;
import org.apache.woden.wsdl20.extensions.http.HTTPBindingMessageReferenceExtensions;
import org.apache.woden.wsdl20.extensions.http.HTTPBindingOperationExtensions;
import org.apache.woden.wsdl20.extensions.http.HTTPEndpointExtensions;
import org.apache.woden.wsdl20.extensions.http.HTTPErrorStatusCode;
import org.apache.woden.wsdl20.extensions.http.HTTPHeader;
import org.apache.woden.wsdl20.extensions.http.HTTPLocation;
import org.apache.woden.wsdl20.extensions.rpc.Argument;
import org.apache.woden.wsdl20.extensions.rpc.RPCInterfaceOperationExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPBindingExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPBindingFaultExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPBindingFaultReferenceExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPBindingMessageReferenceExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPBindingOperationExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPEndpointExtensions;
import org.apache.woden.wsdl20.extensions.soap.SOAPFaultCode;
import org.apache.woden.wsdl20.extensions.soap.SOAPFaultSubcodes;
import org.apache.woden.wsdl20.extensions.soap.SOAPHeaderBlock;
import org.apache.woden.wsdl20.extensions.soap.SOAPModule;
import edu.uga.cs.lsdis.wsdl20.extensions.sawsdl.ModelReference;
import edu.uga.cs.lsdis.wsdl20.extensions.sawsdl.WrappingReaderFactory;
import edu.uga.cs.lsdis.wsdl20.extensions.sawsdl.util.SAWSDLExtensionUtil;
import edu.uga.cs.lsdis.wsdl20.util.SAWSDLUtil;

/**
 * This class implements the transformation from WSDL 2.0 Component Model
 * from the Woden parser into the RDF form as defined by WSDL 2.0 RDF Mapping.
 *
 * @author jacek
 */
public class Wsdl2Rdf {

    /**
     * constructor with explicit rdf output
     * @param rdfOutput
     */
    public Wsdl2Rdf(RDFOutput rdfOutput) {
        this.rdfOutputHolder = rdfOutput;
    }

    /**
     * default constructor, will output n3 into stdout and warnings into stderr
     */
    public Wsdl2Rdf() {
        this.rdfOutputHolder = new PrintStreamN3(System.out, System.err);
    }

    /**
     * holder for rdf outputter
     */
    private RDFOutput rdfOutputHolder;

    /**
     * Transforms a single given file, outputting result on stdout in n-triples.
     * @param args cmd-line arguments
     * @throws Exception when something happens
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("A single parameter required!\n" + "Usage:     wsdl.Wsdl2Rdf  document.wsdl\n" + "The resulting RDF form is output as n-triples on the standard output.");
            System.exit(-1);
        }
        String uri = args[0];
        URI currentDir = new File(".").toURI();
        uri = currentDir.resolve(uri).toASCIIString();
        PrintStreamN3 rdfOut = new PrintStreamN3(System.out, System.err);
        System.setOut(System.err);
        WSDLFactory factory = WSDLFactory.newInstance(WrappingReaderFactory.class.getCanonicalName());
        WSDLReader reader = factory.newWSDLReader();
        reader.setExtensionRegistry(new PopulatedExtensionRegistry());
        SAWSDLExtensionUtil.registerExtensions(reader.getExtensionRegistry(), true);
        Description descComp = reader.readWSDL(uri);
        new Wsdl2Rdf(rdfOut).map(descComp);
    }

    /**
     * Maps a description component and its children, i.e. the whole WSDL.
     * @param desc the description component
     */
    public void map(Description desc) {
        URI descID = ComponentIDs.id(desc);
        n3(descID, Constants.RDF_TYPE, Constants.WSDL_Description);
        Interface[] ifaces = desc.getInterfaces();
        for (int i = 0; i < ifaces.length; i++) {
            map(descID, ifaces[i]);
        }
        Binding[] bindings = desc.getBindings();
        for (int i = 0; i < bindings.length; i++) {
            map(descID, bindings[i]);
        }
        Service[] svcs = desc.getServices();
        for (int i = 0; i < svcs.length; i++) {
            map(descID, svcs[i]);
        }
        mapSAWSDL(desc);
    }

    /**
     * maps the SAWSDL properties on any WSDL component into RDF
     * @param comp the component
     */
    public void mapSAWSDL(WSDLComponent comp) {
        try {
            URI compid = ComponentIDs.id(comp);
            ModelReference mref = SAWSDLUtil.getModelRef(comp);
            if (mref == null) {
                return;
            }
            URI[] uris = mref.getURIs();
            for (int i = 0; i < uris.length; i++) {
                n3(compid, Constants.SAWSDL_modelReference, uris[i]);
            }
        } catch (SAWSDLUtil.ModelRefExtractionException e) {
            throw new RuntimeException("error extracting model reference", e);
        }
    }

    /**
     * maps interface components
     * @param descID
     * @param iface
     */
    public void map(URI descID, Interface iface) {
        URI ifaceID = ComponentIDs.id(iface);
        n3(descID, Constants.WSDL_interface, ifaceID);
        n3(ifaceID, Constants.RDF_TYPE, Constants.WSDL_Interface);
        n3String(ifaceID, Constants.RDFS_label, iface.getName().getLocalPart());
        Interface extended[] = iface.getExtendedInterfaces();
        for (int i = 0; i < extended.length; i++) {
            n3(ifaceID, Constants.WSDL_extends, ComponentIDs.id(extended[i]));
        }
        InterfaceOperation[] ops = iface.getInterfaceOperations();
        for (int i = 0; i < ops.length; i++) {
            map(ifaceID, ops[i]);
        }
        InterfaceFault[] faults = iface.getInterfaceFaults();
        for (int i = 0; i < faults.length; i++) {
            map(ifaceID, faults[i]);
        }
        mapSAWSDL(iface);
    }

    /**
     * maps interface fault components
     * @param ifaceID
     * @param fault
     */
    public void map(URI ifaceID, InterfaceFault fault) {
        URI faultID = ComponentIDs.id(fault);
        n3(ifaceID, Constants.WSDL_interfaceFault, faultID);
        n3(faultID, Constants.RDF_TYPE, Constants.WSDL_InterfaceFault);
        n3String(faultID, Constants.RDFS_label, fault.getName().getLocalPart());
        ElementDeclaration el = fault.getElementDeclaration();
        if (el != null) {
            mapQName(faultID, Constants.WSDL_elementDeclaration, el.getName());
        }
        String mcm = fault.getMessageContentModel();
        mapMsgContentModel(faultID, mcm);
        mapSAWSDL(fault);
    }

    /**
     * maps message content model
     * @param id
     * @param mcm
     */
    public void mapMsgContentModel(URI id, String mcm) {
        if (mcm.equals(Constants.TOKEN_ANY)) {
            n3(id, Constants.WSDL_messageContentModel, Constants.WSDL_AnyContent);
        } else if (mcm.equals(Constants.TOKEN_NONE)) {
            n3(id, Constants.WSDL_messageContentModel, Constants.WSDL_NoContent);
        } else if (mcm.equals(Constants.TOKEN_OTHER)) {
            n3(id, Constants.WSDL_messageContentModel, Constants.WSDL_OtherContent);
        } else if (mcm.equals(Constants.TOKEN_ELEMENT)) {
            n3(id, Constants.WSDL_messageContentModel, Constants.WSDL_ElementContent);
        }
    }

    /**
     * maps a qname belonging to componentID with property
     * @param componentID
     * @param property
     * @param qname
     */
    public void mapQName(URI componentID, URI property, QName qname) {
        if (qname == null) {
            return;
        }
        URI genid = genid();
        n3(componentID, property, genid);
        n3(genid, Constants.RDF_TYPE, Constants.WSDL_QName);
        n3String(genid, Constants.WSDL_localName, qname.getLocalPart());
        n3(genid, Constants.WSDL_namespace, URI.create(qname.getNamespaceURI()));
    }

    /**
     * maps interface operation components
     * @param ifaceID
     * @param operation
     */
    public void map(URI ifaceID, InterfaceOperation operation) {
        URI opID = ComponentIDs.id(operation);
        n3(ifaceID, Constants.WSDL_interfaceOperation, opID);
        n3(opID, Constants.RDF_TYPE, Constants.WSDL_InterfaceOperation);
        n3String(opID, Constants.RDFS_label, operation.getName().getLocalPart());
        InterfaceMessageReference[] msgrefs = operation.getInterfaceMessageReferences();
        for (int i = 0; i < msgrefs.length; i++) {
            map(opID, msgrefs[i]);
        }
        InterfaceFaultReference[] faultrefs = operation.getInterfaceFaultReferences();
        for (int i = 0; i < faultrefs.length; i++) {
            map(opID, faultrefs[i]);
        }
        n3(opID, Constants.WSDL_messageExchangePattern, getMEP(operation));
        URI[] styles = operation.getStyle();
        for (int i = 0; i < styles.length; i++) {
            n3(opID, Constants.WSDL_operationStyle, styles[i]);
        }
        mapExtensions(operation);
    }

    /**
     * workaround for woden bug where it doesn't default properly
     * @param operation
     * @return the mep of the op
     */
    private static URI getMEP(InterfaceOperation operation) {
        URI mep = operation.getMessageExchangePattern();
        if (mep == null) {
            mep = Constants.WSDL_MEP_IN_OUT;
        }
        return mep;
    }

    /**
     * maps extensions of this component
     * @param operation
     */
    public void mapExtensions(InterfaceOperation operation) {
        URI opID;
        opID = ComponentIDs.id(operation);
        InterfaceOperationExtensions safety = (InterfaceOperationExtensions) operation.getComponentExtensionsForNamespace(Constants.WSDLX_NS);
        if (safety != null && safety.isSafety()) {
            n3(opID, Constants.SAWSDL_modelReference, Constants.WSDLX_SafeInteraction);
        }
        RPCInterfaceOperationExtensions rpc = (RPCInterfaceOperationExtensions) operation.getComponentExtensionsForNamespace(Constants.WRPC_NS);
        if (rpc != null) {
            mapRPCSignature(opID, rpc);
        }
        mapSAWSDL(operation);
    }

    /**
     * maps rpc signature on an operation component
     * @param opID
     * @param rpc
     */
    public void mapRPCSignature(URI opID, RPCInterfaceOperationExtensions rpc) {
        Argument[] sig = rpc.getRPCSignature();
        if (sig != null && sig.length != 0) {
            URI sigid = genid();
            n3(opID, Constants.WRPC_signature, sigid);
            n3(sigid, Constants.RDF_TYPE, Constants.WRPC_Signature);
            for (int i = 0; i < sig.length; i++) {
                Argument a = sig[i];
                URI aid = genid();
                n3(sigid, rdfli(i + 1), aid);
                org.apache.woden.wsdl20.extensions.rpc.Direction dir = a.getDirection();
                if (dir.equals(org.apache.woden.wsdl20.extensions.rpc.Direction.IN)) {
                    n3(aid, Constants.RDF_TYPE, Constants.WRPC_InArgument);
                } else if (dir.equals(org.apache.woden.wsdl20.extensions.rpc.Direction.OUT)) {
                    n3(aid, Constants.RDF_TYPE, Constants.WRPC_OutArgument);
                } else if (dir.equals(org.apache.woden.wsdl20.extensions.rpc.Direction.INOUT)) {
                    n3(aid, Constants.RDF_TYPE, Constants.WRPC_InOutArgument);
                } else if (dir.equals(org.apache.woden.wsdl20.extensions.rpc.Direction.RETURN)) {
                    n3(aid, Constants.RDF_TYPE, Constants.WRPC_ReturnArgument);
                }
                mapQName(aid, Constants.WSDL_elementDeclaration, a.getName());
            }
        }
    }

    /**
     * maps interface message reference components
     * @param opID
     * @param msgref
     */
    public void map(URI opID, InterfaceMessageReference msgref) {
        URI id = ComponentIDs.id(msgref);
        n3(opID, Constants.WSDL_interfaceMessageReference, id);
        n3(id, Constants.RDF_TYPE, Constants.WSDL_InterfaceMessageReference);
        ElementDeclaration el = msgref.getElementDeclaration();
        if (el != null) {
            mapQName(id, Constants.WSDL_elementDeclaration, el.getName());
        }
        String mcm = msgref.getMessageContentModel();
        mapMsgContentModel(id, mcm);
        Direction dir = msgref.getDirection();
        mapMsgDir(id, dir);
        String label = msgref.getMessageLabel().toString();
        URI mep = getMEP((InterfaceOperation) msgref.getParent());
        mapMsgLabel(id, mep, label);
        mapSAWSDL(msgref);
    }

    /**
     * maps interface fault reference components
     * @param opID
     * @param fltref
     */
    public void map(URI opID, InterfaceFaultReference fltref) {
        URI id = ComponentIDs.id(fltref);
        n3(opID, Constants.WSDL_interfaceFaultReference, id);
        n3(id, Constants.RDF_TYPE, Constants.WSDL_InterfaceFaultReference);
        n3(id, Constants.WSDL_interfaceFault, ComponentIDs.id(fltref.getInterfaceFault()));
        Direction dir = fltref.getDirection();
        mapMsgDir(id, dir);
        String label = fltref.getMessageLabel().toString();
        URI mep = getMEP((InterfaceOperation) fltref.getParent());
        mapMsgLabel(id, mep, label);
        mapSAWSDL(fltref);
    }

    /**
     * maps binding components
     * @param descID
     * @param binding
     */
    public void map(URI descID, Binding binding) {
        URI id = ComponentIDs.id(binding);
        n3(descID, Constants.WSDL_binding, id);
        n3(id, Constants.RDF_TYPE, Constants.WSDL_Binding);
        n3String(id, Constants.RDFS_label, binding.getName().getLocalPart());
        if (binding.getInterface() != null) {
            n3(id, Constants.WSDL_binds, ComponentIDs.id(binding.getInterface()));
        }
        n3(id, Constants.RDF_TYPE, binding.getType());
        BindingOperation[] ops = binding.getBindingOperations();
        for (int i = 0; i < ops.length; i++) {
            map(id, ops[i]);
        }
        BindingFault[] flts = binding.getBindingFaults();
        for (int i = 0; i < flts.length; i++) {
            map(id, flts[i]);
        }
        mapExtensions(binding);
    }

    /**
     * maps extensions of this component
     * @param binding
     */
    public void mapExtensions(Binding binding) {
        URI id = ComponentIDs.id(binding);
        SOAPBindingExtensions soap = (SOAPBindingExtensions) binding.getComponentExtensionsForNamespace(Constants.WSOAP_NS);
        if (soap != null) {
            URI mepd = soap.getSoapMepDefault();
            if (mepd != null) {
                n3(id, Constants.WSOAP_defaultSoapMEP, mepd);
            }
            n3(id, Constants.WSOAP_protocol, soap.getSoapUnderlyingProtocol());
            n3String(id, Constants.WSOAP_version, soap.getSoapVersion());
            mapSoapModules(id, soap.getSoapModules());
            String coding = soap.getHttpContentEncodingDefault();
            if (coding != null && coding.length() != 0) {
                n3String(id, Constants.WHTTP_defaultContentEncoding, coding);
            }
            String separator = soap.getHttpQueryParameterSeparatorDefault();
            if (separator != null && separator.length() != 0) {
                n3String(id, Constants.WHTTP_defaultQueryParameterSeparator, separator);
            }
            Boolean cookies = soap.isHttpCookies();
            if (cookies != null && cookies) {
                n3(id, Constants.RDF_TYPE, Constants.WHTTP_BindingUsingHTTPCookies);
            }
        }
        HTTPBindingExtensions http = (HTTPBindingExtensions) binding.getComponentExtensionsForNamespace(Constants.WHTTP_NS);
        if (http != null) {
            Boolean cookies = http.isHttpCookies();
            if (cookies != null && cookies) {
                n3(id, Constants.RDF_TYPE, Constants.WHTTP_BindingUsingHTTPCookies);
            }
            String coding = http.getHttpContentEncodingDefault();
            if (coding != null && coding.length() != 0) {
                n3String(id, Constants.WHTTP_defaultContentEncoding, coding);
            }
            String method = http.getHttpMethodDefault();
            if (method != null && method.length() != 0) {
                n3String(id, Constants.WHTTP_defaultMethod, method);
            }
            String separator = http.getHttpQueryParameterSeparatorDefault();
            if (separator != null && separator.length() != 0) {
                n3String(id, Constants.WHTTP_defaultQueryParameterSeparator, separator);
            }
        }
        mapSAWSDL(binding);
    }

    /**
     * maps soap modules of a component
     * @param id
     * @param soapModules
     */
    public void mapSoapModules(URI id, SOAPModule[] soapModules) {
        for (int i = 0; i < soapModules.length; i++) {
            SOAPModule m = soapModules[i];
            if (m.isRequired()) {
                n3(id, Constants.WSOAP_requiresSOAPModule, m.getRef());
            } else {
                n3(id, Constants.WSOAP_offersSOAPModule, m.getRef());
            }
        }
    }

    /**
     * maps soap headers of a component
     * @param id
     * @param soapHeaders
     */
    public void mapSoapHeaders(URI id, SOAPHeaderBlock[] soapHeaders) {
        for (int i = 0; i < soapHeaders.length; i++) {
            SOAPHeaderBlock h = soapHeaders[i];
            URI hid = ComponentIDs.id(h);
            if (h.isRequired()) {
                n3(id, Constants.WSOAP_requiresHeader, hid);
            } else {
                n3(id, Constants.WSOAP_offersHeader, hid);
            }
            n3(hid, Constants.RDF_TYPE, Constants.WSOAP_SOAPHeaderBlock);
            if (h.mustUnderstand()) {
                n3(hid, Constants.RDF_TYPE, Constants.WSOAP_MustUnderstandSOAPHeaderBlock);
            }
            mapQName(hid, Constants.WSDL_elementDeclaration, h.getElementDeclaration().getName());
        }
    }

    /**
     * maps binding operation components
     * @param bid
     * @param operation
     */
    public void map(URI bid, BindingOperation operation) {
        URI id = ComponentIDs.id(operation);
        n3(bid, Constants.WSDL_bindingOperation, id);
        n3(id, Constants.RDF_TYPE, Constants.WSDL_BindingOperation);
        n3(id, Constants.WSDL_binds, ComponentIDs.id(operation.getInterfaceOperation()));
        BindingMessageReference[] msgrefs = operation.getBindingMessageReferences();
        for (int i = 0; i < msgrefs.length; i++) {
            map(id, msgrefs[i]);
        }
        BindingFaultReference[] fltrefs = operation.getBindingFaultReferences();
        for (int i = 0; i < fltrefs.length; i++) {
            map(id, fltrefs[i]);
        }
        mapExtensions(operation);
    }

    /**
     * maps extensions of this component
     * @param operation
     */
    public void mapExtensions(BindingOperation operation) {
        URI id = ComponentIDs.id(operation);
        SOAPBindingOperationExtensions soap = (SOAPBindingOperationExtensions) operation.getComponentExtensionsForNamespace(Constants.WSOAP_NS);
        if (soap != null) {
            URI action = soap.getSoapAction();
            if (action != null) {
                n3(id, Constants.WSOAP_action, action);
            }
            URI soapmep = soap.getSoapMep();
            if (soapmep != null) {
                n3(id, Constants.WSOAP_soapMEP, soapmep);
            }
            mapSoapModules(id, soap.getSoapModules());
            String val;
            HTTPLocation loc = soap.getHttpLocation();
            if (loc != null) {
                val = soap.getHttpLocation().getOriginalLocation();
                if (val != null && val.length() != 0) {
                    n3String(id, Constants.WHTTP_location, val);
                }
            }
            val = soap.getHttpContentEncodingDefault();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_defaultContentEncoding, val);
            }
            val = soap.getHttpQueryParameterSeparator();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_queryParameterSeparator, val);
            }
        }
        HTTPBindingOperationExtensions http = (HTTPBindingOperationExtensions) operation.getComponentExtensionsForNamespace(Constants.WHTTP_NS);
        if (http != null) {
            String val;
            HTTPLocation loc = http.getHttpLocation();
            if (loc != null) {
                val = http.getHttpLocation().getOriginalLocation();
                if (val != null && val.length() != 0) {
                    n3String(id, Constants.WHTTP_location, val);
                }
            }
            val = http.getHttpContentEncodingDefault();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_defaultContentEncoding, val);
            }
            val = http.getHttpInputSerialization();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_inputSerialization, val);
            }
            val = http.getHttpOutputSerialization();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_outputSerialization, val);
            }
            val = http.getHttpFaultSerialization();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_faultSerialization, val);
            }
            val = http.getHttpMethod();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_method, val);
            }
            val = http.getHttpQueryParameterSeparator();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_queryParameterSeparator, val);
            }
            n3Literal(id, Constants.WHTTP_locationIgnoreUncited, http.isHttpLocationIgnoreUncited().toString(), Constants.XS_boolean);
        }
        mapSAWSDL(operation);
    }

    /**
     * maps binding fault components
     * @param bid
     * @param fault
     */
    public void map(URI bid, BindingFault fault) {
        URI id = ComponentIDs.id(fault);
        n3(bid, Constants.WSDL_bindingFault, id);
        n3(id, Constants.RDF_TYPE, Constants.WSDL_BindingFault);
        n3(id, Constants.WSDL_binds, ComponentIDs.id(fault.getInterfaceFault()));
        mapExtensions(fault);
    }

    /**
     * maps extensions of this component
     * @param fault
     */
    public void mapExtensions(BindingFault fault) {
        URI id = ComponentIDs.id(fault);
        SOAPBindingFaultExtensions soap = (SOAPBindingFaultExtensions) fault.getComponentExtensionsForNamespace(Constants.WSOAP_NS);
        if (soap != null) {
            SOAPFaultCode fc = soap.getSoapFaultCode();
            if (fc != null && fc.isQName()) {
                mapQName(id, Constants.WSOAP_faultCode, fc.getQName());
            }
            SOAPFaultSubcodes fsc = soap.getSoapFaultSubcodes();
            if (fsc != null && fsc.isQNames()) {
                URI gid = genid();
                n3(id, Constants.WSOAP_faultSubcodes, gid);
                n3(gid, Constants.RDF_TYPE, Constants.RDF_Seq);
                QName[] sc = fsc.getQNames();
                for (int i = 0; i < sc.length; i++) {
                    mapQName(gid, rdfli(i + 1), sc[i]);
                }
            }
            mapSoapModules(id, soap.getSoapModules());
            mapSoapHeaders(id, soap.getSoapHeaders());
            String val = soap.getHttpContentEncoding();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_contentEncoding, val);
            }
            mapHttpHeaders(id, soap.getHttpHeaders());
        }
        HTTPBindingFaultExtensions http = (HTTPBindingFaultExtensions) fault.getComponentExtensionsForNamespace(Constants.WHTTP_NS);
        if (http != null) {
            HTTPErrorStatusCode code = http.getHttpErrorStatusCode();
            if (code.isCodeUsed()) {
                n3Literal(id, Constants.WHTTP_errorCode, code.getCode().toString(), Constants.XS_int);
            }
            String val = http.getHttpContentEncoding();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_contentEncoding, val);
            }
            mapHttpHeaders(id, http.getHttpHeaders());
        }
        mapSAWSDL(fault);
    }

    /**
     * maps HTTP headers
     * @param id
     * @param httpHeaders
     */
    public void mapHttpHeaders(URI id, HTTPHeader[] httpHeaders) {
        for (int i = 0; i < httpHeaders.length; i++) {
            HTTPHeader h = httpHeaders[i];
            URI hid = ComponentIDs.id(h);
            if (h.isRequired()) {
                n3(id, Constants.WHTTP_requiresHeader, hid);
            } else {
                n3(id, Constants.WHTTP_offersHeader, hid);
            }
            n3(hid, Constants.RDF_TYPE, Constants.WHTTP_HTTPHeader);
            n3String(hid, Constants.WHTTP_headerName, h.getName());
            TypeDefinition type = h.getTypeDefinition();
            if (type != null) {
                mapQName(hid, Constants.WSDL_typeDefinition, type.getName());
            }
        }
    }

    /**
     * returns RDF:_nnn property (aka rdf:li) URI
     * @param n
     * @return the property uri
     */
    public static URI rdfli(int n) {
        assert n > 0;
        return URI.create(Constants.RDF_NS + "_" + n);
    }

    /**
     * maps binding message reference components
     * @param boid
     * @param reference
     */
    public void map(URI boid, BindingMessageReference reference) {
        URI id = ComponentIDs.id(reference);
        n3(boid, Constants.WSDL_bindingMessageReference, id);
        n3(id, Constants.RDF_TYPE, Constants.WSDL_BindingMessageReference);
        n3(id, Constants.WSDL_binds, ComponentIDs.id(reference.getInterfaceMessageReference()));
        mapExtensions(reference);
    }

    /**
     * maps extensions of this component
     * @param reference
     */
    public void mapExtensions(BindingMessageReference reference) {
        URI id = ComponentIDs.id(reference);
        SOAPBindingMessageReferenceExtensions soap = (SOAPBindingMessageReferenceExtensions) reference.getComponentExtensionsForNamespace(Constants.WSOAP_NS);
        if (soap != null) {
            mapSoapModules(id, soap.getSoapModules());
            mapSoapHeaders(id, soap.getSoapHeaders());
            String val = soap.getHttpContentEncoding();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_contentEncoding, val);
            }
            mapHttpHeaders(id, soap.getHttpHeaders());
        }
        HTTPBindingMessageReferenceExtensions http = (HTTPBindingMessageReferenceExtensions) reference.getComponentExtensionsForNamespace(Constants.WHTTP_NS);
        if (http != null) {
            String val = http.getHttpContentEncoding();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_contentEncoding, val);
            }
            mapHttpHeaders(id, http.getHttpHeaders());
        }
        mapSAWSDL(reference);
    }

    /**
     * maps binding fault reference components
     * @param boid
     * @param reference
     */
    public void map(URI boid, BindingFaultReference reference) {
        URI id = ComponentIDs.id(reference);
        n3(boid, Constants.WSDL_bindingFaultReference, id);
        n3(id, Constants.RDF_TYPE, Constants.WSDL_BindingFaultReference);
        n3(id, Constants.WSDL_binds, ComponentIDs.id(reference.getInterfaceFaultReference()));
        mapExtensions(reference);
    }

    /**
     * maps extensions of this component
     * @param reference
     */
    public void mapExtensions(BindingFaultReference reference) {
        URI id = ComponentIDs.id(reference);
        SOAPBindingFaultReferenceExtensions soap = (SOAPBindingFaultReferenceExtensions) reference.getComponentExtensionsForNamespace(Constants.WSOAP_NS);
        if (soap != null) {
            mapSoapModules(id, soap.getSoapModules());
        }
        mapSAWSDL(reference);
    }

    /**
     * maps service components
     * @param descID
     * @param service
     */
    public void map(URI descID, Service service) {
        URI id = ComponentIDs.id(service);
        n3(descID, Constants.WSDL_service, id);
        n3(id, Constants.RDF_TYPE, Constants.WSDL_Service);
        n3String(id, Constants.RDFS_label, service.getName().getLocalPart());
        n3(id, Constants.WSDL_implements, ComponentIDs.id(service.getInterface()));
        Endpoint[] endpoints = service.getEndpoints();
        for (int i = 0; i < endpoints.length; i++) {
            map(id, endpoints[i]);
        }
        mapSAWSDL(service);
    }

    /**
     * maps endpoint components
     * @param sid
     * @param endpoint
     */
    public void map(URI sid, Endpoint endpoint) {
        URI id = ComponentIDs.id(endpoint);
        n3(sid, Constants.WSDL_endpoint, id);
        n3(id, Constants.RDF_TYPE, Constants.WSDL_Endpoint);
        n3String(id, Constants.RDFS_label, endpoint.getName().toString());
        n3(id, Constants.WSDL_usesBinding, ComponentIDs.id(endpoint.getBinding()));
        URI addr = endpoint.getAddress();
        if (addr != null) {
            n3(id, Constants.WSDL_address, addr);
        }
        mapExtensions(endpoint);
    }

    /**
     * maps extensions of this component
     * @param endpoint
     */
    public void mapExtensions(Endpoint endpoint) {
        URI id = ComponentIDs.id(endpoint);
        SOAPEndpointExtensions soap = (SOAPEndpointExtensions) endpoint.getComponentExtensionsForNamespace(Constants.WSOAP_NS);
        if (soap != null) {
            String val = soap.getHttpAuthenticationRealm();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_authenticationRealm, val);
            }
            HTTPAuthenticationScheme scheme = soap.getHttpAuthenticationScheme();
            if (scheme != null) {
                val = scheme.toString();
                if (val != null && val.length() != 0) {
                    n3String(id, Constants.WHTTP_authenticationScheme, val);
                }
            }
        }
        HTTPEndpointExtensions http = (HTTPEndpointExtensions) endpoint.getComponentExtensionsForNamespace(Constants.WHTTP_NS);
        if (http != null) {
            String val = http.getHttpAuthenticationRealm();
            if (val != null && val.length() != 0) {
                n3String(id, Constants.WHTTP_authenticationRealm, val);
            }
            HTTPAuthenticationScheme scheme = http.getHttpAuthenticationScheme();
            if (scheme != null) {
                val = scheme.toString();
                if (val != null && val.length() != 0) {
                    n3String(id, Constants.WHTTP_authenticationScheme, val);
                }
            }
        }
        mapSAWSDL(endpoint);
    }

    /**
     * maps message label of a message
     * @param msgid
     * @param mep
     * @param label
     */
    public void mapMsgLabel(URI msgid, URI mep, String label) {
        if (Constants.WSDL_MEP_IN_ONLY.equals(mep) || Constants.WSDL_MEP_ROBUST_IN_ONLY.equals(mep) || Constants.WSDL_MEP_IN_OUT.equals(mep) || Constants.WSDL_MEP_IN_OPT_OUT.equals(mep) || Constants.WSDL_MEP_OUT_ONLY.equals(mep) || Constants.WSDL_MEP_ROBUST_OUT_ONLY.equals(mep) || Constants.WSDL_MEP_OUT_IN.equals(mep) || Constants.WSDL_MEP_OUT_OPT_IN.equals(mep)) {
            n3(msgid, Constants.WSDL_messageLabel, mep.resolve("#" + label));
        } else {
            this.warn("WARNING: unknown MEP: " + mep.toString() + " -- cannot map message label " + label);
        }
    }

    /**
     * maps message direction
     * @param msgid
     * @param dir
     */
    public void mapMsgDir(URI msgid, Direction dir) {
        if (dir.equals(Direction.IN)) {
            n3(msgid, Constants.RDF_TYPE, Constants.WSDL_InputMessage);
        } else {
            n3(msgid, Constants.RDF_TYPE, Constants.WSDL_OutputMessage);
        }
    }

    /**
     * generates a random unique identifier
     * @return a time-based UUID URN
     */
    private static synchronized URI genid() {
        return URI.create("urn:uuid:" + UUID.randomUUID());
    }

    /**
     * outputs a triple
     * @param subj
     * @param prop
     * @param obj
     */
    private void n3(URI subj, URI prop, URI obj) {
        this.rdfOutputHolder.n3(subj, prop, obj);
    }

    /**
     * outputs a triple
     * @param subj
     * @param prop
     * @param val
     */
    private void n3String(URI subj, URI prop, String val) {
        this.rdfOutputHolder.n3String(subj, prop, val);
    }

    /**
     * outputs a triple
     * @param subj
     * @param prop
     * @param val
     * @param datatype
     */
    private void n3Literal(URI subj, URI prop, String val, URI datatype) {
        this.rdfOutputHolder.n3Literal(subj, prop, val, datatype);
    }

    /**
     * outputs a warning
     * @param msg
     */
    private void warn(String msg) {
        this.rdfOutputHolder.warn(msg);
    }
}
