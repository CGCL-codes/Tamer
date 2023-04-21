package com.gorillalogic.compile.xmidoc;

import com.gorillalogic.compile.XMIImporter;
import com.gorillalogic.util.XMLUtil;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class XMIElementFactoryFactory {

    public static XMIElement.Factory createXMIElementFactory(InputStream xmiIn, XMIImporter imp) throws XMIElementException {
        XMIElement.Factory xmiElementFactory = null;
        Document doc = createDoc(xmiIn, imp);
        String version = getXMIVersion(doc);
        if (version == null || version.length() == 0) {
            throw new XMIElementException(imp, "cannot determine xmi version");
        } else if (version.startsWith("1.")) {
            xmiElementFactory = new com.gorillalogic.compile.xmidoc.v1.V1XMIElementFactory(doc, imp);
        } else if (version.startsWith("2.")) {
            xmiElementFactory = new com.gorillalogic.compile.xmidoc.v2.V2XMIElementFactory(doc, imp);
        } else {
            throw new XMIElementException(imp, "unsupported xmi version" + version);
        }
        return xmiElementFactory;
    }

    private static Document createDoc(InputStream in, XMIImporter imp) throws XMIElementException {
        Exception throwMe = null;
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(in);
        } catch (org.xml.sax.SAXException e) {
            throwMe = e;
        } catch (IOException e) {
            throwMe = e;
        } catch (ParserConfigurationException e) {
            throwMe = e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throwMe = e;
            }
            if (throwMe != null) {
                if (imp.getConfig().stackTraceOnException) {
                    throwMe.printStackTrace();
                }
                throw new XMIElementException(imp.getConfig().getImporter(), "Exception caught creating DOM Document: " + throwMe.getMessage());
            }
        }
        return doc;
    }

    private static String getXMIVersion(Document doc) throws XMIElementException {
        String version = null;
        Element root = doc.getDocumentElement();
        version = root.getAttribute("xmi.version");
        if (version.length() == 0) {
            Element versionEl = null;
            NodeList nodes = doc.getElementsByTagName("XMI");
            versionEl = nodes.getLength() > 0 ? (Element) nodes.item(0) : null;
            if (versionEl != null) {
                version = versionEl.getAttribute("xmi.version");
            } else if (versionEl == null) {
                versionEl = getXMIVersionElement(doc);
                if (versionEl != null) {
                    version = versionEl.getAttribute("xmi:version");
                }
            }
        }
        return version;
    }

    private static Element getXMIVersionElement(Document doc) throws XMIElementException {
        XMLUtil.XPathProcessor xp = new XMLUtil.XPathProcessor(doc);
        String xpathQuery = null;
        Element root = doc.getDocumentElement();
        xpathQuery = "/descendant-or-self::*[attribute::xmi:version]";
        Element xmiVerEl = xp.getFirstElement(xpathQuery, doc);
        return xmiVerEl;
    }
}
