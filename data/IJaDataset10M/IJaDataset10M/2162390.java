package org.jcrontab;

import org.jcrontab.log.Log;

/**
 *	This class ejecutes a native command
 * @author $Author: iolalla $
 * @version $Revision: 1.11 $
 */
public class NativeExec {

    /**
	 * main method
	 * @param args String[] the params passed from the console
	 */
    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("java org.jcrontab.NativeExec <cmd>");
            System.exit(1);
        }
        try {
            String osName = System.getProperty("os.name");
            String[] cmd = new String[3];
            if (osName.equals("Windows NT")) {
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = args[0];
            } else if (osName.equals("Windows 95")) {
                cmd[0] = "command.com";
                cmd[1] = "/C";
                cmd[2] = args[0];
            } else if (osName.equals("Windows 2000")) {
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = args[0];
            } else if (osName.equals("Linux")) {
                cmd = args;
            } else {
                cmd = args;
            }
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
        } catch (Throwable t) {
            Log.error(t.toString(), t);
        }
    }
}
