package es.unav.informesgoogleanalytics.modelo;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author enorvelle
 */
public class Configuración {

    private Document doc;

    private XPath xpath;

    /**
     * Constructor de la clase.  Encuentra y lee el archivo de configuración,
     * señalando una excepción en el caso de que no fuera encontrado.
     * 
     * @throws Exception
     */
    public Configuración(String archivoConfiguración) throws Exception {
        File configFile = new File(archivoConfiguración);
        if (!configFile.exists()) throw new Exception("Archivo de configuración no encontrado");
        DocumentBuilderFactory factoryDOM = DocumentBuilderFactory.newInstance();
        factoryDOM.setNamespaceAware(true);
        DocumentBuilder builder = factoryDOM.newDocumentBuilder();
        doc = builder.parse(new FileInputStream(configFile));
        XPathFactory factoryXPath = XPathFactory.newInstance();
        xpath = factoryXPath.newXPath();
    }

    /**
     * Dado una clave, busca en el documento y devuelve el valor correspondiente
     */
    public String getValor(String clave) throws XPathExpressionException {
        XPathExpression expr = xpath.compile(clave);
        NodeList lista = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return ((Node) lista.item(0)).getTextContent();
    }

    /**
     * Devolver una lista (Vector) de los nombres de las páginas a incluir en el informe.
     * @return Un Vector de los nombres de los sitios
     * @throws XPathExpressionException Señalado en caso de problema con la expresión XPath
     */
    public Vector<String> getNombresDeSitios() throws XPathExpressionException {
        Vector<String> categorías = new Vector<String>();
        XPathExpression expr = xpath.compile("//Página[@incluir='1']");
        NodeList lista = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < lista.getLength(); i++) {
            Node nodoActual = ((Node) lista.item(i));
            NamedNodeMap attrs = nodoActual.getAttributes();
            String nombre = attrs.getNamedItem("nombre").getNodeValue();
            categorías.add(nombre);
        }
        return categorías;
    }

    /**
     * Devolver una lista de las rutas asociadas con una página particular
     * @param página El nombre de la página a buscar
     * @return Un Vector de las rutas asociadas
     * @throws XPathExpressionException Señalado en caso de problema con la expresión XPath
     */
    public Vector<String> getRutasParaPágina(String página) throws XPathExpressionException {
        Vector<String> rutas = new Vector<String>();
        XPathExpression expr = xpath.compile("//Página[@nombre='" + página + "']/Ruta");
        NodeList lista = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < lista.getLength(); i++) {
            Node nodoActual = ((Node) lista.item(i));
            String ruta = nodoActual.getTextContent();
            rutas.add(ruta);
        }
        return rutas;
    }

    /**
     * Devolver una lista de las rutas asociadas con una página particular
     * @param página El nombre de la página a buscar
     * @return Un Vector de las rutas asociadas
     * @throws XPathExpressionException Señalado en caso de problema con la expresión XPath
     */
    public HashMap<String, String> getRutasConNombres() throws XPathExpressionException {
        HashMap<String, String> rutasConNombres = new HashMap<String, String>();
        XPathExpression expr = xpath.compile("//Página");
        NodeList lista = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < lista.getLength(); i++) {
            Node nodoActual = ((Node) lista.item(i));
            NamedNodeMap attrs = nodoActual.getAttributes();
            String nombre = attrs.getNamedItem("nombre").getNodeValue();
            XPathExpression expr2 = xpath.compile("//Página[@nombre='" + nombre + "']/Ruta");
            NodeList lista2 = (NodeList) expr2.evaluate(doc, XPathConstants.NODESET);
            Node rutaNodo = lista2.item(0);
            String ruta = rutaNodo.getTextContent();
            rutasConNombres.put(ruta, nombre);
        }
        return rutasConNombres;
    }
}
