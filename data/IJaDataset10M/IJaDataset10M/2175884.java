package org.webdocwf.util.smime.util;

import org.webdocwf.util.smime.exception.SMIMEException;
import java.util.Vector;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

/**
 * HtmlAnalyzer class is used for parsing html code which has to become content
 * of the message. For parsing is used JTidy parser. As result of parsing, DOM
 * (Document Object Model) structure is obtained. It is tree-like construction
 * with nodes and hierarchical structures that descript input html code. This
 * structure is easy for browsing and searching for specific html elements and
 * attributes. By using DOM, all references to resources (image, movie, sound... ),
 * defined in "src" and "background" attributes, are explored and swapped with
 * generated unique Content-ID values which are necessary in forming
 * "multipart/related" MimeMultipart object.<BR>
 * <BR>
 * DOM, generated inside of the object of this class, is also used in the process of
 * generation plain/text message based on, and derived from the given html code.
 * This plain text is later used in creation of "multipart/alternative"
 * MimeMultipart object.
 */
public class HtmlAnalyzer {

    /**
     * plain/text representation of page
     */
    private String plainText = "";

    /**
     * Enable/disable p tag in text/html to text/plain conversion.
     */
    private boolean pTagEnable = true;

    /**
     * Path to html file or prefix path to the embeded resource's adresses in
     * html code (for example for "src" attribute of IMG tag). Can be null which
     * means that prefix won't be added to resources location in the process of
     * searching for specific adress attributes given in html code.
     */
    private String absolutPath = null;

    /**
     * Container for parsed html document in DOM (Document Object Model)
     * representation.
     */
    private Document doc;

    /**
     * Indent from left margin pointer. This information is used in the process of
     * generation plain text message based on html code.
     */
    private int indent = 0;

    /**
     * Current sequential number of OL (ordered list) html element. This information
     * is used in the process of generation plain text message based on html code.
     */
    private int olNumber = 1;

    /**
     * Current html element is OL (ordered list), UN (unordered list) or something
     * else. This information is used in the process of generation plain text message based
     * on html code.
     */
    private String ul_ol = "";

    /**
     * Constant used in generating indent from left side. This information is used in
     * the process of generation plain text message based on html code.
     */
    private final String indentString = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";

    /**
     * Container for storing pairs of replaced url or file addresses and
     * corresponding generated Content-ID values.
     */
    private Vector sourceLinks = new Vector(0, 1);

    /**
     * Enable/disable swapping resource references in html code with generated
     * value for Content-ID message bodypart header line. Default value is true
     * (enable swapping)
     */
    private boolean enableSwapping = true;

    /**
     * Constructs HtmlAnalyzer from data given from InputStream. This constructor
     * parses html code from input stream withouth swaping resources' locations from
     * atribute's "src" and "background" value with generated Content-ID values. Also,
     * it is performed generation of plain text message based on html code.
     * @param content0 html code given as InputStream
     * @SMIMEException caused by its private method analyze().
     */
    public HtmlAnalyzer(InputStream content0) throws SMIMEException {
        Tidy tidy = new Tidy();
        tidy.setWraplen(1000);
        tidy.setShowWarnings(false);
        tidy.setUpperCaseTags(true);
        doc = (tidy.parseDOM(content0, null));
        enableSwapping = false;
        analyze(doc);
        plainText = plainText + "\r\n";
    }

    /**
     * Constructs HtmlAnalyzer from data given from InputStream. This constructor
     * parses html code from input stream with swaping resources' locations from
     * atribute's "src and "background" value with generated Content-ID values. In
     * that process, it is used given second paremeter "path0" which represents
     * common path to all resources in html code with relative path adresses. Also,
     * it is performed generation of plain text message based on html code.
     * @param content0 html code given as InputStream.
     * @param path0 common path used for resolving all resources in html code with
     * relative path adresses.
     * @SMIMEException caused by its private method analyze().
     */
    public HtmlAnalyzer(InputStream content0, String path0) throws SMIMEException {
        if (path0 != null) {
            absolutPath = new String(path0);
            if (absolutPath.charAt(absolutPath.length() - 1) == '\\' || absolutPath.charAt(absolutPath.length() - 1) == '/') absolutPath = absolutPath.substring(0, absolutPath.length() - 1);
            absolutPath = absolutPath.replace('/', File.separatorChar);
            absolutPath = absolutPath.replace('\\', File.separatorChar) + File.separator;
        }
        Tidy tidy = new Tidy();
        tidy.setWraplen(1000);
        tidy.setShowWarnings(false);
        tidy.setUpperCaseTags(true);
        doc = (tidy.parseDOM(content0, null));
        analyze(doc);
        plainText = plainText + "\r\n";
    }

