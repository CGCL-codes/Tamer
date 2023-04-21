package com.ogc2wsdl.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.ogc2java.classes.wfs.FeatureType;
import com.ogc2java.parsers.WFSParser;
import com.ogc2soa.exceptions.NoMetadataException;

/**
 * This class generates a SOAP service from a WFS file.<br>
 * It uses the WFSParser to retrieve the metadata classes
 * Datas are then written in the WSDL with the makeWSDL method.
 * It can generate two types of structures<br>
 * <list>
 * 	<li> a <b>unique service</b> structure&nbsp;:
 * 		<p>This means that only one wsdl is generated and all the layers are described in it</p>
 * 	</li>
 * 	<li> a <b>service per layer</b> structure&nbsp;:
 * 		<p>This means that each layer will have its own wsdl.
 * 			If you are generating services, then each layer will be a service
 * 			and you will not be able to specify the layer name. This means you
 * 			cannot ask the WMS server for layers to overlay.</p>
 * 	</li>
 * </list>
 * @author Andry Rabemanantsoa
 *
 */
public class WFS2WSDL implements OGC2WSDL {

    /**
	 * Parser to get the metadatas
	 * @see com.ogc2java.parsers.WFSParser
	 */
    protected WFSParser parser;

    /**
	 * Name of the service that will be used
	 */
    protected String serviceName;

    /**
	 * boolean to see if the user needs a "unique service" or a "service per coverage" structure
	 */
    protected boolean uniqueService;

    /**
	 * The current Coverage.
	 */
    protected FeatureType currentFeatureType;

    /**
	 * Constructor of the class
	 * 
	 * @param WFSFileURL path of the WFS File
	 * @param generateOneService true if you want one unique service, false if you want a service
	 *  per feature
	 * @throws NoMetadataException 
	 */
    public WFS2WSDL(String WFSFileURL, boolean generateOneService) throws NoMetadataException {
        this.parser = new WFSParser(WFSFileURL);
        this.parser.run();
        this.uniqueService = generateOneService;
        this.serviceName = "WFS4Axis2";
    }

    public void copyXSD(File servicePath) {
        throw new java.lang.UnsupportedOperationException("This method should not be used in class " + this.getClass());
    }

    public void makeService(String path, String serviceName) {
        if (serviceName == null) {
            this.serviceName = "WFS4Axis2";
        } else {
            this.serviceName = serviceName;
        }
        File servicePath = null;
        String pathName = ".";
        if (path == null) {
            System.out.println("Service will be put in current directory");
        } else {
            pathName = path;
        }
        servicePath = new File(new String(pathName + "/" + this.serviceName + "/META-INF"));
        servicePath.mkdirs();
        if (this.uniqueService) {
            this.makeServiceUnique(servicePath.toString());
        } else {
            this.makeServiceGroup(servicePath.toString());
        }
    }

