package eu.more.localstorage.test.generated;

import javax.xml.namespace.QName;
import org.soda.dpws.service.MessageInfo;
import org.soda.dpws.service.MessagePartInfo;
import org.soda.dpws.wsdl.SimpleSchemaType;
import org.soda.dpws.DPWSException;
import org.soda.dpws.DPWSContext;
import org.soda.dpws.registry.ServiceClass;
import org.soda.dpws.service.binding.MessageBinding;
import org.soda.dpws.soap.Soap12Binding;
import org.soda.dpws.soap.SoapConstants;
import org.soda.dpws.wsdl.OperationInfo;
import org.soda.dpws.wsdl.PortTypeInfo;
import org.soda.dpws.wsdl.WSDLInfo;

public class LocalStorageTestWSDLInfoFactory {

    private static final String TNS = "http://www.ist-more.org/LocalStorageTest/";

    private static WSDLInfo instance = null;

    private LocalStorageTestWSDLInfoFactory() {
    }

    ;

    public static void registerWebService(ServiceClass serviceClass, String WSDLPublicLocation) throws DPWSException {
        serviceClass.addWebService(getWSDLInfo(WSDLPublicLocation));
    }

    public static WSDLInfo getWSDLInfo() throws DPWSException {
        if (instance == null) instance = buildWSDLInfo(new WSDLInfo());
        return instance;
    }

    public static WSDLInfo getWSDLInfo(String location) throws DPWSException {
        if (instance == null) instance = buildWSDLInfo(new WSDLInfo(location));
        return instance;
    }

    private static WSDLInfo buildWSDLInfo(WSDLInfo wsdlInfo) throws DPWSException {
        wsdlInfo.setBindingProvider(new org.soda.bindingprovider.JaxbBindingProvider("eu.more.localstorage.test.generated.jaxb"));
        Class serviceClass, handlerClass;
        MessageInfo message;
        MessagePartInfo msgPart;
        SimpleSchemaType xmlType;
        OperationInfo opInfo;
        Class[] params;
        try {
            serviceClass = Class.forName("eu.more.localstorage.test.generated.LocalStorageTest");
        } catch (ClassNotFoundException e) {
            throw new DPWSException("Could not load the generated eu.more.localstorage.test.generated.LocalStorageTest interface.", e);
        }
        handlerClass = null;
        PortTypeInfo typeInfo0 = new PortTypeInfo(new QName(TNS, "LocalStorageTest"), serviceClass, handlerClass, false);
        params = new Class[2];
        params[0] = DPWSContext.class;
        params[1] = eu.more.localstorage.test.generated.jaxb.LocalTest.class;
        try {
            opInfo = typeInfo0.addOperation("LocalTest", serviceClass.getMethod("LocalTest", params), "http://www.ist-more.org/LocalStorageTest/LocalStorageTest/LocalTestRequest", "http://www.ist-more.org/LocalStorageTest/LocalStorageTest/LocalTestResponse", false);
        } catch (Exception e) {
            throw new DPWSException("Could not find the LocalTest method on the generated eu.more.localstorage.test.generated.LocalStorageTest interface.", e);
        }
        message = opInfo.createMessage(new QName(TNS, "LocalTestRequest"));
        msgPart = message.addMessagePart(new QName(TNS, "parameters"), eu.more.localstorage.test.generated.jaxb.LocalTest.class);
        xmlType = new SimpleSchemaType();
        xmlType.setSchemaType(new QName("http://www.ist-more.org/LocalStorageTest/", "LocalTest"));
        msgPart.setSchemaElement(true);
        msgPart.setSchemaType(xmlType);
        opInfo.setInputMessage(message);
        message = opInfo.createMessage(new QName(TNS, "LocalTestResponse"));
        msgPart = message.addMessagePart(new QName(TNS, "parameters"), eu.more.localstorage.test.generated.jaxb.LocalTestResponse.class);
        xmlType = new SimpleSchemaType();
        xmlType.setSchemaType(new QName("http://www.ist-more.org/LocalStorageTest/", "LocalTestResponse"));
        msgPart.setSchemaElement(true);
        msgPart.setSchemaType(xmlType);
        opInfo.setOutputMessage(message);
        wsdlInfo.addPortType(typeInfo0);
        Soap12Binding binding;
        binding = new Soap12Binding(typeInfo0, "http://www.w3.org/2003/05/soap/bindings/HTTP/");
        binding.setStyle(SoapConstants.STYLE_DOCUMENT);
        binding.setSerializer(new MessageBinding());
        wsdlInfo.addBinding(binding);
        return wsdlInfo;
    }
}
