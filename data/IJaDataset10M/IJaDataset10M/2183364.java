package com.bft.commons.standard.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.bft.commons.standard.MrdnConstants;
import com.bft.commons.standard.exception.MrdnSystemException;
import com.bft.commons.standard.log.LogManager;
import com.bft.commons.standard.log.LogMessage;
import com.bft.commons.standard.util.ResourceLoader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.TraxSource;

class DefaultErrorHandler implements ErrorHandler {

    private static final String CLASS_NAME = DefaultErrorHandler.class.getName();

    public void error(SAXParseException arg0) throws SAXException {
        final String METHOD_NAME = "error(SAXParseException)";
        LogManager.log(new LogMessage(LogManager.ERROR, CLASS_NAME, METHOD_NAME, arg0.getMessage()));
    }

    public void fatalError(SAXParseException arg0) throws SAXException {
        final String METHOD_NAME = "fatalError(SAXParseException)";
        LogManager.log(new LogMessage(LogManager.ERROR, CLASS_NAME, METHOD_NAME, arg0.getMessage()));
    }

    public void warning(SAXParseException arg0) throws SAXException {
        final String METHOD_NAME = "warning(SAXParseException)";
        LogManager.log(new LogMessage(LogManager.WARNING, CLASS_NAME, METHOD_NAME, arg0.getMessage()));
    }
}

/**
 * @author Bryan_Liu
 * 
 */
public final class XMLUtility {

    private static final String CLASS_NAME = XMLUtility.class.getName();

    public static final Object bind(InputStream xmlStream, Transformer xformer) throws MrdnSystemException {
        final String METHOD_NAME = "bind(InputStream, Transformer)";
        XStream xstream = new XStream(new DomDriver());
        Writer buffer = new StringWriter();
        javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(xmlStream);
        javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(buffer);
        try {
            xformer.transform(xmlSource, result);
        } catch (TransformerException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(107, CLASS_NAME, METHOD_NAME, e);
        }
        LogManager.log(new LogMessage(LogManager.DEBUG, CLASS_NAME, METHOD_NAME, buffer.toString()));
        return xstream.fromXML(buffer.toString());
    }

    public static final Object bind(String xmlString) {
        XStream xstream = new XStream();
        return xstream.fromXML(xmlString);
    }

    public static final Object bind(String xmlSourcePath, String xform) throws MrdnSystemException {
        InputStream xmlStream = ResourceLoader.getResourceAsStream(xmlSourcePath);
        Transformer transformer = newTransformer(ResourceLoader.getResourceAsStream(xform));
        return bind(xmlStream, transformer);
    }

    public static final Object bind(String xmlString, Transformer xformer) {
        XStream xstream = new XStream();
        return xstream.fromXML(xmlString);
    }

