package org.maveryx.utils.xml.filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.maveryx.utils.xml.filter.exception.FilterException;
import org.maveryx.utils.xml.filter.exception.SaveXmlException;
import org.maveryx.utils.xml.filter.exception.XmlFilterException;

/**
 * XmlFilter permette di filtrare un documento xml.
 * Contiene metodi per filtrare un'intero documento xml o un nodo specifico dell'albero.
 * @author Riccardo Costa
 * @version 1.1
 */
public class XmlFilter {

    /**
	 * Filtro da utilizzare durante l'elaborazione del documento xml.
	 */
    private Filter myFilter = null;

    /**
	 * Costruisce un oggetto XmlFilter utilizzando il filtro passato come argomento.
	 * @param filter Filtro da usare per trasformare il documento.
	 * Un valore null di filter potrebbe comportare eccezioni di tipo XmlFilterException 
	 * nelle chiamate dei metodi filter o avere risultati inaspettati.
	 * E' comunque possibile settare il filtro utilizzando il metodo setFilter.
	 */
    public XmlFilter(Filter filter) {
        myFilter = filter;
    }

    /**
	 * Permette di filtrare un documento xml.
	 * @param xmlFile File che contiene il documento xml da filtrare.
	 * @return Restituisce il documento filtrato.
	 * @throws XmlFilterException - Errore generato durante il filtraggio del documento.
	 */
    public Document filter(File xmlFile) throws XmlFilterException {
        if (xmlFile == null) throw new IllegalArgumentException("xmlFile null");
        SAXBuilder domBuild = new SAXBuilder();
        Document document = null;
        try {
            document = domBuild.build(xmlFile);
        } catch (JDOMException e) {
            throw new XmlFilterException("Creation JDOM error");
        } catch (IOException e) {
            throw new XmlFilterException("IO error to open xml file");
        }
        return filter(document);
    }

    /**
	 * Permette di filtrare un documento xml salvando il documento in un file.
	 * @param xmlFile File che contiene il documento xml da filtrare.
	 * @param fileNameOutput Nome del file su cui salvare il documento. Non pu� essere null.
	 * @return Restituisce il file dove viene salvato il documento filtrato.
	 * @throws XmlFilterException - Errore generato durante il filtraggio del documento.
	 * @throws SaveXmlException - Errore generato durante il salvataggio del documento.
	 */
    public File filterToSave(File xmlFile, String fileNameOutput) throws XmlFilterException, SaveXmlException {
        if (xmlFile == null) throw new IllegalArgumentException("xmlFile null");
        if (fileNameOutput == null) throw new IllegalArgumentException("fileNameOutput null");
        return saveToFile(filter(xmlFile), fileNameOutput);
    }

    /**
	 * Permette di filtrare un documento xml.
	 * @param doc Documento xml da filtrare.
	 * @return Restituisce il documento filtrato.
	 * @throws XmlFilterException - Errore generato durante il filtraggio del documento.
	 */
    public Document filter(Document doc) throws XmlFilterException {
        if (doc == null) throw new IllegalArgumentException("Input Document null");
        return xmlTransform(doc);
    }

    /**
	 * Permette di filtrare un documento xml e di salvarlo in file.
	 * @param doc Documento xml da filtrare.
	 * @param fileNameOutput Nome del file su cui salvare il documento. Non pu� essere null.
	 * @return Restituisce il file dove viene salvato il documento filtrato.
	 * @throws XmlFilterException - Errore generato durante il filtraggio del documento.
	 * @throws SaveXmlException - Errore generato durante il salvataggio del documento.
	 */
    public File filterToSave(Document doc, String fileNameOutput) throws XmlFilterException, SaveXmlException {
        if (doc == null) throw new IllegalArgumentException("Input Document null");
        if (fileNameOutput == null) throw new IllegalArgumentException("fileNameOutput null");
        return saveToFile(filter(doc), fileNameOutput);
    }

    /**
	 * Filtra un elemento specifico del documento xml identificato dall'id.
	 * L'id e' un'espressione XPath utilizzata per scorrere l'albero xml e trovare uno specifico nodo.
	 * @param xmlFile File che contiene il documento xml da filtrare.
	 * @param id Espressione di tipo XPath. Nel caso di errato formato dell'espressione viene sollevata 
	 * un eccezione XmlFilterException.
	 * @return Restituisce il documento filtrato.
	 * @throws XmlFilterException - Errore generato durante il filtraggio del documento. 
	 */
    public Document filter(File xmlFile, String id) throws XmlFilterException {
        if (xmlFile == null) throw new IllegalArgumentException("xmlFile null");
        SAXBuilder domBuild = new SAXBuilder();
        Document document = null;
        try {
            document = domBuild.build(xmlFile);
        } catch (JDOMException e) {
            throw new XmlFilterException("Creation JDOM error");
        } catch (IOException e) {
            throw new XmlFilterException("IO error to open xml file");
        }
        return filter(document, id);
    }

