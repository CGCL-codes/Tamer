package com.sptci.rwt.webui.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An abstract base class that represents {@link com.sptci.rwt.MetaData}
 * objects that contain fields that hold SQL statements.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-10-07
 * @version $Id: SourceView.java 2 2007-10-19 21:06:36Z rakesh.vidyadharan $
 */
public abstract class SourceView extends AbstractView {

    /** The array of SQL key words to be high-lighted. */
    protected static final String[] KEYWORDS = { "select", "from", "where", "or", "and", "order by", "group by", "insert", "update", "into", "begin", "end", "as", "execute", "call", "for", "if", "return", "loop", "delete", "create", "drop", "procedure", "function", "table", "column", "view", "trigger", "index", "constraint", "primary", "foreign", "key", "type", "alter", "on", "cascade", "replace", "declare", "null", "raise", "exception", ":-", "is" };

    /**
   * The array of {@link java.util.regex.Pattern} instances used for
   * syntax highlighting.
   */
    protected static final Pattern[] PATTERNS = new Pattern[KEYWORDS.length];

    /** Static initialiser for {@link #PATTERNS}. */
    static {
        for (int i = 0; i < KEYWORDS.length; ++i) {
            PATTERNS[i] = Pattern.compile("\\b((?i)" + KEYWORDS[i] + ")\\b");
        }
    }

    /**
   * Apply syntax highlighting to matching words listed in {@link
   * #PATTERNS}.
   *
   * @see #toHtml
   * @param statement The string on which syntax highlighting is to be
   *   applied.
   * @return The modified string.
   */
    protected String syntaxHighlight(final String statement) {
        String text = statement;
        for (Pattern pattern : PATTERNS) {
            Matcher matcher = pattern.matcher(text);
            text = matcher.replaceAll("<font_style='color:_blue;'>$0</font>");
        }
        return toHtml(text);
    }

    /**
   * Replace spaces and other characters with HTML equivalents.
   *
   * @param statement The string which is to be coverted to HTML.
   * @return The modified string.
   */
    protected String toHtml(final String statement) {
        String text = statement.replaceAll("\\r\\n", "<br/>");
        text = text.replaceAll("\\n", "<br/>");
        text = text.replaceAll("\\r", "<br/>");
        text = text.replaceAll("\\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        text = text.replaceAll("\\s", "&nbsp;");
        text = text.replaceAll("font_style", "font style");
        text = text.replaceAll(":_blue", ": blue");
        return text;
    }
}
