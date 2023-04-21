package edu.vt.middleware.ldap.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import edu.vt.middleware.ldap.ConnectionFactory;
import edu.vt.middleware.ldap.Credential;
import edu.vt.middleware.ldap.DefaultConnectionFactory;
import edu.vt.middleware.ldap.props.DefaultConnectionFactoryPropertySource;
import edu.vt.middleware.ldap.props.PropertySource.PropertyDomain;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Abstract base class for all CLI classes.
 *
 * @author  Middleware Services
 * @version  $Revision: 2197 $
 */
public abstract class AbstractCli {

    /** option to print usage. */
    protected static final String OPT_HELP = "help";

    /** option for provider properties. */
    protected static final String OPT_PROVIDER_PROPERTIES = "providerProperties";

    /** command line options. */
    protected Options options = new Options();

    /** whether to output dsml version 1, the default is ldif. */
    protected boolean outputDsmlv1;

    /**
   * Parses command line options and dispatches to the requested action, or the
   * default action if no action is specified.
   *
   * @param  args  command line arguments
   */
    public final void performAction(final String[] args) {
        initOptions();
        try {
            if (args.length > 0) {
                final CommandLineParser parser = new GnuParser();
                final CommandLine line = parser.parse(options, args, false);
                dispatch(line);
            } else {
                printExamples();
            }
        } catch (ParseException pex) {
            System.err.println("Failed parsing command arguments: " + pex.getMessage());
        } catch (IllegalArgumentException iaex) {
            String msg = "Operation failed: " + iaex.getMessage();
            if (iaex.getCause() != null) {
                msg += " Underlying reason: " + iaex.getCause().getMessage();
            }
            System.err.println(msg);
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            System.err.println("Operation failed:");
            ex.printStackTrace(System.err);
        }
    }

    /** Initialize CLI options. */
    protected void initOptions() {
        options.addOption(new Option(OPT_HELP, false, "display all options"));
    }

    /**
   * Initialize a connection factory with command line options.
   *
   * @param  line  parsed command line arguments
   *
   * @return  connection factory that has been initialized
   *
   * @throws  Exception  if a connection config cannot be created
   */
    protected ConnectionFactory initConnectionFactory(final CommandLine line) throws Exception {
        final DefaultConnectionFactory factory = new DefaultConnectionFactory();
        final DefaultConnectionFactoryPropertySource cfSource = new DefaultConnectionFactoryPropertySource(factory, getPropertiesFromOptions(PropertyDomain.LDAP.value(), line));
        cfSource.initialize();
        if (factory.getConnectionConfig().getBindDn() != null && factory.getConnectionConfig().getBindCredential() == null) {
            final char[] pass = System.console().readPassword("[Enter password for %s]: ", factory.getConnectionConfig().getBindDn());
            factory.getConnectionConfig().setBindCredential(new Credential(pass));
        }
        return factory;
    }

    /**
   * Returns the name of the command for which this class provides a CLI
   * interface.
   *
   * @return  name of CLI command
   */
    protected abstract String getCommandName();

    /**
   * Dispatch command line data to the active that can perform the operation
   * requested on the command line.
   *
   * @param  line  parsed command line arguments
   *
   * @throws  Exception  on errors thrown by action
   */
    protected abstract void dispatch(final CommandLine line) throws Exception;

    /** Prints CLI help text. */
    protected void printHelp() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getCommandName(), options);
    }

    /** Prints CLI usage examples. */
    protected void printExamples() {
        final String name = getClass().getSimpleName();
        final InputStream in = getClass().getResourceAsStream(name + ".examples");
        if (in != null) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            try {
                System.out.println();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading examples from resource stream.");
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    System.err.println("Error closing example resource stream.");
                }
                System.out.println();
            }
        } else {
            System.out.println("No usage examples available for " + getCommandName());
        }
    }

    /**
   * Returns the command line argument descriptions for this CLI.
   *
   * @param  classes  that contain arguments used by this CLI
   *
   * @return  map of argument name to description
   */
    protected Map<String, String> getArgDesc(final Class<?>... classes) {
        final Map<String, String> args = new HashMap<String, String>();
        for (Class<?> c : classes) {
            final String name = c.getSimpleName();
            final InputStream in = getClass().getResourceAsStream(name + ".args");
            if (in != null) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                try {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        final String[] s = line.split(":");
                        if (s.length > 1) {
                            args.put(s[0], s[1]);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading arguments from resource stream.");
                } finally {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        System.err.println("Error closing arguments resource stream.");
                    }
                }
            }
        }
        if (args.isEmpty()) {
            System.err.println("No arguments available for " + getCommandName());
        }
        return args;
    }

    /**
   * Reads the options from the supplied command line and returns a properties
   * containing those options.
   *
   * @param  domain  to place property names in
   * @param  line  command line
   *
   * @return  properties for each option and value
   */
    protected Properties getPropertiesFromOptions(final String domain, final CommandLine line) {
        final Properties props = new Properties();
        for (Option o : line.getOptions()) {
            if (o.hasArg()) {
                if (o.getOpt().equals(OPT_PROVIDER_PROPERTIES)) {
                    final String[] s = o.getValue().split("=");
                    props.setProperty(s[0], s[1]);
                } else {
                    props.setProperty(domain + o.getOpt(), o.getValue());
                }
            }
        }
        return props;
    }
}
