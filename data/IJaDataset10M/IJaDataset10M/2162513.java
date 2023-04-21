package fitnesseTfs;

import java.io.IOException;
import java.util.List;

public class SourceControlPlugin {

    static String rootPath = "";

    static String path = "";

    static boolean fileExists = false;

    static boolean output = true;

    static ITfRunner tfRunner = new TfRunner();

    static IOutputter outputter = new ConsoleOutputter();

    public static void setTfRunner(ITfRunner runner) {
        tfRunner = runner;
    }

    public static void setOutputter(IOutputter outputter) {
        SourceControlPlugin.outputter = outputter;
    }

    public static void cmUpdate(String file, String payload) throws IOException {
        parsePayload(payload);
        String fullPath = buildPath(payload, file);
        if (!fileExists) tf("add", fullPath);
        tf("checkin", fullPath);
    }

    public static void cmEdit(String file, String payload) {
        parsePayload(payload);
        String fullPath = buildPath(payload, file);
        checkFileExists(fullPath);
        if (fileExists) tf("checkout", fullPath);
    }

    public static void cmPreDelete(String file, String payload) throws IOException {
    }

    public static void cmDelete(String file, String payload) throws IOException {
        parsePayload(payload);
        String fullPath = buildPath(payload, file);
        checkFileExists(fullPath);
        if (fileExists) tf("delete", fullPath);
    }

    private static void parsePayload(String payload) {
        List<String> parameters = StringSplitter.split(payload);
        rootPath = parameters.get(1);
        path = parameters.get(2);
        output = parameters.size() > 3 ? !parameters.get(3).equals("nooutput") : true;
        tfRunner.setPath(path);
    }

    private static void checkFileExists(String fullPath) {
        String existsOutput = tf("dir", fullPath);
        fileExists = existsOutput.contains("1 item");
    }

    private static String tf(String command, String path) {
        if (output) outputter.output(command + " " + path);
        String result = tfRunner.execute(command, path);
        if (output) outputter.output(result);
        return result;
    }

    private static String buildPath(String payload, String fileName) {
        if (!rootPath.endsWith("\\")) rootPath += "\\";
        fileName = fileName.replace("/", "\\");
        fileName = fileName.replace(".\\", "");
        return rootPath + fileName;
    }
}
