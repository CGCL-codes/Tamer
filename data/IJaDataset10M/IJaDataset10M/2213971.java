package org.xaware.shared.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.springframework.core.io.Resource;
import org.xaware.api.XABizView;
import org.xaware.server.resources.ClassPathResourceLoader;
import org.xaware.server.resources.InitManager;
import org.xaware.server.resources.ResourceHelper;
import org.xaware.shared.util.logging.IExternalLogger;
import org.xaware.shared.util.logging.LogConfigUtil;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Title: XAwareConfig.java
 * <p>
 * Description: Manages the XASystem.xml file
 * <p>
 * Copyright: Copyright (c)
 * <p>
 * Company:
 * <p>
 * 
 * @author
 * @version 1.0
 */
public class XAwareConfig {

    private static final String CLASS_NAME = "XAwareConfig";

    public Element m_BizComponentElement;

    public Element m_BizDriverElement;

    Element m_LoggerElement;

    Element m_LogProcessingElement;

    public Element m_ApplicationElement;

    Element m_XaJmsElement;

    Element m_StartupElement;

    Element m_ShutdownElement;

    Element m_AsyncElement;

    Element m_BldElement;

    Element m_CachePoolElement;

    Element m_ExternalLogger;

    Element m_Features;

    Element m_PublishTypes;

    public Element roleElement;

    Hashtable<String, Element> bizComponents;

    Hashtable<String, Element> bizDocuments;

    Hashtable<String, Element> bizDrivers;

    Hashtable<String, Element> otherDocuments;

    private final int logLevelHashTableSize = 50;

    private int bizComponentFactoryHashTableSize = 50;

    private int bizDriverFactoryHashTableSize = 50;

    private int bizDocumentFactoryHashTableSize = 50;

    private boolean bBizDocCache = true;

    private boolean bBizCompCache = true;

    private boolean bBizDriverCache = true;

    private boolean bBizDocPool = true;

    private boolean bBizCompPool = true;

    private boolean bBizDriverPool = true;

    public String sError;

    public static String sConfigFile = "";

    public static String sDefaultConfigFile = "";

    public static String sHomeDir = System.getProperty("xaware.home");

    public static String sStartTime;

    int iVersion = 1;

    Document xaSystemDoc;

    private static final Namespace xaNamespace = XAwareConstants.xaNamespace;

    private XAwareLogger logger = null;

    private IExternalLogger m_externalloggerclass;

    private Element publishTypesElement;

    private Element searchTypesElement;

    private boolean isDbLogging = false;

    private String adaptive_vxdb = null;

    private boolean designer = false;

    private Hashtable<String, String> logLevelTable;

    private boolean isSnmpEnabled;

    private boolean isJmxEnabled;

    private Element statsElement;

    private static final String ECLIPSE_CLASS = "org.eclipse.ui.IWorkbench";

    public static final String XAWARE_ATTR_APPLICATION = "application";

    public static final String XAWARE_PUBLISH_DIR = sHomeDir + File.separator + "publish";

    public static final String XAWARE_PUBLISH_FILE = "XAPublish.xml";

    /** Start time for the server. */
    private static long serverStartTime;

    /**
     * Inner Holder class for lazily initializing this class using the Initialize-On-Demand Holder Class idiom
     */
    private static class XAwareConfigHolder {

        /** Singleton instance */
        public static XAwareConfig singleton = new XAwareConfig();
    }

    /**
     * Returns the singleton instance of this class.
     * 
     * @return instance of this class.
     */
    public static XAwareConfig GetInstance() {
        return XAwareConfigHolder.singleton;
    }

    /**
     * Package-protected constructor allows for this class to be a singleton.
     */
    XAwareConfig() {
        final Date dt = new Date();
        sStartTime = dt.toString();
        serverStartTime = dt.getTime();
        LogConfigUtil.getInstance().setupGlobalLog();
        logger = XAwareLogger.getXAwareLogger(XAwareConfig.class.getName());
        establishOperatingEnvironment();
        loadXASystem();
        if (isDesigner()) {
            System.out.println("Operating as the Designer contained XAware Server");
        }
        isDbLogging = false;
        if ((m_Features != null) && m_Features.getChild("statistics", xaNamespace) != null) {
            isDbLogging = true;
        }
        if ((m_Features != null) && m_Features.getChild("adaptive_vxdb", xaNamespace) != null) {
            final Element advxdb = m_Features.getChild("adaptive_vxdb", xaNamespace);
            this.adaptive_vxdb = advxdb.getTextTrim();
        }
        isSnmpEnabled = ((m_Features != null) && (m_Features.getChild("snmp", xaNamespace) != null));
        isJmxEnabled = ((m_Features != null) && (m_Features.getChild("jmx", xaNamespace) != null));
    }

