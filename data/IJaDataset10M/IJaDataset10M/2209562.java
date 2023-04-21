package com.xmultra.processor.news;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.xmultra.util.InitMapHolder;
import com.xmultra.util.Strings;
import com.xmultra.util.XmlParseUtils;

/**

 * Identifies and processes items in Nim that are appropriate for

 * insertion into NITF body.end element.<P>

 *

 * All functionality in BodyContentParser assumes the existence of the

 * body.content node in the nitfDoc.  This node is read in from the

 * EMPTY_NITF_DOCUMENT constant in Nitf.java.  BodyContentParser will not

 * function properly without an existing body.content node.<P>

 *

 * Any changes to the empty NITF document should be reflected in this code,

 * and vice-versa.<P>

 *

 * @author      Shannon C. Brown

 * @version     $Revision: #1 $

 * @since       1.2

 */
class BodyContentParser extends NimParser {

    /**

    * Updated automatically by source control management.

    */
    static final String VERSION = "@version $Revision: #1 $";

    static final String PARAGRAPH = Nim.LT + Nitf.P + "(?! ?/)[^>]*" + Nim.GT + "(" + Strings.ANY_TEXT + ")" + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.P + Nim.GT;

    static final String HL2_HEAD = Nim.LT + Nitf.HL2 + "\\s*(?:[^>]*[^/])?" + Nim.GT + "(" + Strings.ANY_TEXT + ")" + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.HL2 + Nim.GT;

    static final String BLOCK = Nim.LT + Nitf.BLOCK + "[^>/]*(?!/)" + Nim.GT + "(" + Strings.ANY_TEXT + ")" + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.BLOCK + Nim.GT;

    static final String TABLE_OPEN = Nim.LT + Nitf.TABLE + "[^>/]*?" + Nim.GT;

    static final String TABLE = BodyContentParser.TABLE_OPEN + "(" + Strings.ANY_TEXT + ")" + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.TABLE + Nim.GT;

    static final String HR = Nim.LT + Nitf.HR + "[^>]*" + Nim.BACKSLASH + Nim.SLASH + Nim.GT;

    static final String PARAGRAPH_OPEN_STYLE_PATTERN = Nim.LT + Nitf.P + " " + Nitf.STYLE_CONTENT_PATTERN + Nim.GT;

    static final String BLOCK_OPEN_STYLE_PATTERN = Nim.LT + Nitf.BLOCK + " " + Nitf.STYLE_CONTENT_PATTERN + Nim.GT;

    static final String HL2_OPEN_STYLE_PATTERN = Nim.LT + Nitf.HL2 + " " + Nitf.STYLE_CONTENT_PATTERN + Nim.GT;

    static final String MEDIA = Nim.LT + Nitf.MEDIA + "[^>]*" + Nim.GT + "(" + Strings.ANY_TEXT + ")" + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.MEDIA + Nim.GT;

    static final String MEDIA_TYPE_OPEN_PATTERN = Nim.LT + Nitf.MEDIA + "[^>]*?" + Nitf.MEDIA_TYPE_CONTENT;

    static final String MEDIA_OPEN_STYLE_PATTERN = Nim.LT + Nitf.MEDIA + "[^>]*?" + Nitf.STYLE_CONTENT_PATTERN;

    static final String MEDIA_OPEN = Nim.LT + Nitf.MEDIA + "[^>]*?" + Nim.GT;

    static final String ANY_BODY_CONTENT_CHILD = PARAGRAPH + "|" + HL2_HEAD + "|" + BLOCK + "|" + TABLE + "|" + MEDIA + "|" + HR;

    private Strings strings;

    private InitMapHolder initMapHolder;

    private XmlParseUtils xmlParseUtils;

    private Document nitfDoc;

    /** Constructor initializes with the InitMapHolder instance */
    BodyContentParser(InitMapHolder imh) {
        initMapHolder = imh;
        xmlParseUtils = (XmlParseUtils) initMapHolder.getEntry(InitMapHolder.XML_PARSE_UTILS);
        strings = (Strings) initMapHolder.getEntry(InitMapHolder.STRINGS);
    }

