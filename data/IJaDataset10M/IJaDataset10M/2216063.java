package org.jsmtpd.plugins;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.IGenericPlugin;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.PluginStore;
import org.jsmtpd.core.common.acl.IACL;
import org.jsmtpd.core.common.dnsService.IDNSResolver;
import org.jsmtpd.tools.cache.ICache;
import org.jsmtpd.tools.cache.SimpleCache;

/**
 * refact: extract code from RBFilter (IP)
 * @author jfp
 *
 */
public abstract class AbstractRblPlugin implements IGenericPlugin {

    private List<String> serverList = new ArrayList<String>();

    protected Log log = LogFactory.getLog(AbstractRblPlugin.class);

    private IDNSResolver resolver = null;

    private IACL acl = null;

    private boolean bypassLocal = true;

    private ICache<String, Boolean> resolved = new SimpleCache<String, Boolean>(500);

    public boolean checkHost(InetAddress input) {
        String ipString = ((Inet4Address) input).getHostAddress();
        if (bypassLocal && acl.isValidRelay(ipString)) return true;
        Boolean check = resolved.get(ipString);
        if (check == null) {
            check = isListedOnServers(ipString, (Inet4Address) input);
            resolved.cache(ipString, check);
        }
        return check;
    }

    private boolean isListedOnServers(String ip, Inet4Address ipAddress) {
        String[] parts = ((Inet4Address) ipAddress).getHostAddress().split("\\.");
        String reverse = parts[3] + "." + parts[2] + "." + parts[1] + "." + parts[0];
        for (String element : serverList) {
            String toCheck = reverse + "." + element;
            log.debug("Client ip: " + ip + " RBL filtering with " + element);
            if (resolver.exists(toCheck)) {
                log.warn("Client ip: " + ip + " rejected by RBLFilter plugin on server " + element);
                return false;
            }
        }
        return true;
    }

    public void initPlugin() throws PluginInitException {
        resolver = PluginStore.getInstance().getResolver();
        if (resolver == null) throw new PluginInitException();
        acl = PluginStore.getInstance().getAcl();
        if (acl == null) throw new PluginInitException();
    }

    public void setRBLServer(String serv) {
        serverList.add(serv);
    }

    public void setBypassLocal(boolean b) {
        bypassLocal = b;
    }

    public abstract String getPluginName();

    public void shutdownPlugin() {
    }
}