    /**
     * Determine if running within the XAware Designer or as a WAR/EAR in an Application Server
     */
    private void establishOperatingEnvironment() {
        try {
            if (XABizView.MODE_DESIGNER.equals(InitManager.getCurrentMode())) {
                designer = true;
            } else {
                designer = false;
            }
        } catch (final Exception e) {
            System.err.println("Unable to detect runtime environment");
            designer = false;
        }
    }

    /**
     * Load the XAsystem.XML file in the xaware.home/conf folder
     * 
     */
    private void loadXASystem() {
        try {
            xaSystemDoc = null;
            if (sConfigFile != null && sConfigFile.trim().length() > 0) {
                try {
                    xaSystemDoc = ResourceHelper.loadXmlResourceIntoDocument(sConfigFile);
                    getLogger().fine("Using XAsystem.xml from " + sConfigFile, CLASS_NAME, "loadXASystem");
                } catch (IOException e) {
                }
            }
            if (xaSystemDoc == null) {
                String resourcePath = XAwareConstants.XA_CONF_URL + "XAsystem.xml";
                Resource resource = null;
                try {
                    resource = ResourceHelper.getResource(resourcePath);
                    sConfigFile = resource.getFile().getAbsolutePath();
                    xaSystemDoc = ResourceHelper.getJdomDocumentFromResource(resource);
                    getLogger().fine("Using XAsystem.xml from " + resourcePath, CLASS_NAME, "loadXASystem");
                } catch (IOException e) {
                    resourcePath = "org/xaware/shared/conf/XAsystem.xml";
                    try {
                        resource = (new ClassPathResourceLoader()).getResource(resourcePath);
                        xaSystemDoc = ResourceHelper.getJdomDocumentFromResource(resource);
                        getLogger().fine("Using XAsystem.xml from " + resourcePath, CLASS_NAME, "loadXASystem");
                    } catch (IOException e2) {
                        String errMsg = "[XAware]Unable to find XAsystem.xml";
                        System.err.println(errMsg);
                        getLogger().severe(errMsg, CLASS_NAME, "loadXASystem");
                        return;
                    }
                } finally {
                    if (resource != null && resource.isOpen()) {
                        try {
                            resource.getInputStream().close();
                        } catch (IOException e2) {
                        }
                    }
                }
            }
            final Element elem = xaSystemDoc.getRootElement();
            if (elem.getAttributeValue("version") != null) {
                iVersion = 2;
            }
            if (iVersion == 2) {
                final Element configElement = elem.getChild("configuration", xaNamespace);
                if (configElement == null) {
                    System.err.println("[XAware]Missing xa:configuration");
                    return;
                }
                m_Features = elem.getChild("features", xaNamespace);
                m_LogProcessingElement = elem.getChild("log_processing", xaNamespace);
                m_ApplicationElement = configElement.getChild(XAWARE_ATTR_APPLICATION, xaNamespace);
                roleElement = configElement.getChild("roles", xaNamespace);
                m_BizComponentElement = configElement.getChild("bizcomponent", xaNamespace);
                m_BizDriverElement = configElement.getChild("bizdriver", xaNamespace);
                m_LoggerElement = configElement.getChild("logger", xaNamespace);
                m_StartupElement = configElement.getChild("startup", xaNamespace);
                m_AsyncElement = configElement.getChild("async", xaNamespace);
                m_BldElement = configElement.getChild("bld_num", xaNamespace);
                m_ShutdownElement = configElement.getChild("shutdown", xaNamespace);
                m_CachePoolElement = configElement.getChild("pool_cache", xaNamespace);
                m_ExternalLogger = configElement.getChild("external_logger");
                m_PublishTypes = elem.getChild("PublishTypes", xaNamespace);
                if (m_CachePoolElement != null) {
                    parseCachePoolElement();
                }
                bizComponents = new Hashtable<String, Element>(bizComponentFactoryHashTableSize);
                bizDrivers = new Hashtable<String, Element>(bizComponentFactoryHashTableSize);
                bizDocuments = new Hashtable<String, Element>(bizComponentFactoryHashTableSize);
                otherDocuments = new Hashtable<String, Element>(bizComponentFactoryHashTableSize);
                logLevelTable = new Hashtable<String, String>(logLevelHashTableSize);
                if (m_ApplicationElement != null) {
                    parseApplicationElement(m_ApplicationElement);
                }
                if (!isDesigner()) {
                    final Element applElem = readXAPublishApplicationElements();
                    parseApplicationElement(applElem);
                }
                statsElement = elem.getChild("StatsProperties", xaNamespace);
            } else {
                final Element configElement = elem.getChild("CONFIGURATION");
                m_ApplicationElement = configElement.getChild("APPLICATION");
                m_BizComponentElement = configElement.getChild("BIZCOMPONENT");
                m_BizDriverElement = configElement.getChild("BIZDRIVER");
                m_LoggerElement = configElement.getChild("LOGGER");
                m_XaJmsElement = configElement.getChild("XAJMS");
                m_StartupElement = configElement.getChild("STARTUP");
                m_AsyncElement = configElement.getChild("ASYNC");
                m_BldElement = configElement.getChild("BLD_NUM");
                m_ShutdownElement = configElement.getChild("SHUTDOWN");
            }
            publishTypesElement = elem.getChild("PublishTypes", xaNamespace);
            searchTypesElement = elem.getChild("SearchTypes", xaNamespace);
        } catch (final JDOMException e) {
            System.err.println("[XAware]Configuration file error" + e.getMessage());
        }
    }

