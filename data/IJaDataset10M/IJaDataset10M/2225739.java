package com.goodcodeisbeautiful.archtea.config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.goodcodeisbeautiful.archtea.util.ArchteaUtil;

/**
 * This class keep archtea's property information.
 * 
 * If a static field starts with  PROP_KEY_, the field is one of property keys.
 * So, if you use it, please take care of using it. For example, write information
 * into MessageResources.properties and so on.
 * @author hata
 *
 */
public class ArchteaPropertyFactory {

    /** log */
    private static final Log log = LogFactory.getLog(ArchteaPropertyFactory.class);

    /** a lock object */
    private static final Object LOCK = new Object();

    /** property key mark. */
    private static final String PROPERTY_KEY_PREFIX = "PROP_KEY_";

    /** sample. */
    public static final String PROP_KEY_SAMPLE = "archtea.config.sample";

    /** an interval to check modification. */
    public static final String PROP_KEY_MODIFY_CHECK_INTERVAL = "archtea.config.modifyCheckInterval";

    /**
     * This property is used to set a new config class.
     * This set class have to extend from DefaultExternalSearchConfig class
     * and have to have the constructor which receive two args. The arguments
     * are ArchteaConfig and File.
     * e.g.
     * <pre>
     * public class HogeSearchConfig extends DefaultExternalSearchConfig {
     *   HogeSearchConfig(ArchteaConfig parentConfig, java.io.File file) {
     *     super(parentConfig);
     *   }
     * }
     * </pre>
     */
    public static final String PROP_KEY_EXTERNAL_SEARCH_CONFIG_CLASS = "archtea.config.externalSearchConfigClass";

    /**
     * This property is used to create ArchteaSearcher instance.
     * This value have to a class which implements
     * com.goodcodeisbeautiful.archtea.search.ArchteaSearcher interface.
     */
    public static final String PROP_KEY_EXTERNAL_ARCHTEA_SEARCHER_CLASS = "archtea.config.externalArchteaSearcherClass";

    /**
     * This property is used to set query url for external search.
     * This will be used in a class which implement ArchteaSearcher.
     */
    public static final String PROP_KEY_EXTERNAL_URL = "archtea.config.externalURL";

    /** If zip file's suffix should be shown, this property should set as true or on. */
    public static final String PROP_KEY_HIDE_ARCHIVE_EXTENSION = "archtea.config.hideArchiveExtension";

    /** If there is only a directory in the fist level directory and files in zip and
     * this flag is off or false, the directory is not shown in the entry path.
     */
    public static final String PROP_KEY_HIDE_ARCHIVE_ONE_SUBDIR = "archtea.config.hideArchiveOneSubdir";

    /** a default flag to hide archive's extension or not. */
    public static final boolean DEFAULT_HIDE_ARCHIVE_EXTENSION = false;

    /** a default flag to hide archive's first subdir if the directory is only one dir.  */
    public static final boolean DEFAULT_HIDE_ARCHIVE_ONE_SUBDIR = false;

    /** default value. */
    public static final int DEFAULT_MODIFY_CHECK_INTERVAL = 10000;

    /** a summary text length to show to a client. */
    public static final String PROP_KEY_BASE_SUMMARY_LENGTH = "archtea.search.baseSummaryLength";

    /** default value. */
    public static final int DEFAULT_BASE_SUMMARY_LENGTH = 60;

    /** a local search's summary tag */
    public static final String PROP_KEY_SUMMARY_MARKUP_TAG = "archtea.search.summaryMarkupTag";

    /** a local search's summary tag */
    public static final String DEFAULT_SUMMARY_MARKUP_TAG = "b";

    /** a local search's summary attributes. */
    public static final String PROP_KEY_SUMMARY_MARKUP_ATTRIBUTES = "archtea.search.summaryMarkupAttributes";

    /** a default summary attributes. */
    public static final String DEFAULT_SUMMARY_MARKUP_ATTRIBUTES = "";

    /** a poperty key for summary fragment length. */
    public static final String PROP_KEY_SUMMARY_FRAGMENT_LENGTH = "archtea.search.summaryFragmentLength";

    /** a default summary fragment length. */
    public static final int DEFAULT_SUMMARY_FRAGMENT_LENGTH = 50;

    /** property key to overwrite modify check interval. */
    public static final String PROP_KEY_INDEXER_DOCS_PER_FLUSH = "archtea.search.indexerDocsPerFlush";

    /** the default interval to flush docs. */
    public static final long DEFAULT_INDEXER_DOCS_PER_FLUSH = 10000;

    /** a time interval to notify parsed document to client. */
    public static final String PROP_KEY_CHECK_TIMER_INTERVAL = "archtea.search.indexerCheckTimerInterval";

    /** a default timer interval. */
    public static final long DEFAULT_CHECK_TIMER_INTERVAL = 60000;

