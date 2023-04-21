package edu.rice.cs.cunit.replay;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import edu.rice.cs.cunit.SyncPointBuffer;
import edu.rice.cs.cunit.util.Debug;
import edu.rice.cs.cunit.util.StreamRedirectThread;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This program replays a program according to a saved schedule.
 *
 * @author Mathias Ricken
 */
public class Replay {

    /**
     * Debug VM.
     */
    private VirtualMachine _vm;

    /**
     * Thread transferring remote error stream to our error stream
     */
    private Thread _errThread = null;

    /**
     * Thread transferring remote output stream to our output stream
     */
    private Thread _outThread = null;

    /**
     * Mode for tracing the Replay program (default=0 off)
     */
    private int _debugTraceMode = 0;

    /**
     * Event thread
     */
    private EventThread _eventThread;

    /**
     * Writer for output.
     */
    private PrintWriter _writer;

    /**
     * Reader for trace.
     */
    private BufferedReader _trace = null;

    /**
     * Tokenizer for the trace.
     */
    private StreamTokenizer _tok;

    /**
     * Total number of sync points read back from trace file.
     */
    private long _numTotalSyncPointsRead = 0;

    /**
     * True if no GUI should be shown.
     */
    private boolean _headless = false;

    /**
     * Debug log name.
     */
    public static final String DRV = "replay.verbose";

