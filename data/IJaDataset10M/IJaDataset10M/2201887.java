package ch.ideenarchitekten.vip.config;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.xml.namespace.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import ch.ideenarchitekten.vip.logic.exception.*;

/**
 * Um die Applikation so dynamisch wie m�glich zu gestalten, werden viele Konstanten
 * und alle Text-Literale in einer XML Datei gespeichert. Diese XML Dateien werden von 
 * dieser Klasse eingelesen und durch die Singleton-Realisierung kann von jeder Stelle der
 * Applikation auf die Konstanten zugegriffen werden.
 * 
 * Zuerst wir eine XML Master Datei gelesen. In dieser stehen anschliessend Anweisungen, wie die 
 * anderen XML Dateien heissen und zu Parsen sind. Dadurch wird eine maximale Flexibilit�t und 
 * Erweiterbarkeit erreicht.
 * @author $LastChangedBy: martinschaub $
 * @version $LastChangedRevision: 397 $
 */
public final class Config {

    /**
	 * Eine Wrapper Klasse, welche zwei String Instanzen beinhaltet.
	 * Diese Klasse wurde statt eines Arrays gew�hlt, da so ohne das Wissen der
	 * Array Indizies zugegriffen werden kann.
	 * @author $LastChangedBy: martinschaub $
	 * @version $LastChangedRevision: 397 $
	 */
    public static class Literal {

        /**
		 * Enth�lt den ersten String, meist wird er mit dem Textteil einer Nachricht gef�llt.
		 */
        private String m_text;

        /**
		 * Enth�lt den zweiten String, meist wird er mit dem Titel einer Nachricht gef�llt.
		 */
        private String m_title;

        /**
		 * Erstellt ein Wrapperobjekt.
		 * @param text Textteil der Nachricht. Muss vorhanden sein.
		 * @param title Titel der Nachricht. Fakultativ
		 */
        public Literal(String text, String title) {
            assert text != null : "Vorbedingung nicht erf�llt: text ist null";
            m_text = text;
            m_title = title;
        }

        /**
		 * Den Textteil zur�ckgeben
		 * @return Textteil
		 */
        public String getText() {
            return m_text;
        }

        /**
		 * Den Titel zur�ckgeben
		 * @return Titel
		 */
        public String getTitle() {
            return m_title;
        }
    }

    /**
	 * Name der XML Masterdatei.
	 */
    private static final String XMLFILES = "xml-files.xml";

    /**
	 * Name der Kategorie, welche die Einstellungen der Applikation repr�sentiert.
	 */
    public static final String CATEGORY_PARAMETER = "parameters";

    /**
	 * Gr�sse des Buffers f�r das Auslesen der Datei.
	 */
    private static final int BUFFER_SIZE = 1024;

    /**
	 * Einzige Instanz der Klasse f�r die Singleton realisierung.
	 */
    private static Config s_instance;

    /**
	 * Enth�lt alle eingelesenen XML Dokumente f�r das sp�tere Lesen
	 * durch DOM.
	 */
    private HashMap<String, Document> m_documents;

    /**
	 * Speichert alle Dateien und die Kategorien zwischen.
	 */
    private HashMap<String, String> m_files;

