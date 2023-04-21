package com.ohua.experiments;

import java.io.IOException;
import com.ohua.eai.os.JMSCommandLineParser;
import com.ohua.engine.OhuaProcessRunner;
import com.ohua.engine.ProcessRunner;
import com.ohua.engine.os.CommandLineParser;
import com.ohua.engine.utils.OhuaFlowStatistics;
import com.ohua.engine.utils.OhuaLoggerFactory;
import com.ohua.engine.utils.OhuaRunStatistics;
import com.ohua.tests.jms.JMSTestOhuaOSProcess;

public class OhuaExperimentOSProcess extends JMSTestOhuaOSProcess {

    class OSExperimentsCLI extends JMSCommandLineParser {

        @Override
        public void parseComandLineArgs(String[] args) {
            super.parseComandLineArgs(args);
            if (args.length > 3) {
                OhuaFlowStatistics.getInstance().setStatisticsConfig(args[args.length - 1]);
                OhuaRunStatistics.getInstance().setStatisticsConfig(args[args.length - 1]);
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        OhuaExperimentOSProcess process = new OhuaExperimentOSProcess();
        process.execute(args);
    }

    @Override
    protected CommandLineParser createCommandLineParser() {
        return new OSExperimentsCLI();
    }

    @Override
    public void tearDown() throws IOException {
        super.tearDown();
        OhuaLoggerFactory.getLogger(getClass()).info("Writing out flow statistics.");
        OhuaFlowStatistics.getInstance().recordFlowStatistics();
        OhuaRunStatistics.getInstance().recordFlowStatistics();
    }

    @Override
    protected ProcessRunner createProcessRunner() {
        return new ProcessRunner(_pathToFlow, new ExperimentsFlowParser());
    }

    @Override
    protected OhuaProcessRunner createNewProcessRunner() {
        return new OhuaProcessRunner(_pathToFlow, new ExperimentsFlowParser());
    }
}
