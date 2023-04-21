package sunlabs.brazil.sunlabs;

import sunlabs.brazil.server.Request;
import sunlabs.brazil.server.Server;
import sunlabs.brazil.template.RewriteContext;
import sunlabs.brazil.template.Template;
import sunlabs.brazil.util.regexp.Regexp;
import java.util.StringTokenizer;

/**
 * Template for generating formatted JSON output.
 * This can be used to replace the JSON macros for
 * greater efficiency.
 */
public class JSONTemplate extends Template {

    int indent = 0;

    int col = 0;

    boolean needComma = false;

    static Regexp noQuoteRe = new Regexp("^((-?[0-9]+)|true|false)$");

    int maxLine = 80;

    int indentLevel = 2;

    boolean was = false;

    boolean processing = false;

    static final String Spaces = "                                           ";

    /**
     * Stop accumulating tokens.
     * All output should be explicit.
     * Note: this will interact poorly with templates
     * that reset accumulate() apriori.
     * JSON tags don't nest.
     * <dl>
     * <dt>maxLine<dd>Min line length for newline (default=80)
     * <dt>indentLevel<dd>Indent increment (default=2)
     * </dl>
     */
    public void tag_json(RewriteContext hr) {
        indent = 0;
        needComma = false;
        was = hr.accumulate(false);
        try {
            indentLevel = Integer.parseInt(hr.get("indentLevel", "2"));
        } catch (NumberFormatException e) {
        }
        try {
            maxLine = Integer.parseInt(hr.get("maxLine", "80"));
        } catch (NumberFormatException e) {
        }
        processing = true;
    }

    /**
     * Reset token accumulation
     */
    public void tag_slash_json(RewriteContext hr) {
        processing = false;
        hr.accumulate(was);
    }

    /** Remove all comments. */
    public void comment(RewriteContext hr) {
    }

    /** Remove all text between tags. */
    public void string(RewriteContext hr) {
        if (processing) hr.accumulate(false);
    }

    /**
     * A Json Item.
     * &lt;item name="name" value="value" /&gt;
     * &lt;item name="name"&gt; ... &lt;/jsonitem&gt;
     */
    public void tag_item(RewriteContext hr) {
        if (hr.isSingleton()) {
            emitComma(hr);
            emit(hr, doValue(hr.get("name")) + ": " + doValue(hr.get("value")));
            needComma = true;
        } else {
            emitComma(hr);
            indent++;
            emit(hr, doValue(hr.get("name")) + ": ");
        }
    }

    public void tag_slash_item(RewriteContext hr) {
        needComma = true;
        indent--;
    }

    /**
     * A JSON array element .
     * &lt;element value="..." /&gt;
     */
    public void tag_element(RewriteContext hr) {
        emitComma(hr);
        String v = hr.get("value");
        if (v != null && !v.equals("")) {
            emit(hr, doValue(v));
            needComma = true;
        }
    }

    /**
     * Begin a JSON object
     * &lt;object&gt; ... &lt;/object&gt;
     */
    public void tag_object(RewriteContext hr) {
        emitComma(hr);
        nl(hr);
        indent++;
        String name = hr.get("name");
        if (name == null || name.equals("")) {
            emit(hr, "{");
        } else {
            emit(hr, "{ " + doValue(name) + " : ");
        }
    }

    /**
     * Begin a JSON array
     * &lt;array&gt; &lt;element... /&gt; ... &lt;element ... /&gt; &lt;/array&gt;
     */
    public void tag_array(RewriteContext hr) {
        emitComma(hr);
        indent++;
        emit(hr, "[ ");
    }

    public void tag_slash_array(RewriteContext hr) {
        indent--;
        emit(hr, " ]");
        needComma = true;
    }

    public void tag_slash_object(RewriteContext hr) {
        emit(hr, "}");
        indent--;
    }

    /**
     * Decide whether a value needs to be enclosed in '"', and do it.
     */
    String doValue(String s) {
        if (s == null) {
            return "\"\"";
        }
        return noQuoteRe.match(s) == null ? "\"" + esc(s) + "\"" : s;
    }

    /**
     * Escape JSON strings - partial implementation.
     * (Stolen from the JSON converter)
     */
    String esc(String value) {
        if (value == null) {
            return "";
        }
        StringTokenizer st = new StringTokenizer(value, "\"\\/\b\t\f\n\r", true);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.length() > 1) {
                sb.append(token);
            } else {
                switch(token.charAt(0)) {
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\"':
                    case '\\':
                    case '/':
                        sb.append("\\").append(token);
                        break;
                    default:
                        sb.append(token);
                        break;
                }
            }
        }
        return sb.toString();
    }

    /** emit a comma. */
    void emitComma(RewriteContext hr) {
        if (needComma) {
            hr.append(",");
            col++;
            if (col > maxLine) {
                nl(hr);
            }
            needComma = false;
        }
    }

    void nl(RewriteContext hr) {
        hr.append("\n");
        skip(hr);
        col = indent * 2;
    }

    /**
     * Emit markup, keep track of colum position
     */
    void emit(RewriteContext hr, String s) {
        col += s.length();
        hr.append(s);
    }

    /**
     * Skip over the proper whitespace.
     */
    void skip(RewriteContext hr) {
        hr.append(Spaces.substring(0, Math.min(indent * indentLevel, Spaces.length())));
    }
}
