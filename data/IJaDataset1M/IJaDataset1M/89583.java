package org.exmaralda.dgd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.exmaralda.exakt.utilities.FileIO;
import org.exmaralda.partitureditor.jexmaralda.convert.StylesheetFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.xml.sax.SAXException;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Schmidt
 */
public class TabulateEventMetadata {

    Document result;

    String directory;

    String HTML_XSL = "/org/exmaralda/dgd/metadataOverview2HTML.xsl";

    public TabulateEventMetadata(String directory) {
        Element re = new Element("metadata");
        result = new Document(re);
        this.directory = directory;
    }

    void doit() throws JDOMException, IOException {
        File dir = new File(directory);
        File[] files = dir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.matches("[A-Z][A-Z]([A-Z]|-){2}_E_\\d{5}\\.xml");
            }
        });
        for (File f : files) {
            System.out.println("Reading " + f.getAbsolutePath());
            try {
                Document xmlDoc = FileIO.readDocumentFromLocalFile(f);
                if (!(xmlDoc.getRootElement().getName().equals("Ereignis"))) {
                    continue;
                }
                Element cre = new Element("event");
                cre.setAttribute("event-id", xmlDoc.getRootElement().getAttributeValue("Kennung"));
                cre.setAttribute("file", f.getName());
                result.getRootElement().addContent(cre);
                addElement("//Basisdaten/Sonstige_Bezeichnungen", xmlDoc, cre);
                addElement("//Basisdaten/Beschreibung", xmlDoc, cre);
                addElement("//Basisdaten/Ort/Region", xmlDoc, cre);
                addElement("//Basisdaten/Institution", xmlDoc, cre);
                addElement("//Basisdaten/Räumlichkeiten", xmlDoc, cre);
                addElement("//Basisdaten/Datum/YYYY-MM-DD", xmlDoc, cre);
                addElement("//Basisdaten/Aufnahmebedingungen", xmlDoc, cre);
                List l = XPath.newInstance("//Sprechereignis/Sprecher").selectNodes(xmlDoc);
                Vector<Element> speakers = new Vector<Element>();
                for (Object o : l) {
                    Element speaker = (Element) o;
                    speakers.add(speaker);
                }
                for (Element speaker : speakers) {
                    speaker.setAttribute("SE-Kennung", speaker.getParentElement().getAttributeValue("Kennung"));
                    speaker.setAttribute("sigle", speaker.getChild("Basisdaten").getChildText("Sigle_in_Transkripten"));
                    speaker.detach();
                    speaker.removeContent();
                    cre.addContent(speaker);
                }
            } catch (FileNotFoundException fnfe) {
                System.out.println("File does not exist");
            }
        }
    }

    void writeResultList(String xmlPath) throws IOException, SAXException, ParserConfigurationException, TransformerConfigurationException, TransformerException {
        FileIO.writeDocumentToLocalFile(new File(xmlPath), result);
        String htmlPath = xmlPath.replaceAll("\\.xml", ".html");
        StylesheetFactory sf = new StylesheetFactory(true);
        String html = sf.applyInternalStylesheetToExternalXMLFile(HTML_XSL, xmlPath);
        FileOutputStream fos = new FileOutputStream(new File(htmlPath));
        fos.write(html.getBytes("UTF-8"));
        fos.close();
        System.out.println(htmlPath + " written.");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.out.println("Usage: TabulateEventMetadata directory output.xml");
                System.exit(0);
            }
            TabulateEventMetadata tabulator = new TabulateEventMetadata(args[0]);
            tabulator.doit();
            tabulator.writeResultList(args[1]);
        } catch (SAXException ex) {
            Logger.getLogger(TabulateEventMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TabulateEventMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(TabulateEventMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(TabulateEventMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(TabulateEventMetadata.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TabulateEventMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addElement(String xpString, Document xmlDoc, Element cre) throws JDOMException {
        XPath xp = XPath.newInstance(xpString);
        Element sb = (Element) (xp.selectSingleNode(xmlDoc));
        if (sb != null) {
            sb.detach();
            cre.addContent(sb);
        } else {
            Element e = new Element(xpString.substring(xpString.lastIndexOf("/") + 1));
            e.setText("Element nicht vorhanden");
            cre.addContent(e);
        }
    }
}
