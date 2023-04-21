package nubric;

import sun.misc.Signal;
import java.io.File;
import nubric.core.Core;

/**
 * This class is holding the Framework's static main which is needed to
 * start the whole thing up.
 * 
 * @author Bastian Preindl, bastian@preindl.net
 * @version 0.1.2
 */
public class Nubric {

    private static String version = "IronGate unstable v0.1.2";

    /**
     * Check's if the configuration file exists and instantiates <code>Core</code>
     * 
     * @param args
     *  <br>arg[0]: Configuration file path<br>
     *  arg[1]: [debug] mode on/off
     *  
     *  @see <code>Core</code>
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            String pathToProperties = args[0];
            File file = new File(pathToProperties);
            if (file.canRead()) {
                boolean debug = false;
                if (args.length == 2) {
                    if (args[1].matches("debug")) {
                        debug = true;
                    } else {
                        usage();
                        System.exit(1);
                    }
                }
                Core core = new Core(pathToProperties, debug);
                Signal.handle(new Signal("INT"), new IronGateSignalHandler(core));
                Signal.handle(new Signal("TERM"), new IronGateSignalHandler(core));
                core.init();
            } else {
                System.out.println("Error: Property file not found: " + pathToProperties);
                usage();
            }
        } else {
            usage();
        }
    }

    /**
     * Is called if the syntax was wrong - prints out a usage()-String and exits
     *
     */
    public static void usage() {
        System.out.println(version + "\n" + "Usage: nubric pathToPropertyFile [debug]");
    }
}