    /**

     * Implements the NimParser interface.  This implementation identifies

     * all the portions of the source document that have been marked up for

     * insertion into the body.content node of the NITF document and

     * inserts them in the proper place.  This includes paragraphs,

     * subheads, and block quotes.

     *

     * @param doc        The Doc object to be parsed.

     */
    void parse(Doc doc) {
        if (doc.getDocStatus().get(DocStatus.THROW_AWAY) != null) {
            return;
        }
        nitfDoc = doc.getNITFDocument();
        String body = doc.getBody();
        Element bodyContentElem = (Element) xmlParseUtils.getSingletonNode(nitfDoc, Nitf.BODY_CONTENT);
        body = removePreviouslyParsedElements(body);
        body = setBlockQuotes(body, bodyContentElem);
        body = cleanEmptyParagraphs(body);
        setBodyContent(body, bodyContentElem);
        doc.setBody(body);
    }

    /**

     * Strips {<ANY_TEXT>} from body of source document.  Previous parsers

     * will have wrapped XML-style tags to indicate that they have already

     * been processed.  Calling this method ensures they are not processed

     * again.

     */
    private String removePreviouslyParsedElements(String body) {
        return strings.strip(Nim.BR_LT + Strings.ANY_TEXT + Nim.BR_GT, body);
    }

    /**

     * Removes empty paragraph markers (&lt;p&gt;&lt;/p&gt;) from the body of

     * the source document.

     */
    private String cleanEmptyParagraphs(String body) {
        body = strings.strip(Nim.PARAGRAPH_BLANK, body);
        return body;
    }

    /**

     * Identifies any block quotes in the pre-parsed source document and

     * inserts them in the NITF document according to its content model.

     */
    private String setBlockQuotes(String body, Element bodyContentElem) {
        String pattern = Nim.LT + Nitf.BQ + Nim.GT + Strings.ANY_TEXT + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.BQ + Nim.GT;
        if (strings.matches(pattern, body)) {
            String matchedQuote = strings.getMatchedGroup(0);
            String credit = null;
            if (strings.matches(Nim.LT + Nitf.CREDIT + Nim.GT + "(" + Strings.ANY_TEXT + ")" + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.CREDIT + Nim.GT, matchedQuote)) {
                credit = strings.getMatchedGroup(1);
                if (strings.matches(Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.CREDIT + Nim.GT + "(" + Strings.ANY_TEXT + ")" + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.BQ + Nim.GT, matchedQuote)) {
                    credit += strings.getMatchedGroup(1);
                    credit = strings.stripSurroundingWhiteSpace(credit);
                    matchedQuote = strings.substitute(Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.CREDIT + Nim.GT + Strings.ANY_TEXT + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.BQ + Nim.GT, Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.CREDIT + Nim.GT + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.BQ + Nim.GT, matchedQuote);
                }
            }
            matchedQuote = strings.strip(Nim.LT + Nitf.CREDIT + Nim.GT + Strings.ANY_TEXT + Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.CREDIT + Nim.GT, matchedQuote);
            matchedQuote = strings.strip(Nim.LT + Nitf.BQ + Nim.GT, matchedQuote);
            matchedQuote = strings.strip(Nim.LT + Nim.BACKSLASH + Nim.SLASH + Nitf.BQ + Nim.GT, matchedQuote);
            matchedQuote = strings.stripSurroundingWhiteSpace(matchedQuote);
            Element blockQuote = nitfDoc.createElement(Nitf.BQ);
            Element innerBlock = nitfDoc.createElement(Nitf.BLOCK);
            Element quoteParagraph = nitfDoc.createElement(Nitf.P);
            quoteParagraph.appendChild(nitfDoc.createTextNode(matchedQuote));
            innerBlock.appendChild(quoteParagraph);
            blockQuote.appendChild(innerBlock);
            if (credit != null) {
                Element creditElem = nitfDoc.createElement(Nitf.CREDIT);
                creditElem.appendChild(nitfDoc.createTextNode(credit));
                blockQuote.appendChild(creditElem);
            }
            bodyContentElem.appendChild(blockQuote);
        }
        body = strings.strip(pattern, body);
        return body;
    }

