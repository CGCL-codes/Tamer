package att.grappa;

import java.io.*;
import java.util.*;
import java_cup.runtime.*;

/**
* This class provides a parser for the <i>dot</i> graph representation format.
* It is used in conjunction with JavaCup, a yacc-like parser generator
* originally by:
* <p>
* <center>
* <a href="http://www.cc.gatech.edu/gvu/people/Faculty/Scott.E.Hudson.html">Scott E. Hudson</a><br>
* Graphics Visualization and Usability Center<br>
* Georgia Institute of Technology<br>
* </center>
* </p>
* and more recently modified and maintained by
* <a href="http://www.cs.princeton.edu/~appel/modern/java/CUP/">
* a number of people at Princeton University</a>.
*
* @version 1.2, 04 Mar 2008; Copyright 1996 - 2008 by AT&T Corp.
* @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
*/
public class Parser extends java_cup.runtime.lr_parser {

    /** Default constructor. */
    public Parser() {
        super();
    }

    /** Constructor which sets the default scanner. */
    public Parser(java_cup.runtime.Scanner s) {
        super(s);
    }

    /** Production table. */
    protected static final short _production_table[][] = unpackFromStrings(new String[] { "\000\077\000\002\002\004\000\002\042\002\000\002\013" + "\005\000\002\013\003\000\002\013\002\000\002\014\005" + "\000\002\014\004\000\002\014\004\000\002\003\003\000" + "\002\003\002\000\002\004\003\000\002\004\003\000\002" + "\010\003\000\002\010\002\000\002\015\005\000\002\016" + "\003\000\002\016\002\000\002\017\004\000\002\017\003" + "\000\002\020\004\000\002\020\004\000\002\024\005\000" + "\002\025\003\000\002\025\003\000\002\041\003\000\002" + "\041\003\000\002\043\002\000\002\005\006\000\002\005" + "\002\000\002\027\003\000\002\027\005\000\002\031\004" + "\000\002\012\004\000\002\012\002\000\002\021\005\000" + "\002\021\003\000\002\006\003\000\002\006\003\000\002" + "\006\003\000\002\011\004\000\002\011\002\000\002\026" + "\003\000\002\026\002\000\002\032\006\000\002\034\003" + "\000\002\034\002\000\002\035\003\000\002\035\005\000" + "\002\036\003\000\002\036\003\000\002\037\005\000\002" + "\040\004\000\002\033\003\000\002\044\002\000\002\030" + "\005\000\002\007\004\000\002\007\003\000\002\007\002" + "\000\002\022\003\000\002\022\002\000\002\023\003\000" + "\002\023\003\000\002\023\002" });

    /** Access to production table. */
    public short[][] production_table() {
        return _production_table;
    }

