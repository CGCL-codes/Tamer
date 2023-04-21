package semwiq;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import at.jku.semwiq.mediator.Constants;
import at.jku.semwiq.mediator.conf.MediatorConfig;
import at.jku.semwiq.swing.SwingApp;

/**
 * @author dorgon
 * 
 * Starts the Swing GUI
 */
public class swing {

    private static Options opts;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Option cfgFile = new Option("c", "config", true, "SemWIQ Mediator configuration file");
        cfgFile.setArgName("filename");
        cfgFile.setOptionalArg(true);
        Option help = new Option("h", "help", false, "help");
        help.setOptionalArg(true);
        opts = new Options();
        opts.addOption(cfgFile);
        opts.addOption(help);
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(opts, args);
            if (cmd.hasOption("h")) {
                printUsage(null);
            } else {
                if (cmd.hasOption("c")) System.setProperty(Constants.SYSTEMPROPERTY_CONFIGFILE, cmd.getOptionValue("c"));
                new SwingApp();
            }
        } catch (Exception e) {
            printUsage(e.getMessage());
        }
    }

    private static void printUsage(String msg) {
        System.out.println("SemWIQ Mediator Swing client " + Constants.VERSION_STRING);
        System.out.println("(C)2008, Institute for Applied Knowledge Processing, J. Kepler University Linz, Austria");
        if (msg != null) System.out.println(msg + '\n');
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("parameters: ", opts);
    }
}
