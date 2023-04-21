package ssmith.html;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author stevec
 *
 */
public class HTMLFuncs_old {

    private static final String CR = "\r\n";

    public HTMLFuncs_old() {
    }

    public static String Title(int size, String text) {
        return "<h" + size + ">" + text + "</h" + size + ">";
    }

    public static String Heading(int size, String text) {
        return Title(size, text);
    }

    public static String Para(String text) {
        return "<p>" + text + "</p>";
    }

    public static String Line(String text) {
        return text + "<br />";
    }

    public static String StartTable(String cls, String width) {
        return "<table class=\"" + cls + "\" width=\"" + width + "\">";
    }

    public static String StartTable() {
        return "<table>";
    }

    public static String EndTable() {
        return "</table>";
    }

    public static String FinishTable() {
        return EndTable();
    }

    public static String StartRow() {
        return "<tr>";
    }

    public static String EndRow() {
        return "</tr>";
    }

    public static String FinishRow() {
        return EndRow();
    }

    public static String StartUnorderedList(boolean nested) {
        return (nested ? "<li>" : "") + "<ul>" + CR;
    }

    public static String EndUnorderedList(boolean nested) {
        return "</ul>" + (nested ? "</li>" : "") + CR;
    }

    public static String AddListEntry(String text) {
        return "<li class=\"simpleborder\">" + text + "</li>" + CR;
    }

    public static String Centre(String text) {
        return "<center>" + text + "</center>";
    }

    public static String AddCell(String text) {
        return AddCell(1, text, false, -1, "", "");
    }

    public static String AddCell(int colspan, String text) {
        return AddCell(colspan, text, false, -1, "", "");
    }

    public static String AddCell(String text, String align) {
        return AddCell(1, text, false, -1, align, "");
    }

    public static String AddCell(String text, int width) {
        return AddCell(1, text, false, width, "", "");
    }

    public static String AddCell(String text, boolean bold, int width) {
        return AddCell(1, text, bold, width, "", "");
    }

    public static String AddCell(int colspan, String text, boolean bold, int width, String align, String valign) {
        StringBuffer str = new StringBuffer();
        str.append("<td" + (width > 0 ? " width=\"" + width + "\"" : ""));
        str.append((colspan > 1 ? " colspan=\"" + colspan + "\"" : ""));
        str.append((align.length() > 0 ? " align=\"" + align + "\"" : ""));
        str.append((valign.length() > 0 ? " valign=\"" + valign + "\"" : ""));
        str.append(">" + (bold ? "<b>" : "") + text + (bold ? "</b>" : ""));
        str.append("</td>");
        return str.toString();
    }

    public static String StartForm(String name, String action, String method) {
        StringBuffer str = new StringBuffer();
        str.append("<form name=\"" + name + "\" action=\"" + action + "\" method=\"" + method + "\">");
        return str.toString();
    }

    public static String EndForm() {
        StringBuffer str = new StringBuffer();
        str.append("</form>");
        return str.toString();
    }

    public static String HiddenValue(String name, String val) {
        return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + val + "\">";
    }

    public static String TextBox(String name, String def, int maxlen) {
        StringBuffer str = new StringBuffer();
        str.append("<input name=\"" + name + "\" type=\"text\"");
        if (def.length() > 0) {
            str.append(" value=\"" + def + "\"");
        }
        str.append(" maxlength=\"" + maxlen + "\" />");
        return str.toString();
    }

    public static String PasswordBox(String name) {
        StringBuffer str = new StringBuffer();
        str.append("<input name=\"" + name + "\" type=\"password\"");
        str.append(" />");
        return str.toString();
    }

    public static String ComboBox(String name, String keys[], String values[]) {
        StringBuffer str = new StringBuffer();
        str.append("<select name=\"" + name + "\">");
        for (int i = 0; i < values.length; i++) {
            str.append("<option value=\"" + keys[i] + "\">" + values[i] + ">");
        }
        str.append("</select>");
        return str.toString();
    }

    public static String ComboBox(String name, ResultSet rs, int def) throws SQLException {
        StringBuffer str = new StringBuffer();
        str.append("<select name=\"" + name + "\">");
        while (rs.next()) {
            str.append("<option value=\"" + rs.getInt(1) + "\"");
            if (def == rs.getInt(1)) {
                str.append(" selected=\"selected\" ");
            }
            str.append(">" + rs.getObject(2) + "</option>");
        }
        str.append("</select>");
        return str.toString();
    }

    public static String ComboBox(String name, String[] values) throws SQLException {
        StringBuffer str = new StringBuffer();
        str.append("<select name=\"" + name + "\">");
        for (int i = 0; i < values.length; i++) {
            str.append("<option value=\"" + i + "\">" + values[i] + "</option>");
        }
        str.append("</select>");
        return str.toString();
    }

    public static String SubmitButton(String text) {
        StringBuffer str = new StringBuffer();
        str.append("<input type=\"submit\" value=\"" + text + "\" />");
        return str.toString();
    }

    public static String Image(String url, String align, String alt) {
        StringBuffer str = new StringBuffer();
        str.append("<img src=\"" + url + "\" align=\"" + align + "\" alt=\"" + HTMLFunctions.s2HTML(alt) + "\" />");
        return str.toString();
    }

    public static String BlankLines(int no) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < no; i++) {
            str.append("<br />");
        }
        return str.toString();
    }
}
