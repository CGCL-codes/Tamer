package org.sharpx;

public class Utils {

    public static int runAntTarget(String antBatPath, String buildXmlPath, String targetName) {
        try {
            Process process = Runtime.getRuntime().exec("cmd.exe /c antBatPath -f buildXmlPath targetName");
            return process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
