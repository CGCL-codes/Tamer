package de.campussource.cse.xslttransform;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.netbeans.j2ee.wsdl.lsfclientadapter.xslttransform.XSLTTransformPortType;

/**
 *
 * @author pete
 */
@WebService(serviceName = "XSLTTransformService", portName = "XSLTTransformPort", endpointInterface = "org.netbeans.j2ee.wsdl.lsfclientadapter.xslttransform.XSLTTransformPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/LSFClientAdapter/XSLTTransform", wsdlLocation = "META-INF/wsdl/XSLTTransform/XSLTTransform.wsdl")
@Stateless
public class XSLTTransform implements XSLTTransformPortType {

    private static final String XSLT_FILE = "/InboundTransformation.xsl";

    public java.lang.String xsltTransformOperation(java.lang.String inputXML) {
        String transformedXML = null;
        try {
            InputStream is = getClass().getResourceAsStream(XSLT_FILE);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(is));
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new StreamSource(new ByteArrayInputStream(inputXML.getBytes())), new StreamResult(stringWriter));
            transformedXML = stringWriter.getBuffer().toString();
        } catch (TransformerException ex) {
            Logger.getLogger(XSLTTransform.class.getName()).log(Level.SEVERE, "TransformerException", ex);
        }
        return transformedXML;
    }
}
