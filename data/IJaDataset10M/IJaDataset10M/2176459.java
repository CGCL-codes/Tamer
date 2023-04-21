package org.opensource.jdom.taggen.html.tag;

import org.opensource.jdom.taggen.html.CommonAttributes;

/**
 * Defines a link to an external resource. It is most commonly used to link a
 * CSS file to an HTML document.<br>
 * link must appear within the head element.
 *
 * <h2>Example</h2>
 * <pre><code class="html"><strong>&lt;link rel="stylesheet"
 * type="text/css" href="default.css" /&gt;</strong>
 * </code></pre>
 *
 * @author sergio.valdez
 */
public class Link extends CommonAttributes {

    /**
     * can be used to specify the target of a link. 
     */
    private String href;

    /**
     * can be used to specify the character set of the target of a link.
     */
    private String charset;

    /**
     * can be used to specify the language (in the form of a language code) of
     * the target of a link. It should only be used when href is also used.
     */
    private String hreflang;

    /**
     * can be used to specify the MIME type of the target of a link.
     */
    private String type;

    /**
     * can be used to specify the relationship of the target of the link to the
     * current page.
     */
    private String rel;

    /**
     * can be used to specify the relationship of the current page to the target
     * of the link.
     */
    private String rev;

    /**
     * can be used to specify which media the link is associated to. A value
     * such as screen, print, projection, braille, speech or all can be used or
     * a combination in a comma-separated list.
     */
    private String media;

    /**
     * can be used to specify the target of a link.
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * can be used to specify the target of a link.
     * @param href the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * can be used to specify the character set of the target of a link.
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * can be used to specify the character set of the target of a link.
     * @param charset the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * can be used to specify the language (in the form of a language code) of
     * the target of a link. It should only be used when href is also used.
     * @return the hreflang
     */
    public String getHreflang() {
        return hreflang;
    }

    /**
     * can be used to specify the language (in the form of a language code) of
     * the target of a link. It should only be used when href is also used.
     * @param hreflang the hreflang to set
     */
    public void setHreflang(String hreflang) {
        this.hreflang = hreflang;
    }

    /**
     * can be used to specify the MIME type of the target of a link.
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * can be used to specify the MIME type of the target of a link.
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * can be used to specify the relationship of the target of the link to the
     * current page.
     * @return the rel
     */
    public String getRel() {
        return rel;
    }

    /**
     * can be used to specify the relationship of the target of the link to the
     * current page.
     * @param rel the rel to set
     */
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
     * can be used to specify the relationship of the current page to the target
     * of the link.
     * @return the rev
     */
    public String getRev() {
        return rev;
    }

    /**
     * can be used to specify the relationship of the current page to the target
     * of the link.
     * @param rev the rev to set
     */
    public void setRev(String rev) {
        this.rev = rev;
    }

    /**
     * can be used to specify which media the link is associated to. A value
     * such as screen, print, projection, braille, speech or all can be used or
     * a combination in a comma-separated list.
     * @return the media
     */
    public String getMedia() {
        return media;
    }

    /**
     * can be used to specify which media the link is associated to. A value
     * such as screen, print, projection, braille, speech or all can be used or
     * a combination in a comma-separated list.
     * @param media the media to set
     */
    public void setMedia(String media) {
        this.media = media;
    }
}
