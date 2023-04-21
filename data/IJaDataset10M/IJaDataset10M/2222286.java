package org.torweg.pulse.util.streamscanner;

import junit.framework.TestCase;
import org.torweg.pulse.TestingEnvironment;
import org.torweg.pulse.util.xml.XMLConverter;

/**
 * @author Thomas Weber
 * @version $Revision$
 */
public class TestConfig extends TestCase {

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        new TestingEnvironment();
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConfig() throws Exception {
        StreamScannerChainConfig chainConfig = new StreamScannerChainConfig();
        StreamScannerConfig conf1 = new StreamScannerConfig();
        conf1.setScannerClass("my.fqn.classname");
        chainConfig.addScannerConfiguration(conf1);
        ClamAVScannerConfig conf2 = new ClamAVScannerConfig();
        conf2.setHostAndPort("localhost", 1234);
        chainConfig.addScannerConfiguration(conf2);
        System.out.println(XMLConverter.marshal(chainConfig));
    }
}
