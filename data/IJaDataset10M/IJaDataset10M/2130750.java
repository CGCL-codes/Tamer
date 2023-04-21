package com.joe.runtime;

public class RuntimeTest {

    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("runtime.bat");
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
            errorGobbler.start();
            StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
            outGobbler.start();
            process.waitFor();
            System.out.println(process.exitValue());
        } catch (Exception e) {
        }
    }
}
