package semwiq;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.jku.semwiq.endpoint.Constants;
import at.jku.semwiq.endpoint.daemon.EndpointDaemonImpl;
import at.jku.semwiq.log.LogBufferDispatcher;
import at.jku.semwiq.rmi.CommonConstants;

/**
 * @author dorgon
 *
 */
public class daemon {

    private static final Logger log = LoggerFactory.getLogger(daemon.class);

    private static Options opts;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Option rmiHost = new Option("h", "host", true, "hostname (if it cannot be correctly determined by the JVM)");
        rmiHost.setArgName("hostname");
        rmiHost.setOptionalArg(true);
        Option rmiPort = new Option("r", "rmi-port", true, "port for RMI registry (default: " + CommonConstants.RMI_REGISTRY_PORT + ")");
        rmiPort.setArgName("rmi-port");
        rmiPort.setOptionalArg(true);
        Option startPort = new Option("p", "port", true, "start port (default: " + Constants.START_PORT + ")");
        startPort.setArgName("start-port");
        startPort.setOptionalArg(true);
        Option help = new Option("?", "help", false, "help");
        help.setOptionalArg(true);
        opts = new Options();
        opts.addOption(help);
        opts.addOption(rmiPort);
        opts.addOption(startPort);
        opts.addOption(rmiHost);
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(opts, args);
            if (cmd.hasOption("?")) {
                printUsage(null);
            } else {
                LogBufferDispatcher.init(-1);
                int port = cmd.hasOption("p") ? Integer.parseInt(cmd.getOptionValue("p")) : Constants.START_PORT;
                if (cmd.getOptionValue("h") != null) {
                    CommonConstants.HOSTNAME = cmd.getOptionValue("h");
                    System.setProperty("java.rmi.server.hostname", cmd.getOptionValue("h"));
                }
                if (cmd.getOptionValue("r") != null) CommonConstants.RMI_REGISTRY_PORT = Integer.parseInt(cmd.getOptionValue("r"));
                EndpointDaemonImpl.createDaemon(port);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void printUsage(String msg) {
        System.out.println("SemWIQ Endpoint Daemon");
        System.out.println("(C)2009, Andreas Langegger, al@jku.at, Institute for Applied Knowledge Processing, J. Kepler University Linz, Austria");
        if (msg != null) System.out.println(msg + '\n');
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("parameters: ", opts);
    }
}
