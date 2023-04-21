package com.aqua.iperf.example;

import jsystem.framework.sut.ChangeSutTest;
import junit.framework.SystemTestCase;
import com.aqua.iperf.Iperf;

/**
 * This class contains tests that verifies the correct installation of Iperf utility (if needed):
 * 	When Iperf already installed
 *  When Iperf installed on client only
 *  When Iperf installed on server only
 *  When Iperf is not installed where it should be (test fail)
 * Each test uses different SUT, all starting with IperfInstall*.xml SUT
 * Each test must be performed once on Windows and once on Linux.
 * 
 * @author shmir
 *
 */
public class IperfBase extends SystemTestCase {

    protected Iperf iperf = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        iperf = (Iperf) system.getSystemObject("iperf");
    }

    protected void changeSut(String sut) throws Exception {
        ChangeSutTest cst = new ChangeSutTest();
        cst.setSut(sut + ".xml");
        cst.changeSut();
        iperf = (Iperf) system.getSystemObject("iperf");
    }
}
