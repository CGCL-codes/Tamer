package org.akrogen.core.codegen.template.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import freemarker.ext.dom.NodeModel;

/**
 * Extend Freemarker NodeModel to manage EntityResolver.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public abstract class ExtendedNodeModel extends NodeModel {

    private static ErrorHandler errorHandler;

    private static EntityResolver entityResolver;

    public static void setErrorHandler(ErrorHandler errorHandler) {
        ExtendedNodeModel.errorHandler = errorHandler;
    }

    public static void setEntityResolver(EntityResolver entityResolver) {
        ExtendedNodeModel.entityResolver = entityResolver;
    }

    protected ExtendedNodeModel(Node node) {
        super(node);
    }

    public static NodeModel parse(InputStream f) throws SAXException, IOException, ParserConfigurationException {
        return parse(f, true, true);
    }

    public static NodeModel parse(InputStream f, boolean removeComments, boolean removePIs) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder builder = getDocumentBuilder();
        Document doc = builder.parse(f);
        if (removeComments) NodeModel.removeComments(doc);
        if (removePIs) NodeModel.removePIs(doc);
        NodeModel.mergeAdjacentText(doc);
        return NodeModel.wrap(doc);
    }

    public static NodeModel parse(InputSource is, boolean removeComments, boolean removePIs) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder builder = getDocumentBuilder();
        Document doc = builder.parse(is);
        if (removeComments && removePIs) {
            NodeModel.simplify(doc);
        } else {
            if (removeComments) NodeModel.removeComments(doc);
            if (removePIs) NodeModel.removePIs(doc);
            NodeModel.mergeAdjacentText(doc);
        }
        return NodeModel.wrap(doc);
    }

    public static NodeModel parse(InputSource is) throws SAXException, IOException, ParserConfigurationException {
        return parse(is, true, true);
    }

    public static NodeModel parse(File f, boolean removeComments, boolean removePIs) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder builder = getDocumentBuilder();
        Document doc = builder.parse(f);
        if (removeComments) removeComments(doc);
        if (removePIs) removePIs(doc);
        mergeAdjacentText(doc);
        return wrap(doc);
    }

    private static DocumentBuilder getDocumentBuilder() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder builder = getDocumentBuilderFactory().newDocumentBuilder();
        if (errorHandler != null) builder.setErrorHandler(errorHandler);
        if (entityResolver != null) builder.setEntityResolver(entityResolver);
        return builder;
    }
}
