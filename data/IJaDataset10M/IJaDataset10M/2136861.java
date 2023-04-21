package simpleorm.simplets.plain;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import simpleorm.simplets.servlet.HRequestletFactory;

/**
 * Simple Widgets, used from HSuperRequestlet as w.*
 * 
 * @author aberglas
 */
public class HHtmlBasic {

    HPlainRequestlet sreq;

    /** field name --> error messages. */
    LinkedHashMap<String, String> errorMessages = new LinkedHashMap();

    HHtmlBasic(HPlainRequestlet sreq) {
        this.sreq = sreq;
    }

    private void outHtml(String html) {
        sreq.outHtml(html);
    }

    private void pushHtml(String html) {
        sreq.pushHtml(html);
    }

    private void popHtml(String html) {
        sreq.popHtml(html);
    }

    private void endTag(String extras) {
        if (extras != null) {
            outHtml(" ");
            outHtml(extras);
        }
        outHtml(">\n");
    }

    public void p() {
        outHtml("<P>\n");
    }

    public void br() {
        outHtml("<br>\n");
    }

    public void hr() {
        outHtml("<hr>\n");
    }

    public void center() {
        pushHtml("<center>");
    }

    public void _center() {
        popHtml("</center>\n");
    }

    /**
   * Produces an a to requestlet clazz with description from
   * its getShortTitle.
   */
    public void a(HRequestletFactory rfact) {
        a(sreq.contextedUrl(rfact.requestletName()), rfact.getShortTitle());
    }

    /** Just output an anchor.  
   * displayHtml escaped but defaults to the url itself.  Both with spaces as nbsp;
   */
    public void a(String url, String displayHtml) {
        outHtml("<a href='");
        outHtml(url);
        outHtml("'>");
        if (displayHtml != null) link(displayHtml); else link(url);
        outHtml("</a>\n");
    }

    /** Currently just outputs html, but could check nesting levels later. */
    public void table() {
        pushHtml("<table>\n");
    }

    public void table(String extras) {
        pushHtml("<table " + extras + ">\n");
    }

    public void table_border() {
        pushHtml("<table border=1 cellspacing=0 cellpadding=5>\n");
    }

    public void table_border(String extras) {
        pushHtml("<table border=1 cellspacing=0 cellpadding=4 " + extras + ">\n");
    }

    public void _table() {
        popHtml("</table>\n");
    }

    public void tr() {
        pushHtml("<tr>");
    }

    public void _tr() {
        popHtml("</tr>\n");
    }

    public void td() {
        pushHtml("<td>");
    }

    public void td(String extras) {
        pushHtml("<td " + (extras != null ? extras : "") + ">");
    }

    public void td_top() {
        pushHtml("<td valign=top>");
    }

    public void td_top(String extras) {
        pushHtml("<td valign=top " + (extras != null ? extras : "") + ">");
    }

    public void td_right() {
        pushHtml("<td align=right>");
    }

    public void td_center() {
        pushHtml("<td align=center>");
    }

    public void td_center(String extras) {
        pushHtml("<td align=center " + extras + ">");
    }

    public void _td() {
        popHtml("</td>\n");
    }

    public void tableHeadings(String... headings) {
        tr();
        for (String head : headings) {
            td_top();
            prompt(head);
            _td();
        }
        _tr();
    }

    /** outputs <IMG with name localized using localUrl. */
    public void localImg(String name, String extras) {
        outHtml("<img src='");
        outHtml(sreq.contextedUrl(name));
        outHtml("'");
        if (extras != null) outHtml(extras);
        outHtml(">");
    }

    public void localImg(String name) {
        localImg(name, null);
    }

    /**
   * Outputs escaped data values, wrapped in &lt;CITE>
   */
    public void data(String data) {
        outHtml("<code>");
        if (data != null && data.length() > 0) sreq.outEscaped(data); else sreq.outHtml("&nbsp;");
        outHtml("</code>");
    }

    /**
   * Outputs escaped weak Data values, wrapped in &lt;kbd>
   */
    public void weakData(String data) {
        outHtml("<kbd>");
        if (data != null) sreq.outEscaped(data); else sreq.outHtml("&nbsp;");
        outHtml("</kbd>");
    }

    /**
   * Outputs links description
   */
    public void link(String data) {
        outHtml("<samp>");
        sreq.outEscaped(data, true);
        outHtml("</samp>");
    }

    /**
   * Outputs preformatted values, wrapped in a PRE, escaped.
   */
    public void preformatted(String data) {
        outHtml("<pre>");
        sreq.outEscaped(data);
        outHtml("</pre>");
    }

    /** Heading (H3), not escaped. */
    public void subheading(String head) {
        outHtml("<h4>" + head + "</h4>\n");
    }

    /**
   *Outputs a prompt, wrapping in &lt;EM>
   *NOT escaped
   */
    public void prompt(String prompt) {
        outHtml("<em>");
        outHtml(prompt);
        outHtml("</em>");
    }

    /**
   *Outputs a bold prompt, wrapping in &lt;STRONG>
   *NOT escaped
   */
    public void strongPrompt(String prompt) {
        outHtml("<strong>");
        outHtml(prompt);
        outHtml("</strong>");
    }

    /** Just output message with var.  Escaped.*/
    public void errorMessage(String message) {
        outHtml("<var>");
        sreq.outEscaped(message);
        outHtml("</var>\n");
    }

    /** Mark field with an error message which will be displayed later by fieldPrompt etc. 
   * Message should end in a "." so that others can be appended.  Escaped.
   * The message is recorded in errorMessages. */
    public void addFieldError(String field, String message) {
        String prev = errorMessages.get(field);
        String msg = (prev == null ? "" : (prev + "  ")) + message;
        errorMessages.put(field, msg);
    }

    /** Add an error not associated with a field. 
   *  Just displayed at front.
   */
    public void addError(String message) {
        String key = NON_FIELD + errorMessages.size() + 1;
        errorMessages.put(key, message);
    }

    private static final String NON_FIELD = "_NonField_";

    /** Outputs any error messages for field that were recorded by addFieldError. */
    public void outputFieldErrorMessages(String field) {
        String msg = errorMessages.get(field);
        if (msg != null) errorMessage(msg);
    }

    /** Outputs all error messages, as would happen at the top of a form.
   *  class=all-errors */
    public void outputAllErrorMessages() {
        boolean any = false;
        for (Entry<String, String> ent : errorMessages.entrySet()) {
            if (!any) outHtml("<Div class=all-errors>\n");
            any = true;
            String field = ent.getKey();
            if (!field.startsWith(NON_FIELD)) {
                sreq.outEscaped(field);
                outHtml(": &nbsp;&nbsp ");
            }
            sreq.outEscaped(ent.getValue());
            outHtml("<br>\n");
        }
        if (any) outHtml("</Div><P>\n");
    }

    public boolean hasErrors() {
        return errorMessages.size() > 0;
    }
}
