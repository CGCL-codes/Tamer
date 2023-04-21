package com.safi.workshop.sqlexplorer.parsers;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import com.safi.db.DbFactory;
import com.safi.workshop.part.SafiWorkshopEditorUtil;
import com.safi.workshop.sqlexplorer.IConstants;
import com.safi.workshop.sqlexplorer.parsers.Tokenizer.Token;
import com.safi.workshop.sqlexplorer.parsers.scp.StructuredCommentException;
import com.safi.workshop.sqlexplorer.parsers.scp.StructuredCommentParser;
import com.safi.workshop.sqlexplorer.plugin.views.SqlexplorerViewConstants;

/**
 * This parser is based on scanning the SQL text looking for separators (eg ";", "go", or
 * "/") that show where to split the text into separate queries; conversely, the new
 * AbstractSyntaxQueryParser derived style is based on scanning the SQL text for language
 * grammar tokens so that it can split the SQL by natural syntax.
 * 
 * (Note: the previous version was the SE3.0.0 parser, this is a rewrite for SE3.5RC6
 * 
 * @modified John Spackman
 */
public class BasicQueryParser extends AbstractQueryParser {

    private static final String QUOTE_CHARS = "'\"";

    private int lineNo;

    private int charIndex;

    private char quoteEscapes;

    private String slComment;

    private String mlCommentStart;

    private String mlCommentEnd;

    private String cmdSeparator;

    private String altSeparator;

    private boolean enableStructuredComments;

    private CharSequence sql;

    private LinkedList<Query> queries;

    /**
   * Constructor
   * 
   * @param sql
   */
    public BasicQueryParser(CharSequence sql) {
        super();
        if (sql == null) this.sql = ""; else this.sql = sql;
        lineNo = 1;
        cmdSeparator = ";";
        altSeparator = "GO";
        slComment = "--";
        mlCommentStart = "/*";
        mlCommentEnd = "*/";
    }

    /**
   * Constructor
   * 
   * @param sql
   */
    public BasicQueryParser(CharSequence sql, Preferences prefs, int pLineNo) {
        this(sql);
        setCmdSeparator(prefs.getString(IConstants.SQL_QRY_DELIMITER));
        setAltSeparator(prefs.getString(IConstants.SQL_ALT_QRY_DELIMITER));
        setSlComment(prefs.getString(IConstants.SQL_SL_COMMENT));
        setMlCommentStart(prefs.getString(IConstants.SQL_ML_COMMENT_START));
        setMlCommentEnd(prefs.getString(IConstants.SQL_ML_COMMENT_END));
        setQuoteEscapes(prefs.getString(IConstants.SQL_QUOTE_ESCAPE_CHAR));
        enableStructuredComments = prefs.getBoolean(IConstants.ENABLE_STRUCTURED_COMMENTS);
        String str = getPref(prefs, IConstants.SQL_QUOTE_ESCAPE_CHAR);
        if (str != null) {
            str = str.trim();
            if (str.length() > 0) quoteEscapes = str.charAt(0);
        }
        lineNo = pLineNo;
    }

    public void parse() throws ParserException {
        if (sql == null) return;
        if (enableStructuredComments) {
            StringBuffer buffer = new StringBuffer(sql.toString());
            Tokenizer tokenizer = new Tokenizer(buffer);
            StructuredCommentParser structuredComments = new StructuredCommentParser(this, buffer);
            try {
                Token token;
                while ((token = tokenizer.nextToken()) != null) {
                    if (token.getTokenType() == Tokenizer.TokenType.EOL_COMMENT || token.getTokenType() == Tokenizer.TokenType.ML_COMMENT) {
                        structuredComments.addComment(token);
                    }
                }
            } catch (StructuredCommentException e) {
            }
            structuredComments.process();
            tokenizer.reset();
            tokenizer = null;
            sql = buffer;
        }
        charIndex = 0;
        queries = new LinkedList<Query>();
        for (BasicQuery query = getNextQuery(); query != null; query = getNextQuery()) {
            queries.add(query);
        }
    }

    public void addLineNoOffset(int originalLineNo, int numLines) {
    }

    public int adjustLineNo(int pLineNo) {
        return pLineNo;
    }

    public Iterator<Query> iterator() {
        return queries.iterator();
    }

