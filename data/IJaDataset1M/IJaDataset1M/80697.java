package org.fernwood.jbasic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;
import net.wimpi.telnetd.TelnetD;
import org.fernwood.jbasic.compiler.Expression;
import org.fernwood.jbasic.compiler.PatternOptimizer;
import org.fernwood.jbasic.compiler.Tokenizer;
import org.fernwood.jbasic.runtime.JBFInput;
import org.fernwood.jbasic.runtime.JBFOutput;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.runtime.JBasicFile;
import org.fernwood.jbasic.runtime.JBasicQueue;
import org.fernwood.jbasic.runtime.JBasicSignal;
import org.fernwood.jbasic.runtime.JBasicThread;
import org.fernwood.jbasic.runtime.RandomNumberGenerator;
import org.fernwood.jbasic.runtime.SymbolTable;
import org.fernwood.jbasic.runtime.UserManager;
import org.fernwood.jbasic.statements.Statement;
import org.fernwood.jbasic.value.ObjectValue;
import org.fernwood.jbasic.value.Value;

/**
 * This class is the session object for the JBasic language processor. 
 * JBasic is an implementation of the BASIC programming language, with
 * some extensions to make it easier to use as an embedded Java class.
 * Language and internals documentation can be found in the <em>JBasic
 * User's Guide</em>, a PDF of which is part of the Eclipse project 
 * workspace, or can be located in the project repository at 
 * <code>http://sf.net/projects/jbasic</code>.<p>
 * 
 * The session object is the master container for a JBasic session. 
 * There can be more than one session object in existence at a time,
 * though this only happens in the case where JBasic is used as an 
 * embedded language tool inside another program.
 * <p>
 *
 * In the case of line mode execution, a single instance of this 
 * object is created by the JBasicMain class which hosts the 
 * command-line interface.
 * 
 * @author Tom Cole
 * @version version 2.9 December 2010
 * 
 */
public class JBasic {

    /**
	 * Default file extension for programs we load from the file 
	 * system. The LOAD and RUN commands will attempt to append
	 * this extension if the name provided by the user/caller 
	 * cannot be opened as supplied.  For example, LOAD "myprogram"
	 * will first try to open myprogram and if that fails, 
	 * will try to open myprogram.jbasic
	 */
    public static final String DEFAULTEXTENSION = ".jbasic";

    /**
	 * Name space prefixes for function program types. This is 
	 * appended to the start of the name and prevents unwanted
	 * collisions between function, test, and program names, 
	 * for example.
	 */
    public static final String FUNCTION = "FUNC$";

    /**
	 * Name space prefixes for program types. This is appended
	 * to the start of the name and prevents unwanted collisions 
	 * between function, test, and program names, for example.
	 * 
	 * AT THIS POINT, it is unsafe to set PROGRAM to something 
	 * other than an empty string, since there are some cases 
	 * where it may not be correctly prefixed at invocation or 
	 * user program reference.
	 */
    public static final String PROGRAM = "";

    /**
	 * Name space prefixes for TEST program types. This is 
	 * appended to the start of the name and prevents unwanted
	 * collisions between function, test, and program names, 
	 * for example.
	 */
    public static final String TEST = "TEST$";

    /**
	 * Name space prefixes for VERB program types. This is 
	 * appended to the start of the name and prevents unwanted 
	 * collisions between function, test, and program names, 
	 * for example.
	 */
    public static final String VERB = "VERB$";

    /**
	 * This string is the prefix for any NEW program created 
	 * without a name. This prefix is given, followed by a 
	 * unique number.
	 */
    public static final String NEWPREFIX = "UNNAMED_";

    /**
	 * This string prefix is added to numerical file 
	 * identifiers. So a reference to file #3 is converted 
	 * to "__FILE_3", for example.
	 */
    public static final String FILEPREFIX = "__FILE_";

    /**
	 * This is the maximum number of line numbers that can be 
	 * <em>referenced</em> in a single statement, such as an 
	 * <code>IF..THEN..ELSE</code> compound statement.
	 */
    public static final int LINENUMBERSPERSTATEMENT = 10;

    /**
	 * This is the maximum depth of the call stack with or 
	 * without recursion.
	 */
    public static final int CALL_DEPTH_LIMIT = 200;

    /**
	 * This is the name of the default CONSOLE file, created
	 * for each thread and session.
	 */
    public static final String CONSOLE_NAME = "%TERMINAL%";

    /**
	 * The default name for the multi-user authentication
	 * and authorization database.
	 */
    public static final String USER_DATA = "JBasic-users.xml";

    /**
	 * Embed a copyright declaration in the class/jar file 
	 * directly.
	 */
    public static final String COPYRIGHT = "Copyright (c) 2003-2011 by Tom Cole, tomcole@users.sf.net";

    /**
	 * This is the name of the global variable that contains partial package
	 * paths, used to locate user-written statements, functions, and also by
	 * NEW() to resolve partial Java class names.
	 */
    public static final String PACKAGES = "SYS$PACKAGES";

    /**
	 * This is the line continuation character that can be used in external
	 * files being loaded into JBasic.  The subsequent line is added to the
	 * current line of text.  
	 */
    public static final char LINE_CONTINUATION_CHAR = '\\';

    /**
	 * This is the maximum width of a source line on a SAVE operation.  Lines
	 * greater than this width will be written as two or more lines using the
	 * LINE_CONTINUATION_CHAR character.  Lines will be broken at blanks or
	 * commas. 
	 */
    public static final int MAX_SOURCE_LINE_LENGTH = 132;

    /**
	 * This is the name of the GLOBAL symbol table.
	 */
    public static final String GLOBAL_TABLE_NAME = "GLOBAL";

    /**
	 * This is the name of the ROOT symbol table.
	 */
    public static final String ROOT_TABLE_NAME = "ROOT";

    /**
	 * This is the name of the CONSTANTS symbol table.
	 */
    public static final String CONSTANTS_TABLE_NAME = "CONSTANTS";

