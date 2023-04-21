package org.xmlprocess.lircServer.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class LocalExec {

    private static final Logger log = Logger.getLogger(LocalExec.class.getName());

    String[] commands;

    public static String lineSep = System.getProperty("line.separator");

    public static void main(String[] args) {
        String[] testCmd = new String[] { "/usr/bin/irsend", "list", "", "" };
        LocalExec le = new LocalExec();
        le.setCommands(testCmd);
        le.printlines(le.runCMDs());
    }

    public void printlines(ArrayList<String> lines) {
        if (lines.size() > 0) {
            log.info("number of lines = " + lines.size());
            for (final Iterator<String> it1 = lines.iterator(); it1.hasNext(); ) {
                log.info(it1.next());
            }
        }
    }

    public void logCmds(String[] cmds) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cmds.length; i++) {
            sb.append(cmds[i]);
            sb.append(" ");
        }
        log.info("logCmds Commands = " + sb.toString());
    }

    public ArrayList<String> runCMDs() {
        ArrayList<String> lines = new ArrayList<String>();
        Process process = null;
        InputStream is = null;
        InputStream es = null;
        try {
            log.info("LocalExec runCMDs");
            logCmds(commands);
            process = Runtime.getRuntime().exec(commands);
            is = process.getInputStream();
            es = process.getErrorStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(es));
            int exiti = process.waitFor();
            log.info("exiti = " + exiti);
            String line = null;
            while ((line = errReader.readLine()) != null) {
                log.info("errreader " + line);
                lines.add(line);
            }
            while ((line = in.readLine()) != null) {
                log.info("inreader " + line);
                lines.add(line);
            }
        } catch (Exception e) {
            log.severe("Error running runCMDs");
            e.printStackTrace();
        }
        return lines;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }
}