    /**
   * Gets the next query, or returns null if there are no more
   * 
   * @return
   */
    private BasicQuery getNextQuery() {
        if (charIndex >= sql.length()) return null;
        int start = charIndex;
        int startOfLine = -1;
        char cQuote = 0;
        boolean inSLComment = false;
        boolean inMLComment = false;
        int startLineNo = -1;
        for (; charIndex < sql.length(); charIndex++) {
            char c = sql.charAt(charIndex);
            char nextC = 0;
            if (charIndex < sql.length() - 1) nextC = sql.charAt(charIndex + 1);
            if (inSLComment) {
                if (c == '\n') {
                    inSLComment = false;
                } else {
                    continue;
                }
            }
            if (inMLComment) {
                if (c == '\n') {
                    startOfLine = -1;
                    lineNo++;
                }
                inMLComment = !nextIs(mlCommentEnd);
                continue;
            }
            if (cQuote != 0) {
                if (c == quoteEscapes && QUOTE_CHARS.indexOf(nextC) > -1) charIndex++; else if (cQuote == c) cQuote = 0;
                continue;
            }
            if (start == charIndex && Character.isWhitespace(c)) {
                start++;
                if (c == '\n') {
                    startOfLine = -1;
                    lineNo++;
                }
                continue;
            }
            if (startOfLine < 0 && !Character.isWhitespace(c)) startOfLine = charIndex;
            if (cmdSeparator != null && nextIs(cmdSeparator)) {
                int oldCharIndex = charIndex;
                charIndex += cmdSeparator.length();
                if (startLineNo < 0 || start + cmdSeparator.length() >= oldCharIndex) {
                    start = charIndex;
                    startLineNo = -1;
                    startOfLine = -1;
                    continue;
                }
                CharSequence qry = sql.subSequence(start, oldCharIndex);
                Set<com.safi.workshop.sqlexplorer.parsers.QueryParameter> parms = null;
                try {
                    parms = parseArgs(qry.toString());
                } catch (ParserException e) {
                    e.printStackTrace();
                    MessageDialog.openError(SafiWorkshopEditorUtil.getActiveShell(), "Parse Param Error", "Error caught while parsing parameters: " + e.getLocalizedMessage());
                    return null;
                }
                BasicQuery bq = new BasicQuery(qry, startLineNo);
                bq.setQueryParameters((LinkedHashSet<com.safi.workshop.sqlexplorer.parsers.QueryParameter>) parms);
                return bq;
            }
            if (QUOTE_CHARS.indexOf(c) > -1) {
                cQuote = c;
                continue;
            }
            if (c == '\n') {
                if (rangeIs(startOfLine, charIndex, altSeparator)) {
                    if (startLineNo < 0 || start + altSeparator.length() >= startOfLine) {
                        start = charIndex + 1;
                        startLineNo = -1;
                        startOfLine = -1;
                        lineNo++;
                        continue;
                    }
                    CharSequence qry = sql.subSequence(start, startOfLine);
                    Set<com.safi.workshop.sqlexplorer.parsers.QueryParameter> parms = null;
                    try {
                        parms = parseArgs(qry.toString());
                    } catch (ParserException e) {
                        e.printStackTrace();
                        MessageDialog.openError(SafiWorkshopEditorUtil.getActiveShell(), "Parse Param Error", "Error caught while parsing parameters: " + e.getLocalizedMessage());
                        return null;
                    }
                    BasicQuery bq = new BasicQuery(qry, startLineNo);
                    bq.setQueryParameters((LinkedHashSet<com.safi.workshop.sqlexplorer.parsers.QueryParameter>) parms);
                    return bq;
                }
                if (startOfLine >= 0) {
                    String lineStart = sql.subSequence(startOfLine, Math.min(charIndex, startOfLine + 15)).toString().toLowerCase();
                    if (lineStart.startsWith("delimiter")) {
                        String delimiter = lineStart.substring(9).trim();
                        if (delimiter.length() == 1) {
                            setCmdSeparator(delimiter);
                            setAltSeparator(null);
                        } else {
                            setCmdSeparator(new String(new byte[] { 0 }));
                            setAltSeparator(delimiter);
                        }
                        if (startLineNo < 0 || start + lineStart.length() >= startOfLine) {
                            start = charIndex + 1;
                            startLineNo = -1;
                            startOfLine = -1;
                            lineNo++;
                            continue;
                        }
                        CharSequence qry = sql.subSequence(start, startOfLine);
                        Set<com.safi.workshop.sqlexplorer.parsers.QueryParameter> parms = null;
                        try {
                            parms = parseArgs(qry.toString());
                        } catch (ParserException e) {
                            e.printStackTrace();
                            MessageDialog.openError(SafiWorkshopEditorUtil.getActiveShell(), "Parse Param Error", "Error caught while parsing parameters: " + e.getLocalizedMessage());
                            return null;
                        }
                        BasicQuery bq = new BasicQuery(qry, startLineNo);
                        bq.setQueryParameters((LinkedHashSet<com.safi.workshop.sqlexplorer.parsers.QueryParameter>) parms);
                        return bq;
                    }
                }
                startOfLine = -1;
                lineNo++;
                if (inSLComment) {
                    inSLComment = false;
                    continue;
                }
            }
            if (nextIs(slComment)) {
                if (rangeIs(startOfLine, charIndex, altSeparator)) {
                    if (startLineNo < 0 || start + altSeparator.length() >= startOfLine) {
                        start = charIndex;
                        startLineNo = -1;
                        startOfLine = -1;
                        continue;
                    }
                    CharSequence qry = sql.subSequence(start, startOfLine);
                    Set<com.safi.workshop.sqlexplorer.parsers.QueryParameter> parms = null;
                    try {
                        parms = parseArgs(qry.toString());
                    } catch (ParserException e) {
                        e.printStackTrace();
                        MessageDialog.openError(SafiWorkshopEditorUtil.getActiveShell(), "Parse Param Error", "Error caught while parsing parameters: " + e.getLocalizedMessage());
                        return null;
                    }
                    BasicQuery bq = new BasicQuery(qry, startLineNo);
                    bq.setQueryParameters((LinkedHashSet<com.safi.workshop.sqlexplorer.parsers.QueryParameter>) parms);
                    return bq;
                }
                inSLComment = true;
                continue;
            }
            if (nextIs(mlCommentStart)) {
                if (rangeIs(startOfLine, charIndex, altSeparator)) {
                    if (startLineNo < 0 || start + altSeparator.length() >= startOfLine) {
                        start = charIndex;
                        startLineNo = -1;
                        startOfLine = -1;
                        continue;
                    }
                    CharSequence qry = sql.subSequence(start, startOfLine);
                    Set<com.safi.workshop.sqlexplorer.parsers.QueryParameter> parms = null;
                    try {
                        parms = parseArgs(qry.toString());
                    } catch (ParserException e) {
                        e.printStackTrace();
                        MessageDialog.openError(SafiWorkshopEditorUtil.getActiveShell(), "Parse Param Error", "Error caught while parsing parameters: " + e.getLocalizedMessage());
                        return null;
                    }
                    BasicQuery bq = new BasicQuery(qry, startLineNo);
                    bq.setQueryParameters((LinkedHashSet<com.safi.workshop.sqlexplorer.parsers.QueryParameter>) parms);
                    return bq;
                }
                inMLComment = true;
                continue;
            }
            if (startLineNo < 0 && !Character.isWhitespace(c)) startLineNo = lineNo;
        }
        if (start < charIndex) {
            if (rangeIs(startOfLine, charIndex, altSeparator)) {
                charIndex = sql.length();
                if (startLineNo < 0 || start + altSeparator.length() >= startOfLine) return null;
                CharSequence qry = sql.subSequence(start, startOfLine);
                Set<com.safi.workshop.sqlexplorer.parsers.QueryParameter> parms = null;
                try {
                    parms = parseArgs(qry.toString());
                } catch (ParserException e) {
                    e.printStackTrace();
                    MessageDialog.openError(SafiWorkshopEditorUtil.getActiveShell(), "Parse Param Error", "Error caught while parsing parameters: " + e.getLocalizedMessage());
                    return null;
                }
                BasicQuery bq = new BasicQuery(qry, startLineNo);
                bq.setQueryParameters((LinkedHashSet<com.safi.workshop.sqlexplorer.parsers.QueryParameter>) parms);
                return bq;
            }
            CharSequence qry = sql.subSequence(start, charIndex);
            Set<com.safi.workshop.sqlexplorer.parsers.QueryParameter> parms = null;
            try {
                parms = parseArgs(qry.toString());
            } catch (ParserException e) {
                e.printStackTrace();
                MessageDialog.openError(SafiWorkshopEditorUtil.getActiveShell(), "Parse Param Error", "Error caught while parsing parameters: " + e.getLocalizedMessage());
                return null;
            }
            BasicQuery bq = new BasicQuery(qry, startLineNo);
            bq.setQueryParameters((LinkedHashSet<com.safi.workshop.sqlexplorer.parsers.QueryParameter>) parms);
            return bq;
        }
        return null;
    }

