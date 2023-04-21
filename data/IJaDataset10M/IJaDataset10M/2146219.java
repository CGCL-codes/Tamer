package ti.oscript.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Vector;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import oscript.OscriptInterpreter;
import oscript.Shell;
import oscript.data.BasicScope;
import oscript.data.JavaBridge;
import oscript.data.Scope;
import oscript.data.Value;
import ti.mcore.u.PluginUtil;
import ti.plato.ui.views.console.IConsoleDocumentPartitioner;
import ti.plato.ui.views.console.IOConsole;
import ti.plato.ui.views.console.IOConsoleInputStream;
import ti.plato.ui.views.console.IOConsoleOutputStream;

/**
 * Extend IOConsole to handle colorized input (multi colors from one input stream)
 * and to notify the {@link ConsoleInputManager} on output (so it can fix caret
 * position)
 * 
 * @author robclark
 */
public class Console extends IOConsole {

    public static final String CONSOLE_PREFIX = "os>";

    private static final int MAX_BUFFER_SIZE = 450 * 1024;

    private static int count = 0;

    private Vector consoleInputManagers = new Vector();

    private ConsoleTabCompleter tabCompleter;

    private OutputStream out;

    /**
   * List of weak-references to consoles, see {@link #getDefaultConsole()} 
   */
    private static LinkedList<WeakReference<Console>> consoleList = new LinkedList<WeakReference<Console>>();

    private Thread consoleThread = null;

    private Action action;

    /**
   * Class Constructor.
   */
    Console() {
        super(getConsoleName(), null, null, null, true);
        IPreferenceStore jFacePrefStore = JFacePreferences.getPreferenceStore();
        RGB hColor = new RGB(0, 0x66, 0xff);
        String hColorStr = StringConverter.asString(hColor);
        jFacePrefStore.setValue(JFacePreferences.HYPERLINK_COLOR, hColorStr);
        String name = getName();
        ThreadGroup tg = new ThreadGroup(name);
        consoleThread = (new Thread(tg, name) {

            public void run() {
                try {
                    consoleMain();
                } catch (Throwable e) {
                    ConsolePlugin.logError("Unhandled Exception in Thread: " + Thread.currentThread(), e);
                }
            }
        });
        ConsoleFactory.registerConsole(this);
        consoleThread.start();
        this.setWaterMarks(2048, MAX_BUFFER_SIZE);
        registerConsole(this);
    }

    /**
   * Main loop for the console thread, which reads input, evaluates the 
   * input, and then prints the result
   *  
   * @throws Throwable
   */
    private void consoleMain() throws Throwable {
        IOConsoleInputStream is = getInputStream();
        out = new ConsoleOutputStream();
        final Scope scope = new BasicScope(OscriptInterpreter.getGlobalScope());
        scope.createMember("console", 0).opAssign(JavaBridge.convertToScriptObject(this));
        scope.createMember("in", 0).opAssign(JavaBridge.convertToScriptObject(is));
        scope.createMember("out", 0).opAssign(JavaBridge.convertToScriptObject(out));
        scope.createMember("display", 0).opAssign(JavaBridge.convertToScriptObject(Display.getDefault()));
        OscriptInterpreter.eval(ConsolePlugin.CONSOLE_INIT_OS, scope);
        PrintWriter ps = new PrintWriter(out);
        ps.println(OscriptInterpreter.getVersionString());
        ps.println("type '?' for help");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Shell shell = new Shell(fgColor(getColor(0, 255, 0), CONSOLE_PREFIX) + " ", br, ps, ps) {

            public Value evalStr(String line) throws oscript.parser.ParseException, oscript.exceptions.PackagedScriptObjectException {
                if (line.trim().equals("?;")) {
                    PluginUtil.openInSystemBrowser("http://objectscript.sourceforge.net/docs/osug.html");
                    return Value.NULL;
                }
                Value val = OscriptInterpreter.eval(line, scope);
                return val;
            }

            public String read() {
                try {
                    return super.read();
                } catch (IOException e) {
                    return "exit;";
                }
            }

            public void evalAndPrint(String line) {
                try {
                    if (action != null) action.setEnabled(true);
                    super.evalAndPrint(line);
                } catch (Throwable e) {
                    ConsolePlugin.logError("Unhandled Exception in Thread: " + Thread.currentThread(), e);
                } finally {
                    if (action != null) action.setEnabled(false);
                }
            }
        };
        try {
            shell.run();
        } finally {
            ConsoleFactory.unregisterConsole(this);
            ti.plato.ui.views.console.ConsolePlugin.getDefault().getConsoleManager().removeConsoles(new IOConsole[] { this });
        }
    }

