package org.wikimodel.wem.xhtml;

import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.WikiPageUtil;
import org.wikimodel.wem.WikiParameters;

/**
 * @author MikhailKotelnikov
 */
public class PrintListener extends PrintInlineListener {

    /**
     * @param printer
     */
    public PrintListener(IWikiPrinter printer) {
        super(printer);
    }

    public void beginDefinitionDescription() {
        print("  <dd>");
    }

    public void beginDefinitionList(WikiParameters parameters) {
        println("<dl>");
    }

    public void beginDefinitionTerm() {
        print("  <dt>");
    }

    public void beginDocument() {
        println("<div class='doc'>");
    }

    public void beginHeader(int level, WikiParameters params) {
        print("<h" + level + params + ">");
    }

    public void beginInfoBlock(char infoType, WikiParameters params) {
        print("<table" + params + "><tr><th>" + infoType + "</th><td>");
    }

    public void beginList(WikiParameters parameters, boolean ordered) {
        if (ordered) println("<ol" + parameters + ">"); else println("<ul" + parameters + ">");
    }

    public void beginListItem() {
        print("  <li>");
    }

    public void beginParagraph(WikiParameters params) {
        print("<p" + params + ">");
    }

    public void beginPropertyBlock(String propertyUri, boolean doc) {
        print("<div class='property' url='" + WikiPageUtil.escapeXmlAttribute(propertyUri) + "'>");
        if (doc) println("");
    }

    public void beginQuotation(WikiParameters params) {
        print("<blockquote" + params + ">");
    }

    public void beginQuotationLine() {
        println("");
    }

    public void beginTable(WikiParameters params) {
        println("<table" + params + "><tbody>");
    }

    public void beginTableCell(boolean tableHead, WikiParameters params) {
        String str = tableHead ? "<th" : "<td";
        print(str + params + ">");
    }

    public void beginTableRow(WikiParameters params) {
        print("  <tr" + params + ">");
    }

    public void endDefinitionDescription() {
        println("</dd>");
    }

    public void endDefinitionList(WikiParameters parameters) {
        println("</dl>");
    }

    public void endDefinitionTerm() {
        println("</dt>");
    }

    public void endDocument() {
        println("</div>");
    }

    public void endHeader(int level, WikiParameters params) {
        println("</h" + level + ">");
    }

    public void endInfoBlock(char infoType, WikiParameters params) {
        println("</td></tr></table>");
    }

    public void endList(WikiParameters parameters, boolean ordered) {
        if (ordered) println("</ol>"); else println("</ul>");
    }

    public void endListItem() {
        println("</li>");
    }

    public void endParagraph(WikiParameters params) {
        println("</p>");
    }

    public void endPropertyBlock(String propertyUri, boolean doc) {
        println("</div>");
    }

    public void endQuotation(WikiParameters params) {
        println("</blockquote>");
    }

    public void endQuotationLine() {
        println("");
    }

    public void endTable(WikiParameters params) {
        println("</tbody></table>");
    }

    public void endTableCell(boolean tableHead, WikiParameters params) {
        String str = tableHead ? "</th>" : "</td>";
        print(str);
    }

    public void endTableRow(WikiParameters params) {
        println("</tr>");
    }

    public void onEmptyLines(int count) {
        if (count > 1) {
            println("<div style='height:" + count + "em;'></div>");
        }
    }

    /**
     * @see org.wikimodel.wem.xhtml.PrintInlineListener#onExtensionBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionBlock(String extensionName, WikiParameters params) {
        println("<div class='extension' extension='" + extensionName + "'" + params + "/>");
    }

    public void onHorizontalLine() {
        println("<hr />");
    }

    public void onMacroBlock(String macroName, WikiParameters params, String content) {
        println("<pre class='macro' macroName='" + macroName + "'" + params + "><![CDATA[" + content + "]]></pre>");
    }

    public void onMacroInline(String macroName, WikiParameters params, String content) {
        print("<span class='macro' macroName='" + macroName + "'" + params + "><![CDATA[" + content + "]]></span>");
    }

    public void onTableCaption(String str) {
    }

    public void onVerbatimBlock(String str) {
        println("<pre>" + WikiPageUtil.escapeXmlString(str) + "</pre>");
    }
}