    /**
   * Determines whether the next part of the SQL is a given string
   * 
   * @param str
   * @return
   */
    private boolean nextIs(String str) {
        if (str == null) return false;
        if (str.length() > sql.length() - charIndex) return false;
        CharSequence sub = sql.subSequence(charIndex, charIndex + str.length());
        return str.equals(sub.toString());
    }

    /**
   * Determines whether a string is exactly in a given range
   * 
   * @param start
   * @param end
   * @param str
   * @return
   */
    private boolean rangeIs(int start, int end, String str) {
        if (str == null || end - start != str.length()) return false;
        String sub = sql.subSequence(start, end).toString();
        return str.equalsIgnoreCase(sub);
    }

    /**
   * Gets a string from preferences, or null if the string is empty
   * 
   * @param prefs
   * @param id
   * @return
   */
    private String getPref(Preferences prefs, String id) {
        String str = prefs.getString(id);
        if (str == null) return null;
        str = str.trim();
        if (str.length() == 0) return null;
        return str;
    }

    private String getValue(String value) {
        if (value == null) return null;
        value = value.trim();
        if (value.length() == 0) return null;
        return value;
    }

    public String getAltSeparator() {
        return altSeparator;
    }

    public void setAltSeparator(String altSeparator) {
        this.altSeparator = getValue(altSeparator);
    }

