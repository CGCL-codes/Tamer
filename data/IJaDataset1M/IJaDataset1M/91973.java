package com.fqr;

import org.apache.log4j.Logger;

public class ShellCommandJob extends FlexJob {

    private static Logger logger = Logger.getLogger(ShellCommandJob.class);

    public String command;

    public String workingDir;

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String postProcessor;

    public String getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(String postProcessor) {
        this.postProcessor = postProcessor;
    }

    public void runJob() throws Exception {
        if (command == null) {
            return;
        }
        logger.info("Running command: " + command);
        String[] cmd = command.split("\\s+");
        for (int i = 0; i < cmd.length; i++) {
            int lbrace = cmd[i].indexOf('{');
            if (lbrace == -1) {
                continue;
            }
            int rbrace = cmd[i].indexOf('}', lbrace);
            StringBuffer buf = new StringBuffer(cmd[i].substring(lbrace));
            buf.append(DateSymbolParser.parse(cmd[i].substring(lbrace + 1, rbrace)));
            buf.append(cmd[i].substring(rbrace + 1, cmd[i].length()));
            cmd[i] = buf.toString();
        }
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectErrorStream(true);
        Process p = processBuilder.start();
        int i = p.waitFor();
        logger.info("reurn code " + i);
    }
}
