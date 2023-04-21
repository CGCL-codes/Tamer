package net.sf.force4maven.support;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ManifestGenerator {

    protected String targetDirectory;

    protected DocumentBuilder documentBuilder;

    protected Document document;

    public ManifestGenerator(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public void generate(String name) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        documentBuilder = dbf.newDocumentBuilder();
        document = documentBuilder.newDocument();
        Element pe = document.createElementNS("http://soap.sforce.com/2006/04/metadata", "Package");
        document.appendChild(pe);
        Element ne = document.createElement("fullName");
        ne.setTextContent(name);
        pe.appendChild(ne);
        generateTypes("CustomApplication", "applications", "app");
        generateCustomFields();
        generateTypes("CustomObject", "objects", "object");
        generateTypes("CustomTab", "tabs", "tab");
        generateDocuments();
        generateTypes("Profile", "profiles", "profile");
        generateTypes("Scontrol", "scontrols", "scf");
        Element ve = document.createElement("version");
        ve.setTextContent("11.0");
        pe.appendChild(ve);
        File target = new File(targetDirectory + File.separator + "package.xml");
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(document), new StreamResult(target));
    }

    protected File[] scanDirectory(String directory, final String extension) {
        File target = new File(targetDirectory + File.separator + directory);
        File[] files = target.listFiles(new FilenameFilter() {

            public boolean accept(File file, String name) {
                return name.endsWith(extension);
            }
        });
        return files;
    }

    protected List<String> convertToArtifactNames(File[] files) {
        if (files == null) {
            return Collections.EMPTY_LIST;
        }
        List<String> artifacts = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
            artifacts.add(Utils.stripExtension(files[i].getName()));
        }
        return artifacts;
    }

    protected void generateTypes(String type, String directory, String extension) {
        File[] files = scanDirectory(directory, extension);
        Element types = createTypesNode(type, convertToArtifactNames(files));
        document.getDocumentElement().appendChild(types);
    }

    protected Element createTypesNode(String type, List<String> artifacts) {
        Element te = document.createElement("types");
        Iterator<String> itr = artifacts.iterator();
        while (itr.hasNext()) {
            Element me = document.createElement("members");
            me.setTextContent(itr.next());
            te.appendChild(me);
        }
        Element ne = document.createElement("name");
        ne.setTextContent(type);
        te.appendChild(ne);
        return te;
    }

    protected void generateCustomFields() throws Exception {
        List<String> fields = new ArrayList<String>();
        File[] files = scanDirectory("objects", "object");
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            Document object = documentBuilder.parse(files[i]);
            NodeList nodes = object.getDocumentElement().getChildNodes();
            String objectName = Utils.stripExtension(files[i].getName());
            fields.addAll(createFieldNames(objectName, nodes));
        }
        Element types = createTypesNode("CustomField", fields);
        document.getDocumentElement().appendChild(types);
    }

    protected void generateDocuments() throws Exception {
        Element types = createTypesNode("Document", getDocumentArtifactNames(null));
        document.getDocumentElement().appendChild(types);
    }

    protected List<String> createFieldNames(String objectName, NodeList nodes) {
        List<String> names = new ArrayList<String>();
        List<Element> fields = Utils.getElementsByName(nodes, "fields");
        for (int i = 0; i < fields.size(); i++) {
            Element name = Utils.getFirstElementByName(fields.get(i).getChildNodes(), "fullName");
            names.add(objectName + "." + name.getTextContent());
        }
        return names;
    }

    protected List<String> getDocumentArtifactNames(String folder) {
        List<String> result = new ArrayList<String>();
        File target = new File(targetDirectory + File.separator + "documents" + ((folder != null) ? File.separator + folder : ""));
        File[] files = target.listFiles();
        if (files == null) {
            return Collections.EMPTY_LIST;
        }
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            if (files[i].isDirectory()) {
                result.addAll(getDocumentArtifactNames(name));
            } else if (name.endsWith("-meta.xml")) {
                result.add(((folder != null) ? folder + "/" : "") + name.substring(0, name.length() - 9));
            }
        }
        return result;
    }
}
