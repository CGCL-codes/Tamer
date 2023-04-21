package library.utils;

import java.io.File;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import library.LibraryBaseServer;
import library.enums.DocumentType;
import library.enums.Language;
import library.enums.Library;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Cgi2XmlConverter {

    public static final String fieldNameRegex = "([A-Z]{1}[a-zA-Z ()]*?):{1}";

    public static final String fieldBodyOffset = "[ ]+";

    public static final String lineTerminator = "\r\n";

    public static final String fieldBody = "(.*?)" + lineTerminator;

    public static final String emptyLine = "={80}" + lineTerminator;

    public static final String fieldRegex = fieldNameRegex + "((" + fieldBodyOffset + fieldBody + ")+)";

    public static final String record = "(" + fieldRegex + "(" + lineTerminator + ")*" + ")+" + emptyLine + "(" + lineTerminator + ")*";

    private static Random rand = new Random();

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
        if (args.length - 1 < 2 || (args.length - 1) % 2 == 1) {
            System.out.println("usage: java Cgi2XmlConverter " + "record1-endnote.cgi record1-fullrecord.cgi" + " [record2xx record2yy ...] output.xml");
            System.exit(1);
        }
        File xmlFile = new File(args[args.length - 1]);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document xmlDocument = builder.newDocument();
        Element root = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_ROOT);
        xmlDocument.appendChild(root);
        System.out.println("starting:");
        System.out.println();
        for (int i = 0; i < args.length - 1; i = i + 2) {
            String endNote = TextFile.read(args[i]);
            String full = TextFile.read(args[i + 1]);
            System.out.println();
            System.out.println("  processing " + args[i] + " & " + args[i + 1]);
            Matcher recordMatcherEndNote = Pattern.compile(record).matcher(endNote);
            Matcher recordMatcherFull = Pattern.compile(record).matcher(full);
            int counter = 1;
            while (recordMatcherEndNote.find() && recordMatcherFull.find()) {
                String recordEndNote = recordMatcherEndNote.group();
                Matcher fieldMatcherEndNote = Pattern.compile(fieldRegex).matcher(recordEndNote);
                Element document = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_DOCUMENT);
                while (fieldMatcherEndNote.find()) {
                    String field = fieldMatcherEndNote.group();
                    Matcher nameMatcher = Pattern.compile(fieldNameRegex).matcher(field);
                    if (nameMatcher.find()) {
                        String name = nameMatcher.group(1);
                        String body = field.substring(name.length() + 1).replaceAll("[ ]{2,}", "");
                        String[] bodyValues = body.split("\\.[ ]*\r\n");
                        if (bodyValues[bodyValues.length - 1].equals("[ ]*(\r\n)*")) {
                            String[] b = new String[bodyValues.length - 1];
                            System.arraycopy(bodyValues, 0, b, 0, bodyValues.length - 1);
                            bodyValues = b;
                        }
                        for (String str : bodyValues) {
                            str = str.replaceAll("\r\n", " ");
                            if (name.equalsIgnoreCase("Author")) {
                                String[] tempName = str.split(", ");
                                String authorLastName = tempName.length == 0 ? "-" : tempName.length == 3 ? tempName[0] + ", " + tempName[2] : tempName[0];
                                String authorFirstName = tempName.length < 2 ? "-" : tempName[1];
                                if (authorLastName.charAt(authorLastName.length() - 1) == ',') {
                                    authorLastName = authorLastName.substring(0, authorLastName.length() - 1);
                                }
                                if (authorFirstName.charAt(authorFirstName.length() - 1) == ',') {
                                    authorFirstName = authorFirstName.substring(0, authorFirstName.length() - 1);
                                }
                                Element authorFirstElement = xmlDocument.createElement(LibraryBaseServer.XML_ATTRIBUTE_AUTHOR_FIRST);
                                Element authorLastElement = xmlDocument.createElement(LibraryBaseServer.XML_ATTRIBUTE_AUTHOR_LAST);
                                Text authorFirstText = xmlDocument.createTextNode(authorFirstName);
                                Text authorLastText = xmlDocument.createTextNode(authorLastName);
                                authorFirstElement.appendChild(authorFirstText);
                                authorLastElement.appendChild(authorLastText);
                                document.appendChild(authorFirstElement);
                                document.appendChild(authorLastElement);
                            } else if (name.equalsIgnoreCase("City")) {
                                Element cityElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_CITY);
                                Text cityText = xmlDocument.createTextNode(str);
                                cityElement.appendChild(cityText);
                                document.appendChild(cityElement);
                            } else if (name.equalsIgnoreCase("Notes")) {
                                Element notesElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_NOTE);
                                Text notesText = xmlDocument.createTextNode(str);
                                notesElement.appendChild(notesText);
                                document.appendChild(notesElement);
                            } else if (name.equalsIgnoreCase("Publisher")) {
                                Element publisherElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_PUBLISHER);
                                Text publisherText = xmlDocument.createTextNode(str);
                                publisherElement.appendChild(publisherText);
                                document.appendChild(publisherElement);
                            } else if (name.equalsIgnoreCase("Series Title")) {
                                Element serieElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_SERIES);
                                Text serieText = xmlDocument.createTextNode(str);
                                serieElement.appendChild(serieText);
                                document.appendChild(serieElement);
                            } else if (name.equalsIgnoreCase("Title")) {
                                Element titleElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_TITLE);
                                Text titleText = xmlDocument.createTextNode(str);
                                titleElement.appendChild(titleText);
                                document.appendChild(titleElement);
                            } else if (name.equalsIgnoreCase("Translator")) {
                                String[] tempName = str.split(", ");
                                String translatorLastName = tempName.length == 0 ? "-" : tempName.length == 3 ? tempName[0] + ", " + tempName[2] : tempName[0];
                                String translatorFirstName = tempName.length < 2 ? "-" : tempName[1];
                                if (translatorLastName.charAt(translatorLastName.length() - 1) == ',') {
                                    translatorLastName = translatorLastName.substring(0, translatorLastName.length() - 1);
                                }
                                if (translatorFirstName.charAt(translatorFirstName.length() - 1) == ',') {
                                    translatorFirstName = translatorFirstName.substring(0, translatorFirstName.length() - 1);
                                }
                                Element translatorFirstElement = xmlDocument.createElement(LibraryBaseServer.XML_ATTRIBUTE_TRANSLATOR_FIRST);
                                Element translatorLastElement = xmlDocument.createElement(LibraryBaseServer.XML_ATTRIBUTE_TRANSLATOR_LAST);
                                Text translatorFirstText = xmlDocument.createTextNode(translatorFirstName);
                                Text translatorLastText = xmlDocument.createTextNode(translatorLastName);
                                translatorFirstElement.appendChild(translatorFirstText);
                                translatorLastElement.appendChild(translatorLastText);
                                document.appendChild(translatorFirstElement);
                                document.appendChild(translatorLastElement);
                            } else if (name.equalsIgnoreCase("ISBN")) {
                                Element isbnElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_ISBN);
                                Text isbnText = xmlDocument.createTextNode(str);
                                isbnElement.appendChild(isbnText);
                                document.appendChild(isbnElement);
                            }
                        }
                    }
                }
                String recordFull = recordMatcherFull.group();
                Matcher fieldMatcherFull = Pattern.compile(fieldRegex).matcher(recordFull);
                while (fieldMatcherFull.find()) {
                    String field = fieldMatcherFull.group();
                    Matcher nameMatcher = Pattern.compile(fieldNameRegex).matcher(field);
                    if (nameMatcher.find()) {
                        String name = nameMatcher.group(1);
                        String body = field.substring(name.length() + 1).replaceAll("[ ]{2,}", "");
                        String[] bodyValues = body.split("\\.[ ]*\r\n");
                        if (bodyValues[bodyValues.length - 1].equals("[ ]*(\r\n)*")) {
                            String[] b = new String[bodyValues.length - 1];
                            System.arraycopy(bodyValues, 0, b, 0, bodyValues.length - 1);
                            bodyValues = b;
                        }
                        for (String str : bodyValues) {
                            str = str.replaceAll("\r\n", " ");
                            if (name.equalsIgnoreCase("Description")) {
                                Element descriptionElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_DESCRIPTION);
                                Text descriptionText = xmlDocument.createTextNode(str);
                                descriptionElement.appendChild(descriptionText);
                                document.appendChild(descriptionElement);
                            } else if (name.equalsIgnoreCase("Linked Items")) {
                                Element linkedItemsElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_LINKED_ITEMS);
                                Text linkedItemsText = xmlDocument.createTextNode(str);
                                linkedItemsElement.appendChild(linkedItemsText);
                                document.appendChild(linkedItemsElement);
                            } else if (name.equalsIgnoreCase("Subject(s)")) {
                                Element keywordElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_KEYWORD);
                                Text keywordText = xmlDocument.createTextNode(str);
                                keywordElement.appendChild(keywordText);
                                document.appendChild(keywordElement);
                            } else if (name.equalsIgnoreCase("Variant Title")) {
                                Element linkedItemsElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_TITLE);
                                Text linkedItemsText = xmlDocument.createTextNode(str);
                                linkedItemsElement.appendChild(linkedItemsText);
                                document.appendChild(linkedItemsElement);
                            } else if (name.equalsIgnoreCase("Primary Material")) {
                                Element documentTypeElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_DOCUMENT_TYPE);
                                String converted = str.trim().toUpperCase().replaceAll("[ ]", "_");
                                DocumentType dt = DocumentType.getValue(converted);
                                Text documentTypeText = xmlDocument.createTextNode(converted);
                                documentTypeElement.appendChild(documentTypeText);
                                document.appendChild(documentTypeElement);
                            }
                        }
                    }
                }
                Element libraryElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_LIBRARY);
                Attr itemsAttribute = xmlDocument.createAttribute(LibraryBaseServer.XML_ATTRIBUTE_ITEMS_COUNT);
                itemsAttribute.setNodeValue(rand.nextInt(20) + "");
                Text libraryText = xmlDocument.createTextNode(Library.IT_LIBRARY.getDatabaseName());
                libraryElement.setAttributeNode(itemsAttribute);
                libraryElement.appendChild(libraryText);
                document.appendChild(libraryElement);
                NodeList docTypeList = document.getElementsByTagName(LibraryBaseServer.XML_ELEMENT_DOCUMENT_TYPE);
                if (docTypeList.getLength() == 0) {
                    Element documentTypeElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_DOCUMENT_TYPE);
                    Text documentTypeText = xmlDocument.createTextNode(DocumentType.NULL + "");
                    documentTypeElement.appendChild(documentTypeText);
                    document.appendChild(documentTypeElement);
                }
                NodeList languageList = document.getElementsByTagName(LibraryBaseServer.XML_ELEMENT_LANGUAGE);
                if (languageList.getLength() == 0) {
                    Element languageElement = xmlDocument.createElement(LibraryBaseServer.XML_ELEMENT_LANGUAGE);
                    Text languageText = xmlDocument.createTextNode(Language.NULL + "");
                    languageElement.appendChild(languageText);
                    document.appendChild(languageElement);
                }
                root.appendChild(document);
                System.out.print("    (" + counter++ + ")  ");
                if (counter % 10 == 0) {
                    System.out.println();
                }
            }
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        Source xmlSource = new DOMSource(xmlDocument);
        Result xmlResult = new StreamResult(xmlFile);
        t.transform(xmlSource, xmlResult);
        System.out.println();
        System.out.println();
        System.out.println("done..");
    }
}
