package com.safi.workshop.sqlexplorer.parsers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;
import com.safi.workshop.sqlexplorer.IConstants;
import com.safi.workshop.sqlexplorer.parsers.Tokenizer.Token;
import com.safi.workshop.sqlexplorer.parsers.scp.StructuredCommentParser;
import com.safi.workshop.sqlexplorer.plugin.SQLExplorerPlugin;
import com.safi.workshop.sqlexplorer.util.BackedCharSequence;

/**
 * Implements the foundations of a query parser; derived implementations are expected to
 * use their platform-specific knowledge of a language to break the SQL text into
 * individual queries.
 * 
 * @author John Spackman
 */
public abstract class AbstractSyntaxQueryParser extends AbstractQueryParser {

    private static final int MAX_PREVIOUS_TOKENS = 5;

    private static class LineNoOffset implements Comparable {

        public int lineNo;

        public int offset;

        public LineNoOffset(int lineNo, int offset) {
            super();
            this.lineNo = lineNo;
            this.offset = offset;
        }

        public int compareTo(Object o) {
            LineNoOffset that = (LineNoOffset) o;
            return lineNo - that.lineNo;
        }

        @Override
        public boolean equals(Object obj) {
            LineNoOffset that = (LineNoOffset) obj;
            return that.lineNo == lineNo;
        }

        @Override
        public String toString() {
            return "lineNo=" + lineNo + ", offset=" + offset;
        }
    }

    private StringBuffer buffer;

    private Tokenizer tokenizer;

    private int tokenNumber;

    private LinkedList<Tokenizer.Token> previousTokens = new LinkedList<Tokenizer.Token>();

    private LinkedList<Tokenizer.Token> futureTokens = new LinkedList<Tokenizer.Token>();

    private Tokenizer.Token currentToken;

    private LinkedList<Query> queries = new LinkedList<Query>();

    private boolean enableStructuredComments;

    private SortedSet<LineNoOffset> lineNoOffsets = new TreeSet<LineNoOffset>();

    /**
   * Constructor, initialises the parser/tokenizer with <code>sql</code>.
   * 
   * @param sql
   */
    public AbstractSyntaxQueryParser(CharSequence sql) {
        this(sql, SQLExplorerPlugin.getDefault().getPreferenceStore().getBoolean(IConstants.ENABLE_STRUCTURED_COMMENTS));
    }

    /**
   * Constructor, initialises the parser/tokenizer with <code>sql</code>.
   * 
   * @param sql
   * @param enableStructuredComments
   */
    public AbstractSyntaxQueryParser(CharSequence sql, boolean enableStructuredComments) {
        super();
        this.enableStructuredComments = enableStructuredComments;
        if (enableStructuredComments) {
            buffer = new StringBuffer(sql);
            tokenizer = new Tokenizer(buffer);
        } else {
            tokenizer = new Tokenizer(sql);
        }
    }

    public void parse() throws ParserException {
        if (enableStructuredComments) {
            StructuredCommentParser preprocessor = new StructuredCommentParser(this, buffer);
            Token token;
            tokenizer.reset();
            while ((token = tokenizer.nextToken()) != null) {
                if (token.getTokenType() == Tokenizer.TokenType.EOL_COMMENT || token.getTokenType() == Tokenizer.TokenType.ML_COMMENT) {
                    preprocessor.addComment(token);
                }
            }
            preprocessor.process();
            tokenizer.reset();
        }
        parseQueries();
        tokenizer = null;
    }

    /**
   * Parses the text into a series of queries
   */
    protected abstract void parseQueries() throws ParserException;

    public void addLineNoOffset(int originalLineNo, int numLines) {
        lineNoOffsets.add(new LineNoOffset(originalLineNo, numLines));
    }

    public int adjustLineNo(int lineNo) {
        for (LineNoOffset offset : lineNoOffsets) {
            if (lineNo <= offset.lineNo) return lineNo;
            if (offset.offset > 0) {
                if (lineNo >= offset.lineNo && lineNo < offset.lineNo + offset.offset) return offset.lineNo;
            }
            lineNo -= offset.offset;
        }
        return lineNo;
    }

