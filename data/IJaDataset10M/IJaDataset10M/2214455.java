package com.lowagie.rups.view.itext.treenodes;

import com.lowagie.text.pdf.PdfDictionary;

/**
 * A special treenode that will be used for the trailer dictionary
 * of a PDF file.
 */
public class PdfTrailerTreeNode extends PdfObjectTreeNode {

    /**
     * Constructs a simple text tree node.
     */
    public PdfTrailerTreeNode() {
        super("pdf.png", null);
        setUserObject("Open a PDF file");
    }

    /**
	 * Sets the object for this node.
	 * @param trailer	the trailer dictionary of a PDF file.
	 */
    public void setTrailer(PdfDictionary trailer) {
        object = trailer;
    }

    /** A serial version id. */
    private static final long serialVersionUID = -3607980103983635182L;
}
