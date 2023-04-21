package ch.bbv.dnm.users.common;

import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import ch.bbv.cmdlineparser.CmdLineBuilder;

public class UserApplicationConstants {

    /**
   * The name of the ORB subsystem as registered.
   */
    public static final String ORB_NAME = "CORBA_ORB";

    /**
   * The schema name of the database containing the persistent data.
   */
    public static final String SCHEMA = "orm-dnm";

    /**
   * The mode for the 2-tier mode of the application.
   */
    public static final String USE_DIRECT = "direct";

    /**
   * The mode for the CORBA mode of the application.
   */
    public static final String USE_CORBA = "corba";

    /**
   * The mode for the J2EE mode of the application.
   */
    public static final String USE_J2EE = "j2ee";

    /**
   * The mode for the OJB ORM mode of the application.
   */
    public static final String USE_OJB = "ojb";

    /**
   * The mode for the hibernate ORM mode of the application.
   */
    public static final String USE_HIBERNATE = "hibernate";

    /**
   * Mode for the configuration of the object-relation mapping subsystem.
   */
    public static final String USE_REPLACE = "REPLACE";

    /**
   * Mode for the configuration of the object-relation mapping subsystem.
   */
    public static final String USE_SYNCHRONIZE = "SYNCHRONIZE";

    /**
   * Mode for the configuration of the object-relation mapping subsystem.
   */
    public static final String USE_DETACHED = "DETACHED";

    /**
   * Creates the command line of the application.
   * @return the command line of the test application
   */
    public static Options createCommandLine() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        options.addOption(CmdLineBuilder.createNoArgOption("h", "help", "display the list of available commands", false));
        group.addOption(CmdLineBuilder.createNoArgOption("v", "verbose", "set the logging level to verbose", false));
        group.addOption(CmdLineBuilder.createNoArgOption("q", "quiet", "set the logging level to quiet", false));
        options.addOptionGroup(group);
        options.addOption(CmdLineBuilder.createArgOption("m", "mode", "defines the mode of the application [direct | CORBA | J2EE]", "mode", false, String.class));
        options.addOption(CmdLineBuilder.createArgOption("o", "orm", "defines the ORM framework [ojb | hibernate]", "orm", true, String.class));
        options.addOption(CmdLineBuilder.createArgOption("s", "synch", "defines the O/R synchronization [replace | synchronize | detach]", "synch", false, String.class));
        options.addOption(CmdLineBuilder.createArgOption("ORBInitialPort", null, "the ORB TCP/IP port", "port", false, String.class));
        options.addOption(CmdLineBuilder.createArgOption("ORBInitialHost", null, "the host where the ORB is running", "host", false, String.class));
        options.addOption(CmdLineBuilder.createJavaProperty());
        return options;
    }
}