    /**

     * Identifies all child elements of body.content (&lt;p&gt;, &lt;block&gt;,

     * &lt;hl2&gt;, &lt;table&gt; &lt;media&gt;) and properly inserts them in

     * the NITF document in the order in which they are encountered.  Assumes

     * that the content model of the NITF is properly followed inside each of

     * these two types of element.

     *

     * @param body   A String containing the body of the document.

     * @param bodyContentElem

     *               An Element representing the NITF body.content node.

     */
    private void setBodyContent(String body, Element bodyContentElem) {
        Element newElem = null;
        String after = body;
        String content = null;
        String style = null;
        String contentWithTags = null;
        while (strings.matches(ANY_BODY_CONTENT_CHILD, after)) {
            boolean checkForEmptyContent = true;
            style = null;
            after = strings.getPostMatch();
            if ((content = strings.getGroup(1)) != null) {
                contentWithTags = strings.getGroup(0);
                if (strings.matches(PARAGRAPH_OPEN_STYLE_PATTERN, contentWithTags)) {
                    style = strings.getGroup(1);
                }
                newElem = nitfDoc.createElement(Nitf.P);
            } else if ((content = strings.getGroup(2)) != null) {
                contentWithTags = strings.getGroup(0);
                if (strings.matches(HL2_OPEN_STYLE_PATTERN, contentWithTags)) {
                    style = strings.getGroup(1);
                }
                content = strings.stripRepeatingAndSurroundingSpace(null, content);
                newElem = nitfDoc.createElement(Nitf.HL2);
            } else if ((content = strings.getGroup(3)) != null) {
                contentWithTags = strings.getGroup(0);
                if (strings.matches(BLOCK_OPEN_STYLE_PATTERN, contentWithTags)) {
                    style = strings.getGroup(1);
                }
                newElem = nitfDoc.createElement(Nitf.BLOCK);
            } else if ((content = strings.getGroup(4)) != null) {
                contentWithTags = strings.getGroup(0);
                newElem = nitfDoc.createElement(Nitf.TABLE);
                String tableOpenTag = null;
                if (strings.matches(TABLE_OPEN, contentWithTags)) {
                    tableOpenTag = strings.getGroup(0);
                }
                if (tableOpenTag != null) {
                    if (strings.matches(Nitf.ALIGN_CONTENT, tableOpenTag)) {
                        newElem.setAttribute(Nitf.ALIGN, strings.getGroup(1));
                    }
                    if (strings.matches(Nitf.WIDTH_CONTENT, tableOpenTag)) {
                        newElem.setAttribute(Nitf.WIDTH, strings.getGroup(1));
                    }
                    if (strings.matches(Nitf.CLASS_CONTENT, tableOpenTag)) {
                        newElem.setAttribute(Nitf.CLASS, strings.getGroup(1));
                    }
                    if (strings.matches(Nitf.CELLPADDING_CONTENT, tableOpenTag)) {
                        newElem.setAttribute(Nitf.CELLPADDING, strings.getGroup(1));
                    }
                    if (strings.matches(Nitf.CELLSPACING_CONTENT, tableOpenTag)) {
                        newElem.setAttribute(Nitf.CELLSPACING, strings.getGroup(1));
                    }
                    if (strings.matches(Nitf.BORDER_CONTENT, tableOpenTag)) {
                        newElem.setAttribute(Nitf.BORDER, strings.getGroup(1));
                    }
                }
            } else if ((content = strings.getGroup(5)) != null) {
                contentWithTags = strings.getGroup(0);
                newElem = nitfDoc.createElement(Nitf.MEDIA);
                String mediaOpenTag = null;
                if (strings.matches(MEDIA_OPEN, contentWithTags)) {
                    mediaOpenTag = strings.getGroup(0);
                }
                if (mediaOpenTag != null) {
                    if (strings.matches(Nitf.STYLE_CONTENT_PATTERN, mediaOpenTag)) {
                        style = strings.getGroup(1);
                    }
                    if (strings.matches(Nitf.MEDIA_TYPE_CONTENT, mediaOpenTag)) {
                        newElem.setAttribute(Nitf.MEDIA_TYPE, strings.getGroup(1));
                    }
                    if (strings.matches(Nitf.CLASS_CONTENT, mediaOpenTag)) {
                        newElem.setAttribute(Nitf.CLASS, strings.getGroup(1));
                    }
                }
            } else if (strings.matches(HR, strings.getGroup(0))) {
                newElem = nitfDoc.createElement(Nitf.HR);
                checkForEmptyContent = false;
            }
            if (style != null) {
                newElem.setAttribute(Nitf.STYLE, style);
            }
            if (checkForEmptyContent) {
                if (content == null) continue;
                content = strings.substituteWithOptions("^\\s+", "", content, "s");
                content = strings.substituteWithOptions("\\s+$", "", content, "s");
                if (content.equals("")) continue;
                newElem.appendChild(nitfDoc.createTextNode(content));
            }
            bodyContentElem.appendChild(newElem);
        }
    }
}
