package org.jboss.jetty;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.management.ObjectName;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.jetty.jmx.JBossWebApplicationContextMBean;
import org.jboss.jetty.security.JBossUserRealm;
import org.jboss.logging.Logger;
import org.jboss.metadata.WebMetaData;
import org.jboss.web.WebApplication;
import org.jboss.web.AbstractWebContainer.WebDescriptorParser;
import org.mortbay.j2ee.J2EEWebApplicationContext;
import org.mortbay.j2ee.session.AbstractReplicatedStore;
import org.mortbay.j2ee.session.Manager;
import org.mortbay.j2ee.session.Store;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.WebApplicationContext;
import org.mortbay.jetty.servlet.WebApplicationHandler;
import org.mortbay.jetty.servlet.XMLConfiguration;
import org.mortbay.util.MultiException;
import org.mortbay.util.Resource;
import org.mortbay.xml.XmlParser;

public class JBossWebApplicationContext extends J2EEWebApplicationContext {

    protected static Logger __log = Logger.getLogger(JBossWebApplicationContext.class);

    protected Jetty _jetty;

    protected WebDescriptorParser _descriptorParser;

    protected WebApplication _webApp;

    protected String _subjAttrName = "j_subject";

    protected JBossUserRealm _realm = null;

    public JBossWebApplicationContext(Jetty jetty, WebDescriptorParser descriptorParser, WebApplication webApp, String warUrl) throws IOException {
        super(warUrl);
        _jetty = jetty;
        _descriptorParser = descriptorParser;
        _webApp = webApp;
        _subjAttrName = jetty.getSubjectAttributeName();
    }

    protected void doStart() throws Exception {
        if (_jetty.getSupportJSR77()) setConfigurationClassNames(new String[] { "org.jboss.jetty.JBossWebApplicationContext$Configuration", "org.mortbay.jetty.servlet.JettyWebConfiguration", "org.mortbay.jetty.servlet.jsr77.Configuration" }); else setConfigurationClassNames(new String[] { "org.jboss.jetty.JBossWebApplicationContext$Configuration", "org.mortbay.jetty.servlet.JettyWebConfiguration" });
        MultiException e = null;
        try {
            super.doStart();
        } catch (MultiException me) {
            e = me;
        }
        if (_jetty.getSupportJSR77()) setUpDeploymentInfo();
        if (e != null) throw e;
    }

    public void destroy() {
        super.destroy();
        _jetty = null;
        _descriptorParser = null;
        _webApp = null;
        _subjAttrName = null;
    }

    public void setContextPath(String contextPathSpec) {
        __log = Logger.getLogger(getClass().getName() + "#" + contextPathSpec);
        super.setContextPath(contextPathSpec);
    }

    protected boolean _timeOutPresent = false;

    protected int _timeOutMinutes = 0;

    protected void initClassLoader(boolean forceContextLoader) throws java.net.MalformedURLException, IOException {
        super.initClassLoader(true);
        ClassLoader loader = getClassLoader();
        if (loader instanceof org.mortbay.http.ContextLoader) {
            boolean java2ClassLoadingCompliance = this._webApp.getMetaData().getJava2ClassLoadingCompliance();
            ((org.mortbay.http.ContextLoader) loader).setJava2Compliant(java2ClassLoadingCompliance);
        }
    }

    String _separator = System.getProperty("path.separator");