    /**
	 * This is the current version number, which is also 
	 * surfaced in the variable $VERSION in the JBasic 
	 * runtime environment.  This is updated by the 
	 * initialization process to include the build time of 
	 * the JBasic object in the jar file, so it 
	 * <em>cannot</em> be FINAL.
	 */
    public static String version = "2.9";

    /**
	 * Master abort flag, set by the control-C interrupt handler. 
	 * When a SIGINT is received (usually a control-c from the 
	 * console) then this flag is set to true. This is tested 
	 * periodically in the byte code execution engine to
	 * see if we should abort.
	 */
    public static boolean interruptSignalled = false;

    /**
	 * Default name for a workspace. This file is loaded from the
	 * user's home directory at startup if it exists.
	 */
    public static String defaultWSName = "Workspace.jbasic";

    /**
	 * Local copy of the newline character, used in various 
	 * print() and println() functions. A local copy is slightly
	 * faster to access than fetching the system property by 
	 * name each time.
	 */
    public static String newLine = System.getProperty("line.separator");

    /**
	 * This is the character that separates compound statements 
	 * from each other; i.e. the colon in this statement:<p>
	 * 
	 * <code>100 LET X = 3 : GOTO START</code>
	 * 
	 * Some dialects of BASIC use a backslash "\" character for 
	 * this separator.
	 */
    public static String compoundStatementSeparator = ":";

    /**
	 * This is the central list of QUEUE objects that are used to 
	 * communicate between threads, as a thread safe FIFO queue.  
	 * This list is available to the current session and to any 
	 * threads created by the <em>current</em> session.  Threads 
	 * created by other sessions (even parents of this thread)
	 * will not be able to share QUEUEs.
	 */
    public static TreeMap<String, JBasicQueue> queueList;

    /**
	 * An instance of an output file used for logging.  When null,
	 * there is no logging enabled.  This file will be created the
	 * first time logging is written.
	 */
    public static JBFOutput log;

    /**
	 * This is the root symbol table that is the top-most parent 
	 * of all symbol tables in all instances of JBasic in this 
	 * process. That is, any symbol placed in this table is 
	 * visible to any JBasic object on any thread. This is the 
	 * only symbol table shared across all instances of JBasic.
	 */
    public static SymbolTable rootTable = new SymbolTable(null, "Root", null);

    /**
	 * This is used to generate unique sequence numbers during 
	 * the life of this session object.
	 */
    private static int uniqueIDSequence;

    /**
	 * Random number generator instance.
	 */
    public static RandomNumberGenerator random;

    /**
	 * The user data handler for multiuser mode. 
	 */
    public static UserManager userManager;

    /**
	 * This is the root object for the telnet manager that 
	 * supports multiple user connections.  This is null 
	 * unless SERVER mode is started.
	 */
    public static TelnetD telnetDaemon;

    /**
	 * This properties object holds the descriptive information 
	 * about how to instantiate the telnet daemon.  This is 
	 * loaded once from a properties file the first time the 
	 * daemon is started.  From then on it is reused if the 
	 * daemon is shutdown and restarted.
	 */
    public static Properties telnetProperties;

    /**
	 * This is a list of all active sessions.  The key is a 
	 * string containing the session ID number.
	 */
    public static TreeMap<String, JBasic> activeSessions;

    /**
	 * This is the list of SERVER SOCKETS created by this process,
	 * which are used when a new instance of the SERVER SOCKET is
	 * opened... we must fetch the ServerSocket and do another
	 * accept() on it to get a client connection.
	 */
    private static TreeMap<String, ServerSocket> serverSocketList;

    /**
	 * This represents the first JBasic session created, which is typically
	 * the main or controlling session.  This is used in a few cases (such
	 * as printing out the ROOT table) where there is no default session
	 * and we attach to the first one used.
	 */
    public static JBasic firstSession;

    /**
	 * This is the global symbol table. This is the root of
	 * all other chains of tables, and contains values that 
	 * are available to all programs run in this session of 
	 * JBasic.
	 */
    private SymbolTable globals;

    /**
	 * This is the symbol table that holds "macro" values that
	 * are part of the text substitution operators <@ and @>
	 */
    public SymbolTable macroTable;

    /**
	 * This is the stack (LIFO arrayValue) of on_units. 
	 * These are statements and match values that are used to 
	 * determine if an error should result in the execution 
	 * of the ON statement. The match can be a specific error 
	 * or a catch-all for any error. The <code>ON</code> 
	 * statement is used to define them, and they are global 
	 * in scope.
	 */
    public JBasicSignal onStatementStack;

    /**
	 * Per-session abort flag, used to kill threads and such.
	 * This flag is set by the KILL THREAD command.  It is 
	 * polled at statement execution boundaries to see if 
	 * the thread should "voluntarily" stop running.
	 * 
	 */
    private boolean abort = false;

    /**
	 * ProgramManager object that tracks the stored programs 
	 * available to this session object.
	 */
    public ProgramManager programs;

    /**
	 * Character-based input stream for command, <code>INPUT
	 * </code>, and <code>LINE INPUT</code> processing.
	 */
    public JBasicFile stdin;

    /**
	 * Character-based output stream for console errors and 
	 * <code>PRINT</code> statement processing.
	 */
    public JBasicFile stdout;

    /**
	 * The name of the current object's workspace. This is 
	 * defaultWSName unless overridden with a SAVE WORKSPACE 
	 * command that changes the workspace for the current session.
	 */
    private String wsName = null;

    /**
	 * This is the list of user files that are currently being 
	 * accessed for input or output via an <code>OPEN</code> 
	 * statement. This is not the same as files that are opened 
	 * by the JBasic runtime itself, such as the help or Library 
	 * files. The key for accessing things in this TreeMap is
	 * the file identifier name.
	 */
    public HashMap<Integer, JBasicFile> openUserFiles;

    /**
	 * This is the status code from initialization; you can 
	 * check this after creating the JBasic object to see if 
	 * any intermediate startup errors occurred from within 
	 * the initialization. This is most useful when JBasic is 
	 * being used as a class within another program (as 
	 * opposed to the self-contained mode where JBasic runs 
	 * as a program itself).
	 */
    Status initStatus;

