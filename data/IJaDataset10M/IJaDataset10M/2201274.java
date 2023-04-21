package org.eclipse.osgi.framework.debug;

/**
 * A framework trace entry is a bean containing all of the attributes for a single trace message.
 */
public class FrameworkDebugTraceEntry {

    /**
	 * Construct a new FrameworkTraceRecord object
	 * 
	 * @param bundleSymbolicName
	 *            The symbolic name of the bundle being traced
	 * @param optionPath
	 *            The trace optionPath
	 * @param message
	 *            The trace message
	 * @param traceClass
	 *            The class that calls the trace API
	 */
    public FrameworkDebugTraceEntry(final String bundleSymbolicName, final String optionPath, final String message, final Class traceClass) {
        this(bundleSymbolicName, optionPath, message, null, traceClass);
    }

    /**
	 * Construct a new FrameworkTraceRecord object
	 * 
	 * @param bundleSymbolicName
	 *            The symbolic name of the bundle being traced
	 * @param optionPath
	 *            The trace optionPath
	 * @param message
	 *            The trace message
	 * @param error
	 *            An exception to be traced
	 * @param traceClass
	 *            The class that calls the trace API 
	 */
    public FrameworkDebugTraceEntry(String bundleSymbolicName, final String optionPath, final String message, final Throwable error, final Class traceClass) {
        this.threadName = Thread.currentThread().getName();
        if (optionPath == null) {
            this.optionPath = FrameworkDebugTraceEntry.DEFAULT_OPTION_PATH;
        } else {
            this.optionPath = optionPath;
        }
        this.timestamp = System.currentTimeMillis();
        this.bundleSymbolicName = bundleSymbolicName;
        this.message = message;
        this.throwable = error;
        String determineClassName = null;
        String determineMethodName = null;
        int determineLineNumber = 0;
        StackTraceElement[] stackElements = new Exception().getStackTrace();
        int i = 0;
        while (i < stackElements.length) {
            String fullClassName = stackElements[i].getClassName();
            if (!fullClassName.equals(Thread.class.getName()) && !fullClassName.equals(FrameworkDebugTraceEntry.class.getName()) && !fullClassName.equals(EclipseDebugTrace.class.getName())) {
                if ((traceClass == null) || !fullClassName.equals(traceClass.getName())) {
                    determineClassName = stackElements[i].getClassName();
                    determineMethodName = stackElements[i].getMethodName();
                    determineLineNumber = stackElements[i].getLineNumber();
                }
                break;
            }
            i++;
        }
        this.className = determineClassName;
        this.methodName = determineMethodName;
        this.lineNumber = determineLineNumber;
    }

    public String toString() {
        final StringBuffer buffer = new StringBuffer(this.threadName);
        buffer.append(" ");
        buffer.append(this.timestamp);
        buffer.append(" ");
        buffer.append(this.bundleSymbolicName);
        buffer.append(" ");
        buffer.append(this.optionPath);
        buffer.append(" ");
        buffer.append(this.className);
        buffer.append(" ");
        buffer.append(this.methodName);
        buffer.append(" ");
        buffer.append(this.lineNumber);
        if (this.message != null) {
            buffer.append(": ");
            buffer.append(this.message);
        }
        if (this.throwable != null) {
            buffer.append(this.throwable);
        }
        return buffer.toString();
    }

    /**
	 * Accessor to the threads name
	 * 
	 * @return the name of the thread
	 */
    public final String getThreadName() {
        return this.threadName;
    }

    /**
	 * Accessor to the timestamp for this trace record
	 * 
	 * @return the date
	 */
    public final long getTimestamp() {
        return this.timestamp;
    }

    /**
	 * Accessor for the symbolic name of the bundle being traced
	 * 
	 * @return The symbolic name of the bundle being traced
	 */
    public final String getBundleSymbolicName() {
        return this.bundleSymbolicName;
    }

    /**
	 * Accessor for the trace message
	 * 
	 * @return the trace message
	 */
    public final String getMessage() {
        return this.message;
    }

    /**
	 * Accessor for the trace exception. This may be null if there is no exception.
	 * 
	 * @return the trace exception or null if none was defined.
	 */
    public final Throwable getThrowable() {
        return this.throwable;
    }

    /**
	 * Accessor for the name of the class being traced.
	 * 
	 * @return The name of the class being traced.
	 */
    public final String getClassName() {
        return this.className;
    }

    /**
	 * Accessor for the method being traced.
	 * 
	 * @return The name of the method being traced.
	 */
    public final String getMethodName() {
        return this.methodName;
    }

    /**
	 * Accessor for the option-path being traced. The <i>&lt;option-path&gt;</i> part of the debug option string
	 * required for the Eclipse debugging framework.
	 * 
	 * <pre>
	 *    Examples:
	 *       1) If a trace string com.ibm.myplugin.core/debug=true is specified then 'debug' is the option-path value.
	 *       2) If a trace string com.ibm.myplugin.core/debug/perf=true is specified then 'debug/perf' is the option-path value.
	 * </pre>
	 * 
	 * 
	 * @return The option-path being traced.
	 */
    public final String getOptionPath() {
        return this.optionPath;
    }

    /**
	 * Return the line number in the class/method where the trace originator
	 * 
	 * @return The line number from the class and method where the trace request originated
	 */
    public final int getLineNumber() {
        return this.lineNumber;
    }

    /**
	 * The name of the thread executing the code
	 */
    private final String threadName;

    /**
	 * The date and time when the trace occurred.
	 * 
	 */
    private final long timestamp;

    /**
	 * The trace option-path
	 */
    private final String optionPath;

    /**
	 * The symbolic name of the bundle being traced
	 */
    private final String bundleSymbolicName;

    /**
	 * The class being traced
	 */
    private final String className;

    /**
	 * The method being traced
	 */
    private String methodName = null;

    /**
	 * The line number
	 */
    private final int lineNumber;

    /**
	 * The trace message
	 */
    private final String message;

    /**
	 * The trace exception
	 */
    private final Throwable throwable;

    /** If a bundles symbolic name is not specified then the default value of /debug can be used */
    public static final String DEFAULT_OPTION_PATH = "/debug";
}
