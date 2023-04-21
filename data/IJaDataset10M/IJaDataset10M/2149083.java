package org.fgraph.io;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.progeeks.util.*;
import org.progeeks.util.log.Log;
import org.fgraph.*;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.sql.SqlStoreFactory;
import org.fgraph.tstore.sql.ProfileStats;
import org.fgraph.util.JdbcConnections;

/**
 *
 *  @version   $Revision: 572 $
 *  @author    Paul Speed
 */
public class NQuadsLoader {

    static Log log = Log.getLog();

    public static final String EDGE_TYPE = "rdf:arc";

    public static final String CONTEXT_EDGE_TYPE = "rdf:context";

    public static final String TEXT_EDGE_TYPE = "rdf:languageText";

    public static final String NODE_ID = "rdf:about";

    public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static final String NQ_NODE_TYPE = RDF_NS + "type";

    private FGraph graph;

    private boolean lookupNodes = false;

    private Map<String, Node> iriCache = new HashMap<String, Node>();

    private long lineCount = 0;

    /**
     *  Creates a new loader that will add N-quads triple to the
     *  specified graph.  If lookupNodes is true then the graph
     *  is checked for existing rdf nodes.  In the case where a
     *  new graph is being filled or if it is known that there are
     *  no existing matching nodes then setting lookupNodes to false
     *  will be faster.
     */
    public NQuadsLoader(FGraph graph, boolean lookupNodes) {
        this.graph = graph;
    }

    protected Node getNode(String id, String nodeType, boolean setId) {
        Node result = iriCache.get(id);
        if (result != null) return result;
        if (lookupNodes) {
            for (GraphObject o : graph.search().findObjects(NODE_ID, id)) {
                Node n = (Node) o;
                if (result != null) {
                    log.warn("There are multiple nodes with " + NODE_ID + " = " + id);
                    break;
                } else {
                    result = n;
                }
            }
        }
        if (result == null) {
            result = graph.newNode();
            if (nodeType != null) result.add("nodeType", nodeType);
            if (setId) result.add("rdf:about", id);
            System.out.println("New node:" + id + "  count:" + iriCache.size() + "  lines:" + lineCount);
        } else {
            System.out.println("Loaded node:" + id + "  count:" + iriCache.size() + "  lines:" + lineCount);
        }
        iriCache.put(id, result);
        return result;
    }

    public void load(Reader r) throws IOException {
        BufferedReader in;
        if (r instanceof BufferedReader) in = (BufferedReader) r; else in = new BufferedReader(r, 32000);
        NqParser parser = new NqParser(in);
        Token[] triple = null;
        while ((triple = parser.readNextTriple()) != null) {
            Node start = null;
            lineCount = parser.getLineNumber();
            if (triple[0] instanceof UriRef) {
                start = getNode(triple[0].toString(), "rdf:node", true);
            } else if (triple[0] instanceof NodeId) {
                start = getNode(triple[0].toString(), "rdf:node", false);
            } else {
                throw new RuntimeException("Unknown subject type:" + triple[0] + "  " + triple[0].getClass());
            }
            if (!(triple[1] instanceof UriRef)) throw new RuntimeException("Unknown predicate type:" + triple[1] + "  " + triple[1].getClass());
            UriRef predicate = (UriRef) triple[1];
            if (NQ_NODE_TYPE.equals(triple[1].toString())) {
                System.out.println("Setting node type[" + triple[1] + " to:" + triple[2]);
                System.out.println("On node:" + start);
                start.put(triple[1].toString(), triple[2].toString());
                System.out.println("node properties:" + start.keySet());
                continue;
            }
            Node end = null;
            if (triple[2] instanceof UriRef) {
                end = getNode(triple[2].toString(), "rdf:node", true);
            } else if (triple[2] instanceof NodeId) {
                end = getNode(triple[2].toString(), "rdf:node", true);
            } else if (triple[2] instanceof Literal) {
                if (triple[2] instanceof StringLiteral) {
                    StringLiteral sl = (StringLiteral) triple[2];
                    String name = predicate.toString();
                    String language = sl.getLanguage();
                    if (language == null || language.equals("en")) {
                        start.put(name, sl.toString());
                        continue;
                    }
                    String existing = (String) start.get(name);
                    if (language.startsWith("en-") && (existing == null || existing.length() == 0)) {
                        if (existing == null) start.add(name, sl.toString()); else start.put(name, sl.toString());
                        continue;
                    }
                    if (existing == null) start.add(name, "");
                    Node fText = getNode(triple[0] + ":" + predicate, "rdf:textNode", true);
                    Edge back = fText.edge(TEXT_EDGE_TYPE, Direction.IN);
                    if (back == null) {
                        back = start.addEdge(fText, TEXT_EDGE_TYPE);
                        back.put("rdf:type", predicate);
                    } else {
                        if (!ObjectUtils.areEqual(start, back.otherEnd(fText))) throw new RuntimeException("Existing back edge from text node does not point to referred start node.");
                    }
                    System.out.println("Adding language reference[" + language + "] = " + sl.toString());
                    fText.put(language, sl.toString());
                    continue;
                } else if (triple[2] instanceof DataLiteral) {
                    DataLiteral dl = (DataLiteral) triple[2];
                    if (dl.getType() != null) System.out.println("**** Dropping type:" + dl.getType() + " for value[" + dl + "]");
                }
                start.put(predicate.toString(), triple[2].toString());
                continue;
            } else {
                throw new RuntimeException("Unknown subject type:" + triple[2] + "  " + triple[2].getClass());
            }
            Edge e = start.addEdge(end, predicate.toString());
            if (triple.length > 3) {
                Node context = getNode(triple[3].toString(), "rdf:node", true);
                Edge prov = e.addEdge(context, CONTEXT_EDGE_TYPE);
            }
        }
        System.out.println("Line count:" + parser.getLineNumber());
    }

