package ti.plato.components.logger.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ti.mcore.Environment;
import ti.mcore.u.StringUtil;
import ti.mcore.u.log.PlatoLogger;

/**
 * Utilities for transforming XML documents
 * 
 * @author alex.k@ti.com
 */
public class XmlTransformerUtil {

    private static final PlatoLogger LOGGER = PlatoLogger.getLogger(XmlTransformerUtil.class);

    private static final ErrorListener TRANSFORMER_ERROR_LISTENER = new ErrorListener() {

        public void warning(TransformerException exception) throws TransformerException {
            LOGGER.logWarning(exception);
            throw exception;
        }

        public void error(TransformerException exception) throws TransformerException {
            LOGGER.logError(exception);
            throw exception;
        }

        public void fatalError(TransformerException exception) throws TransformerException {
            LOGGER.logError(exception);
            throw exception;
        }
    };

    private XmlTransformerUtil() {
    }

    /**
	 * Return a non-<code>null</code> file, or return <code>null</code> and
	 * log errors.
	 * 
	 * @param path
	 * @return File, <code>null</code> if does not exist.
	 * 
	 * @author alex.k@ti.com
	 */
    private static File getFile_logErrors(String path) {
        if (path == null || path.equals(StringUtil.EMPTY_STR)) {
            LOGGER.logError("path == \"" + path + "\"");
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            LOGGER.logError("path \"" + path + "\" does not exist.");
            return null;
        }
        return file;
    }

    /**
	 * @param xmlPath
	 * @param xslPath
	 * 
	 * @author alex.k@ti.com
	 */
    public static void transformXml(String xmlPath, String xslPath, OutputStream outStream) {
        final String fName = "transformXml";
        File xmlFile = getFile_logErrors(xmlPath);
        if (xmlFile == null) {
            return;
        }
        File xslFile = getFile_logErrors(xslPath);
        if (xslFile == null) {
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.logError(e);
            return;
        }
        Document document;
        try {
            document = builder.parse(xmlFile);
        } catch (SAXException e) {
            LOGGER.logError(e);
            return;
        } catch (IOException e) {
            LOGGER.logError(e);
            return;
        }
        Transformer transformer = getTransformer(xslFile);
        if (transformer == null) {
            LOGGER.logError(fName + ": transformer == null");
            return;
        }
        StreamResult streamResult = new StreamResult(outStream);
        transformNode(document, transformer, streamResult);
    }

    /**
	 * @param xslFile
	 * @return
	 *
	 * @author alex.k@ti.com
	 */
    public static Transformer getTransformer(File xslFile) {
        if (xslFile == null) {
            String msg = "xslFile == null";
            LOGGER.logError(msg);
            Environment.getEnvironment().showErrorMessage(msg);
            return null;
        }
        if (!xslFile.exists()) {
            String msg = "xslFile \"" + xslFile.toString() + "\" does not exist.";
            LOGGER.logError(msg);
            Environment.getEnvironment().showErrorMessage(msg);
            return null;
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        StreamSource stylesource = new StreamSource(xslFile);
        Transformer transformer = null;
        try {
            transformer = tFactory.newTransformer(stylesource);
            transformer.setErrorListener(TRANSFORMER_ERROR_LISTENER);
        } catch (TransformerConfigurationException e) {
            LOGGER.logError(xslFile.toString(), e);
            Environment.getEnvironment().showErrorMessage(xslFile.toString() + ": " + e.toString());
            return null;
        }
        return transformer;
    }

    /**
	 * @param node
	 * @param transformer
	 * @param stylesource
	 * @param result
	 *
	 * @author alex.k@ti.com
	 */
    public static void transformNode(Node node, Transformer transformer, StreamResult result) {
        final String fName = "transformNode(node, transformer, result)";
        if (node == null) {
            LOGGER.logError(fName + ": node == null");
            return;
        }
        if (transformer == null) {
            LOGGER.logError(fName + ": transformer == null");
            return;
        }
        if (result == null) {
            LOGGER.logError(fName + ": result == null");
            return;
        }
        DOMSource source = new DOMSource(node);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            return;
        }
    }
}