    /**
	 * Flag to indicate if the immediate-mode console input
	 * loop is running. When the loop should terminate (usually
	 * due to a QUIT command) this value becomes false.
	 */
    private boolean fConsoleRunning;

    /**
	 * This flag is set while loading the Library file(s). 
	 * It is off at all other times. This is used by the 
	 * program loader to set the fSystemObject flag in each
	 * program object loaded during initialization.
	 */
    private boolean fLoadingSystemObjects;

    /**
	 * Start time that the JBasic class was instantiated. 
	 * Used for time-stamping and supporting the seconds() 
	 * function.
	 */
    public long startTime;

    /**
	 * Counts the number of JBasic statements executed by 
	 * the current JBasic object session.
	 */
    public int statementsExecuted;

    /**
	 * Counts the number of ByteCode instructions executed 
	 * by the current JBasic session.
	 */
    public int instructionsExecuted;

    /**
	 * Count the number of statements executed that had no 
	 * compile() method and therefore had to be executed 
	 * interpretively.
	 */
    public int statementsInterpreted;

    /**
	 * Count of the number of statement compilations performed.
	 */
    public int statementsCompiled;

    /**
	 * Count of the number of compiled statements executed.
	 */
    public int statementsByteCodeExecuted;

    /**
	 * The name of this JBasic object. Used purely for 
	 * documentation and/or tracking purposes. This is used 
	 * with the instance number to form a unique identifier.
	 */
    private String instanceName;

    /**
	 * A unique identifying number for this instance of the
	 * JBasic object within the current process. The instance
	 * and name together are used to form a unique identifier.
	 */
    private int instanceID;

    /**
	 * List of child threads created from this instance of 
	 * JBASIC.  A thread can access the thread data for threads
	 * that it creates <b>itself</b> via this list.  Note that 
	 * threads created by other sessions or threads are not
	 * visible.
	 */
    private TreeMap<String, JBasicThread> childThreads;

    /**
	 * This flag is used to indicate if a prompt is needed at 
	 * the console.  In normal mode, this is always true and 
	 * a prompt is printed each time a command is entered.
	 * 
	 * If one is in NOPROMPT mode, then this prompt is only 
	 * printed when there is a termination of a running program.
	 */
    private boolean needPrompt = true;

    /**
	 * The user identity of the currently logged in user, or 
	 * null if we are not in multiuser mode.
	 */
    private User userIdentity;

    /**
	 * This is the instance of the logical name manager for 
	 * the <em>controlling</em> session that launched our session.
	 * This is normally null, unless we are in multiuser mode, in
	 * which case this points to the server master session.
	 */
    private LogicalNameManager namespace;

    /**
	 * Flag indicating if we are in "sandbox" mode.  This is 
	 * echoed in the global variable SYS$SANDBOX, but is here 
	 * for faster access during permissions checks.
	 */
    private boolean fSandbox;

    /**
	 * This is the message manager for this session, that tracks the
	 * bindings between signal names and message text for all languages
	 * supported.
	 */
    public MessageManager messageManager;

    /**
	 * If we are a child session of a parent (created by a SHELL or thread
	 * operation) this is the link to our parent's session.
	 */
    public JBasic parentSession;

    /**
	 * This is the shared instance of the pattern-based optimizer that is
	 * used to optimize bytecode buffers.
	 */
    public PatternOptimizer pmOptimizer;

    /**
	 * Default constructor for a JBasic session object. This
	 * constructor calls the shared initialization that is 
	 * also used when JBasic is run as a main program. The 
	 * session name is generated as <code>JBASIC_<em>nn</em>
	 * </code> where <em>nn</em> is a sequence number.
	 * <p>
	 * You can consult the variable initStatus in the object 
	 * to determine if initialization succeeded or not.
	 */
    public JBasic() {
        instanceID = JBasic.getUniqueID();
        programs = new ProgramManager();
        instanceName = "JBASIC_" + Integer.toString(instanceID);
        initStatus = initializeJBasic();
        if (initStatus.failed()) initStatus.print(this);
        enableSandbox(false);
    }

    /**
	 * Constructor for a JBasic child session object. This
	 * constructor calls the shared initialization that is 
	 * also used when JBasic is run as a main program. The 
	 * session name is generated as <code>JBASIC_<em>nn</em>
	 * </code> where <em>nn</em> is a sequence number.
	 * <p>
	 * You can consult the variable initStatus in the object 
	 * to determine if initialization succeeded or not.
	 * @param session the JBasic session that is the parent
	 * of this thread.
	 */
    public JBasic(JBasic session) {
        instanceID = JBasic.getUniqueID();
        parentSession = session;
        programs = new ProgramManager();
        instanceName = "JBASIC_" + Integer.toString(instanceID);
        initStatus = initializeJBasic();
        if (initStatus.failed()) initStatus.print(this);
        enableSandbox(false);
    }

    /**
	 * Default constructor for a JBasic session object, with 
	 * a caller-supplied name for the session. This constructor
	 * calls the shared initialization that is also used when 
	 * JBasic is run as a main program. You can consult the 
	 * variable initStatus in the object to determine if
	 * initialization succeeded or not.
	 * 
	 * @param theName the name to give to the new instance
	 */
    public JBasic(final String theName) {
        instanceID = JBasic.getUniqueID();
        instanceName = theName;
        programs = new ProgramManager();
        initStatus = initializeJBasic();
        if (initStatus.failed()) initStatus.print(this);
        enableSandbox(false);
    }

    /**
	 * This method is largely used for debugging. Given a 
	 * JBasic object, return a string representation of 
	 * the object, which is its instance name.
	 * 
	 * @return A string representation of this instance name.
	 */
    public String toString() {
        StringBuffer debugName = new StringBuffer();
        debugName.append("JBasic instance ");
        debugName.append(instanceName);
        debugName.append('[');
        debugName.append(Integer.toString(instanceID));
        debugName.append(']');
        return debugName.toString();
    }