    /** Parse-action table. */
    protected static final short[][] _action_table = unpackFromStrings(new String[] { "\000\121\000\020\002�\003\010\004￸\012\007\013" + "￸\014\004\015\005\001\002\000\006\020￴\027\120" + "\001\002\000\006\020￴\027\120\001\002\000\006\004" + "\117\013\116\001\002\000\006\004￹\013￹\001\002" + "\000\004\002￾\001\002\000\004\002\114\001\002\000" + "\004\020\000\001\002\000\004\020\015\001\002\000\004" + "\002￿\001\002\000\020\004\026\005\037\006\025\007" + "\016\020￈\021￱\027\022\001\002\000\006\020￉" + "\027\113\001\002\000\022\004ￍ\005ￍ\006ￍ\007" + "ￍ\016ￍ\020ￍ\021ￍ\027ￍ\001\002\000\022" + "\004￞\005￞\006￞\007￞\016￞\020￞\021" + "￞\027￞\001\002\000\004\021\112\001\002\000\036" + "\004￠\005￠\006￠\007￠\010￠\011￠\016" + "￠\017￠\020￠\021￠\022￠\024\063\025\071" + "\027￠\001\002\000\030\004￪\005￪\006￪\007" + "￪\010￪\011￪\016￪\020￪\021￪\022￪" + "\027￪\001\002\000\004\020ￌ\001\002\000\006\022" + "ￛ\027ￛ\001\002\000\006\022￝\027￝\001\002" + "\000\032\004￤\005￤\006￤\007￤\010￤\011" + "￤\016￤\017￤\020￤\021￤\022￤\027￤" + "\001\002\000\022\004ￆ\005ￆ\006ￆ\007ￆ\016" + "\074\020ￆ\021ￆ\027ￆ\001\002\000\030\004￥" + "\005￥\006￥\007￥\010\101\011\077\016￥\020" + "￥\021￥\022￥\027￥\001\002\000\022\004ￆ" + "\005ￆ\006ￆ\007ￆ\016\074\020ￆ\021ￆ\027" + "ￆ\001\002\000\032\004￫\005￫\006￫\007￫" + "\010￫\011￫\016￫\017\066\020￫\021￫\022" + "￫\027￫\001\002\000\020\004￯\005￯\006￯" + "\007￯\020￯\021￯\027￯\001\002\000\006\022" + "￙\027\041\001\002\000\020\004\026\005\037\006\025" + "\007\016\020￈\021￲\027\022\001\002\000\006\022" + "ￜ\027ￜ\001\002\000\020\004￰\005￰\006￰" + "\007￰\020￰\021￰\027￰\001\002\000\004\024" + "\065\001\002\000\004\022ￗ\001\002\000\004\022\045" + "\001\002\000\024\004￟\005￟\006￟\007￟\016" + "￟\020￟\021￟\022￘\027￟\001\002\000\010" + "\023ￔ\026\051\027\046\001\002\000\004\024\063\001" + "\002\000\014\016\060\017\057\023ￕ\026ￃ\027ￃ" + "\001\002\000\014\016￑\017￑\023￑\026￑\027" + "￑\001\002\000\004\027\056\001\002\000\014\016ￓ" + "\017ￓ\023ￓ\026ￓ\027ￓ\001\002\000\014\016" + "￐\017￐\023￐\026￐\027￐\001\002\000\004" + "\023\055\001\002\000\024\004ￖ\005ￖ\006ￖ\007" + "ￖ\016ￖ\020ￖ\021ￖ\022ￖ\027ￖ\001\002" + "\000\014\016ￎ\017ￎ\023ￎ\026ￎ\027ￎ\001" + "\002\000\006\026ￄ\027ￄ\001\002\000\006\026ￅ" + "\027ￅ\001\002\000\006\026\051\027\046\001\002\000" + "\014\016ￒ\017ￒ\023ￒ\026ￒ\027ￒ\001\002" + "\000\004\027\064\001\002\000\030\004ￏ\005ￏ\006" + "ￏ\007ￏ\016ￏ\017ￏ\020ￏ\021ￏ\023ￏ" + "\026ￏ\027ￏ\001\002\000\004\022ￚ\001\002\000" + "\004\027\067\001\002\000\034\004￠\005￠\006￠" + "\007￠\010￠\011￠\016￠\017￠\020￠\021" + "￠\022￠\025\071\027￠\001\002\000\032\004￣" + "\005￣\006￣\007￣\010￣\011￣\016￣\017" + "￣\020￣\021￣\022￣\027￣\001\002\000\004" + "\027\073\001\002\000\032\004￢\005￢\006￢\007" + "￢\010￢\011￢\016￢\017￢\020￢\021￢" + "\022￢\027￢\001\002\000\032\004￡\005￡\006" + "￡\007￡\010￡\011￡\016￡\017￡\020￡" + "\021￡\022￡\027￡\001\002\000\020\004ￇ\005" + "ￇ\006ￇ\007ￇ\020ￇ\021ￇ\027ￇ\001\002" + "\000\020\004￭\005￭\006￭\007￭\020￭\021" + "￭\027￭\001\002\000\010\007￧\020￧\027￧" + "\001\002\000\010\007￨\020￨\027￨\001\002\000" + "\024\004ￗ\005ￗ\006ￗ\007ￗ\016ￗ\020ￗ" + "\021ￗ\022ￗ\027ￗ\001\002\000\010\007￩\020" + "￩\027￩\001\002\000\024\004￬\005￬\006￬" + "\007￬\016￬\020￬\021￬\022\045\027￬\001" + "\002\000\024\004￘\005￘\006￘\007￘\016￘" + "\020￘\021￘\022￘\027￘\001\002\000\010\007" + "\016\020￈\027\067\001\002\000\030\004￥\005￥" + "\006￥\007￥\010\101\011\077\016￥\020￥\021" + "￥\022￥\027￥\001\002\000\024\004￦\005￦" + "\006￦\007￦\016￦\020￦\021￦\022￦\027" + "￦\001\002\000\020\004￮\005￮\006￮\007￮" + "\020￮\021￮\027￮\001\002\000\004\020\015\001" + "\002\000\030\004ￋ\005ￋ\006ￋ\007ￋ\010ￋ" + "\011ￋ\016ￋ\020ￋ\021ￋ\022ￋ\027ￋ\001" + "\002\000\032\002￳\004￳\005￳\006￳\007￳" + "\010￳\011￳\016￳\020￳\021￳\022￳\027" + "￳\001\002\000\004\020ￊ\001\002\000\004\002\001" + "\001\002\000\006\020￴\027\120\001\002\000\006\020" + "￶\027￶\001\002\000\006\020￷\027￷\001\002" + "\000\004\020￵\001\002\000\004\020￼\001\002\000" + "\004\020￺\001\002\000\004\020￻\001\002" });

