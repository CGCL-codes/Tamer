package com.ohua.engine.os;

import java.io.IOException;
import com.ohua.engine.ProcessRunner;

public abstract class AbstractOhuaOSProcess {

    protected String _pathToFlow = null;

    protected String _pathToRuntimeConfiguration = null;

    private OSLoggingHandler _logging = null;

    protected void initialize() throws Exception {
    }

    public final void execute(String[] args) throws Throwable {
        CommandLineParser parser = createCommandLineParser();
        parser.parseComandLineArgs(args);
        parser.apply(this);
        _logging = new OSLoggingHandler(parser._loggingConfig);
        _logging.loggingSetup();
        try {
            initialize();
            run();
            tearDown();
        } catch (RuntimeException re) {
            System.err.println("A fatal error occured during execution: ");
            re.printStackTrace();
            handleFatalError();
            if (re.getCause() != null) {
                throw re.getCause();
            } else {
                throw re;
            }
        }
    }

    protected CommandLineParser createCommandLineParser() {
        return new CommandLineParser();
    }

    protected void handleFatalError() {
        _logging.loggingTearDown();
    }

    public void tearDown() throws IOException {
        _logging.loggingTearDown();
    }

    protected void run() throws IOException, ClassNotFoundException {
        ProcessRunner runner = createProcessRunner();
        runner.loadRuntimeConfiguration(_pathToRuntimeConfiguration);
        runner.run();
    }

    protected ProcessRunner createProcessRunner() {
        ProcessRunner runner = new ProcessRunner(_pathToFlow);
        return runner;
    }
}
