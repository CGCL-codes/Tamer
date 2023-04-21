package br.mps.eti.siljac.plugins;

import java.io.FileInputStream;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.log4j.Logger;
import br.mps.eti.siljac.dataprovider.DataProvider;
import br.mps.eti.siljac.util.PropertyManager;

/**
 * Siljac plugin to get a IPV4 number of a network interface
 * 
 * @author <a href="mailto:mpserafim@yahoo.com.br">Marcos Paulo Serafim</a>
 * @since 0.0.1
 */
public class IPNumber implements DataProvider {

    private static final Logger _log = Logger.getLogger(IPNumber.class);

    private static final String DOWN = "\"interface down\"";

    private static final String IPADDR_FILE = "ipnumber.properties";

    private static final String INTERFACE = "interface";

    private String interf;

    public void initialize() {
        if (_log.isDebugEnabled()) _log.debug(PropertyManager.getInstance().getProperty("siljac.plugin.initialization"));
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(IPADDR_FILE));
            interf = p.getProperty(INTERFACE);
        } catch (Exception e) {
            _log.error(PropertyManager.getInstance().getProperty("siljac.plugin.error"), e);
            e.printStackTrace();
        }
    }

    public String getData() {
        if (_log.isDebugEnabled()) _log.debug(PropertyManager.getInstance().getProperty("siljac.plugin.getting.data"));
        try {
            NetworkInterface i = NetworkInterface.getByName(interf);
            Enumeration en = i.getInetAddresses();
            Object o = null;
            Inet4Address addr = null;
            if (en != null) {
                while (en.hasMoreElements()) {
                    o = en.nextElement();
                    if (o instanceof Inet4Address) {
                        addr = (Inet4Address) o;
                        break;
                    }
                }
            }
            if (addr != null) return addr.toString();
        } catch (Exception e) {
            _log.error(PropertyManager.getInstance().getProperty("siljac.plugin.error"), e);
            e.printStackTrace();
        }
        return DOWN;
    }
}
