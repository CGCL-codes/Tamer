package FDUFW.localhost.File;

public class BadExecJavac {

    public static void cmdmain(String arg) {
        if (arg.length() < 1) {
            System.out.println("USAGE: java GoodWindowsExec <cmd>");
            System.exit(1);
        }
        try {
            String osName = System.getProperty("os.name");
            String[] cmd = new String[3];
            if (osName.equals("Windows NT")) {
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = arg;
            } else if (osName.equals("Windows XP")) {
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                cmd[2] = arg;
            } else if (osName.equals("Windows 95")) {
                cmd[0] = "command.com";
                cmd[1] = "/C";
                cmd[2] = arg;
            }
            Runtime rt = Runtime.getRuntime();
            System.out.println(" cmd 命令  : " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
            Process proc = rt.exec(cmd);
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
