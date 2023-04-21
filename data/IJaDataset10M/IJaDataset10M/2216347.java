package org.tm4j.topicmap.ozone;

import org.apache.xerces.parsers.SAXParser;
import org.ozoneDB.ExternalDatabase;
import org.ozoneDB.OzoneInterface;
import org.ozoneDB.xml.util.SAXChunkProducer;
import org.ozoneDB.xml.util.SAXChunkProducerDelegate;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProcessingException;
import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapProviderException;
import org.tm4j.topicmap.TopicMapProviderFactory;
import org.tm4j.topicmap.utils.TopicMapBuilder;
import org.xml.sax.InputSource;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author <a href="mailto:kal@techquila.com">Kal Ahmed</a>
 * @author <a href="mailto:gerd@smb-tec.com">Gerd Mueller</a>
 */
public class OzoneXTMBuilder implements SAXChunkProducerDelegate, TopicMapBuilder {

    private OzoneInterface m_database = null;

    private OzoneXTMBuilderHelper m_helper = null;

    private Hashtable m_properties = null;

    private OzoneTopicMapProviderImpl m_provider = null;

    /** */
    public OzoneXTMBuilder(OzoneTopicMapProviderImpl provider) {
        m_database = provider.getDatabase();
        m_properties = new Hashtable();
        m_provider = provider;
    }

    public void setProperty(String prop, Object value) {
        m_properties.put(prop, value);
    }

    /** */
    public TopicMap buildTopicMap(InputSource input) throws Exception {
        return buildTopicMap(input, null);
    }

    /** */
    public TopicMap buildTopicMap(InputSource input, TopicMap existingTopicMap) throws Exception {
        return buildTopicMap(input, existingTopicMap, null, null);
    }

    public TopicMap buildTopicMap(InputSource input, TopicMap existingTopicMap, Locator resourceLocator, Topic[] addedThemes) throws Exception {
        m_helper = (OzoneXTMBuilderHelper) m_database.createObject(OzoneXTMBuilderHelperImpl.class.getName());
        Iterator it = m_properties.keySet().iterator();
        while (it.hasNext()) {
            String prop = (String) it.next();
            m_helper.setProperty(prop, m_properties.get(prop));
        }
        if (existingTopicMap != null) {
            m_helper.setTopicMap(existingTopicMap);
        }
        if (resourceLocator != null) {
            m_helper.setResourceLocator(resourceLocator);
        }
        if (addedThemes != null) {
            m_helper.setAddedThemes(addedThemes);
        }
        SAXChunkProducer producer = new SAXChunkProducer(this);
        SAXParser parser = new SAXParser();
        parser.setContentHandler(producer);
        parser.parse(input);
        TopicMap result = m_helper.getTopicMap();
        m_database.deleteObject(m_helper);
        return result;
    }

    /** */
    public void processChunk(SAXChunkProducer producer) throws Exception {
        m_helper.putChunk(producer.chunkStream().toByteArray());
    }

