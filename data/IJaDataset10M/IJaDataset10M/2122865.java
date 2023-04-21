package org.tigr.microarray.mev.cluster.clusterUtil.submit;

import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.xerces.parsers.DOMParser;
import org.tigr.microarray.mev.TMEV;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author  braisted
 */
public class RepositoryConfigParser extends DefaultHandler {

    /** XML Document
     */
    Document document;

    /** Root Element */
    private Element root;

    /** Hashtable with repository name keys and repository Element values */
    private Hashtable repositoryHash;

    /** Creates a new instance of RepositoryConfigParser */
    public RepositoryConfigParser() {
    }

    /** Parses config file
     */
    public boolean parseSubmissionConfigFile() {
        try {
            File file = TMEV.getConfigurationFile("archive_submission_config.xml");
            if (file == null || !file.exists()) {
                JOptionPane.showMessageDialog(new JFrame(), "Error during submission configuration.  The file archive_submission_config.xml which contains\n" + "cluter repository information was missing or not in MeV's config folder.\n", "Cluster Submission Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            URL url = file.toURL();
            DOMParser parser = new DOMParser();
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setErrorHandler(this);
            parser.parse(url.toString());
            document = parser.getDocument();
            root = document.getDocumentElement();
        } catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "Error parsing archive_submission_config.xml which contains repository information.", "Cluster Submission Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "Error parsing archive_submission_config.xml which contains repository information.", "Cluster Submission Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        initializeRepositoryHash();
        return true;
    }

    /** Returns the root element
     */
    public Element getRoot() {
        return root;
    }

    /** Returns available repository names
     */
    public String[] getRepositoryNames() {
        if (repositoryHash == null) return null;
        Set keys = repositoryHash.keySet();
        String[] names = new String[keys.size()];
        Iterator iter = keys.iterator();
        for (int i = 0; i < names.length; i++) names[i] = (String) (iter.next());
        return names;
    }

    /** returns a repository description given a repository name.
     * @param repName name of the repository
     */
    public String getRepositoryDescription(String repName) {
        if (repositoryHash == null) return null;
        Element elem = getRepositoryXMLElement(repName);
        NodeList list = elem.getElementsByTagName("description");
        return list.item(0).getChildNodes().item(0).getNodeValue();
    }

    /** returns a repository's DOM Element given a repository name
     * @param repName name of the repository
     */
    public Element getRepositoryXMLElement(String repName) {
        if (repositoryHash == null) return null;
        return (Element) (repositoryHash.get(repName));
    }

    /** Returns all available repository names
     */
    private void initializeRepositoryHash() {
        this.repositoryHash = new Hashtable();
        NodeList nameList = root.getElementsByTagName("submit_entry");
        String[] names = new String[nameList.getLength()];
        for (int i = 0; i < names.length; i++) {
            repositoryHash.put(((Element) (nameList.item(i))).getAttribute("name"), (Element) (nameList.item(i)));
        }
    }

    /**
     * Returns a table of repository specifications
     */
    public String getRepositorySpecifications(String repName) {
        String table = new String();
        String key, value;
        table += "<table cellpadding=5><tr><th colspan=2 align=left>Additional Submission Details</th></tr>";
        Element elem = getRepositoryXMLElement(repName);
        elem = (Element) (elem.getElementsByTagName("submission").item(0));
        elem = (Element) (elem.getElementsByTagName("specification_table").item(0));
        NodeList list = elem.getElementsByTagName("spec");
        for (int i = 0; i < list.getLength(); i++) {
            key = ((Element) (list.item(i))).getAttribute("key");
            value = ((Element) (list.item(i))).getAttribute("value");
            table += "<tr><td><b>" + key + ":</b></td><td>" + value + "</td></tr>";
        }
        table += "</table>";
        return table;
    }

    /**
     * Constructs a repository description page
     */
    public String getRepositoryPage(String repName) {
        String text = new String();
        text += "<html><body><h2>" + repName + "</h2><hr size=3>" + "<b>Repository Name:&nbsp&nbsp</b>" + repName + "<br><br>" + "<b>Repository Web Site:&nbsp&nbsp</b>" + getRepositoryURL(repName) + "<br><br>" + "<b>Description:&nbsp&nbsp</b>" + getRepositoryDescription(repName) + "<br><br>";
        text += getRepositorySpecifications(repName);
        text += "</body></html>";
        return text;
    }

    /** Returns user information in a Hashtable with possible, user_name, password, and email
     */
    public Hashtable getUserInfo(String repName) {
        Hashtable table = new Hashtable();
        Element elem = getRepositoryXMLElement(repName);
        NodeList list = elem.getElementsByTagName("user");
        elem = (Element) (list.item(0));
        String value = elem.getAttribute("user_name");
        table.put("user_name", value);
        value = elem.getAttribute("password");
        table.put("password", value);
        value = elem.getAttribute("email");
        table.put("email", value);
        return table;
    }

    /** Returns the implementation class for the repository
     */
    public String getRepositorySubmissionClass(String repName) {
        Element elem = getRepositoryXMLElement(repName);
        NodeList list = elem.getElementsByTagName("impl");
        return ((Element) (list.item(0))).getAttribute("impl_class");
    }

    /** Returns the repository's URL if available, else null
     * @param repName repository name
     */
    public String getRepositoryURL(String repName) {
        Element elem = getRepositoryXMLElement(repName);
        if (elem != null) return elem.getAttribute("url");
        return null;
    }

    /** Reports Parser Exceptions (Warning level exp.)
     * @param e reported exception
     * @throws SAXException
     */
    public void warning(SAXParseException e) throws SAXException {
        System.err.println("Warning:  " + e);
    }

    /** Parse error reporting.
     */
    public void error(SAXParseException e) throws SAXException {
        System.err.println("Error:  " + e);
    }

    /** Parse Fatal errors
     */
    public void fatalError(SAXParseException e) throws SAXException {
        System.err.println("Fatal Error:  " + e);
    }
}