    /**
     * Get start time for the server.
     * 
     * @return Start time for the server.
     */
    public long getServerStartTime() {
        return serverStartTime;
    }

    /**
     * Determine if the XAwareConfig is running in Designer or as an independent server
     * 
     * @return True if the runtime environment is in XAware's Designer
     */
    public boolean isDesigner() {
        return designer;
    }

    public synchronized void saveConfig() {
        try {
            final FileWriter fp = new FileWriter(sConfigFile, false);
            final XMLOutputter outputter = new XMLOutputter();
            outputter.output(xaSystemDoc, fp);
            fp.close();
        } catch (final java.io.IOException e) {
            System.out.println("Unable to save config file");
            getLogger().severe("IO exception:" + e.getMessage(), "XAwareConfig", "saveConfig");
        }
    }

    /**
     * Reload the configuration
     * 
     */
    public synchronized void reload() {
        loadXASystem();
        if (!isDesigner()) {
            System.out.println("Operating as the XAware Server independent of Designer");
            org.xaware.shared.util.logging.LogConfigUtil.getInstance().setupGlobalLog();
        }
    }

    private XAwareLogger getLogger() {
        return logger;
    }

    public Element getApplicationElement() {
        return m_ApplicationElement;
    }

    public Element getStartUpElement() {
        return m_StartupElement;
    }

    public Element getShutdownElement() {
        return m_ShutdownElement;
    }

    public Element getPublishTypes() {
        return m_PublishTypes;
    }

    public static String getHomeDirectory() {
        return sHomeDir;
    }

    public static XAwareConfig SetConfigFile(final String sFileName) {
        sConfigFile = sFileName;
        XAwareConfig xaConfig = new XAwareConfig();
        XAwareConfigHolder.singleton = xaConfig;
        return xaConfig;
    }

    public Element getJmsElement() {
        return m_XaJmsElement;
    }

    public Element getAsyncElement() {
        return m_AsyncElement;
    }

    /**
     * get the text value of the bizdoc element. The bizdoc element is a child of log_processing element
     * 
     * @return null if bizdoc element or log_processing does not exit. Otherwise return value of bizdoc element
     */
    public String getLogProcessingBizDocName() {
        if (m_LogProcessingElement == null) {
            return null;
        }
        final Element bizDocElement = m_LogProcessingElement.getChild("bizdoc", xaNamespace);
        if (bizDocElement == null) {
            return null;
        }
        return bizDocElement.getText();
    }

