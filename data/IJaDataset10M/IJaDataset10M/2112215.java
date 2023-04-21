package basis;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author Tom
 */
public class WoordXMLParser extends XMLParser {

    private ArrayList woorden;

    private ArrayList categorieen;

    private boolean woordGevonden = false;

    private String categorie;

    private Woord woord;

    public WoordXMLParser() {
        super();
        woorden = new ArrayList();
        categorieen = new ArrayList();
    }

    public boolean laadBestand(String bestandsNaam) {
        boolean gelezen = false;
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(new File(bestandsNaam), handler);
            gelezen = true;
        } catch (Throwable t) {
            t.printStackTrace();
            gelezen = false;
        }
        return gelezen;
    }

    public void leesWoordenSet(String bestandsNaam) {
        laadBestand(bestandsNaam);
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = new String(buf, offset, len);
        if (woordGevonden) {
            woord = new Woord(s, categorie);
            woorden.add(woord);
        }
    }

    public void startElement(String nameSpaceURI, String lName, String qName, Attributes attrs) throws SAXException {
        String eName = lName;
        if ("".equals(eName)) eName = qName;
        if (eName.equals("woordenset")) {
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String aName = attrs.getLocalName(i);
                    if ("".equals(aName)) aName = attrs.getQName(i);
                    if (aName.equals("categorie")) {
                        categorie = attrs.getValue(i);
                        categorieen.add(categorie);
                    }
                }
            }
        }
        if (eName.equals("woord")) {
            woordGevonden = true;
        }
    }

    public void endElement(String namespaceURI, String lName, String qName) throws SAXException {
        String eName = qName;
        if (eName.equals("woord")) {
            woordGevonden = false;
            woord = null;
        }
    }

    public boolean laadDirectory(String dir) {
        File directory = new File(dir);
        String bestanden[] = directory.list();
        for (int i = 0; i < bestanden.length; i++) {
            File bestand = new File(bestanden[i]);
            if (!bestand.isDirectory() && bestand.getName().contains("woord") && bestand.getName().endsWith(".xml")) {
                leesWoordenSet(dir + bestand.getName());
            }
        }
        return true;
    }

    public ArrayList getWoorden() {
        return woorden;
    }

    public String getWoord(String categorie) {
        String woordString = new String();
        Iterator woordit = woorden.iterator();
        while (woordit.hasNext()) {
            Woord woord = (Woord) woordit.next();
            if (woord.getCategorie() == categorie) {
                woordString = woord.getWoord();
                break;
            }
        }
        return woordString;
    }

    public ArrayList getCategorieen() {
        return categorieen;
    }
}
