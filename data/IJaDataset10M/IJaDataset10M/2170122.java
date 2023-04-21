package net.sf.jtmt.indexers.hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import net.sf.jtmt.tokenizers.lucene.NumericTokenFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * A 3-stage Hadoop job that reads a sequence file of blog articles,
 * and produces a term document vector of the article collection as
 * its output. The phases are as follows:
 * (1) Analyze each article using a custom Lucene analyzer and use
 *     Lucene's Term API to write out (docId:word) => occurrence.
 * (2) Read output of previous step and write out word => occurrence.
 * (3) Read output of step 2 to create a word to position map. Read
 *     output of step 1 to map docId => (word:occurrence) in the map
 *     phase, and docId => position,... in the reduce phase.
 * @author Sujit Pal
 * @version $Revision: 55 $
 */
public class TermDocumentMatrixGenerator {

    /** Minimum occurrence for a given word in a document to be counted */
    private static final int MIN_OCCUR_THRESHOLD = 2;

    /** Key for the stop word file */
    private static final String STOPWORD_FILE_LOCATION_KEY = "stf.loc.key";

    /** Key for the terms directory generated in phase 2 */
    private static final String TERMS_DIR_KEY = "terms.dir.key";

    /**
   * Map phase converts an input document into a set of (docId:word) 
   * => occurrence pairs. Stop words are removed, words are stemmed,
   * etc, using a custom analyzer. Words which occur below a threshold
   * are removed from consideration.
   */
    public static class Mapper1 extends Mapper<Text, MapWritable, Text, LongWritable> {

