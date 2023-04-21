package com.lowagie.text.html;

import java.util.HashMap;
import com.lowagie.text.ElementTags;
import com.lowagie.text.FontFactory;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 */
public class HtmlTagMap extends HashMap {

    private static final long serialVersionUID = 5287430058473705350L;

    /**
	 * Constructs an HtmlTagMap.
	 */
    public HtmlTagMap() {
        super();
        HtmlPeer peer;
        peer = new HtmlPeer(ElementTags.ITEXT, HtmlTags.HTML);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PHRASE, HtmlTags.SPAN);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.CHUNK, HtmlTags.CHUNK);
        peer.addAlias(ElementTags.FONT, HtmlTags.FONT);
        peer.addAlias(ElementTags.SIZE, HtmlTags.SIZE);
        peer.addAlias(ElementTags.COLOR, HtmlTags.COLOR);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.ANCHOR, HtmlTags.ANCHOR);
        peer.addAlias(ElementTags.NAME, HtmlTags.NAME);
        peer.addAlias(ElementTags.REFERENCE, HtmlTags.REFERENCE);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PARAGRAPH, HtmlTags.PARAGRAPH);
        peer.addAlias(ElementTags.ALIGN, HtmlTags.ALIGN);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PARAGRAPH, HtmlTags.DIV);
        peer.addAlias(ElementTags.ALIGN, HtmlTags.ALIGN);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PARAGRAPH, HtmlTags.H[0]);
        peer.addValue(ElementTags.SIZE, "20");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PARAGRAPH, HtmlTags.H[1]);
        peer.addValue(ElementTags.SIZE, "18");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PARAGRAPH, HtmlTags.H[2]);
        peer.addValue(ElementTags.SIZE, "16");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PARAGRAPH, HtmlTags.H[3]);
        peer.addValue(ElementTags.SIZE, "14");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PARAGRAPH, HtmlTags.H[4]);
        peer.addValue(ElementTags.SIZE, "12");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PARAGRAPH, HtmlTags.H[5]);
        peer.addValue(ElementTags.SIZE, "10");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.LIST, HtmlTags.ORDEREDLIST);
        peer.addValue(ElementTags.NUMBERED, "true");
        peer.addValue(ElementTags.SYMBOLINDENT, "20");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.LIST, HtmlTags.UNORDEREDLIST);
        peer.addValue(ElementTags.NUMBERED, "false");
        peer.addValue(ElementTags.SYMBOLINDENT, "20");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.LISTITEM, HtmlTags.LISTITEM);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PHRASE, HtmlTags.I);
        peer.addValue(ElementTags.STYLE, Markup.CSS_VALUE_ITALIC);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PHRASE, HtmlTags.EM);
        peer.addValue(ElementTags.STYLE, Markup.CSS_VALUE_ITALIC);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PHRASE, HtmlTags.B);
        peer.addValue(ElementTags.STYLE, Markup.CSS_VALUE_BOLD);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PHRASE, HtmlTags.STRONG);
        peer.addValue(ElementTags.STYLE, Markup.CSS_VALUE_BOLD);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PHRASE, HtmlTags.S);
        peer.addValue(ElementTags.STYLE, Markup.CSS_VALUE_LINETHROUGH);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PHRASE, HtmlTags.CODE);
        peer.addValue(ElementTags.FONT, FontFactory.COURIER);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PHRASE, HtmlTags.VAR);
        peer.addValue(ElementTags.FONT, FontFactory.COURIER);
        peer.addValue(ElementTags.STYLE, Markup.CSS_VALUE_ITALIC);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.PHRASE, HtmlTags.U);
        peer.addValue(ElementTags.STYLE, Markup.CSS_VALUE_UNDERLINE);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.CHUNK, HtmlTags.SUP);
        peer.addValue(ElementTags.SUBSUPSCRIPT, "6.0");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.CHUNK, HtmlTags.SUB);
        peer.addValue(ElementTags.SUBSUPSCRIPT, "-6.0");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.HORIZONTALRULE, HtmlTags.HORIZONTALRULE);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.TABLE, HtmlTags.TABLE);
        peer.addAlias(ElementTags.WIDTH, HtmlTags.WIDTH);
        peer.addAlias(ElementTags.BACKGROUNDCOLOR, HtmlTags.BACKGROUNDCOLOR);
        peer.addAlias(ElementTags.BORDERCOLOR, HtmlTags.BORDERCOLOR);
        peer.addAlias(ElementTags.COLUMNS, HtmlTags.COLUMNS);
        peer.addAlias(ElementTags.CELLPADDING, HtmlTags.CELLPADDING);
        peer.addAlias(ElementTags.CELLSPACING, HtmlTags.CELLSPACING);
        peer.addAlias(ElementTags.BORDERWIDTH, HtmlTags.BORDERWIDTH);
        peer.addAlias(ElementTags.ALIGN, HtmlTags.ALIGN);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.ROW, HtmlTags.ROW);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.CELL, HtmlTags.CELL);
        peer.addAlias(ElementTags.WIDTH, HtmlTags.WIDTH);
        peer.addAlias(ElementTags.BACKGROUNDCOLOR, HtmlTags.BACKGROUNDCOLOR);
        peer.addAlias(ElementTags.BORDERCOLOR, HtmlTags.BORDERCOLOR);
        peer.addAlias(ElementTags.COLSPAN, HtmlTags.COLSPAN);
        peer.addAlias(ElementTags.ROWSPAN, HtmlTags.ROWSPAN);
        peer.addAlias(ElementTags.NOWRAP, HtmlTags.NOWRAP);
        peer.addAlias(ElementTags.HORIZONTALALIGN, HtmlTags.HORIZONTALALIGN);
        peer.addAlias(ElementTags.VERTICALALIGN, HtmlTags.VERTICALALIGN);
        peer.addValue(ElementTags.HEADER, "false");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.CELL, HtmlTags.HEADERCELL);
        peer.addAlias(ElementTags.WIDTH, HtmlTags.WIDTH);
        peer.addAlias(ElementTags.BACKGROUNDCOLOR, HtmlTags.BACKGROUNDCOLOR);
        peer.addAlias(ElementTags.BORDERCOLOR, HtmlTags.BORDERCOLOR);
        peer.addAlias(ElementTags.COLSPAN, HtmlTags.COLSPAN);
        peer.addAlias(ElementTags.ROWSPAN, HtmlTags.ROWSPAN);
        peer.addAlias(ElementTags.NOWRAP, HtmlTags.NOWRAP);
        peer.addAlias(ElementTags.HORIZONTALALIGN, HtmlTags.HORIZONTALALIGN);
        peer.addAlias(ElementTags.VERTICALALIGN, HtmlTags.VERTICALALIGN);
        peer.addValue(ElementTags.HEADER, "true");
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.IMAGE, HtmlTags.IMAGE);
        peer.addAlias(ElementTags.URL, ElementTags.SRC);
        peer.addAlias(ElementTags.ALT, HtmlTags.ALT);
        peer.addAlias(ElementTags.PLAINWIDTH, HtmlTags.PLAINWIDTH);
        peer.addAlias(ElementTags.PLAINHEIGHT, HtmlTags.PLAINHEIGHT);
        put(peer.getAlias(), peer);
        peer = new HtmlPeer(ElementTags.NEWLINE, HtmlTags.NEWLINE);
        put(peer.getAlias(), peer);
    }

    /**
	 * Checks if this is the root tag.
	 * @param tag a tagvalue
	 * @return true if tag is HTML or html
	 */
    public static boolean isHtml(String tag) {
        return HtmlTags.HTML.equalsIgnoreCase(tag);
    }

    /**
	 * Checks if this is the head tag.
	 * @param tag a tagvalue
	 * @return true if tag is HEAD or head
	 */
    public static boolean isHead(String tag) {
        return HtmlTags.HEAD.equalsIgnoreCase(tag);
    }

    /**
	 * Checks if this is the meta tag.
	 * @param tag a tagvalue
	 * @return true if tag is META or meta
	 */
    public static boolean isMeta(String tag) {
        return HtmlTags.META.equalsIgnoreCase(tag);
    }

    /**
	 * Checks if this is the link tag.
	 * @param tag a tagvalue
	 * @return true if tag is LINK or link
	 */
    public static boolean isLink(String tag) {
        return HtmlTags.LINK.equalsIgnoreCase(tag);
    }

    /**
	 * Checks if this is the title tag.
	 * @param tag a tagvalue
	 * @return true if tag is TITLE or title
	 */
    public static boolean isTitle(String tag) {
        return HtmlTags.TITLE.equalsIgnoreCase(tag);
    }

    /**
	 * Checks if this is the root tag.
	 * @param tag a tagvalue
	 * @return true if tag is BODY or body
	 */
    public static boolean isBody(String tag) {
        return HtmlTags.BODY.equalsIgnoreCase(tag);
    }

    /**
	 * Checks if this is a special tag.
	 * @param tag a tagvalue
	 * @return true if tag is a HTML, HEAD, META, LINK or BODY tag (case insensitive)
	 */
    public static boolean isSpecialTag(String tag) {
        return isHtml(tag) || isHead(tag) || isMeta(tag) || isLink(tag) || isBody(tag);
    }
}
