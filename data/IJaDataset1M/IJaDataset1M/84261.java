package org.sourceforge.jemm.server;

import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ServerOptions {

    public static final int DEFAULT_CLIENT_PORT = 5001;

    public static final int DEFAULT_CONTROL_PORT = 5002;

    private final ServerAction action;

    private final int controlPort;

    private final boolean debug;

    private final int port;

    private final ServerMode mode;

    private final File dataDir;

    public ServerOptions(CommandLine cmd) throws ParseException {
        if (!cmd.hasOption('a')) throw new ParseException("'action' must be defined");
        action = ServerAction.parseServerAction(cmd.getOptionValue('a'));
        debug = cmd.hasOption('x');
        if (cmd.hasOption('c')) controlPort = parsePortNumber("controlport", cmd.getOptionValue('c')); else controlPort = DEFAULT_CONTROL_PORT;
        if (cmd.hasOption('p')) port = parsePortNumber("port", cmd.getOptionValue('p')); else port = DEFAULT_CLIENT_PORT;
        if (controlPort == port) throw new ParseException("Client port and control port must be different.");
        if (cmd.hasOption('m')) mode = ServerMode.parseServerMode(cmd.getOptionValue('m')); else mode = ServerMode.DEFAULT;
        if (mode == ServerMode.PERSISTENT) {
            if (action == ServerAction.STOP) dataDir = null; else if (!cmd.hasOption('d')) throw new ParseException("For persistent server data directory must be specified"); else {
                dataDir = new File(cmd.getOptionValue('d'));
                if (!fileDirExistsAndIsWritable(dataDir)) throw new ParseException("given datadir '" + dataDir + "' is not a directory or is not writeable");
            }
        } else {
            if (cmd.hasOption('d')) throw new ParseException("DataDir not allowed for memory based servers");
            dataDir = null;
        }
    }

    protected boolean fileDirExistsAndIsWritable(File file) {
        return file.exists() && file.isDirectory() && file.canRead() && file.canWrite();
    }

    private int parsePortNumber(String optionName, String optionValue) throws ParseException {
        try {
            int value = Integer.parseInt(optionValue);
            if (value < 1) throw new ParseException(optionName + " must not be < 1");
            return value;
        } catch (NumberFormatException nfe) {
            throw new ParseException("value for " + optionName + " not an integer");
        }
    }

    public static Options generateOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help information and exit");
        options.addOption("a", "action", true, "The process action to take start|stop");
        options.addOption("m", "mode", true, "Mode to run in persistent|memory (default persistent)");
        options.addOption("p", "port", true, "The port that the server listens for client connections on" + " (default " + DEFAULT_CLIENT_PORT + ")");
        options.addOption("c", "controlport", true, "The port that the server listens for control commands" + " on (default " + DEFAULT_CONTROL_PORT + ")");
        options.addOption("d", "datadir", true, "The persistent data directory (required when mode=persistent)" + " on (default " + DEFAULT_CONTROL_PORT + ")");
        options.addOption("x", "debug", false, "Logs internal debugging information");
        return options;
    }

    public ServerAction getAction() {
        return action;
    }

    public int getControlPort() {
        return controlPort;
    }

    public int getPort() {
        return port;
    }

    public ServerMode getMode() {
        return mode;
    }

    public File getDataDir() {
        return dataDir;
    }

    public boolean getDebug() {
        return debug;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("mode=").append(getMode()).append('\n');
        if (mode == ServerMode.PERSISTENT) sb.append("dataDir=").append(getDataDir()).append('\n');
        sb.append("port=" + getPort()).append('\n');
        sb.append("controlPort=" + getControlPort());
        return sb.toString();
    }
}
