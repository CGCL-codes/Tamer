package com.ohua.experiments;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;
import com.ohua.engine.ProcessRunner;
import com.ohua.engine.utils.FileUtils;
import com.ohua.tests.AbstractFlowTestCase;

public class SchedulingExperiment extends AbstractFlowTestCase {

    protected class SchedulingExperimentConfig implements ExperimentRunConfig<String> {

        public Map<String, String> adjustStatistics(String run) {
            String[] r = run.split("\\|");
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("inter-section-arc-boundary", r[0]);
            m.put("inner-section-arc-boundary", r[1]);
            m.put("amount", r[2]);
            m.put("pipeline", r[3]);
            m.put("section-strategy", r[5]);
            return m;
        }

        public Map<String, String> adjustRuntimeProperties(String run) {
            String[] r = run.split("\\|");
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("inter-section-arc-boundary", r[0]);
            m.put("inner-section-arc-boundary", r[1]);
            m.put("section-strategy", r[4]);
            return m;
        }

        public Map<String, String> adjustFlow(String run) {
            String[] r = run.split("\\|");
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("XXXAMOUNTXXX", r[2]);
            m.put("XXXPIPELINEXXX", r[3]);
            return m;
        }
    }

    @Override
    protected ProcessRunner createProcessRunner(String pathToFlow) {
        return new ProcessRunner(pathToFlow, new ExperimentsFlowParser());
    }

    @Ignore
    @Test
    public void experiment() throws Throwable {
        ExperimentsOSProcessRunner<String> runner = new ExperimentsOSProcessRunner<String>();
        String data = FileUtils.loadFileContents(new File(getTestMethodInputDirectory() + "experiment.data"));
        String[] testData = data.split(",");
        runner._runs = testData;
        runner._runConfig = new SchedulingExperimentConfig();
        runner._rootOutputDir = getTestMethodOutputDirectory();
        runner._pathToFlowTemplate = getTestClassInputDirectory() + "pipeline.xml";
        runner._pathToPropertiesTemplate = getTestClassInputDirectory() + "runtime-parameters.properties";
        runner._pathToLoggingTemplate = getTestClassInputDirectory() + "logging-configuration.properties";
        runner._statisticsConfigTemplate = getTestMethodInputDirectory() + "statistics-config.properties";
        runner._cleanUp = false;
        runner.forkAndWait(false);
    }

    protected class SchedulingExperiment2Config implements ExperimentRunConfig<String> {

        public Map<String, String> adjustStatistics(String run) {
            String[] r = run.split("\\|");
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("amount", r[0]);
            m.put("pipeline", r[1]);
            m.put("section-strategy", r[4]);
            m.put("section-size", r[3]);
            return m;
        }

        public Map<String, String> adjustRuntimeProperties(String run) {
            String[] r = run.split("\\|");
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("section-strategy", r[2]);
            m.put("section-size", r[3]);
            return m;
        }

        public Map<String, String> adjustFlow(String run) {
            String[] r = run.split("\\|");
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("XXXAMOUNTXXX", r[0]);
            m.put("XXXPIPELINEXXX", r[1]);
            return m;
        }
    }

    @Test
    public void experiment2() throws Throwable {
        ExperimentsOSProcessRunner<String> runner = new ExperimentsOSProcessRunner<String>();
        String data = FileUtils.loadFileContents(new File(getTestMethodInputDirectory() + "experiment.data"));
        String[] testData = data.split(",");
        runner._runs = testData;
        runner._runConfig = new SchedulingExperiment2Config();
        runner._rootOutputDir = getTestMethodOutputDirectory();
        runner._pathToFlowTemplate = getTestClassInputDirectory() + "pipeline.xml";
        runner._pathToPropertiesTemplate = getTestClassInputDirectory() + "runtime-parameters.properties";
        runner._pathToLoggingTemplate = getTestClassInputDirectory() + "logging-configuration.properties";
        runner._statisticsConfigTemplate = getTestMethodInputDirectory() + "statistics-config.properties";
        runner._cleanUp = false;
        runner.forkAndWait(false);
    }
}