    public String GetBizComponentClass(final String sBizComponentType) {
        getLogger().entering("XAwareConfig", "GetBizComponentClass", "BizComponent Type: " + sBizComponentType);
        if (sBizComponentType == null) {
            getLogger().finest("BizComponent type is null", "XAwareConfig", "GetBizComponentClass");
            return null;
        }
        if (m_BizComponentElement == null) {
            getLogger().severe("BizComponent element is null", "XAwareConfig", "GetBizComponentClass");
            return null;
        }
        final Element bizElement = m_BizComponentElement.getChild(sBizComponentType);
        if (bizElement == null) {
            getLogger().finest("BizComponent type: " + sBizComponentType + " not found", "XAwareConfig", "GetBizComponentClass");
            return null;
        }
        final String sType = bizElement.getText();
        if (sType != null) {
            getLogger().finest("BizComponent Class name found " + sType, "XAwareConfig", "GetBizComponentClass");
            return sType;
        }
        getLogger().finest("Return null.No BizComponent class found for:" + sBizComponentType, "XAwareConfig", "GetBizComponentClass");
        return null;
    }

    public String GetBizDriverClass(final String sBizDriverType) {
        getLogger().entering("BizDriver Type: " + sBizDriverType, "XAwareConfig", "GetBizDriverClass");
        if (sBizDriverType == null) {
            getLogger().severe("BizDriver type is null", "XAwareConfig", "GetBizDriverClass");
            return null;
        }
        if (m_BizDriverElement == null) {
            getLogger().severe("BizDriver element is null", "XAwareConfig", "GetBizDriverClass");
            return null;
        }
        final Element bizElement = m_BizDriverElement.getChild(sBizDriverType);
        if (bizElement == null) {
            getLogger().finest("BizDriver type: " + sBizDriverType + " not found", "XAwareConfig", "GetBizDriverClass");
            return null;
        }
        final String sType = bizElement.getText();
        if (sType != null) {
            getLogger().finest("BizDiver Class name found" + sType, "XAwareConfig", "GetBizDriverClass");
            return sType;
        }
        getLogger().finest("Return null.No BizDriver class found for: " + sBizDriverType, "XAwareConfig", "GetBizDriverClass");
        return null;
    }

    public String GetBizDocumentForApplication(String sApplication) {
        getLogger().entering("Application: " + sApplication, "XAwareConfig", "GetBizDocumentForApplication");
        if (sApplication == null || m_ApplicationElement == null) {
            getLogger().severe("Application element or name is null", "XAwareConfig", "GetBizDocumentForApplication");
            return sApplication;
        }
        sApplication = normalizeSlashes(sApplication);
        Element matchingChild = null;
        for (final Iterator i = m_ApplicationElement.getChildren().iterator(); i.hasNext(); ) {
            final Element child = (Element) i.next();
            if (normalizeSlashes(child.getName()).equals(sApplication)) {
                matchingChild = child;
                break;
            }
        }
        if (matchingChild == null) {
            getLogger().finest("Application: " + sApplication + " not found in XASystem.xml. Returning " + sApplication, "XAwareConfig", "GetBizDocumentForApplication");
            return sApplication;
        }
        final String sType = matchingChild.getText();
        if (sType != null) {
            return sType;
        }
        getLogger().finest("No matching application found", "XAwareConfig", "GetBizDocumentForApplication");
        return sApplication;
    }

    public String getBuildNumber() {
        if (m_BldElement != null) {
            return m_BldElement.getText();
        }
        return "No build number available";
    }

    public int getBizComponentFactoryHashTableSize() {
        return bizComponentFactoryHashTableSize;
    }

    public boolean isBizComponentCacheOn() {
        return bBizCompCache;
    }

    public boolean isBizComponentPoolOn() {
        return bBizCompPool;
    }

    public int getBizDriverFactoryHashTableSize() {
        return bizDriverFactoryHashTableSize;
    }

    public boolean isBizDriverCacheOn() {
        return bBizDriverCache;
    }

    public boolean isBizDriverPoolOn() {
        return bBizDriverPool;
    }

    public int getBizDocumentFactoryHashTableSize() {
        return bizDocumentFactoryHashTableSize;
    }

    public boolean isBizDocumentCacheOn() {
        return bBizDocCache;
    }

    public boolean isBizDocumentPoolOn() {
        return bBizDocPool;
    }

