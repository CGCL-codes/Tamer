package fr.crim.m2im.a2009.NadiaMakouar;

import java.io.File;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import java.util.HashMap;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

public class CharlieParker2 extends DefaultHandler {

    /** Noeud texte courant, aliment� au passage de characters().
	 * Cette variable inscrite ici pour la classe la rend accessible � toutes
	 * les m�thodes de la classe, dans l'�tat o� l'a laiss� la derni�re modification.  */
    StringBuilder text;

    /** Chemin de l'�l�ment courant en cours de traitement,
	 * permet de garder la m�moire de ses parents. */
    String path = "";

    /** cr�ation des variables. */
    String lemme;

    String categorie;

    String orthography;

    HashMap<String, String> formes;

    public CharlieParker2(HashMap<String, String> formes) {
        this.formes = formes;
    }

    public CharlieParker2() {
    }

    /** D�but du Main */
    public static void main(String[] args) throws Exception {
        File fichier = new File("src/corpus/test.txt");
        String[] texte = Util.charger(fichier, "UTF-8").toLowerCase(Util.locale).split(Util.split);
        HashMap<String, String> Fformes = Util.effectifs(texte);
        String uri = new File("src/corpus/Morphalou-2.0.xml").getAbsolutePath();
        if (args.length > 0) uri = args[0];
        XMLReader parseur = XMLReaderFactory.createXMLReader();
        CharlieParker2 monfiltre = new CharlieParker2(Fformes);
        parseur.setContentHandler(monfiltre);
        parseur.setErrorHandler(monfiltre);
        System.out.println("parsing de Morphalou....");
        parseur.parse(uri);
        System.out.println("G�n�ration du fichier xml....");
        new FichierXML(texte, Fformes);
        System.out.println("Fichier sortie.xml cr�e! oufff....");
    }

    public void startElement(String nsURI, String lName, String qName, Attributes attributes) {
        this.path = this.path + "/" + qName;
        this.text = new StringBuilder();
    }

    public void characters(char[] ch, int start, int length) {
        this.text.append(ch, start, length);
    }

    public void endElement(String nsURI, String lName, String qName) throws SAXException {
        String valeur = this.text.toString().trim();
        if (this.path.equals("/lexicon/lexicalEntry/formSet/lemmatizedForm/orthography")) {
            this.lemme = valeur.trim();
        }
        if (lName.equals("grammaticalCategory")) {
            this.categorie = valeur.trim();
        }
        if (lName.equals("orthography")) {
            this.orthography = valeur.trim();
            if (formes.containsKey(this.orthography)) {
                formes.put(this.orthography, this.lemme + ";" + this.categorie);
            }
        }
        this.path = this.path.substring(0, this.path.lastIndexOf("/" + qName));
    }
}
