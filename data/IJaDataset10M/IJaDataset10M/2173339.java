package bexee.wsdl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.namespace.QName;
import bexee.util.Constants;

/**
 * This class is used as a WSDL:Binding and WSDL:Service factory. It will be
 * used for the generation of concrete bindings and services for abstract web
 * services.
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/15 14:18:16 $
 * @author Patric Fornasier
 * @author Pawel Kowalski
 */
public class WSDLBindingFactory {

    /**
     * Add a binding information to the Definitions instance. Uses the given URI
     * String as the service location.
     * 
     * @param definition
     * @param uri
     * @return @throws
     *         WSDLException
     */
    public Definition addBinding(Definition definition, String uri) throws WSDLException {
        ExtensionRegistry extReg = definition.getExtensionRegistry();
        Map portTypes = definition.getPortTypes();
        for (Iterator iter = portTypes.values().iterator(); iter.hasNext(); ) {
            PortType portType = (PortType) iter.next();
            Binding binding = getBinding(portType, definition);
            definition.addBinding(binding);
            definition.addService(getService(binding, portType, definition, uri));
        }
        definition.addNamespace(Constants.WSDL_SOAP_PPREFIX, Constants.URI_WSDL_SOAP);
        definition.addNamespace(Constants.WSDL_PRFIX, Constants.URI_WSDL);
        return definition;
    }

    private Service getService(Binding binding, PortType portType, Definition def, String uri) throws WSDLException {
        ExtensionRegistry extReg = def.getExtensionRegistry();
        Service service = def.createService();
        service.setQName(new QName(portType.getQName().getNamespaceURI(), portType.getQName().getLocalPart() + Constants.SERVICE_SUFFIX));
        Port port = def.createPort();
        port.setBinding(binding);
        port.setName(portType.getQName().getLocalPart());
        service.addPort(port);
        SOAPAddress soapAddress = (SOAPAddress) extReg.createExtension(Port.class, Constants.SOAP_ADDRESS_QNAME);
        soapAddress.setLocationURI(uri);
        port.addExtensibilityElement(soapAddress);
        return service;
    }

    private Binding getBinding(PortType portType, Definition def) throws WSDLException {
        ExtensionRegistry extReg = def.getExtensionRegistry();
        Binding binding = def.createBinding();
        binding.setUndefined(false);
        binding.setQName(new QName(portType.getQName().getNamespaceURI(), portType.getQName().getLocalPart() + Constants.SOAP_BINDING_SUFFIX));
        binding.setPortType(portType);
        SOAPBinding soapBinding = (SOAPBinding) extReg.createExtension(Binding.class, Constants.SOAP_BINDING_QNAME);
        soapBinding.setStyle(bexee.util.Constants.DEFAULT_STYLE);
        soapBinding.setTransportURI(bexee.util.Constants.URI_SOAP_HTTP);
        binding.addExtensibilityElement(soapBinding);
        List operations = portType.getOperations();
        for (Iterator iterator = operations.iterator(); iterator.hasNext(); ) {
            Operation operation = (Operation) iterator.next();
            binding.addBindingOperation(getBindingOperation(operation, def));
        }
        return binding;
    }

    private BindingOperation getBindingOperation(Operation operation, Definition definition) throws WSDLException {
        ExtensionRegistry extReg = definition.getExtensionRegistry();
        BindingOperation bindingOper = definition.createBindingOperation();
        BindingInput bindingInput = definition.createBindingInput();
        BindingOutput bindingOutput = definition.createBindingOutput();
        bindingOper.setName(operation.getName());
        bindingOper.setOperation(operation);
        SOAPOperation soapOper = (SOAPOperation) extReg.createExtension(BindingOperation.class, Constants.SOAP_OPERATION_QNAME);
        soapOper.setSoapActionURI("");
        bindingOper.addExtensibilityElement(soapOper);
        bindingOper.setBindingInput(getBindingInput(operation.getInput(), definition));
        bindingOper.setBindingOutput(getBindingOutput(operation.getOutput(), definition));
        Map faults = operation.getFaults();
        for (Iterator iter = faults.values().iterator(); iter.hasNext(); ) {
            Fault fault = (Fault) iter.next();
            bindingOper.addBindingFault(getBindingFault(fault, definition));
        }
        return bindingOper;
    }

    private BindingInput getBindingInput(Input input, Definition definition) throws WSDLException {
        BindingInput bindInput = definition.createBindingInput();
        bindInput.setName(input.getMessage().getQName().getLocalPart());
        bindInput.addExtensibilityElement(getSOAPBody(bindInput.getClass(), definition));
        return bindInput;
    }

    private BindingOutput getBindingOutput(Output output, Definition definition) throws WSDLException {
        BindingOutput bindOutput = definition.createBindingOutput();
        bindOutput.setName(output.getMessage().getQName().getLocalPart());
        bindOutput.addExtensibilityElement(getSOAPBody(bindOutput.getClass(), definition));
        return bindOutput;
    }

    private BindingFault getBindingFault(Fault fault, Definition definition) throws WSDLException {
        BindingFault bindingFault = definition.createBindingFault();
        bindingFault.setName(fault.getName());
        bindingFault.addExtensibilityElement(getSOAPFault(bindingFault, definition));
        return bindingFault;
    }

    private ExtensibilityElement getSOAPBody(Class clazz, Definition definition) throws WSDLException {
        ExtensionRegistry extReg = definition.getExtensionRegistry();
        SOAPBody soapBody = (SOAPBody) extReg.createExtension(clazz.getInterfaces()[0], Constants.SOAP_BODY_QNAME);
        soapBody.setUse(Constants.DEFAULT_USE);
        soapBody.setEncodingStyles(Constants.ENCODING_STYLES);
        soapBody.setNamespaceURI(definition.getTargetNamespace());
        return soapBody;
    }

    private SOAPFault getSOAPFault(BindingFault fault, Definition definition) throws WSDLException {
        ExtensionRegistry extReg = definition.getExtensionRegistry();
        SOAPFault soapFault = (SOAPFault) extReg.createExtension(BindingFault.class, Constants.SOAP_FAULT_QNAME);
        soapFault.setUse(Constants.DEFAULT_USE);
        soapFault.setEncodingStyles(Constants.ENCODING_STYLES);
        return soapFault;
    }
}
