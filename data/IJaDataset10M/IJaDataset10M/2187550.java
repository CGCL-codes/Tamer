package com.google.gwt.dom.client;

/**
 * This contains generic meta-information about the document.
 * 
 * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/global.html#edef-META">W3C HTML Specification</a>
 */
@TagName(MetaElement.TAG)
public class MetaElement extends Element {

    public static final String TAG = "meta";

    /**
   * Assert that the given {@link Element} is compatible with this class and
   * automatically typecast it.
   */
    public static MetaElement as(Element elem) {
        assert elem.getTagName().equalsIgnoreCase(TAG);
        return (MetaElement) elem;
    }

    protected MetaElement() {
    }

    /**
   * Associated information.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/global.html#adef-content">W3C HTML Specification</a>
   */
    public final native String getContent();

    /**
   * HTTP response header name [IETF RFC 2616].
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/global.html#adef-http-equiv">W3C HTML Specification</a>
   */
    public final native String getHttpEquiv();

    /**
   * Meta information name.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/global.html#adef-name-META">W3C HTML Specification</a>
   */
    public final native String getName();

    /**
   * Associated information.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/global.html#adef-content">W3C HTML Specification</a>
   */
    public final native void setContent(String content);

    /**
   * HTTP response header name [IETF RFC 2616].
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/global.html#adef-http-equiv">W3C HTML Specification</a>
   */
    public final native void setHttpEquiv(String httpEquiv);

    /**
   * Meta information name.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/global.html#adef-name-META">W3C HTML Specification</a>
   */
    public final native void setName(String name);
}
