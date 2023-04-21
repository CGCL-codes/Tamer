package com.dalonedrau.bus;

/**
 * An implementation of a quote that can display itself as an html string.
 * the quote display follows the pattern below:<br><br>
 * Lorem ipso lorem factum lorem<br>
 *                      - author
 * @author DaLoneDrau
 */
public class Quote {

    /** the quote. */
    private String quote;

    /** the author. */
    private String author;

    /**
     * Creates a new instance of <code>Quote</code>.
     * @param q the actual quote
     * @param a the author
     */
    public Quote(final String q, final String a) {
        quote = q;
        author = a;
    }

    /**
     * Gets the <code>Quote</code>'s author.
     * @return <code>String</code>
     */
    public final String getAuthor() {
        return author;
    }

    /**
     * Gets the quote portion of the <code>Quote</code>.
     * @return <code>String</code>
     */
    public final String getQuote() {
        return quote;
    }

    /**
     * Gets the <code>Quote</code>'s html markup string.
     * @return <code>String</code>
     */
    public final String getHTMLMarkup() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<div style=\"text-align:left; font-style:italic;\">");
        buffer.append(quote);
        buffer.append("</div>");
        buffer.append("<div style=\"text-align:right;\">");
        buffer.append("-&nbsp;");
        buffer.append(author);
        buffer.append("</div>");
        return buffer.toString();
    }
}
