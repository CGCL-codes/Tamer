package com.google.gwt.dom.builder.shared;

import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Builds an iframe element.
 */
public interface IFrameBuilder extends ElementBuilderBase<IFrameBuilder> {

    /**
   * Request frame borders.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-frameborder">W3C
   *      HTML Specification</a>
   */
    IFrameBuilder frameBorder(int frameBorder);

    /**
   * Throws {@link UnsupportedOperationException}.
   * 
   * <p>
   * Appending children or content directly to an iframe isn't supported. You
   * must use the src attribute to specify the url of the content to load, or
   * wait until the document is loaded.
   * </p>
   * 
   * @throws UnsupportedOperationException
   */
    @Override
    IFrameBuilder html(SafeHtml html);

    /**
   * Frame margin height, in pixels.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-marginheight">W3C
   *      HTML Specification</a>
   */
    IFrameBuilder marginHeight(int marginHeight);

    /**
   * Frame margin width, in pixels.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-marginwidth">W3C
   *      HTML Specification</a>
   */
    IFrameBuilder marginWidth(int marginWidth);

    /**
   * The frame name (object of the target attribute).
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-name-FRAME">W3C
   *      HTML Specification</a>
   */
    IFrameBuilder name(String name);

    /**
   * Forbid user from resizing frame.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-noresize">W3C
   *      HTML Specification</a>
   */
    IFrameBuilder noResize();

    /**
   * Specify whether or not the frame should have scrollbars.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-scrolling">W3C
   *      HTML Specification</a>
   */
    IFrameBuilder scrolling(String scrolling);

    /**
   * A URI designating the initial frame contents.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-src-FRAME">W3C
   *      HTML Specification</a>
   */
    IFrameBuilder src(String src);

    /**
   * Throws {@link UnsupportedOperationException}.
   * 
   * <p>
   * Appending children or content directly to an iframe isn't supported. You
   * must use the src attribute to specify the url of the content to load, or
   * wait until the document is loaded.
   * </p>
   * 
   * @throws UnsupportedOperationException
   */
    @Override
    IFrameBuilder text(String html);
}
