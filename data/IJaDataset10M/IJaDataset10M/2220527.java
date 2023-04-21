package org.dbe.signature.xades;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.dbe.signature.xmldsig.validation.XMLDsigSignatureValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.xades.QualifyingPropertiesType;

/**
 * 
 * 
 */
public class XaDESExtendedSignatureHelper {

    private static Logger logger = Logger.getLogger(XaDESExtendedSignatureHelper.class);

    /**
	 * Type of the XaDES Object reference
	 */
    public static final String EXTENDED_SIGNATURE_REFERENCE_TYPE = "http://uri.etsi.org/01903/v1.1.1#SignedProperties";

    /**
	 * Id of the XaDES Object reference
	 */
    public static final String EXTENDED_SIGNATURE_REFERENCE = "ExtendedReference";

    /**
	 * Add a XaDES Object to the given DOM Document
	 * 
	 * @param doc
	 *            the DOM Document
	 * @param alias
	 *            userID to access the KeyStore
	 * @throws XaDESException
	 */
    public static void addXaDESObject(Document doc, X509Certificate certificate) throws XaDESException {
        try {
            boolean validateResult = XMLDsigSignatureValidator.validateSignature(doc);
            if (validateResult) {
                JAXBContext context1 = JAXBContext.newInstance(QualifyingPropertiesType.class);
                XaDESBinding bindingHelper = new XaDESBinding(certificate);
                JAXBElement<QualifyingPropertiesType> xadesObject = bindingHelper.getQualifyingProperties();
                Marshaller m = context1.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                Element object = (Element) doc.getElementsByTagName("Object").item(0);
                m.marshal(xadesObject, object);
            } else throw new XaDESException("ERROR - The signedDocument is not valid for an XaEDS extention");
        } catch (Exception e) {
            throw new XaDESException("ERROR - The signedDocument is not valid for an XaEDS extention", e);
        }
    }

    /**
	 * Create a XaDES Node by marshalling the XaDES JAXBElement in a DOM Node
	 * 
	 * @param alias
	 *            userID to access the KeyStore
	 * @return Xades node
	 * @throws XaDESException
	 */
    public static Node createXaDESNode(X509Certificate certificate) throws XaDESException {
        Document doc = null;
        try {
            JAXBContext context1 = JAXBContext.newInstance(QualifyingPropertiesType.class);
            logger.info("INFO - JAXBContext created ");
            XaDESBinding bindingHelper = new XaDESBinding(certificate);
            JAXBElement<QualifyingPropertiesType> xadesObject = bindingHelper.getQualifyingProperties();
            Marshaller m = context1.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            doc = dbf.newDocumentBuilder().newDocument();
            m.marshal(xadesObject, doc);
            logger.info("INFO - Xades Object created ");
        } catch (Exception e) {
            throw new XaDESException("ERROR - The Signed Document is not valid for an XaEDS extention", e);
        }
        return doc;
    }

    /**
	 * Unmarshall Xades Node if it existes in the given node
	 * @param node
	 * @return
	 * @throws XaDESException
	 */
    public static QualifyingPropertiesType unmarshallXadesNode(Node node) throws XaDESException {
        QualifyingPropertiesType xadesNode = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(QualifyingPropertiesType.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<QualifyingPropertiesType> element = (JAXBElement<QualifyingPropertiesType>) unmarshaller.unmarshal(node);
            xadesNode = element.getValue();
        } catch (JAXBException e) {
            throw new XaDESException("ERROR - Faild to unmarshall Xades Node from the given node " + node.getNodeName(), e);
        }
        return xadesNode;
    }
}
