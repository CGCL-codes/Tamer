package eu.more.diaball.login.generated;

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

public class LoginWSDLInfoFactory {

    private static final String TNS = "http://www.ist-more.org/login/";

    private static WSDLInfo instance = null;

    private LoginWSDLInfoFactory() {
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
        wsdlInfo.setBindingProvider(new org.soda.bindingprovider.JaxbBindingProvider("eu.more.diaball.login.generated.jaxb"));
        Class serviceClass, handlerClass;
        MessageInfo message;
        MessagePartInfo msgPart;
        SimpleSchemaType xmlType;
        OperationInfo opInfo;
        Class[] params;
        try {
            serviceClass = Class.forName("eu.more.diaball.login.generated.Login");
        } catch (ClassNotFoundException e) {
            throw new DPWSException("Could not load the generated eu.more.diaball.login.generated.Login interface.", e);
        }
        try {
            handlerClass = Class.forName("eu.more.diaball.login.generated.LoginHandler");
        } catch (ClassNotFoundException e) {
            throw new DPWSException("Could not load the generated eu.more.diaball.login.generated.LoginHandler interface.", e);
        }
        PortTypeInfo typeInfo0 = new PortTypeInfo(new QName(TNS, "Login"), serviceClass, handlerClass, true);
        params = new Class[2];
        params[0] = DPWSContext.class;
        params[1] = eu.more.diaball.login.generated.jaxb.LoggedInResponse.class;
        try {
            opInfo = typeInfo0.addOperation("LoggedIn", handlerClass.getMethod("handleLoggedIn", params), null, "http://www.ist-more.org/login/Login/LoggedIn", true);
        } catch (Exception e) {
            throw new DPWSException("Could not find the LoggedIn method on the generated eu.more.diaball.login.generated.Login interface.", e);
        }
        message = opInfo.createMessage(new QName(TNS, "LoggedInResponse"));
        msgPart = message.addMessagePart(new QName(TNS, "parameters"), eu.more.diaball.login.generated.jaxb.LoggedInResponse.class);
        xmlType = new SimpleSchemaType();
        xmlType.setSchemaType(new QName("http://www.ist-more.org/login/", "LoggedInResponse"));
        msgPart.setSchemaElement(true);
        msgPart.setSchemaType(xmlType);
        opInfo.setOutputMessage(message);
        params = new Class[2];
        params[0] = DPWSContext.class;
        params[1] = eu.more.diaball.login.generated.jaxb.Echo.class;
        try {
            opInfo = typeInfo0.addOperation("echo", serviceClass.getMethod("echo", params), "http://www.ist-more.org/login/Login/echoRequest", "http://www.ist-more.org/login/Login/echoResponse", false);
        } catch (Exception e) {
            throw new DPWSException("Could not find the echo method on the generated eu.more.diaball.login.generated.Login interface.", e);
        }
        message = opInfo.createMessage(new QName(TNS, "echoRequest"));
        msgPart = message.addMessagePart(new QName(TNS, "parameters"), eu.more.diaball.login.generated.jaxb.Echo.class);
        xmlType = new SimpleSchemaType();
        xmlType.setSchemaType(new QName("http://www.ist-more.org/login/", "echo"));
        msgPart.setSchemaElement(true);
        msgPart.setSchemaType(xmlType);
        opInfo.setInputMessage(message);
        message = opInfo.createMessage(new QName(TNS, "echoResponse"));
        msgPart = message.addMessagePart(new QName(TNS, "parameters"), eu.more.diaball.login.generated.jaxb.EchoResponse.class);
        xmlType = new SimpleSchemaType();
        xmlType.setSchemaType(new QName("http://www.ist-more.org/login/", "echoResponse"));
        msgPart.setSchemaElement(true);
        msgPart.setSchemaType(xmlType);
        opInfo.setOutputMessage(message);
        params = new Class[2];
        params[0] = DPWSContext.class;
        params[1] = eu.more.diaball.login.generated.jaxb.LoggedOutResponse.class;
        try {
            opInfo = typeInfo0.addOperation("LoggedOut", handlerClass.getMethod("handleLoggedOut", params), null, "http://www.ist-more.org/login/Login/LoggedOut", true);
        } catch (Exception e) {
            throw new DPWSException("Could not find the LoggedOut method on the generated eu.more.diaball.login.generated.Login interface.", e);
        }
        message = opInfo.createMessage(new QName(TNS, "LoggedOutResponse"));
        msgPart = message.addMessagePart(new QName(TNS, "parameters"), eu.more.diaball.login.generated.jaxb.LoggedOutResponse.class);
        xmlType = new SimpleSchemaType();
        xmlType.setSchemaType(new QName("http://www.ist-more.org/login/", "LoggedOutResponse"));
        msgPart.setSchemaElement(true);
        msgPart.setSchemaType(xmlType);
        opInfo.setOutputMessage(message);
        params = new Class[2];
        params[0] = DPWSContext.class;
        params[1] = eu.more.diaball.login.generated.jaxb.Who.class;
        try {
            opInfo = typeInfo0.addOperation("Who", serviceClass.getMethod("Who", params), "http://www.ist-more.org/login/Login/WhoRequest", "http://www.ist-more.org/login/Login/WhoResponse", false);
        } catch (Exception e) {
            throw new DPWSException("Could not find the Who method on the generated eu.more.diaball.login.generated.Login interface.", e);
        }
        message = opInfo.createMessage(new QName(TNS, "WhoRequest"));
        msgPart = message.addMessagePart(new QName(TNS, "parameters"), eu.more.diaball.login.generated.jaxb.Who.class);
        xmlType = new SimpleSchemaType();
        xmlType.setSchemaType(new QName("http://www.ist-more.org/login/", "Who"));
        msgPart.setSchemaElement(true);
        msgPart.setSchemaType(xmlType);
        opInfo.setInputMessage(message);
        message = opInfo.createMessage(new QName(TNS, "WhoResponse"));
        msgPart = message.addMessagePart(new QName(TNS, "parameters"), eu.more.diaball.login.generated.jaxb.WhoResponse.class);
        xmlType = new SimpleSchemaType();
        xmlType.setSchemaType(new QName("http://www.ist-more.org/login/", "WhoResponse"));
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
