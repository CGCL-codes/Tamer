package api.client.bpmModel;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import api.client.genClient.GClient;
import api.server.editWSDL.WSDLParser;
import com.ibm.wsdl.MessageImpl;

/**
 * A Container is a component which embraces several commands.
 * 
 * @author M. Weber
 */
public class Container extends Component {

    /**
	 * HashMap which includes entries of the name of a bpel specific command and
	 * the command itself
	 */
    private static HashMap<String, BPELCommand> bpelData = new HashMap<String, BPELCommand>();

    /**
	 * buf Buffer to generate source code. Its faster than string concatination.
	 */
    public StringBuffer buf = new StringBuffer();

    /**
	 * namespace Namespace context of the container.
	 */
    public URL namespace;

    /**
	 * 
	 * @element-type Component
	 */
    private HashMap<String, Component> myComponent = new HashMap<String, Component>();

    /**
	 * namespaceid Namespace identifier.
	 */
    public String namespaceid;

    /**
	 * platform independent command for a new Line (instead of "\n")
	 */
    private String nLine = System.getProperty("line.separator");

    /**
	 * 
	 * @param t
	 *            Visitor which is used (BPEL for now, later also SCAVisitor or
	 *            something else)
	 * @return a StringBuffer with content of this container
	 */
    public StringBuffer accept(final Visitor t) {
        return t.visit(this);
    }

    /**
	 * adds a component to the myComponent HashMap
	 * 
	 * @param com
	 */
    public final void addComponent(final Component com) {
        myComponent.put(com.getName(), com);
    }

    public void excecute() {
    }

    /**
	 * proofs if the name is in the HashMap which includes the bpel commands
	 * 
	 * @param name
	 * @return
	 */
    protected static boolean existsBpelData(final String name) {
        return bpelData.containsKey(name);
    }

    /**
	 * Generates the infrastructure for BPELCommands; 
	 * Every implemented BPELCommand calls this method
	 * 
	 * @param partnerLink partnerling
	 * @param operation operation 
	 * @param ns namespace
	 * @param url url
	 * @param pName partnerlinkname
	 */
    public static void genInfra(final String partnerLink, final String operation, final String ns, final String url, final String pName) {
        if (!Container.existsBpelData(url + pName + operation + "p")) {
            BPELCommand bCommandImport = new BPELCommand("import");
            BPELCommand bCommandPLink = new BPELCommand("partnerLink");
            BPELCommand bCommandVariable1 = new BPELCommand("variable");
            BPELCommand bCommandVariable2 = new BPELCommand("variable");
            bCommandImport.addUtility(new Attribute("importType", "http://schemas.xmlsoap.org/wsdl/"));
            bCommandImport.addUtility(new Attribute("location", url));
            bCommandImport.addUtility(new Attribute("namespace", "http://" + pName));
            bCommandImport.setNamespace(ns);
            bCommandPLink.addUtility(new Attribute("partnerLinkType", ns + ":" + partnerLink));
            bCommandPLink.addUtility(new Attribute("myRole", operation));
            bCommandPLink.addUtility(new Attribute("partnerRole", operation));
            bCommandPLink.addUtility(new Attribute("name", partnerLink));
            if (url == "wsdl/echoDefs.wsdl") {
                bCommandVariable1.addUtility(new Attribute("name", "echoRequest"));
                bCommandVariable1.addUtility(new Attribute("messageType", ns + ":echoRequest"));
                bCommandVariable2.addUtility(new Attribute("name", "echoResponse"));
                bCommandVariable2.addUtility(new Attribute("messageType", ns + ":echoResponse"));
            } else {
                GClient g = new GClient(url);
                String inputVariable = ((MessageImpl) g.getMessages().get(0)).getQName().getLocalPart();
                String outputVariable = ((MessageImpl) g.getMessages().get(1)).getQName().getLocalPart();
                bCommandVariable1.addUtility(new Attribute("name", inputVariable));
                bCommandVariable1.addUtility(new Attribute("messageType", ns + ":" + inputVariable));
                bCommandVariable2.addUtility(new Attribute("name", outputVariable));
                bCommandVariable2.addUtility(new Attribute("messageType", ns + ":" + outputVariable));
            }
            Container.setBpelData(url + pName + operation + "i", bCommandImport);
            Container.setBpelData(url + pName + operation + "p", bCommandPLink);
            Container.setBpelData(url + pName + operation + "v1", bCommandVariable1);
            Container.setBpelData(url + pName + operation + "v2", bCommandVariable2);
        }
    }

