package fr.crim.m2im.a2009.PierreMarchal;

import java.util.*;
import java.io.IOException;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
Pierre Marchal, M2 Ingénierie Multilingue (CRIM/INaLCO)
Objectif : étiquetage morphosyntaxique d'un texte
Consigne :
 - utiliser un parseur SAX pour extraire d'une BD lexicale (type Morphalou) les informations utiles
 - utiliser l'appareillage DOM pour construire un fichier XML en sortie
*/
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 1 && args.length != 2) {
            System.out.println("usage : java Main TXT_FILE [XML_FILE]");
            System.exit(0);
        }
        final String fichierSortie = (args.length == 1) ? null : args[1];
        final String morphalou = "./morphalou/Morphalou-2.0.xml";
        String texte = new String(Outils.ChargeTexte(args[0]));
        SortedMap<String, Set<String>> mesFormes = new TreeMap<String, Set<String>>();
        String tableauTexte[] = texte.split("[ \n\t,.?!:;'\"()]+");
        for (String token : tableauTexte) {
            mesFormes.put(token.toLowerCase(), new HashSet<String>());
        }
        XMLReader monXMLReader = XMLReaderFactory.createXMLReader();
        OpenTagger monHandler = new OpenTagger(mesFormes);
        monXMLReader.setContentHandler(monHandler);
        monXMLReader.setErrorHandler(monHandler);
        try {
            monXMLReader.parse(morphalou);
        } catch (SAXException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        OpenTagger.AjouteExtraPOS(mesFormes);
        XMLDocumentCreator xdc = new XMLDocumentCreator(texte, mesFormes);
        xdc.ConstruitDocumentXML();
        if (fichierSortie == null) {
            xdc.AfficheDocument();
        } else {
            xdc.EnregistreDocument(fichierSortie);
        }
    }
}
