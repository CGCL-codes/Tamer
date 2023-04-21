package org.tm4j.admintool.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapProviderFactory;
import org.tm4j.topicmap.source.SerializedTopicMapSource;
import org.xml.sax.SAXException;
import uk.co.jezuk.mango.Pair;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Simple implementation of the TM4JConfiguration interface. Can be used as the base class
 * for application configuration models.
 */
public class TM4JConfigurationImpl implements TM4JConfiguration {

    private File m_src;

    private Map m_providersByName = new HashMap();

    private Map m_providerPropsByName = new HashMap();

    private Map m_tmMapsByProvider = new HashMap();

    private boolean m_dirty;

    private Log m_log = LogFactory.getLog(TM4JConfiguration.class);

    public void setLog(Log log) {
        m_log = log;
    }

    public Log getLog() {
        return m_log;
    }

    public boolean isDirty() {
        return m_dirty;
    }

    public String[] getProviderNames() {
        if (m_providersByName.isEmpty()) {
            return null;
        } else {
            return (String[]) m_providersByName.keySet().toArray(new String[1]);
        }
    }

    public TopicMapProvider getProviderByName(String name) {
        Object ret = m_providersByName.get(name);
        if ((ret != null) && (ret instanceof TopicMapProvider)) {
            return (TopicMapProvider) ret;
        } else {
            return null;
        }
    }

    public Throwable getProviderErrorByName(String name) {
        Object ret = m_providersByName.get(name);
        if ((ret != null) && ret instanceof Throwable) {
            return (Throwable) ret;
        }
        return null;
    }

    public String[] getTopicMapNames(String providerName) {
        Map tmMap = (Map) m_tmMapsByProvider.get(providerName);
        if ((tmMap != null) && (!tmMap.isEmpty())) {
            return (String[]) tmMap.keySet().toArray(new String[1]);
        } else {
            return null;
        }
    }

    public TopicMap getTopicMap(String providerName, String topicMapName) {
        Map tmMap = (Map) m_tmMapsByProvider.get(providerName);
        if (tmMap != null) {
            Pair mapAndSrc = (Pair) tmMap.get(topicMapName);
            if (mapAndSrc.first instanceof TopicMap) {
                return (TopicMap) mapAndSrc.first;
            }
        }
        return null;
    }

    public Throwable getTopicMapError(String providerName, String topicMapName) {
        Map tmMap = (Map) m_tmMapsByProvider.get(providerName);
        if (tmMap != null) {
            Pair errAndSrc = (Pair) tmMap.get(topicMapName);
            if ((errAndSrc != null) && (errAndSrc.first != null) && (errAndSrc.first instanceof Throwable)) {
                return (Throwable) errAndSrc.first;
            }
        }
        return null;
    }

    public String getTopicMapSource(String providerName, String topicMapName) {
        Map tmMap = (Map) m_tmMapsByProvider.get(providerName);
        if (tmMap != null) {
            Pair mapAndSrc = (Pair) tmMap.get(topicMapName);
            return (String) mapAndSrc.second;
        } else {
            return null;
        }
    }

    public void addProviderConfiguration(String providerName, String factoryClassName, Properties configProperties) throws TM4JConfigurationException {
        try {
            Class providerFactoryClass = Class.forName(factoryClassName);
            TopicMapProviderFactory providerFactory = (TopicMapProviderFactory) providerFactoryClass.newInstance();
            TopicMapProvider provider = providerFactory.newTopicMapProvider(configProperties);
            m_providersByName.put(providerName, provider);
            m_providerPropsByName.put(providerName, configProperties);
        } catch (Exception ex) {
            throw new TM4JConfigurationException("Error initialising provider factory. ", ex);
        }
    }

    public void addProviderConfiguration(String providerName, String factoryClassName, Properties props, Exception error) {
        m_providersByName.put(providerName, error);
        m_providerPropsByName.put(providerName, props);
    }

