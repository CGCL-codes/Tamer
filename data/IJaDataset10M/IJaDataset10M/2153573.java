package jtcpfwd.forwarder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jtcpfwd.ForwarderHandlerThread;
import jtcpfwd.ForwarderThread;
import jtcpfwd.Lookup;
import jtcpfwd.Module;
import jtcpfwd.NoMoreSocketsException;
import jtcpfwd.listener.Listener;
import jtcpfwd.util.SplitOutputStream;
import jtcpfwd.util.StreamForwarder;

public class ConsoleForwarder extends Forwarder {

    public static final String SYNTAX = "Console@[!][log|restart|rules][,log|restart|rules[,...]]";

    public static final Class[] getRequiredClasses() {
        return new Class[] { Lookup.class, ForwarderThread.class, ForwarderHandlerThread.class, StreamForwarder.class, Listener.class, NoMoreSocketsException.class, SplitOutputStream.class };
    }

    private static final String shortCommands = "?q!.=+-#";

    private static final List longCommands = Arrays.asList(new String[] { "help", "exit", "restart", "log", "list", "add", "remove", "activate" });

    private static final String[] commandDescriptions = new String[] { "Show this help screen", "Exit console mode", "Restart jTCPfwd", "Show accumulated log buffer", "List currently configured listeners/forwarders", "Add a new listener and forwarder", "Remove listener/forwarder by index", "Activate added listener/forwarder by index" };

    private final boolean restartAllowed;

    private final List rules;

    private final List readOnlyRules = new ArrayList();

    private final List startedForwarderThreads = new ArrayList();

    private final ByteArrayOutputStream logBuffer;

    private final String allowedCommands;

    public ConsoleForwarder(String rule) throws Exception {
        if (rule.startsWith("!")) rule = rule.substring(1);
        List params = new ArrayList(Arrays.asList(rule.split(",")));
        logBuffer = params.contains("log") ? new ByteArrayOutputStream() : null;
        restartAllowed = params.contains("restart");
        rules = params.contains("rules") ? new ArrayList() : null;
        allowedCommands = "?q" + (logBuffer == null ? "" : ".") + (restartAllowed ? "!" : "") + (rules == null ? "" : "=+-#");
        params.remove("log");
        params.remove("restart");
        params.remove("rules");
        params.remove("");
        if (params.size() > 0) {
            throw new IllegalArgumentException("Unsupported arguments: " + params);
        }
        if (logBuffer != null) {
            System.setOut(new PrintStream(new SplitOutputStream(new OutputStream[] { logBuffer, System.out }), true));
            System.setErr(new PrintStream(new SplitOutputStream(new OutputStream[] { logBuffer, System.err }), true));
        }
        if (rules != null) {
            BufferedReader br = new BufferedReader(new FileReader("consolerules"));
            String line;
            while ((line = br.readLine()) != null) {
                rules.add(line);
                if (line.indexOf("Console@!") != -1) {
                    readOnlyRules.addAll(rules);
                    rules.clear();
                }
            }
            br.close();
        }
    }

    public Module[] getUsedModules() {
        return super.getUsedModules();
    }

    public Socket connect(Socket listener) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(listener.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(listener.getOutputStream()));
            String line;
            while (true) {
                bw.write("> ");
                bw.flush();
                line = br.readLine();
                if (line == null) return null;
                line = line.trim() + " ";
                if (line.matches(".*[\0-\10\12-\37].*")) {
                    bw.write("Control characters detected, ignoring line.\r\n");
                    continue;
                }
                if (line.length() == 0) continue;
                char cmd;
                String args;
                if (shortCommands.indexOf(line.charAt(0)) != -1) {
                    cmd = line.charAt(0);
                    args = line.substring(1).trim();
                } else {
                    int pos = line.indexOf(' ');
                    args = line.substring(pos).trim();
                    pos = longCommands.indexOf(line.substring(0, pos));
                    if (pos == -1) continue;
                    cmd = shortCommands.charAt(pos);
                }
                if (allowedCommands.indexOf(cmd) == -1) continue;
                switch(cmd) {
                    case '?':
                        bw.write("Supported commands:\r\n\r\n");
                        bw.write("\tShort\tLong\tDescription\r\n");
                        bw.write("\t=====\t====\t===========\r\n\r\n");
                        for (int i = 0; i < shortCommands.length(); i++) {
                            if (allowedCommands.indexOf(shortCommands.charAt(i)) != -1) bw.write("\t" + shortCommands.charAt(i) + "\t" + longCommands.get(i) + "\t" + commandDescriptions[i] + "\r\n");
                        }
                        bw.write("\r\nShort commands do not need a space before their argument, long commands do.\r\n");
                        break;
                    case 'q':
                        return null;
                    case '!':
                        System.exit(10);
                        break;
                    case '.':
                        synchronized (logBuffer) {
                            byte[] result = logBuffer.toByteArray();
                            logBuffer.reset();
                            bw.write(new String(result) + "\r\n");
                        }
                        break;
                    case '=':
                        if (readOnlyRules.size() > 0) {
                            bw.write("Readonly rules:\r\n");
                            for (int i = 0; i < readOnlyRules.size(); i++) {
                                bw.write("\t" + readOnlyRules.get(i) + "\r\n");
                            }
                            bw.write("\r\n");
                        }
                        bw.write("Rules:\r\n");
                        for (int i = 0; i < rules.size(); i++) {
                            bw.write("  [" + i + "]\t" + rules.get(i) + "\r\n");
                        }
                        break;
                    case '+':
                        if (args.indexOf("Console@!") != -1) {
                            bw.write("Error: Added commands may not contain \"Console@!\".\r\n");
                        } else {
                            rules.add(args);
                            bw.write("Added.\r\n");
                            saveRules();
                        }
                        break;
                    case '-':
                        try {
                            int idx = Integer.parseInt(args);
                            if (idx >= 0 && idx < rules.size()) {
                                rules.remove(idx);
                                bw.write("Removed.\r\n");
                                saveRules();
                            }
                        } catch (NumberFormatException ex) {
                        }
                        break;
                    case '#':
                        try {
                            int idx = Integer.parseInt(args);
                            if (idx >= 0 && idx < rules.size()) {
                                String rule = (String) rules.get(idx);
                                String[] parts = rule.split("[ \t]+");
                                if (parts.length == 2) {
                                    ForwarderThread t = new ForwarderThread(parts[0], parts[1]);
                                    startedForwarderThreads.add(t);
                                    t.start();
                                }
                            }
                        } catch (NumberFormatException ex) {
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    default:
                        throw new RuntimeException("Invalid command: " + cmd);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void saveRules() throws IOException {
        FileWriter fw = new FileWriter("consolerules");
        for (int i = 0; i < readOnlyRules.size(); i++) {
            fw.write(readOnlyRules.get(i) + "\r\n");
        }
        for (int i = 0; i < rules.size(); i++) {
            fw.write(rules.get(i) + "\r\n");
        }
        fw.close();
    }

    public void dispose() throws IOException {
        for (int i = 0; i < startedForwarderThreads.size(); i++) {
            ((ForwarderThread) startedForwarderThreads.get(i)).dispose();
        }
    }
}
