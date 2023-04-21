package edu.ucdavis.genomics.metabolomics.binbase.cluster.test.integration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.ClusterUtil;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.ejb.client.Configurator;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.streaming.BinBaseMonitor;

/**
 * the basic generation to have an integration test running for a cluster. Does
 * nothign else than to bring the application servern and cluster in a defined
 * state.
 * 
 * @author wohlgemuth
 * 
 */
public abstract class AbstractClusterTest extends AbstractApplicationServerTest {

    /**
	 * needed util which needs to be initialized in the sub classes
	 */
    public static ClusterUtil UTIL = null;

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        UTIL.destroy();
    }

    /**
	 * standard setup of the cluster util
	 * 
	 * @throws Exception
	 */
    public static void initializeUtil() throws Exception {
        if (System.getProperty("test.binbase.cluster.application-server") != null) {
            System.out.println("using defined application server: " + System.getProperty("test.binbase.cluster.application-server"));
            UTIL.initializeCluster(System.getProperty("test.binbase.cluster.application-server"));
        } else {
            System.out.println("using cluster frontend as application server: " + System.getProperty("test.binbase.cluster.server"));
            UTIL.initializeCluster(System.getProperty("test.binbase.cluster.server"));
        }
        UTIL.prepare();
    }

    protected abstract String getClusterFactoryClass();

    /**
	 * clears all calculation queues and kills all jobs on the cluster. It also
	 * reinitialized the event processing system.
	 * 
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        logger.info("setting cluster service up");
        if (UTIL == null) {
            logger.error("you need to setup the util in one of the subclasses. Please define a method called: \"public static void setUpBeforeClass() throws Exception\" and add the annotation \"@BeforeClass\" The name of the variable to setup is UTIL");
            Assert.fail("you need to setup the util in one of the subclasses. Please define a method called: \"public static void setUpBeforeClass() throws Exception\" and add the annotation \"@BeforeClass\" The name of the variable to setup is UTIL");
        }
        logger.info("killing queue of the current user");
        UTIL.killJobOfCurrentUser();
        logger.info("waiting till its really empty");
        UTIL.waitTillQueueCurrentUserIsEmpty();
        logger.info("empty calculation queue of the application server");
        Configurator.getConfigService().clearQueue();
        logger.info("destroying all existing monitors");
        BinBaseMonitor.getMonitor().removeAllMonitors();
        logger.info("starting the test");
        Configurator.getConfigService().setIddleTime(5000);
        Configurator.getConfigService().setTimeout(5000);
        Configurator.getConfigService().setAutoStart(false);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
        logger.info("cleaning up after the test");
        BinBaseMonitor.getMonitor().removeAllMonitors();
        if (UTIL == null) {
            logger.error("you need to setup the util in one of the subclasses. Please define a method called: \"public static void setUpBeforeClass() throws Exception\" and add the annotation \"@BeforeClass\" The name of the variable to setup is UTIL");
            Assert.fail("you need to setup the util in one of the subclasses. Please define a method called: \"public static void setUpBeforeClass() throws Exception\" and add the annotation \"@BeforeClass\" The name of the variable to setup is UTIL");
        } else {
            logger.info("killing queue of the current user");
            UTIL.killJobOfCurrentUser();
            logger.info("waiting till its really empty");
            UTIL.waitTillQueueCurrentUserIsEmpty();
            logger.info("empty calculation queue of the application server");
            Configurator.getConfigService().clearQueue();
        }
    }
}
