package cu.ftpd.logging;

import cu.ftpd.user.User;
import java.io.*;
import java.util.Map;
import java.util.Locale;
import java.util.Date;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.InetAddress;

/**
 * @author Markus Jevring <markus@jevring.net>
 * @version $Id: CommandLog.java 268 2008-10-30 22:24:30Z jevring $
 * @since 2008-okt-04 - 23:37:40
 */
public class CommandLog {

    private int fileMode;

    private int verbosity;

    private File directory;

    private PrintWriter singleLog;

    private Map<String, PrintWriter> usernameLogs = new HashMap<String, PrintWriter>();

    private Map<Long, PrintWriter> connectionIdLogs = new HashMap<Long, PrintWriter>();

    private final DateFormat time = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.ENGLISH);

    protected CommandLog() {
    }

    public CommandLog(int verbosity, int fileMode, String directory) throws FileNotFoundException {
        this.fileMode = fileMode;
        this.verbosity = verbosity;
        this.directory = new File(directory);
        if (this.directory.exists() && this.directory.isDirectory()) {
            if (fileMode == 1) {
                singleLog = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(this.directory, "commandlog.log"), true)), true);
            }
        } else {
            if (this.directory.mkdirs()) {
                if (fileMode == 1) {
                    singleLog = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(this.directory, "commandlog.log"), true)), true);
                }
            } else {
                throw new FileNotFoundException("Could not find or create directory for command log, or indicated file is not a directory.");
            }
        }
    }

    public void logCommand(User user, long connectionId, InetAddress remoteHost, String command) {
        if (verbosity == 1 || verbosity == 2) {
            log(user, connectionId, remoteHost, command, true);
        }
    }

    public void logResponse(User user, long connectionId, InetAddress remoteHost, String response) {
        if (verbosity == 2) {
            log(user, connectionId, remoteHost, response, false);
        }
    }

    private void log(User user, long connectionId, InetAddress remoteHost, String message, boolean isCommand) {
        PrintWriter log;
        if (fileMode == 1) {
            log = singleLog;
        } else if (fileMode == 2) {
            log = usernameLogs.get(user.getUsername());
            if (log == null) {
                try {
                    log = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(this.directory, user.getUsername() + ".log"), true)));
                    usernameLogs.put(user.getUsername(), log);
                } catch (FileNotFoundException e) {
                    Logging.getErrorLog().reportCritical("Failed to create username log for writing: " + e.getMessage());
                    return;
                }
            }
        } else if (fileMode == 3) {
            log = connectionIdLogs.get(connectionId);
            if (log == null) {
                try {
                    log = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(this.directory, String.valueOf(new Date()).replaceAll(":", ".") + "." + String.valueOf(connectionId) + ".log"))));
                    connectionIdLogs.put(connectionId, log);
                } catch (FileNotFoundException e) {
                    Logging.getErrorLog().reportCritical("Failed to create connectionId log for writing: " + e.getMessage());
                    return;
                }
            }
        } else {
            return;
        }
        log.println(time.format(new Date()) + ":" + connectionId + ":" + (user == null ? "<unknown user>" : user.getUsername()) + ":" + remoteHost + (isCommand ? ":C:" : ":R:") + message);
        log.flush();
    }

    public void shutdown() {
        if (fileMode == 1) {
            singleLog.close();
        } else if (fileMode == 2) {
            for (PrintWriter pw : usernameLogs.values()) {
                pw.close();
            }
        } else if (fileMode == 3) {
            for (PrintWriter pw : connectionIdLogs.values()) {
                pw.close();
            }
        }
    }
}
