package com.defaultcompany.activities.util;

import java.io.*;

/**
 * Unix Shell 스크립트를 실행시키는 Runner.
 *
 * @author Edward KIM
 * @since 1.0
 */
public class ShellScriptRunner {

    /**
	 * 인자없는 Shell Script를 실행한다.
	 *
	 * @param command Shell Script명(절대경로)
	 * @return 처리 결과
	 */
    public static ShellScriptResult run(String command) {
        try {
            return runShellScript(command);
        } catch (Exception e) {
            return new ShellScriptResult(1, e.getMessage());
        }
    }

    /**
	 * 인자있는 Shell Script를 실행한다.
	 *
	 * @param command  Shell Script명(절대경로)
	 * @param argument 인자(예; <tt>-lsa</tt>)
	 * @return 처리 결과
	 */
    public static ShellScriptResult run(String command, String argument) {
        try {
            return runShellScript(command, argument);
        } catch (Exception e) {
            return new ShellScriptResult(1, e.getMessage());
        }
    }

    /**
	 * 인자있는 Shell Script를 실행한다.
	 *
	 * @param command Shell Script명(절대경로)
	 * @return 처리 결과
	 */
    private static ShellScriptResult runShellScript(String command) throws IOException, InterruptedException {
        return runShellScript(command, "");
    }

    /**
	 * 인자있는 Shell Script를 실행한다.
	 *
	 * @param command  Shell Script명(절대경로)
	 * @param argument 인자(예; <tt>-lsa</tt>)
	 * @return 처리 결과
	 */
    private static ShellScriptResult runShellScript(String command, String argument) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command + " " + argument);
        OutputStream os = process.getOutputStream();
        InputStream is = process.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        DataOutputStream dos = new DataOutputStream(os);
        process.waitFor();
        int available = bis.available();
        byte[] bytes = new byte[available];
        bis.read(bytes);
        String result = new String(bytes);
        dos.close();
        bis.close();
        return new ShellScriptResult(process.exitValue(), result);
    }
}