    /**
	 * Initialisiert die Klasse indem die XML Dateien geparsed werden.
	 */
    private Config() {
        final String xmlFilesEntry = "file";
        final String categoryName = "category";
        final String fileName = "name";
        final String schemaName = "schema";
        final String writableName = "writeable";
        m_documents = new HashMap<String, Document>();
        m_files = new HashMap<String, String>();
        try {
            DocumentBuilder xmlFilesBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlFiles = xmlFilesBuilder.parse(getConfigFileString(XMLFILES));
            NodeList xmlFileList = xmlFiles.getElementsByTagName(xmlFilesEntry);
            for (int i = 0, z = xmlFileList.getLength(); i < z; ++i) {
                NamedNodeMap attributes = xmlFileList.item(i).getAttributes();
                Node name = attributes.getNamedItem(fileName);
                Node schema = attributes.getNamedItem(schemaName);
                Node category = attributes.getNamedItem(categoryName);
                Node writable = attributes.getNamedItem(writableName);
                if (name != null && schema != null && category != null) {
                    parseFile(name.getNodeValue(), schema.getNodeValue(), category.getNodeValue(), Boolean.parseBoolean(writable.getNodeValue()));
                }
            }
        } catch (SAXException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XMLFileError, getConfigFileString(XMLFILES));
        } catch (ParserConfigurationException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XMLParseError, getConfigFileString(XMLFILES));
        } catch (IOException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XMLFileReadError, getConfigFileString(XMLFILES));
        }
    }

    /**
	 * Hilfsklasse f�r das Lesen einer XML Datei, mit �berpr�fen der Validit�t �ber
	 * ein Schema und dem Hinzuf�gen zur Liste aller geparsten XML Dokumente.
	 * @param file Relativer Dateiname der XML Datei.
	 * @param schema Relativer Dateiname des Schemas.
	 * @param category Name der Kategorie unter der das Dokument in die Liste der geparsten Dokumente eingef�gt wird.
	 * @param writable true, falls die Datei ge�ndert werden kann und deshalb kopiert werden muss.
	 */
    private void parseFile(String file, String schema, String category, boolean writable) {
        assert !m_documents.containsKey(category) : "Vorbedinung nicht erf�llt: Kategorie bereits enthalten";
        assert category != null : "Vorbedinung nicht erf�llt: category ist null";
        assert schema != null : "Vorbedinung nicht erf�llt: schema ist null";
        assert file != null : "Vorbedinung nicht erf�llt: file ist null";
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            DocumentBuilderFactory factoryDocBuilder = DocumentBuilderFactory.newInstance();
            factoryDocBuilder.setSchema(schemaFactory.newSchema(getConfigFile(schema)));
            DocumentBuilder docBuilder = factoryDocBuilder.newDocumentBuilder();
            Document doc;
            if (writable) {
                File f = new File(file);
                if (!f.exists()) {
                    try {
                        InputStream in = getClass().getResourceAsStream(file);
                        OutputStream out = new FileOutputStream(file);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int nRead = 0;
                        while ((nRead = in.read(buffer)) >= 0) {
                            out.write(buffer, 0, nRead);
                        }
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        ExceptionHandler exp = ExceptionHandler.getInstance();
                        exp.handleException(e, ExceptionHandler.errors.CouldNotCopyXMLFile, file);
                    }
                }
                doc = docBuilder.parse(file);
            } else {
                doc = docBuilder.parse(getConfigFileString(file));
            }
            m_documents.put(category, doc);
            m_files.put(category, file);
        } catch (SAXException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XMLFileError, getConfigFileString(file));
        } catch (ParserConfigurationException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XMLParseError, getConfigFileString(file));
        } catch (IOException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XMLFileReadError, getConfigFileString(file));
        }
    }

    /**
	 * Gibt die als Singleton realisierte Instanz der Klasse zur�ck.
	 * @return Singleton der Klasse
	 */
    public static synchronized Config getInstance() {
        if (s_instance == null) {
            s_instance = new Config();
        }
        assert s_instance != null : "Nachbedinung nicht erf�llt: m_instance ist null";
        return s_instance;
    }

    /**
	 * Hilfsfunktion der die den Pfad einer Datei als String zur�ckgibt. Dabei wird 
	 * davon ausgegangen, dass sich die Datei im selben Verzeichnis wie die Klasse Config
	 * befindet.
	 * @param file Dateiname ohne Pfad.
	 * @return Voller Dateiname mit Pfad
	 */
    private String getConfigFileString(String file) {
        assert file != null : "Vorbedingung nicht erf�llt: file ist null";
        String ret = null;
        try {
            ret = getConfigFile(file).toURI().toString();
        } catch (URISyntaxException e) {
            throw new InternalError();
        }
        return ret;
    }

    /**
	 * Hilfsfunktion die den Pfad einer Datei als URL zur�ckgibt. Dabei wird 
	 * davon ausgegangen, dass sich die Datei im selben Verzeichnis wie die Klasse Config
	 * befindet.
	 * @param file Dateiname 
	 * @return URL f�r den vollen Pfad
	 */
    private URL getConfigFile(String file) {
        assert file != null : "Vorbedingung nicht erf�llt: file ist null";
        return getClass().getResource(file);
    }

    /**
	 * Parsed das gew�schte XML Dokument und gibt eine NodeList mit den Ergebnissen zur�ck.
	 * @param category Unter welchem Namen das XML Dokument zu m_documents hinzugef�gt worden ist(XML Master File)
	 * @param xpathExpression Der zu ausf�hrende XPath Ausdruck.
	 * @return Eine Liste von Nodes (NodeList), die als Kompartiblit�t mit anderen �berladenen Funktionen als Object behandelt wird.
	 * @throws XPathExpressionException Bei einem ung�ltigen XPath Ausdruck.
	 */
    public Object getNodes(String category, String xpathExpression) throws XPathExpressionException {
        return getNodes(category, xpathExpression, XPathConstants.NODESET);
    }

    /**
	 * Parsed das gew�schte XML Dokument und gibt das Resultat im gew�schten Typ zur�ck.
	 * @param category Unter welchem Namen das XML Dokument zu m_documents hinzugef�gt worden ist(XML Master File)
	 * @param xpathExpression Der zu ausf�hrende XPath Ausdruck.
	 * @param type Was f�r ein R�ckgabewert erwartet wird.
	 * @return Das Resultat des XPath Ausdrucks. Der Typ h�ngt vom Parameter type ab.
	 * @throws XPathExpressionException Bei einem ung�ltigen XPath Ausdruck.
	 */
    public Object getNodes(String category, String xpathExpression, QName type) throws XPathExpressionException {
        assert category != null : "Vorbedingung nicht erf�llt: category ist null";
        assert xpathExpression != null : "Vorbedingung nicht erf�llt: elementName ist null";
        Object list = null;
        Document doc = m_documents.get(category);
        XPath xPath = XPathFactory.newInstance().newXPath();
        if (doc != null) {
            list = xPath.evaluate(xpathExpression, doc, type);
        }
        return list;
    }

    /**
	 * F�r die Realisierung in mehreren Sprachen wird im Dokument literals jeder Text in mehreren Sprachen 
	 * angeboten. Die getLiteral Funktion findet anhand der gesetzten Sprache und dem idetifier den gew�schten Eintrag
	 * heraus und gibt ihn zur�ck. Das Resulat besteht aus einem Titel, sowie einem Textelement. Diese werden
	 * in einem Wrapper-Objekt vom Typ Literal gespeichert, was einen knfortableren Zugriff als ein Array erlaubt.s
	 * @param identifier Eindeutige Identifikation in der XML Datei.
	 * @return Der Resultattext und Titel in der definierten Sprache.
	 */
    public Literal getLiteral(String identifier) {
        assert identifier != null : "Vorbedingung nicht erf�llt: identifier ist null";
        String title = null;
        String text = null;
        try {
            final String literalName = "literals";
            Node titleEl = (Node) getNodes(literalName, "//literal[@id = '" + identifier + "']/entry[@lang = '" + getConstant("language") + "']/title", XPathConstants.NODE);
            Node textEl = (Node) getNodes(literalName, "//literal[@id = '" + identifier + "']/entry[@lang = '" + getConstant("language") + "']/text", XPathConstants.NODE);
            Pattern removeBeginningWhitespaces = Pattern.compile("^\\s+");
            Pattern removeEndingWhitespaces = Pattern.compile("\\s+$");
            Pattern removeRepeatingWhitespaces = Pattern.compile("[\t ]+");
            Pattern removeSpacesNewLine = Pattern.compile("\\n\\s+");
            if (textEl != null) {
                text = textEl.getTextContent();
                Matcher beginningWhitespaces = removeBeginningWhitespaces.matcher(text);
                text = beginningWhitespaces.replaceAll("");
                Matcher endingWhitespaces = removeEndingWhitespaces.matcher(text);
                text = endingWhitespaces.replaceAll("");
                Matcher repeatingWhitespaces = removeRepeatingWhitespaces.matcher(text);
                text = repeatingWhitespaces.replaceAll(" ");
                Matcher spacesNewLine = removeSpacesNewLine.matcher(text);
                text = spacesNewLine.replaceAll("\n");
            }
            if (titleEl != null) {
                title = titleEl.getTextContent();
                Matcher beginningWhitespaces = removeBeginningWhitespaces.matcher(title);
                title = beginningWhitespaces.replaceAll("");
                Matcher endingWhitespaces = removeEndingWhitespaces.matcher(title);
                title = endingWhitespaces.replaceAll("");
                Matcher repeatingWhitespaces = removeRepeatingWhitespaces.matcher(title);
                title = repeatingWhitespaces.replaceAll(" ");
                Matcher spacesNewLine = removeSpacesNewLine.matcher(title);
                title = spacesNewLine.replaceAll("\n");
            }
        } catch (XPathExpressionException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        }
        Literal ret = null;
        if (text == null) {
            ret = new Literal("", "");
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(new Exception(identifier), ExceptionHandler.errors.LiteralNotFound, this);
        } else {
            ret = new Literal(text, title);
        }
        return ret;
    }

    /**
	 * Gibt den Node in der XML Parameter Datei zur�ck, der auf den 
	 * gegebenen eindeutigen String matched (d.h. das Attribut name ist 
	 * gleich dem String)
	 * @param name Der eindeutige Name des Parameters.
	 * @return Den gefundenen Node.
	 */
    private Node getConstantNode(String name) {
        assert name != null : "Vorbedingung nicht erf�llt: identifier ist name";
        Node constant = null;
        try {
            constant = (Node) getNodes(CATEGORY_PARAMETER, "//parameter[@name = '" + name + "']/*/@current", XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        }
        if (constant == null) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(new Exception(name), ExceptionHandler.errors.ParameterNotFound, this);
            constant = new javax.imageio.metadata.IIOMetadataNode();
        }
        assert constant != null : "Nachbedinung nicht erf�llt: constant ist null bei " + name;
        return constant;
    }

    /**
	 * Die Konstaten, f�r welche es sinnvoll sein kann, dass der Benutzer diese �ndern kann, werden in einer
	 * XML Datei mit der Kategorie parameters gespeichert. Diese Funktion durchsucht die Datei nach dem gew�schten
	 * Eintrag und liefert ihn zur�ck.
	 * @param name Eindeutiger Name der Konstante.
	 * @return Das Resultat der Abfrage.
	 */
    public String getConstant(String name) {
        assert name != null : "Vorbedingung nicht erf�llt: identifier ist name";
        return getConstantNode(name).getTextContent();
    }

    /**
	 * Speichert einen Wert in den Parameter name. Der Parameter muss existieren!
	 * Nach dem anpassen des XML Dom Baumes, wird dieser gerade in die Datei geschrieben.
	 * Das heisst, bei vielen �nderungen sollte die setConstantNoWrite verwendet werden und danach
	 * per flushConstants die Datei aktualisiert werden.
	 * @param name Name des Parameters.
	 * @param newValue Neuer Wert.
	 */
    public void setConstant(String name, String newValue) {
        assert name != null : "Vorbedingung nicht erf�llt: identifier ist name";
        setConstantNoWrite(name, newValue);
        flushConstants();
    }

    /**
	 * Speichert einen Wert in den Parameter name. Der Parameter muss existieren! 
	 * Der aktualisierte Baum wird nicht in die Datei geschrieben.
	 * @param name Name des Parameters.
	 * @param newValue Neuer Wert.
	 */
    public void setConstantNoWrite(String name, String newValue) {
        assert name != null : "Vorbedingung nicht erf�llt: identifier ist name";
        Node constant = getConstantNode(name);
        if (!constant.getTextContent().equals(newValue)) {
            constant.setNodeValue(newValue);
            PreferencesChangeController con = PreferencesChangeController.getInstance();
            con.handlePreferencesChange();
        }
    }

    /**
	 * Schreibt den XML DOM Baum zur�ck in die Datei.
	 */
    public void flushConstants() {
        try {
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            DOMSource src = new DOMSource(m_documents.get(CATEGORY_PARAMETER));
            StreamResult out = new StreamResult(new File(m_files.get(CATEGORY_PARAMETER)));
            trans.transform(src, out);
        } catch (TransformerConfigurationException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        } catch (TransformerFactoryConfigurationError e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        } catch (TransformerException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        }
    }

    /**
	 * Speichert die History von gew�hlten Computern in einer XML Datei.
	 * @param vals Strings, welche den Computernamen repr�sentieren
	 */
    public void flushNetworkHistory(Vector<String> vals) {
        final String historyName = "networkHistory";
        try {
            Node rootNode = (Node) getNodes(historyName, "/networkHistory", XPathConstants.NODE);
            int maxEntries = Integer.parseInt(getConstant("networkHistoryLength"));
            Document doc = m_documents.get(historyName);
            Node toDel = null;
            while ((toDel = rootNode.getFirstChild()) != null) {
                rootNode.removeChild(toDel);
            }
            for (int i = 0; i < vals.size() && i < maxEntries; ++i) {
                Element e = doc.createElement("entry");
                e.appendChild(doc.createTextNode(vals.get(i)));
                rootNode.appendChild(e);
            }
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            DOMSource src = new DOMSource(m_documents.get(historyName));
            StreamResult out = new StreamResult(new File(m_files.get(historyName)));
            trans.transform(src, out);
        } catch (TransformerConfigurationException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        } catch (TransformerFactoryConfigurationError e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        } catch (TransformerException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        } catch (XPathExpressionException e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        }
    }

    /**
	 * Speichert die ver�nderte SuitCaseConfig ab.
	 * 
	 * @param index welcher Koffer ge�ndert wird.
	 * @param newContent Die neuen Symbole
	 */
    public void saveSuitcaseConfig(int index, Object[] newContent) {
        try {
            final String suitcaseName = "suitcase";
            Document suitcaseDocument = m_documents.get(suitcaseName);
            NodeList suitcases = suitcaseDocument.getElementsByTagName("suitcase");
            Node modifiedSuitcase = suitcases.item(index);
            NodeList childs = modifiedSuitcase.getChildNodes();
            for (int i = 0; i < childs.getLength(); ++i) {
                modifiedSuitcase.removeChild(childs.item(i));
            }
            for (int i = 0; i < newContent.length; i++) {
                Node symbol = suitcaseDocument.createElement("content");
                Attr symattr = suitcaseDocument.createAttribute("name");
                symattr.setValue(newContent[i].toString());
                symbol.appendChild(symattr);
                modifiedSuitcase.appendChild(symbol);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(new File(m_files.get(suitcaseName)));
            DOMSource source = new DOMSource(suitcaseDocument);
            transformer.transform(source, result);
        } catch (Exception e) {
            ExceptionHandler exp = ExceptionHandler.getInstance();
            exp.handleException(e, ExceptionHandler.errors.XPathError, this);
        }
    }
}
