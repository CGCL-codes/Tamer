package org.intelligentsia.keystone.updater;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.intelligentsia.keystone.boot.Command;

/**
 * Update is an example of class which use WebUdate.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class Update implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, String> arguments;

    private final WebUpdate updater;

    public static void main(final String[] args) {
        new Update(args).run();
    }

    public Update(final String[] args) {
        arguments = Command.loadArguments(args);
        updater = new WebUpdate();
    }

    @Override
    public void run() {
        logger.trace("Update Running with {}", arguments);
        if (Command.getBooleanArgument(arguments, "update", Boolean.FALSE)) {
            try {
                if (updater.checkForUpdate()) {
                    logger.debug("Find Update");
                    logger.debug("Downloaded {}", updater.download());
                }
            } catch (final Throwable t) {
                logger.error("Error occurs when updating ", t);
            }
        }
        if (!Command.getBooleanArgument(arguments, "norestart", Boolean.TRUE)) {
            restart();
        }
    }

    /**
	 * Process a restart in order to install all updates.
	 */
    public void restart() {
        try {
            final String home = System.getProperties().getProperty("BootStrap.home", new File(".").getPath());
            String location = System.getProperties().getProperty("BootStrap.location");
            if ((updater.getTarget() != null) && !"".equals(updater.getTarget())) {
                location = new File(new File(home), updater.getTarget()).getPath();
            }
            logger.debug("Restart using location {}", location);
            Command.run("-jar", "tools" + File.separator + "keystone.jar", "--BootStrap.restartAfterUpdate", "--home=" + home, "--target=" + location);
            Thread.sleep(1000);
        } catch (final IOException ex) {
            logger.error("On restart", ex);
        } catch (final InterruptedException ex) {
            logger.error("On restart", ex);
        }
    }
}
