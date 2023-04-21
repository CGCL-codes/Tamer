package airwolf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Wrapper for executing OS level commands.
 * 
 * @author Wade
 *
 */
public class ProcessPerCommand {

    private static final Logger logger = Logger.getLogger(ProcessPerCommand.class.getName());

    /**
	 * The format of the DCOP command used for passing
	 * DCOP messages/commands.
	 */
    private static String DCOP_BASE_COMMAND;

    static {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Properties props = new Properties();
            props.load(cl.getResourceAsStream("airwolf.properties"));
            if (System.getProperty("airwolf.dcop.user") != null && System.getProperty("airwolf.dcop.session") != null) {
                String baseCmd = MessageFormat.format(props.getProperty("dcop.explicit.command"), System.getProperty("airwolf.dcop.user"), System.getProperty("airwolf.dcop.session"));
                logger.info("DCOP base command: " + baseCmd);
                DCOP_BASE_COMMAND = baseCmd;
            } else {
                String implicitCmd = props.getProperty("dcop.implicit.command");
                logger.info("Running command: " + implicitCmd + " " + props.getProperty("amarok.command.player.state"));
                List<String> resp = ProcessPerCommand.runProcess((implicitCmd + " " + props.getProperty("amarok.command.player.state")).split(" "));
                if (resp.size() == 1 && Integer.parseInt(resp.get(0)) < 3) {
                    logger.info("DCOP base command: " + implicitCmd);
                    DCOP_BASE_COMMAND = implicitCmd;
                }
                String user = System.getenv("USER");
                String session = null;
                String getSessionsCmd = MessageFormat.format(props.getProperty("dcop.sessions"), user);
                logger.info("Running command: " + getSessionsCmd);
                List<String> userSessions = ProcessPerCommand.runProcess(getSessionsCmd.split(" "));
                for (String s : userSessions) {
                    s = s.trim();
                    if (s.startsWith(".DCOPserver")) {
                        logger.info("Trying DCOP session ID: " + s);
                        String statusCmd = MessageFormat.format(props.getProperty("dcop.explicit.command"), user, s) + " " + props.getProperty("amarok.command.player.state");
                        logger.info("Running command: " + statusCmd);
                        List<String> response = ProcessPerCommand.runProcess(statusCmd.split(" "));
                        if (response.size() == 1 && Integer.parseInt(response.get(0)) < 3) {
                            logger.info("DCOP session found: " + s);
                            session = s;
                            break;
                        } else {
                            logger.info("DCOP session failed: " + response);
                        }
                    }
                }
                String baseCmd = MessageFormat.format(props.getProperty("dcop.explicit.command"), user, session);
                logger.info("DCOP base command: " + baseCmd);
                DCOP_BASE_COMMAND = baseCmd;
            }
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
	 * Returns the command used to pass DCOP messages/commands.
	 * @return the command
	 */
    private static String getDcopBaseCommand() {
        return DCOP_BASE_COMMAND;
    }

    /**
	 * Executes the given DCOP command with the specified arguments, if any.
	 * @param command the command to execute
	 * @param arguments the arguments passed to the command
	 * @return the output of the command
	 * @throws AirwolfException
	 */
    public static List<String> executeCommand(String command, String... arguments) throws AirwolfException {
        command = ProcessPerCommand.getDcopBaseCommand() + " " + command;
        String logCmd = command;
        for (String s : arguments) {
            logCmd += " " + s;
        }
        logger.info("Executing command: " + logCmd);
        try {
            List<String> cmd = new ArrayList<String>();
            for (String s : command.split(" ")) {
                cmd.add(s);
            }
            for (String s : arguments) {
                cmd.add(s);
            }
            return ProcessPerCommand.runProcess(cmd.toArray(new String[0]));
        } catch (Exception ex) {
            throw new AirwolfException(ex, "Error running command: " + command);
        }
    }

    /**
	 * Run a process with the given array of command and arguments. 
	 * @param cmd the command/arguments array
	 * @return the process output
	 * @throws IOException
	 * @throws InterruptedException
	 */
    private static List<String> runProcess(String[] cmd) throws IOException, InterruptedException {
        List<String> lines = new ArrayList<String>();
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process p = pb.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = in.readLine();
        while (line != null) {
            lines.add(line);
            line = in.readLine();
        }
        int exitValue = p.waitFor();
        p.destroy();
        if (exitValue != 0) {
            logger.warning("Exit value " + exitValue + " for command: " + Arrays.toString(cmd));
        }
        return lines;
    }
}