    /**
	 * 
	 * @param name identitfier
	 * @return the BPELCommand which fits to the name
	 */
    protected static BPELCommand getBpelData(final String name) {
        return bpelData.get(name);
    }

    public final Iterator<Component> getChildrenIt() {
        return myComponent.values().iterator();
    }

    /**
	 * reads the needed wsdls and creates a vector in which includes a string
	 * with the content of the wsdl
	 * 
	 * @return a vector with the needed wsdls saved as strings
	 */
    public final Vector<String> getNeededWSDLs() {
        Vector<String> v = new Vector<String>();
        String url;
        Iterator<BPELCommand> commands = bpelData.values().iterator();
        while (commands.hasNext()) {
            BPELCommand comm = commands.next();
            if (comm.getName() == "import") {
                url = ((Attribute) comm.getUtility("location")).getValue();
                if (url.startsWith("http://")) {
                    v.add(WSDLParser.getOriginalWSDL(url));
                }
            }
        }
        return v;
    }

    /**
	 * 
	 * @param s
	 *            string which identify the item which will be remove
	 */
    public final void removeComponent(final String s) {
        myComponent.remove(s);
    }

    protected static void setBpelData(final String name, final BPELCommand b) {
        bpelData.put(name, b);
    }

    /**
	 * creates a BPEL-file in which are all necessary items
	 * 
	 * @param name
	 *            name of the service
	 * @return the content of the BPEL-File as a string
	 */
    public final String toBPELFile(final String name) {
        buf.delete(0, buf.length());
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buf.append(nLine);
        buf.append("<process name=\"");
        buf.append(name);
        buf.append("\" suppressJoinFailure=\"yes\"");
        buf.append(" targetNamespace=\"http://");
        buf.append(name);
        buf.append("\" xmlns=\"http://schemas.xmlsoap.org/ws/2003/03/business-process/\"");
        buf.append(" xmlns:bpws=\"http://schemas.xmlsoap.org/ws/2003/03/business-process/\"");
        Iterator<BPELCommand> it = bpelData.values().iterator();
        BPELCommand command;
        while (it.hasNext()) {
            command = it.next();
            if (command.name == "import") {
                String identifier = ((Attribute) command.myUtility.get("namespace")).getValue().substring(7);
                if (command.getNamespace() == "ns2") {
                    GClient gClient = new GClient(((Attribute) command.getUtility("location")).getValue());
                    buf.append("xmlns:ns2=\"" + gClient.getTns() + "\"");
                } else {
                    buf.append((" xmlns:" + command.getNamespace() + "=\"http://activebpel.org/" + identifier + "Defs\" "));
                }
            }
        }
        buf.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
        buf.append(nLine);
        buf.append("<partnerLinks>");
        buf.append(nLine);
        it = bpelData.values().iterator();
        while (it.hasNext()) {
            command = it.next();
            if (command.name == "partnerLink") {
                buf.append((command.accept(new BPELVisitor())));
            }
        }
        buf.append(nLine);
        buf.append("</partnerLinks>");
        buf.append(nLine);
        buf.append("<variables>");
        buf.append(nLine);
        it = bpelData.values().iterator();
        while (it.hasNext()) {
            command = it.next();
            if (command.name == "variable") {
                buf.append((command.accept(new BPELVisitor())));
            }
        }
        buf.append(nLine);
        buf.append("</variables>");
        buf.append(nLine);
        buf.append(accept(new BPELVisitor()));
        buf.append("</process>");
        return buf.toString();
    }

