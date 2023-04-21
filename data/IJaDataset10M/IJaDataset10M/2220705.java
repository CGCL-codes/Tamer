package com.ohua.tests.checkpoint;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public abstract class OhuaOSProcessFactory {

    private static String[] _debug = new String[] { "java", "-Xdebug", "-Xnoagent", "-Djava.compiler=NONE", "-Xrunjdwp:transport=dt_socket,address=localhost:8000,server=y,suspend=y", "-ea", "-Djava.util.logging.config.file=./com.ohua.engine/config/consoleLogging.properties", "-classpath" };

    private static String[] _noDebug = new String[] { "java", "-Xnoagent", "-Djava.compiler=NONE", "-ea", "-Djava.util.logging.config.file=./com.ohua.engine/config/consoleLogging.properties", "-classpath" };

    public static OhuaOSProcessHandle createOSProcess(String loggingConfiguration, boolean debugMode, String... commands) throws IOException {
        OhuaOSProcessHandle processHandle = new OhuaOSProcessHandle();
        processHandle._processBuilder = new ProcessBuilder();
        Properties props = new Properties();
        FileReader reader = new FileReader(loggingConfiguration);
        props.load(reader);
        performStdIOVerification(props);
        String systemInput = props.getProperty("system.in");
        new File(systemInput).mkdirs();
        File processRedirect = new File(systemInput + "/system.in.log");
        FileWriter redirect = new FileWriter(processRedirect);
        processHandle._inputDrainer = new ProcessInputDrainer(redirect);
        String classPath = getOhuaClassPath();
        List<String> command = new ArrayList<String>();
        if (debugMode) {
            Collections.addAll(command, _debug);
        } else {
            Collections.addAll(command, _noDebug);
        }
        command.add(classPath);
        Collections.addAll(command, commands);
        processHandle._processBuilder.command(command);
        return processHandle;
    }

    private static String getOhuaClassPath() {
        if (System.getProperties().containsKey("ohua.class.path")) {
            return System.getProperty("ohua.class.path");
        } else {
            return System.getProperty("java.class.path");
        }
    }

    public static void startOSProcess(OhuaOSProcessHandle processHandle) throws IOException {
        processHandle._ohuaChildProcess = processHandle._processBuilder.start();
        InputStream processInput = processHandle._ohuaChildProcess.getInputStream();
        processHandle._inputDrainer.setProcessInput(processInput);
        Thread drainer = new Thread(processHandle._inputDrainer, "process-input-drainer");
        drainer.start();
    }

    private static void performStdIOVerification(Properties props) {
        if (!props.containsKey("system.in")) {
            throw new IllegalArgumentException("Missing system.in configuration");
        }
        if (!props.containsKey("system.out")) {
            throw new IllegalArgumentException("Missing system.out configuration");
        }
        if (!props.containsKey("system.err")) {
            throw new IllegalArgumentException("Missing system.err configuration");
        }
    }
}
