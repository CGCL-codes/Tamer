package org.componentbasedtesting.accessibilityView.accessibilityView;

import java.io.*;
import org.jdom.*;
import org.jdom.output.*;
import org.jdom.input.*;

/**
 * Definizione di un repository che memorizza i documenti delle rappresentazioni dell'interfaccia utente su file.
 * @author Giacomo Perreca
 */
public class FileAccessibilityViewRepository extends AccessibilityViewRepository {

    private XMLOutputter serializer;

    private SAXBuilder builder;

    private Document lastDocument;

    private String lastDocumentName;

    /**
	 * Costruisce un nuovo repository che memorizza i documenti delle rappresentazioni su file.
	 */
    public FileAccessibilityViewRepository() {
        super();
        serializer = new XMLOutputter(Format.getPrettyFormat());
        builder = new SAXBuilder(true);
        return;
    }

    /**
	 * Elimina dal repository un documento della rappresentazione dell'interfaccia utente.
	 *  
	 * @param documentName il nome del documento della rappresentazione da eliminare.
	 * @throws java.lang.IllegalArgumentException il nome del documento non pu� essere null.
	 */
    public boolean deleteDocument(String documentName) {
        if (documentName == null || documentName.length() == 0) throw new IllegalArgumentException("null documentName");
        File f = new File(documentName);
        return f.delete();
    }

    /**
	 * Restituisce true se il documento della rappresentazione dell'interfaccia utente specificato esiste
	 * nel repository, false altrimenti. Deve essere implementato dal repository concreto.
	 * 
	 * @param documentName nome del documento della rappresentazione.
	 * @return true se il documento esiste, false altrimenti.
	 * @throws java.lang.IllegalArgumentException il nome del documento ed il documento non possono essere null.
	 */
    public boolean documentExists(String documentName) {
        if (documentName == null || documentName.length() == 0) throw new IllegalArgumentException("null documentName");
        File f = new File(documentName);
        return f.exists();
    }

    /**
	 * Restituisce il documento della rappresentazione dell'interfaccia utente memorizzato nel repository avente
	 * il nome specificato dal parametro in input. Se il documento non esiste o vi sono problemi durante il caricamento
	 * viene restituito null.
	 * 
	 * @param documentName il nome del documento della rappresentazione memorizzata da restituire.
	 * @return il documento della rappresentazione memorizzata.
	 * @throws java.lang.IllegalArgumentException il nome del documento non pu� essere null.
	 */
    public Document getDocument(String documentName) {
        if (documentName == null || documentName.length() == 0) throw new IllegalArgumentException("null documentName");
        if (documentName.compareTo(lastDocumentName) == 0) return lastDocument;
        Document doc;
        try {
            doc = builder.build(documentName);
            lastDocumentName = documentName;
            lastDocument = doc;
        } catch (JDOMException ex) {
            doc = null;
        } catch (IOException ex) {
            doc = null;
        }
        return doc;
    }

    /**
	 * Memorizza un documento della rappresentazione dell'interfaccia utente associandolo ad un nome. Restituisce
	 * true se l'operazione ha esito positivo, false altrimenti.
	 * 
	 * @param documentName nome del documento della rappresentazione.
	 * @param document documento della rappresentazione.
	 * @return esito dell'operazione.
	 * @throws java.lang.IllegalArgumentException il nome del documento ed il documento non possono essere null.
	 */
    protected boolean put(String documentName, Document document) {
        if (documentName == null || documentName.length() == 0) throw new IllegalArgumentException("null documentName");
        if (document == null) throw new IllegalArgumentException("null document");
        boolean res = true;
        try {
            BufferedOutputStream o = new BufferedOutputStream(new FileOutputStream(documentName));
            serializer.output(document, o);
            o.flush();
            o.close();
            lastDocument = document;
            lastDocumentName = documentName;
        } catch (IOException ioex) {
            res = false;
        }
        return res;
    }
}