    /**
	 * Generate a unique integer sequence number. This is 
	 * guaranteed to be unique across multiple instances of 
	 * JBasic sessions in a single program. The routine is 
	 * synchronized so only one caller at a time can access
	 * this, preventing the possibility of giving out the 
	 * same value twice.
	 * 
	 * @return Unique non-zero positive integer value.
	 */
    public static synchronized int getUniqueID() {
        JBasic.uniqueIDSequence++;
        return JBasic.uniqueIDSequence;
    }

    /**
	 * Retrieve the global symbol table for this instance 
	 * of JBasic. This is the symbol table that is accessible 
	 * to all programs or subroutines.
	 * 
	 * @return The SymbolTable object for the global table.
	 */
    public SymbolTable globals() {
        return globals;
    }

    /**
	 * Add a JBasic statement package to the list of packages
	 * searched for external language elements such as Statement
	 * or JBasicFunction objects. 
	 * 
	 * This list of package names is searched when calling 
	 * compiler or execution elements of statements or functions.
	 * 
	 * @param name
	 *            The fully qualified package name, such as 
	 *            org.mycompany.jbasic
	 * @throws JBasicException the package name is invalid or
	 * is not accessible
	 */
    public void addPackage(final String name) throws JBasicException {
        if (globals() == null) throw new JBasicException(Status.FAULT, "Global symbol table not set up before call to addPackage.");
        Value array = globals.findReference(JBasic.PACKAGES, false);
        if (array == null) {
            array = new Value(Value.ARRAY, JBasic.PACKAGES);
            globals.insert(JBasic.PACKAGES, array);
        }
        globals.markReadOnly(JBasic.PACKAGES);
        String localName = name;
        while (localName.endsWith(".")) {
            localName = localName.substring(0, name.length() - 1);
        }
        final int len = array.size();
        for (int ix = 1; ix <= len; ix++) {
            final Value oldItem = array.getElement(ix);
            if (oldItem.getType() != Value.STRING) continue;
            if (oldItem.getString().equals(localName)) return;
        }
        array.setElementOverride(new Value(localName), len + 1);
    }

    /**
	 * Given a suffix for a class name, search the list of 
	 * packages to find the fully qualified name for the 
	 * class object. 
	 * 
	 * @param suffix a String containing the suffix, such 
	 * as "RightFunction", which is searched for in all 
	 * the known packages.
	 * @return The fully qualified name, such as 
	 * "org.fernwood.jbasic.funcs.RightFunction"
	 * where the class was found, or a null string if not 
	 * found.
	 */
    public String findPackage(String suffix) {
        Value list = globals.localReference("SYS$PACKAGES");
        if (list == null) return null;
        for (int ix = 1; ix <= list.size(); ix++) {
            String className = list.getString(ix) + "." + suffix;
            Class c;
            try {
                c = Class.forName(className);
            } catch (ClassNotFoundException e) {
                continue;
            }
            if (c != null) return className;
        }
        return null;
    }

    /**
	 * Public method to run a single JBasic statement. This 
	 * statement can be the loading or invocation of a program, 
	 * using the currently existing runtime state of the JBasic
	 * environment.
	 * 
	 * @param cmd
	 *            String containing a single JBasic statement or 
	 *            command
	 * @return Status block indicating the success of the 
	 * operation. The status value has not been printed; use 
	 * the printError() method of the status block to print 
	 * information about the error if needed.
	 */
    public Status run(final String cmd) {
        final Statement s = new Statement(this);
        s.store(cmd);
        running(true);
        return s.execute(globals(), false);
    }

