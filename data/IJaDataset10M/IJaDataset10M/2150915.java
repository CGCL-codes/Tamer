package jade;

import jade.core.MicroRuntime;
import jade.util.leap.Properties;
import jade.util.Logger;
import java.io.IOException;

/**
   Main class to start JADE as a split-container.
   @author Giovanni Caire - TILAB
 */
public class MicroBoot {

    private static Logger logger = Logger.getMyLogger("jade.MicroBoot");

    /**
       Default constructor.
	 */
    public MicroBoot() {
    }

    /**
	 * Fires up the <b><em>JADE</em></b> runtime.
	 */
    public static void main(String args[]) {
        String propsFile = null;
        try {
            Properties props = parseCmdLineArgs(args);
            propsFile = props.getProperty("conf");
            if (propsFile != null) {
                props.load(propsFile);
            }
            Logger.initialize(props);
            if (props.getProperty(MicroRuntime.JVM_KEY) == null) {
                props.setProperty(MicroRuntime.JVM_KEY, MicroRuntime.J2SE);
            }
            MicroRuntime.startJADE(props, new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                    }
                    logger.log(Logger.INFO, "Exiting now!");
                    System.exit(0);
                }
            });
        } catch (IllegalArgumentException iae) {
            logger.log(Logger.SEVERE, "Error reading command line configuration properties. " + iae.getMessage());
            iae.printStackTrace();
            printUsage();
            System.exit(-1);
        } catch (IOException ioe) {
            logger.log(Logger.SEVERE, "Error reading configuration properties from file " + propsFile + ".", ioe);
            printUsage();
            System.exit(-1);
        }
    }

    private static Properties parseCmdLineArgs(String[] args) throws IllegalArgumentException {
        Properties props = new Properties();
        int i = 0;
        while (i < args.length) {
            if (args[i].startsWith("-")) {
                String name = args[i].substring(1);
                if (++i < args.length) {
                    props.setProperty(name, args[i]);
                } else {
                    throw new IllegalArgumentException("No value specified for property \"" + name + "\"");
                }
                ++i;
            } else {
                if (props.getProperty(MicroRuntime.AGENTS_KEY) != null) {
                    if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "WARNING: overriding agents specification set with the \"-agents\" option");
                }
                String agents = args[i];
                props.setProperty(MicroRuntime.AGENTS_KEY, args[i]);
                if (++i < args.length) {
                    if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "WARNING: ignoring command line argument " + args[i] + " occurring after agents specification");
                    if (agents != null && agents.indexOf('(') != -1 && !agents.endsWith(")")) {
                        if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "Note that agent arguments specifications must not contain spaces");
                    }
                    if (args[i].indexOf(':') != -1) {
                        if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "Note that agent specifications must be separated by a semicolon character \";\" without spaces");
                    }
                }
                break;
            }
        }
        return props;
    }

    private static void printUsage() {
        logger.log(Logger.ALL, "Usage:");
        logger.log(Logger.ALL, "java -cp <classpath> jade.MicroBoot [options] [agents]");
        logger.log(Logger.ALL, "Options:");
        logger.log(Logger.ALL, "    -conf <file-name>. Read configuration properties from the specified file name");
        logger.log(Logger.ALL, "    -host <host-name>. The name/address of the host where the BackEnd has to be created");
        logger.log(Logger.ALL, "    -port <port-number>. The port of the J2SE container active on \"host\"");
        logger.log(Logger.ALL, "    -<key> <value>");
        logger.log(Logger.ALL, "Agents: [-agents] <semicolon-separated agent-specifiers>");
        logger.log(Logger.ALL, "     where agent-specifier = <agent-name>:<agent-class>[(comma separated args)]\n");
    }
}