    /** Access to parse-action table. */
    public short[][] action_table() {
        return _action_table;
    }

    /** <code>reduce_goto</code> table. */
    protected static final short[][] _reduce_table = unpackFromStrings(new String[] { "\000\121\000\010\003\005\013\010\014\011\001\001\000" + "\004\010\122\001\001\000\004\010\121\001\001\000\004" + "\004\114\001\001\000\002\001\001\000\002\001\001\000" + "\002\001\001\000\004\042\012\001\001\000\004\015\013" + "\001\001\000\002\001\001\000\034\006\034\007\023\016" + "\020\017\035\020\033\021\027\024\031\025\030\027\032" + "\030\022\031\026\033\017\037\016\001\001\000\002\001" + "\001\000\002\001\001\000\002\001\001\000\002\001\001" + "\000\004\012\071\001\001\000\002\001\001\000\004\044" + "\107\001\001\000\002\001\001\000\002\001\001\000\002" + "\001\001\000\004\022\106\001\001\000\006\005\077\041" + "\075\001\001\000\004\022\074\001\001\000\002\001\001" + "\000\002\001\001\000\004\011\041\001\001\000\030\006" + "\034\007\023\020\037\021\027\024\031\025\030\027\032" + "\030\022\031\026\033\017\037\016\001\001\000\002\001" + "\001\000\002\001\001\000\002\001\001\000\006\026\042" + "\032\043\001\001\000\002\001\001\000\002\001\001\000" + "\014\034\053\035\046\036\051\037\047\040\052\001\001" + "\000\002\001\001\000\004\023\060\001\001\000\002\001" + "\001\000\002\001\001\000\002\001\001\000\002\001\001" + "\000\002\001\001\000\002\001\001\000\002\001\001\000" + "\002\001\001\000\002\001\001\000\010\036\061\037\047" + "\040\052\001\001\000\002\001\001\000\002\001\001\000" + "\002\001\001\000\002\001\001\000\004\031\067\001\001" + "\000\004\012\071\001\001\000\002\001\001\000\002\001" + "\001\000\002\001\001\000\002\001\001\000\002\001\001" + "\000\002\001\001\000\004\043\103\001\001\000\002\001" + "\001\000\006\026\101\032\102\001\001\000\002\001\001" + "\000\002\001\001\000\002\001\001\000\014\007\023\025" + "\104\027\032\030\022\031\026\001\001\000\006\005\105" + "\041\075\001\001\000\002\001\001\000\002\001\001\000" + "\004\015\110\001\001\000\002\001\001\000\002\001\001" + "\000\002\001\001\000\002\001\001\000\004\010\120\001" + "\001\000\002\001\001\000\002\001\001\000\002\001\001" + "\000\002\001\001\000\002\001\001\000\002\001\001" });

    /** Access to <code>reduce_goto</code> table. */
    public short[][] reduce_table() {
        return _reduce_table;
    }

    /** Instance of action encapsulation class. */
    protected CUP$Parser$actions action_obj;

    /** Action encapsulation object initializer. */
    protected void init_actions() {
        action_obj = new CUP$Parser$actions(this);
    }

    /** Invoke a user supplied parse action. */
    public java_cup.runtime.Symbol do_action(int act_num, java_cup.runtime.lr_parser parser, java.util.Stack stack, int top) throws java.lang.Exception {
        return action_obj.CUP$Parser$do_action(act_num, parser, stack, top);
    }

    /** Indicates start state. */
    public int start_state() {
        return 0;
    }

    /** Indicates start production. */
    public int start_production() {
        return 0;
    }

    /** <code>EOF</code> Symbol index. */
    public int EOF_sym() {
        return 0;
    }

    /** <code>error</code> Symbol index. */
    public int error_sym() {
        return 1;
    }

    /** User initialization code. */
    public void user_init() throws java.lang.Exception {
        lexer.init();
        action_obj.graph = theGraph;
    }

    /** Scan to get the next Symbol. */
    public java_cup.runtime.Symbol scan() throws java.lang.Exception {
        return lexer.next_token(debugLevel);
    }

    private Graph theGraph = null;

    private Reader inReader;

    private PrintWriter errWriter;

    private Lexer lexer;

    private int debugLevel = 0;

    /**
   * Create an instance of <code>Parser</code> with input, error output and
   * a supplied <code>Graph</code> object.  The graph object is cleared (reset) before
   * new graph components are added to it by this parsing operation.
   *
   * @param inputReader input <code>Reader</code> object
   * @param errorWriter error output <code>Writer</code> object (or null to suppress error output)
   * @param graph <code>Graph</code> object for storing parsed graph information (or null to create a new object)
   */
    public Parser(Reader inputReader, PrintWriter errorWriter, Graph graph) {
        super();
        inReader = inputReader;
        errWriter = errorWriter;
        theGraph = graph;
        lexer = new Lexer(inputReader, errorWriter);
    }