    public static void main(String[] args) throws Exception {
        Log.initialize();
        Log.initialize();
        List<String> argList = new ArrayList<String>(Arrays.asList(args));
        Connection conn = JdbcConnections.getConnection(argList);
        conn.setAutoCommit(false);
        FGraph g = DefaultGraph.create(new SqlStoreFactory(conn));
        NanoTimer timer = new NanoTimer();
        timer.start();
        NQuadsLoader loader = new NQuadsLoader(g, false);
        for (int i = 3; i < args.length; i++) {
            loader.load(new FileReader(args[i]));
        }
        conn.commit();
        timer.stop();
        System.out.println("Total execution time:" + timer.getAccumulatedTimeMillis() + " ms");
        System.out.println("Break down:");
        ProfileStats.printStats();
        g.close();
    }

    protected static class Token {

        private String s;

        public Token(String s) {
            this.s = s;
        }

        public String toString() {
            return s;
        }
    }

    protected static class UriRef extends Token {

        public UriRef(String s) {
            super(s);
        }
    }

    protected static class NodeId extends Token {

        public NodeId(String s) {
            super(s);
        }
    }

    protected static class Literal extends Token {

        public Literal(String s) {
            super(s);
        }
    }

    protected static class StringLiteral extends Literal {

        private String language;

        public StringLiteral(String s, String language) {
            super(s);
            this.language = language;
        }

        public String getLanguage() {
            return language;
        }
    }

    protected static class DataLiteral extends Literal {

        private UriRef type;

        public DataLiteral(String s, UriRef type) {
            super(s);
            this.type = type;
        }

        public UriRef getType() {
            return type;
        }
    }

    /**
     *  Hand-coded implementation of the N-quads extension of this grammar:
     *  ntripleDoc      ::=     line*
     *  line            ::=     ws* ( comment | triple )? eoln
     *  comment         ::=     '#' ( character - ( cr | lf ) )*
     *  triple          ::=     subject ws+ predicate ws+ object ws* '.' ws*
     *  subject         ::=     uriref | nodeID
     *  predicate       ::=     uriref
     *  object          ::=     uriref | nodeID | literal
     *  uriref          ::=     '<' absoluteURI '>'
     *  nodeID          ::=     '_:' name
     *  literal         ::=     langString | datatypeString
     *  langString      ::=     '"' string '"' ( '@' language )?
     *  datatypeString  ::=     '"' string '"' '^^' uriref
     *  language        ::=     [a-z]+ ('-' [a-z0-9]+ )*
     *                          encoding a language tag.
     *  ws              ::=     space | tab
     *  eoln            ::=     cr | lf | cr lf
     *  space           ::=     #x20    US-ASCII space - decimal 32
     *  cr              ::=     #xD         US-ASCII carriage return - decimal 13
     *  lf              ::=     #xA         US-ASCII line feed - decimal 10
     *  tab             ::=     #x9     US-ASCII horizontal tab - decimal 9
     *  string          ::=     character* with escapes as defined in section Strings
     *  name            ::=     [A-Za-z][A-Za-z0-9]*
     *  absoluteURI     ::=     character+ with escapes as defined in section URI References
     *  character       ::=     [#x20-#x7E]  US-ASCII space to decimal 126
     *
     *  N-Quads extends this to replace triple with contextTriple as:
     *  contextTriple   ::=     subject ws+ predicate ws+ object ( ws+ context )? ws* '.' ws*
     *  context         ::=     uriref | nodeID | literal
     */
    protected static class NqParser {

        private Reader in;

        private int pushed = -1;

        private long line = 0;

        private int pos = 0;

        public NqParser(Reader in) {
            this.in = in;
        }

        public long getLineNumber() {
            return line;
        }

        public int getPosition() {
            return pos;
        }

        protected String position() {
            return "[" + (line + 1) + ":" + (pos + 1) + "]";
        }

        protected void error(String message) {
            throw new RuntimeException(message + position());
        }

        protected void assertMatch(int read, int expected) {
            if (read != expected) error("Expected '" + (char) expected + "' but found '" + (char) read + "' (" + read + ").");
        }

        protected void pushback(int c) {
            pushed = c;
        }

        protected boolean isWhitespace(int i) {
            return i == ' ' || i == '\t';
        }

        protected boolean isEol(int i) {
            return i == 0xd || i == 0xa;
        }

