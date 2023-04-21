package com.sun.pdfview.font;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFShapeCmd;

/**
 * A single glyph in a stream of PDF text, which knows how to write itself
 * onto a PDF command stream
 */
public class PDFGlyph {

    /** the character code of this glyph */
    private char src;

    /** the name of this glyph */
    private String name;

    /** the advance from this glyph */
    private Point2D advance;

    /** the shape represented by this glyph (for all fonts but type 3) */
    private GeneralPath shape;

    /** the PDFPage storing this glyph's commands (for type 3 fonts) */
    private PDFPage page;

    /** Creates a new instance of PDFGlyph based on a shape */
    public PDFGlyph(char src, String name, GeneralPath shape, Point2D.Float advance) {
        this.shape = shape;
        this.advance = advance;
        this.src = src;
        this.name = name;
    }

    /** Creates a new instance of PDFGlyph based on a page */
    public PDFGlyph(char src, String name, PDFPage page, Point2D advance) {
        this.page = page;
        this.advance = advance;
        this.src = src;
        this.name = name;
    }

    /** Get the character code of this glyph */
    public char getChar() {
        return src;
    }

    /** Get the name of this glyph */
    public String getName() {
        return name;
    }

    /** Get the shape of this glyph */
    public GeneralPath getShape() {
        return shape;
    }

    /** Get the PDFPage for a type3 font glyph */
    public PDFPage getPage() {
        return page;
    }

    /** Add commands for this glyph to a page */
    public Point2D addCommands(PDFPage cmds, AffineTransform transform, int mode) {
        if (shape != null) {
            GeneralPath outline = (GeneralPath) shape.createTransformedShape(transform);
            cmds.addCommand(new PDFShapeCmd(outline, mode));
        } else if (page != null) {
            cmds.addCommands(page, transform);
        }
        return advance;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(name);
        return str.toString();
    }
}