    /**
	 * creates the content for the wsdlCatalog.xml file
	 * 
	 * @param name
	 *            the name of the service
	 * @return the String with the content of the wsdlCatalog.xml
	 */
    public final String toCatalogFile(final String name) {
        buf.delete(0, buf.length());
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><wsdlCatalog>");
        buf.append(" <wsdlEntry location=\"wsdl/" + name + "Defs.wsdl\" classpath=\"wsdl/" + name + "Defs.wsdl\" />");
        buf.append("</wsdlCatalog>");
        return buf.toString();
    }

    /**
	 * creates the content of the "projectname".pdd file
	 * 
	 * @param name
	 * @return
	 */
    public final String toPDDFile(final String name) {
        String bigname = name.substring(0, 1).toUpperCase() + name.substring(1);
        buf.delete(0, buf.length());
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><process location=\"bpel/");
        buf.append(name);
        buf.append(".bpel\" name=\"bpelns:" + name + "\"");
        buf.append(" xmlns=\"http://schemas.active-endpoints.com/pdd/2004/09/pdd.xsd\"");
        buf.append(" xmlns:bpelns=\"http://" + name + "\"");
        buf.append(" xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2003/03/addressing\">");
        buf.append(nLine);
        buf.append("<partnerLinks>");
        buf.append(nLine);
        Iterator<BPELCommand> commands = bpelData.values().iterator();
        while (commands.hasNext()) {
            BPELCommand command = commands.next();
            if (command.getName() == "partnerLink") {
                buf.append("<partnerLink name=\"" + ((Attribute) command.getUtility("name")).getValue() + "\">");
                buf.append("<myRole allowedRoles=\"\" binding=\"RPC\" service=\"" + ((Attribute) command.getUtility("partnerRole")).getValue() + "Service\"/>");
                buf.append("</partnerLink>");
                buf.append(nLine);
            }
        }
        buf.append("</partnerLinks>");
        buf.append(nLine);
        buf.append("<wsdlReferences>");
        commands = bpelData.values().iterator();
        int wsdlNumbers = 1;
        while (commands.hasNext()) {
            BPELCommand command = commands.next();
            if (command.getName() == "import") {
                if (command.getNamespace() != "ns1") {
                    GClient gClient = new GClient(((Attribute) command.getUtility("location")).getValue());
                    buf.append(nLine);
                    buf.append("<wsdl location=\"");
                    buf.append(wsdlNumbers + ".wsdl");
                    buf.append("\" namespace=\"");
                    buf.append(gClient.getTns() + "\"");
                    buf.append("/>");
                    wsdlNumbers++;
                } else {
                    buf.append(nLine);
                    buf.append("<wsdl location=\"" + ((Attribute) command.getUtility("location")).getValue());
                    buf.append("\" namespace=\"");
                    buf.append("http://activebpel.org/");
                    buf.append(((Attribute) command.getUtility("namespace")).getValue().substring(7));
                    buf.append("Defs\"/>");
                }
            }
        }
        buf.append(nLine);
        buf.append("</wsdlReferences></process>");
        return buf.toString();
    }

    /**
	 * creates the content of the "projectname".wsdl file
	 * 
	 * @param name
	 *            name of the service
	 * @return String with the content of the WSDL-File
	 */
    public final String toWSDLFile(final String name) {
        String bigname = name.substring(0, 1).toUpperCase() + name.substring(1);
        buf.delete(0, buf.length());
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buf.append(nLine);
        buf.append("<wsdl:definitions targetNamespace=\"http://activebpel.org/echoDefs\"");
        buf.append(" xmlns:tns=\"http://activebpel.org/" + name + "Defs\"");
        buf.append(nLine);
        buf.append(" xmlns:wsdlsoap=\"http://schemas.xmlsoap.org/wsdl/soap/\"");
        buf.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
        buf.append(nLine);
        buf.append(" xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\"");
        buf.append(" xmlns:plnk=\"http://schemas.xmlsoap.org/ws/2003/05/partner-link/\">");
        buf.append(nLine);
        buf.append("<wsdl:message name=\"" + name + "Request\">");
        buf.append("<wsdl:part name=\"" + name + "Part1\" type=\"xsd:string\"/>");
        buf.append("<wsdl:part name=\"" + name + "Part2\" type=\"xsd:string\"/>");
        buf.append("</wsdl:message>");
        buf.append(nLine);
        buf.append("<wsdl:message name=\"" + name + "Response\">");
        buf.append("<wsdl:part name=\"" + name + "Part1and2\" type=\"xsd:string\"/>");
        buf.append("</wsdl:message>");
        buf.append(nLine);
        buf.append("<plnk:partnerLinkType name=\"" + name + "PLT\">");
        buf.append("<plnk:role name=\"" + name + "\">");
        buf.append("<plnk:portType name=\"tns:" + bigname + "Service\"/>");
        buf.append("</plnk:role></plnk:partnerLinkType>");
        buf.append(nLine);
        buf.append("<wsdl:portType name=\"" + bigname + "Service\">");
        buf.append(nLine);
        buf.append(" <wsdl:operation name=\"" + name + "\">");
        buf.append(nLine);
        buf.append("<wsdl:input name=\"" + name + "Request\" message=\"tns:" + name + "Request\"/>");
        buf.append(nLine);
        buf.append("<wsdl:output name=\"" + name + "Response\" message=\"tns:" + name + "Response\"/>");
        buf.append(nLine);
        buf.append(" </wsdl:operation></wsdl:portType>");
        buf.append(nLine);
        buf.append("<wsdl:binding name=\"" + bigname + "ServiceSoapBinding\" type=\"tns:" + bigname + "Service\">");
        buf.append(nLine);
        buf.append("<wsdlsoap:binding style=\"rpc\"");
        buf.append(" transport=\"http://schemas.xmlsoap.org/soap/http\"");
        buf.append(" xmlns:wsdlsoap=\"http://schemas.xmlsoap.org/wsdl/soap/\"/>");
        buf.append(nLine);
        buf.append("<wsdl:operation name=\"" + name + "\">");
        buf.append(nLine);
        buf.append("<wsdlsoap:operation soapAction=\"\" xmlns:wsdlsoap=\"http://schemas.xmlsoap.org/wsdl/soap/\"/>");
        buf.append(nLine);
        buf.append("<wsdl:input name=\"" + name + "Request\">");
        buf.append(nLine);
        buf.append("<wsdlsoap:body encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"");
        buf.append(nLine);
        buf.append(" namespace=\"http://activebpel.org/" + name + "Defs\"");
        buf.append(" use=\"encoded\"");
        buf.append(" xmlns:wsdlsoap=\"http://schemas.xmlsoap.org/wsdl/soap/\"/>");
        buf.append(nLine);
        buf.append("</wsdl:input>");
        buf.append(nLine);
        buf.append("<wsdl:output name=\"" + name + "Response\">");
        buf.append(nLine);
        buf.append("<wsdlsoap:body encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"");
        buf.append(" namespace=\"http://activebpel.org/" + name + "Defs\"");
        buf.append(" use=\"encoded\"");
        buf.append(" xmlns:wsdlsoap=\"http://schemas.xmlsoap.org/wsdl/soap/\"/>");
        buf.append(nLine);
        buf.append("</wsdl:output></wsdl:operation></wsdl:binding>");
        buf.append(nLine);
        buf.append("<plnk:partnerLinkType name=\"BLZPLT\"><plnk:role name=\"getBank\"><plnk:portType name=\"ns2:BLZServicePortType\"/></plnk:role></plnk:partnerLinkType>");
        buf.append("<wsdl:service name=\"" + bigname + "ServiceService\">");
        buf.append(nLine);
        buf.append("<wsdl:port name=\"" + bigname + "Service\" binding=\"tns:" + bigname + "ServiceSoapBinding\">");
        buf.append(nLine);
        buf.append("<wsdlsoap:address location=\"");
        buf.append("http://localhost:8080/active-bpel/" + bigname + "Service");
        buf.append("\" xmlns:wsdlsoap=\"http://schemas.xmlsoap.org/wsdl/soap/\"/>");
        buf.append(nLine);
        buf.append("</wsdl:port></wsdl:service></wsdl:definitions>");
        return buf.toString();
    }
}
