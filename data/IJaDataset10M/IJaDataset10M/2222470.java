package org.openejb.ui.jedi.openejb11.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openejb.ui.jedi.openejb11.ejb.MetaDataContainer;
import org.opentools.deployer.plugins.LoadException;
import org.opentools.deployer.plugins.MetaData;
import org.opentools.deployer.plugins.SaveException;
import org.opentools.deployer.xml.DTDResolver;
import org.opentools.deployer.xml.DocumentWriter;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The main metadata structure for all OpenEJB server information.  This
 * holds references to all the other metadata structures in the package.
 * It knows how to invoke XMLReader and XMLWriter to save and load the
 * data.
 *
 * @see org.openejb.ui.jedi.openejb11.server.XMLReader
 * @see org.openejb.ui.jedi.openejb11.server.XMLWriter
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.3 $
 */
public class OpenEjbMetaData extends MetaData {

    private List containers;

    private List cms;

    private List connectors;

    private ServerMetaData server;

    private SecurityMetaData security;

    private TransactionMetaData trans;

    public OpenEjbMetaData() {
        containers = new LinkedList();
        cms = new LinkedList();
        connectors = new LinkedList();
        server = new ServerMetaData();
        security = new SecurityMetaData();
        trans = new TransactionMetaData();
    }

    public void addContainer(MetaDataContainer cont) {
        containers.add(cont);
    }

    public void removeContainer(MetaDataContainer cont) {
        containers.remove(cont);
    }

    public MetaDataContainer[] getContainers() {
        return (MetaDataContainer[]) containers.toArray(new MetaDataContainer[containers.size()]);
    }

    public void addConnectionManager(CMMetaData cm) {
        cms.add(cm);
    }

    public void removeConnectionManager(CMMetaData cm) {
        cms.remove(cm);
    }

    public CMMetaData[] getConnectionManagers() {
        return (CMMetaData[]) cms.toArray(new CMMetaData[cms.size()]);
    }

    public void addConnector(ConnectorMetaData con) {
        connectors.add(con);
    }

    public void removeConnector(ConnectorMetaData con) {
        connectors.remove(con);
    }

    public ConnectorMetaData[] getConnectors() {
        return (ConnectorMetaData[]) connectors.toArray(new ConnectorMetaData[connectors.size()]);
    }

    public ServerMetaData getServerData() {
        return server;
    }

    public void setServerData(ServerMetaData data) {
        server = data;
    }

    public SecurityMetaData getSecurityData() {
        return security;
    }

    public void setSecurityData(SecurityMetaData data) {
        security = data;
    }

    public TransactionMetaData getTransactionData() {
        return trans;
    }

    public void setTransactionData(TransactionMetaData data) {
        trans = data;
    }

    public String[] loadXML(Reader in) throws LoadException, IOException {
        containers.clear();
        final List warnings = new ArrayList();
        try {
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setValidating(true);
            DocumentBuilder parser = fac.newDocumentBuilder();
            parser.setEntityResolver(new DTDResolver());
            parser.setErrorHandler(new ErrorHandler() {

                boolean enabled = true;

                public void warning(SAXParseException exception) throws SAXException {
                    if (enabled) {
                        warnings.add("XML WARNING: openejb-config.xml:" + exception.getLineNumber() + "," + exception.getColumnNumber() + " " + exception.getMessage());
                        if (exception.getMessage().equals("Valid documents must have a <!DOCTYPE declaration.")) {
                            warnings.add("  Cannot validate deployment descriptor without DOCTYPE");
                            warnings.add("  (you may want to save it from here to add the DOCTYPE).");
                            enabled = false;
                        }
                    }
                }

                public void error(SAXParseException exception) throws SAXException {
                    if (enabled) warnings.add("XML ERROR: openejb-config.xml:" + exception.getLineNumber() + "," + exception.getColumnNumber() + " " + exception.getMessage());
                }

                public void fatalError(SAXParseException exception) throws SAXException {
                }
            });
            Document doc = parser.parse(new InputSource(new BufferedReader(in)));
            new XMLReader(this, doc).load();
        } catch (SAXParseException e) {
            System.out.println("XML Exception on line: " + e.getLineNumber() + ", col " + e.getColumnNumber());
            e.printStackTrace();
            throw new IOException("XML Error: " + e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("XML Error: " + e);
        }
        return (String[]) warnings.toArray(new String[warnings.size()]);
    }

    public void saveXML(Writer out) throws SaveException, IOException {
        XMLWriter writer = null;
        try {
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setValidating(true);
            DocumentBuilder builder = fac.newDocumentBuilder();
            builder.setEntityResolver(new DTDResolver());
            Document doc = builder.newDocument();
            writer = new XMLWriter(doc, this);
            writer.save();
            DocumentWriter.writeDocument(doc, out, "-//OpenEJB//DTD OpenEJB Server 1.0//EN", "http://www.openejb.org/openejb-config.dtd");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("XML Error: " + e);
        }
        String warnings[] = writer.getWarnings();
        if (warnings.length > 0) {
            StringBuffer cat = new StringBuffer();
            for (int i = 0; i < warnings.length; i++) {
                if (i > 0) cat.append('\n');
                cat.append(warnings[i]);
            }
            throw new SaveException(cat.toString());
        }
    }
}