        protected int read() throws IOException {
            int i = in.read();
            if (i < 0) return i;
            if (i == 0x0d) {
                line++;
                pos = 0;
            } else if (i == 0x0a) {
                if (pos > 0) {
                    pos = 0;
                    line++;
                }
            } else {
                pos++;
            }
            return i;
        }

        /**
         *  Returns the next available character, skipping
         *  whitespace.
         */
        protected int nextChar() throws IOException {
            int i;
            if (pushed >= 0) {
                i = pushed;
                pushed = -1;
                if (!isWhitespace(i)) return i;
            }
            while ((i = read()) >= 0) {
                if (!isWhitespace(i)) {
                    return i;
                }
            }
            return -1;
        }

        protected int peek() throws IOException {
            if (pushed >= 0) return pushed;
            pushed = nextChar();
            return pushed;
        }

        protected char readUnicode() throws IOException {
            int result = 0;
            for (int i = 0; i < 4; i++) {
                int c = read();
                int d = Character.digit((char) c, 16);
                if (d < 0) error("Improperly formed Unicode sequence.");
                result = (result << 4) | d;
            }
            return (char) result;
        }

        protected UriRef readUriRef() throws IOException {
            int c = nextChar();
            assertMatch(c, '<');
            StringBuilder sb = new StringBuilder();
            while ((c = nextChar()) != '>') {
                if (c < 0) error("Unexpected end of file reading URI Ref.");
                if (c == '<') error("Unexpected start of new URI reference.");
                if (c == '\\') {
                    c = read();
                    if (c < 0) error("Improperly formed escape sequence.");
                    if (c == 'u') {
                        c = readUnicode();
                    } else {
                        error("Unsupported escape code:" + (char) c + " (" + c + ")");
                    }
                }
                sb.append((char) c);
            }
            return new UriRef(sb.toString());
        }

        protected NodeId readNodeId() throws IOException {
            int c = nextChar();
            assertMatch(c, '_');
            StringBuilder sb = new StringBuilder();
            while (!isWhitespace(c = read())) {
                if (c < 0) error("Unexpected end of file reading Node ID.");
                sb.append((char) c);
            }
            return new NodeId(sb.toString());
        }

        protected String readString() throws IOException {
            int c = nextChar();
            assertMatch(c, '"');
            StringBuilder sb = new StringBuilder();
            while ((c = read()) > 0) {
                if (c == '"') break;
                if (isEol(c)) error("String literal not terminated, end of line reached.");
                if (c == '\\') {
                    c = read();
                    if (c < 0) error("Improperly formed escape sequence.");
                    if (c == 'n') c = 0x0a; else if (c == 'r') c = 0x0d; else if (c == 't') c = 0x9; else if (c == 'u') c = readUnicode();
                    sb.append((char) c);
                } else {
                    sb.append((char) c);
                }
            }
            return sb.toString();
        }

        protected String readLanguage() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c = read();
            if (!Character.isLetter(c)) error("Invalid language char '" + (char) c + "'");
            sb.append((char) c);
            while ((c = read()) > 0) {
                if (c == '-' || Character.isLetter(c) || Character.isDigit(c)) {
                    sb.append((char) c);
                } else if (isWhitespace(c)) {
                    break;
                } else {
                    error("Invalid language char '" + (char) c + "' (" + c + ")");
                }
            }
            return sb.toString().toLowerCase();
        }

        protected Token readLiteral() throws IOException {
            String s = readString();
            int c = read();
            if (c == '@') return new StringLiteral(s, readLanguage());
            if (c == '^') {
                assertMatch(read(), '^');
                return new DataLiteral(s, readUriRef());
            }
            pushback(c);
            return new StringLiteral(s, null);
        }

        protected Token readSubject() throws IOException {
            int c = peek();
            if (c == '<') return readUriRef();
            if (c == '_') return readNodeId();
            error("Expected '<' or '_' but found '" + (char) c + "'");
            return null;
        }

        protected Token readPredicate() throws IOException {
            return readUriRef();
        }

        protected Token readObject() throws IOException {
            int c = peek();
            if (c == '<') return readUriRef();
            if (c == '_') return readNodeId();
            if (c == '"') return readLiteral();
            error("Expected '<', '_', or '\"' but found '" + (char) c + "'");
            return null;
        }

        protected Token readContext() throws IOException {
            return readObject();
        }

        protected Token[] readTriple() throws IOException {
            Token subject = readSubject();
            Token predicate = readPredicate();
            Token object = readObject();
            int c = nextChar();
            if (c == '.') return new Token[] { subject, predicate, object };
            if (isEol(c)) error("Missing line terminator.");
            pushback(c);
            Token context = readContext();
            c = nextChar();
            if (c == '.') return new Token[] { subject, predicate, object, context };
            error("Missing line terminator.");
            return null;
        }

        protected void readComment() throws IOException {
            int c = 0;
            while ((c = read()) >= 0) {
                if (isEol(c)) return;
            }
        }

        public Token[] readNextTriple() throws IOException {
            int c = 0;
            while ((c = peek()) >= 0) {
                if (c == '#') readComment(); else if (isEol(c)) nextChar(); else return readTriple();
            }
            return null;
        }
    }
}