    /**
     * search xaware.home/publish/all_directories for XAPublish.xml. When found add each component element to applElem
     * modifying the location attribute to include the full path location. Test to be sure there are no conflict names.
     */
    protected Element readXAPublishApplicationElements() {
        final Element packageApplElem = new Element(XAWARE_ATTR_APPLICATION, xaNamespace);
        final Vector files = getXAPublishFiles();
        final Iterator itr = files.iterator();
        while (itr.hasNext()) {
            final String filename = (String) itr.next();
            try {
                getComponentsFromXAPublish(packageApplElem, filename);
            } catch (final Exception e) {
            }
        }
        return packageApplElem;
    }

    /**
     * search xaware.home/publish/all_directories for XAPublish.xml. When found add each filename to a Vector. Return
     * the Vector as an String [].
     */
    protected Vector getXAPublishFiles() {
        final Vector<String> publishFiles = new Vector<String>(50);
        final File packageDir = new File(XAWARE_PUBLISH_DIR);
        final File[] dirs = packageDir.listFiles();
        if (dirs != null) {
            for (int i = 0; i < dirs.length; i++) {
                if (dirs[i].isDirectory()) {
                    try {
                        final String pubName = dirs[i].getAbsolutePath() + File.separator + XAWARE_PUBLISH_FILE;
                        publishFiles.add(pubName);
                    } catch (final Exception e) {
                    }
                }
            }
        }
        return publishFiles;
    }