    /**
	 * Routine to initialize the default global symbol table 
	 * for this instance of JBasic. This is responsible for 
	 * creating the symbol table, and then loading in the 
	 * "predefined" system symbols. Some of these symbols are
	 * also marked read-only so the user's programs cannot 
	 * change the value (an example is <code>SYS$VERSION
	 * </code> which is the version number of
	 * JBasic).
	 * @throws JBasicException 
	 * 
	 */
    private void initializeGlobalSymbols() throws JBasicException {
        if (rootTable == null) rootTable = new SymbolTable(this, "Root", null);
        if (rootTable.size() == 0) {
            rootTable.insertSynchronized("$AUTHOR", new Value("Tom Cole"));
            rootTable.insertSynchronized("$VERSION", new Value(JBasic.version));
            rootTable.insertSynchronized("$COPYRIGHT", new Value(COPYRIGHT));
        }
        final SymbolTable constantsTable = new SymbolTable(this, "Constants", JBasic.rootTable);
        constantsTable.setReadOnly(true);
        constantsTable.insertReadOnly("INFINITY", new Value(Double.POSITIVE_INFINITY));
        constantsTable.insertReadOnly("PI", new Value(3.1415926535897927));
        globals = new SymbolTable(this, "Global", constantsTable);
        globals.setReadOnly(true);
        globals.insert("SYS$PREFIX_FUNCTION", JBasic.FUNCTION);
        globals.insert("SYS$PREFIX_VERB", JBasic.VERB);
        globals.insert("SYS$PREFIX_TEST", JBasic.TEST);
        globals.insert("SYS$USER", System.getProperty("user.name"));
        globals.insert("SYS$HOME", System.getProperty("user.home"));
        globals.insert("SYS$ROOTUSER", false);
        globals.insert("SYS$SANDBOX", false);
        globals.insert("SYS$CURRENT_PROGRAM", "");
        globals.insert("SYS$TRACE_STATEMENTS", false);
        globals.setReadOnly(false);
        globals.insert("SYS$SHELL_LEVEL", 0);
        globals.insert("SYS$ISTHREAD", false);
        globals.insert("SYS$INPUT_PROMPT", "? ");
        globals.insert("SYS$RETOKENIZE", true);
        globals.insert("SYS$DISASSEMBLE", false);
        globals.insert("SYS$TRACE_BYTECODE", false);
        globals.insert("SYS$OPTIMIZE", true);
        globals.insert("SYS$LANGUAGE", System.getProperty("user.language").toUpperCase());
        globals.insert("SYS$AUTOCOMMENT", true);
        globals.insert("SYS$TIME_GC", true);
        globals.insert("SYS$AUTORENUMBER", false);
        globals.insert("SYS$DEBUG_DEFAULTCMD", "STEP 1");
        globals.insert("SYS$DEBUG_PROMPT", "DBG> ");
        globals.insert("SYS$STATIC_TYPES", false);
        globals.insert("SYS$STRUCTURE_POOLING", false);
        globals.insert("SYS$CMDPROMPT", true);
        globals.insert("SYS$LABELWIDTH", 10);
        globals.insert("SYS$STATEMENT_TEXT", false);
        globals.insert("SYS$LOOP_OPT", false);
        globals.insert("SYS$SOURCE_LINE_LENGTH", 80);
        globals.insert("SYS$SQL_COMMANDS", false);
        globals.insert("SYS$SQL_OPT", true);
        globals.insert("SYS$SQL_DISASM", false);
        Value mQuotes = new Value(Value.ARRAY, null);
        mQuotes.addElement(new Value("<@"));
        mQuotes.addElement(new Value("@>"));
        globals.insert("SYS$MACRO_QUOTES", mQuotes);
        globals.insert("SYS$$FCACHE_HITS", 0);
        globals.insert("SYS$$FCACHE_TRIES", 0);
        globals.insert("SYS$$STATEMENTS", 0);
        globals.insert("SYS$$STATEMENTS_EXECUTED", 0);
        globals.insert("SYS$$STATEMENTS_COMPILED", 0);
        globals.insert("SYS$$STATEMENTS_INTERPRETED", 0);
        globals.insert("SYS$$INSTRUCTIONS_EXECUTED", 0);
        globals.insert("SYS$STATUS", new Value(new Status()));
        globals.insert("SYS$ALIASES", new Value(Value.RECORD, null));
        Value objectValue = new Value(Value.RECORD, "OBJECT");
        objectValue.setObjectAttribute("NAME", new Value("OBJECT"));
        objectValue.setObjectAttribute("ISCLASS", new Value(false));
        objectValue.setObjectAttribute("ID", new Value(JBasic.getUniqueID()));
        globals.parentTable.insertReadOnly("OBJECT", objectValue);
        objectValue = new Value(Value.RECORD, "CLASS");
        objectValue.setObjectAttribute("NAME", new Value("CLASS"));
        objectValue.setObjectAttribute("CLASS", new Value("OBJECT"));
        objectValue.setObjectAttribute("ISCLASS", new Value(true));
        objectValue.setObjectAttribute("ID", new Value(JBasic.getUniqueID()));
        globals.parentTable.insertReadOnly("CLASS", objectValue);
    }

    /**
	 * Method to create a default ABOUT program. This is used 
	 * by the interactive startup, but someone embedding JBasic 
	 * might not want this. Note that the program is created and 
	 * registered. By registering it, the program object is 
	 * retrievable later and we don't have to do anything else 
	 * with the 'aboutProgram' object.
	 * 
	 * @return an instance of the ABOUT program
	 */
    public Program initializeAboutProgram() {
        setLoadingSystemObjects(true);
        final Program aboutProgram = new Program(this, "$ABOUT");
        final String ptext[] = { "print \"JBASIC Version \"; $version; \", by \"; $author ", "msg = message(\"_GETHELP\")", "if msg = \"GETHELP\" then msg =  \"Type HELP for information on how to use JBASIC\"", "if permission(\"FILE_IO\") then print msg; \".\"", "print", "return" };
        final int i = ptext.length;
        for (int n = 0; n < i; n++) aboutProgram.add(ptext[n]);
        aboutProgram.renumber(100, 10);
        aboutProgram.register();
        aboutProgram.setSystemObject(true);
        setLoadingSystemObjects(false);
        return aboutProgram;
    }

    /**
	 * Shared initialization code between stand-alone and
	 * embedded versions of JBasic. This is called automatically
	 * by the JBasic constructor, and explicitly from the
	 * main() shell for stand-alone use.
	 * 
	 * @return A status object indicating if there were any 
	 * errors in initializing the runtime environment.
	 */
    Status initializeJBasic() {
        Status returnStatus = null;
        try {
            startTime = System.currentTimeMillis();
            running(true);
            if (queueList == null) queueList = new TreeMap<String, JBasicQueue>();
            onStatementStack = new JBasicSignal(JBasic.CALL_DEPTH_LIMIT);
            onStatementStack.fDebugSignals = false;
            final Version v = new Version();
            JBasic.version = v.getVersion();
            wsName = System.getProperty("user.home") + System.getProperty("file.separator") + JBasic.defaultWSName;
            initializeGlobalSymbols();
            globals.insertReadOnly("SYS$WORKSPACE", new Value(wsName));
            globals.insertReadOnly("SYS$INSTANCE_NAME", new Value(instanceName));
            globals.insertReadOnly("SYS$INSTANCE_ID", new Value(instanceID));
            globals.insertReadOnly("SYS$MODE", new Value("EMBEDDED"));
            globals.insertReadOnly("SYS$LOGIN_TIME", new Value(new Date().toString()));
            globals.insert("SYS$LOAD_LIST", new Value(Value.ARRAY, null));
            globals.markReadOnly("SYS$LOAD_LIST");
            Loader.addPath(this, "@");
            Loader.addPath(this, "@/");
            Loader.addPath(this, "@/org.fernwood.jbasic/");
            Loader.addPath(this, "");
            Value array = new Value(Value.ARRAY, "SYS$PROGRAMS");
            globals.insertReadOnly("SYS$PROGRAMS", array);
            addPackage("org.fernwood.jbasic.funcs");
            openUserFiles = new HashMap<Integer, JBasicFile>();
            stdin = new JBFInput(this);
            stdin.setIdentifier("CONSOLE_INPUT");
            stdin.open(new Value(JBasic.CONSOLE_NAME), null);
            stdin.setSystemFlag();
            stdout = new JBFOutput(this);
            stdout.setIdentifier("CONSOLE_OUTPUT");
            stdout.open(new Value(JBasic.CONSOLE_NAME), null);
            stdout.setSystemFlag();
            Value console = null;
            console = stdin.getFileID();
            console.setElement(new Value(true), "SYSTEM");
            globals().parentTable.insert("CONSOLE_INPUT", console);
            console = stdout.getFileID();
            console.setElement(new Value(true), "SYSTEM");
            globals().parentTable.insert("CONSOLE_OUTPUT", console);
            macroTable = new SymbolTable(this, "Macro Symbols", globals);
            messageManager = new MessageManager(this);
            globals.insert("SYS$STOP_MESSAGE", getMessage(Status._STOPMSG));
            globals.insert("SYS$SAVEPROMPT", getMessage(Status._SAVEPROMPT));
            globals.insert("SYS$OPT_DEADCODE", true);
            initializeLog();
            pmOptimizer = new PatternOptimizer(this);
            if (pmOptimizer.status.failed()) pmOptimizer.status.print(this);
            statementsExecuted = 0;
            setLoadingSystemObjects(true);
            returnStatus = initializeLibrary("Library");
            if (returnStatus.success()) returnStatus = initializeFunctions(this, "Functions");
            setLoadingSystemObjects(false);
            if (JBasic.userManager == null) JBasic.userManager = new UserManager(this, true);
        } catch (JBasicException e) {
            System.out.println("Failure to initialize JBasic");
            e.printStackTrace();
        } finally {
        }
        return returnStatus;
    }