    /**
     * Returns pairs of swapped resource URL adresses or File paths and appropriate
     * generated Content IDs.
     * @return Vector object whose even (and 0) indexes contain resource addresses
     * as File or String objects, and whose odd indexes contain appropriate
     * swapped Content-ID values.
     */
    public Vector getSwappedAdresses() {
        return sourceLinks;
    }

    /**
     * Returns plain/text representation of given html code document
     * @return html document transformed to plain/text.
     */
    public String getPlainText() {
        return plainText;
    }

    /**
     * Returns html/text document passed throught JTidy html parser. All resource
     * references which were accessible on the file system are swapped with
     * generated content ID value. Also, all virtual references to appropriate
     * InputStream resources (see setContent methods in classes from package
     * org.webdocwf.util.smime.smime) are also swapped with generated Content-ID
     * value.
     * @return parsed html/text document.
     * @exception SMIMEException caused by non SMIMEException which is:
     * UnsupportedEncodingException.
     */
    public String getHtmlText() throws SMIMEException {
        String returnString;
        Tidy tidy = new Tidy();
        tidy.setWraplen(1000);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        tidy.pprint(doc, out);
        try {
            returnString = out.toString("ISO-8859-1");
            out.close();
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "getHtmlText");
        }
        return returnString;
    }

    /**
     * Analyzes html code and creates alternate plain/text message from html code.
     * Also, it creates Vector with corresponding pairs of resource locations discovered
     * in html code (values of "background" and "src" attributes) and generated
     * Content-ID values.
     * @param node0 node element got from JTidy parser.
     * @exception SMIMEException caused by MimeAssist.generateID() method or by
     * its private method existenceOfResource().
     */
    private void analyze(Node node0) throws SMIMEException {
        if (node0 == null) {
            return;
        }
        String brLine = "\r\n";
        int type = node0.getNodeType();
        boolean pTagEnable_old = true;
        int indent_old = 0;
        int olNumber_old = 1;
        String ul_ol_old = "";
        switch(type) {
            case Node.DOCUMENT_NODE:
                analyze(((Document) node0).getDocumentElement());
                break;
            case Node.ELEMENT_NODE:
                String elName = node0.getNodeName();
                if (elName.equalsIgnoreCase("br")) {
                    plainText = plainText + brLine;
                    if (indent > 0) plainText = plainText + indentString.substring(0, indent - 1);
                } else if (elName.equalsIgnoreCase("hr")) {
                    plainText = plainText + brLine + "==================================================" + brLine;
                } else if (elName.equalsIgnoreCase("p")) {
                    if (pTagEnable) {
                        plainText = plainText + brLine + brLine;
                        if (indent > 0) plainText = plainText + indentString.substring(0, indent - 1);
                    }
                    pTagEnable = true;
                } else if (elName.equalsIgnoreCase("ul")) {
                    pTagEnable_old = pTagEnable;
                    pTagEnable = false;
                    ul_ol_old = ul_ol;
                    ul_ol = elName;
                    indent_old = indent;
                    indent++;
                } else if (elName.equalsIgnoreCase("ol")) {
                    pTagEnable_old = pTagEnable;
                    pTagEnable = false;
                    ul_ol_old = ul_ol;
                    ul_ol = elName;
                    indent_old = indent;
                    indent++;
                    olNumber_old = olNumber;
                } else if (elName.equalsIgnoreCase("li")) {
                    pTagEnable = false;
                    if (ul_ol.equalsIgnoreCase("ul")) {
                        plainText = plainText + brLine + indentString.substring(0, indent - 1) + ">> ";
                    } else if (ul_ol.equalsIgnoreCase("ol")) {
                        plainText = plainText + brLine + indentString.substring(0, indent - 1) + olNumber + ". ";
                        olNumber++;
                    }
                } else if (elName.equalsIgnoreCase("blockquote")) {
                    pTagEnable_old = pTagEnable;
                    pTagEnable = false;
                    indent_old = indent;
                    indent++;
                    plainText = plainText + brLine + indentString.substring(0, indent);
                } else if (elName.equalsIgnoreCase("q")) {
                    pTagEnable_old = pTagEnable;
                    pTagEnable = false;
                    plainText = plainText + "\"";
                } else if (elName.equalsIgnoreCase("table")) {
                    plainText = plainText + brLine + "**************************************************" + brLine + "--------------------------------------------------" + brLine + "--  --  --  --  --  --  --  --  --  --  --  --  --" + brLine;
                } else if (elName.equalsIgnoreCase("tr")) {
                    plainText = plainText + brLine;
                } else if (elName.equalsIgnoreCase("td")) {
                    plainText = plainText + brLine;
                }
                NamedNodeMap attrs = node0.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    attrs.item(i).getNodeName().toUpperCase();
                    if (enableSwapping && ((attrs.item(i).getNodeName()).equalsIgnoreCase("src") || (attrs.item(i).getNodeName()).equalsIgnoreCase("background"))) {
                        String resource = attrs.item(i).getNodeValue();
                        String cid = null;
                        if (resource.substring(0, 5).equalsIgnoreCase("*****")) {
                            for (int j = 0; j < sourceLinks.size() & cid == null; j = j + 2) {
                                if (sourceLinks.elementAt(j) instanceof String && ((String) sourceLinks.elementAt(j)).equals(resource)) cid = (String) sourceLinks.elementAt(j + 1);
                            }
                            if (cid == null) {
                                cid = MimeAssist.generateID();
                                sourceLinks.add(resource);
                                sourceLinks.add(cid);
                            }
                            attrs.item(i).setNodeValue("cid:" + cid);
                        } else {
                            File fRes = existenceOfResource(resource);
                            if (fRes != null) {
                                for (int j = 0; j < sourceLinks.size() & cid == null; j = j + 2) {
                                    if (sourceLinks.elementAt(j) instanceof File && ((File) sourceLinks.elementAt(j)).compareTo(fRes) == 0) cid = (String) sourceLinks.elementAt(j + 1);
                                }
                                if (cid == null) {
                                    cid = MimeAssist.generateID();
                                    sourceLinks.add(fRes);
                                    sourceLinks.add(cid);
                                }
                                attrs.item(i).setNodeValue("cid:" + cid);
                            }
                        }
                    }
                }
                NodeList children = node0.getChildNodes();
                if (children != null) {
                    int len = children.getLength();
                    for (int i = 0; i < len; i++) {
                        analyze(children.item(i));
                    }
                }
                if (elName.equalsIgnoreCase("ul")) {
                    pTagEnable = pTagEnable_old;
                    ul_ol = ul_ol_old;
                    indent = indent_old;
                } else if (elName.equalsIgnoreCase("ol")) {
                    pTagEnable = pTagEnable_old;
                    ul_ol = ul_ol_old;
                    indent = indent_old;
                    olNumber = olNumber_old;
                } else if (elName.equalsIgnoreCase("table")) {
                    plainText = plainText + brLine + "**************************************************";
                } else if (elName.equalsIgnoreCase("tr")) {
                    plainText = plainText + brLine + "--------------------------------------------------";
                } else if (elName.equalsIgnoreCase("td")) {
                    plainText = plainText + brLine + "--  --  --  --  --  --  --  --  --  --  --  --  --";
                } else if (elName.equalsIgnoreCase("blockquote")) {
                    indent = indent_old;
                    pTagEnable = pTagEnable_old;
                } else if (elName.equalsIgnoreCase("q")) {
                    plainText = plainText + "\"";
                    pTagEnable = pTagEnable_old;
                }
                break;
            case Node.TEXT_NODE:
                String nodeVal = node0.getNodeValue();
                plainText = plainText + nodeVal;
                break;
        }
    }

    /**
     * Method checks if it is given a resource reachable in the destination file system.
     * @param resource0 can be absolute or relative path with specified file name
     * or adress of file in URL form (example "file:///c:/temp/example.gif" )
     * @return object of class File which represents existance of the resource file
     * or null if resource does not exist on the destination in file system.
     * @SMIMEException caused by non SMIMEException which is IOException.
     */
    private File existenceOfResource(String resource0) throws SMIMEException {
        boolean resourceIsUrl = true;
        String resource = new String(resource0);
        URL url = null;
        try {
            url = new URL(resource0);
        } catch (MalformedURLException e) {
            resourceIsUrl = false;
        }
        if (resourceIsUrl == true && (!url.getProtocol().equalsIgnoreCase("file"))) return null; else if (resourceIsUrl == true && url.getProtocol().equalsIgnoreCase("file")) {
            resource = url.getFile();
        }
        resource = replaceHex(resource);
        resource = resource.replace('/', File.separatorChar);
        resource = resource.replace('\\', File.separatorChar);
        File fRes = new File(resource);
        try {
            if (fRes.exists()) return fRes.getAbsoluteFile().getCanonicalFile();
            fRes = new File(absolutPath + resource);
            if (fRes.exists()) return fRes.getAbsoluteFile().getCanonicalFile();
            fRes = new File(absolutPath + resource);
            if (fRes.exists()) return fRes.getAbsoluteFile().getCanonicalFile();
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "existenceOfResource");
        }
        return null;
    }

    /**
     * Replaces possible hexadecimal representation of blank characters (presented
     * with "%20") from resource String representation, with blank character.
     * @param resources0 resource which is examined for hex representation of blank
     * characters.
     * @return String with replaced hexadecimal representation of blank characters.
     */
    private String replaceHex(String resources0) {
        while (resources0.indexOf("%20") != -1) {
            resources0 = resources0.substring(0, resources0.indexOf("%20")) + " " + resources0.substring(resources0.indexOf("%20") + 3);
        }
        return resources0;
    }
}