    /** property key for a summary parsed length which is rough indication to be parsed. */
    public static final String PROP_KEY_SUMMARY_PARSED_LENGTH = "archtea.search.maxParsedSummaryLength";

    /** To avoid reading many bytes to create summary.
     *  This value is max bytes to be read from contents. */
    public static final int DEFAULT_SUMMARY_PARSED_LENGTH = 81920;

    /** property key of timeout millseconds to request to sites via opensearch */
    public static final String PROP_KEY_OPEN_SEARCH_TIMEOUT = "archtea.search.openSearchTimeout";

    /** a default httpclient timeout for requesting via opensearch. */
    public static final int DEFAULT_OPEN_SEARCH_TIMEOUT = 30000;

    /** property key of a text to connect summary fragments */
    public static final String PROP_KEY_SUMMARY_FRAGMENT_SEPARATOR = "archtea.search.summaryFragmentSeparator";

    /** a default connection text for summary fragments */
    public static final String DEFAULT_SUMMARY_FRAGMENT_SEPARATOR = "...";

    /** a property key of a max fragments per summary */
    public static final String PROP_KEY_MAX_FRAGMENTS_PER_SUMMARY = "archtea.search.maxFragmentsPerSummary";

    /** a default value for a max fragments per summary */
    public static final int DEFAULT_MAX_FRAGMENTS_PER_SUMMARY = 3;

    /** a property key for IndexWriter.setCommitLockTImeout */
    public static final String PROP_KEY_WRITER_COMMIT_LOCK_TIMEOUT = "archtea.indexer.lucene.writer.commitLockTimeout";

    /** a property key for IndexWriter.setMaxBufferedDocs */
    public static final String PROP_KEY_WRITER_MAX_BUFFERED_DOCS = "archtea.indexer.lucene.writer.maxBufferedDocs";

    /** a property key for IndexWriter.setMaxFieldLength */
    public static final String PROP_KEY_WRITER_MAX_FIELD_LENGTH = "archtea.indexer.lucene.writer.maxFieldLength";

    /** a property key for IndexWriter.setMaxMergeDocs */
    public static final String PROP_KEY_WRITER_MAX_MERGE_DOCS = "archtea.indexer.lucene.writer.maxMergeDocs";

    /** a property key for IndexWriter.setMaxMergeFactor */
    public static final String PROP_KEY_WRITER_MERGE_FACTOR = "archtea.indexer.lucene.writer.mergeFactor";

    /** a property key for IndexWriter.setTermIndexInterval */
    public static final String PROP_KEY_WRITER_TERM_INDEX_INTERVAL = "archtea.indexer.lucene.writer.termIndexInterval";

    /** a property key for IndexWriter.setWriteLockTimeout */
    public static final String PROP_KEY_WRITER_WRITE_LOCK_TIMEOUT = "archtea.indexer.lucene.writer.writeLockTimeout";

    public static final long DEFAULT_MAX_WAIT_INDEXER_STOP = 60000;

    public static final String PROP_KEY_MAX_WAIT_INDEXER_STOP = "archtea.indexer.service.maxWaitIndexerStop";

    /** ArchteaIndexer class */
    public static final String PROP_KEY_ARCHTEA_INDEXER_CLASS = "archtea.indexer.class";

    public static final String DEFAULT_ARCHTEA_INDEXER_CLASS = "com.goodcodeisbeautiful.archtea.search.SingleArchteaIndexer";

    public static final String PROP_KEY_INDEXER_SCHEDULE = "archtea.indexer.service.schedule";

    public static final String PROP_KEY_OPTIMIZE_SCHEDULE = "archtea.indexer.service.optimizeSchedule";

    public static final long DEFAULT_OPTIMIZE_THRESHOLD_RATE = 30;

    public static final String PROP_KEY_OPTIMIZE_THRESHOLD_RATE = "archtea.indexer.service.optimizeThresholdRate";

    public static final long DEFAULT_INDEXER_INTERVAL = 600000;

    public static final String PROP_KEY_INDEXER_INTERVAL = "archtea.indexer.service.interval";

    public static final String PROP_KEY_LUCENE_SIMILARITY_CLASS = "archtea.search.luceneSimilarityClass";

    public static final String DEFAULT_LUCENE_SIMILARITY_CLASS = "com.goodcodeisbeautiful.archtea.search.LuceneArchteaSimilarity";

    /** comment text. */
    private static final String PROPERTY_COMMENT = "# ";

    /** If more than this chars, create a new line. */
    private static final int THRESHOLD_CHARS_PER_LINE = 72;

    /** white space */
    private static final String WHITE_SPACE = " ";

    /** key value separator. */
    private static final String KEY_VALUE_SEPARATOR = "=";

    /** category */
    private static final String CATEGORY = "category: ";

    /** default. */
    private static final String DEFAULT_VALUE = "default: ";

