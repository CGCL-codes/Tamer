package net.sf.katta.util;

import java.util.Set;
import net.sf.katta.index.indexer.Indexer;
import net.sf.katta.index.indexer.ShardSelectionMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.lucene.analysis.Analyzer;

public class IndexConfiguration extends KattaConfiguration {

    public IndexConfiguration() {
        super("/katta.index.properties");
    }

    /**
   * Katta Related *
   */
    public static final String INDEX_ZIP_CLASS = "index.zip.class";

    public static final String DOCUMENT_FACTORY_CLASS = "document.factory.class";

    public static final String INDEX_PUBLISHER_CLASS = "index.publisher.class";

    public static final String INDEX_SHARD_KEY_GENERATOR_CLASS = "indexer.shardKeyGenerator.class";

    public static final String INDEX_SHARD_COUNT = "index.shardCount";

    /**
   * Lucene Related *
   */
    public static final String FLUSH_THRESHOLD = "index.flush.threshold";

    public static final String INDEXER_MERGE_FACTOR = "indexer.mergeFactor";

    public static final String INDEXER_MAX_MERGE = "indexer.maxMergeDocs";

    public static final String INDEXER_TERM_INTERVALL = "indexer.term.intervall";

    public static final String INDEXER_MAX_FIELD_LENGTH = "indexer.max.fieldLength";

    public static final String INDEXER_MAX_BUFFERED_DOCS = "indexer.max.bufferedDocs";

    public static final String INDEXER_ANALYZER = "indexer.analyzer";

    /**
   * Path Related *
   */
    public static final String INDEX_TMP_DIRECTORY = "index.tmp.directory";

    public static final String INDEX_UPLOAD_PATH = "index.upload.path";

    public static final String INDEX_ARCHIVE_PATH = "index.archive.path";

    /**
   * Mapred IO *
   */
    public static final String INPUT_FORMAT_CLASS = "input.format.class";

    public static final String INPUT_KEY_CLASS = "index.input.key.class";

    public static final String INPUT_VALUE_CLASS = "index.input.value.class";

    public static Analyzer getAnalyzer(Configuration conf) {
        Analyzer analyzer = null;
        try {
            analyzer = (Analyzer) Class.forName(conf.get(IndexConfiguration.INDEXER_ANALYZER, "org.apache.lucene.analysis.standard.StandardAnalyzer")).newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return analyzer;
    }

    @SuppressWarnings("unchecked")
    public JobConf createJobConf(final Configuration configuration) {
        JobConf jobConf = new JobConf(configuration);
        jobConf.setJobName("Index Xml");
        final Set<Object> keySet = _properties.keySet();
        for (final Object key : keySet) {
            final String value = getProperty((String) key);
            if (key.equals(INPUT_FORMAT_CLASS)) {
                jobConf.setInputFormat((Class<? extends InputFormat>) getClass(key.toString()));
            } else {
                jobConf.set(key.toString(), value);
            }
        }
        jobConf.setMapOutputKeyClass(Text.class);
        jobConf.setMapOutputValueClass(BytesWritable.class);
        jobConf.setMapperClass(ShardSelectionMapper.class);
        jobConf.setReducerClass(Indexer.class);
        jobConf.setNumReduceTasks(getInt(INDEX_SHARD_COUNT));
        jobConf.setReduceSpeculativeExecution(false);
        return jobConf;
    }

    public void enrichJobConf(Configuration conf, String confKey) {
        conf.set(confKey, getProperty(confKey));
    }
}
