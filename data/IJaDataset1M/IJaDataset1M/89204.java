package net.sf.ediknight.codec.jdbc.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import net.sf.ediknight.ParseException;
import net.sf.ediknight.Recognizer;
import net.sf.ediknight.io.JailedInputStream;
import net.sf.ediknight.codec.jdbc.xml.JDBCXMLReader;
import net.sf.ediknight.record.RecordHandler;
import net.sf.ediknight.record.RecordParser;
import org.xml.sax.XMLReader;

/**
 * This is the JDBC parser.
 */
public final class JDBCParser implements RecordParser<JDBCFormat> {

    /** */
    private static final int BUFLEN = 1024;

    /** */
    private static final int EOF = -1;

    /** */
    private static final int EOL = 0;

    /** */
    private static final int SEPARATOR = 1;

    /** */
    private static final int DATA = 2;

    /** */
    private static final int TABLENAME = 3;

    /** */
    private Reader reader;

    /** */
    private String encoding;

    /** */
    private StringBuffer token = new StringBuffer();

    /** */
    private int ch;

    /** */
    private int tc;

    /** */
    private RecordHandler handler;

    /** */
    private JDBCRecognizer recognizer;

    /**
     * {@inheritDoc}
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * {@inheritDoc}
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * {@inheritDoc}
     */
    public RecordHandler getRecordHandler() {
        return handler;
    }

    /**
     * {@inheritDoc}
     */
    public void setRecordHandler(RecordHandler handler) {
        this.handler = handler;
    }

    /**
     * {@inheritDoc}
     */
    public void parse(File file) throws IOException, ParseException {
        if (file == null) {
            throw new NullPointerException();
        }
        InputStream stream = new FileInputStream(file);
        try {
            parse(stream);
        } finally {
            stream.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void parse(InputStream stream) throws IOException, ParseException {
        if (stream == null) {
            throw new NullPointerException();
        }
        if (getEncoding() == null) {
            if (!(stream instanceof JailedInputStream)) {
                stream = new JailedInputStream(stream);
            }
            stream.mark(BUFLEN);
            Recognizer<JDBCFormat> rec = getRecognizer();
            JDBCFormat format = rec.inspect(stream).getFormat();
            setEncoding(format.getEncoding());
            stream.reset();
            ((JailedInputStream) stream).release();
        }
        reader = new InputStreamReader(stream, getEncoding());
        parse();
    }

    /**
     * {@inheritDoc}
     * @see net.sf.ediknight.Parser#getRecognizer()
     */
    public Recognizer<JDBCFormat> getRecognizer() {
        if (recognizer == null) {
            recognizer = new JDBCRecognizer(this);
        }
        return recognizer;
    }

    /**
     * {@inheritDoc}
     * @see net.sf.ediknight.Parser#getXMLReader()
     */
    public XMLReader getXMLReader() {
        return new JDBCXMLReader(this);
    }

    /**
     * @throws IOException if an I/O error occurs while parsing
     */
    private void parse() throws IOException {
        if (handler == null) {
            handler = new DummyRecordHandler();
        }
        ch = reader.read();
        nextToken();
        while (tc != EOF) {
            if (tc == TABLENAME) {
                handler.nextTable(token.toString());
                nextToken();
                while (tc == EOL) {
                    nextToken();
                }
            }
            handler.nextRow();
            while (tc != EOF && tc != EOL) {
                if (tc == DATA) {
                    handler.nextValue(token.toString());
                }
                nextToken();
            }
            while (tc == EOL) {
                nextToken();
            }
        }
        handler.finish();
    }

    /**
     * Fetch the next token.
     *
     * @throws IOException if an I/O error occurs
     */
    private void nextToken() throws IOException {
        token.setLength(0);
        if (tc == EOF) {
            return;
        }
        if (ch == -1) {
            tc = EOF;
            return;
        }
        switch(ch) {
            case '\r':
                tc = EOL;
                ch = reader.read();
                if (ch == '\n') {
                    ch = reader.read();
                }
                return;
            case '\n':
                tc = EOL;
                ch = reader.read();
                return;
            case '|':
                tc = SEPARATOR;
                token.append((char) ch);
                ch = reader.read();
                return;
            case '[':
                tc = TABLENAME;
                ch = reader.read();
                while (ch != -1 && ch != ']') {
                    token.append((char) ch);
                    ch = reader.read();
                }
                ch = reader.read();
                return;
            case '"':
                tc = DATA;
                ch = reader.read();
                while (ch != -1 && ch != '"') {
                    token.append((char) ch);
                    ch = reader.read();
                }
                return;
            default:
                tc = DATA;
                token.append((char) ch);
                ch = reader.read();
                while (ch != -1 && ch != '|' && ch != '\r' && ch != '\n') {
                    token.append((char) ch);
                    ch = reader.read();
                }
        }
    }
}
