package backend;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 *
 * @author Adnaan2
 */
public class XMLParser_GraphMLtoJUNG {

    /** Creates a new instance of XMLParser */
    public File xmlfile;

    public XMLParser_GraphMLtoJUNG(File file) {
        this.xmlfile = file;
        parsenodes();
        parseedges();
    }

    public HashMap<String, HashMap<String, String>> userdata = new HashMap<String, HashMap<String, String>>();

    public HashMap<String, HashMap<String, String>> edgedata = new HashMap<String, HashMap<String, String>>();

    public LinkedList<String> keynode = new LinkedList<String>();

    public LinkedList<String> keyedge = new LinkedList<String>();

    public void parsenodes() {
        keynode.add("labelid");
        keynode.addLast("extdescid");
        keynode.addLast("sizeid");
        keynode.addLast("rcolorid");
        keynode.addLast("gcolorid");
        keynode.addLast("bcolorid");
        keynode.addLast("listintid");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlfile);
            NodeList nodes = doc.getElementsByTagName("node");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node curnode = nodes.item(i);
                NodeList children = curnode.getChildNodes();
                int childcount = 0;
                HashMap<String, String> temp = new HashMap<String, String>();
                for (int j = 0; j < children.getLength(); j++) {
                    Node curchild = children.item(j);
                    String val = curchild.getTextContent();
                    if (!val.trim().equals("")) {
                        temp.put(this.keynode.get(childcount), val);
                        childcount++;
                        System.out.println(val);
                    }
                }
                this.userdata.put(String.valueOf(i), temp);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void parseedges() {
        this.keyedge.add("sizeid");
        keyedge.addLast("rcolorid");
        keyedge.addLast("gcolorid");
        keyedge.addLast("bcolorid");
        keyedge.addLast("directionid");
        keyedge.addLast("extdescid");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlfile);
            NodeList nodes = doc.getElementsByTagName("edge");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node curnode = nodes.item(i);
                NodeList children = curnode.getChildNodes();
                int childcount = 0;
                HashMap<String, String> temp = new HashMap<String, String>();
                for (int j = 0; j < children.getLength(); j++) {
                    Node curchild = children.item(j);
                    String val = curchild.getTextContent();
                    if (!val.trim().equals("")) {
                        temp.put(this.keyedge.get(childcount), val);
                        childcount++;
                        System.out.println(val);
                    }
                }
                this.edgedata.put(String.valueOf(i), temp);
            }
            System.out.println(this.userdata.size());
            System.out.println(this.edgedata.size());
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