    /**
   * Skips to the first token on the next line
   */
    protected void skipToEndOfLine() throws ParserException {
        int lineNo = currentToken.getLineNo();
        while (nextToken() != null && currentToken.getLineNo() == lineNo) ;
    }

    /**
   * Adds a query, taking the text between the two tokens inclusively, IE the query starts
   * with the first character of start and end with the last character of end.
   * 
   * @param start
   * @param end
   */
    protected void addQuery(Tokenizer.Token start, Tokenizer.Token end) {
        AnnotatedQuery query;
        if (end != null) query = newQueryInstance(start.outerSequence(end), start.getLineNo()); else query = newQueryInstance(start.superSequence(start.getStart()), start.getLineNo());
        HashMap<String, NamedParameter> map = new HashMap<String, NamedParameter>();
        for (NamedParameter param : getParameters()) if (param.getComment().getStart() <= query.getQuerySql().getStart()) map.put(param.getName(), param);
        if (!map.isEmpty()) query.setParameters(map);
        queries.add(query);
    }

    /**
   * Instantiates a new Query object
   * 
   * @param buffer
   * @param lineNo
   * @return
   */
    protected AnnotatedQuery newQueryInstance(BackedCharSequence buffer, int lineNo) {
        return new AnnotatedQuery(buffer, lineNo);
    }

    /**
   * Returns the next token
   * 
   * @return
   */
    protected Tokenizer.Token nextToken() throws ParserException {
        return nextToken(true);
    }

    /**
   * Returns the next token but only trims the history if trimPrevious is true; this is so
   * that lookAhead() can work easily without loosing the current value
   * 
   * @param trimPrevious
   * @return
   */
    private Tokenizer.Token nextToken(boolean trimPrevious) throws ParserException {
        if (currentToken != null) {
            previousTokens.add(currentToken);
            if (trimPrevious && previousTokens.size() > MAX_PREVIOUS_TOKENS) previousTokens.removeFirst();
        }
        if (!futureTokens.isEmpty()) currentToken = futureTokens.removeFirst(); else currentToken = tokenizer.nextToken();
        tokenNumber++;
        return currentToken;
    }

    /**
   * Un-gets the token, and changes the current token to be the previous
   */
    protected void ungetToken() {
        if (tokenNumber > 0 && previousTokens.isEmpty()) throw new IllegalStateException("Cannot unget because there are not enough previous tokens");
        tokenNumber--;
        if (currentToken == null) throw new IllegalStateException("No token to unget");
        futureTokens.add(currentToken);
        if (previousTokens.size() > 0) currentToken = previousTokens.removeLast();
    }

    /**
   * Looks ahead a given number of places; returns null if there are not enough tokens. A
   * distance of 1 is the next token, but leaves the current token as it is
   * 
   * @param distance
   * @return
   */
    protected Tokenizer.Token lookAhead(int distance) throws ParserException {
        if (futureTokens.size() <= distance) return futureTokens.get(distance - 1);
        Tokenizer.Token futureToken = null;
        for (int i = 0; i < distance; i++) {
            futureToken = nextToken(false);
            if (futureToken == null) {
                distance = i;
                break;
            }
        }
        while (distance > 0) {
            ungetToken();
            distance--;
        }
        return futureToken;
    }

    /**
   * Returns the last token we saw
   * 
   * @return
   */
    protected Tokenizer.Token lastToken() {
        return lastToken(1);
    }

    /**
   * Returns the token distance times back (distance of 1 is the last token)
   * 
   * @param distance
   * @return
   */
    protected Tokenizer.Token lastToken(int distance) {
        if (previousTokens.size() < distance) throw new IllegalArgumentException();
        return previousTokens.get(previousTokens.size() - distance);
    }

    /**
   * @return the currentToken
   */
    public Tokenizer.Token getCurrentToken() {
        return currentToken;
    }

    public Iterator<Query> iterator() {
        return queries.iterator();
    }

    /**
   * Sets the line number of the first line in the query
   * 
   * @param initialLineNo
   */
    public void setInitialLineNo(int initialLineNo) {
        if (tokenizer != null) tokenizer.setInitialLineNo(initialLineNo);
    }
}