    /**
     * JVM Entry Point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        new Replay(args);
    }

    /**
     * Constructor.
     *
     * @param args command line arguments.
     *
     */
    public Replay(String[] args) {
        boolean closeWriter = false;
        _writer = new PrintWriter(System.out);
        int inx;
        for (inx = 0; inx < args.length; ++inx) {
            String arg = args[inx];
            Debug.out.println(DRV, "Argument " + inx + ": " + arg);
            if (arg.charAt(0) != '-') {
                break;
            }
            if (arg.equals("-quiet")) {
                _writer = new PrintWriter(new OutputStream() {

                    public void write(int b) {
                    }
                });
            } else if (arg.equals("-headless")) {
                _headless = true;
            } else if (arg.equals("-verbose")) {
                Debug.out.setDebug(true);
                if ((inx < args.length - 1) && (!args[inx + 1].startsWith("-"))) {
                    String logs = args[++inx].toLowerCase();
                    for (int c = 0; c < logs.length(); ++c) {
                        switch(logs.charAt(c)) {
                            case 'm':
                                {
                                    Debug.out.setLogTarget(DRV, Debug.LogTarget.MAIN);
                                    break;
                                }
                            case 'e':
                                {
                                    Debug.out.setLogTarget(EventThread.DREV, Debug.LogTarget.MAIN);
                                    break;
                                }
                            default:
                                {
                                    System.err.println("Unknown log character. Usage: -verbose [m][e].");
                                    System.exit(-1);
                                    break;
                                }
                        }
                    }
                } else {
                    Debug.out.setLogTarget(DRV, Debug.LogTarget.MAIN);
                    Debug.out.setLogTarget(EventThread.DREV, Debug.LogTarget.MAIN);
                }
            } else if (arg.equals("-output")) {
                try {
                    _writer = new PrintWriter(new FileWriter(args[++inx]));
                    closeWriter = true;
                } catch (IOException exc) {
                    System.err.println("Cannot open output file: " + args[inx] + " - " + exc);
                    System.exit(1);
                }
            } else if (arg.equals("-trace")) {
                try {
                    _trace = new BufferedReader(new FileReader(args[++inx]));
                    _tok = new StreamTokenizer(_trace);
                    _tok.slashSlashComments(true);
                    _tok.eolIsSignificant(true);
                    _tok.parseNumbers();
                } catch (IOException exc) {
                    System.err.println("Cannot open trace file: " + args[inx] + " - " + exc);
                    System.exit(1);
                }
            } else if (arg.equals("-dbgtrace")) {
                _debugTraceMode = Integer.parseInt(args[++inx]);
            } else if (arg.equals("-help")) {
                help();
                System.exit(0);
            } else if (arg.startsWith("-J")) {
                ++inx;
                break;
            } else {
                System.err.println("No option: " + arg);
                help();
                System.exit(1);
            }
        }
        if (_trace == null) {
            System.err.println("-trace <filename> missing");
            help();
            System.exit(1);
        }
        if (inx >= args.length) {
            System.err.println("<class> missing");
            help();
            System.exit(1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(args[inx]);
        for (++inx; inx < args.length; ++inx) {
            sb.append(' ');
            sb.append(args[inx]);
        }
        Debug.out.println(DRV, "Command line: " + sb.toString());
        run(sb.toString(), _writer, closeWriter);
    }

    /**
     * Runs the tracer.
     *
     * @param cmdLine command line
     * @param writer  writer for debug output
     * @param closeWriter true if the writer should be closed at the end
     */
    private void run(String cmdLine, PrintWriter writer, boolean closeWriter) {
        _vm = launchDebugVM(cmdLine);
        generateTrace(writer, closeWriter);
    }

    /**
     * Generates trace.
     *
     * @param writer writer for debug output
     * @param closeWriter true if the writer should be closed at the end
     */
    void generateTrace(PrintWriter writer, boolean closeWriter) {
        _vm.setDebugTraceMode(_debugTraceMode);
        _eventThread = new EventThread(_vm, writer, new IReplayAdapter() {

            public void loadSynchronizationPoints() {
                Replay.this.loadSynchronizationPoints();
            }

            public void processMessage() {
                Replay.this.processMessage();
            }

            public void enableReplay() {
                Replay.this.enableReplay();
            }
        });
        _eventThread.setEventRequests();
        _eventThread.start();
        redirectOutput();
        _vm.resume();
        try {
            _eventThread.join();
            _errThread.join();
            _outThread.join();
        } catch (InterruptedException exc) {
        }
        long numReadBack = _numTotalSyncPointsRead;
        writer.println("// Note: Number of sync points read back from trace file = " + numReadBack);
        while (readSyncPoint(null, false)) {
        }
        writer.println("// Note: Number of sync points NOT read back from trace file before application exited = " + (_numTotalSyncPointsRead - numReadBack));
        writer.flush();
        if (closeWriter) {
            writer.close();
        }
    }

    /**
     * Launches the debug VM.
     *
     * @param cmdLine command line
     * @return VirtualMachine interface
     */
    VirtualMachine launchDebugVM(String cmdLine) {
        LaunchingConnector connector = findLaunchingConnector();
        Map<String, Connector.Argument> arguments = connectorArguments(connector, cmdLine);
        try {
            return connector.launch(arguments);
        } catch (IOException exc) {
            throw new Error("Unable to launch target VM: " + exc);
        } catch (IllegalConnectorArgumentsException exc) {
            throw new Error("Internal error: " + exc);
        } catch (VMStartException exc) {
            throw new Error("Target VM failed to initialize: " + exc.getMessage());
        }
    }

    /**
     * Redirects output from debugged program to console.
     */
    void redirectOutput() {
        Process process = _vm.process();
        _errThread = new StreamRedirectThread("error reader", process.getErrorStream(), System.err);
        _outThread = new StreamRedirectThread("output reader", process.getInputStream(), System.out);
        _errThread.start();
        _outThread.start();
    }

    /**
     * Finds a com.sun.jdi.CommandLineLaunch connector.
     *
     * @return LaunchingConnector interface
     */
    LaunchingConnector findLaunchingConnector() {
        List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
        for (Connector connector : connectors) {
            if (connector.name().equals("com.sun.jdi.CommandLineLaunch")) {
                return (LaunchingConnector) connector;
            }
        }
        throw new Error("No launching connector");
    }

    /**
     * Returns the launching connector's arguments.
     *
     * @param connector launching connector interface
     * @param cmdLine   command line
     * @return argument map
     */
    Map<String, Connector.Argument> connectorArguments(LaunchingConnector connector, String cmdLine) {
        Map<String, Connector.Argument> arguments = connector.defaultArguments();
        Connector.Argument mainArg = arguments.get("main");
        if (mainArg == null) {
            throw new Error("Bad launching connector");
        }
        mainArg.setValue(cmdLine);
        return arguments;
    }

    /**
     * Prints command line help message.
     */
    void help() {
        System.err.println("Usage: java edu.rice.cs.cunit.replay.Replay -trace <filename> [options] <trace> <class> [args]");
        System.err.println("[options] are:");
        System.err.println("  -quiet               No trace output");
        System.err.println("  -output <filename>   Output trace to <filename>");
        System.err.println("  -auto [n]            Automatically update on thread starts/stops.");
        System.err.println("                          Optional: n=delay in ms, n>=100. Default: 1000");
        System.err.println("  -obj                 Also process object sync points");
        System.err.println("  -help                Print this help message");
        System.err.println("  -J                   Pass all following options to the debug JVM");
        System.err.println("<class> is the program to trace");
        System.err.println("[args] are the arguments to <class>");
    }

    /**
     * Load synchronization points into the list in the debugged VM.
     */
    private void loadSynchronizationPoints() {
        if (_vm == null || _eventThread == null || !_eventThread.isConnected()) {
            return;
        }
        _writer.println("// Note: Attempting to load sync points");
        _vm.suspend();
        try {
            List<ReferenceType> classes = _vm.classesByName("edu.rice.cs.cunit.SyncPointBuffer");
            ClassType bufferClass = null;
            for (ReferenceType cl : classes) {
                if (cl.name().equals("edu.rice.cs.cunit.SyncPointBuffer")) {
                    if (cl instanceof ClassType) {
                        bufferClass = (ClassType) cl;
                        break;
                    }
                }
            }
            if (null == bufferClass) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer class.");
            }
            Field replayArrayField = bufferClass.fieldByName("_replaySyncPoints");
            if (null == replayArrayField) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer._replaySyncPoints field.");
            }
            Value replayArrayValue = bufferClass.getValue(replayArrayField);
            if (!(replayArrayValue instanceof ArrayReference)) {
                throw new Error("Unexpected type for edu.rice.cs.cunit.SyncPointBuffer._replaySyncPoints.");
            }
            ArrayReference replayArray = (ArrayReference) replayArrayValue;
            ArrayList<LongValue> values = new ArrayList<LongValue>();
            boolean eof = false;
            while (!eof && (values.size() < SyncPointBuffer.REPLAY_SIZE)) {
                eof = !readSyncPoint(values, true);
            }
            _writer.println("// Note: read " + (values.size() / 2) + " sync points from trace file (buffer size = " + (SyncPointBuffer.REPLAY_SIZE / 2) + ")");
            _writer.flush();
            while (values.size() < SyncPointBuffer.REPLAY_SIZE) {
                LongValue zero = _vm.mirrorOf((long) 0);
                values.add(zero);
            }
            try {
                replayArray.setValues(values);
            } catch (InvalidTypeException e) {
                throw new Error("Could not set SyncPointBuffer._replaySyncPoints contents.");
            } catch (ClassNotLoadedException e) {
                throw new Error("Could not set SyncPointBuffer._replaySyncPoints contents.");
            }
        } finally {
            _writer.flush();
            _vm.resume();
        }
    }

