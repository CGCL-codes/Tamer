package org.ita.osgi.mule.serviceImpl.dose;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.ita.implement.concept.SemanticMapperTfIdf;
import org.ita.implement.detect.DetectarIdioma;
import org.ita.main.Activator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Dose implements IDose {

    /** test that the connect to a Web service.  
	 * @return a hello message
	 * */
    private static final String resultFileName = "translator.xml";

    Document document;

    @Override
    public String echo(String string, String clientSession) {
        return "hello";
    }

    /** return the concept of a  word to include in a synset and indicate the language parameter 
	 *  We make a Stemmer of word synset and the word parameter  
	 * 	*/
    @Override
    public String translator(String ontology, String word, String lang) {
        try {
            SemanticMapperTfIdf semancticMapper = new SemanticMapperTfIdf(ontology, lang);
            semancticMapper.getConceptOfWord(word, lang);
            return semancticMapper.getConceptOfWord(word, lang);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: ontology dont found";
        }
    }

    /** the same funtion translator   
	 * return the concept of a  word to include in a synset and indicate the language parameter 
	 *  We make a Stemmer of word synset and the word parameter  
	 * 	*/
    @Override
    public String translatorNoOntology(String word, String lang) {
        URL fileURL = FileLocator.find(Activator.getBundleContext().getBundle(), new Path("resources/eContract_Traduccion.xml"), null);
        try {
            SemanticMapperTfIdf semancticMapper = new SemanticMapperTfIdf(fileURL.toString(), lang);
            semancticMapper.getConceptOfWord(word, lang);
            String concepto = semancticMapper.getConceptOfWord(word, lang);
            System.out.print("->" + concepto + "<-");
            return concepto;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: ontology dont found";
        }
    }

    /*** a partir de una uri detecta el idioma, siempre que este bien formada la lista de stopword y
	 *  est� ese idioma dentro del fichero de stopword
	 *  @return devuelve el idioma definido seg�n la clase Lang
	 * */
    @Override
    public String detectLangNO(String uri) {
        System.out.print(uri);
        DetectarIdioma idi = new DetectarIdioma(uri);
        return idi.lang();
    }

    @Override
    public String detectLangSI() {
        DetectarIdioma idi = new DetectarIdioma();
        return idi.lang();
    }

    @Override
    public String translatorXml(String uriXml, String UriOntology, String lang) {
        try {
            System.out.print("WS inside [" + UriOntology + "]");
            SemanticMapperTfIdf semancticMapper = new SemanticMapperTfIdf(new URL(UriOntology).toString(), lang);
            return loadXml(uriXml, semancticMapper, lang);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String loadXml(String uriXml, SemanticMapperTfIdf semancticMapper, String lang) {
        String resultURI = URLEncoder.encode(uriXml);
        URL pathResources = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        File resourceFile;
        try {
            pathResources = FileLocator.toFileURL(FileLocator.find(Activator.getBundleContext().getBundle(), new Path("/resources"), null));
            builder = factory.newDocumentBuilder();
            document = builder.parse(uriXml);
            searchConceptAttribute(semancticMapper, "id", lang);
            searchConceptAttribute(semancticMapper, "class", lang);
            searchConceptAttribute(semancticMapper, "name", lang);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            System.out.println(pathResources.getPath() + "/" + resultURI);
            resourceFile = new File(pathResources.getPath() + "/" + resultURI);
            FileOutputStream fos = new FileOutputStream(resourceFile);
            DOMSource source = new DOMSource(document.getLastChild());
            StreamResult result = new StreamResult(fos);
            transformer.transform(source, result);
            String urlResult = new URL("http", InetAddress.getLocalHost().getHostAddress().toString(), Activator.FILE_SERVER_PORT, "/" + resultURI).toString();
            urlResult = urlResult.replaceAll("%", "%25");
            return urlResult;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void searchConceptAttribute(SemanticMapperTfIdf semancticMapper, String nameAtribute, String lang) throws XPathExpressionException {
        NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate("//*[@" + nameAtribute + "]", document.getDocumentElement(), XPathConstants.NODESET);
        String ontologyAtribute;
        Node mynode;
        String idAtribute;
        int estaElConcepto = 0;
        int fin = "</concept>".length();
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getAttributes().getNamedItem(nameAtribute));
            mynode = nodes.item(i).getAttributes().getNamedItem(nameAtribute);
            idAtribute = mynode.toString();
            ontologyAtribute = semancticMapper.getConceptOfWord(idAtribute.substring(nameAtribute.length() + 2, idAtribute.length() - 1), lang);
            System.out.println(ontologyAtribute);
            if ((estaElConcepto = ontologyAtribute.indexOf("#")) > 0) {
                mynode.setNodeValue(ontologyAtribute.substring(estaElConcepto + 1, ontologyAtribute.length() - fin));
            }
        }
    }

    public static void main(String arg[]) {
        File file2 = new File(".\\resources\\eContract_Traduccion.xml");
        File file = new File(".\\resources\\oferta_hosting_1.xml");
        new Dose().translatorXml(file.getPath(), file2.getPath(), "es");
        file = new File(".\\resources\\oferta_housing_1.xml");
        new Dose().translatorXml(file.getPath(), file2.getPath(), "es");
        file = new File(".\\resources\\oferta_sgr_1.xml");
        new Dose().translatorXml(file.getPath(), file2.getPath(), "es");
        file = new File(".\\resources\\oferta_web_1.xml");
        new Dose().translatorXml(file.getPath(), file2.getPath(), "es");
    }
}
