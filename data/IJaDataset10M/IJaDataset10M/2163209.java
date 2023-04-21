package org.apache.axis2.jaxws.sample.dynamic;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

@WebServiceProvider(serviceName = "GenericService", wsdlLocation = "WEB-INF/wsdl/DynamicSample.wsdl")
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
public class DynamicServiceProvider implements Provider<Source> {

    @Resource(type = WebServiceContext.class)
    public WebServiceContext context;

    public Source invoke(Source source) {
        if (source == null) {
            return source;
        }
        if (context == null) {
            throw new WebServiceException("A WebServiceException should have been injected.");
        }
        QName wsdlService = (QName) context.getMessageContext().get(MessageContext.WSDL_SERVICE);
        QName wsdlOperation = (QName) context.getMessageContext().get(MessageContext.WSDL_OPERATION);
        System.out.println("[DynamicServiceProvider]   service name: " + wsdlService);
        System.out.println("[DynamicServiceProvider] operation name: " + wsdlOperation);
        StringWriter writer = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            Result result = new StreamResult(writer);
            t.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throw new WebServiceException(e);
        } catch (TransformerFactoryConfigurationError e) {
            throw new WebServiceException(e);
        } catch (TransformerException e) {
            throw new WebServiceException(e);
        }
        String text = writer.getBuffer().toString();
        if (text != null && text.contains("throwWebServiceException")) {
            throw new WebServiceException("provider");
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(text.getBytes());
        Source srcStream = new StreamSource((InputStream) stream);
        return srcStream;
    }
}
