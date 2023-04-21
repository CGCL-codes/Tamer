package fr.crim.m2im.a2009.PierreMarchal;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class OpenTagger extends DefaultHandler {

    private SortedMap<String, Set<String>> mesFormes;

    private StringBuilder buffer;

    private StringBuilder path;

    private String pos;

    public OpenTagger(SortedMap<String, Set<String>> mesFormes) {
        super();
        this.mesFormes = mesFormes;
        buffer = new StringBuilder();
        path = new StringBuilder();
        pos = new String();
    }

    public void characters(char[] ch, int start, int length) {
        buffer.append(ch, start, length);
    }

    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes atts) throws SAXException {
        path.append("/" + localName);
    }

    public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
        if ((path.toString()).equals("/lexicon/lexicalEntry/formSet/lemmatizedForm/grammaticalCategory")) pos = buffer.toString(); else if ((path.toString()).equals("/lexicon/lexicalEntry/formSet/inflectedForm/orthography") && mesFormes.containsKey(buffer.toString())) mesFormes.get(buffer.toString()).add(pos); else if ((path.toString()).equals("/lexicon/lexicalEntry/formSet")) pos = "";
        buffer.delete(0, buffer.length());
        path.delete(path.lastIndexOf("/"), path.length());
    }

    /**
	AjouteExtraPOS:complete la table de hachage avec deux autres POS "numeral" et "unknown"
	@param mesFormes une table de hachage associant à chacune des formes du texte une ou plusieurs POS
	*/
    public static void AjouteExtraPOS(SortedMap<String, Set<String>> mesFormes) {
        Set cles = mesFormes.keySet();
        Iterator i = cles.iterator();
        Pattern p = Pattern.compile("^\\d+$");
        while (i.hasNext()) {
            String cle = i.next().toString();
            Matcher m = p.matcher(cle);
            if (m.matches()) {
                mesFormes.get(cle).add("numeral");
                continue;
            }
            if (mesFormes.get(cle).isEmpty()) mesFormes.get(cle).add("unknown");
        }
    }
}
