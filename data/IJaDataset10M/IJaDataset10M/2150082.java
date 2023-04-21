package XmlFilter;

import java.io.File;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;
import XmlFilter.xmlFilterException.FilterException;

/**
 * Implementa l'interfaccia Filter per filtrare un elemento xml.
 * Il filtro permette di trasformare un nodo dell'albero creato dal componente Accessibility.
 * Per trasformare il nodo utilizza un file xsl "viewAccessible.xsl" all'interno del
 * quale si trovano le regole di trasformazione.
 * @author Costa Riccardo
 *
 */
public class FilterAccessible implements Filter {

    /**
	 * Filtra un determinato nodo xml proveniente da documenti creati dal componente Accessibility.
	 * Per trasformare l'elemento utilizza un file xsl.
	 * @param root Elemento da filtrare.
	 * @return Restituisce l'intero ramo, a partire dal nodo root, filtrato.
	 * @throws FilterException - Errore generato durante la trasformazione xsl.
	 */
    public Element filterElement(Element root) throws FilterException {
        if (root == null) throw new IllegalArgumentException("Element root null");
        Document doc = new Document(root);
        JDOMResult out = new JDOMResult();
        JDOMSource xmlSource = new JDOMSource(doc);
        Source xsltSource = new StreamSource(new File(filterXslFileName));
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans;
        try {
            trans = transFact.newTransformer(xsltSource);
            trans.transform(xmlSource, out);
        } catch (TransformerConfigurationException e) {
            throw new FilterException("Configuration xslt error");
        } catch (TransformerException e) {
            throw new FilterException("Trasformation xml error");
        }
        return out.getDocument().detachRootElement();
    }

    public void setXSLFilter(String name) {
        filterXslFileName = name;
    }

    private String filterXslFileName = "viewFilter.xsl";
}
