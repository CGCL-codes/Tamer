package lv.ante.xwiki.testdigester.http;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ProxySettingsTest {

    private ProxySettings proxySettings;

    @Before
    public void setUp() {
        proxySettings = new ProxySettings();
        proxySettings.setEnabled(true);
        proxySettings.setExcludedHosts("localhost|127.0.0.1");
        proxySettings.setHttpProxyHost("proxy.accenture.lv");
        proxySettings.setHttpProxyPort(8080);
    }

    @Test
    public void testExcluded() {
        assertTrue(proxySettings.isExcludedAddress("http://localhost:9080/someaddress"));
    }

    @Test
    public void testNotExcluded() {
        assertFalse(proxySettings.isExcludedAddress("http://adt:8080/someaddress"));
    }
}
