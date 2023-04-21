package net.fortytwo.ripple.cli;

import jline.Completor;
import jline.ConsoleReader;
import jline.MultiCompletor;
import net.fortytwo.ripple.Ripple;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.cli.ast.ListAST;
import net.fortytwo.ripple.cli.jline.DirectiveCompletor;
import net.fortytwo.ripple.control.Scheduler;
import net.fortytwo.ripple.control.TaskQueue;
import net.fortytwo.ripple.control.ThreadedInputStream;
import net.fortytwo.ripple.flow.CollectorHistory;
import net.fortytwo.ripple.flow.Sink;
import net.fortytwo.ripple.model.Lexicon;
import net.fortytwo.ripple.model.ModelConnection;
import net.fortytwo.ripple.model.RippleList;
import net.fortytwo.ripple.query.Command;
import net.fortytwo.ripple.query.PipedIOStream;
import net.fortytwo.ripple.query.QueryEngine;
import org.apache.log4j.Logger;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A command-line interpreter/browser which coordinates user interaction with a Ripple query engine.
 */
public class CommandLineInterface {

    private static final Logger LOGGER = Logger.getLogger(CommandLineInterface.class);

    private static final byte[] EOL = { '\n' };

    private final PipedIOStream writeIn;

    private final ThreadedInputStream consoleReaderInput;

    private final Interpreter interpreter;

    private final ConsoleReader reader;

    private final QueryEngine queryEngine;

    private final CollectorHistory<RippleList, RippleException> queryResultHistory = new CollectorHistory<RippleList, RippleException>(2);

    private final TaskQueue taskQueue = new TaskQueue();

    private int lineNumber;

    private boolean lastQueryContinued = false;

    /**
	 * Console input:
	 *     is --> filter --> consoleReaderInput --> reader --> readOut --> writeIn --> interpreter
	 *
	 * Normal output:
	 *     [commands and queries] --> queryEngine.getPrintStream()
	 *
	 * Error output:
	 *     alert() --> queryEngine.getErrorPrintStream()
	 */
    public CommandLineInterface(final QueryEngine qe, final InputStream is) throws RippleException {
        queryEngine = qe;
        Sink<ListAST, RippleException> querySink = new Sink<ListAST, RippleException>() {

            public void put(final ListAST ast) throws RippleException {
                addCommand(new VisibleQueryCommand(ast, queryResultHistory, lastQueryContinued));
                lastQueryContinued = false;
                addCommand(new UpdateCompletorsCmd());
                executeCommands();
            }
        };
        Sink<ListAST, RippleException> continuingQuerySink = new Sink<ListAST, RippleException>() {

            public void put(final ListAST ast) throws RippleException {
                addCommand(new VisibleQueryCommand(ast, queryResultHistory, lastQueryContinued));
                lastQueryContinued = true;
                addCommand(new UpdateCompletorsCmd());
                executeCommands();
            }
        };
        Sink<Command, RippleException> commandSink = new Sink<Command, RippleException>() {

            public void put(final Command cmd) throws RippleException {
                addCommand(cmd);
                addCommand(new UpdateCompletorsCmd());
                executeCommands();
            }
        };
        Sink<RecognizerEvent, RippleException> eventSink = new Sink<RecognizerEvent, RippleException>() {

            public void put(final RecognizerEvent event) throws RippleException {
                switch(event) {
                    case NEWLINE:
                        readLine();
                        break;
                    case ESCAPE:
                        LOGGER.debug("received escape event");
                        abortCommands();
                        break;
                    case QUIT:
                        LOGGER.debug("received quit event");
                        abortCommands();
                        throw new ParserQuitException();
                    default:
                        throw new RippleException("event not yet supported: " + event);
                }
            }
        };
        RecognizerAdapter ra = new RecognizerAdapter(querySink, continuingQuerySink, commandSink, eventSink, qe.getErrorPrintStream());
        Sink<Exception, RippleException> parserExceptionSink = new ParserExceptionSink(qe.getErrorPrintStream());
        InputStream filter = new InputStreamEventFilter(is, ra);
        consoleReaderInput = new ThreadedInputStream(filter);
        String jlineDebugOutput = Ripple.getProperties().getString(Ripple.JLINE_DEBUG_OUTPUT);
        try {
            reader = new ConsoleReader(consoleReaderInput, new OutputStreamWriter(qe.getPrintStream()));
            if (null != jlineDebugOutput && 0 < jlineDebugOutput.length()) {
                reader.setDebug(new PrintWriter(new FileWriter(jlineDebugOutput, true)));
            }
        } catch (Throwable t) {
            throw new RippleException(t);
        }
        jline.Terminal term = reader.getTerminal();
        writeIn = new PipedIOStream();
        updateCompletors();
        interpreter = new Interpreter(ra, writeIn, parserExceptionSink);
    }

    public void run() throws RippleException {
        lineNumber = 0;
        interpreter.parse();
    }

    private void readLine() {
        try {
            ++lineNumber;
            String prefix = "" + lineNumber + ")  ";
            System.out.println("reading a line");
            System.out.println("    consoleReaderInput.available() = " + consoleReaderInput.available());
            String line = reader.readLine(prefix);
            System.out.println("done reading the line: " + line);
            if (null != line) {
                byte[] bytes = line.getBytes();
                writeIn.write(bytes, 0, bytes.length);
                writeIn.write(EOL);
                writeIn.flush();
            }
        } catch (java.io.IOException e) {
            alert("IOException: " + e.toString());
        }
    }

    private void alert(final String s) {
        queryEngine.getErrorPrintStream().println("\n" + s + "\n");
    }

    private void updateCompletors() {
        LOGGER.debug("updating completors");
        List<Completor> completors = new ArrayList<Completor>();
        try {
            Lexicon lex = queryEngine.getLexicon();
            synchronized (lex) {
                completors.add(lex.getCompletor());
            }
            ArrayList<String> directives = new ArrayList<String>();
            directives.add("@count");
            directives.add("@define");
            directives.add("@help");
            directives.add("@list");
            directives.add("@prefix");
            directives.add("@quit");
            directives.add("@redefine");
            directives.add("@undefine");
            directives.add("@unprefix");
            completors.add(new DirectiveCompletor(directives));
            try {
                Completor multiCompletor = new MultiCompletor(completors);
                reader.addCompletor(multiCompletor);
            } catch (Throwable t) {
                throw new RippleException(t);
            }
        } catch (RippleException e) {
            e.logError();
            LOGGER.error("failed to update completors");
        }
    }

    private class UpdateCompletorsCmd extends Command {

        public void execute(final QueryEngine qe, final ModelConnection mc) throws RippleException {
            updateCompletors();
        }

        protected void abort() {
        }
    }

    private void addCommand(final Command cmd) {
        cmd.setQueryEngine(queryEngine);
        taskQueue.add(cmd);
    }

    private void executeCommands() throws RippleException {
        Scheduler.add(taskQueue);
        consoleReaderInput.setEager(true);
        try {
            taskQueue.waitUntilFinished();
        } catch (RippleException e) {
            consoleReaderInput.setEager(false);
            throw e;
        }
        consoleReaderInput.setEager(false);
    }

    private void abortCommands() {
        taskQueue.stop();
    }
}