    public void makeWSDL(String path, String serviceName, String originalWSDL) {
        if (serviceName == null) {
            serviceName = this.serviceName;
        }
        System.out.println("\n\t* Generating WSDL File");
        String address = "";
        String wsdlURI;
        if (originalWSDL == null) {
            wsdlURI = this.getClass().getClassLoader().getResource("wsdl/WFSDefault.wsdl").toString();
        } else {
            wsdlURI = originalWSDL;
        }
        WSDLFactory wsdlFactory;
        if (path == null) {
            address = "./" + serviceName + ".wsdl";
        } else {
            address = path + "/" + serviceName + ".wsdl";
        }
        try {
            wsdlFactory = WSDLFactory.newInstance();
            WSDLReader wsdlReader = wsdlFactory.newWSDLReader();
            wsdlReader.setFeature("javax.wsdl.verbose", false);
            wsdlReader.setFeature("javax.wsdl.importDocuments", true);
            System.out.println("\t\t- Reading default File");
            Definition definition = wsdlReader.readWSDL(wsdlURI);
            if (this.uniqueService) {
                System.out.println("\t\t- Adding Features to WSDL File");
                for (FeatureType l : this.parser.getFeatureTypeList()) {
                    this.currentFeatureType = l;
                    definition.getTypes().addExtensibilityElement(this.addFeatureType());
                }
            } else {
                definition.getTypes().addExtensibilityElement(this.addFeatureType());
            }
            System.out.println("\t\t- Name of service will be : " + serviceName);
            Service service = definition.getService(new QName("http://wfs.wrapper.service.org/", "WFS4Axis2"));
            if (service != null) {
                service.setQName(new QName("http://wfs.wrapper.service.org/", serviceName));
                List list = service.getPort("WFSSOAP11port_http").getExtensibilityElements();
                boolean SOAPAddress = false;
                int i = 0;
                ExtensibilityElement Ele = null;
                while (!SOAPAddress && i < list.size()) {
                    Ele = (ExtensibilityElement) list.get(i);
                    if (Ele.getElementType().getLocalPart().equalsIgnoreCase("address")) {
                        SOAPAddress = true;
                        SOAPAddress soap = (SOAPAddress) Ele;
                        String URI = soap.getLocationURI().replace("WFS4Axis2", serviceName);
                        soap.setLocationURI(URI);
                    }
                    i++;
                }
                list = service.getPort("WFSSOAP12port_http").getExtensibilityElements();
                SOAPAddress = false;
                i = 0;
                while (!SOAPAddress && i < list.size()) {
                    Ele = (ExtensibilityElement) list.get(i);
                    if (Ele.getElementType().getLocalPart().equalsIgnoreCase("address")) {
                        SOAPAddress = true;
                        SOAP12Address soap = (SOAP12Address) Ele;
                        String URI = soap.getLocationURI().replace("WFS4Axis2", serviceName);
                        soap.setLocationURI(URI);
                    }
                    i++;
                }
            }
            System.out.println("\t\t- Writing to :" + address);
            WSDLWriter wsdlWriter = wsdlFactory.newWSDLWriter();
            File file = new File(address);
            FileOutputStream out = new FileOutputStream(file);
            wsdlWriter.writeWSDL(definition, out);
            System.out.println("\n******************* WSDL Generation done");
        } catch (WSDLException e) {
            System.err.println("An error occured.\n" + "Verify that the default wsdl file is in " + wsdlURI + "\nOr that the file " + address + " is not protected.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.err.println("\nCouldn't open the file " + address);
            e.printStackTrace();
        }
    }

    public void makeWSDLGroup(String servicePath, String serviceName, String originalWSDL) {
        if (serviceName == null) {
            this.serviceName = serviceName;
        }
        for (int j = 0; j < this.parser.getFeatureTypeList().size(); j++) {
            FeatureType ft = this.parser.getFeatureTypeList().get(j);
            this.currentFeatureType = ft;
            this.makeWSDL(servicePath, new String(this.serviceName + "-FeatureType_" + j + "-" + ft.getName()), originalWSDL);
        }
    }

    public void makeXML(String address) {
        System.out.println("\n\t* Generating service.xml");
        InputStream defaultFile;
        System.out.println("\t\t- Retrieving the default services.xml");
        defaultFile = this.getClass().getClassLoader().getResourceAsStream("wsdl/WFSservices.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            Document dom = db.parse(defaultFile);
            dom.getFirstChild();
            System.out.println("\t\t- Adding parameters");
            this.setParameters(dom.getElementsByTagName("parameter"), null);
            System.out.println("\t\t- Changing service name : " + this.serviceName);
            dom.getElementsByTagName("service").item(0).getAttributes().item(0).setTextContent(this.serviceName);
            System.out.println("\t\t- Adding description");
            dom.getElementsByTagName("Description").item(0).setTextContent(this.parser.getServiceDescription());
            OutputFormat format = new OutputFormat(dom);
            format.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(new File(address)), format);
            serializer.serialize(dom);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("******************* services.xml Generation done");
    }

    private void makeServiceGroup(String servicePath) {
        for (int j = 0; j < this.parser.getFeatureTypeList().size(); j++) {
            FeatureType ft = this.parser.getFeatureTypeList().get(j);
            this.currentFeatureType = ft;
            this.makeWSDL(servicePath, new String(this.serviceName + "-FeatureType_" + j + "-" + ft.getName()), null);
        }
        this.makeGroupXML(new String(servicePath + "/services.xml"));
    }

    private void makeServiceUnique(String servicePath) {
        this.makeWSDL(servicePath, this.serviceName, null);
        this.makeXML(new String(servicePath + "/services.xml"));
    }

    /**
	 * Adds the current layer into a DOM element
	 * @return the extensible element
	 */
    private ExtensibilityElement addFeatureType() {
        UnknownExtensibilityElement ele = new UnknownExtensibilityElement();
        ele.setElement(this.currentFeatureType.toXML());
        ele.setElementType(new QName("http://wfs.wrapper.service.org/classes/xsd", "FeatureType"));
        return ele;
    }

    /**
	 * set the parameters of the services.xml
	 * @param parameters
	 * @param typeName the name of the feature type for a service group
	 */
    private void setParameters(NodeList parameters, String typeName) {
        for (int i = 0; i < parameters.getLength(); i++) {
            Node param = parameters.item(i);
            NamedNodeMap p = param.getAttributes();
            for (int j = 0; j < p.getLength(); j++) {
                if (p.item(j).getTextContent().equalsIgnoreCase("WFS_URL")) {
                    System.out.println("\t\t- Adding parameter WFS_URL : " + this.parser.getWFSPath());
                    param.setTextContent(this.parser.getWFSPath());
                } else if (p.item(j).getTextContent().equalsIgnoreCase("UniqueFeature")) {
                    System.out.println("\t\t- Adding parameter UniqueFeature with value : " + this.uniqueService);
                    param.setTextContent(Boolean.toString(this.uniqueService));
                } else if (p.item(j).getTextContent().equalsIgnoreCase("TypeName") && !this.uniqueService) {
                    if (typeName == null) {
                        typeName = "";
                    }
                    System.out.println("\t\t- Adding parameter TypeName : " + typeName);
                    param.setTextContent(typeName);
                }
            }
        }
    }

    /**
	 * this method generates a services.xml file for a group of services
	 * @param address the address to which the file will be saved
	 */
    private void makeGroupXML(String address) {
        System.out.println("\n\t* Generating service.xml");
        InputStream defaultFile = this.getClass().getClassLoader().getResourceAsStream("wsdl/WFSservices.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            Document dom = db.parse(defaultFile);
            Element root = dom.getDocumentElement();
            Node defaultService = root.getElementsByTagName("service").item(0);
            root.removeChild(defaultService);
            for (int j = 0; j < this.parser.getFeatureTypeList().size(); j++) {
                FeatureType ft = this.parser.getFeatureTypeList().get(j);
                Element featEle = dom.createElement("service");
                featEle.setAttribute("name", new String(this.serviceName + "-FeatureType_" + j + "-" + ft.getName()));
                for (int i = 0; i < defaultService.getChildNodes().getLength(); i++) {
                    featEle.appendChild(defaultService.getChildNodes().item(i).cloneNode(true));
                }
                featEle.getElementsByTagName("Description").item(0).setTextContent(ft.toString());
                this.setParameters(featEle.getElementsByTagName("parameter"), ft.getName());
                root.appendChild(featEle);
            }
            OutputFormat format = new OutputFormat(dom);
            format.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(new File(address)), format);
            serializer.serialize(dom);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("******************* services.xml Generation done");
    }
}
