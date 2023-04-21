package net.lukemurphey.nsia.response;

import net.lukemurphey.nsia.eventlog.EventLogMessage;
import net.lukemurphey.nsia.extension.ArgumentFieldsInvalidException;
import net.lukemurphey.nsia.extension.FieldLayout;
import net.lukemurphey.nsia.extension.FieldPassword;
import net.lukemurphey.nsia.extension.FieldText;
import net.lukemurphey.nsia.extension.MessageValidator;
import net.lukemurphey.nsia.scan.ScanResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class SSHCommandAction extends Action {

    private static final long serialVersionUID = 1L;

    private String hostname = null;

    private String username = null;

    private String password = null;

    private String commands = null;

    public static final String USER_DESCRIPTION = "Run a command via an SSH session";

    protected SSHCommandAction(Hashtable<String, String> arguments) throws ArgumentFieldsInvalidException {
        super("SSH Command", USER_DESCRIPTION);
        configure(arguments);
    }

    protected SSHCommandAction(String hostname, String username, String password, String commandList) {
        super("SSH Command", USER_DESCRIPTION);
        if (hostname == null) {
            throw new IllegalArgumentException("The hostname to connect to cannot be null");
        }
        if (hostname.isEmpty()) {
            throw new IllegalArgumentException("The hostname to connect to cannot be empty");
        }
        if (username == null) {
            throw new IllegalArgumentException("The username cannot be null");
        }
        if (username.isEmpty()) {
            throw new IllegalArgumentException("The username cannot be empty");
        }
        if (password == null) {
            throw new IllegalArgumentException("The password cannot be null");
        }
        if (commandList == null) {
            throw new IllegalArgumentException("The list of commands to execute cannot be null");
        }
        if (commandList.isEmpty()) {
            throw new IllegalArgumentException("The list of commands to execute cannot be empty");
        }
        this.hostname = hostname;
        this.username = username;
        this.password = password;
    }

    @Override
    public Hashtable<String, String> getValues() {
        Hashtable<String, String> values = new Hashtable<String, String>();
        if (this.hostname != null) {
            values.put("Hostname", this.hostname);
        }
        if (this.username != null) {
            values.put("Username", this.username);
        }
        if (this.password != null) {
            values.put("Password", this.password);
        }
        if (this.commands != null) {
            values.put("Commands", this.commands);
        }
        return values;
    }

    @Override
    public FieldLayout getLayoutWithValues() {
        FieldLayout layout = SSHCommandAction.getLayout();
        layout.setFieldsValues(this.getValues());
        return layout;
    }

    @Override
    public void execute(EventLogMessage logMessage) throws ActionFailedException {
        try {
            String[] processedCommands = splitUpCommands(commands);
            Vector<MessageVariable> vars = new Vector<MessageVariable>();
            for (int c = 0; c < processedCommands.length; c++) {
                processedCommands[c] = MessageVariable.processMessageTemplate(processedCommands[c], vars);
            }
            runSSHCommands(processedCommands, hostname, username, password);
        } catch (IOException e) {
            throw new ActionFailedException("Exception thrown while attempting to execute SSH commands", e);
        }
    }

    public static FieldLayout getLayout() {
        FieldLayout layout = new FieldLayout(1);
        layout.addField(new FieldText("Hostname", "Hostname", "Enter the name of the server to connect to", 1, 1, new MessageValidator("hostname")));
        layout.addField(new FieldText("Username", "Login Name", "Enter the user name to use when authenticating to the remote system", 1, 1, new MessageValidator("login name")));
        layout.addField(new FieldPassword("Password", "Password", "Enter the password to use when authenticating to the remote system", 1, new MessageValidator("password")));
        layout.addField(new FieldText("Commands", "Commands", "Enter the commands to be executed on the server (mulitple commands are accepted on separate lines)", 1, 10, new MessageValidator("commands")));
        return layout;
    }

    private String[] splitUpCommands(String commands) throws IOException {
        StringReader stringReader = new StringReader(commands);
        BufferedReader reader = new BufferedReader(stringReader);
        Vector<String> commandList = new Vector<String>();
        String lastLine = null;
        while ((lastLine = reader.readLine()) != null) {
            commandList.add(lastLine);
        }
        String[] commandsArray = new String[commandList.size()];
        commandList.toArray(commandsArray);
        return commandsArray;
    }

    private void runSSHCommands(String[] commands, String hostname, String username, String password) throws IOException {
        Connection conn = null;
        Session sess = null;
        try {
            conn = new Connection(hostname);
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (isAuthenticated == false) {
                throw new IOException("Authentication failed.");
            }
            for (String command : commands) {
                sess = conn.openSession();
                sess.execCommand(command);
                InputStream stdout = new StreamGobbler(sess.getStdout());
                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                StringBuffer standardOut = new StringBuffer();
                while (true) {
                    String line = br.readLine();
                    if (line == null) break;
                    standardOut.append(line);
                }
                sess.close();
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public String getConfigDescription() {
        return username + "@" + hostname;
    }

    @Override
    protected void setField(String name, String value) {
        if ("Hostname".equals(name)) {
            this.hostname = value;
        } else if ("Username".equals(name)) {
            this.username = value;
        } else if ("Password".equals(name)) {
            this.password = value;
        } else if ("Commands".equals(name)) {
            this.commands = value;
        }
    }

    @Override
    public void execute(ScanResult scanResult) throws ActionFailedException {
        try {
            String[] processedCommands = splitUpCommands(commands);
            for (int c = 0; c < processedCommands.length; c++) {
                processedCommands[c] = getMessage(processedCommands[c], scanResult);
            }
            runSSHCommands(processedCommands, hostname, username, password);
        } catch (IOException e) {
            throw new ActionFailedException("Exception thrown while attempting to execute SSH commands", e);
        }
    }
}
