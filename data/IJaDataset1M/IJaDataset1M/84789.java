package index;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * WordCountsForDocsReducer counts the number of documents in the
 */
public class WordsInCorpusTFIDFReducer extends Reducer<Text, Text, Text, Text> {

    private static final DecimalFormat DF = new DecimalFormat("###.########");

    private Text wordAtDocument = new Text();

    private Text tfidfCounts = new Text();

    public WordsInCorpusTFIDFReducer() {
    }

    /**
     * @param key is the key of the mapper
     * @param values are all the values aggregated during the mapping phase
     * @param context contains the context of the job run PRE-CONDITION: receive a list of <word, ["doc1=n1/N1",
     *            "doc2=n2/N2"]> POST-CONDITION: <"word@doc1#D, [n+n, N, TF-IDF]">, <"word2@a.txt, 5/13">
     */
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        int numberOfDocumentsInCorpus = context.getConfiguration().getInt("docsInCorpus", 0);
        int numberOfDocumentsInCorpusWhereKeyAppears = 0;
        Map<String, String> tempFrequencies = new HashMap<String, String>();
        for (Text val : values) {
            String[] documentAndFrequencies = val.toString().split("=");
            numberOfDocumentsInCorpusWhereKeyAppears++;
            tempFrequencies.put(documentAndFrequencies[0], documentAndFrequencies[1]);
        }
        for (String document : tempFrequencies.keySet()) {
            String[] wordFrequenceAndTotalWords = tempFrequencies.get(document).split("/");
            double tf = Double.valueOf(Double.valueOf(wordFrequenceAndTotalWords[0]) / Double.valueOf(wordFrequenceAndTotalWords[1]));
            double idf = (double) numberOfDocumentsInCorpus / (double) numberOfDocumentsInCorpusWhereKeyAppears;
            double tfIdf = numberOfDocumentsInCorpus == numberOfDocumentsInCorpusWhereKeyAppears ? tf : tf * Math.log10(idf);
            this.wordAtDocument.set(key + "@" + document);
            this.tfidfCounts.set("[" + numberOfDocumentsInCorpusWhereKeyAppears + "/" + numberOfDocumentsInCorpus + " , " + wordFrequenceAndTotalWords[0] + "/" + wordFrequenceAndTotalWords[1] + " , " + DF.format(tfIdf) + "]");
            context.write(this.wordAtDocument, this.tfidfCounts);
        }
    }
}
