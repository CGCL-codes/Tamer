package gate.yam.translate;

/**
 * Constants used when translating HTML. One entry for each type of parse tree
 * node. Each entry contains the node type name, the start constant and the
 * end constant.
 * <pre>
 * constantsTable[CONSTANTNAME] is the node type name
 * constantsTable[CONSTANTSTART] is the start string
 * constantsTable[CONSTANTEND] is the end string
 * </pre>
 * A separate map is defined for predicates, and there are misc patterns for
 * e.g. headings construction.
 * @author Hamish Cunningham
 */
public interface HtmlConstants {

    /** Array mapping node type name to start/end strings. */
    final String[][] htmlConstantsTable = { { "YamDocument", "", "</body></html>\n" }, { "Sep", "", "" }, { "Title", "", "" }, { "Text", "", "" }, { "Verbatim", "<pre>", "</pre>" }, { "Word", "", "" }, { "Escape", "<span class=\"cow-escape\">", "</span>" }, { "Plain", "", "" }, { "Control", "", "" }, { "TargetControl", "", "" }, { "Hr", "<hr>", "" }, { "Url", "<a class=\"_Y_\" href=\"_X_\">", "</a>" }, { "Anchor", "<a name=\"_X_\">", "</a>" }, { "Br", "<br>", "" }, { "Nbsp", "&nbsp;", "" }, { "Unit", "", "" }, { "SectionHead", "", "" }, { "SectionText", "", "" }, { "Paragraph", "<p>", "</p>" }, { "List", "", "" }, { "ListItem", "<li>", "</li>" }, { "OList", "<ol>", "</ol>" }, { "UList", "<ul>", "</ul>" }, { "Table", "<table>", "</table>" }, { "Row", "<tr>", "</tr>" }, { "Column", "<td>", "</td>" }, { "TableSep", "", "" }, { "Whsp", "", "" }, { "Contents", "<div class=\"cow-contents\">", "</div>" }, { "Footnote", "", "" }, { "Predicate", "", "" }, { "TextOrTable", "", "" }, { "Skip", "", "" }, { "*", "<b>", "</b>" }, { "^", "<tt>", "</tt>" }, { "_", "<em>", "</em>" }, { "__", "<u>", "</u>" }, { "<", "&lt;", "" }, { "&", "&amp;", "" }, { "--", "&mdash;", "" }, { "%\"", "<blockquote>", "</blockquote>" }, { "image", "<img", ">" }, { "footnote", "<footnote>", "</footnote>" }, { "cite", "<span class=\"cow-cite\" keys=\"_X_\">", "</span>" }, { "include", "", "" }, { "olistPara", "<p><ol>", "</ol></p>" }, { "ulistPara", "<p><ul>", "</ul></p>" }, { "anchorPattern", "<a class=\"_Y_\" name=\"_X_\"></a>", "" }, { "linkPattern", "<a href=\"_X_\">_Y_</a>", "" }, { "headingPattern", "<h_X_ class=\"_Z_\">_Y_</h_X_>", "" }, { "nbSpace", "&nbsp;", "" }, { "headNumber", "<span class=\"cow-sec-number\">", "</span>" }, { "footnoteText", "<span class=\"cow-footnote-text\" name=\"_X_\">", "</span>" }, { "footnoteSection", "<span class=\"cow-footnote-section\">", "</span>" }, { "comment", "<!--", "-->" }, { "1", "1", "" }, { "2", "2", "" }, { "3", "3", "" }, { "4", "4", "" }, { "5", "5", "" }, { "6", "6", "" }, { "7", "7", "" }, { "8", "8", "" }, { "9", "9", "" } };

    /** Attributes allowed on images. */
    final String[] imageAttrs = { " src=\"_X_\"", " alt=\"_X_\"", " width=\"_X_\"", " height=\"_X_\"", " align=\"_X_\"", " border=\"_X_\"" };

    /** Attributes allowed on footnotes. */
    final String[] footnoteAttrs = { "_X_" };

    /** Attributes allowed on includes. */
    final String[] includeAttrs = { "_X_" };

    /** Array mapping predicate type name to attributes. */
    final Object[][] htmlPredicatesTable = { { "image", imageAttrs }, { "footnote", footnoteAttrs }, { "include", includeAttrs } };
}
