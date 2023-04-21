package com.rcreations.network;

import java.net.NetworkInterface;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Robert Chou
 */
public class NicUtilsTest extends TestCase {

    public NicUtilsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    protected void tearDown() throws Exception {
    }

    /**
    */
    void dumpNics(ArrayList<NetworkInterface> nics) {
        for (NetworkInterface nic : nics) {
            NicUtils.dumpNic(nic);
        }
    }

    /**
    * Test of getNonLocalNicList method, of class org.llnl.common.NicUtils.
    */
    public void testGetNonLocalNicList() {
        System.out.println("getNonLocalNicList");
        ArrayList<NetworkInterface> result = NicUtils.getNonLocalNicList();
        dumpNics(result);
    }

    /**
    * Test of getNonLocalNicListAllowWaitOnDhcp method, of class org.llnl.common.NicUtils.
    */
    public void testGetNonLocalNicListAllowWaitOnDhcp() {
        System.out.println("getNonLocalNicListAllowWaitOnDhcp");
        ArrayList<NetworkInterface> result = NicUtils.getNonLocalNicListAllowWaitOnDhcp();
        dumpNics(result);
    }
}