    /** */
    public static void main(String[] args) throws Exception {
        String dbURL = null;
        String file = null;
        String name = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-dbURL=")) {
                dbURL = args[i].substring("-dbURL=".length());
            } else if (args[i].startsWith("-topicmap=")) {
                file = args[i].substring("-topicmap=".length());
            } else if (args[i].startsWith("-tmname=")) {
                name = args[i].substring("-tmname=".length());
            } else {
                printHelpAndExit();
            }
        }
        if ((dbURL == null) || (file == null) || (name == null)) {
            printHelpAndExit();
        }
        if (!new File(file).exists()) {
            System.out.println("File '" + file + "' doesn't exist !");
            System.exit(0);
        }
        TopicMapProviderFactory pf = new OzoneTopicMapProviderFactoryImpl();
        System.getProperties().put("dburl", dbURL);
        TopicMapProvider provider = pf.newTopicMapProvider(System.getProperties());
        ExternalDatabase database = ((OzoneTopicMapProviderImpl) provider).getDatabase();
        OzoneXTMBuilder builder = new OzoneXTMBuilder((OzoneTopicMapProviderImpl) provider);
        TopicMap topicMap = (TopicMap) database.objectForName(name);
        boolean newTMap = (topicMap == null);
        System.out.println("   --> Storing topicmap " + file + " as: '" + name + "'");
        topicMap = builder.buildTopicMap(new InputSource(file), topicMap);
        if (newTMap) {
            database.nameObject((OzoneTopicMap) topicMap, name);
        }
        System.out.println("   --> found " + topicMap.getTopicCount() + " topics");
        database.close();
    }

    /** */
    private static void printHelpAndExit() {
        System.out.println("usage: java OzoneXTMBuilder -dbURL=<database URL> -topicmap=<XTM file> -tmname=<topicmap name>");
        System.exit(0);
    }

    /**
     * @deprecated
     */
    public void build(InputStream src, Locator srcLoc, TopicMap tm, TopicMapProvider provider, Topic[] addedThemes) throws IOException, LocatorFactoryException, TopicMapProcessingException, PropertyVetoException, TopicMapProviderException {
        if (provider instanceof OzoneTopicMapProviderImpl) {
            OzoneTopicMapProviderImpl ozoneProvider = (OzoneTopicMapProviderImpl) provider;
            this.m_database = ozoneProvider.getDatabase();
            try {
                buildTopicMap(new InputSource(src), tm, srcLoc, addedThemes);
            } catch (Exception ex) {
                throw new TopicMapProviderException("Could not build topic map.", ex);
            }
        } else {
            throw new TopicMapProviderException("The provider for the input TopicMap is not supported by this TopicMapBuilder.");
        }
    }

    /**
     * @deprecated
     */
    public void build(Reader src, Locator srcLoc, TopicMap tm, TopicMapProvider provider, Topic[] addedThemes) throws IOException, LocatorFactoryException, TopicMapProcessingException, PropertyVetoException, TopicMapProviderException {
        if (provider instanceof OzoneTopicMapProviderImpl) {
            OzoneTopicMapProviderImpl ozoneProvider = (OzoneTopicMapProviderImpl) provider;
            this.m_database = ozoneProvider.getDatabase();
            try {
                buildTopicMap(new InputSource(src), tm, srcLoc, addedThemes);
            } catch (Exception ex) {
                throw new TopicMapProviderException("Could not build topic map.", ex);
            }
        } else {
            throw new TopicMapProviderException("The provider for the input TopicMap is not supported by this TopicMapBuilder.");
        }
    }

    public boolean isSupportedProperty(String propertyName) {
        return false;
    }

    public void build(InputStream src, Locator srcLoc, TopicMap tm) throws IOException, LocatorFactoryException, TopicMapProcessingException, PropertyVetoException, TopicMapProviderException {
        build(src, srcLoc, tm, m_provider);
    }

    public void build(Reader src, Locator srcLoc, TopicMap tm) throws IOException, LocatorFactoryException, TopicMapProcessingException, PropertyVetoException, TopicMapProviderException {
        build(src, srcLoc, tm, m_provider, null);
    }

    public void build(InputStream src, Locator srcLoc, TopicMap tm, TopicMapProvider provider) throws IOException, LocatorFactoryException, TopicMapProcessingException, PropertyVetoException, TopicMapProviderException {
        build(src, srcLoc, tm, provider, null);
    }

    public void build(Reader src, Locator srcLoc, TopicMap tm, TopicMapProvider provider) throws IOException, LocatorFactoryException, TopicMapProcessingException, PropertyVetoException, TopicMapProviderException {
        build(src, srcLoc, tm, provider, null);
    }

    public void build(InputStream src, Locator srcLoc, TopicMap tm, Topic[] addedThemes) throws IOException, LocatorFactoryException, TopicMapProcessingException, PropertyVetoException, TopicMapProviderException {
        build(src, srcLoc, tm, tm.getProvider(), null);
    }

    public void build(Reader src, Locator srcLoc, TopicMap tm, Topic[] addedThemes) throws IOException, LocatorFactoryException, TopicMapProcessingException, PropertyVetoException, TopicMapProviderException {
        build(src, srcLoc, tm, tm.getProvider(), null);
    }
}
