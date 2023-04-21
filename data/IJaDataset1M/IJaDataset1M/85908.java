package org.databene.dbsanity.html;

import java.util.List;
import org.databene.commons.CollectionUtil;

/**
 * Represents an HTML table row.<br/><br/>
 * Created: 02.12.2011 19:12:54
 * @since 0.9.3
 * @author Volker Bergmann
 */
public class HtmlTableRow extends HtmlElement<HtmlTableRow> {

    private List<HtmlTableCell> cells;

    public HtmlTableRow(HtmlTableCell... cells) {
        super("tr", false);
        withCells(cells);
    }

    public HtmlTableRow withCells(HtmlTableCell... cells) {
        this.cells = CollectionUtil.toList(cells);
        return this;
    }

    public HtmlTableRow topAligned() {
        this.attributes.put("valign", "top");
        return this;
    }

    public void render(HtmlPrintWriter out) {
        renderStartTagWithAttributes(out);
        for (HtmlTableCell cell : cells) cell.render(out);
        renderEndTag(out);
    }
}
