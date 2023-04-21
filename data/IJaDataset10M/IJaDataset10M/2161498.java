package jorgan;

import java.util.logging.Level;
import java.util.logging.Logger;
import jorgan.bootstrap.Classpath;
import jorgan.bootstrap.Exceptions;
import jorgan.bootstrap.Logging;
import jorgan.bootstrap.Main;

/**
 * Bootstrapping for {@link App}.
 */
public class Bootstrap {

    private static Logger logger = Logger.getLogger(Bootstrap.class.getName());

    public void start(final String[] args) {
        try {
            new Logging();
            new Exceptions(logger);
            new Classpath("lib");
            new Main("jorgan.App", args);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "bootstrapping failed", t);
        }
    }

    public static void main(final String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start(args);
    }
}
