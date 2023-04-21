package net.sf.oxygen.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import net.sf.oxygen.client.controller.LocalController;
import net.sf.oxygen.client.util.CommandArguments;
import org.apache.log4j.Logger;

/**
 * This class is the static entry point to launch the oxygen framework.
 * @author <A HREF="mailto:seniorc@users.sourceforge.net?subject=net.sf.oxygen.client.Main">Chris Senior</A>
 */
public abstract class Main {

    /**
   * Java VM entry point load local controller and start framework in directory
   * "fw"
   * @param args String arguments
   */
    public static void main(String[] argStrs) {
        CommandArguments args = new CommandArguments(argStrs);
        File fwRoot = new File(args.getOption(CMD_OPTION_FW_DIR, "fw"));
        fwRoot.mkdir();
        Settings.instance().addProperties(args.getOptions());
        try {
            File props = new File(args.getOption(CMD_OPTION_FW_PROPS, "oxygen.properties"));
            if (props.exists()) {
                logger.debug("Loading runtime properties from " + props);
                Properties runtimeProps = new Properties();
                runtimeProps.load(new FileInputStream(props));
                Settings.instance().addProperties(runtimeProps);
            } else logger.warn("No such file as " + props);
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        LocalController localController = new LocalController();
        localController.run();
    }

    /**
   * Logger for debug
   */
    public static final Logger logger = Logger.getLogger(Main.class);

    /**
   * Command line option to specify the framework's working directory. Default
   * is a directory called "fw" in wherever the bundle is run.
   */
    public static final String CMD_OPTION_FW_DIR = "-fwdir";

    /**
   * Command line option to specify the runtime proerties of the framework.
   */
    public static final String CMD_OPTION_FW_PROPS = "-fwdir";
}
