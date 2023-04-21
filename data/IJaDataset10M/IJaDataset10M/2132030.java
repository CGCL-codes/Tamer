package net.woodstock.rockapi.chain.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import net.woodstock.rockapi.chain.Operation;
import net.woodstock.rockapi.chain.OperationFactory;
import net.woodstock.rockapi.chain.OperationNotFoundException;
import net.woodstock.rockapi.sys.SysLogger;
import net.woodstock.rockapi.utils.ClassLoaderUtils;
import net.woodstock.rockapi.xml.dom.XmlDocument;
import net.woodstock.rockapi.xml.dom.XmlElement;
import org.apache.commons.logging.Log;
import org.xml.sax.SAXException;

public class OperationFactoryImpl extends OperationFactory {

    public static final String OPERATIONS_XML_FILE = "META-INF/operations.xml";

    public static final String OPERATIONS_XSD_FILE = "operations.xsd";

    private static Map<String, Operation> operations;

    private static InputStream xsdDocument;

    public OperationFactoryImpl() {
        super();
        try {
            this.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Operation getOperation(String name) {
        if (!OperationFactoryImpl.operations.containsKey(name)) {
            throw new OperationNotFoundException(name);
        }
        return OperationFactoryImpl.operations.get(name);
    }

    private void init() throws SAXException, IOException, URISyntaxException {
        InputStream input = ClassLoaderUtils.getResourceAsStream(OperationFactoryImpl.OPERATIONS_XML_FILE);
        XmlDocument document = XmlDocument.read(input);
        if (false) {
            OperationFactoryImpl.xsdDocument = ClassLoaderUtils.getResourceAsStream(OperationFactoryImpl.OPERATIONS_XSD_FILE);
            document.validateSchemaWithError(OperationFactoryImpl.xsdDocument, XmlDocument.W3C_XML_SCHEMA_NS_URI);
        }
        OperationFactoryImpl.operations = new HashMap<String, Operation>();
        this.loadDocument(document);
    }

    private void loadDocument(XmlDocument document) throws SAXException, IOException, URISyntaxException {
        XmlElement root = document.getRoot();
        if (root.hasElement("include")) {
            for (XmlElement e : root.getElements("include")) {
                String file = e.getAttribute("file");
                this.getLogger().info("Loading file " + file);
                InputStream input = ClassLoaderUtils.getResourceAsStream(file);
                XmlDocument d = XmlDocument.read(input);
                this.loadDocument(d);
            }
        }
        if (root.hasElement("operation")) {
            for (XmlElement e : root.getElements("operation")) {
                String name = e.getAttribute("name");
                Operation o = new OperationImpl(e);
                this.getLogger().info("Adding operation " + name);
                OperationFactoryImpl.operations.put(name, o);
            }
        }
    }

    protected Log getLogger() {
        return SysLogger.getLogger();
    }
}