    /**
   * Register the tab completer... this is called from "console-init.os",
   * since the tab completer is implemented in ObjectScript.
   * 
   * @param tabCompleter
   */
    public void setConsoleTabCompleter(ConsoleTabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    ConsoleTabCompleter getConsoleTabCompleter() {
        return tabCompleter;
    }

    /**
   * Get the output stream which prints to this console.
   */
    public OutputStream getOutputStream() {
        return out;
    }

    /**
   * Get the offset of the start of the input region
   */
    public int getInputStart() {
        IConsoleDocumentPartitioner p = getPartitioner();
        IDocument d = getDocument();
        for (int i = d.getLength() - 1; i >= 0; i--) if (p.isReadOnly(i)) return i + 1;
        return -1;
    }

    /**
   * Get the length of the input region.
   */
    public int getInputLength() {
        return getDocument().getLength() - getInputStart();
    }

    /**
   * a new output stream is created for each unique color, and they are
   * cached in this table
   */
    private Hashtable<Integer, IOConsoleOutputStream> color2os = new Hashtable<Integer, IOConsoleOutputStream>();

    private OutputStream getColorOutputStream(final int c) {
        Integer key = new Integer(c);
        IOConsoleOutputStream os = (IOConsoleOutputStream) color2os.get(key);
        if (os == null) {
            os = newOutputStream();
            Display d = Display.getCurrent();
            if (d == null) d = Display.getDefault();
            final Display dFinal = d;
            final IOConsoleOutputStream osFinal = os;
            Display.getDefault().syncExec(new Runnable() {

                public void run() {
                    osFinal.setColor(new Color(dFinal, getRed(c), getGreen(c), getBlue(c)));
                }
            });
            color2os.put(key, os);
        }
        return os;
    }

    private class ConsoleOutputStream extends OutputStream {

        /**
     * The current output stream.  When we enter a new region, the
     * current stream is pushed on to the <code>stack</code>, and
     * when exiting the region, it is popped back off.
     */
        private OutputStream current = getColorOutputStream(getColor(0xff, 0xff, 0xff));

        /**
     * stack of output streams
     */
        private Stack<OutputStream> stack = new Stack<OutputStream>();

        private synchronized void push(OutputStream os) {
            stack.push(current);
            current = os;
        }

        private synchronized void pop() {
            current = (OutputStream) stack.pop();
        }

        private abstract class State {

            public abstract State handle(int b) throws IOException;

            /** called when entering this state... overload if needed */
            public void enter() {
            }
        }

        private abstract class ColorState extends State {

            private int r, g, b;

            public State handle(int n) throws IOException {
                n = n & 0xff;
                if (r == -1) r = n; else if (g == -1) g = n; else if (b == -1) b = n;
                if (b != -1) {
                    complete(getColor(r, g, b));
                    return NULL_STATE;
                }
                return this;
            }

            public void enter() {
                super.enter();
                r = g = b = -1;
            }

            protected abstract void complete(int c);
        }

        private State NULL_STATE = new State() {

            public State handle(int b) throws IOException {
                if (b == ESCAPE_CHAR) return ESCAPE_STATE;
                current.write(b);
                current.flush();
                return this;
            }
        };

        private State FGCOLOR_STATE = new ColorState() {

            public void complete(int c) {
                push(getColorOutputStream(c));
            }
        };

        private State ESCAPE_STATE = new State() {

            public State handle(int b) throws IOException {
                switch(b) {
                    case FGCOLOR_ATTR_OPEN:
                        return FGCOLOR_STATE;
                    case ATTR_CLOSE:
                        pop();
                        return NULL_STATE;
                }
                return NULL_STATE;
            }
        };

        private State state = NULL_STATE;

        public void write(int b) throws IOException {
            State newState = state.handle(b);
            if (newState != state) {
                newState.enter();
                state = newState;
            }
        }

        public void flush() throws IOException {
            current.flush();
        }
    }

    static final char ESCAPE_CHAR = 0;

    static final char ATTR_CLOSE = 1;

    static final char FGCOLOR_ATTR_OPEN = 2;

    static final char BGCOLOR_ATTR_OPEN = 3;

    static final char HYPERLINK_ATTR_OPEN = 4;

    /**
   * Function to create a string with the specified foreground color attribute.
   * 
   * @param c      the foreground color
   * @param str    the string
   * @return a string with the appropriate embedded control characters
   */
    public static String fgColor(int c, String str) {
        if ((str == null) || (str.length() == 0)) return str;
        StringBuffer sb = new StringBuffer(str.length() + 7);
        sb.append(ESCAPE_CHAR);
        sb.append(FGCOLOR_ATTR_OPEN);
        sb.append((char) (getRed(c)));
        sb.append((char) (getGreen(c)));
        sb.append((char) (getBlue(c)));
        sb.append(str);
        sb.append(ESCAPE_CHAR);
        sb.append(ATTR_CLOSE);
        return sb.toString();
    }

    /**
   * Function to create a string with the specified background color attribute.
   * 
   * @param c      the background color
   * @param str    the string
   * @return a string with the appropriate embedded control characters
   */
    public static String bgColor(int c, String str) {
        return str;
    }

    /**
   * Function to create a string with a hyperlink.  Each time the user clicks 
   * the hyperlink, the runnable is invoked.  
   * 
   * @param r            the runnable to invoke when user clicks link
   * @param str          the string to apply it to
   * @return a string with embedded control characters
   * 
   * @deprecated NYI (not implemented)
   */
    public static String hyperlink(Runnable r, String str) {
        return str;
    }

    private static int getColor(int r, int g, int b) {
        return ((r << 16) & 0x00ff0000) | ((g << 8) & 0x0000ff00) | (b & 0x000000ff);
    }

    private static int getRed(int c) {
        return (c >> 16) & 0xff;
    }

    private static int getGreen(int c) {
        return (c >> 8) & 0xff;
    }

    private static int getBlue(int c) {
        return c & 0xff;
    }

    private static synchronized String getConsoleName() {
        if (count++ == 0) return "ObjectScript";
        return "ObjectScript " + count;
    }

    /**
   * Evaluate the specified string within the context of this console.
   * 
   * @param the valid script syntax to evaluate
   */
    public void evaluate(final String str) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                ConsolePlugin.workspaceSaveContainer.history.add(str);
                try {
                    getDocument().replace(getInputStart(), getInputLength(), str + "\n");
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
   * Get the default console.
   * @return
   */
    public static Console getDefaultConsole() {
        for (Iterator itr = consoleList.iterator(); itr.hasNext(); ) {
            Reference ref = (Reference) (itr.next());
            Console c = (Console) (ref.get());
            if (c != null) return c;
        }
        ConsoleFactory.getDefault().openConsole();
        return getDefaultConsole();
    }

    private static void registerConsole(Console c) {
        for (Iterator itr = consoleList.iterator(); itr.hasNext(); ) if (((Reference) (itr.next())).get() == null) itr.remove();
        consoleList.add(new WeakReference<Console>(c));
    }

    public void terminate() {
        if (consoleThread != null) consoleThread.interrupt();
    }

    public void setAction(Action action) {
        action.setEnabled(false);
        this.action = action;
    }

    public void removeAction() {
        action = null;
    }
}
