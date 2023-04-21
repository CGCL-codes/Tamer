package com.ohua.eai.os;

import java.io.IOException;
import com.ohua.eai.SimpleProcessListener;
import com.ohua.engine.OhuaProcessRunner;
import com.ohua.engine.ProcessNature;
import com.ohua.engine.UserRequest;
import com.ohua.engine.UserRequestType;
import com.ohua.engine.os.AbstractOhuaOSProcess;
import com.ohua.engine.os.CommandLineParser;
import com.ohua.engine.utils.parser.OhuaFlowParser;

public class JMSOhuaOSProcess extends AbstractOhuaOSProcess {

    protected ProcessNature _processType = ProcessNature.SOURCE_DRIVEN;

    private OhuaProcessRunner _runner = null;

    private OhuaFlowParser _parser = new OhuaFlowParser();

    public static void main(String[] args) throws Throwable {
        AbstractOhuaOSProcess process = new JMSOhuaOSProcess();
        process.execute(args);
    }

    @Override
    protected CommandLineParser createCommandLineParser() {
        return new JMSCommandLineParser();
    }

    public SimpleProcessListener load() throws IOException, ClassNotFoundException {
        _runner = createNewProcessRunner();
        _runner.loadRuntimeConfiguration(_pathToRuntimeConfiguration);
        SimpleProcessListener listener = new SimpleProcessListener();
        _runner.register(listener);
        new Thread(_runner, "jms-reader-process").start();
        return listener;
    }

    @Override
    protected void run() throws IOException, ClassNotFoundException {
        SimpleProcessListener listener = load();
        _runner.submitUserRequest(new UserRequest(UserRequestType.INITIALIZE));
        listener.awaitProcessingCompleted();
        listener.reset();
        _runner.submitUserRequest(new UserRequest(UserRequestType.START_COMPUTATION));
        switch(_processType) {
            case SOURCE_DRIVEN:
                System.out.println("Waiting for source driven computation to finish.");
                listener.awaitProcessingCompleted();
                break;
            case USER_DRIVEN:
                System.out.println(this + " Waiting for user driven computation to finish." + System.currentTimeMillis());
                waitForComputationFinish();
                System.out.println("Requesting process to finish computation." + System.currentTimeMillis());
                listener.reset();
                _runner.submitUserRequest(new UserRequest(UserRequestType.FINISH_COMPUTATION));
                System.out.println("Waiting for process to finish computation.");
                listener.awaitProcessingCompleted();
                break;
        }
        System.out.println("Computation finished.");
        listener.reset();
        _runner.submitUserRequest(new UserRequest(UserRequestType.SHUT_DOWN));
        listener.awaitProcessingCompleted();
    }

    protected void waitForComputationFinish() {
    }

    protected OhuaProcessRunner createNewProcessRunner() {
        return new OhuaProcessRunner(_pathToFlow, _parser);
    }

    public void submit(UserRequestType req) {
        _runner.submitUserRequest(new UserRequest(req));
    }

    public void setFlowParser(OhuaFlowParser parser) {
        _parser = parser;
    }
}
