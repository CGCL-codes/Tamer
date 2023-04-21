package edu.ucdavis.genomics.metabolomics.binbase.bci;

import java.io.File;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.util.RocksClusterFactoryImpl;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.util.RocksClusterImplementation;
import edu.ucdavis.genomics.metabolomics.util.PropertySetter;
import edu.ucdavis.genomics.metabolomics.util.io.source.FileSource;

/**
 * cluster implementation for the scheduler test
 * 
 * @author wohlgemuth
 * 
 */
public class RocksClusterSchedulerIntegrationTest extends AbstractSchedulerIntegrationTest {

    /**
	 * needed to tell the cluster util the correct implementation 
	 * @throws Exception
	 */
    @BeforeClass
    public static void initializeCluster() throws Exception {
        Logger logger = Logger.getLogger(RocksClusterSchedulerIntegrationTest.class);
        PropertySetter.setPropertiesToSystem("src/test/resources/test.properties");
        UTIL = new RocksClusterImplementation(System.getProperty("test.binbase.cluster.server"), System.getProperty("test.binbase.cluster.username"), System.getProperty("test.binbase.cluster.password"));
        RocksClusterSchedulerIntegrationTest.initializeUtil();
        File dir = new File("target/bci-integration-test.dir/");
        for (File lib : dir.listFiles()) {
            logger.info("uploading libary: " + lib.getName());
            UTIL.uploadLibrary(lib.getName(), new FileSource(lib));
        }
    }

    @Override
    protected String getClusterFactoryClass() {
        return RocksClusterFactoryImpl.class.getName();
    }
}
