package tfidf;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * WordFrequenceInDocMapper implements the Job 1 specification for the TF-IDF algorithm
 */
public class WordFrequenceInDocMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    /**
     * Google's search Stopwords
     */
    private static Set<String> googleStopwords;

    static {
        googleStopwords = new HashSet<String>();
        googleStopwords.add("I");
        googleStopwords.add("a");
        googleStopwords.add("about");
        googleStopwords.add("an");
        googleStopwords.add("are");
        googleStopwords.add("as");
        googleStopwords.add("at");
        googleStopwords.add("be");
        googleStopwords.add("by");
        googleStopwords.add("com");
        googleStopwords.add("de");
        googleStopwords.add("en");
        googleStopwords.add("for");
        googleStopwords.add("from");
        googleStopwords.add("how");
        googleStopwords.add("in");
        googleStopwords.add("is");
        googleStopwords.add("it");
        googleStopwords.add("la");
        googleStopwords.add("of");
        googleStopwords.add("on");
        googleStopwords.add("or");
        googleStopwords.add("that");
        googleStopwords.add("the");
        googleStopwords.add("this");
        googleStopwords.add("to");
        googleStopwords.add("was");
        googleStopwords.add("what");
        googleStopwords.add("when");
        googleStopwords.add("where");
        googleStopwords.add("who");
        googleStopwords.add("will");
        googleStopwords.add("with");
        googleStopwords.add("and");
        googleStopwords.add("the");
        googleStopwords.add("www");
    }

    /**
     * Pattern used to select the words
     */
    private static final Pattern PATTERN = Pattern.compile("\\w+");

    /**
     * Default word 
     */
    private Text word = new Text();

    /**
     * Default single counter
     */
    private IntWritable singleCount = new IntWritable(1);

    public WordFrequenceInDocMapper() {
    }

    /**
     * @param key is the byte offset of the current line in the file;
     * @param value is the line from the file
     * @param output has the method "collect()" to output the key,value pair
     * @param reporter allows us to retrieve some information about the job (like the current filename) 
     * 
     *     POST-CONDITION: Output <"word", "filename@offset"> pairs
     */
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Matcher m = PATTERN.matcher(value.toString());
        String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
        StringBuilder valueBuilder = new StringBuilder();
        while (m.find()) {
            String matchedKey = m.group().toLowerCase();
            if (!Character.isLetter(matchedKey.charAt(0)) || Character.isDigit(matchedKey.charAt(0)) || googleStopwords.contains(matchedKey) || matchedKey.contains("_") || matchedKey.length() < 3) {
                continue;
            }
            valueBuilder.append(matchedKey);
            valueBuilder.append("@");
            valueBuilder.append(fileName);
            this.word.set(valueBuilder.toString());
            context.write(this.word, this.singleCount);
            valueBuilder.setLength(0);
        }
    }
}
