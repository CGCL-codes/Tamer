package eu.annocultor.core.utils;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

/**
 * An implementation of the RDFWriter interface that writes RDF documents in
 * XML-serialized RDF format.
 */
public class SortedRDFXMLWriter extends RDFXMLWriter {

    List<Statement> statements;

    /**
	 * Creates a new RDFXMLWriter that will write to the supplied OutputStream.
	 * 
	 * @param out
	 *          The OutputStream to write the RDF/XML document to.
	 */
    public SortedRDFXMLWriter(OutputStream out) {
        this(new OutputStreamWriter(out, Charset.forName("UTF-8")));
    }

    /**
	 * Creates a new RDFXMLWriter that will write to the supplied Writer.
	 * 
	 * @param writer
	 *          The Writer to write the RDF/XML document to.
	 */
    public SortedRDFXMLWriter(Writer writer) {
        super(writer);
        statements = new LinkedList<Statement>();
    }

    @Override
    public void handleStatement(Statement st) throws RDFHandlerException {
        if (!writingStarted) {
            throw new RuntimeException("Document writing has not yet been started");
        }
        statements.add(st);
    }

    /**
	 * Returns predicate name in the form <code>ns:name</code>
	 */
    private String getPredicatePrintName(URI uri) {
        return uri.toString();
    }

    @Override
    public void endRDF() throws RDFHandlerException {
        try {
            if (!headerWritten) {
                writeHeader();
            }
            Collections.sort(statements, new Comparator<Statement>() {

                public int compare(Statement left, Statement right) {
                    int result = left.getSubject().stringValue().compareTo(right.getSubject().stringValue());
                    if (result == 0) {
                        result = getPredicatePrintName(left.getPredicate()).compareTo(getPredicatePrintName(right.getPredicate()));
                        if (result == 0) {
                            result = left.getObject().stringValue().compareTo(right.getObject().stringValue());
                        }
                    }
                    return result;
                }
            });
            for (Statement st : statements) {
                try {
                    super.handleStatement(st);
                } catch (Exception e) {
                    throw new Exception("Error handling statement " + st, e);
                }
            }
            statements.clear();
        } catch (Exception e) {
            throw new RDFHandlerException(e);
        }
        super.endRDF();
    }
}
