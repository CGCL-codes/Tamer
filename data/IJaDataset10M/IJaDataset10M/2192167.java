package net.jsrb.startup;

import java.io.*;
import java.util.*;

/**
 * Util class for remove all sysv objects
 */
public class jsrbipcrm {

    static class ReaperThread extends Thread {

        Writer writer;

        InputStream is;

        ReaperThread(Writer writer, InputStream is) {
            super("reaper thread");
            this.writer = writer;
            this.is = is;
        }

        public void run() {
            byte[] data = new byte[4096];
            int len = 0;
            try {
                while ((len = is.read(data)) > 0) {
                    writer.write(new String(data, 0, len));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            rmipc(new String[] { "ipcs", "-m" }, new String[] { "ipcrm", "-m" });
            rmipc(new String[] { "ipcs", "-s" }, new String[] { "ipcrm", "-s" });
            rmipc(new String[] { "ipcs", "-q" }, new String[] { "ipcrm", "-q" });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    static void rmipc(String[] ipcsCommand, String[] ipcrmCommand) throws Exception {
        StringWriter writer = new StringWriter();
        ProcessBuilder pb = new ProcessBuilder(ipcsCommand);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        ReaperThread thread = new ReaperThread(writer, process.getInputStream());
        thread.setDaemon(true);
        thread.start();
        int exitV = process.waitFor();
        if (exitV != 0) {
            System.out.println("run ipcs failed ,errorcode =" + exitV);
            System.exit(exitV);
        }
        Thread.sleep(500);
        String ipcsmOutput = writer.toString();
        List<String> ipcids = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new StringReader(ipcsmOutput));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String ipcid = parseLine(line);
            if (ipcid != null) {
                ipcids.add(ipcid);
            }
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < ipcsCommand.length; i++) {
            buffer.append(ipcsCommand[i]);
            buffer.append(" ");
        }
        buffer.append("returns ");
        buffer.append(ipcids);
        System.out.println(buffer);
        for (String ipcid : ipcids) {
            List<String> crmCommands = new ArrayList<String>();
            crmCommands.addAll(Arrays.asList(ipcrmCommand));
            crmCommands.add(ipcid);
            ProcessBuilder crmpb = new ProcessBuilder(crmCommands);
            Process crmp = crmpb.start();
            crmp.waitFor();
            Thread.sleep(50);
        }
    }

    /**
     * Parse line , find the ipc id.
     * <PRE>
     * Linux:
     * key        msqid      owner      perms      used-bytes   messages    
	 * 0xc10123c9 4063232    zhugf      666        0            0
	 * 
	 * AIX:
	 * 
     * </PRE>
     */
    static String parseLine(String line) {
        line = line.trim();
        if (line.length() < 2) {
            return null;
        }
        String result = null;
        StringTokenizer token = new StringTokenizer(line, " \t\r\n");
        while (token.hasMoreTokens()) {
            String str = token.nextToken();
            str = str.trim();
            if (str.length() == 0) {
                continue;
            }
            str = str.toLowerCase();
            if (str.indexOf("0x") >= 0) {
                continue;
            }
            boolean allNumber = true;
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c < '0' || c > '9') {
                    allNumber = false;
                    break;
                }
            }
            if (allNumber) {
                result = str;
                break;
            }
        }
        return result;
    }
}
