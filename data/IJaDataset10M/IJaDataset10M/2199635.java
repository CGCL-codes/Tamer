package com.google.gwt.dom.builder.shared;

/**
 * Builds an area element.
 */
public interface AreaBuilder extends ElementBuilderBase<AreaBuilder> {

    /**
   * A single character access key to give access to the form control.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-accesskey">W3C
   *      HTML Specification</a>
   */
    AreaBuilder accessKey(String accessKey);

    /**
   * Alternate text for user agents not rendering the normal content of this
   * element.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-alt">W3C
   *      HTML Specification</a>
   */
    AreaBuilder alt(String alt);

    /**
   * Comma-separated list of lengths, defining an active region geometry. See
   * also shape for the shape of the region.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-coords">W3C
   *      HTML Specification</a>
   */
    AreaBuilder coords(String coords);

    /**
   * The URI of the linked resource.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/links.html#adef-href">W3C
   *      HTML Specification</a>
   */
    AreaBuilder href(String href);

    /**
   * The shape of the active area. The coordinates are given by coords.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/objects.html#adef-shape">W3C
   *      HTML Specification</a>
   */
    AreaBuilder shape(String shape);

    /**
   * Frame to render the resource in.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-target">W3C
   *      HTML Specification</a>
   */
    AreaBuilder target(String target);
}
