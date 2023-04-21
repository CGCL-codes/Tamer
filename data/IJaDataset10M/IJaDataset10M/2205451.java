package com.hyk.proxy.client.application.gae.config;

import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hyk.proxy.client.util.GoogleAvailableService;
import com.hyk.proxy.common.Constants;
import com.hyk.proxy.common.Version;
import com.hyk.proxy.common.secure.NoneSecurityService;

/**
 *
 */
@XmlRootElement(name = "Configure")
public class Config {

    protected static Logger logger = LoggerFactory.getLogger(Config.class);

    private static Config instance = null;

    static {
        try {
            JAXBContext context = JAXBContext.newInstance(Config.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            instance = (Config) unmarshaller.unmarshal(Config.class.getResource("/" + Constants.CLIENT_CONF_NAME));
            instance.init();
        } catch (Exception e) {
            logger.error("Failed to load default config file!", e);
        }
    }

    public static enum ConnectionMode {

        HTTP2GAE(1), XMPP2GAE(2), HTTPS2GAE(3);

        int value;

        ConnectionMode(int v) {
            value = v;
        }

        public static ConnectionMode fromInt(int v) {
            return values()[v - 1];
        }
    }

    public static class SimpleSocketAddress {

        @XmlAttribute
        public String host;

        @XmlAttribute
        public int port;
    }

    public static class HykProxyServerAuth {

        @XmlAttribute
        public String appid;

        @XmlAttribute
        public String user;

        @XmlAttribute
        public String passwd;
    }

    public static enum ProxyType {

        HTTP("http"), HTTPS("https");

        String value;

        ProxyType(String v) {
            value = v;
        }

        public static ProxyType fromStr(String str) {
            if (str.equalsIgnoreCase("http")) {
                return HTTP;
            }
            if (str.equalsIgnoreCase("https")) {
                return HTTPS;
            }
            return HTTP;
        }
    }

    public static class ProxyInfo {

        @XmlElement
        public String host;

        @XmlElement
        public int port = 80;

        @XmlElement
        public String user;

        @XmlElement
        public String passwd;

        @XmlElement
        public ProxyType type = ProxyType.HTTP;

        @XmlElement
        public String nextHopGoogleServer;
    }

    public static class XmppAccount {

        private static final String GTALK_SERVER = "talk.google.com";

        private static final String GTALK_SERVER_NAME = "gmail.com";

        private static final int GTALK_SERVER_PORT = 5222;

        private static final String OVI_SERVER = "chat.ovi.com";

        private static final String OVI_SERVER_NAME = "ovi.com";

        private static final int OVI_SERVER_PORT = 5223;

        protected static final int DEFAULT_PORT = 5222;

        public XmppAccount init() {
            String server = StringUtils.parseServer(jid).trim();
            if (server.equals(GTALK_SERVER_NAME)) {
                if (null == this.serverHost || this.serverHost.isEmpty()) {
                    this.serverHost = GTALK_SERVER;
                }
                if (0 == this.serverPort) {
                    this.serverPort = GTALK_SERVER_PORT;
                }
                this.name = jid;
            } else if (server.equals(OVI_SERVER_NAME)) {
                if (null == this.serverHost || this.serverHost.isEmpty()) {
                    this.serverHost = OVI_SERVER;
                }
                if (0 == this.serverPort) {
                    this.serverPort = OVI_SERVER_PORT;
                }
                this.name = StringUtils.parseName(jid);
                this.isOldSSLEnable = true;
            } else {
                if (null == this.serverHost || this.serverHost.isEmpty()) {
                    this.serverHost = server;
                }
                if (0 == this.serverPort) {
                    this.serverPort = DEFAULT_PORT;
                }
                this.name = StringUtils.parseName(jid);
            }
            String serviceName = server;
            connectionConfig = new ConnectionConfiguration(this.serverHost, serverPort, serviceName);
            if (isOldSSLEnable) {
                connectionConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
                connectionConfig.setSocketFactory(SSLSocketFactory.getDefault());
            }
            return this;
        }

        @XmlAttribute(name = "user")
        public String jid;

        @XmlAttribute
        public String passwd;

        @XmlAttribute
        public int serverPort;

        @XmlAttribute
        public String serverHost;

        @XmlAttribute(name = "oldSSLEnable")
        public boolean isOldSSLEnable;

        @XmlTransient
        public ConnectionConfiguration connectionConfig;

        @XmlTransient
        public String name;
    }

    private List<HykProxyServerAuth> hykProxyServerAuths = new LinkedList<HykProxyServerAuth>();

    @XmlElements(@XmlElement(name = "hyk-proxy-server"))
    public void setHykProxyServerAuths(List<HykProxyServerAuth> hykProxyServerAuths) {
        this.hykProxyServerAuths = hykProxyServerAuths;
    }

    private List<XmppAccount> xmppAccounts;

    @XmlElements(@XmlElement(name = "XMPPAccount"))
    public void setXmppAccounts(List<XmppAccount> xmppAccounts) {
        this.xmppAccounts = xmppAccounts;
    }

    private int httpConnectionPoolSize;

    @XmlElement
    public void setHttpConnectionPoolSize(int httpConnectionPoolSize) {
        this.httpConnectionPoolSize = httpConnectionPoolSize;
    }

    private List<String> injectRangeHeaderSiteSet = new ArrayList<String>();

    private String injectRangeHeaderSites;

    @XmlElement
    void setInjectRangeHeaderSites(String injectRangeHeaderSites) {
        this.injectRangeHeaderSites = injectRangeHeaderSites;
        String[] sites = injectRangeHeaderSites.split(";");
        for (String s : sites) {
            injectRangeHeaderSiteSet.add(s.trim());
        }
    }

    String getInjectRangeHeaderSites() {
        return injectRangeHeaderSites;
    }

    public boolean isInjectRangeHeaderSitesMatchHost(String host) {
        for (String site : injectRangeHeaderSiteSet) {
            if (!site.isEmpty() && host.indexOf(site) != -1) {
                return true;
            }
        }
        return false;
    }

    private int rpcTimeOut;

    @XmlElement(name = "RPCTimeOut")
    public void setRpcTimeOut(int rpcTimeOut) {
        this.rpcTimeOut = rpcTimeOut;
    }

    private boolean simpleURLEnable;

    @XmlElement
    public void setSimpleURLEnable(boolean simpleURLEnable) {
        this.simpleURLEnable = simpleURLEnable;
    }

    private String compressor;

    @XmlElement
    public void setCompressor(String compressor) {
        this.compressor = compressor;
    }

    private int fetchLimitSize;

    @XmlElement
    public void setFetchLimitSize(int fetchLimitSize) {
        this.fetchLimitSize = fetchLimitSize;
    }

    private int maxFetcherNumber;

    @XmlElement
    public void setMaxFetcherNumber(int maxFetcherNumber) {
        this.maxFetcherNumber = maxFetcherNumber;
    }

    private ProxyInfo localProxy;

    @XmlElement(name = "localProxy")
    public void setHykProxyClientLocalProxy(ProxyInfo localProxy) {
        this.localProxy = localProxy;
    }

    public ProxyInfo getHykProxyClientLocalProxy() {
        return localProxy;
    }

    private ConnectionMode client2ServerConnectionMode;

    @XmlTransient
    public ConnectionMode getClient2ServerConnectionMode() {
        return client2ServerConnectionMode;
    }

    public void setClient2ServerConnectionMode(ConnectionMode client2ServerConnectionMode) {
        this.client2ServerConnectionMode = client2ServerConnectionMode;
    }

    private String httpUpStreamEncrypter;

    public String getHttpUpStreamEncrypter() {
        return httpUpStreamEncrypter;
    }

    @XmlElement
    public void setHttpUpStreamEncrypter(String httpUpStreamEncrypter) {
        this.httpUpStreamEncrypter = httpUpStreamEncrypter;
    }

    public void init() throws Exception {
        if (localProxy != null) {
            if (null != localProxy.host) {
                localProxy.host = localProxy.host.trim();
            }
            if (null != localProxy.nextHopGoogleServer) {
                localProxy.nextHopGoogleServer = localProxy.nextHopGoogleServer.trim();
                if (localProxy.nextHopGoogleServer.isEmpty()) {
                    localProxy.nextHopGoogleServer = null;
                }
            }
            if (null == localProxy.host || localProxy.host.isEmpty()) {
                localProxy = null;
            }
        }
        if (null != hykProxyServerAuths) {
            for (int i = 0; i < hykProxyServerAuths.size(); i++) {
                HykProxyServerAuth auth = hykProxyServerAuths.get(i);
                if (auth.appid == null || auth.appid.trim().isEmpty()) {
                    hykProxyServerAuths.remove(i);
                    i--;
                    continue;
                }
                if (auth.user == null || auth.user.equals("")) {
                    auth.user = Constants.ANONYMOUSE_NAME;
                }
                if (auth.passwd == null || auth.passwd.equals("")) {
                    auth.passwd = Constants.ANONYMOUSE_NAME;
                }
                auth.appid = auth.appid.trim();
                auth.user = auth.user.trim();
                auth.passwd = auth.passwd.trim();
            }
        }
        if (client2ServerConnectionMode.equals(ConnectionMode.XMPP2GAE)) {
            for (int i = 0; i < xmppAccounts.size(); i++) {
                XmppAccount account = xmppAccounts.get(i);
                if (account.jid == null || account.jid.isEmpty()) {
                    xmppAccounts.remove(i);
                    i--;
                } else {
                    account.init();
                }
            }
        }
        if (client2ServerConnectionMode.equals(ConnectionMode.XMPP2GAE) && (null == xmppAccounts || xmppAccounts.isEmpty())) {
            throw new Exception("Since the connection mode is " + ConnectionMode.XMPP2GAE + ", at least one XMPP account needed.");
        }
        if (null == httpUpStreamEncrypter) {
            httpUpStreamEncrypter = NoneSecurityService.NAME;
        }
        if (localProxy == null || localProxy.host.contains("google")) {
            simpleURLEnable = true;
        }
    }

    @XmlElement
    void setConnectionMode(int mode) {
        client2ServerConnectionMode = ConnectionMode.fromInt(mode);
    }

    int getConnectionMode() {
        return client2ServerConnectionMode.value;
    }

    public List<HykProxyServerAuth> getHykProxyServerAuths() {
        return hykProxyServerAuths;
    }

    public int getHttpConnectionPoolSize() {
        return httpConnectionPoolSize;
    }

    public int getFetchLimitSize() {
        return fetchLimitSize;
    }

    public int getRpcTimeOut() {
        return rpcTimeOut;
    }

    public boolean isSimpleURLEnable() {
        return simpleURLEnable;
    }

    public String getCompressor() {
        return compressor;
    }

    public int getMaxFetcherNumber() {
        return maxFetcherNumber;
    }

    public List<XmppAccount> getXmppAccounts() {
        return xmppAccounts;
    }

    public void clearProxy() {
        localProxy = null;
    }

    public boolean selectDefaultHttpProxy() {
        if (null == localProxy) {
            ProxyInfo info = new ProxyInfo();
            info.host = GoogleAvailableService.getInstance().getAvailableHttpService();
            if (null != info.host) {
                localProxy = info;
                return true;
            }
        }
        return false;
    }

    public boolean selectDefaultHttpsProxy() {
        if (null == localProxy) {
            ProxyInfo info = new ProxyInfo();
            info.host = GoogleAvailableService.getInstance().getAvailableHttpsService();
            info.port = 443;
            info.type = ProxyType.HTTPS;
            if (null != info.host) {
                localProxy = info;
                return true;
            }
        }
        return false;
    }

    @XmlElementWrapper(name = "AppIdBindings")
    @XmlElements(@XmlElement(name = "Binding"))
    private List<AppIdBinding> appIdBindings;

    @XmlElement(name = "HttpProxyUserAgent")
    private HttpProxyUserAgent httpProxyUserAgent;

    static class AppIdBinding {

        @XmlAttribute
        String appid;

        @XmlElements(@XmlElement(name = "site"))
        List<String> sites;
    }

    static class HttpProxyUserAgent {

        @XmlAttribute
        String choice;

        @XmlElements(@XmlElement(name = "UserAgent"))
        List<UserAgent> agents;
    }

    static class UserAgent {

        @XmlAttribute
        String name;

        @XmlValue
        String value;
    }

    public String getBindingAppId(String host) {
        if (null != appIdBindings) {
            for (AppIdBinding binding : appIdBindings) {
                for (String site : binding.sites) {
                    if (host.contains(site)) {
                        return binding.appid.trim();
                    }
                }
            }
        }
        return null;
    }

    public String getSimulateUserAgent() {
        String defaultUserAgent = Constants.PROJECT_NAME + " V" + Version.value;
        if (null != httpProxyUserAgent) {
            String choice = httpProxyUserAgent.choice;
            List<UserAgent> list = httpProxyUserAgent.agents;
            for (UserAgent ua : list) {
                if (ua.name.equals(choice)) {
                    return ua.value.trim();
                }
            }
        }
        return defaultUserAgent;
    }

    public static Config getInstance() {
        return instance;
    }

    public void saveConfig() throws Exception {
        try {
            init();
            JAXBContext context = JAXBContext.newInstance(Config.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
            URL url = Config.class.getResource("/" + Constants.CLIENT_CONF_NAME);
            String conf = URLDecoder.decode(url.getFile(), "UTF-8");
            FileOutputStream fos = new FileOutputStream(conf);
            marshaller.marshal(this, fos);
            fos.close();
        } catch (Exception e) {
            throw e;
        }
    }
}
