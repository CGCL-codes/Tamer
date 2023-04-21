package jsesh.mdc.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.constants.Dialect;
import jsesh.mdc.constants.JSeshInfoConstants;
import jsesh.mdc.jseshInfo.JSeshInfoReader;
import jsesh.utils.ByteArraysUtils;
import jsesh.utils.StringBufferUtils;
import jsesh.utils.SystemUtils;

public class MDCDocumentReader {

    private String defaultEncoding = null;

    MDCDocument document;

    /**
	 * Open a file in a given defaultEncoding.
	 * 
	 * @param file
	 * @param defaultEncoding
	 * @throws IOException
	 * @throws MDCSyntaxError
	 */
    public MDCDocument loadFile(File file) throws IOException, MDCSyntaxError {
        byte[] bytes = ByteArraysUtils.readFileInByteArray(file);
        return extractDocumentFrom(bytes, file);
    }

    /**
	 * Reads a document from a stream. Associate it with a specific file...
	 * 
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws MDCSyntaxError
	 */
    public MDCDocument readStream(InputStream in, File file) throws IOException, MDCSyntaxError {
        byte[] bytes = ByteArraysUtils.readStreamInByteArray(in);
        return extractDocumentFrom(bytes, file);
    }

    /**
	 * Extract a document from an array of bytes.
	 * 
	 * @param bytes
	 *            an array of bytes containing the document
	 * @param file
	 *            the file the document should be saved to (possibly later).
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MDCSyntaxError
	 */
    private MDCDocument extractDocumentFrom(byte[] bytes, File file) throws UnsupportedEncodingException, MDCSyntaxError {
        document = new MDCDocument();
        document.setFile(file);
        document.setEncoding(defaultEncoding);
        int first = 0;
        first = guessEncodingAndDialect(file, bytes);
        StringBuffer buff;
        if (bytes.length == 0) buff = new StringBuffer(); else {
            buff = new StringBuffer(new String(bytes, first, bytes.length - first, document.getEncoding()));
        }
        readHeader(buff);
        StringReader r = new StringReader(buff.toString());
        document.getHieroglyphicTextModel().readTopItemList(r, document.getDialect());
        return document;
    }

    private void readHeader(StringBuffer buff) {
        int headerEnd = 0;
        if (Dialect.MACSCRIBE.equals(document.getDialect())) {
            while (StringBufferUtils.startsWith(buff, headerEnd, "++") || StringBufferUtils.startsWith(buff, headerEnd, "+O")) {
                headerEnd = StringBufferUtils.nextLineIndex(buff, headerEnd);
            }
        } else if (Dialect.WINGLYPH.equals(document.getDialect())) {
            if (buff.length() > 0 && buff.charAt(0) == '@') {
                headerEnd = StringBufferUtils.nextLineIndex(buff, 0);
            }
        } else if (Dialect.JSESH1.equals(document.getDialect())) {
            JSeshInfoReader infoReader = new JSeshInfoReader();
            infoReader.process(buff, document);
            headerEnd = infoReader.getNextPos();
        } else if (Dialect.JSESH.equals(document.getDialect())) {
        } else if (Dialect.TKSESH.equals(document.getDialect())) {
        } else {
        }
        if (headerEnd > 0) buff.delete(0, headerEnd);
    }

    /**
	 * Guess the encoding and dialect. Returns the "correct" first reading
	 * position to start reading the header.
	 * 
	 * @param bytes
	 * @return
	 */
    private int guessEncodingAndDialect(File f, byte[] bytes) {
        int firstByte = 0;
        String guessedEncoding = null;
        if (ByteArraysUtils.startsWith(bytes, firstByte, new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF })) {
            document.setEncoding("UTF-8");
            firstByte += 3;
        }
        if (f.getName().endsWith(".hie")) {
            document.setDialect(Dialect.TKSESH);
            guessedEncoding = "iso-8859-1";
        } else if (ByteArraysUtils.startsWith(bytes, firstByte, "@")) {
            guessedEncoding = "windows-1252";
            document.setDialect(Dialect.WINGLYPH);
        } else if (ByteArraysUtils.startsWith(bytes, firstByte, "++Created by: MacScribe")) {
            guessedEncoding = "MacRoman";
            document.setDialect(Dialect.MACSCRIBE);
        } else if (ByteArraysUtils.startsWith(bytes, firstByte, "++" + JSeshInfoConstants.JSESH_INFO)) {
            guessedEncoding = "UTF-8";
            document.setDialect(Dialect.JSESH1);
        } else {
            document.setDialect(Dialect.JSESH);
        }
        if (guessedEncoding == null) guessedEncoding = SystemUtils.getDefaultEncoding();
        if (document.getEncoding() == null) document.setEncoding(guessedEncoding);
        return firstByte;
    }

    /**
	 * Sets the defaultEncoding. If null, the defaultEncoding will be "guessed".
	 * 
	 * @param defaultEncoding
	 */
    public void setEncoding(String encoding) {
        this.defaultEncoding = encoding;
    }

    public String getEncoding() {
        return defaultEncoding;
    }

    /**
	 * Create a new Document from a string containing standard JSesh text.
	 * 
	 * @param mdc
	 * @param file
	 *            the file for saving the document
	 * @return the document.
	 * @throws MDCSyntaxError
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
    public MDCDocument readString(String mdc, File file) throws MDCSyntaxError {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter w = new OutputStreamWriter(out, "UTF-8");
            w.write(mdc);
            w.close();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            return readStream(in, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
