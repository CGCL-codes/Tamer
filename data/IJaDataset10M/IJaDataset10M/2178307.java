package com.google.gwt.dom.client;

/**
 * Embedded image.
 * 
 * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#edef-IMG">W3C HTML Specification</a>
 */
@TagName(ImageElement.TAG)
public class ImageElement extends Element {

    public static final String TAG = "img";

    /**
   * Assert that the given {@link Element} is compatible with this class and
   * automatically typecast it.
   */
    public static ImageElement as(Element elem) {
        assert elem.getTagName().equalsIgnoreCase(TAG);
        return (ImageElement) elem;
    }

    protected ImageElement() {
    }

    /**
   * Alternate text for user agents not rendering the normal content of this
   * element.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-alt">W3C HTML Specification</a>
   */
    public final native String getAlt();

    /**
   * Height of the image in pixels.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-height-IMG">W3C HTML Specification</a>
   */
    public final native int getHeight();

    /**
   * URI designating the source of this image.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-src-IMG">W3C HTML Specification</a>
   */
    public final String getSrc() {
        return DOMImpl.impl.imgGetSrc(this);
    }

    /**
   * The width of the image in pixels.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-width-IMG">W3C HTML Specification</a>
   */
    public final native int getWidth();

    /**
   * Use server-side image map.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-ismap">W3C HTML Specification</a>
   */
    public final native boolean isMap();

    /**
   * Alternate text for user agents not rendering the normal content of this
   * element.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-alt">W3C HTML Specification</a>
   */
    public final native void setAlt(String alt);

    /**
   * Height of the image in pixels.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-height-IMG">W3C HTML Specification</a>
   */
    public final native void setHeight(int height);

    /**
   * Use server-side image map.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-ismap">W3C HTML Specification</a>
   */
    public final native void setIsMap(boolean isMap);

    /**
   * <p>
   * URI designating the source of this image.
   * </p>
   * <p>
   * If you {@link #cloneNode(boolean)} an {@link ImageElement}, or an element
   * that contains an {@link ImageElement}, then you must call
   * {@link #setSrc(String)} on the cloned element to ensure it is loaded
   * properly in IE6. Failure to do so may cause performance problems, or your
   * image may not load due to an IE6 specific workaround.
   * </p>
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-src-IMG">W3C HTML Specification</a>
   */
    public final void setSrc(String src) {
        DOMImpl.impl.imgSetSrc(this, src);
    }

    /**
   * Use client-side image map.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-usemap">W3C HTML Specification</a>
   */
    public final native void setUseMap(boolean useMap);

    /**
   * The width of the image in pixels.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-width-IMG">W3C HTML Specification</a>
   */
    public final native void setWidth(int width);

    /**
   * Use client-side image map.
   * 
   * @see <a href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-usemap">W3C HTML Specification</a>
   */
    public final native boolean useMap();
}