    /**
   * A convenience constructor equivalent to <code>Parser(inputReader,errorWriter,null)</code>.
   *
   * @param inputReader input <code>Reader</code> object
   * @param errorWriter error output <code>Writer</code> object (or null to suppress error output)
   */
    public Parser(Reader inputReader, PrintWriter errorWriter) {
        this(inputReader, errorWriter, null);
    }

    /**
   * A convenience constructor equivalent to <code>Parser(inputReader,null,null)</code>.
   *
   * @param inputReader input <code>Reader</code> object
   */
    public Parser(Reader inputReader) {
        this(inputReader, (PrintWriter) null, null);
    }

    /**
   * Create an instance of <code>Parser</code> with input, error output and
   * a supplied <code>Graph</code> object.  The input stream is converted to
   * a <code>Reader</code> and the error stream is converted to a <code>Writer</code>.
   *
   * @param inputStream input <code>InputStream</code> object
   * @param errorStream error output <code>OutputStream</code> object (or null to suppress error output)
   * @param graph <code>Graph</code> object for storing parsed graph information (or null to create a new object)
   */
    public Parser(InputStream inputStream, OutputStream errorStream, Graph graph) {
        this(new InputStreamReader(inputStream), new PrintWriter(errorStream, true), graph);
    }

    /**
   * A convenience constructor equivalent to <code>Parser(inputStream,errorStream,null)</code>.
   *
   * @param inputStream input <code>InputStream</code> object
   * @param errorStream error output <code>OutputStream</code> object
   */
    public Parser(InputStream inputStream, OutputStream errorStream) {
        this(new InputStreamReader(inputStream), new PrintWriter(errorStream, true), null);
    }

    /**
   * A convenience constructor equivalent to <code>Parser(inputStream,null,null)</code>.
   *
   * @param inputStream input <code>InputStream</code> object
   */
    public Parser(InputStream inputStream) {
        this(new InputStreamReader(inputStream), (PrintWriter) null, null);
    }

    /**
   * Get the <code>Lexer</code> object associated with this parser.
   *
   * @return the associated lexical analyzer.
   */
    public Lexer getLexer() {
        return lexer;
    }

    /**
   * Get the error writer, if any, for this parser.
   *
   * @return the error writer for this parser.
   */
    public PrintWriter getErrorWriter() {
        return (errWriter);
    }

    /**
   * Get the debug level for this parser.
   * The debug level is set to a non-zero value by calling <code>debug_parse</code>.
   *
   * @return the debug level of this parser.
   * @see Parser#debug_parse(int)
   */
    public int getDebugLevel() {
        return (debugLevel);
    }

    /**
   * Report a fatal error.
   * Calling this method will throw a <code>GraphParserException</code>.
   *
   * @param message the error message to send to the error stream and include in the thrown exception
   * @param info not used
   *
   * @exception GraphParserException whenver this method is called
   */
    public void report_error(String message, Object info) throws GraphParserException {
        String loc = getLexer().getLocation();
        if (errWriter != null) {
            errWriter.println("ERROR: Parser" + loc + ": " + message);
        }
        throw new GraphParserException("at " + loc + ": " + message);
    }

    /**
   * Report a non-fatal error.
   *
   * @param message the warning message to send to the error stream, if the stream non-null.
   * @param info not used
   */
    public void report_warning(String message, Object info) {
        String loc = getLexer().getLocation();
        if (errWriter != null) {
            errWriter.println("WARNING: Parser" + loc + ": " + message);
        }
    }

    /**
   * Write a debugging message to the error stream.
   * The debug level of the message is 5.
   *
   * @param message the debug message to send to the error stream, if the stream non-null.
   * @see Parser#debug_message(int,String)
   */
    public void debug_message(String message) {
        debug_message(5, message);
    }

    /**
   * Write a debugging message to the error stream.
   * A message is written only if the error stream is not null and the
   * debug level of the message is greater than or equal to the debugging
   * level of the parser.
   *
   * @param level the level of the message
   * @param message the debug message to send to the error stream, if the stream non-null.
   * @see Parser#getDebugLevel()
   */
    public void debug_message(int level, String message) {
        if (debugLevel < level) {
            return;
        }
        String loc = getLexer().getLocation();
        if (errWriter != null) {
            errWriter.println("DEBUG: Parser" + loc + ": " + message);
        }
    }

