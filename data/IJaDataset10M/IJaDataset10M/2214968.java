package com.hyper9.vmm.client;

import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * This is the test class for Properties.
 * 
 * @author akutz
 * 
 */
public class PropertiesTest {

    private static Properties clientProps;

    private static Properties serverProps;

    private static Properties getClientProps() throws Exception {
        if (clientProps != null) return clientProps;
        java.util.Properties p = new java.util.Properties();
        p.load(new FileInputStream("/etc/vmm/vmm.properties"));
        Map<String, String> map = new HashMap<String, String>();
        for (Object ok : p.keySet()) {
            String k = (String) ok;
            if (k.matches("com.hyper9.vmm.(?!server\\.).*")) {
                String v = p.getProperty(k);
                map.put(k, v);
            }
        }
        clientProps = new com.hyper9.vmm.client.Properties(map);
        return clientProps;
    }

    private static Properties getServerProps() throws Exception {
        if (serverProps != null) return serverProps;
        java.util.Properties p = new java.util.Properties();
        p.load(new FileInputStream("/etc/vmm/vmm.properties"));
        Map<String, String> map = new HashMap<String, String>();
        for (Object ok : p.keySet()) {
            String k = (String) ok;
            if (k.matches("com.hyper9.vmm.(?!client\\.).*")) {
                String v = p.getProperty(k);
                map.put(k, v);
            }
        }
        serverProps = new com.hyper9.vmm.client.Properties(map);
        return serverProps;
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getPropertyTest1() throws Exception {
        Properties p = getClientProps();
        String v = p.getProperty("client", "serverTypeAliases");
        assertTrue("vmware,hyperv,xen5,demo".equals(v));
        v = getClientProps().getProperty(null, "serverTypeAliases");
        assertTrue("vmware,hyperv,xen5,demo".equals(v));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getPropertyTest2() throws Exception {
        String v = getServerProps().getProperty("server", null, "demo", "hostName");
        assertTrue("vmmdemo".equals(v));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getPropertyTest3() throws Exception {
        String v = getServerProps().getProperty("server", "vmware", "esx01", "akutz", "vm01.hyper9.com", "ie7", "port");
        assertTrue("443".equals(v));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getPropertyTest4() throws Exception {
        String v = getServerProps().getProperty("client", "vmware", "vi35", "displayName");
        assertTrue("VI 3.5".equals(v));
    }
}
