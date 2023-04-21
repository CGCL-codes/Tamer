package jws.core.config;

import java.util.ArrayList;
import java.util.Collection;
import org.dom4j.Element;

final class RemoteModuleRefConf extends ConfigurationObject {

    private TransportConf _tx;

    private ArrayList<ServiceProxyConf> _proxies;

    public RemoteModuleRefConf(Element root) throws ConfigurationException {
        super(root);
        _tx = new TransportConf(root.element("transport"));
        _proxies = new ArrayList<ServiceProxyConf>();
        for (Object obj : root.elements("service-proxy")) {
            Element elem = (Element) obj;
            ServiceProxyConf conf = new ServiceProxyConf(elem, _tx);
            _proxies.add(conf);
        }
    }

    public Collection<ServiceProxyConf> getProxies() {
        return new ArrayList<ServiceProxyConf>(_proxies);
    }

    public TransportConf getTransport() {
        return _tx;
    }
}