    /**
	 * 
	 * Attempt to load the built-in library of code. This 
	 * file contains predefined system functions written in
	 * the JBasic language, which may include built-in verbs,
	 * etc.  This is also where the $MAIN and $PREFERENCES
	 * programs are normally stored.
	 * 
	 * @param libraryName
	 *            A String containing the name of the library. 
	 *            This is usually "Library", but could be 
	 *            something else in the future.
	 * 
	 * @return Status indicating that the operation succeeded 
	 * without error.
	 */
    Status initializeLibrary(final String libraryName) {
        Status status;
        status = Loader.loadFile(this, libraryName + JBasic.DEFAULTEXTENSION);
        if (status.failed() && !status.equals(Status.INFILE)) return status;
        try {
            Loader.pathLoad(this, libraryName);
        } catch (JBasicException e) {
            status = e.getStatus();
        }
        if (status.failed() && !status.equals(Status.INFILE)) return status;
        return new Status(Status.SUCCESS);
    }

    /**
	 * Initialize the functions written in JBasic, which are 
	 * typically stored in the Functions.jbasic file in the 
	 * jar or the home directory. This is called once during 
	 * initialization. It just uses the path loader to find
	 * the source file name given and load it's contents.
	 * 
	 * @param session
	 *            The JBasic object in which the functions 
	 *            are loaded.
	 * @param fname
	 *            The name of the source file
	 * @return A status object indicating if there were syntax
	 * errors in the loaded function(s).
	 */
    static Status initializeFunctions(final JBasic jbenv, final String fname) {
        try {
            return Loader.pathLoad(jbenv, fname);
        } catch (JBasicException e) {
            return new Status(Status.FAULT, "unable to initialize functions");
        }
    }

    /**
	 * Command line loop. Used by the main program, and 
	 * can also be used by threads wishing to do shell-like 
	 * console command input.
	 * 
	 * @param shellSymbols Symbol table to use as the default 
	 * symbol table for statements executed by this shell.  
	 * If null is passed, then a new symbol table is 
	 * automatically created that is a child of the global 
	 * table for this session.
	 * 
	 * @param pString The prompt string to use for this 
	 * command shell, or null if the default prompt string 
	 * is to be used.
	 * 
	 * @return Status indicating success or failure of command 
	 * loop
	 */
    public Status shell(final SymbolTable shellSymbols, final String pString) {
        SymbolTable consoleSymbols = shellSymbols;
        if (consoleSymbols == null) consoleSymbols = new SymbolTable(this, "Local To Shell", globals);
        String line = null;
        final boolean fSavedConsoleRunning = isRunning();
        boolean fDebugState = false;
        Status status = null;
        String oldPrompt = consoleSymbols.getString("SYS$PROMPT");
        if (pString != null) {
            try {
                consoleSymbols.insert("SYS$PROMPT", pString);
            } catch (JBasicException e2) {
                return e2.getStatus();
            }
        }
        final Statement s = new Statement(this);
        while (isRunning()) {
            if (stdin().hasReadAhead()) {
                stdout.println(getMessage("_UNUSEDBUFF"));
                try {
                    stdin().flushReadAhead();
                } catch (JBasicException e) {
                    e.printStackTrace();
                }
            }
            String p = consoleSymbols.getString("SYS$PROMPT");
            if (p == null) p = "BASIC> ";
            if (!consoleSymbols.getBoolean("SYS$CMDPROMPT")) p = "\nBASIC\n";
            final int l = consoleSymbols.getInteger("SYS$SHELL_LEVEL");
            if (l > 0) p = "[" + Integer.toString(l) + "] " + p;
            if (needsPrompt()) {
                stdout.print(p);
                setNeedPrompt(consoleSymbols.getBoolean("SYS$CMDPROMPT"));
            }
            line = stdin().read();
            if (line == null) {
                running(false);
                break;
            }
            s.program = null;
            s.store(line);
            if (s.status.printError(this)) continue;
            if (s.status.equals(Status.STMTADDED)) continue;
            fDebugState = globals().getBoolean("SYS$TRACE_STATEMENTS");
            try {
                status = s.execute(consoleSymbols, fDebugState);
                if (status.equals("*STEP")) status = new Status("STEP"); else if (status.equals(Status.QUIT)) running(false); else if (status.failed()) status.printError(this);
            } catch (final Exception e) {
                stdout.println("Java error: " + e);
            }
        }
        running(fSavedConsoleRunning);
        if (oldPrompt != null) try {
            consoleSymbols.insert("SYS$PROMPT", oldPrompt);
        } catch (JBasicException e) {
            return e.getStatus();
        }
        return status;
    }

