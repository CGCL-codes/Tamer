package com.ohua.tests.checkpoint;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import junit.framework.Assert;
import com.ohua.engine.utils.OhuaLoggerFactory;

public class CrashRestartRunner {

    private Logger _logger = OhuaLoggerFactory.getLogger(getClass());

    private ProcessBuilder _processBuilder = new ProcessBuilder();

    private String _loggingConfiguration = null;

    private String _restartLoggingConfiguration = null;

    private String _pathToFlow = null;

    private String _pathToRuntimeConfiguration = null;

    private String _pathToRestartRuntimeConfiguration = null;

    public int crashAndRestart(long timeToCrash, boolean debug) throws IOException, InterruptedException {
        printProcessInformation();
        OhuaOSProcessHandle childProcess = OhuaOSProcessFactory.createOSProcess(_loggingConfiguration, debug, "com/ohua/tests/checkpoint/OhuaOSProcess", _pathToFlow, _pathToRuntimeConfiguration, _loggingConfiguration);
        OhuaOSProcessFactory.startOSProcess(childProcess);
        Thread.sleep(timeToCrash);
        childProcess._ohuaChildProcess.destroy();
        childProcess._ohuaChildProcess.waitFor();
        childProcess._inputDrainer.done();
        Assert.assertTrue("Unexpected exit code for killed child process: " + childProcess._ohuaChildProcess.exitValue(), childProcess._ohuaChildProcess.exitValue() == 1 || childProcess._ohuaChildProcess.exitValue() == 143);
        _logger.info("Process killed: " + childProcess._ohuaChildProcess.exitValue());
        OhuaOSProcessHandle restartedChildProcess = OhuaOSProcessFactory.createOSProcess(_restartLoggingConfiguration, debug, "com/ohua/tests/checkpoint/OhuaOSProcess", _pathToFlow, _pathToRestartRuntimeConfiguration, _restartLoggingConfiguration);
        OhuaOSProcessFactory.startOSProcess(restartedChildProcess);
        int result = restartedChildProcess._ohuaChildProcess.waitFor();
        restartedChildProcess._inputDrainer.done();
        _logger.info("Process finished: " + restartedChildProcess._ohuaChildProcess.exitValue());
        return result;
    }

    private void printProcessInformation() {
        System.out.println("System environment configuration:");
        Map<String, String> sysEnv = System.getenv();
        for (Map.Entry<String, String> entry : sysEnv.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        System.out.println("System properties configuration:");
        Properties sysProps = System.getProperties();
        for (Map.Entry<Object, Object> entry : sysProps.entrySet()) {
            System.out.println(entry.getKey().toString() + "=" + entry.getValue().toString());
        }
        System.out.println("Process Builder environment configuration:");
        sysEnv = _processBuilder.environment();
        for (Map.Entry<String, String> entry : sysEnv.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }

    public final String getPathToFlow() {
        return _pathToFlow;
    }

    public final void setPathToFlow(String pathToFlow) {
        _pathToFlow = pathToFlow;
    }

    public final String getPathToRuntimeConfiguration() {
        return _pathToRuntimeConfiguration;
    }

    public final void setPathToRuntimeConfiguration(String pathToRuntimeConfiguration) {
        _pathToRuntimeConfiguration = pathToRuntimeConfiguration;
    }

    public final String getPathToRestartRuntimeConfiguration() {
        return _pathToRestartRuntimeConfiguration;
    }

    public final void setPathToRestartRuntimeConfiguration(String pathToRestartRuntimeConfiguration) {
        _pathToRestartRuntimeConfiguration = pathToRestartRuntimeConfiguration;
    }

    public final String getLoggingConfiguration() {
        return _loggingConfiguration;
    }

    public final void setLoggingConfiguration(String loggingConfiguration) {
        _loggingConfiguration = loggingConfiguration;
        _restartLoggingConfiguration = _loggingConfiguration;
    }

    public String getRestartLoggingConfiguration() {
        return _restartLoggingConfiguration;
    }

    public void setRestartLoggingConfiguration(String restartLoggingConfiguration) {
        _restartLoggingConfiguration = restartLoggingConfiguration;
    }

    public void applyDefaultConfiguration(String testMethodInputDirectory) {
        setPathToRuntimeConfiguration(testMethodInputDirectory + "runtime-parameters.properties");
        setPathToRestartRuntimeConfiguration(testMethodInputDirectory + "restart-runtime-parameters.properties");
        setLoggingConfiguration(testMethodInputDirectory + "logging-configuration.properties");
        setRestartLoggingConfiguration(testMethodInputDirectory + "restart-logging-configuration.properties");
    }
}