    public static void main(String[] args) throws MrdnSystemException, DOMException, IOException, SAXException, XPathException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
        System.out.println(query(ResourceLoader.getResourceAsStream("com/meridian/commons/standard/xml/planets.xml"), "/PLANETS/PLANET", MrdnConstants.XPATH_DOCUMENT));
    }

    public static final Transformer newTransformer() throws MrdnSystemException {
        final String METHOD_NAME = "newTransformer()";
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(106, CLASS_NAME, METHOD_NAME, e);
        } catch (TransformerFactoryConfigurationError e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(106, CLASS_NAME, METHOD_NAME, e);
        }
        return transformer;
    }

    public static final Transformer newTransformer(InputStream is) throws MrdnSystemException {
        final String METHOD_NAME = "newTransformer(InputStream)";
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(is));
        } catch (TransformerConfigurationException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(106, CLASS_NAME, METHOD_NAME, e);
        } catch (TransformerFactoryConfigurationError e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(106, CLASS_NAME, METHOD_NAME, e);
        }
        return transformer;
    }

    public static final void parse(File filePath, ContentHandler contentHandler) throws MrdnSystemException {
        parse(ResourceLoader.getResourceAsStream(filePath.getName(), XMLUtility.class), contentHandler);
    }

    public static final void parse(InputStream is, ContentHandler contentHandler) throws MrdnSystemException {
        final String METHOD_NAME = "parse(InputStream, ContentHandler)";
        XMLReader parser;
        try {
            parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        } catch (SAXException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(110, CLASS_NAME, METHOD_NAME, e);
        }
        parser.setContentHandler(contentHandler);
        parser.setErrorHandler(new DefaultErrorHandler());
        try {
            parser.parse(new InputSource(is));
        } catch (IOException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(108, CLASS_NAME, METHOD_NAME, e);
        } catch (SAXException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(110, CLASS_NAME, METHOD_NAME, e);
        }
    }

    public static Object query(InputStream in, String statement, int outputType) throws MrdnSystemException {
        final String METHOD_NAME = "query(InputStream, String, int)";
        javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new org.apache.xalan.extensions.ExtensionNamespaceContext());
        xpath.setXPathFunctionResolver(new org.apache.xalan.extensions.XPathFunctionResolverImpl());
        javax.xml.xpath.XPathExpression expression = null;
        try {
            expression = xpath.compile(statement);
        } catch (XPathExpressionException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(104, CLASS_NAME, METHOD_NAME, e);
        }
        try {
            switch(outputType) {
                case MrdnConstants.XPATH_NODESET:
                    return (NodeList) expression.evaluate(new org.xml.sax.InputSource(in), XPathConstants.NODESET);
                case MrdnConstants.XPATH_DOCUMENT:
                    {
                        Transformer serializer = newTransformer();
                        serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                        Writer buffer = new StringWriter();
                        NodeList nl = (NodeList) expression.evaluate(new org.xml.sax.InputSource(in), XPathConstants.NODESET);
                        for (int counter = 0; null != nl && counter < nl.getLength(); counter++) {
                            try {
                                serializer.transform(new DOMSource(nl.item(counter)), new StreamResult(buffer));
                            } catch (TransformerException e) {
                                LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
                            }
                        }
                        LogManager.log(new LogMessage(LogManager.DEBUG, CLASS_NAME, METHOD_NAME, buffer.toString()));
                        return buffer.toString();
                    }
                default:
                    throw new MrdnSystemException(103, CLASS_NAME, METHOD_NAME, null);
            }
        } catch (XPathExpressionException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
        }
        return null;
    }

    public static final String unbind(Object obj) {
        XStream xstream = new XStream();
        return xstream.toXML(obj);
    }

    public static final String unbind(Object obj, String xform) throws MrdnSystemException {
        Transformer transformer = newTransformer(ResourceLoader.getResourceAsStream(xform));
        return unbind(obj, transformer);
    }

    public static final String unbind(Object obj, Transformer xformer) throws MrdnSystemException {
        final String METHOD_NAME = "unbind(Object, Transformer)";
        XStream xstream = new XStream();
        TraxSource traxSource = new TraxSource(obj, xstream);
        Writer buffer = new StringWriter();
        try {
            xformer.transform(traxSource, new StreamResult(buffer));
        } catch (TransformerConfigurationException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(105, CLASS_NAME, METHOD_NAME, e);
        } catch (TransformerFactoryConfigurationError e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(106, CLASS_NAME, METHOD_NAME, e);
        } catch (TransformerException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(107, CLASS_NAME, METHOD_NAME, e);
        }
        return buffer.toString();
    }

    public static final Document createDocument(String xml) throws MrdnSystemException {
        final String METHOD_NAME = "createDocument(String)";
        SAXBuilder sb = new SAXBuilder();
        StringReader read = new StringReader(xml);
        InputSource source = new InputSource(read);
        Document doc = null;
        try {
            doc = sb.build(source);
        } catch (JDOMException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(108, CLASS_NAME, METHOD_NAME, e);
        } catch (IOException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.toString()));
            throw new MrdnSystemException(108, CLASS_NAME, METHOD_NAME, e);
        }
        return doc;
    }

    public static final void output(Document export, String filePath, boolean append) {
        File file = new File(filePath);
        if (!file.exists()) {
            append = false;
        }
        if (append) {
            append(export, file);
        } else {
            output(export, file);
        }
    }

    public static final void output(Document export, File file) {
        XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
        try {
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            xmlOut.output(export, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final void append(Document export, File file) {
        XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
        SAXBuilder sb = new SAXBuilder();
        Document doc = null;
        try {
            doc = sb.build(file);
            Element root = doc.getRootElement();
            root.addContent(export.getRootElement().removeContent());
            FileWriter writer = new FileWriter(file);
            xmlOut.output(doc, writer);
            writer.close();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
