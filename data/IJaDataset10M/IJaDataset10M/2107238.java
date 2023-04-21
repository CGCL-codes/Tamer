package com.hyper9.vmm.server;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.*;
import com.hyper9.vmm.client.ConnectionInfo;
import com.hyper9.vmm.client.ServerObjectHost;
import com.hyper9.vmm.client.ServerObjectVM;
import com.hyper9.vmm.server.AddServerObjectCallback;
import com.hyper9.vmm.server.SessionAttrs;
import com.hyper9.vmm.server.XenServer5Plugin;
import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Session;

/**
 * The test class for XenServer5Plugin.
 * 
 * @author akutz
 * 
 */
public class XenServer5PluginTest {

    /**
     * Tests the getConnection(ConnectionInfo connInfo) method.
     * 
     * @throws Exception
     */
    @Test
    public void getConnectionTest() throws Exception {
        XenServer5Plugin plugin = getPlugin();
        ConnectionInfo ci = getConnInfo();
        Connection c = plugin.getConnection(ci);
        String sref = c.getSessionReference();
        assertNotNull("got session reference", sref);
        writeConnInfo(plugin.getRequest(), ci);
        plugin.getRequest().getSession().setAttribute(XenServer5Plugin.SESSION_REF, sref);
        c = plugin.getConnection();
        sref = c.getSessionReference();
        assertNotNull("got session reference", sref);
        Session.logout(c);
    }

    /**
     * Tests the login(ConnectionInfo connInfo) method.
     * 
     * @throws Exception
     */
    @Test
    public void loginTest() throws Exception {
        XenServer5Plugin plugin = getPlugin();
        ConnectionInfo ci = getConnInfo();
        boolean loggedIn = plugin.login(ci);
        assertTrue("login successful", loggedIn);
        writeConnInfo(plugin.getRequest(), ci);
        plugin.logout();
        ci.setPassword("");
        loggedIn = plugin.login(ci);
        assertFalse("login failed", loggedIn);
    }

    /**
     * Tests the getHost() method.
     * 
     * @throws Exception
     */
    @Test
    public void getHostTest() throws Exception {
        XenServer5Plugin plugin = getPlugin();
        ConnectionInfo ci = getConnInfo();
        boolean loggedIn = plugin.login(ci);
        assertTrue("login successful", loggedIn);
        writeConnInfo(plugin.getRequest(), ci);
        ServerObjectHost soHost = plugin.getHost(new ServerObjectHost().getAllPropertiesMask());
        Assert.assertEquals("scruffy", soHost.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getVMsTest() throws Exception {
        XenServer5Plugin plugin = getPlugin();
        ConnectionInfo ci = getConnInfo();
        boolean loggedIn = plugin.login(ci);
        assertTrue("login successful", loggedIn);
        writeConnInfo(plugin.getRequest(), ci);
        final List<ServerObjectVM> vms = new ArrayList<ServerObjectVM>();
        plugin.getVMs(new ServerObjectVM().getAllPropertiesMask(), new AddServerObjectCallback<ServerObjectVM>() {

            public void add(ServerObjectVM serverObject) throws Exception {
                vms.add(serverObject);
            }
        });
        assertTrue(vms.size() == 1);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void setNetworkTest() throws Exception {
        XenServer5Plugin plugin = getPlugin();
        ConnectionInfo ci = getConnInfo();
        boolean loggedIn = plugin.login(ci);
        assertTrue("login successful", loggedIn);
        writeConnInfo(plugin.getRequest(), ci);
        ServerObjectVM soVM = new ServerObjectVM("9ed05096-e2d6-2fab-73cc-194e0a9316bd", "odin.lostcreations.local");
        plugin.setNetwork(soVM, true);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void sendPowerOpTest() throws Exception {
        XenServer5Plugin plugin = getPlugin();
        ConnectionInfo ci = getConnInfo();
        boolean loggedIn = plugin.login(ci);
        assertTrue("login successful", loggedIn);
        writeConnInfo(plugin.getRequest(), ci);
    }

    private ConnectionInfo getConnInfo() {
        ConnectionInfo ci = new ConnectionInfo();
        ci.setUserName("root");
        ci.setPassword("password");
        ci.setServerAddress("192.168.0.44");
        ci.setUseSsl(true);
        ci.setIgnoreCert(true);
        ci.setPort(443);
        return ci;
    }

    private XenServer5Plugin getPlugin() {
        FakeHttpRequest req = new FakeHttpRequest();
        req.getSession();
        XenServer5Plugin x = new XenServer5Plugin();
        x.init(req, "transactionID");
        return x;
    }

    private void writeConnInfo(HttpServletRequest req, ConnectionInfo connInfo) {
        req.getSession().setAttribute(SessionAttrs.SERVER_ADDRESS, connInfo.getServerAddress());
        req.getSession().setAttribute(SessionAttrs.SERVER_PORT, connInfo.getPort());
        req.getSession().setAttribute(SessionAttrs.USE_SSL, connInfo.isUseSsl());
        req.getSession().setAttribute(SessionAttrs.IGNORE_CERT, connInfo.isIgnoreCert());
    }
}
