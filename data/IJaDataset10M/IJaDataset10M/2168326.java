package com.lowagie.text;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;
import com.lowagie.text.pdf.OutputStreamCounter;

/**
 * An abstract <CODE>Writer</CODE> class for documents.
 * <P>
 * <CODE>DocWriter</CODE> is the abstract class of several writers such
 * as <CODE>PdfWriter</CODE> and <CODE>HtmlWriter</CODE>.
 * A <CODE>DocWriter</CODE> can be added as a <CODE>DocListener</CODE>
 * to a certain <CODE>Document</CODE> by getting an instance (see method
 * <CODE>getInstance()</CODE> in the specific writer-classes).
 * Every <CODE>Element</CODE> added to the original <CODE>Document</CODE>
 * will be written to the <CODE>OutputStream</CODE> of the listening
 * <CODE>DocWriter</CODE>.
 *
 * @see   Document
 * @see   DocListener
 */
public abstract class DocWriter implements DocListener {

    /** This is some byte that is often used. */
    public static final byte NEWLINE = (byte) '\n';

    /** This is some byte that is often used. */
    public static final byte TAB = (byte) '\t';

    /** This is some byte that is often used. */
    public static final byte LT = (byte) '<';

    /** This is some byte that is often used. */
    public static final byte SPACE = (byte) ' ';

    /** This is some byte that is often used. */
    public static final byte EQUALS = (byte) '=';

    /** This is some byte that is often used. */
    public static final byte QUOTE = (byte) '\"';

    /** This is some byte that is often used. */
    public static final byte GT = (byte) '>';

    /** This is some byte that is often used. */
    public static final byte FORWARD = (byte) '/';

    /** The pageSize. */
    protected Rectangle pageSize;

    /** This is the document that has to be written. */
    protected Document document;

    /** The outputstream of this writer. */
    protected OutputStreamCounter os;

    /** Is the writer open for writing? */
    protected boolean open = false;

    /** Do we have to pause all writing actions? */
    protected boolean pause = false;

    /** Closes the stream on document close */
    protected boolean closeStream = true;

    protected DocWriter() {
    }

    /**
 * Constructs a <CODE>DocWriter</CODE>.
 *
 * @param document  The <CODE>Document</CODE> that has to be written
 * @param os  The <CODE>OutputStream</CODE> the writer has to write to.
 */
    protected DocWriter(Document document, OutputStream os) {
        this.document = document;
        this.os = new OutputStreamCounter(new BufferedOutputStream(os));
    }

    /**
 * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class.
 * 
 * @param element A high level object to add
 * @return  <CODE>false</CODE>
 * @throws  DocumentException when a document isn't open yet, or has been closed
 */
    public boolean add(Element element) throws DocumentException {
        return false;
    }

    /**
 * Signals that the <CODE>Document</CODE> was opened.
 */
    public void open() {
        open = true;
    }

    /**
 * Sets the pagesize.
 *
 * @param pageSize  the new pagesize
 * @return  a <CODE>boolean</CODE>
 */
    public boolean setPageSize(Rectangle pageSize) {
        this.pageSize = pageSize;
        return true;
    }

