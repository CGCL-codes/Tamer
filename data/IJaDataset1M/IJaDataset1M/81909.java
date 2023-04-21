package org.infoeng.ofbiz.edi.test.container;

import org.infoeng.ofbiz.edi.container.*;
import junit.framework.TestCase;
import java.io.*;
import java.util.*;
import java.security.*;
import org.infoeng.ofbiz.ltans.util.LtansUtils;
import org.ofbiz.base.container.ContainerConfig;

public class OpenAS2ContainerTest extends TestCase {

    public void testOne() throws Exception {
        KeyStore ks = LtansUtils.getDefaultKeyStore();
        Properties props = LtansUtils.getDefaultProperties();
        String configFile = props.getProperty("ltans.ofbiz.containerFile");
        ContainerConfig.Container cfg = ContainerConfig.getContainer("edi", configFile);
        ContainerConfig.Container.Property ediSessionCfg = cfg.getProperty("edi-session-config");
        System.out.println("configFile: " + configFile + "");
        OpenAS2Container container = new OpenAS2Container();
        String[] args = null;
        container.init(args, configFile);
        container.start();
        container.stop();
    }
}
