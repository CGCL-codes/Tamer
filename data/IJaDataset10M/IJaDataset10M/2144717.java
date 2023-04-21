package ar.com.oddie.core.xmlwrapper;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XMLUtilities {

    private static Logger logger = Logger.getLogger(XMLUtilities.class);

    private static String xmlEncoding = StringUtils.EMPTY;

    private static String xmlversion = StringUtils.EMPTY;

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String DEFAULT_VERSION = "1.0";

    public static String getEncoding() {
        return xmlEncoding;
    }

    public static void setEncoding(String encoding) {
        xmlEncoding = encoding;
    }

    public static String getVersion() {
        return xmlversion;
    }

    public static void setVersion(String version) {
        xmlversion = version;
    }

    /**
	 * Este metodo crea un objeto document a partir del archivo pasado por
	 * parametro
	 * 
	 * @param fichero
	 *            : El nombre del archivo a procesar.
	 * @return
	 * @throws Exception
	 */
    public static Document File2Document(String archivo) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ClassLoader loader = (XMLUtilities.class).getClassLoader();
        URL urlfichero = loader.getResource(archivo);
        Document XMLDoc = factory.newDocumentBuilder().parse(new InputSource(urlfichero.openStream()));
        return XMLDoc;
    }

    /**
	 * Guarda el Document pasado en el XML fisico.
	 * 
	 * @param texto
	 *            Texto del document listo para ser almacenado.
	 */
    public static void document2File(Document document, String path) {
        StringWriter strWriter = null;
        XMLSerializer serializeXML = null;
        OutputFormat ouputFormat = null;
        try {
            serializeXML = new XMLSerializer();
            strWriter = new StringWriter();
            ouputFormat = new OutputFormat();
            if (getEncoding() == StringUtils.EMPTY) {
                setEncoding(DEFAULT_ENCODING);
            }
            ouputFormat.setEncoding(getEncoding());
            if (getVersion() == StringUtils.EMPTY) {
                setVersion(DEFAULT_VERSION);
            }
            ouputFormat.setVersion(getVersion());
            ouputFormat.setIndenting(true);
            ouputFormat.setIndent(4);
            serializeXML.setOutputCharStream(strWriter);
            serializeXML.setOutputFormat(ouputFormat);
            serializeXML.serialize(document);
            strWriter.close();
        } catch (IOException ioEx) {
            logger.error(ioEx);
        }
        try {
            OutputStream fout = new FileOutputStream(path);
            OutputStream bout = new BufferedOutputStream(fout);
            if (getEncoding() == StringUtils.EMPTY) {
                setEncoding(DEFAULT_ENCODING);
            }
            OutputStreamWriter out = new OutputStreamWriter(bout, getEncoding());
            out.write(strWriter.toString());
            out.flush();
            out.close();
        } catch (UnsupportedEncodingException e) {
            logger.error("Error encoding", e);
        } catch (IOException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
	 * Este metodo convierte un objeto de tipo org.w3c.dom.Node a String
	 * 
	 * @param nodo
	 * @return
	 * @throws Exception
	 */
    public static String Node2String(Node nodo) throws Exception {
        StringWriter sw = new StringWriter();
        StreamResult sR = new StreamResult(sw);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(nodo), sR);
        return sw.toString();
    }

    /**
	 * Este metodo convierte un objeto de tipo org.w3c.dom.NodeList a String
	 * Este metodo solo sirve para pintar los resultados.
	 * 
	 * @param nodo
	 * @return
	 * @throws Exception
	 */
    public static String Nodes2String(NodeList nodes) throws Exception {
        StringBuffer lista = new StringBuffer();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = (Node) nodes.item(i);
            lista.append(XMLUtilities.Node2String(node));
        }
        return lista.toString();
    }

    /**
	 * Crea un Element de DOM a partir de un objeto.
	 * 
	 * @param doc
	 *            Document asociado al XML.
	 * @param obj
	 *            Objeto a persistir.
	 * @param name
	 *            Nombre del tag del Element.
	 * @param attributesToSerialize
	 *            Lista con los atributos del objeto a persistir.
	 * @return El elemento creado.
	 */
    public static Element createElementFromObject(Document doc, Object obj, String name, List<String> attributesToSerialize) {
        Element docEle = doc.createElement(name);
        String tag = StringUtils.EMPTY;
        try {
            for (Iterator<String> iter = attributesToSerialize.iterator(); iter.hasNext(); ) {
                tag = iter.next();
                Element idEle = doc.createElement(tag);
                String data = BeanUtils.getProperty(obj, tag);
                Text idText = doc.createTextNode(data);
                idEle.appendChild(idText);
                docEle.appendChild(idEle);
            }
        } catch (Throwable ex) {
            logger.error(ex);
        }
        return docEle;
    }
}