    public void addTopicMap(String providerName, String topicMapName, String tmSrc) throws TM4JConfigurationException {
        TopicMapProvider provider = getProviderByName(providerName);
        TopicMap topicMap = null;
        if (provider == null) {
            throw new TM4JConfigurationException("The provider named " + providerName + " was not correctly initialised.");
        }
        try {
            if (tmSrc != null) {
                InputStream is = null;
                if (provider instanceof org.tm4j.topicmap.memory.TopicMapProviderImpl) {
                    try {
                        URL u = new URL(tmSrc);
                        is = u.openStream();
                    } catch (Exception ex) {
                        try {
                            File f = new File(tmSrc);
                            if (f.exists()) {
                                is = new FileInputStream(f);
                                tmSrc = f.toURL().toString();
                            }
                        } catch (Exception e) {
                            if (m_log.isErrorEnabled()) m_log.error("Unable to read topic map source '" + tmSrc + "' as a URL or a local file name.", e);
                            throw new SAXException("Unable to read topic map source '" + tmSrc + "' as a URL or as a local file name.", e);
                        }
                    }
                    if (is == null) {
                        m_log.error("Unable to read topic map from address " + tmSrc);
                    } else {
                        try {
                            Locator baseLoc = provider.getLocatorFactory().createLocator("URI", tmSrc);
                            SerializedTopicMapSource src = new SerializedTopicMapSource(is, baseLoc);
                            topicMap = provider.addTopicMap(src);
                        } catch (Exception ex) {
                            throw new SAXException("Unable to convert topic map source address into a base locator '" + tmSrc + "' from provider " + providerName, ex);
                        }
                    }
                } else {
                    try {
                        Locator baseLoc = provider.getLocatorFactory().createLocator("URI", tmSrc);
                        topicMap = provider.getTopicMap(baseLoc);
                    } catch (Exception ex) {
                        throw new SAXException("Unable to find topic map with base locator '" + tmSrc + "' in provider " + providerName, ex);
                    }
                }
            }
        } catch (Exception ex) {
            throw new TM4JConfigurationException("Unable to load topic map with base locator '" + tmSrc + "' in provider " + providerName, ex);
        }
        if (topicMap != null) {
            Map tms = (Map) m_tmMapsByProvider.get(providerName);
            if (tms == null) {
                tms = new HashMap();
                m_tmMapsByProvider.put(providerName, tms);
            }
            Pair mapAndSrc = new Pair(topicMap, tmSrc);
            tms.put(topicMapName, mapAndSrc);
        } else {
            throw new TM4JConfigurationException("Failed to load topic map with base locator '" + tmSrc + "' in provider " + providerName);
        }
    }

    public void addTopicMapConfiguration(String providerName, String tmName, TopicMap tm, String tmSrc) {
        HashMap tms = (HashMap) m_tmMapsByProvider.get(providerName);
        if (tms == null) {
            tms = new HashMap();
            m_tmMapsByProvider.put(providerName, tms);
        }
        Pair mapAndSrc = new Pair(tm, tmSrc);
        tms.put(tmName, mapAndSrc);
        m_dirty = true;
    }

    public void addTopicMapConfiguration(String providerName, String tmName, String tmSrc, Exception error) {
        HashMap tms = (HashMap) m_tmMapsByProvider.get(providerName);
        if (tms == null) {
            tms = new HashMap();
            m_tmMapsByProvider.put(providerName, tms);
        }
        Pair errAndSrc = new Pair(error, tmSrc);
        tms.put(tmName, errAndSrc);
        m_dirty = true;
    }

    public File getSrc() {
        return m_src;
    }

    public void setDirty(boolean dirty) {
        m_dirty = dirty;
    }

    public void setSrc(File src) {
        m_src = src;
    }

    public Properties getProviderProperties(String providerName) {
        return (Properties) m_providerPropsByName.get(providerName);
    }

    public void renameProvider(String oldName, String newName) {
        m_providersByName.put(newName, m_providersByName.get(oldName));
        m_providersByName.remove(oldName);
        m_providerPropsByName.put(newName, m_providerPropsByName.get(oldName));
        m_providerPropsByName.remove(oldName);
        m_tmMapsByProvider.put(newName, m_tmMapsByProvider.get(oldName));
        m_tmMapsByProvider.remove(oldName);
    }

    public void removeProvider(String providerName) {
        m_tmMapsByProvider.remove(providerName);
        m_providerPropsByName.remove(providerName);
        m_providersByName.remove(providerName);
    }

    public void removeTopicMap(String providerName, String topicMapName) {
        HashMap tms = (HashMap) m_tmMapsByProvider.get(providerName);
        tms.remove(topicMapName);
        setDirty(true);
    }

    public void renameTopicMap(String providerName, String oldName, String newName) {
        HashMap tms = (HashMap) m_tmMapsByProvider.get(providerName);
        Object o = tms.remove(oldName);
        if (o != null) {
            tms.put(newName, o);
        }
        setDirty(true);
    }

    public String getTopicMapName(String providerName, Locator tmLoc) {
        HashMap tms = (HashMap) m_tmMapsByProvider.get(providerName);
        if (tms != null) {
            Iterator it = tms.keySet().iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                Pair tmAndSrc = (Pair) tms.get(name);
                if ((tmAndSrc.first != null) && (tmAndSrc.first instanceof TopicMap)) {
                    TopicMap tm = (TopicMap) tmAndSrc.first;
                    if ((tm.getBaseLocator() != null) && (tm.getBaseLocator().equals(tmLoc))) {
                        return name;
                    }
                }
            }
        }
        return null;
    }
}
