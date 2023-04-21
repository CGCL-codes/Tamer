package com.google.gwt.dom.builder.shared;

/**
 * Builds an frame element.
 */
public interface FrameBuilder extends ElementBuilderBase<FrameBuilder> {

    /**
   * Request frame borders.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-frameborder">W3C
   *      HTML Specification</a>
   */
    FrameBuilder frameBorder(int frameBorder);

    /**
   * URI designating a long description of this image or frame.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-longdesc-FRAME">W3C
   *      HTML Specification</a>
   */
    FrameBuilder longDesc(String longDesc);

    /**
   * Frame margin height, in pixels.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-marginheight">W3C
   *      HTML Specification</a>
   */
    FrameBuilder marginHeight(int marginHeight);

    /**
   * Frame margin width, in pixels.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-marginwidth">W3C
   *      HTML Specification</a>
   */
    FrameBuilder marginWidth(int marginWidth);

    /**
   * The frame name (object of the target attribute).
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-name-FRAME">W3C
   *      HTML Specification</a>
   */
    FrameBuilder name(String name);

    /**
   * Forbid user from resizing frame.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-noresize">W3C
   *      HTML Specification</a>
   */
    FrameBuilder noResize();

    /**
   * Specify whether or not the frame should have scrollbars.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-scrolling">W3C
   *      HTML Specification</a>
   */
    FrameBuilder scrolling(String scrolling);

    /**
   * A URI designating the initial frame contents.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-src-FRAME">W3C
   *      HTML Specification</a>
   */
    FrameBuilder src(String src);
}
