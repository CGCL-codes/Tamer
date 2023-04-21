package org.ponder.groovy.builders.pdf.itext;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfTemplate;

/**
 * This will take PDF that was read (maybe using the {@link Document#load(java.io.InputStream)} and is encapsulated
 * in a {@link PdfReader}, and will add a particular page of the PDF document to the current document. 
 * 
 * @author elponderador
 *
 */
public class Template extends ItextContextObject {

    private PdfTemplate template;

    private PdfReader src;

    private int page = 1;

    private float x;

    private float y;

    protected float scaleX = 1;

    protected float scaleY = 1;

    protected float rotation = 0;

    public Template(ItextParentObject parent) {
        super(parent);
    }

    @Override
    public ItextContext createContext(ItextContext parentContext) {
        Rectangle ps = src.getPageSize(page);
        ItextContext ctx = new ItextContext(parentContext, y, x, ps.getHeight(), ps.getWidth());
        return ctx;
    }

    @Override
    public void apply(ItextContext ctx) {
        if (this.src == null) throw new RuntimeException("No source provided for the template");
        this.template = ctx.createTemplate(src, page);
        Rectangle ps = src.getPageSize(page);
        ctx.applyTemplate(template, x, y, rotation, scaleX, scaleY);
        super.apply(ctx);
    }

    /**
	 * @return The left coordinate where the imported page should be 
	 */
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    /**
	 * @return The top coordinate where the imported page should be
	 */
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
	 * @return The page number of the document you want to import/use as a template
	 */
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
	 * @return The actual source document the imported page will be loaded from
	 */
    public PdfReader getSrc() {
        return src;
    }

    public void setSrc(PdfReader src) {
        this.src = src;
    }
}