    /**
 * Sets the margins.
 * <P>
 * This does nothing. Has to be overridden if needed.
 *
 * @param marginLeft    the margin on the left
 * @param marginRight   the margin on the right
 * @param marginTop   the margin on the top
 * @param marginBottom  the margin on the bottom
 * @return  <CODE>false</CODE>
 */
    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        return false;
    }

    /**
 * Signals that an new page has to be started.
 * <P>
 * This does nothing. Has to be overridden if needed.
 *
 * @return  <CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
 */
    public boolean newPage() {
        if (!open) {
            return false;
        }
        return true;
    }

    /**
 * Changes the header of this document.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class if they actually support the use of
 * headers.
 *
 * @param header    the new header
 */
    public void setHeader(HeaderFooter header) {
    }

    /**
 * Resets the header of this document.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class if they actually support the use of
 * headers.
 */
    public void resetHeader() {
    }

    /**
 * Changes the footer of this document.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class if they actually support the use of
 * footers.
 *
 * @param footer    the new footer
 */
    public void setFooter(HeaderFooter footer) {
    }

    /**
 * Resets the footer of this document.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class if they actually support the use of
 * footers.
 */
    public void resetFooter() {
    }

    /**
 * Sets the page number to 0.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class if they actually support the use of
 * pagenumbers.
 */
    public void resetPageCount() {
    }

    /**
 * Sets the page number.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class if they actually support the use of
 * pagenumbers.
 *
 * @param pageN   the new page number
 */
    public void setPageCount(int pageN) {
    }

    /**
 * Signals that the <CODE>Document</CODE> was closed and that no other
 * <CODE>Elements</CODE> will be added.
 */
    public void close() {
        open = false;
        try {
            os.flush();
            if (closeStream) os.close();
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    /** Converts a <CODE>String</CODE> into a <CODE>Byte</CODE> array
 * according to the ISO-8859-1 codepage.
 * @param text the text to be converted
 * @return the conversion result
 */
    public static final byte[] getISOBytes(String text) {
        if (text == null) return null;
        int len = text.length();
        byte b[] = new byte[len];
        for (int k = 0; k < len; ++k) b[k] = (byte) text.charAt(k);
        return b;
    }

    /**
 * Let the writer know that all writing has to be paused.
 */
    public void pause() {
        pause = true;
    }

    /**
     * Checks if writing is paused.
     *
     * @return		<CODE>true</CODE> if writing temporarily has to be paused, <CODE>false</CODE> otherwise.
     */
    public boolean isPaused() {
        return pause;
    }

    /**
 * Let the writer know that writing may be resumed.
 */
    public void resume() {
        pause = false;
    }

    /**
 * Flushes the <CODE>BufferedOutputStream</CODE>.
 */
    public void flush() {
        try {
            os.flush();
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    /**
 * Writes a <CODE>String</CODE> to the <CODE>OutputStream</CODE>.
 *
 * @param string    the <CODE>String</CODE> to write
 * @throws IOException
 */
    protected void write(String string) throws IOException {
        os.write(getISOBytes(string));
    }

    /**
 * Writes a number of tabs.
 *
 * @param   indent  the number of tabs to add
 * @throws IOException
 */
    protected void addTabs(int indent) throws IOException {
        os.write(NEWLINE);
        for (int i = 0; i < indent; i++) {
            os.write(TAB);
        }
    }

    /**
 * Writes a key-value pair to the outputstream.
 *
 * @param   key     the name of an attribute
 * @param   value   the value of an attribute
 * @throws IOException
 */
    protected void write(String key, String value) throws IOException {
        os.write(SPACE);
        write(key);
        os.write(EQUALS);
        os.write(QUOTE);
        write(value);
        os.write(QUOTE);
    }

    /**
 * Writes a starttag to the outputstream.
 *
 * @param   tag     the name of the tag
 * @throws IOException
 */
    protected void writeStart(String tag) throws IOException {
        os.write(LT);
        write(tag);
    }

    /**
 * Writes an endtag to the outputstream.
 *
 * @param   tag     the name of the tag
 * @throws IOException
 */
    protected void writeEnd(String tag) throws IOException {
        os.write(LT);
        os.write(FORWARD);
        write(tag);
        os.write(GT);
    }

    /**
 * Writes an endtag to the outputstream.
 * @throws IOException
 */
    protected void writeEnd() throws IOException {
        os.write(SPACE);
        os.write(FORWARD);
        os.write(GT);
    }

    /**
 * Writes the markup attributes of the specified <CODE>MarkupAttributes</CODE>
 * object to the <CODE>OutputStream</CODE>.
 * @param markup   a <CODE>Properties</CODE> collection to write.
 * @return true, if writing the markup attributes succeeded
 * @throws IOException
 */
    protected boolean writeMarkupAttributes(Properties markup) throws IOException {
        if (markup == null) return false;
        Iterator attributeIterator = markup.keySet().iterator();
        String name;
        while (attributeIterator.hasNext()) {
            name = String.valueOf(attributeIterator.next());
            write(name, markup.getProperty(name));
        }
        markup.clear();
        return true;
    }

    /** Checks if the stream is to be closed on document close
     * @return true if the stream is closed on document close
     *
     */
    public boolean isCloseStream() {
        return closeStream;
    }

    /** Sets the close state of the stream after document close
     * @param closeStream true if the stream is closed on document close
     *
     */
    public void setCloseStream(boolean closeStream) {
        this.closeStream = closeStream;
    }

    /**
     * @see com.lowagie.text.DocListener#setMarginMirroring(boolean)
     */
    public boolean setMarginMirroring(boolean MarginMirroring) {
        return false;
    }

    /**
     * @see com.lowagie.text.DocListener#setMarginMirroring(boolean)
     * @since	2.1.6
     */
    public boolean setMarginMirroringTopBottom(boolean MarginMirroring) {
        return false;
    }
}