    /**
   * Invokes the parser in debug mode.
   * The lowering the debug level reduces the amount of debugging output.
   * A level of 0 inhibits all debugging messages, generally a level of 10
   * will let all messages get through.
   *
   * @param debug the debug level to use for filtering debug messages based on priority. 
   * @exception Exception if <code>parse()</code> does
   */
    public Symbol debug_parse(int debug) throws java.lang.Exception {
        if (debug == 0) {
            return parse();
        }
        debugLevel = debug;
        int act;
        Symbol lhs_sym = null;
        short handle_size, lhs_sym_num;
        production_tab = production_table();
        action_tab = action_table();
        reduce_tab = reduce_table();
        debug_message(5, "# Initializing parser");
        init_actions();
        user_init();
        cur_token = scan();
        debug_message(5, "# Current Symbol is #" + cur_token.sym);
        stack.push(new Symbol(0, start_state()));
        tos = 0;
        for (_done_parsing = false; !_done_parsing; ) {
            act = get_action(((Symbol) stack.peek()).parse_state, cur_token.sym);
            if (act > 0) {
                cur_token.parse_state = act - 1;
                debug_shift(cur_token);
                stack.push(cur_token);
                tos++;
                cur_token = scan();
                debug_message(5, "# Current token is #" + cur_token.sym);
            } else if (act < 0) {
                lhs_sym = do_action((-act) - 1, this, stack, tos);
                lhs_sym_num = production_tab[(-act) - 1][0];
                handle_size = production_tab[(-act) - 1][1];
                debug_reduce((-act) - 1, lhs_sym_num, handle_size);
                for (int i = 0; i < handle_size; i++) {
                    stack.pop();
                    tos--;
                }
                act = get_reduce(((Symbol) stack.peek()).parse_state, lhs_sym_num);
                lhs_sym.parse_state = act;
                stack.push(lhs_sym);
                tos++;
                debug_message(5, "# Goto state #" + act);
            } else if (act == 0) {
                syntax_error(cur_token);
                if (!error_recovery(true)) {
                    unrecovered_syntax_error(cur_token);
                    done_parsing();
                } else {
                    lhs_sym = (Symbol) stack.peek();
                }
            }
        }
        return lhs_sym;
    }

    CUP$Parser$actions getActionObject() {
        return action_obj;
    }