    /**
     * read XAPublish.xml from the name provided and update m_ApplicationElement passed in with all component elements.
     * 
     * @param xaPubName
     */
    public void getComponentsFromXAPublish(final Element applElem, final String xaPubName) throws Exception {
        final File xaPubFile = new File(xaPubName);
        try {
            if (xaPubFile.exists()) {
                final SAXBuilder builder = new SAXBuilder();
                final Document doc = builder.build(xaPubFile);
                final Element root = doc.getRootElement();
                final Element pubAppl = root.getChild(XAWARE_ATTR_APPLICATION, xaNamespace);
                final List children = pubAppl.getChildren(XAwareConstants.XAWARE_COMPONENT, xaNamespace);
                final Iterator itr = children.iterator();
                while (itr.hasNext()) {
                    final Element component = (Element) itr.next();
                    String relLoc = component.getAttributeValue(XAwareConstants.XAWARE_LOCATION, xaNamespace);
                    if (relLoc == null || "".equals(relLoc)) {
                        relLoc = component.getAttributeValue(XAwareConstants.XAWARE_LOCATION);
                    }
                    String type = component.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE, xaNamespace);
                    if (type == null || "".equals(type)) {
                        type = component.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE);
                    }
                    String logLevel = component.getAttributeValue(XAwareConstants.XAWARE_LOGLEVEL, xaNamespace);
                    if (logLevel == null || "".equals(logLevel)) {
                        logLevel = component.getAttributeValue(XAwareConstants.XAWARE_LOGLEVEL);
                    }
                    final String location = xaPubFile.getParent() + File.separator + relLoc;
                    final Element newComp = (Element) component.clone();
                    newComp.detach();
                    if (relLoc != null) {
                        newComp.setAttribute(XAwareConstants.XAWARE_ATTR_NAME, relLoc, xaNamespace);
                    }
                    if (type != null) {
                        newComp.setAttribute(XAwareConstants.XAWARE_ATTR_TYPE, type, xaNamespace);
                    }
                    if (logLevel != null) {
                        newComp.setAttribute(XAwareConstants.XAWARE_LOGLEVEL, logLevel, xaNamespace);
                    }
                    if (location != null) {
                        newComp.setAttribute(XAwareConstants.XAWARE_LOCATION, location, xaNamespace);
                        newComp.setAttribute(XAwareConstants.XAWARE_LOCATION, location);
                    }
                    applElem.addContent(newComp);
                }
            }
        } catch (final Exception e) {
            System.err.println("Exception reading " + xaPubName + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void parseApplicationElement(final Element applElem) {
        final Iterator iter = applElem.getChildren().iterator();
        while (iter.hasNext()) {
            final Element childElem = (Element) iter.next();
            final String childType = childElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE, xaNamespace);
            final String childName = childElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_NAME, xaNamespace);
            final String childLoc = childElem.getAttributeValue(XAwareConstants.XAWARE_LOCATION, xaNamespace);
            final String childLogLevel = childElem.getAttributeValue(XAwareConstants.XAWARE_LOGLEVEL, xaNamespace);
            if ((childType == null) || (childName == null) || (childLoc == null)) {
                continue;
            }
            if (childType.equals("xa:bizcomponent")) {
                bizComponents.put(normalizeSlashes(childName), childElem);
            } else if (childType.equals("xa:bizview") || childType.equals("xa:bizdoc")) {
                bizDocuments.put(normalizeSlashes(childName), childElem);
            } else if (childType.equals("xa:bizdriver")) {
                bizDrivers.put(normalizeSlashes(childName), childElem);
            } else {
                otherDocuments.put(childName, childElem);
            }
            if (childLogLevel != null && childLogLevel.length() > 0) {
                logLevelTable.put(childName, childLogLevel);
            }
        }
    }

    public synchronized void deleteFromApplicationElement(final Element removeElem) {
        final Iterator iter = m_ApplicationElement.getChildren().iterator();
        final String removeName = removeElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_NAME, xaNamespace);
        final String removeType = removeElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE, xaNamespace);
        while (iter.hasNext()) {
            final Element childElem = (Element) iter.next();
            final String childType = childElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE, xaNamespace);
            final String childName = childElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_NAME, xaNamespace);
            if (childType.equals(removeType) && childName.equals(removeName)) {
                iter.remove();
                childElem.detach();
            }
        }
    }

    public synchronized boolean modifyApplicationElement(final Element modifyElem, final boolean bOverwrite) {
        final Iterator iter = m_ApplicationElement.getChildren().iterator();
        final String modifyName = modifyElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_NAME, xaNamespace);
        final String modifyType = modifyElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE, xaNamespace);
        while (iter.hasNext()) {
            final Element childElem = (Element) iter.next();
            final String childType = childElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE, xaNamespace);
            final String childName = childElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_NAME, xaNamespace);
            if (childType.equals(modifyType) && childName.equals(modifyName)) {
                if (bOverwrite == false) {
                    return false;
                }
                iter.remove();
                childElem.detach();
            }
        }
        m_ApplicationElement.addContent((Element) modifyElem.clone());
        return true;
    }

    public synchronized boolean containsApplicationElementAlias(final String alias) {
        final Iterator iter = m_ApplicationElement.getChildren().iterator();
        while (iter.hasNext()) {
            final Element childElem = (Element) iter.next();
            final String childName = childElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_NAME, xaNamespace);
            if (childName.equals(alias)) {
                return true;
            }
        }
        return false;
    }

    private void parseCachePoolElement() {
        final Iterator iter = m_CachePoolElement.getChildren().iterator();
        while (iter.hasNext()) {
            final Element childElem = (Element) iter.next();
            boolean bCache = true;
            boolean bPool = true;
            final String childType = childElem.getAttributeValue(XAwareConstants.XAWARE_ATTR_TYPE, xaNamespace);
            final String childCache = childElem.getAttributeValue("metacache", xaNamespace);
            final String childPool = childElem.getAttributeValue("pool", xaNamespace);
            final String childTableSize = childElem.getAttributeValue("hashtablesize", xaNamespace);
            if (childCache != null && childCache.equals(XAwareConstants.XAWARE_NO)) {
                bCache = false;
            }
            if (childPool != null && childPool.equals(XAwareConstants.XAWARE_NO)) {
                bPool = false;
            }
            if (childType.equals("xa:bizcomponent")) {
                bizComponentFactoryHashTableSize = Integer.parseInt(childTableSize);
                if (bizComponentFactoryHashTableSize <= 0) {
                    bizComponentFactoryHashTableSize = 50;
                }
                bBizCompCache = bCache;
                bBizCompPool = bPool;
            } else if (childType.equals("xa:bizdocument")) {
                bizDocumentFactoryHashTableSize = Integer.parseInt(childTableSize);
                if (bizDocumentFactoryHashTableSize <= 0) {
                    bizDocumentFactoryHashTableSize = 50;
                }
                bBizDocCache = bCache;
                bBizDocPool = bPool;
            } else if (childType.equals("xa:bizdriver")) {
                bizDriverFactoryHashTableSize = Integer.parseInt(childTableSize);
                if (bizDriverFactoryHashTableSize <= 0) {
                    bizDriverFactoryHashTableSize = 50;
                }
                bBizDriverCache = bCache;
                bBizDriverPool = bPool;
            }
        }
    }

    public Element getBizComponentElement(final String bizComponentName) {
        if (bizComponents == null) {
            return null;
        }
        return (Element) bizComponents.get(normalizeSlashes(bizComponentName));
    }

    public Element getBizDocumentElement(final String bizDocName) {
        if ((bizDocuments == null) || (bizDocName == null)) {
            return null;
        }
        return (Element) bizDocuments.get(normalizeSlashes(bizDocName));
    }

    public String getBizViewLocation(final String bizDocName) {
        Element bizElem = getBizDocumentElement(bizDocName);
        if (bizElem == null) {
            bizElem = getBizComponentElement(bizDocName);
        }
        if (bizElem == null) {
            bizElem = getBizDriverElement(bizDocName);
        }
        if (bizElem == null) {
            return bizDocName;
        }
        final String sBizDocLocation = bizElem.getAttributeValue("location", XAwareConstants.xaNamespace);
        if (sBizDocLocation == null) {
            return bizDocName;
        }
        return sBizDocLocation;
    }

    public Element getBizDriverElement(final String bizDriverName) {
        if ((bizDrivers == null) || (bizDriverName == null)) {
            return null;
        }
        return (Element) bizDrivers.get(normalizeSlashes(bizDriverName));
    }

    private String normalizeSlashes(final String path) {
        return path.replace('\\', '/');
    }

    public Element getOtherDocumentElement(final String bizDriverName) {
        if (otherDocuments == null) {
            return null;
        }
        return (Element) otherDocuments.get(bizDriverName);
    }

    public Collection getBizComponentElement() {
        if (bizComponents == null) {
            return null;
        }
        return bizComponents.values();
    }

    public Collection getBizDocumentElement() {
        if (iVersion == 1) {
            return m_ApplicationElement.getChildren();
        }
        if (bizDocuments == null) {
            return null;
        }
        return bizDocuments.values();
    }

    public Collection getBizDriverElement() {
        if (bizDrivers == null) {
            return null;
        }
        return bizDrivers.values();
    }

    public void getBizDocumentElements(final Element output, final String sFilter) {
        if (bizDocuments == null) {
            return;
        }
        final Iterator iter = bizDocuments.values().iterator();
        while (iter.hasNext()) {
            final Element childElem = (Element) iter.next();
            final Element e = (Element) childElem.clone();
            output.addContent(e);
        }
    }

    public void getBizComponentElements(final Element output, final String sFilter) {
        if (bizComponents == null) {
            return;
        }
        final Iterator iter = bizComponents.values().iterator();
        while (iter.hasNext()) {
            final Element childElem = (Element) iter.next();
            final Element e = (Element) childElem.clone();
            output.addContent(e);
        }
    }

    public void getBizDriverElements(final Element output, final String sFilter) {
        if (bizDrivers == null) {
            return;
        }
        final Iterator iter = bizDrivers.values().iterator();
        while (iter.hasNext()) {
            final Element childElem = (Element) iter.next();
            final Element e = (Element) childElem.clone();
            output.addContent(e);
        }
    }

    public boolean isExternalLogEnabled() {
        if (m_ExternalLogger == null) {
            return false;
        } else {
            String enable = "";
            try {
                enable = m_ExternalLogger.getAttribute("enable").getValue();
            } catch (final NullPointerException npe) {
                System.out.println(" The XASystem.xml ENABLE attribute is mandatory" + " for the external_logger (case sensitive)");
            }
            if (enable != null && enable.length() > 0 && enable.compareToIgnoreCase("true") == 0) {
                return (true);
            } else {
                return (false);
            }
        }
    }

    /**
     * Returns an instance of the external logger
     * 
     * @return - Instance of IExternalLogger
     */
    public IExternalLogger getExternalLogger() {
        if (m_externalloggerclass != null) {
            return (m_externalloggerclass);
        }
        final Element executeusing = m_ExternalLogger.getChild("execute_using");
        if (executeusing != null) {
            final Element loggerclass = executeusing.getChild("class");
            if (loggerclass != null) {
                final Attribute fqn = loggerclass.getAttribute("fqn");
                if (fqn != null) {
                    final String classname = fqn.getValue();
                    if (classname != null && classname.length() > 0) {
                        try {
                            ClassLoader cl = ThreadLocalClassLoader.get();
                            if (cl == null) {
                                cl = Thread.currentThread().getContextClassLoader();
                            }
                            final Class externalloggerclass = Class.forName(classname, true, cl);
                            final Attribute singleton = loggerclass.getAttribute("singleton");
                            if (singleton != null) {
                                final String issingleton = singleton.getValue();
                                if (issingleton != null && issingleton.length() > 0) {
                                    if (issingleton.compareToIgnoreCase("yes") == 0) {
                                        final Method[] methodarray = externalloggerclass.getMethods();
                                        if (methodarray != null && methodarray.length > 0) {
                                            for (int i = 0; i < methodarray.length; i++) {
                                                final Method method = methodarray[i];
                                                final String methodname = method.getName();
                                                if (methodname != null && methodname.length() > 0 && methodname.compareToIgnoreCase("getInstance") == 0) {
                                                    try {
                                                        final Object obj = method.invoke(null, (java.lang.Object[]) null);
                                                        if (obj != null) {
                                                            m_externalloggerclass = (IExternalLogger) obj;
                                                            return (m_externalloggerclass);
                                                        } else {
                                                            System.out.println("Null Logger facade instance" + " returned from " + classname + ".getInstance. Please check the " + "implementation");
                                                        }
                                                    } catch (final IllegalAccessException iae) {
                                                        System.out.println("Illegal Access Exception " + " trying to get an instance of " + classname);
                                                        System.out.println("Error Message " + iae.getMessage());
                                                    } catch (final InvocationTargetException ite) {
                                                        System.out.println("Invocation Target Exception " + " trying to get an instance of " + classname);
                                                        System.out.println("Error Message " + ite.getTargetException().getMessage());
                                                    }
                                                }
                                            }
                                        } else {
                                            System.out.println("Please make sure class " + classname + " has getInstance method or do not specify " + " singleton access to the class");
                                        }
                                    }
                                }
                            }
                            try {
                                final Object obj = externalloggerclass.newInstance();
                                if (obj != null) {
                                    m_externalloggerclass = (IExternalLogger) obj;
                                    return (m_externalloggerclass);
                                }
                            } catch (final IllegalAccessException iae) {
                                System.out.println("Illegal Access Exception " + " trying to get an instance of " + classname);
                                System.out.println("Error Message " + iae.getMessage());
                            } catch (final InstantiationException ie) {
                                System.out.println("Instantiation Exception " + " trying to get an instance of " + classname);
                                System.out.println("Error Message " + ie.getMessage());
                            }
                        } catch (final ClassNotFoundException cnfe) {
                            System.out.println("Please make sure class " + classname + " is" + " in classpath");
                        }
                    } else {
                        System.out.println("Please check XASystem.xml. External Logger " + "configuration is incomplete. Missing class name" + " Please check the value of fqn attribute" + " for class subelement of execute_using element");
                    }
                } else {
                    System.out.println("Please check XASystem.xml. External Logger " + "configuration is incomplete. Missing attribute fqn" + " for class subelement of execute_using element");
                }
            } else {
                System.out.println("Please check XASystem.xml. External Logger " + "configuration is incomplete. Missing class element" + " for execute_using");
            }
        } else {
            System.out.println("Please check XASystem.xml. External Logger " + "configuration is incomplete. Missing execute_using" + " child element");
        }
        return (null);
    }

    public boolean isSnmpEnabled() {
        return isSnmpEnabled;
    }

    public boolean isJmxEnabled() {
        return isJmxEnabled;
    }

    public boolean isDbLogging() {
        return isDbLogging;
    }

    public String getAdaptiveVXDB() {
        return this.adaptive_vxdb;
    }

    public Element getStatsElement() {
        return statsElement;
    }

    public Element getPublishTypesElement() {
        return publishTypesElement;
    }

    public Element getSearchTypesElement() {
        return searchTypesElement;
    }

    public static void main(final String[] args) {
        @SuppressWarnings("unused") final XAwareConfig XAwareConfig1 = new XAwareConfig();
    }
}