    /**
     * Property key table.
     * This field access via getPropertyKeys().
     * List &lt; String &gt;
     */
    private static volatile List<String> m_propertyKeys;

    /** locale */
    private Locale m_locale;

    /**
     * List &lt; ArchteaProperty &gt;
     */
    private volatile List<ArchteaProperty> m_properties;

    /**
     * A default properties.
     */
    private volatile Properties m_defaultProperties;

    /**
     * The constructor.
     * Use Locale.getDefault() as a locale.
     */
    public ArchteaPropertyFactory() {
        this(Locale.getDefault());
    }

    /**
     * The constructor.
     * @param locale is a locale.
     */
    public ArchteaPropertyFactory(Locale locale) {
        super();
        m_locale = locale;
    }

    /**
     * Get a key.
     * @param key is a property key.
     * @return a property.
     */
    public ArchteaProperty getArchteaProperty(String key) {
        if (key == null) return null;
        List propertyList = getArchteaPropertyList();
        Iterator it = propertyList.iterator();
        while (it.hasNext()) {
            ArchteaProperty prop = (ArchteaProperty) it.next();
            if (key.equals(prop.getPropertyKey())) {
                return prop;
            }
        }
        return null;
    }

    /**
     * Get all ArchteaProperty as a list.
     * @return List &lt; ArchteaProperty &gt;
     */
    public List getArchteaPropertyList() {
        if (m_properties == null) {
            synchronized (this) {
                if (m_properties == null) {
                    List propertyKeysList = getPropertyKeys();
                    Iterator it = propertyKeysList.iterator();
                    m_properties = new ArrayList<ArchteaProperty>(propertyKeysList.size());
                    while (it.hasNext()) {
                        m_properties.add(new ArchteaPropertyImpl(ArchteaUtil.ARCHTEA_RESOURCES, m_locale, (String) it.next()));
                    }
                }
            }
        }
        return Collections.unmodifiableList(m_properties);
    }

    /**
     * Get default properties.
     * @return Properties
     */
    public Properties getDefaultProperties() {
        if (m_defaultProperties == null) {
            synchronized (this) {
                if (m_defaultProperties == null) {
                    m_defaultProperties = new Properties();
                    List l = getArchteaPropertyList();
                    Iterator it = l.iterator();
                    while (it.hasNext()) {
                        ArchteaProperty prop = ((ArchteaProperty) it.next());
                        m_defaultProperties.put(prop.getPropertyKey(), prop.getDefault());
                    }
                }
            }
        }
        return m_defaultProperties;
    }

    /**
     * Write property information to output stream.
     * @param out is a destination stream.
     */
    public void writeProperties(OutputStream out) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
            List props = getArchteaPropertyList();
            Iterator it = props.iterator();
            while (it.hasNext()) {
                ArchteaProperty prop = (ArchteaProperty) it.next();
                writer.println(PROPERTY_COMMENT);
                writer.println(PROPERTY_COMMENT + prop.getShortDesc());
                String[] words = prop.getLongDesc().split("\\s");
                StringBuffer buff = new StringBuffer();
                for (int i = 0; i < words.length; i++) {
                    if (THRESHOLD_CHARS_PER_LINE < (buff.length() + words[i].length())) {
                        writer.println(PROPERTY_COMMENT + new String(buff).trim());
                        buff = new StringBuffer(words[i] + WHITE_SPACE);
                    } else {
                        buff.append(words[i] + WHITE_SPACE);
                    }
                }
                writer.println(PROPERTY_COMMENT);
                writer.println(PROPERTY_COMMENT + new String(buff).trim());
                writer.println(PROPERTY_COMMENT);
                writer.println(PROPERTY_COMMENT + CATEGORY + prop.getCategory());
                writer.println(PROPERTY_COMMENT + DEFAULT_VALUE + prop.getDefault());
                writer.println(PROPERTY_COMMENT);
                writer.println(prop.getPropertyKey() + KEY_VALUE_SEPARATOR + prop.getDefault());
                writer.println("");
            }
        } catch (IOException e) {
            log.error(ArchteaUtil.getString("archtea.config.FailedToWriteProperties"), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Get property keys.
     * @return List &lt; String &gt;
     */
    private static List getPropertyKeys() {
        if (m_propertyKeys == null) {
            synchronized (LOCK) {
                if (m_propertyKeys == null) {
                    m_propertyKeys = new LinkedList<String>();
                    Field[] fields = ArchteaPropertyFactory.class.getFields();
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].getName().startsWith(PROPERTY_KEY_PREFIX)) {
                            try {
                                Object o = fields[i].get(null);
                                if (o instanceof java.lang.String) m_propertyKeys.add((String) o);
                            } catch (IllegalAccessException e) {
                                log.error(ArchteaUtil.getString("archtea.config.CannotAccessField", new Object[] { fields[i].getName() }), e);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return m_propertyKeys;
    }
}
