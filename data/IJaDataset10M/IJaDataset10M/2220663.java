package com.google.gwt.dom.builder.shared;

/**
 * Builds an frameset element.
 */
public interface FrameSetBuilder extends ElementBuilderBase<FrameSetBuilder> {

    /**
   * The number of columns of frames in the frameset.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-cols-FRAMESET">W3C
   *      HTML Specification</a>
   */
    FrameSetBuilder cols(String cols);

    /**
   * The number of rows of frames in the frameset.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/present/frames.html#adef-rows-FRAMESET">W3C
   *      HTML Specification</a>
   */
    FrameSetBuilder rows(String rows);
}
