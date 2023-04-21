package net.sf.worldsaver.util.doc;

import javax.swing.*;
import javax.swing.tree.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 *<p>
 * This class grants easy access to an application's documentation. One has only to write
 * the documentation once in XML format (using the Schema
 * <a href="http://sammael.tabu.stw-bonn.de/schema/documentation.xsd">here</a>) and can
 * then use this class to display a help browser or a specific help text. This class is
 * also a command line utility to generate HTML out of the documentation.
 *</p>
 *<p>
 * You can also use this class to display several documentations in one help browser. For
 * ease of use, one can specify a class to be Documented (using the interface) and provide
 * several of this classes instead of the appropriate Documentations.
 *</p>
 *<p>
 * Depending on the amount of documentation one can use either one Documentation for the
 * application (and one class implements Documented) or several classes to be
 * Documented. If you use the methods that require several Documented classes as argument,
 * they are displayed as top-level documentation nodes.
 *</p>
 *@author Andreas Schmitz
 *@version March 18th 2002
 */
public class Documentation {

    private Document doc;

    private TransformerFactory tf = TransformerFactory.newInstance();

    private HashMap topicnodes = new HashMap();

    /**
     * Constructs one using the given stream.
     *@param in the stream.
     */
    public Documentation(InputStream in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setAttribute("http://apache.org/xml/features/validation/schema", Boolean.TRUE);
            DocumentBuilder db = dbf.newDocumentBuilder();
            setDocument(db.parse(new InputSource(in)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs one using the given file.
     *@param location the path to the file.
     */
    public Documentation(String location) {
        this(new File(location));
    }

    /**
     * Constructs one using the given URL.
     *@param location the URL.
     */
    public Documentation(URL location) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setAttribute("http://apache.org/xml/features/validation/schema", Boolean.TRUE);
            DocumentBuilder db = dbf.newDocumentBuilder();
            setDocument(db.parse(location.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs one using the given file.
     *@param location the file.
     */
    public Documentation(File location) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setAttribute("http://apache.org/xml/features/validation/schema", Boolean.TRUE);
            DocumentBuilder db = dbf.newDocumentBuilder();
            setDocument(db.parse(location));
        } catch (Exception e) {
            System.err.println("Darn.");
            e.printStackTrace();
        }
    }

    private void setDocument(Document doc) {
        this.doc = doc;
    }

    /**
     * Displays the requested documentation in swing. This generates and displays a JFrame
     * containing the documentation regarding the topic.
     *@param id the topic to be displayed.
     */
    public void displayDocumentationFor(String id) {
        new Browser(id, this).show();
    }

    /**
     * Displays all documentation in swing. This generates and displays a JFrame
     * containing a JTree displaying the topic overview and an empty JTextPane.
     */
    public void displayDocumentation() {
        new Browser(this).show();
    }

    /**
     * Displays all documentation in swing. Works like #displayDocumentation().
     *@param doc an array containing the Documented classes whose documentation should be
     * used.
     */
    public static void displayDocumentation(Documented[] doc) {
    }

    private void topicsRecurse(DefaultMutableTreeNode node, Node n) {
        NodeList nl = n.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getLocalName() == null) continue;
            if (nl.item(i).getLocalName().equals("topic")) {
                Topic newtopic = new Topic((nl.item(i).getAttributes().getNamedItem("id").getNodeValue()), nl.item(i).getAttributes().getNamedItem("name").getNodeValue());
                DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(newtopic);
                node.add(newnode);
                topicnodes.put(newtopic.getId(), newnode);
                topicsRecurse(newnode, nl.item(i));
            }
        }
    }

    /**
     * Returns the title of this documentation.
     *@param language the desired language.
     *@return the title.
     */
    public String getTitle(String language) {
        NodeList nl = doc.getElementsByTagName("language");
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getAttributes().getNamedItemNS("http://www.w3.org/XML/1998/namespace", "lang").getNodeValue().equals(language)) return nl.item(i).getAttributes().getNamedItem("title").getNodeValue();
        }
        return null;
    }

    /**
     * Returns a JTree with all the topics.
     *@param language the desired language.
     *@return the tree.
     */
    public synchronized JTree getTopics(String language) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("topnode");
        NodeList nl = doc.getElementsByTagName("language");
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getAttributes().getNamedItemNS("http://www.w3.org/XML/1998/namespace", "lang").getNodeValue().equals(language)) topicsRecurse(top, nl.item(i));
        }
        return new JTree(top);
    }

    public TreePath getTreePathForTopic(JTree tree, String topic) {
        return new TreePath(((DefaultMutableTreeNode) topicnodes.get(topic)).getPath());
    }

    /**
     * Returns the requested topic as HTML in the String.
     *@param id the topic.
     *@param language the language.
     *@return the String.
     */
    public String getTopic(String id, String language) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = tf.newTransformer(new StreamSource(ClassLoader.getSystemResourceAsStream("net/sf/worldsaver/util/doc/xsl/32topic.xsl")));
            t.setParameter("topic", id);
            t.setParameter("language", language);
            t.transform(new DOMSource(doc), new StreamResult(sw));
            sw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    private static void generatePages(File file) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        Documentation documentation = new Documentation(file);
        NodeList nl = documentation.doc.getElementsByTagName("language");
        Transformer t;
        for (int i = 0; i < nl.getLength(); i++) {
            t = documentation.tf.newTransformer(new StreamSource(ClassLoader.getSystemResourceAsStream("net/sf/worldsaver/util/doc/xsl/4topic.xsl")));
            Node n = nl.item(i);
            String lang = "documentation" + File.separator + n.getAttributes().getNamedItemNS("http://www.w3.org/XML/1998/namespace", "lang").getNodeValue();
            new File(lang).mkdirs();
            new File(lang + File.separator + "topics").mkdirs();
            NodeList list = documentation.doc.getElementsByTagNameNS("http://sammael.tabu.stw-bonn.de/schema/documentation", "topic");
            t.setParameter("language", n.getAttributes().getNamedItemNS("http://www.w3.org/XML/1998/namespace", "lang").getNodeValue());
            for (int j = 0; j < list.getLength(); j++) {
                String topic = list.item(j).getAttributes().getNamedItem("id").getNodeValue();
                t.setParameter("topic", topic);
                t.transform(new DOMSource(documentation.doc), new StreamResult(new FileOutputStream(lang + File.separator + "topics" + File.separator + topic + ".html")));
            }
            t = documentation.tf.newTransformer(new StreamSource(ClassLoader.getSystemResourceAsStream("net/sf/worldsaver/util/doc/xsl/4index.xsl")));
            t.setParameter("language", n.getAttributes().getNamedItemNS("http://www.w3.org/XML/1998/namespace", "lang").getNodeValue());
            t.transform(new DOMSource(documentation.doc), new StreamResult(new FileOutputStream(lang + File.separator + "topics.html")));
            PrintWriter out = new PrintWriter(new FileWriter(lang + File.separator + "index.html"));
            out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
            out.println("<html><head><title>");
            out.println("Documentation");
            out.println("</title></head>");
            out.println("<frameset cols=\"20%,80%\">");
            out.println("<frameset rows=\"70%,30%\">");
            out.println("<frame src=\"topics.html\" name=\"topics\">");
            out.println("<frame src=\"../languages.html\" name=\"lang\">");
            out.println("</frameset>");
            out.println("<frame src=\"topics.html\" name=\"main\">");
            out.println("</frameset></html>");
            out.close();
        }
        t = documentation.tf.newTransformer(new StreamSource(ClassLoader.getSystemResourceAsStream("net/sf/worldsaver/util/doc/xsl/4langindex.xsl")));
        t.transform(new DOMSource(documentation.doc), new StreamResult(new FileOutputStream("documentation" + File.separator + "languages.html")));
    }

    /**
     * Run this to generate HTML out of the documentation.
     */
    public static void main(String[] args) throws Exception {
        generatePages(new File(args[0]));
    }
}
