package org.openejb.alt.assembler.modern.jar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openejb.alt.assembler.modern.ContainerMetaData;
import org.openejb.alt.assembler.modern.LoadException;
import org.openejb.alt.assembler.modern.jar.ejb11.EJB11MetaData;
import org.openejb.alt.assembler.modern.xml.DTDResolver;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The main metadata structure for EJB JAR metadata.  Since all the OpenEJB
 * classes extend the normal EJB classes, both types of information
 * (EJB standard and OpenEJB extensions) are available through this class.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.3 $
 */
public class OpenEjbMetaData extends EJB11MetaData {

    private List containers;

    public OpenEjbMetaData() {
        containers = new LinkedList();
    }

    public OpenEjbMetaData(EJB11MetaData source) {
        super(source);
        clearEJBs();
        clearSecurityRoles();
        containers = new LinkedList();
    }

    public void addContainer(ContainerMetaData cont) {
        containers.add(cont);
    }

    public void removeContainer(ContainerMetaData cont) {
        containers.remove(cont);
    }

    public ContainerMetaData[] getContainers() {
        return (ContainerMetaData[]) containers.toArray(new ContainerMetaData[containers.size()]);
    }

    public String[] loadXML(EJB11MetaData source, Reader in) throws LoadException, IOException {
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
                        warnings.add("XML WARNING: openejb-jar.xml:" + exception.getLineNumber() + "," + exception.getColumnNumber() + " " + exception.getMessage());
                        if (exception.getMessage().equals("Valid documents must have a <!DOCTYPE declaration.")) {
                            warnings.add("  Cannot validate deployment descriptor without DOCTYPE");
                            warnings.add("  (you may want to save it from here to add the DOCTYPE).");
                            enabled = false;
                        }
                    }
                }

                public void error(SAXParseException exception) throws SAXException {
                    if (enabled) warnings.add("XML ERROR: openejb-jar.xml:" + exception.getLineNumber() + "," + exception.getColumnNumber() + " " + exception.getMessage());
                }

                public void fatalError(SAXParseException exception) throws SAXException {
                }
            });
            Document doc = parser.parse(new InputSource(new BufferedReader(in)));
            XMLReader reader = new XMLReader(this, source, doc);
            reader.load();
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
}
