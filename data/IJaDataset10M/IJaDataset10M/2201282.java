package net.sourceforge.gosp.jsesh.glyph;

import net.sourceforge.gosp.jsesh.geom.Dimension;

public class Glyph {

    protected String code;

    protected Dimension size = new Dimension();

    protected GlyphShape shape;

    public Glyph() {
    }

    public GlyphShape getShape() {
        return shape;
    }

    public void setShape(GlyphShape shape) {
        this.shape = shape;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }
}
