package com.lowagie.text.rtf;

import java.io.IOException;
import java.io.OutputStream;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Font;
import com.lowagie.text.rtf.document.RtfDocument;

/**
 * The RtfAddableElement is the superclass for all rtf specific elements
 * that need to be added to an iText document. It is an extension of Chunk
 * and it also implements RtfBasicElement. It is an abstract class thus it
 * cannot be instantiated itself and has to be subclassed to be used.
 * 
 * @version $Id:RtfAddableElement.java 3126 2008-02-07 20:30:46Z hallm $
 * @author Mark Hall (Mark.Hall@mail.room3b.eu)
 * @author Thomas Bickel (tmb99@inode.at)
 */
public abstract class RtfAddableElement extends Chunk implements RtfBasicElement {

    /**
	 * The RtfDocument this RtfAddableElement belongs to.
	 */
    protected RtfDocument doc = null;

    /**
	 * Whether this RtfAddableElement is contained in a table.
	 */
    protected boolean inTable = false;

    /**
	 * Whether this RtfAddableElement is contained in a header.
	 */
    protected boolean inHeader = false;

    /**
	 * Constructs a new RtfAddableElement. The Chunk content is
	 * set to an empty string and the font to the default Font().
	 */
    public RtfAddableElement() {
        super("", new Font());
    }

    /**
     * Writes the element content to the given output stream.
	 */
    public abstract void writeContent(OutputStream out) throws IOException;

    /**
	 * Sets the RtfDocument this RtfAddableElement belongs to.
	 */
    public void setRtfDocument(RtfDocument doc) {
        this.doc = doc;
    }

    /**
	 * Sets whether this RtfAddableElement is contained in a table.
	 */
    public void setInTable(boolean inTable) {
        this.inTable = inTable;
    }

    /**
	 * Sets whether this RtfAddableElement is contained in a header/footer.
	 */
    public void setInHeader(boolean inHeader) {
        this.inHeader = inHeader;
    }

    /**
     * Transforms an integer into its String representation and then returns the bytes
     * of that string.
     *
     * @param i The integer to convert
     * @return A byte array representing the integer
     */
    public byte[] intToByteArray(int i) {
        return DocWriter.getISOBytes(Integer.toString(i));
    }

    /**
     *  RtfAddableElement subclasses are never assumed to be empty.
     */
    public boolean isEmpty() {
        return false;
    }
}
