package exec;

import java.io.InputStreamReader;

public class RuntimeExec {

    public static void exec(String cmd, String logFile, String errLogFile, StringBuffer logBuf) {
        System.out.println(cmd);
        try {
            Process child = Runtime.getRuntime().exec(cmd);
            InputStreamReader stdin = new InputStreamReader(child.getInputStream());
            StreamLogHandler handelerout = new StreamLogHandler(stdin, logFile, logBuf);
            handelerout.start();
            InputStreamReader stderr = new InputStreamReader(child.getErrorStream());
            StreamLogHandler handelererr = new StreamLogHandler(stderr, errLogFile, logBuf);
            handelererr.start();
            child.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