    /**
	 * Return the instance ID string for this JBasic session. 
	 * This is used, among other things, to support returning
	 * the thread information from the THREAD commands.
	 * 
	 * @return a String containing the unique instance name.
	 */
    public String getInstanceID() {
        return globals.getString("SYS$INSTANCE_NAME");
    }

    /**
	 * Given a string expression (which can be as simple 
	 * as a single variable), return the expression result
	 * as a Value object.  This is most commonly used by 
	 * callers of embedded JBasic to return values of 
	 * variables and functions.
	 * <p>
	 * Tokenize the text given and pass the token stream 
	 * to the compiler's immediate expression evaluator, 
	 * which returns a result value.
	 * 
	 * @param text The text of the expression to execute.
	 * @return a Value object containing the result, or 
	 * null if the expression could not be successfully 
	 * evaluated.
	 */
    public Value expression(String text) {
        Expression exp = new Expression(this);
        Value result = exp.evaluate(new Tokenizer(text), globals);
        if (exp.status.printError(this)) return null;
        return result;
    }

    /**
	 * Set or clear "sandbox" mode.  When in sandbox mode, 
	 * user programs are not allowed to manipulate the file 
	 * system or execute native code.  This is meant to 
	 * prevent web-applications or other uses from being security
	 * risks.
	 * @param flag boolean flag indicating if sandbox mode is 
	 * enabled.
	 */
    public void enableSandbox(boolean flag) {
        fSandbox = flag;
        SymbolTable globals = globals();
        Value sandbox = globals.localReference("SYS$SANDBOX");
        if (sandbox == null) {
            try {
                sandbox = new Value(flag);
                globals.insertReadOnly("SYS$SANDBOX", sandbox);
                return;
            } catch (JBasicException e) {
                return;
            }
        }
        sandbox.setBoolean(flag);
    }

    /**
	 * Initialize the logging system, if needed.
	 */
    public void initializeLog() {
        if (log != null) return;
        log = new JBFOutput(this);
        try {
            log.open(new Value(CONSOLE_NAME), globals);
        } catch (JBasicException e) {
            e.printStackTrace();
        }
        log.setSystemFlag();
        Value v = null;
        try {
            v = globals().reference("SYS$LOGLEVEL");
        } catch (JBasicException e) {
            v = new Value(Value.INTEGER, null);
            try {
                globals().insertReadOnly("SYS$LOGLEVEL", v);
            } catch (JBasicException e1) {
                e1.printStackTrace();
            }
        }
        v.setInteger(1);
    }

    /**
	 * Indicate if we are running in "Sandbox" mode, which 
	 * means there is  little or no access to local system 
	 * resources.  This mode would be used in a web deployment
	 * or when in multiuser mode.
	 * 
	 * @return true if the current session is in "sandbox" mode.
	 */
    public boolean inSandbox() {
        return fSandbox;
    }

    /**
	 * Determine if the current user has permission to 
	 * perform a given operation. If sandbox mode is not 
	 * enabled, then everything is allowed. If the current 
	 * session is sandboxed but does not have an identity,
	 * then nothing is allowed.  Otherwise, use the 
	 * identity to check the permissions value.
	 * 
	 * @param permission the name of the permission
	 * @return true if the permission is granted, else false.
	 */
    public boolean hasPermission(String permission) {
        if (!inSandbox()) {
            if (this.userIdentity == null) return true;
            return userIdentity.hasPermission(permission);
        }
        if (getUserIdentity() == null) return false;
        return getUserIdentity().hasPermission(permission);
    }

    /**
	 * This is used to report something to the controlling
	 * session of the multiuser server. If there is no 
	 * server running, the message is just printed on 
	 * the console.
	 * @param string the message to report
	 */
    public void report(String string) {
        String stamp = new Date().toString();
        if (getUserIdentity() == null) {
            stdout.println("REPORT(" + stamp + ") " + string);
            return;
        }
        String msg = "REPORT(" + stamp + ", " + getUserIdentity().getName() + ") " + string;
        JBasic.userManager.getSession().stdout.println(msg);
    }

    /**
	 * Delete the current session from the list of active sessions.
	 */
    public void deleteInstance() {
        if (JBasic.activeSessions == null) return;
        String key = globals.getString("SYS$INSTANCE_ID");
        JBasic.activeSessions.remove(key);
    }

    /**
	 * Return an integer value from the session's global 
	 * symbol table.
	 * @param name the name of the symbol
	 * @return the integer value of the symbol
	 */
    public int getInteger(String name) {
        return globals.getInteger(name);
    }

    /**
	 * Return a string value from the session's global 
	 * symbol table.
	 * @param name the name of the symbol
	 * @return the string value of the symbol
	 */
    public String getString(String name) {
        if (globals == null) return null;
        return globals.getString(name);
    }

    /**
	 * Return a boolean value from the session's global 
	 * symbol table.
	 * @param name the name of the symbol
	 * @return the boolean value of the symbol
	 */
    public boolean getBoolean(String name) {
        return globals.getBoolean(name);
    }

    /**
	 * Called to generate an interrupt on the current session.
	 * 
	 */
    public void interrupt() {
        setAbort(true);
    }

    /**
	 * Set the current program for the session.  This 
	 * involves setting the global name SYS$CURRENT_PROGRAM. This
	 * routine cannot be called until after initializeGlobalSymbols()
	 * is run.
	 * @param programName the name to set.  If null, the name
	 * is set to an empty string.
	 */
    public void setCurrentProgramName(String programName) {
        Value cp = globals.findReference("SYS$CURRENT_PROGRAM", false);
        cp.setString(programName == null ? "" : programName);
        return;
    }

