package rddl.evaluate;

import java.io.*;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.MapList;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.ChildrenIterator;

public class LogReader {

    public static final boolean ALLOW_MULTIPLE_CLIENT_NAMES = true;

    public XPath _xpath = XPathFactory.newInstance().newXPath();

    public Document _doc = null;

    public HashMap<String, MapList> _client2data = null;

    public LogReader(File f) {
        File f2 = new File(f.toString() + ".clean");
        if (!f2.exists()) {
            CleanFile(f, f2);
        }
        _client2data = new HashMap<String, MapList>();
        try {
            DOMParser parser = new DOMParser();
            InputStream byteStream = new FileInputStream(f2);
            parser.parse(new org.xml.sax.InputSource(byteStream));
            _doc = parser.getDocument();
        } catch (Exception e) {
            System.out.println(e);
        }
        NodeList nodes = _doc.getChildNodes().item(0).getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (!n.getNodeName().equals("round-end")) continue;
            String client_name = null;
            String instance_name = null;
            double reward = Double.NaN;
            NodeList children = n.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node c = children.item(j);
                if (c.getNodeName().equals("client-name")) {
                    client_name = c.getFirstChild().getNodeValue();
                }
                if (c.getNodeName().equals("instance-name")) {
                    instance_name = c.getFirstChild().getNodeValue();
                }
                if (c.getNodeName().equals("round-reward")) {
                    reward = new Double(c.getFirstChild().getNodeValue());
                }
            }
            if (client_name == null) {
                System.err.println("Client name null... skipping");
                PrintNode(n, "", 0);
                continue;
            }
            MapList ml = _client2data.get(client_name);
            if (ml == null) {
                ml = new MapList();
                _client2data.put(client_name, ml);
            }
            ml.putValue(instance_name, reward);
        }
    }

    public static void CleanFile(File f, File f2) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            PrintStream ps = new PrintStream(new FileOutputStream(f2));
            String line = br.readLine();
            ps.println("<root>");
            while ((line = br.readLine()) != null) {
                if (line.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) continue;
                ps.println(line);
            }
            ps.println("</root>");
            br.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Cannot process file: '" + f + "'\n" + e);
            System.exit(1);
        }
    }

    /**
     * Creates a string buffer of spaces
     * @param depth the number of spaces
     * @return string of spaces
     */
    public static StringBuffer Pad(int depth) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < depth; i++) sb.append("  ");
        return sb;
    }

    /**
     * Print the DOM tree on stdout
     * @param n root node of a document
     * @param prefix
     * @param depth
     */
    public static void PrintNode(Node n, String prefix, int depth) {
        try {
            System.out.print("\n" + Pad(depth) + "[" + n.getNodeName());
            NamedNodeMap m = n.getAttributes();
            for (int i = 0; m != null && i < m.getLength(); i++) {
                Node item = m.item(i);
                System.out.print(" " + item.getNodeName() + "=" + item.getNodeValue());
            }
            System.out.print("] ");
            NodeList cn = n.getChildNodes();
            for (int i = 0; cn != null && i < cn.getLength(); i++) {
                Node item = cn.item(i);
                if (item.getNodeType() == Node.TEXT_NODE) {
                    String val = item.getNodeValue().trim();
                    if (val.length() > 0) System.out.print(" \"" + item.getNodeValue().trim() + "\"");
                } else PrintNode(item, prefix, depth + 2);
            }
        } catch (Exception e) {
            System.out.println(Pad(depth) + "Exception e: ");
        }
    }

    public Object XPathQuery(Node doc, String query, QName query_type) {
        try {
            XPathExpression xPathExpression = _xpath.compile(query);
            return xPathExpression.evaluate(doc, query_type);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        LogReader d = new LogReader(new File("TestComp/MDP/rddl-2320.log"));
        System.out.println(d._client2data);
    }
}
