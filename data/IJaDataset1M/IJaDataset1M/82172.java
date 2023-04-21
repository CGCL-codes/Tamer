package it.uniroma1.dis.omega.upnpqos.argument;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import it.uniroma1.dis.omega.upnpqos.structure.v3LinkReachableMacsExtension;
import it.uniroma1.dis.omega.upnpqos.utils.Util;
import it.uniroma1.dis.omega.upnpqos.utils.Validator;
import org.cybergarage.upnp.Argument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RotameterInformation extends LinkedList<v3LinkReachableMacsExtension> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final String xsd = "/schemas/RotameterInformation-v3-20080624.xsd";

    private static final String arg_name = "RotameterInformation";

    private static final String arg_type = "A_ARG_TYPE_" + arg_name;

    protected Node nextVersionNode;

    public RotameterInformation() {
        super();
        init();
    }

    /**
	 * 
	 * @param n
	 * @throws ArgumentException
	 */
    public RotameterInformation(Node n) throws ArgumentException {
        super();
        init();
        readNode(n);
        checkMandatoryFields();
    }

    protected void init() {
        nextVersionNode = null;
    }

    /**
	 * 
	 * @param arg 
	 * @throws ArgumentException
	 */
    public RotameterInformation(Argument arg) throws ArgumentException {
        super();
        readArgument(arg);
        checkMandatoryFields();
    }

    protected void readArgument(Argument arg) throws ArgumentException {
        if (arg != null) {
            if (arg.getRelatedStateVariableName().equals(arg_type)) {
                String xml = arg.getValue();
                URL xsdUrl;
                try {
                    xsdUrl = new URL("http://" + InetAddress.getLocalHost().getHostAddress() + ":8080" + xsd);
                    Document doc = Validator.validateXmlAgainstXsd(xsdUrl.openStream(), xml);
                    if (doc != null) {
                        readXML(doc);
                    } else {
                        throw new ArgumentException(arg.getName() + " argument does not contain a well formed xml fragment");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new ArgumentException("The argument differs from " + arg_type);
            }
        }
    }

    private void checkMandatoryFields() throws ArgumentException {
        checkMandatoryFields("");
    }

    protected void checkMandatoryFields(String msg) throws ArgumentException {
        int count = 0;
        if (!msg.equals("")) count++;
        if (size() == 0) {
            if (count > 0) msg += "and ";
            msg += "LinkReachableMacs ";
            count++;
        }
        if (count > 0) {
            if (count > 1) msg += "are "; else msg += "is ";
            msg += "missing";
            throw new ArgumentException(msg);
        }
    }

    protected void readNode(Node node) throws ArgumentException {
        Node n = null;
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            n = nl.item(i);
            if (n.getNodeName().endsWith("LinkReachableMacs")) {
                v3LinkReachableMacsExtension lrm = new v3LinkReachableMacsExtension(n);
                add(lrm);
            } else if (n.getNodeName().endsWith("v4")) {
                nextVersionNode = n;
            } else if (Util.ignoreTag(n.getNodeName())) {
            } else {
                throw new ArgumentException(arg_name + " structure contains the following unknown tag: " + n.getNodeName());
            }
        }
    }

    private void readXML(Document doc) throws ArgumentException {
        Node n = Util.standardReadXML(doc, arg_name);
        readNode(n);
    }

    /**
	 * 
	 * @return the XML fragment representing a full XML file
	 */
    public String getXMLFile() {
        String attributes = " xsi:schemaLocation=\"http://www.upnp.org/schemas/RotameterInformation.xsd http://localhost:8080/schemas/RotameterInformation-v3-20080624.xsd\"" + " xmlns=\"http://www.upnp.org/schemas/RotameterInformation.xsd\"" + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + getXMLFragment(attributes, "", "");
    }

    /**
	 * 
	 * @return the XML fragment with no XML header
	 */
    public String getXMLFragment() {
        return getXMLFragment("", "", "");
    }

    public String getXMLFragment(String namespace) {
        return getXMLFragment("", "", namespace);
    }

    public String getXMLFragment(String namespace, String externalTag) {
        return getXMLFragment("", "", namespace, externalTag);
    }

    protected String getXMLFragment(String attributes, String extension, String namespace) {
        return getXMLFragment(attributes, extension, namespace, arg_name);
    }

    /**
	 * 
	 * @param attribute namespaces, namespaceLocation, etc.
	 * @param extension next version tags
	 * @param namespace should be comprehensive of ":"!
	 * @param externalTag rename the TrafficDescriptor tag with something else...
	 * @return
	 */
    protected String getXMLFragment(String attributes, String extension, String namespace, String externalTag) {
        String xml = "<" + externalTag + attributes + ">";
        for (int i = 0; i < size(); i++) {
            xml += get(i).getXMLFragment(namespace);
        }
        xml += extension + "</" + externalTag + ">";
        return xml;
    }
}
