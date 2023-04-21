package com.google.gwt.dom.builder.shared;

/**
 * Builds an tablesection element.
 */
public interface TableSectionBuilder extends ElementBuilderBase<TableSectionBuilder> {

    String UNSUPPORTED_HTML = "Table section elements do not support setting inner html or text. " + "Use startTR() instead to append a table row to the section.";

    /**
   * Horizontal alignment of data in cells. See the align attribute for
   * HTMLTheadElement for details.
   */
    TableSectionBuilder align(String align);

    /**
   * Alignment character for cells in a column.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/tables.html#adef-char">W3C
   *      HTML Specification</a>
   */
    TableSectionBuilder ch(String ch);

    /**
   * Offset of alignment character.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/struct/tables.html#adef-charoff">W3C
   *      HTML Specification</a>
   */
    TableSectionBuilder chOff(String chOff);

    /**
   * Vertical alignment of data in cells. See the valign attribute for
   * HTMLTheadElement for details.
   */
    TableSectionBuilder vAlign(String vAlign);
}