    /**
	 * Determine if the current program has the given permission.
	 * If not, throw a SANDBOX error.
	 * @param permission The named permission being tested
	 * @throws JBasicException indicating that the requested permission
	 * is not granted to the current session.
	 */
    public void checkPermission(String permission) throws JBasicException {
        if (!hasPermission(permission)) {
            log.permission(this, permission);
            throw new JBasicException(Status.SANDBOX, permission);
        }
    }

    /**
	 * For a given message code, get the appropriate current message
	 * string (including all needed localization).  This is most often
	 * used to get string constants from the message data.
	 * @param code The message code to fetch.
	 * @return a string containing the localized version of the 
	 * corresponding string.  If no string is available in the
	 * current localization, then the English (EN) version is 
	 * returned.
	 */
    public String getMessage(String code) {
        Status sts = new Status(code);
        return sts.getMessage(this);
    }

    /**
	 * When JBasic is used as an embedded program, you can pass an object
	 * to it via this method.  The object must have a name, and is stored
	 * in the global symbol table by that that name as a wrapper on a Java
	 * object.  The user can then manipulate the JBasic object from within
	 * JBasic programs.
	 * @param name The name of the object to store in the Global symbol table
	 * @param object The Java object to be so stored.
	 * @return A status indicating success; typically the only error that 
	 * would occur here is if there was already a read-only symbol of that
	 * name.
	 */
    public Status setObject(String name, Object object) {
        try {
            checkPermission(Permissions.JAVA);
            globals.insert(name.toUpperCase(), new ObjectValue(object));
        } catch (JBasicException e) {
            return e.getStatus();
        }
        return new Status();
    }

    /**
	 * Set the flag indicating if the console is still accepting command input
	 * @param runningState true if the program is still running, else
	 * false if a QUIT signal has been processed and we are shutting down.
	 */
    public void running(boolean runningState) {
        fConsoleRunning = runningState;
    }

    /**
	 * Return flag indicating if the console is still accepting command input
	 * @return true if the program is still running, else
	 * false if a QUIT signal has been processed and we are shutting down.
	 */
    public boolean isRunning() {
        return fConsoleRunning;
    }

    /**
	 * Set the flag indicating if a prompt is needed to be displayed to the
	 * user at this time.
	 * @param promptState the prompt state to set
	 */
    public void setNeedPrompt(boolean promptState) {
        needPrompt = promptState;
    }

    /**
	 * Returns the flag indicating if a prompt should be printed.
	 * @return the needPrompt
	 */
    public boolean needsPrompt() {
        return needPrompt;
    }

    /**
	 * @param userIdentity the user identity to set
	 */
    public void setUserIdentity(User userIdentity) {
        this.userIdentity = userIdentity;
    }

    /**
	 * @return the userIdentity
	 */
    public User getUserIdentity() {
        return userIdentity;
    }

    /**
	 * Set the current logical name manager to a specific (pre-existing)
	 * logical name manager.  This is used when a parent thread wants to
	 * "give" it's logical name space manager to a child thread to use.
	 * @param parentNameSpace the parent's logical name space
	 */
    public void setNameSpace(LogicalNameManager parentNameSpace) {
        namespace = parentNameSpace;
    }

    /**
	 * Return the name space (logical name manager) for the current session.
	 * If there isn't one, then a new one is created.
	 * @return the namespace
	 */
    public LogicalNameManager getNamespace() {
        if (namespace == null) namespace = new LogicalNameManager();
        return namespace;
    }

    /**
	 * Get the map listing all child threads for this session.  If the
	 * map has never been initialized, it is created with this call.
	 * @return the childThreads
	 */
    public TreeMap<String, JBasicThread> getChildThreads() {
        if (childThreads == null) childThreads = new TreeMap<String, JBasicThread>();
        return childThreads;
    }

    /**
	 * @param fLoadingSystemObjects the fLoadingSystemObjects to set
	 */
    public void setLoadingSystemObjects(boolean fLoadingSystemObjects) {
        this.fLoadingSystemObjects = fLoadingSystemObjects;
    }

    /**
	 * @return the fLoadingSystemObjects
	 */
    public boolean isLoadingSystemObjects() {
        return fLoadingSystemObjects;
    }

    /**
	 * @param abort the abort state to set
	 */
    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    /**
	 * @return the abort
	 */
    public boolean isAbortSignalled() {
        return abort;
    }

    /**
	 * @param basicFile the stdin to set
	 */
    public void setStdin(JBasicFile basicFile) {
        this.stdin = basicFile;
    }

    /**
	 * @return the stdin
	 */
    public JBasicFile stdin() {
        return stdin;
    }

    /**
	 * @param wsName the wsName to set
	 */
    public void setWorkspaceName(String wsName) {
        this.wsName = wsName;
    }

    /**
	 * @return the wsName
	 */
    public String getWorkspaceName() {
        return wsName;
    }

    /**
	 * Server sockets are managed on a process-wide basis for all JBasic 
	 * threads.  When an attempt is made to open a SERVER SOCKET, we go
	 * here to either create it the first time or just do a new accept
	 * on an existing server socket.
	 * @param socketNumber the integer port number we are to use to get
	 * client connections
	 * @return a client Socket connection
	 * @throws IOException  if an error occurs
	 */
    public static synchronized Socket getAndAcceptServerSocket(int socketNumber) throws IOException {
        if (serverSocketList == null) serverSocketList = new TreeMap<String, ServerSocket>();
        String key = Integer.toString(socketNumber);
        ServerSocket s = serverSocketList.get(key);
        if (s == null) {
            s = new ServerSocket(socketNumber);
            serverSocketList.put(key, s);
        }
        return s.accept();
    }

    /**
	 * Set a permission for the local user to a given state.  This
	 * is only permitted for command-line sessions; it is ignored
	 * for sandboxed routines (whose permissions can only be set
	 * by an administrator)
	 * 
	 * @param pname The permission name string
	 * @param state permission setting (enabled or disabled)
	 */
    public void setPermission(String pname, boolean state) {
        if (this.inSandbox()) return;
        if (this.userIdentity == null) userIdentity = new User("JBasic Shell", null, "");
        userIdentity.setPermission(pname, state);
    }
}