        private Analyzer analyzer;

        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            FileSystem hdfs = FileSystem.get(conf);
            FSDataInputStream fis = hdfs.open(new Path(conf.get(STOPWORD_FILE_LOCATION_KEY)));
            final Set<String> stopset = new HashSet<String>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = reader.readLine()) != null) {
                stopset.add(line);
            }
            reader.close();
            fis.close();
            analyzer = new Analyzer() {

                public TokenStream tokenStream(String fieldName, Reader reader) {
                    return new PorterStemFilter(new StopFilter(false, new LowerCaseFilter(new NumericTokenFilter(new StandardFilter(new StandardTokenizer(Version.LUCENE_30, reader)))), stopset));
                }
            };
        }

        @Override
        public void map(Text key, MapWritable value, Context context) throws IOException, InterruptedException {
            Text content = (Text) value.get(new Text("content"));
            TermFreqVector contentTf = getTermFrequencyVector(content.toString());
            String[] terms = contentTf.getTerms();
            int[] frequencies = contentTf.getTermFrequencies();
            for (int i = 0; i < terms.length; i++) {
                Text outputKey = new Text(StringUtils.join(new String[] { key.toString(), terms[i] }, ":"));
                context.write(outputKey, new LongWritable(frequencies[i]));
            }
        }

        private TermFreqVector getTermFrequencyVector(String content) throws IOException {
            RAMDirectory ramdir = new RAMDirectory();
            IndexWriter writer = new IndexWriter(ramdir, analyzer, MaxFieldLength.UNLIMITED);
            Document doc = new Document();
            doc.add(new Field("text", content.toString(), Store.YES, Index.ANALYZED, TermVector.YES));
            writer.addDocument(doc);
            writer.commit();
            writer.close();
            IndexReader reader = IndexReader.open(ramdir, true);
            TermFreqVector termFreqVector = reader.getTermFreqVector(0, "text");
            reader.close();
            return termFreqVector;
        }
    }

    /**
   * Sums up the occurrences of (docId:word) occurrences and removes
   * those which occur infrequently.
   */
    public static class Reducer1 extends Reducer<Text, LongWritable, Text, LongWritable> {

        @Override
        public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0L;
            for (LongWritable value : values) {
                sum += value.get();
            }
            if (sum > MIN_OCCUR_THRESHOLD) {
                context.write(key, new LongWritable(sum));
            }
        }
    }

    /**
   * Extract the word from the key of the previous output, and emit
   * them. This will be used to build a word to position map.
   */
    public static class Mapper2 extends Mapper<Text, LongWritable, Text, LongWritable> {

        private static final LongWritable ONE = new LongWritable(1);

        @Override
        public void map(Text key, LongWritable value, Context context) throws IOException, InterruptedException {
            String word = StringUtils.split(key.toString(), ":")[1];
            context.write(new Text(word), ONE);
        }
    }

    /**
   * Aggregates the word count. 
   */
    public static class Reducer2 extends Reducer<Text, LongWritable, Text, LongWritable> {

        @Override
        public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long occurrences = 0;
            for (LongWritable value : values) {
                occurrences++;
            }
            context.write(key, new LongWritable(occurrences));
        }
    }

    /**
   * Reads the output of step 1 and emits docId => (word:occurrence)
   * pairs.
   */
    public static class Mapper3 extends Mapper<Text, LongWritable, Text, Text> {

        @Override
        public void map(Text key, LongWritable value, Context context) throws IOException, InterruptedException {
            String[] pair = StringUtils.split(key.toString(), ":");
            context.write(new Text(pair[0]), new Text(StringUtils.join(new String[] { pair[1], String.valueOf(value.get()) }, ":")));
        }
    }

    /**
   * Converts the output of step 2 into a map of word and position.
   * Flattens the docId => (word:occurrence) pairs to docId => dense
   * positional list of occurrences. 
   */
    public static class Reducer3 extends Reducer<Text, Text, Text, Text> {

        private Map<String, Integer> terms = new HashMap<String, Integer>();

        @Override
        public void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            FileSystem hdfs = FileSystem.get(conf);
            FileStatus[] partFiles = hdfs.listStatus(new Path(conf.get(TERMS_DIR_KEY)));
            for (FileStatus partFile : partFiles) {
                if (!partFile.getPath().getName().startsWith("part-r")) {
                    continue;
                }
                FSDataInputStream fis = hdfs.open(partFile.getPath());
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String line = null;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    String term = StringUtils.split(line, "\t")[0];
                    terms.put(term, i);
                    i++;
                }
                reader.close();
                fis.close();
            }
        }

        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            SortedMap<String, Long> frequencies = new TreeMap<String, Long>();
            for (Text value : values) {
                String[] parts = StringUtils.split(value.toString(), ":");
                String word = parts[0];
                Long occurrence = new Long(parts[1]);
                frequencies.put(word, occurrence);
            }
            context.write(key, flatten(frequencies));
        }

        private Text flatten(SortedMap<String, Long> frequencies) {
            long[] array = new long[terms.size()];
            for (String word : frequencies.keySet()) {
                int pos = terms.get(word);
                array[pos] = frequencies.get(word);
            }
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    buf.append(",");
                }
                buf.append(String.valueOf(array[i]));
            }
            return new Text(buf.toString());
        }
    }

    /**
   * Calling method.
   * @param argv command line args.
   * @throws Exception if thrown.
   */
    public static void main(String[] argv) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, argv).getRemainingArgs();
        if (otherArgs.length != 4) {
            System.err.println("Usage: tdmg <prefix> <stopwords> <indir> <outdir>");
            System.exit(-1);
        }
        Path basedir = new Path(otherArgs[0] + otherArgs[2]).getParent();
        Job job1 = new Job(conf, "phase-1");
        job1.getConfiguration().set(STOPWORD_FILE_LOCATION_KEY, otherArgs[0] + otherArgs[1]);
        FileInputFormat.addInputPath(job1, new Path(otherArgs[0] + otherArgs[2]));
        FileOutputFormat.setOutputPath(job1, new Path(basedir, "temp1"));
        job1.setJarByClass(TermDocumentMatrixGenerator.class);
        job1.setMapperClass(Mapper1.class);
        job1.setReducerClass(Reducer1.class);
        job1.setInputFormatClass(SequenceFileInputFormat.class);
        job1.setOutputFormatClass(SequenceFileOutputFormat.class);
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(LongWritable.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(LongWritable.class);
        job1.setNumReduceTasks(2);
        boolean job1Success = job1.waitForCompletion(true);
        if (!job1Success) {
            System.err.println("Job1 failed, exiting");
            System.exit(-1);
        }
        Job job2 = new Job(conf, "phase-2");
        FileInputFormat.addInputPath(job2, new Path(basedir, "temp1"));
        FileOutputFormat.setOutputPath(job2, new Path(basedir, "temp2"));
        job2.setJarByClass(TermDocumentMatrixGenerator.class);
        job2.setMapperClass(Mapper2.class);
        job2.setReducerClass(Reducer2.class);
        job2.setInputFormatClass(SequenceFileInputFormat.class);
        job2.setOutputFormatClass(TextOutputFormat.class);
        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(LongWritable.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(LongWritable.class);
        job2.setNumReduceTasks(2);
        boolean job2Success = job2.waitForCompletion(true);
        if (!job2Success) {
            System.err.println("Job2 failed, exiting");
            System.exit(-1);
        }
        Job job3 = new Job(conf, "phase-3");
        job3.getConfiguration().set(TERMS_DIR_KEY, basedir + "/temp2");
        FileInputFormat.addInputPath(job3, new Path(basedir, "temp1"));
        FileOutputFormat.setOutputPath(job3, new Path(otherArgs[0] + otherArgs[3]));
        job3.setJarByClass(TermDocumentMatrixGenerator.class);
        job3.setMapperClass(Mapper3.class);
        job3.setReducerClass(Reducer3.class);
        job3.setInputFormatClass(SequenceFileInputFormat.class);
        job3.setOutputFormatClass(TextOutputFormat.class);
        job3.setMapOutputKeyClass(Text.class);
        job3.setMapOutputValueClass(Text.class);
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(Text.class);
        job3.setNumReduceTasks(2);
        System.exit(job3.waitForCompletion(true) ? 0 : 1);
    }
}
