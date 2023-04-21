package au.edu.archer.metadata.mde.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.memory.MemoryStore;

/**
 * This helper class provides some common methods for manipulating RDF 'documents'.
 * We use Sesame to process RDF, so the representation is (for now) a RepositoryConnection.
 * 
 * @author crawley
 */
public class RDFHelper {

    private static String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(RDFHelper.class);

    private RDFHelper() {
    }

    /**
     * Create an empty RDF document.  
     * 
     * @return the document
     * @throws ParserConfigurationException
     */
    public static RepositoryConnection createDocument() throws RepositoryException {
        SailRepository sr = new SailRepository(new MemoryStore());
        sr.initialize();
        return sr.getConnection();
    }

    public static RepositoryConnection decodeRDFDocument(String content) throws RDFParseException, RepositoryException, IOException {
        RepositoryConnection conn = RDFHelper.createDocument();
        conn.add(new StringReader(content), "", RDFFormat.RDFXML);
        return conn;
    }

    public static RepositoryConnection decodeRDFDocument(byte[] content) throws RDFParseException, RepositoryException, IOException {
        RepositoryConnection conn = RDFHelper.createDocument();
        conn.add(new ByteArrayInputStream(content), "", RDFFormat.RDFXML);
        return conn;
    }

    public static String encodeRDFDocument(RepositoryConnection document) throws RepositoryException, RDFHandlerException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        RDFXMLWriter w = new IdStrippingRDFXMLWriter(bos);
        document.export(w);
        return bos.toString();
    }

    public static Object readRDFDocument(BufferedReader br, int i) throws RDFParseException, RepositoryException, IOException {
        RepositoryConnection conn = RDFHelper.createDocument();
        conn.add(br, "", RDFFormat.RDFXML);
        return conn;
    }

    /**
     * This RDFWriter tweaks the standard RDFXMLPrettyWriter to throw away any nodeID 
     * attributes on blank nodes.
     * 
     * @author scrawley
     */
    private static class IdStrippingRDFXMLWriter extends RDFXMLPrettyWriter {

        public IdStrippingRDFXMLWriter(OutputStream out) {
            super(out);
        }

        public IdStrippingRDFXMLWriter(Writer writer) {
            super(writer);
        }

        @Override
        protected void writeAttribute(String namespace, String attName, String value) throws IOException {
            if (attName.equals("nodeID") && namespace.equals(RDF_NS)) {
                return;
            }
            super.writeAttribute(namespace, attName, value);
        }
    }
}
