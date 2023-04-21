package bigraphspace.model.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import bigraphspace.io.BigraphReader;
import bigraphspace.io.IOConstants;
import bigraphspace.model.BasicSignature;
import bigraphspace.model.Bigraph;
import bigraphspace.sorting.SortingException;

/**
 * @author cmg
 *
 */
public class DomXmlBigraphReader extends BigraphReader {

    /** cons */
    DomXmlBigraphReader(BasicSignature signature) {
        super(IOConstants.FORMAT_XML);
        this.setSignature(signature);
    }

    @Override
    public Bigraph read(String text) throws IOException, SortingException {
        return read(new ByteArrayInputStream(text.getBytes("UTF-8")));
    }

    @Override
    public Bigraph read(InputStream is) throws IOException, SortingException {
        DomBigraph bigraph = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setCoalescing(true);
            factory.setExpandEntityReferences(false);
            factory.setIgnoringComments(true);
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            factory.setIgnoringElementContentWhitespace(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            bigraph = new DomBigraph(signature, doc);
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception e) {
            throw new IOException("Reading XML bigraph", e);
        }
        validate(bigraph);
        return bigraph;
    }

    public Bigraph read(Reader reader) throws IOException, SortingException {
        try {
            return read(new ReaderInputStream(reader));
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception e) {
            throw new IOException("Reading XML bigraph", e);
        }
    }

    /** workaround class */
    static class ReaderInputStream extends InputStream {

        /** cons */
        ReaderInputStream(Reader reader) {
            this.reader = reader;
            baos = new ByteArrayOutputStream();
            osw = new OutputStreamWriter(baos);
        }

        protected Reader reader;

        protected byte[] buf;

        protected int pos;

        protected ByteArrayOutputStream baos;

        protected OutputStreamWriter osw;

        public int read() throws IOException {
            if (buf == null || pos >= buf.length) {
                int c = reader.read();
                if (c < 0) return c;
                osw.write(c);
                osw.flush();
                buf = baos.toByteArray();
                baos.reset();
                pos = 0;
                if (buf.length == 0) throw new IOException("Read char '" + ((char) c) + "' (" + c + ") but did not get any bytes back");
            }
            return buf[pos++];
        }
    }
}
