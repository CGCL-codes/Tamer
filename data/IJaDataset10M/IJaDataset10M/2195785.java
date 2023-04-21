package br.uece.tcc.fh.jxta.util.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Enumeration;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;

public class FhJxtaQueryMessage {

    /**
     * A query SQL passada para a QueryMsg.
     */
    private String sqlQuery = null;

    /**
     * Creates a query object using the given base and power.
     *
     * @param   aQuery query Sql para a queryMsg.
     */
    public FhJxtaQueryMessage(String aQuery) {
        super();
        this.sqlQuery = aQuery;
    }

    /**
     * Creates a query object by parsing the given input stream.
     *
     * @param       stream the InputStream source of the query data.
     * @exception   IOException if an error occurs reading the stream.
     */
    public FhJxtaQueryMessage(InputStream stream) throws IOException {
        StructuredTextDocument document = (StructuredTextDocument) StructuredDocumentFactory.newStructuredDocument(new MimeMediaType("text/xml"), stream);
        Enumeration elements = document.getChildren();
        while (elements.hasMoreElements()) {
            TextElement element = (TextElement) elements.nextElement();
            if (element.getName().equals("sqlQuery")) {
                sqlQuery = element.getTextValue();
                continue;
            }
        }
    }

    /**
     * Retorna o valor de sqlQuery para a query.
     *
     * @return  o valor de sqlQuery para a query.
     */
    public String getSqlQuery() {
        return sqlQuery;
    }

    /**
     * Returns a Document representation of the query.
     *
     * @param   asMimeType the desired MIME type representation for the
     *          query.
     * @return  a Document form of the query in the specified MIME
     *          representation.
     */
    public Document getDocument(MimeMediaType asMimeType) {
        StructuredDocument document = (StructuredTextDocument) StructuredDocumentFactory.newStructuredDocument(asMimeType, "example:ExampleQuery");
        Element element;
        element = document.createElement("sqlQuery", getSqlQuery());
        document.appendChild(element);
        return document;
    }

    /**
     * Returns an XML String representation of the query.
     *
     * @return  the XML String representing this query.
     */
    public String toString() {
        try {
            StringWriter out = new StringWriter();
            StructuredTextDocument doc = (StructuredTextDocument) getDocument(new MimeMediaType("text/xml"));
            doc.sendToWriter(out);
            return out.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
