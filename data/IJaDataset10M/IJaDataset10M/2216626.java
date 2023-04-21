package org.openliberty.igf.attributeService.policy.application;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.openliberty.igf.attributeService.CarmlDoc;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

public class CarmlPolUtil {

    public static final String AppIdPolicy_NS = "urn:LibertyAlliance:igf:0.1:appIdPolicy";

    public static Logger logger = Logger.getLogger(CarmlPolUtil.class);

    public static final String AppIdPolicy_PREFIX = "appIdPol";

    private static DOMConfiguration config;

    private static DOMImplementation domImpl;

    private static DOMImplementationLS lsImpl;

    private static LSParser parser;

    static {
        DOMImplementationRegistry reg;
        try {
            reg = DOMImplementationRegistry.newInstance();
            domImpl = reg.getDOMImplementation("LS 3.0");
            lsImpl = (DOMImplementationLS) domImpl;
            parser = lsImpl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, CarmlDoc.XML_SCHEMA);
            config = parser.getDomConfig();
            config.setParameter("error-handler", new DOMErrorHandlerImpl());
            config.setParameter("validate", Boolean.FALSE);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        PolicyEngine.registerBuilder(PurposeAssertion.qelement, new PurposeAssertionBuilder());
    }

    public static final OMElement convertDomToOm(Element node) throws Exception {
        return XMLUtils.toOM(node);
    }

    public static final Element convertToDom(OMElement node) throws Exception {
        return XMLUtils.toDOM(node);
    }

    public static final Element policyToElement(Policy pol) {
        String spol = policyToString(pol);
        return parseStringToElement(spol);
    }

    public static final String policyToString(Policy pol) {
        String res = null;
        StringWriter swriter = new StringWriter();
        try {
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(swriter);
            pol.serialize(writer);
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
        res = swriter.toString();
        try {
            swriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Element parseStringToElement(String pol) {
        try {
            LSInput lsin = lsImpl.createLSInput();
            lsin.setStringData(pol);
            Document doc = parser.parse(lsin);
            return doc.getDocumentElement();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class DOMErrorHandlerImpl implements DOMErrorHandler {

        public boolean handleError(DOMError error) {
            CarmlPolUtil.logger.warn("Error Message:" + error.getMessage());
            if (error.getSeverity() == DOMError.SEVERITY_WARNING) return true; else return false;
        }
    }
}