    /**
	 * Filtra un elemento specifico del documento xml identificato dall'id.
	 * L'id e' un'espressione XPath utilizzata per scorrere l'albero xml e trovare uno specifico nodo.
	 * Salva il risultato in un file.
	 * @param xmlFile File che contiene il documento xml da filtrare.
	 * @param id Espressione di tipo XPath. Nel caso di errato formato dell'espressione viene sollevata 
	 * un eccezione XmlFilterException.
	 * @param fileNameOutput Nome del file su cui salvare il documento. Non pu� essere null.
	 * @return Restituisce il file dove viene salvato il documento filtrato.
	 * @throws XmlFilterException - Errore generato durante il filtraggio del documento.
	 * @throws SaveXmlException - Errore generato durante il salvataggio del documento. 
	 */
    public File filterToSave(File xmlFile, String id, String fileNameOutput) throws XmlFilterException, SaveXmlException {
        if (xmlFile == null) throw new IllegalArgumentException("xmlFile null");
        if (fileNameOutput == null) throw new IllegalArgumentException("fileNameOutput null");
        return saveToFile(filter(xmlFile, id), fileNameOutput);
    }

    /**
	 * Filtra un elemento specifico del documento xml identificato dall'id.
	 * L'id e' un'espressione XPath utilizzata per scorrere l'albero xml e trovare uno specifico nodo.
	 * @param doc Documento xml da filtrare.
	 * @param id Espressione di tipo XPath. Nel caso di errato formato dell'espressione viene sollevata 
	 * un eccezione XmlFilterException.
	 * @return Restituisce il documento filtrato.
	 * @throws XmlFilterException - Errore occorso durante il filtraggio del documento.
	 */
    public Document filter(Document doc, String id) throws XmlFilterException {
        if (doc == null) throw new IllegalArgumentException("Input Document null");
        JDOMXPath path = null;
        List results = null;
        Element root = null;
        try {
            path = new JDOMXPath(id);
            results = path.selectNodes(doc);
        } catch (JaxenException e) {
            throw new XmlFilterException("Selection node error");
        }
        root = doc.detachRootElement();
        root.removeContent();
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
            Element el = (Element) iter.next();
            Content node = el.detach();
            root.addContent(node);
        }
        doc.setRootElement(root);
        doc = xmlTransform(doc);
        return doc;
    }

    /**
	 * Filtra un elemento specifico del documento xml identificato dall'id.
	 * L'id e' un'espressione XPath utilizzata per scorrere l'albero xml e trovare uno specifico nodo.
	 * Salva il risultato in un file.
	 * @param doc Documento xml da filtrare.
	 * @param id Espressione di tipo XPath. Nel caso di errato formato dell'espressione viene sollevata 
	 * un eccezione XmlFilterException.
	 * @param fileNameOutput Nome del file su cui salvare il documento. Non pu� essere null.
	 * @return Restituisce il file dove viene salvato il documento filtrato.
	 * @throws XmlFilterException - Errore generato durante il filtraggio del documento.
	 * @throws SaveXmlException - Errore generato durante il salvataggio del documento. 
	 */
    public File filterToSave(Document doc, String id, String fileNameOutput) throws XmlFilterException, SaveXmlException {
        if (doc == null) throw new IllegalArgumentException("Input Document null");
        if (fileNameOutput == null) throw new IllegalArgumentException("fileNameOutput null");
        return saveToFile(filter(doc, id), fileNameOutput);
    }

    /**
	 * Setta il filtro da utilizzare per filtrare il documento xml.
	 * @param f Filtro da utilizzare.
	 */
    public void setFilter(Filter f) {
        myFilter = f;
    }

    /**
	 * Restituisce il filtro utilizzato per filtrare il documento xml.
	 * @return filtro utilizzato.
	 */
    public Filter getFilter() {
        return myFilter;
    }

    /**
	 * Trasforma il documento xml utilizzando il filtro settato. Nel caso in cui il filtro
	 * non fosse settato lancia un eccezione di tipo XmlFilterException.
	 * @param source Documento sorgente da trasformare.
	 * @return Restituisce il documento trasformato.
	 * @throws XmlFilterException - Errore generato durante la trasformazione.
	 */
    private Document xmlTransform(Document source) throws XmlFilterException {
        if (source == null) throw new IllegalArgumentException("source xml null");
        if (myFilter == null) throw new XmlFilterException("Filter not setting");
        Element root = source.detachRootElement();
        try {
            root = myFilter.filterElement(root);
            source.setRootElement(root);
        } catch (FilterException e) {
            throw new XmlFilterException("Filter error: " + e.getMessage());
        }
        return source;
    }

    /**
	 * Salva il documento xml passato in input in un file.
	 * @param source Documento xml da salvare.
	 * @param fileName Nome del file in cui salvare il documento xml.
	 * @return File sul quale � stato memorizzato il documento xml.
	 * @throws SaveXmlException - Errore generato durante il salvataggio del documento.
	 */
    private File saveToFile(Document source, String fileName) throws SaveXmlException {
        if (source == null) throw new IllegalArgumentException("source xml null");
        if (fileName == null) throw new IllegalArgumentException("filename to save xml null");
        FileOutputStream outFile = null;
        Format f = Format.getPrettyFormat();
        try {
            outFile = new FileOutputStream(fileName);
            new XMLOutputter(f).output(source, outFile);
            outFile.close();
        } catch (FileNotFoundException e) {
            throw new SaveXmlException("error to save file xml");
        } catch (IOException e) {
            throw new SaveXmlException("error to save file xml");
        }
        return new File(fileName);
    }
}
