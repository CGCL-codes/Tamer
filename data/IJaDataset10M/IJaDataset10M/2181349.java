package com.ractoc.pffj.test.runner.registry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.ractoc.pffj.core.PluginController;

/**
 * Starts up the PluginController for the distributed writer test. This is a
 * plugin controller that just responds to incoming messages, so no action is
 * taken here. The TestDistributedWriter has to be started BEFORE the
 * TestDistributedReader.
 * 
 * @author Mark
 * @version 0.1
 */
public final class TestRegistryWriterRunner {

    private static final String FATAL_ERROR = "Fatal error in the PluginController";

    private static Logger logger = Logger.getLogger(TestRegistryWriterRunner.class.getName());

    private TestRegistryWriterRunner() {
    }

    /**
     * Startup method for the PluginController.
     * 
     * @param args
     *            Commandline parameters. Currently, no parameters are
     *            supported.
     */
    public static void main(final String[] args) {
        PropertyConfigurator.configureAndWatch("/log4j.xml");
        final ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
        try {
            logger.debug("Starting the controller");
            threadExecutor.submit((Callable<Integer>) new PluginController("/config/writerRegistry.xml")).get();
        } catch (final InterruptedException e) {
            logger.error(FATAL_ERROR, e);
            System.exit(-1);
        } catch (final ExecutionException e) {
            logger.error(FATAL_ERROR, e);
            System.exit(-2);
        }
    }
}
