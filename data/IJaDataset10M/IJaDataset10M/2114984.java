package de.tu_darmstadt.informatik.rbg.klein.osgi.navigator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameQuery;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import de.tu_darmstadt.informatik.rbg.klein.osgi.navigator.concepts.Component;
import de.tu_darmstadt.informatik.rbg.klein.osgi.navigator.concepts.Interface;
import de.tu_darmstadt.informatik.rbg.klein.osgi.navigator.concepts.Property;
import de.tu_darmstadt.informatik.rbg.klein.osgi.navigator.concepts.Reference;
import de.tu_darmstadt.informatik.rbg.klein.osgi.navigator.concepts.Service;
import de.tu_darmstadt.informatik.rbg.klein.osgi.navigator.util.QNameGenerator;

/**
 * Parses the given File and creates a representative data structure
 * in Elmo.
 * 
 * @author Tim Klein
 * @version 0.1
 *
 */
public class ManifestParser {

    private Document dom = null;

    private RDFManager rdfManager = null;

    private SesameManager sesameManager = null;

    List<Reference> referenceList = null;

    List<Property> propertyList = null;

    List<Interface> interfaceList = null;

    /**
	 * Runs the Parser with the given File.
	 * 
	 */
    public void parse(File file) {
        prepareParser(file);
        parseDocument(dom.getFirstChild());
    }

    /**
	 * Prepares the Parser.
	 * 
	 */
    private void prepareParser(File file) {
        FileInputStream fileInputStream = null;
        dom = null;
        referenceList = null;
        interfaceList = null;
        propertyList = null;
        sesameManager = null;
        referenceList = new LinkedList<Reference>();
        interfaceList = new LinkedList<Interface>();
        propertyList = new LinkedList<Property>();
        sesameManager = rdfManager.getSesameManager();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            fileInputStream = new FileInputStream(file);
            dom = db.parse(file);
            fileInputStream.close();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
	 * Parsing process.
	 * 
	 */
    private void parseDocument(Node node) {
        if (node != null) {
            if (node.getNodeName() == "component") {
                NodeList nodes = node.getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++) {
                    parseDocument(nodes.item(i));
                }
                if (interfaceList.isEmpty()) {
                    Component component = sesameManager.create(Component.class, QNameGenerator.generate());
                    component.setName(node.getAttributes().item(0).getNodeValue());
                    component.setReferences(referenceList);
                    component.setProperties(propertyList);
                } else {
                    Service service = sesameManager.create(Service.class, QNameGenerator.generate());
                    service.setName(node.getAttributes().item(0).getNodeValue());
                    service.setInterfaces(interfaceList);
                    service.setReferences(referenceList);
                    service.setProperties(propertyList);
                }
            } else if (node.getNodeName() == "service") {
                NodeList list = node.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    if (list.item(i).hasAttributes()) {
                        String interfName = null;
                        Interface interf = null;
                        Iterator interfIter = null;
                        SesameQuery<Interface> interfaceQuery = null;
                        interfName = list.item(i).getAttributes().item(0).getNodeValue();
                        interfaceQuery = sesameManager.findAll(Interface.class);
                        interfIter = interfaceQuery.iterator();
                        while (interfIter.hasNext() && interf == null) {
                            Interface tempInterf = null;
                            tempInterf = (Interface) interfIter.next();
                            if (interfName.equals(tempInterf.getName())) {
                                interf = tempInterf;
                            }
                        }
                        interfaceQuery.close();
                        if (interf == null) {
                            interf = sesameManager.create(Interface.class, QNameGenerator.generate());
                            interf.setName(interfName);
                        }
                        interfaceList.add(interf);
                    }
                }
            } else if (node.getNodeName() == "property") {
                String name = null;
                String value = null;
                Property p = null;
                name = node.getAttributes().item(0).getNodeValue();
                value = node.getAttributes().item(1).getNodeValue();
                p = sesameManager.create(Property.class, QNameGenerator.generate());
                p.setName(name);
                p.setValue(value);
                propertyList.add(p);
            } else if (node.getNodeName() == "reference") {
                NamedNodeMap attributes = node.getAttributes();
                Reference r = sesameManager.create(Reference.class, QNameGenerator.generate());
                List<Interface> interfaces = new LinkedList<Interface>();
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.item(i).getNodeName().equals("name")) {
                        r.setName(attributes.item(i).getNodeValue());
                    } else if (attributes.item(i).getNodeName().equals("target")) {
                        r.setTarget(attributes.item(i).getNodeValue());
                    } else if (attributes.item(i).getNodeName().equals("interface")) {
                        String interfName = null;
                        Interface interf = null;
                        Iterator interfIter = null;
                        SesameQuery<Interface> interfaceQuery = null;
                        interfName = attributes.item(i).getNodeValue();
                        interfaceQuery = sesameManager.findAll(Interface.class);
                        interfIter = interfaceQuery.iterator();
                        while (interfIter.hasNext() && interf == null) {
                            Interface tempInterf = null;
                            tempInterf = (Interface) interfIter.next();
                            if (interfName.equals(tempInterf.getName())) {
                                interf = tempInterf;
                            }
                        }
                        interfaceQuery.close();
                        if (interf == null) {
                            interf = sesameManager.create(Interface.class, QNameGenerator.generate());
                            interf.setName(interfName);
                        }
                        interfaces.add(interf);
                    }
                }
                r.setInterfaces(interfaces);
                referenceList.add(r);
            } else if (node.getNodeName() == "#text") {
            } else {
            }
        }
    }

    /**
	 * Sets the RdfManager.
	 * 
	 */
    public void setRdfManager(RDFManager rdfManager) {
        this.rdfManager = rdfManager;
    }
}
