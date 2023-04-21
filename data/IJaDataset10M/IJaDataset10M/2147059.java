package net.sf.buildbox.worker.util;

import net.sf.buildbox.util.BbxCommandline;
import net.sf.buildbox.util.BbxMiscUtils;
import net.sf.buildbox.worker.api.WorkerFeedback;
import org.codehaus.plexus.util.cli.CommandLineCallable;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.joda.time.Period;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
* @author Petr Kozelka
*/
public class FireRamp implements Callable<Integer> {

    private static final Logger LOGGER = Logger.getLogger(FireRamp.class.getName());

    private final String id;

    private final BbxCommandline cl;

    private final long timeout;

    private WorkerFeedback workerFeedback;

    private final List<StreamConsumer> stdoutList = new ArrayList<StreamConsumer>();

    private final List<StreamConsumer> stderrList = new ArrayList<StreamConsumer>();

    private InputStream stdin;

    private static final String HR = "----------------------------------------------------------------------- - - -  -";

    public FireRamp(String id, BbxCommandline cl, long timeout) {
        this.id = id;
        this.cl = cl;
        this.timeout = timeout;
    }

    public void setWorkerFeedback(final WorkerFeedback workerFeedback) {
        this.workerFeedback = workerFeedback;
        stdoutList.add(0, new StreamConsumer() {

            public void consumeLine(String line) {
                workerFeedback.stdout(line);
            }
        });
        stderrList.add(0, new StreamConsumer() {

            public void consumeLine(String line) {
                workerFeedback.stderr(line);
            }
        });
    }

    public void setStdin(InputStream stdin) {
        this.stdin = stdin;
    }

    public void addStdOutConsumer(StreamConsumer stdoutConsumer) {
        stdoutList.add(stdoutConsumer);
    }

    public void addStdErrConsumer(StreamConsumer stderrConsumer) {
        stderrList.add(stderrConsumer);
    }

    public Integer call() throws CommandLineException {
        final AtomicInteger stdoutCounter = new AtomicInteger(0);
        final StreamConsumer stdout = new StreamConsumer() {

            public void consumeLine(String line) {
                stdoutCounter.incrementAndGet();
                for (StreamConsumer streamConsumer : new ArrayList<StreamConsumer>(stdoutList)) {
                    streamConsumer.consumeLine(line);
                }
                if (stdoutList.isEmpty()) {
                    LOGGER.info(line);
                }
            }
        };
        final AtomicInteger stderrCounter = new AtomicInteger(0);
        final StreamConsumer stderr = new StreamConsumer() {

            public void consumeLine(String line) {
                stderrCounter.incrementAndGet();
                for (StreamConsumer streamConsumer : new ArrayList<StreamConsumer>(stderrList)) {
                    streamConsumer.consumeLine(line);
                }
                if (stderrList.isEmpty()) {
                    LOGGER.severe(line);
                }
            }
        };
        workerFeedback.console("/" + HR);
        workerFeedback.console("| Executing commandline task '%s'", id);
        if (cl.getWorkingDirectory() != null) {
            workerFeedback.console("| cd " + cl.getWorkingDirectory());
        }
        workerFeedback.console("| " + CommandLineUtils.toString(cl.getCommandline()));
        workerFeedback.console("+" + HR);
        final long startTime = System.currentTimeMillis();
        try {
            final int timeoutInSeconds = (int) (timeout / 1000);
            final CommandLineCallable clc = CommandLineUtils.executeCommandLineAsCallable(cl, stdin, stdout, stderr, timeoutInSeconds);
            final Integer exitCode = clc.call();
            workerFeedback.console("+" + HR);
            workerFeedback.console("| exitCode = %s (%s)", exitCode, BbxMiscUtils.explainExitCode(exitCode));
            return exitCode;
        } catch (RuntimeException e) {
            handleException(e);
            throw e;
        } catch (CommandLineException e) {
            handleException(e);
            throw e;
        } finally {
            final long duration = System.currentTimeMillis() - startTime;
            final Period period = new Period(duration);
            workerFeedback.console("| duration: %02d:%02d:%02d.%03d (%d millis), stdout: %d lines, stderr: %d lines", period.getHours(), period.getMinutes(), period.getSeconds(), period.getMillis(), duration, stdoutCounter.get(), stderrCounter.get());
            workerFeedback.console("\\" + HR);
        }
    }

    private void handleException(Throwable e) {
        workerFeedback.console("+" + HR);
        workerFeedback.console("catched: %s: %s", e.getClass().getName(), e.getMessage());
        final StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        final String[] lines = stringWriter.toString().split(Pattern.quote(System.getProperty("line.separator")));
        for (String line : lines) {
            workerFeedback.console(line);
        }
    }
}
