package com.leclercb.commons.api.coder;

import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.commons.api.utils.CheckUtils;

/**
 * Encodes and decodes a factory to/from an XML stream.
 * 
 * @author Benjamin Leclerc
 */
public abstract class AbstractFactoryXMLCoder implements FactoryCoder {

    /**
	 * Name of the root tag of the XML stream.
	 */
    private String rootName;

    /**
	 * Creates a XML coder.
	 * 
	 * @param rootName
	 *            name of the root tag of the XML stream
	 */
    public AbstractFactoryXMLCoder(String rootName) {
        CheckUtils.isNotNull(rootName);
        this.rootName = rootName;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void decode(InputStream input) throws FactoryCoderException {
        CheckUtils.isNotNull(input);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);
            document.getDocumentElement().normalize();
            if (!document.getChildNodes().item(0).getNodeName().equals(this.rootName)) throw new Exception("Root name must be \"" + this.rootName + "\"");
            Node root = document.getChildNodes().item(0);
            this.decode(root);
        } catch (FactoryCoderException e) {
            throw e;
        } catch (Exception e) {
            throw new FactoryCoderException(e.getMessage(), e);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void encode(OutputStream output) throws FactoryCoderException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, null, null);
            Element root = document.createElement(this.rootName);
            document.appendChild(root);
            this.encode(document, root);
            DOMSource domSource = new DOMSource(document);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult sr = new StreamResult(output);
            transformer.transform(domSource, sr);
        } catch (Exception e) {
            throw new FactoryCoderException(e.getMessage(), e);
        }
    }

    /**
	 * Decodes a factory from the root node. All the decoded objects will be
	 * created (and inserted) by the factory.
	 * 
	 * @param root
	 *            root node
	 * @throws FactoryCoderException
	 *             if an error occurs during decoding
	 */
    protected abstract void decode(Node root) throws FactoryCoderException;

    /**
	 * Encodes a factory in the root node. All the encoded objects come from the
	 * factory.
	 * 
	 * @param document
	 *            XML document
	 * @param root
	 *            root node
	 * @throws FactoryCoderException
	 *             if an error occurs during encoding
	 */
    protected abstract void encode(Document document, Element root) throws FactoryCoderException;
}
