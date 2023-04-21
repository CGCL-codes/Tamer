package org.ita.osgi.mule.serviceImpl.econtract2owl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.ita.osgi.opaals.econtract2owl.Activator;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

/**
 * @author emendoza
 *
 */
public class Econtract2owlImpl implements Econtract2owl {

    private static String stylesheetRelativePath = "resources/econtract2owl.xsl";

    private static LinkedList<URL> sourceEcontractURLs = new LinkedList<URL>();

    public String addEcontractsToOwl(String[] sourceFilesURLs) {
        addSourceEContractsURLs(sourceFilesURLs);
        return generateOWL();
    }

    public String removeEcontractsFromOwl(String[] sourceFilesURLs) {
        removeSourceEContractsURLs(sourceFilesURLs);
        return generateOWL();
    }

    /**
	 * Add some eContracts source file URLs to the eContracts source file
	 * URLs list
	 * @param sourceFilesURLs the eContracts source file URLs to add
	 */
    private void addSourceEContractsURLs(String[] sourceFilesURLs) {
        URL newURL;
        for (int i = 0; i < sourceFilesURLs.length; i++) {
            try {
                newURL = new URL(sourceFilesURLs[i]);
                if (!sourceEcontractURLs.contains(newURL)) sourceEcontractURLs.add(newURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Remove some eContracts source file URLs from the eContracts source file
	 * URLs list
	 * @param sourceFilesURLs the eContracts source file URLs to remove
	 */
    private void removeSourceEContractsURLs(String[] sourceFilesURLs) {
        URL newURL;
        for (int i = 0; i < sourceFilesURLs.length; i++) {
            try {
                newURL = new URL(sourceFilesURLs[i]);
                if (sourceEcontractURLs.contains(newURL)) {
                    sourceEcontractURLs.remove(newURL);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Generate the OWL file
	 * @return the OWL file URL
	 */
    private String generateOWL() {
        File temp;
        try {
            temp = File.createTempFile("mergedSource", ".xml");
            temp.deleteOnExit();
            if (!mergeSourceEContracts(temp.getAbsolutePath())) {
                return "ERROR: Can't merge source eContracts.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR: IOException. Can't merge source eContracts.";
        }
        URL stylesheetURL;
        try {
            stylesheetURL = FileLocator.resolve(FileLocator.find(Activator.getBundleContext().getBundle(), new Path(stylesheetRelativePath), null));
        } catch (IOException e1) {
            e1.printStackTrace();
            return "ERROR: IOException. Can't resolve stylesheet URL.";
        }
        if (transformEContracts2Owl(temp, stylesheetURL, Activator.getLocalOwlUrl())) {
            replaceInFile(Activator.getLocalOwlUrl(), "&amp;", "&");
            return Activator.getRemoteOwlUrl();
        }
        return "ERROR: Can't transform source eContracts into an ontology.";
    }

    /**
	 * Merge the files which URLs are stored in sourceEcontractURLs
	 * @param destFile the generated file path
	 * @return false in case of error. true otherwise
	 */
    private boolean mergeSourceEContracts(String destFile) {
        Element mergedFileRoot = new Element("contracts");
        ListIterator<URL> it = sourceEcontractURLs.listIterator();
        while (it.hasNext()) {
            URL currentURL = it.next();
            SAXBuilder builder = new SAXBuilder(false);
            org.jdom.Document sourceEcontrDoc;
            try {
                sourceEcontrDoc = builder.build(currentURL);
            } catch (JDOMException e) {
                e.printStackTrace();
                sourceEcontractURLs.remove(currentURL);
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                sourceEcontractURLs.remove(currentURL);
                continue;
            }
            Element sourceEcontractRoot = sourceEcontrDoc.getRootElement();
            if (!sourceEcontractRoot.getName().equals("contract")) {
                sourceEcontractURLs.remove(currentURL);
                continue;
            }
            Element contractElement = new Element("contract");
            Element filename = new Element("filename");
            String sourceFilePath = currentURL.getPath();
            filename.addContent(sourceFilePath.substring(sourceFilePath.lastIndexOf('/') + 1, sourceFilePath.lastIndexOf('.')));
            contractElement.addContent(0, filename);
            contractElement.addContent(sourceEcontractRoot.cloneContent());
            mergedFileRoot.addContent(contractElement);
        }
        org.jdom.Document mergedFileDoc = new org.jdom.Document(mergedFileRoot);
        try {
            XMLOutputter out = new XMLOutputter();
            FileOutputStream file = new FileOutputStream(destFile);
            out.output(mergedFileDoc, file);
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
	 * Generate an ontology owl file form an eContracts file and an xsl file
	 * @param sourceEContractsFile tje source eContracts file
	 * @param stylesheetFile the xsl file
	 * @param owlFile the owl generated file
	 * @return false in case of error. true otherwise
	 */
    private boolean transformEContracts2Owl(File sourceEContractsFile, URL stylesheetURL, URL owlURL) {
        org.w3c.dom.Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        try {
            document = builder.parse(sourceEContractsFile.getAbsolutePath());
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        StreamSource stylesource = new StreamSource(stylesheetURL.toString());
        Transformer transformer;
        try {
            transformer = tFactory.newTransformer(stylesource);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(owlURL.toString());
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean replaceInFile(URL file, String toReplace, String replacing) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
            String line = "", oldtext = "";
            while ((line = reader.readLine()) != null) {
                oldtext += line + "\r\n";
            }
            reader.close();
            String newtext = oldtext.replaceAll(toReplace, replacing);
            FileWriter writer = new FileWriter(file.getPath());
            writer.write(newtext);
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    public String getOwlURL() {
        return Activator.getRemoteOwlUrl();
    }
}
