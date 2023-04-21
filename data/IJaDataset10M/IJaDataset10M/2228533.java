package vi.crawl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import vi.Doc;
import vi.crawl.Filter.CustomFilter;

/**
 * Custom filter which filters on duplicate content (based on cosine similarity) in repository 
 * @author Daniel Petras
 * 
 */
public class DuplicateContentCustomFilter implements CustomFilter {

    /**
	 * Repository containing documents to compare to
	 */
    private PersistentRepository rep;

    /**
	 * List of all stems found in all filtered documents
	 */
    private List<String> allStems;

    /**
	 * Map containing map of stem index and TF pair, mapped to doc URL
	 */
    private Map<URL, Map<Integer, Float>> docStems;

    /**
	 * Analyzer that use lower case and whitespace tokenizers, and than stop and porter stem filters
	 */
    private Analyzer stemAnalyzer;

    /**
	 * Value that must be passed to filter document out (0 - no correlation, 1 - full correlation)
	 */
    private float threshold = 0.9f;

    /**
	 * Which evaluation method to use (TF / TF-IDF)
	 */
    private EvaluationMethod method;

    /**
	 * Map containing stem index and idf pair
	 */
    private Map<Integer, Float> idfMap;

    /**
	 * Type representing evaluation type for word stems
	 * @author Daniel Petras
	 */
    public enum EvaluationMethod {

        /**
		 * Term Frequency
		 */
        TF {

            @Override
            public String toString() {
                return "TF";
            }
        }
        , /**
		 * Term Frequency - Inverse Document Frequency
		 */
        TF_IDF {

            @Override
            public String toString() {
                return "TF-IDF";
            }
        }

    }

    public DuplicateContentCustomFilter(PersistentRepository repository) {
        this(repository, StandardAnalyzer.STOP_WORDS_SET);
    }

    public DuplicateContentCustomFilter(PersistentRepository repository, final Set<?> stopWords) {
        rep = repository;
        allStems = new ArrayList<String>();
        docStems = new HashMap<URL, Map<Integer, Float>>();
        idfMap = new HashMap<Integer, Float>();
        method = EvaluationMethod.TF_IDF;
        stemAnalyzer = new Analyzer() {

            public final TokenStream tokenStream(String fieldName, Reader reader) {
                return new PorterStemFilter(new StopFilter(false, new WhitespaceTokenizer(new LowerCaseTokenizer(reader), reader), stopWords));
            }
        };
    }

    /**
	 * Reset custom filter to be used on new set of documents
	 */
    public void reset() {
        allStems.clear();
        docStems.clear();
    }

    /**
	 * Set current threshold for this custom filter.
	 * Threshold is value which must be lower than cosine
	 * distance between documents to be filtered.
	 * @param threshold threshold to be set
	 */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    /**
	 * Get current threshold for this custom filter
	 * Threshold is value which must be lower than cosine
	 * distance between documents to be filtered.
	 * @return current threshold
	 */
    public float getThreshold() {
        return threshold;
    }

    public void setEvaluationMethod(EvaluationMethod method) {
        this.method = method;
    }

    /**
	 * Get method to evaluate word stems in documents.
	 * It can be TF (term frequency) or TF-IDF (term frequency - inverse document frequency)
	 * @return current evaluation method
	 */
    public EvaluationMethod getEvaluationMethod() {
        return method;
    }