    public String getFileClassPath() {
        List list = new ArrayList();
        getFileClassPath(getClassLoader(), list);
        String classpath = "";
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            URL url = (URL) i.next();
            if (!url.getProtocol().equals("file")) {
                __log.warn("JSP classpath: non-'file' protocol: " + url);
                continue;
            }
            try {
                Resource res = Resource.newResource(url);
                if (res.getFile() == null) __log.warn("bad classpath entry: " + url); else {
                    String tmp = res.getFile().getCanonicalPath();
                    classpath += (classpath.length() == 0 ? "" : _separator) + tmp;
                }
            } catch (IOException ioe) {
                __log.warn("JSP Classpath is damaged, can't convert path for :" + url, ioe);
            }
        }
        if (__log.isTraceEnabled()) __log.trace("JSP classpath: " + classpath);
        return classpath;
    }

    public void getFileClassPath(ClassLoader cl, List list) {
        if (cl == null) return;
        URL[] urls = null;
        try {
            Class[] sig = {};
            Method getAllURLs = cl.getClass().getMethod("getAllURLs", sig);
            Object[] args = {};
            urls = (URL[]) getAllURLs.invoke(cl, args);
        } catch (Exception ignore) {
        }
        if (urls == null && cl instanceof java.net.URLClassLoader) urls = ((java.net.URLClassLoader) cl).getURLs();
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                if (url != null) {
                    String path = url.getPath();
                    if (path != null) {
                        File f = new File(path);
                        if (f.exists() && f.canRead() && (f.isDirectory() || path.endsWith(".jar") || path.endsWith(".zip")) && (!list.contains(url))) list.add(url);
                    }
                }
            }
        }
        getFileClassPath(cl.getParent(), list);
    }

    protected String findJarByResource(String resource) throws Exception {
        String path = getClass().getClassLoader().getResource(resource).toString();
        return path.substring("jar:file:".length(), path.length() - (resource.length() + 2));
    }

    protected void startHandlers() throws Exception {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (getDistributable() && getDistributableSessionManager() != null) setUpDistributableSessionManager(loader);
        setUpENC(loader);
        if (_realm != null) _realm.init();
        super.startHandlers();
    }

    protected void setUpDistributableSessionManager(ClassLoader loader) {
        try {
            Manager sm = getDistributableSessionManager();
            Store store = sm.getStore();
            if (store instanceof AbstractReplicatedStore) ((AbstractReplicatedStore) store).setLoader(loader);
            if (_timeOutPresent) sm.setMaxInactiveInterval(_timeOutMinutes * 60);
            getServletHandler().setSessionManager(sm);
        } catch (Exception e) {
            __log.error("could not set up Distributable HttpSession Manager - using local one", e);
        }
    }

    protected void setUpENC(ClassLoader loader) throws Exception {
        _webApp.setClassLoader(loader);
        _webApp.setName(getDisplayName());
        _webApp.setAppData(this);
        __log.debug("setting up ENC...");
        _descriptorParser.parseWebAppDescriptors(_webApp.getClassLoader(), _webApp.getMetaData());
        __log.debug("setting up ENC succeeded");
    }

    protected void setUpDeploymentInfo() throws Exception {
        if (_mbean == null) return;
        DeploymentInfo di = _descriptorParser.getDeploymentInfo();
        di.deployedObject = _mbean.getObjectName();
        List mbeanNames = di.mbeans;
        WebApplicationHandler wah = (WebApplicationHandler) getServletHandler();
        List components = new ArrayList();
        ServletHolder servlets[] = wah.getServlets();
        if (servlets != null) for (int i = 0; i < servlets.length; i++) components.add(servlets[i]);
        Object filters[] = wah.getFilters().toArray();
        if (filters != null) for (int i = 0; i < filters.length; i++) components.add(filters[i]);
        components.add(wah.getSessionManager());
        ObjectName[] names = _mbean.getComponentMBeans(components.toArray(), null);
        Set jsr77Names = _mbean.getJsr77ObjectNames();
        Iterator itor = jsr77Names.iterator();
        while (itor.hasNext()) {
            ObjectName on = (ObjectName) itor.next();
            __log.info("Adding jsr77 mbean=" + on.toString());
            mbeanNames.add(on);
        }
    }

    protected JBossWebApplicationContextMBean _mbean;

    public void setMBeanPeer(JBossWebApplicationContextMBean mbean) {
        _mbean = mbean;
    }

    public static class Configuration extends XMLConfiguration {

        public Configuration() {
            super();
        }

        public JBossWebApplicationContext getJBossWebApplicationContext() {
            return (JBossWebApplicationContext) getWebApplicationContext();
        }

        protected void initWebXmlElement(String element, org.mortbay.xml.XmlParser.Node node) throws Exception {
            if ("resource-ref".equals(element) || "resource-env-ref".equals(element) || "env-entry".equals(element) || "ejb-ref".equals(element) || "ejb-local-ref".equals(element) || "security-domain".equals(element)) {
            } else if ("distributable".equals(element)) {
                getJBossWebApplicationContext().setDistributable(true);
            } else if ("login-config".equals(element)) {
                super.initWebXmlElement(element, node);
                String realmName = getJBossWebApplicationContext().getRealmName();
                if (null == realmName) {
                    WebMetaData metaData = getJBossWebApplicationContext()._webApp.getMetaData();
                    realmName = metaData.getSecurityDomain();
                    if (null != realmName) {
                        if (realmName.endsWith("/")) realmName = realmName.substring(0, realmName.length());
                        int idx = realmName.lastIndexOf('/');
                        if (idx >= 0) realmName = realmName.substring(idx + 1);
                    }
                }
                if (__log.isDebugEnabled()) __log.debug("setting Realm: " + realmName);
                getJBossWebApplicationContext()._realm = new JBossUserRealm(realmName, getJBossWebApplicationContext()._subjAttrName);
                getJBossWebApplicationContext().setRealm(getJBossWebApplicationContext()._realm);
            } else super.initWebXmlElement(element, node);
        }

        protected void initSessionConfig(XmlParser.Node node) {
            XmlParser.Node tNode = node.get("session-timeout");
            if (tNode != null) {
                getJBossWebApplicationContext()._timeOutPresent = true;
                getJBossWebApplicationContext()._timeOutMinutes = Integer.parseInt(tNode.toString(false, true));
            }
            super.initSessionConfig(node);
        }
    }
}