    /**
     * Read a sync point in from the trace file.
     * @param values array list of long values for replay list; or null, if sync points should not be buffered
     * @return true if sync point could be read; false if end of file
     * @param print print sync point to writer?
     */
    boolean readSyncPoint(ArrayList<LongValue> values, boolean print) {
        int token;
        boolean eof = false;
        do {
            try {
                token = _tok.nextToken();
                switch(token) {
                    case StreamTokenizer.TT_NUMBER:
                        long thread = (long) _tok.nval;
                        token = _tok.nextToken();
                        switch(token) {
                            case StreamTokenizer.TT_NUMBER:
                                long code = (long) _tok.nval;
                                if ((code < 0) || (code > SyncPointBuffer.SP.LAST_VALID_CODE.intValue())) {
                                    throw new Error("Error reading trace file at line " + _tok.lineno() + ": unexpected nval " + code);
                                }
                                token = _tok.nextToken();
                                switch(token) {
                                    case StreamTokenizer.TT_EOL:
                                    case StreamTokenizer.TT_EOF:
                                        if (print) {
                                            _writer.println(thread + " " + code);
                                            _writer.flush();
                                        }
                                        if (values != null) {
                                            LongValue tagValue = _vm.mirrorOf(code);
                                            LongValue threadValue = _vm.mirrorOf(thread);
                                            values.add(tagValue);
                                            values.add(threadValue);
                                        }
                                        ++_numTotalSyncPointsRead;
                                        return true;
                                    default:
                                        throw new Error("Error reading trace file: unexpected token " + _tok);
                                }
                            default:
                                throw new Error("Error reading trace file: unexpected token " + _tok);
                        }
                    case StreamTokenizer.TT_EOL:
                        break;
                    case StreamTokenizer.TT_EOF:
                        eof = true;
                        break;
                    default:
                        throw new Error("Error reading trace file: unexpected token " + _tok);
                }
            } catch (IOException e) {
                throw new Error("Error reading trace file: " + _tok + ", " + e.toString());
            }
        } while (!eof);
        return false;
    }

