package pe.createfoxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 *concrete implementation of CreateFoXML.  This implementation creates one
 *foxml file and adds a new datastream for each file found as it traverses the
 *directory structure.  This will add a dublin_core datastream to the foxml.
 *the dublin core metadata is read from the properties file.  Also adds a mods
 *datastream to the foxml.
 * @author ppound
 */
public class CreateFoXMLOneStreamPerFile extends CreateFoXML {

    private Document foXMLDoc = null;

    private String dc_file;

    private String mods_file;

    private int fileCount = 0;

    /** Creates a new instance of CreateFoXMLOneStreamPerFile
     * @param propertyFile
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public CreateFoXMLOneStreamPerFile(String propertyFile) throws FileNotFoundException, IOException {
        props = new Properties();
        props.load(new FileInputStream(new File(propertyFile)));
        ext = props.getProperty("extension");
        urlPrefix = props.getProperty("urlPrefix");
        directory = props.getProperty("directory");
        replaceWhiteSpace = props.getProperty("replacewhitespace", "false");
        maxFiles = new Integer(props.getProperty("maxFiles", "100")).intValue();
        pidPrefix = props.getProperty("pidPrefix");
        pidSuffix = new Integer(props.getProperty("pidSuffix")).intValue();
        collection = props.getProperty("collection");
        directoryForFoXML = props.getProperty("directoryForGenereatedXML");
        dc_file = props.getProperty("dublin_core_file");
        mods_file = props.getProperty("mods_file");
        System.out.println("dc_file " + dc_file);
    }

    protected void processFile(File file) throws IOException, DocumentException {
        if (count > maxFiles) {
            throw new RuntimeException("Max number of files have been processed");
        }
        String fileName = file.getName();
        Namespace defaultNS = new Namespace("foxml", "info:fedora/fedora-system:def/foxml#");
        String url = null;
        StringTokenizer st = new StringTokenizer(getExt());
        while (st.hasMoreTokens()) {
            if (fileName.substring(fileName.length() - 3).equals(st.nextToken())) {
                if (replaceWhiteSpace.equals("true")) {
                    file = createNewFile(file);
                    fileName = file.getName();
                }
                url = file.getPath().substring(getDirectory().length());
                url = url.replace('\\', '/');
                if (foXMLDoc == null) {
                    foXMLDoc = createXMLDocument(file, url, null);
                }
                createDataStreamFromFile(foXMLDoc.getRootElement(), defaultNS, file, url);
                System.out.println("Processed--- " + file.getPath());
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new RuntimeException("Path to property file not set. usage: CreateFoXML pathtopropertyfile");
        }
        String propertyFile = args[0];
        if (propertyFile == null || propertyFile.equals("")) {
            throw new RuntimeException("Path to property file not set. usage: CreateFoXML pathtopropertyfile");
        }
        try {
            CreateFoXMLOneStreamPerFile main = new CreateFoXMLOneStreamPerFile(propertyFile);
            try {
                main.processDirectory(new File(getDirectory()));
                main.writeFoXML();
            } catch (DocumentException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * creates a foxml document for ingest into fedora
     * @param file
     * @param url
     * @param dcDoc
     * @return
     */
    public Document createXMLDocument(File file, String url, Document dcDoc) {
        String label = file.getName();
        QName rootName = DocumentFactory.getInstance().createQName("digitalObject", "foxml", "info:fedora/fedora-system:def/foxml#");
        pid = pidPrefix + pidSuffix++;
        Element root = DocumentFactory.getInstance().createElement(rootName);
        root.add(new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
        root.addAttribute("xsi:schemaLocation", "info:fedora/fedora-system:def/foxml# http://www.fedora.info/definitions/1/0/foxml1-0.xsd");
        root.add(new Namespace("xlink", "http://www.w3.org/TR/xlink"));
        root.addAttribute("PID", pid);
        Document document = DocumentFactory.getInstance().createDocument(root);
        Namespace defaultNS = new Namespace("foxml", "info:fedora/fedora-system:def/foxml#");
        Element objectProperties = root.addElement(new QName("objectProperties", defaultNS));
        objectProperties.addElement(new QName("property", defaultNS)).addAttribute("NAME", "http://www.w3.org/1999/02/22-rdf-syntax-ns#type").addAttribute("VALUE", "FedoraObject");
        objectProperties.addElement(new QName("property", defaultNS)).addAttribute("NAME", "info:fedora/fedora-system:def/model#label").addAttribute("VALUE", label);
        objectProperties.addElement(new QName("property", defaultNS)).addAttribute("NAME", "info:fedora/fedora-system:def/model#ownerId").addAttribute("VALUE", "fedoraAdmin");
        addDCToElement(root, dc_file);
        createRelationShips(root, defaultNS);
        addModsElements(root, defaultNS, mods_file);
        return document;
    }

    private void addDCToElement(Element root, String existingDC) {
        Document dcDoc = readExisitingXML(existingDC);
        if (dcDoc == null) {
            return;
        }
        root.add(dcDoc.getRootElement());
    }

    private void writeFoXML() throws IOException {
        File foxmlFile = new File(directoryForFoXML + File.separator + "MultiStream_foXML.xml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(foxmlFile), format);
        writer.write(foXMLDoc);
        writer.close();
    }
}