    public String getCmdSeparator() {
        return cmdSeparator;
    }

    public void setCmdSeparator(String cmdSeparator) {
        this.cmdSeparator = getValue(cmdSeparator);
    }

    public String getMlCommentEnd() {
        return mlCommentEnd;
    }

    public void setMlCommentEnd(String mlCommentEnd) {
        this.mlCommentEnd = getValue(mlCommentEnd);
    }

    public String getMlCommentStart() {
        return mlCommentStart;
    }

    public void setMlCommentStart(String mlCommentStart) {
        this.mlCommentStart = getValue(mlCommentStart);
    }

    public char getQuoteEscapes() {
        return quoteEscapes;
    }

    public void setQuoteEscapes(char quoteEscapes) {
        this.quoteEscapes = quoteEscapes;
    }

    public void setQuoteEscapes(String quoteEscapes) {
        quoteEscapes = getValue(quoteEscapes);
        this.quoteEscapes = quoteEscapes == null ? 0 : quoteEscapes.charAt(0);
    }

    public String getSlComment() {
        return slComment;
    }

    public void setSlComment(String slComment) {
        this.slComment = getValue(slComment);
    }

    public static void updateParameters(com.safi.db.Query query) {
        java.util.regex.Pattern p = Pattern.compile(SqlexplorerViewConstants.PATT_PARAM);
        java.util.regex.Matcher m = p.matcher(query.getQuerySql());
        String param = null;
        int i = 0;
        EList<com.safi.db.QueryParameter> params = query.getParameters();
        while (m.find()) {
            param = m.group();
            if (params.size() > i) {
                com.safi.db.QueryParameter qm = params.get(i);
                if (!StringUtils.equals(qm.getName(), param)) {
                    com.safi.db.QueryParameter pm = DbFactory.eINSTANCE.createQueryParameter();
                    pm.setName(param);
                    params.set(i, pm);
                }
            } else {
                com.safi.db.QueryParameter pm = DbFactory.eINSTANCE.createQueryParameter();
                pm.setName(param);
                params.add(pm);
            }
            i++;
        }
        for (; i < params.size(); ) params.remove(i);
    }
}