    /**
     * Processes a message in the client VM.
     */
    private void processMessage() {
        if (_vm == null || _eventThread == null || !_eventThread.isConnected()) {
            return;
        }
        _vm.suspend();
        try {
            List<ReferenceType> classes = _vm.classesByName("edu.rice.cs.cunit.SyncPointBuffer");
            ClassType bufferClass = null;
            for (ReferenceType cl : classes) {
                if (cl.name().equals("edu.rice.cs.cunit.SyncPointBuffer")) {
                    if (cl instanceof ClassType) {
                        bufferClass = (ClassType) cl;
                        break;
                    }
                }
            }
            if (null == bufferClass) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer class.");
            }
            Field messageField = bufferClass.fieldByName("_message");
            if (null == messageField) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer._message field.");
            }
            Value messageValue = bufferClass.getValue(messageField);
            if (!(messageValue instanceof StringReference)) {
                throw new Error("Unexpected type for edu.rice.cs.cunit.SyncPointBuffer._message.");
            }
            StringReference message = (StringReference) messageValue;
            String msg = message.value();
            _writer.println("// " + msg);
            if (!msg.startsWith("\t")) {
                List<ThreadReference> allThreads = _vm.allThreads();
                for (ThreadReference t : allThreads) {
                    classes = _vm.classesByName("java.lang.Thread");
                    ClassType threadClass = null;
                    for (ReferenceType cl : classes) {
                        if (cl.name().equals("java.lang.Thread")) {
                            if (cl instanceof ClassType) {
                                threadClass = (ClassType) cl;
                                break;
                            }
                        }
                    }
                    if (null == threadClass) {
                        throw new Error("Could not find java.lang.Thread class.");
                    }
                    Field threadIDField = threadClass.fieldByName("$$$threadID$$$");
                    if (null == threadIDField) {
                        throw new Error("Could not find java.lang.Thread.$$$threadID$$$ field.");
                    }
                    Value threadIDValue = t.getValue(threadIDField);
                    if (!(threadIDValue instanceof LongValue)) {
                        throw new Error("Unexpected type for java.lang.Thread.$$$threadID$$$.");
                    }
                    LongValue threadID = (LongValue) threadIDValue;
                    Field oldThreadField = threadClass.fieldByName("$$$oldThread$$$");
                    if (null == oldThreadField) {
                        throw new Error("Could not find java.lang.Thread.$$$oldThread$$$ field.");
                    }
                    Value oldThreadValue = t.getValue(oldThreadField);
                    if (!(oldThreadValue instanceof BooleanValue)) {
                        throw new Error("Unexpected type for java.lang.Thread.$$$oldThread$$$.");
                    }
                    BooleanValue oldThread = (BooleanValue) oldThreadValue;
                    _writer.printf("// \t$$$threadID$$$ = %d, $$$oldThread$$$ = %s: %s%s", threadID.value(), String.valueOf(oldThread.value()), t.name(), System.getProperty("line.separator"));
                    _writer.flush();
                }
            }
        } catch (Error error) {
            throw new Error("Could not read SyncPointBuffer._message.");
        } finally {
            _writer.flush();
            _vm.resume();
        }
    }

    /**
     * Enables replay in the client VM.
     */
    private void enableReplay() {
        if (_vm == null || _eventThread == null || !_eventThread.isConnected()) {
            return;
        }
        _writer.println("// Note: Enabling replay");
        _vm.suspend();
        try {
            List<ReferenceType> classes = _vm.classesByName("edu.rice.cs.cunit.SyncPointBuffer");
            ClassType bufferClass = null;
            for (ReferenceType cl : classes) {
                if (cl.name().equals("edu.rice.cs.cunit.SyncPointBuffer")) {
                    if (cl instanceof ClassType) {
                        bufferClass = (ClassType) cl;
                        break;
                    }
                }
            }
            if (null == bufferClass) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer class.");
            }
            Field replayingField = bufferClass.fieldByName("_replaying");
            if (null == replayingField) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer._replaying field.");
            }
            bufferClass.setValue(replayingField, _vm.mirrorOf(true));
        } catch (InvalidTypeException e) {
            throw new Error("Could not set SyncPointBuffer._replaying.");
        } catch (ClassNotLoadedException e) {
            throw new Error("Could not set SyncPointBuffer._replaying.");
        } catch (Error error) {
            throw new Error("Could not set SyncPointBuffer._replaying.");
        } finally {
            _writer.flush();
            _vm.resume();
        }
    }
}
