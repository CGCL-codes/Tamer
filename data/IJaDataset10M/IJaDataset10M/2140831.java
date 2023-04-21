package eu.vph.predict.vre.base.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import eu.vph.predict.vre.base.exception.MessageKeys;
import eu.vph.predict.vre.base.exception.VRESystemException;

/**
 * JAXB/JAXP utility class.
 *
 * @author Geoff Williams
 */
public class BaseJAXUtil {

    public static final String JAXB_FORMATTED_OUTPUT = "jaxb.formatted.output";

    public static final String JAXB_SCHEMALOCATION = "jaxb.schemaLocation";

    public static final String JAXB_NONAMESPACESCHEMALOCATION = "jaxb.noNamespaceSchemaLocation";

    private static final Log log = LogFactory.getLog(BaseJAXUtil.class);

    /**
   * Validate the provided xml.
   * 
   * @param xml XML to validate.
   * @param validNamespaces Namespaces which the VRE app is aware of.
   * @param rootElementName Name of root element (or null for no check).
   * @return Namespace of the validated document's root node/element.
   */
    public static String validateXML(final File file, final Set<String> validNamespaces, final String rootElementName) {
        log.debug("~validateXML(..) : Going to validate for root element name [" + rootElementName + "]");
        if (validNamespaces.size() == 0) log.fatal("~validateXML(..) : No valid namespaces supplied by app ctx");
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        InputStream inputStream1 = null;
        try {
            inputStream1 = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.warn("~validateXML(..) : FileNotFoundException1 [" + e.getMessage() + "]");
            throw new VRESystemException(MessageKeys.DATA_INVALID, new Object[] { "File for input stream" });
        }
        Set<String> documentNamespaces = new HashSet<String>();
        Document document = null;
        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(inputStream1);
            documentNamespaces = BaseDOMUtil.retrieveNamespaces(document);
        } catch (ParserConfigurationException pce) {
            log.warn("~validateXML(..) : ParserConfigurationException [" + pce.getMessage() + "]");
            pce.printStackTrace();
        } catch (IOException ioe) {
            log.warn("~validateXML(..) : IOException [" + ioe.getMessage() + "]");
            ioe.printStackTrace();
        } catch (SAXException se) {
            log.warn("~validateXML(..) : SAXException [" + se.getMessage() + "]");
            se.printStackTrace();
        }
        if (documentNamespaces.size() > 0) {
            boolean hasValidNamespace = false;
            for (final String documentNamespace : documentNamespaces) {
                log.debug("~validateXML(..) : Checking document namespace [" + documentNamespace + "]");
                if (validNamespaces.contains(documentNamespace)) {
                    hasValidNamespace = true;
                    break;
                }
            }
            if (!hasValidNamespace) {
                log.error("~validateXML(..) : Document namespaces found aren't listed among valid namespaces");
                return null;
            }
        } else {
            log.warn("~validateXML(..) : No namespaces found in document");
        }
        final String rootNodeNamespace = BaseDOMUtil.checkRootNode(document, rootElementName, validNamespaces);
        log.debug("~validateXML(..) : Going to validate the document against remote schema");
        BaseDOMUtil.retrieveDocument(file, false);
        return rootNodeNamespace;
    }

    /**
   * Retrieve a JAXB marshaller according to package name.
   * 
   * @param packageName Package containing JAXB objects derived from XSD.
   * @param formatted Whether to format the output or not.
   * @param noNamespaceSchemaLocation Location of noNamespaceSchemalocation (or null if not necessary).
   * @param schemaLocation Location of schema (or null if not necessary)
   * @return Marshaller appropriate for the package.
   */
    public static Marshaller retrieveMarshaller(final String packageName, final boolean formatted, final String noNamespaceSchemaLocation, final String schemaLocation) {
        log.debug("~retrieveMarshaller(..) : Retrieving marshaller");
        Marshaller marshaller = null;
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
            marshaller = jaxbContext.createMarshaller();
            if (noNamespaceSchemaLocation != null) marshaller.setProperty(JAXB_NONAMESPACESCHEMALOCATION, noNamespaceSchemaLocation);
            if (schemaLocation != null) marshaller.setProperty(JAXB_SCHEMALOCATION, schemaLocation);
            if (formatted) marshaller.setProperty(JAXB_FORMATTED_OUTPUT, true);
        } catch (JAXBException je) {
            je.printStackTrace();
            log.error("~retrieveMarshaller(..) : Failed to retrieve marshaller with JAXB error [" + je.getMessage() + "]");
            throw new VRESystemException(MessageKeys.CREATION_FAIL_GENERIC, new Object[] { "JAXBMarshaller", packageName });
        }
        return marshaller;
    }

    /**
   * Retrieve the JAXB object based on the package name and xml content.
   * 
   * @param packageName Package name, e.g. eu.vph...
   * @param xmlObject XML object in whichever form can be handled by an unmarshaller
   * @return JAXBObject.
   */
    public static Object retrieveJAXBObject(final String packageName, final File file) {
        log.debug("~retrieveJAXBObject(String,File) : retrieve JAXB obj [" + packageName + "] obj [" + file + "]");
        Object jaxbObject = null;
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            jaxbObject = unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            log.error("~retrieveJAXBObject(..) : JAXB exception [" + e.getMessage() + "]");
            e.printStackTrace();
        }
        return jaxbObject;
    }
}
