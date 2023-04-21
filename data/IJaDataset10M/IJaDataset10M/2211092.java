package net.sourceforge.mtac.kernel;

import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.Stack;

/**
 * This is a context.
 *
 * @author Slava Pestov
 * @author Brant Gurganus
 * @version 0.1
 */
public class Context {

    /**
     * TODO: DOCUMENT ME!
     */
    public static final int KERNEL_FUNCTION = 1;

    /**
     * TODO: DOCUMENT ME!
     */
    public static final int USER_FUNCTION = 2;

    /**
     * TODO: DOCUMENT ME!
     */
    public static final int FILE = 3;

    /**
     * TODO: DOCUMENT ME!
     */
    private static String lastThread;

    /**
     * TODO: DOCUMENT ME!
     */
    private static Context lastContext;

    /**
     * TODO: DOCUMENT ME!
     */
    private static Hashtable threads = new Hashtable();

    /**
     * TODO: DOCUMENT ME!
     */
    public int lineNumber = 0;

    /**
     * TODO: DOCUMENT ME!
     */
    private Stack stack;

    /**
     * TODO: DOCUMENT ME!
     */
    private String source;

    /**
     * TODO: DOCUMENT ME!
     */
    private int sourceType;

    /**
     * Creates a new Context object.
     */
    private Context() {
        stack = new Stack();
    }

    /**
     * TODO: DOCUMENT ME!
     *
     * @return TODO: DOCUMENT ME!
     */
    public static Context getContext() {
        String thread = Thread.currentThread().getName();
        if (thread.equals(lastThread)) {
            return lastContext;
        }
        Context context = (Context) threads.get(thread);
        if (context == null) {
            context = new Context();
            threads.put(thread, context);
        }
        lastThread = thread;
        lastContext = context;
        return context;
    }

    /**
     * TODO: DOCUMENT ME!
     *
     * @return TODO: DOCUMENT ME!
     */
    public String getStackTraceString() {
        StringBuffer buf = new StringBuffer("Stack trace:\n");
        buf.append("    ");
        buf.append(new StackFrame(sourceType, source, lineNumber));
        buf.append('\n');
        for (int i = stack.size() - 1; i >= 0; i--) {
            buf.append("    ");
            buf.append(stack.elementAt(i));
            buf.append('\n');
        }
        buf.setLength(buf.length() - 1);
        return buf.toString();
    }

    /**
     * TODO: DOCUMENT ME!
     */
    public void popStackFrame() {
        try {
            if (stack.isEmpty()) {
                source = null;
            } else {
                StackFrame frame = (StackFrame) stack.pop();
                sourceType = frame.sourceType;
                source = frame.source;
                lineNumber = frame.lineNumber;
            }
        } catch (EmptyStackException e) {
            System.err.println("Internal kernel error:");
            e.printStackTrace();
        }
    }

    /**
     * TODO: DOCUMENT ME!
     *
     * @param sourceType TODO: DOCUMENT ME!
     * @param source TODO: DOCUMENT ME!
     */
    public void pushStackFrame(int sourceType, String source) {
        if (this.source != null) {
            stack.push(new StackFrame(this.sourceType, this.source, lineNumber));
        }
        this.sourceType = sourceType;
        this.source = source;
    }

    /**
     * This is the frame for a stack.
     */
    static class StackFrame {

        /**
         * TODO: DOCUMENT ME!
         */
        String source;

        /**
         * TODO: DOCUMENT ME!
         */
        int lineNumber;

        /**
         * TODO: DOCUMENT ME!
         */
        int sourceType;

        /**
         * Creates a new StackFrame object.
         *
         * @param sourceType TODO: DOCUMENT ME!
         * @param source TODO: DOCUMENT ME!
         * @param lineNumber TODO: DOCUMENT ME!
         */
        StackFrame(int sourceType, String source, int lineNumber) {
            this.sourceType = sourceType;
            this.source = source;
            this.lineNumber = lineNumber;
        }

        /**
         * TODO: DOCUMENT ME!
         *
         * @return TODO: DOCUMENT ME!
         */
        public String toString() {
            if (sourceType == KERNEL_FUNCTION) {
                return "in low-level function " + source;
            }
            if (sourceType == USER_FUNCTION) {
                return "in high-level function " + source;
            } else {
                return "in file " + source + " at line " + lineNumber;
            }
        }
    }
}