    /**
   * Get the graph resulting from the parsing operations.
   *
   * @return the graph generated from the input.
   */
    public Graph getGraph() {
        return action_obj.graph;
    }
}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$Parser$actions {

    Subgraph rootSubgraph;

    Subgraph lastSubgraph;

    Graph graph;

    Subgraph thisGraph;

    Node thisNode;

    Edge thisEdge;

    Node fromNode;

    Node toNode;

    String portName = null;

    String toPortName;

    String fromPortName;

    int thisAttrType;

    int thisElemType;

    boolean directed = true;

    String graphType;

    private int anon_id = 0;

    Vector attrs = new Vector(8, 4);

    Vector nodes = new Vector(8, 4);

    Vector edges = new Vector(8, 4);

    void appendAttr(String name, String value) {
        attrs.addElement(new Attribute(thisElemType, name, value));
    }

    void noMacros() {
        parser.report_error("attribute macros are not supported yet", null);
    }

    void attrStmt(int kind, String macroName) {
        if (macroName != null) {
            noMacros();
            return;
        }
        if (attrs.size() == 0) return;
        Attribute attr = null;
        for (int i = 0; i < attrs.size(); i++) {
            if ((attr = (Attribute) (attrs.elementAt(i))).getValue() == null) {
                continue;
            } else {
                switch(kind) {
                    case Grappa.NODE:
                        parser.debug_message(1, "adding node default attr (" + attr.getName() + ") to thisGraph(" + thisGraph.getName() + ")");
                        thisGraph.setNodeAttribute(attr);
                        break;
                    case Grappa.EDGE:
                        parser.debug_message(1, "adding edge default attr (" + attr.getName() + ") to thisGraph(" + thisGraph.getName() + ")");
                        thisGraph.setEdgeAttribute(attr);
                        break;
                    case Grappa.SUBGRAPH:
                        parser.debug_message(1, "adding subg default attr (" + attr.getName() + ") to thisGraph(" + thisGraph.getName() + ")");
                        thisGraph.setAttribute(attr);
                        break;
                }
            }
        }
        attrs.removeAllElements();
    }

    void startGraph(String name, boolean type, boolean strict) {
        if (graph == null) {
            graph = new Graph(name, type, strict);
        } else {
            graph.reset(name, type, strict);
        }
        directed = type;
        rootSubgraph = (Subgraph) graph;
        parser.debug_message(1, "Creating top level graph (" + name + ")");
        anon_id = 0;
    }

    void openGraph() {
        thisGraph = rootSubgraph;
        thisElemType = Grappa.SUBGRAPH;
        parser.debug_message(1, "thisGraph(" + thisGraph.getName() + ")");
    }

    void closeGraph() {
        int level = 1;
        if (parser.getErrorWriter() != null && parser.getDebugLevel() >= level) {
            parser.debug_message(level, "parsed graph follows:");
            rootSubgraph.printSubgraph(parser.getErrorWriter());
        }
    }

    void openSubg(String name) {
        thisGraph = new Subgraph(thisGraph, name);
        parser.debug_message(1, "thisGraph(" + thisGraph.getName() + ")");
        thisElemType = Grappa.SUBGRAPH;
    }

    String anonStr() {
        return Grappa.ANONYMOUS_PREFIX + anon_id++;
    }

    void closeSubg() {
        lastSubgraph = thisGraph;
        thisGraph = thisGraph.getSubgraph();
        if (thisGraph == null) {
            parser.report_error("parser attempted to go above root Subgraph", null);
            thisGraph = rootSubgraph;
        }
        parser.debug_message(1, "Created subgraph (" + lastSubgraph.getName() + ") in subgraph (" + thisGraph.getName() + ")...");
        parser.debug_message(1, "thisGraph(" + thisGraph.getName() + ")");
    }

    void appendNode(String name, String port) {
        if ((thisNode = rootSubgraph.findNodeByName(name)) == null) {
            parser.debug_message(1, "Creating node in subgraph (" + thisGraph.getName() + ")...");
            thisNode = new Node(thisGraph, name);
        } else {
            parser.debug_message(1, "Node already in subgraph (" + thisNode.getSubgraph().getName() + ")...");
        }
        Object[] pair = new Object[2];
        pair[0] = thisNode;
        pair[1] = port;
        nodes.addElement(pair);
        parser.debug_message(1, "thisNode(" + thisNode.getName() + ")");
        thisElemType = Grappa.NODE;
    }

    void nodeWrap() {
        Object[] pair = null;
        if (nodes.size() > 0 && attrs.size() > 0) {
            for (int i = 0; i < nodes.size(); i++) {
                pair = (Object[]) (nodes.elementAt(i));
                applyAttrs((Element) pair[0], null, null);
            }
        }
        attrs.removeAllElements();
        nodes.removeAllElements();
    }

    void bufferEdges() {
        Object[] pair = new Object[2];
        if (nodes.size() > 0) {
            pair[0] = nodes;
            nodes = new Vector(8, 4);
            pair[1] = new Boolean(true);
        } else if (lastSubgraph != null) {
            pair[0] = lastSubgraph;
            lastSubgraph = null;
            pair[1] = new Boolean(false);
        } else {
            parser.report_error("EDGE_OP without clear antecedent nodelist or subgraph", null);
            return;
        }
        edges.addElement(pair);
    }

    void edgeWrap() {
        bufferEdges();
        Attribute key = null;
        Attribute name = null;
        Attribute attr = null;
        int skip = -1;
        for (int i = 0; i < attrs.size(); i++) {
            attr = (Attribute) (attrs.elementAt(i));
            if (attr.getName().equals("key")) {
                key = attr;
                if (name != null) break;
            } else if (attr.getName().equals("__nAmE__")) {
                name = attr;
                if (key != null) break;
            }
        }
        Object[] tailPair = (Object[]) (edges.elementAt(0));
        Object[] headPair = null;
        for (int i = 1; i < edges.size(); i++) {
            headPair = (Object[]) (edges.elementAt(i));
            if (((Boolean) (tailPair[1])).booleanValue()) {
                Vector list = (Vector) (tailPair[0]);
                Object[] nodePair = null;
                for (int j = 0; j < list.size(); j++) {
                    nodePair = (Object[]) (list.elementAt(j));
                    edgeRHS((Node) (nodePair[0]), (String) (nodePair[1]), headPair, key, name);
                }
                list.removeAllElements();
            } else {
                Subgraph subg = (Subgraph) (tailPair[0]);
                Enumeration enm = subg.elements(Grappa.NODE);
                while (enm.hasMoreElements()) {
                    edgeRHS((Node) (enm.nextElement()), null, headPair, key, name);
                }
            }
            tailPair = headPair;
        }
        edges.removeAllElements();
        attrs.removeAllElements();
    }

    void edgeRHS(Node tail, String tailPort, Object[] headPair, Attribute keyAttr, Attribute nameAttr) {
        String key = (keyAttr == null) ? null : keyAttr.getStringValue();
        String name = (nameAttr == null) ? null : nameAttr.getStringValue();
        if (((Boolean) (headPair[1])).booleanValue()) {
            Vector list = (Vector) (headPair[0]);
            Object[] nodePair = null;
            for (int j = 0; j < list.size(); j++) {
                nodePair = (Object[]) (list.elementAt(j));
                thisEdge = new Edge(thisGraph, tail, tailPort, (Node) (nodePair[0]), (String) (nodePair[1]), key, name);
                parser.debug_message(1, "Creating edge in subgraph (" + thisGraph.getName() + ")...");
                parser.debug_message(1, "thisEdge(" + thisEdge.getName() + ")");
                thisElemType = Grappa.EDGE;
                applyAttrs((Element) thisEdge, keyAttr, nameAttr);
            }
        } else {
            Subgraph subg = (Subgraph) (headPair[0]);
            Enumeration enm = subg.elements(Grappa.NODE);
            while (enm.hasMoreElements()) {
                thisEdge = new Edge(thisGraph, tail, tailPort, (Node) (enm.nextElement()), null, key, name);
                parser.debug_message(1, "Creating edge in subgraph (" + thisGraph.getName() + ")...");
                parser.debug_message(1, "thisEdge(" + thisEdge.getName() + ")");
                thisElemType = Grappa.EDGE;
                applyAttrs((Element) thisEdge, keyAttr, nameAttr);
            }
        }
    }

    void applyAttrs(Element elem, Attribute skip1, Attribute skip2) {
        Attribute attr = null;
        for (int i = 0; i < attrs.size(); i++) {
            attr = (Attribute) attrs.elementAt(i);
            if (attr == skip1) continue; else if (attr == skip2) continue;
            elem.setAttribute(attr);
        }
    }

    private final Parser parser;

    /** Constructor */
    CUP$Parser$actions(Parser parser) {
        this.parser = parser;
    }

    /** Method with the actual generated action code. */
    public final java_cup.runtime.Symbol CUP$Parser$do_action(int CUP$Parser$act_num, java_cup.runtime.lr_parser CUP$Parser$parser, java.util.Stack CUP$Parser$stack, int CUP$Parser$top) throws java.lang.Exception {
        java_cup.runtime.Symbol CUP$Parser$result;
        switch(CUP$Parser$act_num) {
            case 62:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(17, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 61:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(17, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 60:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(17, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 59:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(16, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 58:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(16, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 57:
                {
                    String RESULT = null;
                    RESULT = anonStr();
                    CUP$Parser$result = new java_cup.runtime.Symbol(5, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 56:
                {
                    String RESULT = null;
                    RESULT = anonStr();
                    CUP$Parser$result = new java_cup.runtime.Symbol(5, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 55:
                {
                    String RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String val = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    RESULT = val;
                    CUP$Parser$result = new java_cup.runtime.Symbol(5, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 54:
                {
                    Object RESULT = null;
                    if (((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value != null) RESULT = (Object) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                    String val = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                    closeSubg();
                    CUP$Parser$result = new java_cup.runtime.Symbol(22, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 53:
                {
                    Object RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String val = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    openSubg(val);
                    CUP$Parser$result = new java_cup.runtime.Symbol(34, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 52:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(25, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 51:
                {
                    Object RESULT = null;
                    int nameleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int nameright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String name = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    appendAttr(name, null);
                    CUP$Parser$result = new java_cup.runtime.Symbol(30, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 50:
                {
                    Object RESULT = null;
                    int nameleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                    int nameright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                    String name = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                    int valueleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valueright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String value = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    appendAttr(name, value);
                    CUP$Parser$result = new java_cup.runtime.Symbol(29, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 49:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(28, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 48:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(28, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 47:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(27, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 46:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(27, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 45:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(26, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 44:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(26, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 43:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(24, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 42:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(20, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 41:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(20, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 40:
                {
                    String RESULT = null;
                    RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(7, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 39:
                {
                    String RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                    String val = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                    RESULT = val;
                    CUP$Parser$result = new java_cup.runtime.Symbol(7, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 38:
                {
                    Integer RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    Integer val = (Integer) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    RESULT = new Integer(Grappa.EDGE);
                    CUP$Parser$result = new java_cup.runtime.Symbol(4, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 37:
                {
                    Integer RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    Integer val = (Integer) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    RESULT = new Integer(Grappa.NODE);
                    CUP$Parser$result = new java_cup.runtime.Symbol(4, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 36:
                {
                    Integer RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    Integer val = (Integer) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    RESULT = new Integer(Grappa.SUBGRAPH);
                    CUP$Parser$result = new java_cup.runtime.Symbol(4, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 35:
                {
                    Object RESULT = null;
                    attrStmt(Grappa.SUBGRAPH, null);
                    CUP$Parser$result = new java_cup.runtime.Symbol(15, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 34:
                {
                    Object RESULT = null;
                    int typeleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                    int typeright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                    Integer type = (Integer) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                    int nameleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                    int nameright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                    String name = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                    attrStmt(type.intValue(), name);
                    CUP$Parser$result = new java_cup.runtime.Symbol(15, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 33:
                {
                    String RESULT = null;
                    RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(8, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 32:
                {
                    String RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String val = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    RESULT = val;
                    CUP$Parser$result = new java_cup.runtime.Symbol(8, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 31:
                {
                    Object RESULT = null;
                    int nameleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                    int nameright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                    String name = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                    int portleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int portright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String port = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    appendNode(name, port);
                    CUP$Parser$result = new java_cup.runtime.Symbol(23, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 30:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(21, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 29:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(21, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 28:
                {
                    Boolean RESULT = null;
                    thisElemType = Grappa.NODE;
                    RESULT = new Boolean(false);
                    CUP$Parser$result = new java_cup.runtime.Symbol(3, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 27:
                {
                    Boolean RESULT = null;
                    if (((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value != null) RESULT = (Boolean) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    Boolean val = (Boolean) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    thisElemType = Grappa.EDGE;
                    RESULT = new Boolean(true);
                    CUP$Parser$result = new java_cup.runtime.Symbol(3, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 26:
                {
                    Object RESULT = null;
                    thisElemType = Grappa.EDGE;
                    bufferEdges();
                    CUP$Parser$result = new java_cup.runtime.Symbol(33, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 25:
                {
                    Object RESULT = null;
                    if (directed) {
                        parser.report_error("attempt to create a non-directed edge in a directed graph", null);
                    }
                    CUP$Parser$result = new java_cup.runtime.Symbol(31, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 24:
                {
                    Object RESULT = null;
                    if (!directed) {
                        parser.report_error("attempt to create a directed edge in a non-directed graph", null);
                    }
                    CUP$Parser$result = new java_cup.runtime.Symbol(31, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 23:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(19, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 22:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(19, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 21:
                {
                    Object RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                    Boolean val = (Boolean) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                    if (val.booleanValue()) edgeWrap(); else nodeWrap();
                    CUP$Parser$result = new java_cup.runtime.Symbol(18, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 20:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(14, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 19:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(14, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 18:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(13, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 17:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(13, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 16:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(12, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 15:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(12, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 14:
                {
                    Object RESULT = null;
                    CUP$Parser$result = new java_cup.runtime.Symbol(11, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 13:
                {
                    String RESULT = null;
                    RESULT = anonStr();
                    CUP$Parser$result = new java_cup.runtime.Symbol(6, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 12:
                {
                    String RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String val = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    RESULT = val;
                    CUP$Parser$result = new java_cup.runtime.Symbol(6, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 11:
                {
                    Boolean RESULT = null;
                    RESULT = new Boolean(true);
                    CUP$Parser$result = new java_cup.runtime.Symbol(2, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 10:
                {
                    Boolean RESULT = null;
                    RESULT = new Boolean(false);
                    CUP$Parser$result = new java_cup.runtime.Symbol(2, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 9:
                {
                    Boolean RESULT = null;
                    RESULT = new Boolean(false);
                    CUP$Parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 8:
                {
                    Boolean RESULT = null;
                    RESULT = new Boolean(true);
                    CUP$Parser$result = new java_cup.runtime.Symbol(1, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 7:
                {
                    Object RESULT = null;
                    int nameleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int nameright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String name = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    startGraph(name, true, true);
                    CUP$Parser$result = new java_cup.runtime.Symbol(10, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 6:
                {
                    Object RESULT = null;
                    int nameleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int nameright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String name = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    startGraph(name, true, false);
                    CUP$Parser$result = new java_cup.runtime.Symbol(10, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 5:
                {
                    Object RESULT = null;
                    int strictleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
                    int strictright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
                    Boolean strict = (Boolean) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
                    int typeleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                    int typeright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                    Boolean type = (Boolean) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                    int nameleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int nameright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    String name = (String) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    startGraph(name, type.booleanValue(), strict.booleanValue());
                    CUP$Parser$result = new java_cup.runtime.Symbol(10, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 4:
                {
                    Object RESULT = null;
                    graph = new Graph("empty");
                    ((Parser) (parser)).report_warning("The graph to parse is empty.", null);
                    CUP$Parser$result = new java_cup.runtime.Symbol(9, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 3:
                {
                    Object RESULT = null;
                    int valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
                    int valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
                    Object val = (Object) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
                    parser.report_error("An error was encountered while graph parsing (" + val.toString() + ").", null);
                    CUP$Parser$result = new java_cup.runtime.Symbol(9, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 2:
                {
                    Object RESULT = null;
                    if (((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value != null) RESULT = (Object) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                    closeGraph();
                    CUP$Parser$result = new java_cup.runtime.Symbol(9, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 1:
                {
                    Object RESULT = null;
                    openGraph();
                    CUP$Parser$result = new java_cup.runtime.Symbol(32, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                return CUP$Parser$result;
            case 0:
                {
                    Object RESULT = null;
                    int start_valleft = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
                    int start_valright = ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
                    Object start_val = (Object) ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
                    RESULT = start_val;
                    CUP$Parser$result = new java_cup.runtime.Symbol(0, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
                }
                CUP$Parser$parser.done_parsing();
                return CUP$Parser$result;
            default:
                throw new Exception("Invalid action number found in internal parse table");
        }
    }
}
