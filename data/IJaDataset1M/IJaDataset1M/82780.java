package org.qtitools.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ContentPackage {

    private static final int BUFFER_SIZE = 4096;

    private static final String MANIFEST_FILE_NAME = "imsmanifest.xml";

    private static final String[] TEST_EXPRESSION = { "/imscp:manifest/imscp:resources/imscp:resource[@type='imsqti_test_xmlv2p1']/@href", "/imscp:manifest/imscp:resources/imscp:resource[@type='imsqti_assessment_xmlv2p1']/@href" };

    private static final String[] ITEM_EXPRESSION = { "/imscp:manifest/imscp:resources/imscp:resource[@type='imsqti_item_xmlv2p0']/@href", "/imscp:manifest/imscp:resources/imscp:resource[@type='imsqti_item_xmlv2p1']/@href", "/imscp:manifest/imscp:resources/imscp:resource[@type='imsqti_item_xml_v2p1']/@href" };

    private class QTINamespaceContext implements NamespaceContext {

        private static final String DEFAULT_NAME_SPACE = "imscp";

        private static final String DEFAULT_NAME_SPACE_URI = "http://www.imsglobal.org/xsd/imscp_v1p1";

        private String prefix;

        private String uri;

        public QTINamespaceContext() {
            this(DEFAULT_NAME_SPACE, DEFAULT_NAME_SPACE_URI);
        }

        public QTINamespaceContext(String prefix, String uri) {
            this.prefix = prefix;
            this.uri = uri;
        }

        public String getNamespaceURI(String prefix) {
            if (prefix.equals(this.prefix)) return uri; else if (prefix.equals(XMLConstants.XML_NS_PREFIX)) return XMLConstants.XML_NS_URI; else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) return XMLConstants.XMLNS_ATTRIBUTE_NS_URI; else return XMLConstants.NULL_NS_URI;
        }

        public String getPrefix(String uri) {
            if (uri.equals(this.uri)) return prefix; else if (uri.equals(XMLConstants.XML_NS_URI)) return XMLConstants.XML_NS_PREFIX; else if (uri.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) return XMLConstants.XMLNS_ATTRIBUTE; else return null;
        }

        public Iterator<String> getPrefixes(String namespaceURI) {
            Collection<String> collection = new ArrayList<String>();
            collection.add(getPrefix(namespaceURI));
            return collection.iterator();
        }
    }

    private ZipInputStream zipInputStream;

    private File zipFile;

    private File destDirectory;

    private File manifestFile;

    public ContentPackage(InputStream inputStream) {
        this.zipInputStream = new ZipInputStream(inputStream);
    }

    public ContentPackage(ZipInputStream zipInputStream) {
        this.zipInputStream = zipInputStream;
    }

    public ContentPackage(String zipFileName) {
        this(new File(zipFileName));
    }

    public ContentPackage(File zipFile) {
        this.zipFile = zipFile;
    }

    public ContentPackage(URI zipUri) {
        this(new File(zipUri));
    }

    public void unpack(File destDirectory) {
        unpack(destDirectory, false);
    }

    public void unpack(File destDirectory, boolean delete) {
        if (delete) delete(destDirectory);
        if (destDirectory.exists()) throw new ContentPackageException("Destination directory already exists.");
        this.destDirectory = destDirectory;
        this.manifestFile = new File(destDirectory, MANIFEST_FILE_NAME);
        try {
            if (zipInputStream == null) zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File destFile = new File(destDirectory, zipEntry.getName());
                destFile.getParentFile().mkdirs();
                if (!zipEntry.isDirectory()) {
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int length;
                    while ((length = zipInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) output.write(buffer, 0, length);
                    output.close();
                    zipInputStream.closeEntry();
                }
            }
            zipInputStream.close();
        } catch (IOException ex) {
            throw new ContentPackageException(ex);
        }
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) delete(files[i]);
        }
        file.delete();
    }

    public File getDestination() {
        return destDirectory;
    }

    public File getManifest() {
        return manifestFile;
    }

    public File getTest() {
        NodeList list = null;
        for (String s : TEST_EXPRESSION) {
            list = getNodeList(s);
            if (list.getLength() > 0) break;
        }
        if (list == null) throw new ContentPackageException("Cannot find test.");
        return new File(destDirectory, list.item(0).getNodeValue());
    }

    public File[] getItems() {
        List<NodeList> list = new ArrayList<NodeList>();
        int length = 0;
        for (String s : ITEM_EXPRESSION) {
            NodeList l = getNodeList(s);
            list.add(l);
            length += l.getLength();
        }
        File[] result = new File[length];
        int j = 0;
        for (NodeList l : list) {
            for (int i = 0; i < l.getLength(); i++, j++) result[j] = new File(destDirectory, l.item(i).getNodeValue());
        }
        return result;
    }

    private NodeList getNodeList(String expression) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new QTINamespaceContext());
            InputSource input = new InputSource(getManifest().getAbsolutePath());
            return (NodeList) xpath.evaluate(expression, input, XPathConstants.NODESET);
        } catch (XPathException ex) {
            throw new ContentPackageException(ex);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("ContentPackage reading started.");
        File sourceFile = new File("C:/Home/jk2/asdel/QTI/qtiv2p1pd2/examples/test_package_minfiles/test_package_minfiles.zip");
        File destDirectory = new File("C:/temp/cpg");
        if (args.length > 0) sourceFile = new File(args[0]);
        if (args.length > 1) destDirectory = new File(args[1]);
        System.out.println("Source = '" + sourceFile + "'");
        ContentPackage cpg = new ContentPackage(sourceFile);
        cpg.unpack(destDirectory, true);
        System.out.println("Destination = '" + cpg.getDestination() + "'");
        System.out.println("Manifest = '" + cpg.getManifest() + "'");
        File test = cpg.getTest();
        System.out.println("Test = '" + test + "'");
        System.out.print("Validating " + test + " ...");
        XmlUtils.validate(test);
        System.out.println(" done");
        System.out.println("Items:");
        File[] items = cpg.getItems();
        for (int i = 0; i < items.length; i++) {
            System.out.println(items[i]);
            System.out.print("Validating " + items[i] + " ...");
            XmlUtils.validate(items[i]);
            System.out.println(" done");
        }
        System.out.println("ContentPackage reading finished.");
    }
}
