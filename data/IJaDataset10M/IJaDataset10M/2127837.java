package com.google.gwt.dom.client;

/**
 * For the H1 to H6 elements.
 * 
 * @see <a
 *      href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/global.html#edef-H1">W3C
 *      HTML Specification</a>
 */
@TagName({ HeadingElement.TAG_H1, HeadingElement.TAG_H2, HeadingElement.TAG_H3, HeadingElement.TAG_H4, HeadingElement.TAG_H5, HeadingElement.TAG_H6 })
public class HeadingElement extends Element {

    static final String[] TAGS = { HeadingElement.TAG_H1, HeadingElement.TAG_H2, HeadingElement.TAG_H3, HeadingElement.TAG_H4, HeadingElement.TAG_H5, HeadingElement.TAG_H6 };

    public static final String TAG_H1 = "h1";

    public static final String TAG_H2 = "h2";

    public static final String TAG_H3 = "h3";

    public static final String TAG_H4 = "h4";

    public static final String TAG_H5 = "h5";

    public static final String TAG_H6 = "h6";

    /**
   * Assert that the given {@link Element} is compatible with this class and
   * automatically typecast it.
   */
    public static HeadingElement as(Element elem) {
        if (HeadingElement.class.desiredAssertionStatus()) {
            String tag = elem.getTagName().toLowerCase();
            assert tag.length() == 2;
            assert tag.charAt(0) == 'h';
            int n = Integer.parseInt(tag.substring(1, 2));
            assert (n >= 1) && (n <= 6);
        }
        return (HeadingElement) elem;
    }

    protected HeadingElement() {
    }
}
