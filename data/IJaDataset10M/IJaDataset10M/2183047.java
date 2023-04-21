package de.schlund.pfixcore.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import de.schlund.pfixxml.config.GlobalConfigurator;
import de.schlund.pfixxml.resources.FileResource;
import de.schlund.pfixxml.resources.ResourceUtil;
import de.schlund.pfixxml.util.Xml;

/**
 * DelLang.java
 *
 * @author <a href="mailto:jtl@schlund.de">Jens Lautenbacher</a>
 * @version 1.0
 */
public class DelLang {

    private static final DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();

    private Pattern pattern = Pattern.compile("^\\s*$");

    int onelangcount = 0;

    int multilangcount = 0;

    static {
        dbfac.setNamespaceAware(true);
    }

    public DelLang(String docroot) {
    }

    public static void main(String[] args) throws Exception {
        String docroot = args[0];
        String files = args[1];
        if (files == null || docroot == null) {
            System.err.println("Usage: java DelLang DOCROOT includefilelist");
            System.exit(0);
        }
        GlobalConfigurator.setDocroot(docroot);
        DelLang dellang = new DelLang(docroot);
        Logging.configure("generator_quiet.xml");
        dellang.transform(files);
    }

    public void transform(String files) throws Exception {
        BufferedReader input = new BufferedReader(new FileReader(files));
        Set<FileResource> inames = new TreeSet<FileResource>();
        String line;
        while ((line = input.readLine()) != null) {
            inames.add(ResourceUtil.getFileResourceFromDocroot(line.substring(2)));
        }
        input.close();
        Document doc;
        for (Iterator<FileResource> i = inames.iterator(); i.hasNext(); ) {
            FileResource path = i.next();
            System.out.print(path + ":");
            doc = Xml.parseMutable(path);
            handleDoc(doc);
            File out = File.createTempFile("temp", ".TEMPFILE");
            out.getParentFile().mkdirs();
            Xml.serialize(doc, out, false, true);
            ResourceUtil.copy(ResourceUtil.getFileResource(out.toURI()), path);
            out.delete();
            System.out.println("");
        }
        System.out.println("Multilang: " + multilangcount + " Only default lang: " + onelangcount);
    }

    public void handleDoc(Document doc) {
        Element root = doc.getDocumentElement();
        NodeList rootchildren = root.getChildNodes();
        int localmultilangcount = 0;
        for (int i = 0; i < rootchildren.getLength(); i++) {
            Node rootchild = rootchildren.item(i);
            if (rootchild.getNodeType() == Node.ELEMENT_NODE && rootchild.getNodeName().equals("part")) {
                String partname = ((Element) rootchild).getAttribute("name");
                NodeList partchildren = rootchild.getChildNodes();
                for (int j = 0; j < partchildren.getLength(); j++) {
                    Node partchild = partchildren.item(j);
                    if (partchild.getNodeType() == Node.ELEMENT_NODE && partchild.getNodeName().equals("product")) {
                        String productname = ((Element) partchild).getAttribute("name");
                        NodeList prodchildren = partchild.getChildNodes();
                        int count = prodchildren.getLength();
                        boolean multilang = false;
                        for (int k = 0; k < count; k++) {
                            Node tmp = prodchildren.item(k);
                            if (tmp.getNodeType() == Node.ELEMENT_NODE) {
                                if (tmp.getNodeName().equals("lang")) {
                                    Element tmpelem = (Element) tmp;
                                    if (!tmpelem.getAttribute("name").equals("default")) {
                                        multilang = true;
                                    }
                                } else {
                                    System.out.println("*** Wrong element " + tmp.getNodeName() + " under part/product " + partname + "/" + productname);
                                    System.exit(1);
                                }
                            }
                        }
                        if (multilang) {
                            multilangcount++;
                            localmultilangcount++;
                        } else {
                            onelangcount++;
                        }
                        Element theme = doc.createElement("theme");
                        theme.setAttribute("name", ((Element) partchild).getAttribute("name"));
                        Element pfxlang = null;
                        if (multilang) {
                            pfxlang = doc.createElement("pfx:langselect");
                            theme.appendChild(pfxlang);
                        }
                        for (int k = 0; k < count; k++) {
                            Node prodchild = prodchildren.item(0);
                            partchild.removeChild(prodchild);
                            if (prodchild.getNodeType() == Node.ELEMENT_NODE && !multilang) {
                                NodeList langchildren = prodchild.getChildNodes();
                                int langcount = langchildren.getLength();
                                for (int l = 0; l < langcount; l++) {
                                    Node langchild = langchildren.item(0);
                                    prodchild.removeChild(langchild);
                                    theme.appendChild(langchild);
                                }
                            } else if (!multilang) {
                                if ((k == 0 || k == (count - 1)) && prodchild.getNodeType() == Node.TEXT_NODE) {
                                    Matcher matcher = pattern.matcher(prodchild.getNodeValue());
                                    if (!matcher.matches()) {
                                        System.out.println("==>" + partname + "/" + productname + ":" + prodchild.getNodeValue());
                                        theme.appendChild(prodchild);
                                    }
                                }
                            } else if (prodchild.getNodeType() == Node.ELEMENT_NODE) {
                                Element langinst = doc.createElement("pfx:lang");
                                langinst.setAttribute("name", ((Element) prodchild).getAttribute("name"));
                                NodeList langchildren = prodchild.getChildNodes();
                                int langcount = langchildren.getLength();
                                for (int l = 0; l < langcount; l++) {
                                    Node langchild = langchildren.item(0);
                                    prodchild.removeChild(langchild);
                                    langinst.appendChild(langchild);
                                }
                                pfxlang.appendChild(langinst);
                            } else {
                                pfxlang.appendChild(prodchild);
                            }
                        }
                        rootchild.replaceChild(theme, partchild);
                    }
                }
            }
        }
        System.out.print(" (" + localmultilangcount + ") ");
    }
}