    @Override
    public boolean filter(Doc doc) {
        try {
            Map<Integer, Float> doc1Vector = getDocTfVector(doc.getBody());
            docStems.put(doc.getUrl(), doc1Vector);
            if (method == EvaluationMethod.TF_IDF) {
                refreshIdf(doc1Vector);
                doc1Vector = convertToTfIdf(doc1Vector);
            }
            for (Doc d : rep.getDocuments()) {
                try {
                    Map<Integer, Float> doc2vector = docStems.get(d.getUrl());
                    if (doc2vector == null) {
                        doc2vector = getDocTfVector(d.getBody());
                        docStems.put(d.getUrl(), doc2vector);
                    }
                    if (method == EvaluationMethod.TF_IDF) {
                        doc2vector = convertToTfIdf(doc2vector);
                    }
                    float sim = cosineSimilarity(doc1Vector, doc2vector);
                    if (sim > threshold) {
                        System.out.println("Document filtered based on similarity (" + method + "): " + sim);
                        System.out.println("Original document: " + d.getUrl());
                        System.out.println("Filtered document: " + doc.getUrl());
                        System.out.println();
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 * Converts TF vector to TF-IDF vector based on internal IDF map.
	 * Internal IDF map can be refreshed using refreshIdf() method
	 * @param tfVector - TF vector (won't be changed in the process)
	 * @return tfIdfVector - new instance with values converted
	 */
    private Map<Integer, Float> convertToTfIdf(final Map<Integer, Float> tfVector) {
        Map<Integer, Float> tfIdfVector = new HashMap<Integer, Float>();
        if (!idfMap.isEmpty()) {
            for (int index : tfVector.keySet()) {
                tfIdfVector.put(index, tfVector.get(index) * idfMap.get(index));
            }
        }
        return tfIdfVector;
    }

    /**
	 * Builds internal IDF map for each stem in all documents
	 * @throws IOException
	 */
    private void refreshIdf(final Map<Integer, Float> tfVector) throws IOException {
        idfMap.clear();
        final int docCount = rep.getDocuments().size();
        if (docCount == 0) {
            return;
        }
        for (Doc d : rep.getDocuments()) {
            Map<Integer, Float> tf2Vector = docStems.get(d.getUrl());
            if (tf2Vector == null) {
                tf2Vector = getDocTfVector(d.getBody());
                docStems.put(d.getUrl(), tf2Vector);
            }
            for (int stemIndex : tf2Vector.keySet()) {
                if (idfMap.containsKey(stemIndex)) {
                    idfMap.put(stemIndex, idfMap.get(stemIndex) + 1);
                } else {
                    idfMap.put(stemIndex, 1.0f);
                }
            }
        }
        for (int stemIndex : tfVector.keySet()) {
            if (idfMap.containsKey(stemIndex)) {
                idfMap.put(stemIndex, idfMap.get(stemIndex) + 1);
            } else {
                idfMap.put(stemIndex, 1.0f);
            }
        }
        for (int stemIndex : idfMap.keySet()) {
            idfMap.put(stemIndex, (float) Math.log10((docCount + 1) / idfMap.get(stemIndex)));
        }
    }

    /**
	 * Computes Term Frequency vector for given HTML text
	 * @param body Text representation of HTML body element
	 * @return Mapping from index of stem to TF in given text
	 * @throws IOException When TokenStream.incrementToken() fails
	 */
    private Map<Integer, Float> getDocTfVector(String body) throws IOException {
        TextExtractor te = new TextExtractor(new Source(body));
        TokenStream tokens = stemAnalyzer.tokenStream("", new StringReader(te.toString()));
        TermAttribute term = (TermAttribute) tokens.addAttribute(TermAttribute.class);
        int wordCount = 0;
        Map<Integer, Float> docVector = new HashMap<Integer, Float>();
        while (tokens.incrementToken()) {
            ++wordCount;
            String termStr = term.term();
            if (!allStems.contains(termStr)) {
                allStems.add(termStr);
            }
            int index = allStems.indexOf(termStr);
            if (docVector.containsKey(index)) {
                docVector.put(index, docVector.get(index) + 1);
            } else {
                docVector.put(index, 1.0f);
            }
        }
        for (int index : docVector.keySet()) {
            docVector.put(index, docVector.get(index) / wordCount);
        }
        return docVector;
    }

    /**
	 * Computes cosine similarity of two vectors.
	 * Vectors don't need to be same size, because are represented as maps (index -> value).
	 * Missing index entries are automatically considered to be equal to zero.
	 * @param v1 Vector one mapping
	 * @param v2 Vector two mapping
	 * @return Cosine similarity of the two vectors (from interval <0;1>)
	 */
    private static float cosineSimilarity(Map<Integer, Float> v1, Map<Integer, Float> v2) {
        Set<Integer> union = new HashSet<Integer>(v1.keySet());
        union.addAll(v2.keySet());
        float dot = 0;
        float len1 = 0, len2 = 0;
        for (int index : union) {
            Float val1obj = v1.get(index);
            Float val2obj = v2.get(index);
            float val1 = (val1obj != null ? val1obj : 0);
            float val2 = (val2obj != null ? val2obj : 0);
            dot += val1 * val2;
            len1 += val1 * val1;
            len2 += val2 * val2;
        }
        if (len1 == 0 || len2 == 0) return 0;
        return (float) (dot / (Math.sqrt(len1 * len2)));
    }

    public static void main(String[] args) throws Exception {
        PersistentRepository repository = new PersistentRepository();
        Crawler crawler = new BreadthFirstCrawler(repository);
        DuplicateContentCustomFilter duplicateFilter = new DuplicateContentCustomFilter(repository);
        duplicateFilter.setThreshold(0.70f);
        duplicateFilter.setEvaluationMethod(EvaluationMethod.TF_IDF);
        Filter filter = new Filter();
        filter.addContentTypes("text/html");
        filter.setMaxDepth(1);
        filter.addCustomFilter(duplicateFilter);
        try {
            crawler.crawlURL(new URL("http://en.wikipedia.org/w/index.php?title=Un_Concert_pour_Mazarin&action=history"), filter);
            filter.setMaxDepth(0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
