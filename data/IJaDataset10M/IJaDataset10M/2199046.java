package darwInvest.news.tfidf;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.ujmp.core.*;

/**
 * The index class maintains an inverted index of several documents
 * This was originally written for CS4300, and modified slightly
 * for use with darwInvest
 * 
 * @author Kevin Dolan
 */
public class Index {

    private static final long serialVersionUID = -5674812940089816891L;

    private TreeMap<String, TreeMap<Integer, Integer>> index;

    private Hashtable<String, Integer> termTable;

    private Hashtable<Integer, String> inverseTermTable;

    private int newsCount;

    public Index() {
        index = new TreeMap<String, TreeMap<Integer, Integer>>();
        newsCount = 0;
    }

    /**
	 * @return the number of documents in the index
	 */
    public int getNumberDocuments() {
        return newsCount;
    }

    /**
	 * @return the number of unique terms
	 */
    public int getNumberTerms() {
        return index.size();
    }

    /**
	 * Notify the index that a document is being added
	 * @return the index that refers to this document
	 */
    public int addDocument() {
        newsCount++;
        return newsCount - 1;
    }

    /**
	 * Add an instance of a term to the index
	 * @param document the index of the document to add
	 * @param term	   the term being added
	 */
    public void addInstance(int document, String term) {
        TreeMap<Integer, Integer> postings = index.get(term);
        if (postings == null) {
            postings = new TreeMap<Integer, Integer>();
            index.put(term, postings);
            termTable = null;
            inverseTermTable = null;
        }
        Integer posting = postings.get(document);
        if (posting == null) posting = 1; else posting++;
        postings.put(document, posting);
    }

    /**
	 * Return the document frequency of a term
	 * @param term the term to look up
	 * @return 	   the number of documents in which the term appears
	 */
    public int getDocumentFrequency(String term) {
        TreeMap<Integer, Integer> postings = index.get(term);
        if (postings == null) return 0; else return postings.size();
    }

    /**
	 * Return the term at index specified
	 * @param index the index of the term
	 */
    public String getTermByIndex(int index) {
        return inverseTermTable.get(index);
    }

    /**
	 * Return the term frequency of a term in a document
	 * @param term  the term to lookup
	 * @param docId the id of the document to lookup
	 * @return 		the number of times the term appears in the document
	 */
    public int getTermFrequency(String term, int docId) {
        TreeMap<Integer, Integer> postings = index.get(term);
        if (postings == null) return 0; else {
            Integer posting = postings.get(docId);
            if (posting == null) return 0; else return posting;
        }
    }

    /**
	 * Return a matrix representing a column-vector of
	 * the pseudo document, where the value is 1 if the
	 * input tokens contains the term, 0 otherwise
	 * @param tokens the list of tokens in the pseudo document
	 * @return		 a column-vector
	 */
    public Matrix getPseudoDocumentVector(List<String> tokens) {
        if (termTable == null) buildTermTable();
        Matrix result = MatrixFactory.sparse(getNumberTerms(), 1);
        for (String token : tokens) {
            Integer index = termTable.get(token);
            if (index != null) result.setAsDouble(1., index, 0);
        }
        return result;
    }

    /**
	 * Calculate the tf.idf weighted, unit normalized
	 * document incidence matrix for this index
	 * @return the document incidence matrix
	 */
    public Matrix getDocumentIncidenceMatrix() {
        Matrix result = MatrixFactory.sparse(getNumberDocuments(), getNumberTerms());
        Set<Entry<String, TreeMap<Integer, Integer>>> indexSet = index.entrySet();
        int j = 0;
        for (Entry<String, TreeMap<Integer, Integer>> indexItem : indexSet) {
            TreeMap<Integer, Integer> postings = indexItem.getValue();
            double idf = Math.log((double) getNumberDocuments() / postings.size());
            Set<Entry<Integer, Integer>> postingsSet = postings.entrySet();
            for (Entry<Integer, Integer> postingsItem : postingsSet) {
                int i = postingsItem.getKey();
                double tf = Math.log(1 + postingsItem.getValue());
                result.setAsDouble(tf * idf, i, j);
            }
            j++;
        }
        for (int i = 0; i < getNumberDocuments(); i++) {
            double sum = 0;
            for (j = 0; j < getNumberTerms(); j++) {
                double value = result.getAsDouble(i, j);
                sum += value * value;
            }
            double length = Math.sqrt(sum);
            for (j = 0; j < getNumberTerms(); j++) {
                double value = result.getAsDouble(i, j);
                result.setAsDouble(value / length, i, j);
            }
        }
        return result;
    }

    /**
	 * Build the hash-table for term-index association
	 */
    private void buildTermTable() {
        termTable = new Hashtable<String, Integer>();
        inverseTermTable = new Hashtable<Integer, String>();
        Set<String> terms = index.keySet();
        int j = 0;
        for (String term : terms) {
            termTable.put(term, j);
            inverseTermTable.put(j, term);
            j++;
        }
    }

    public String toString() {
        String output = "";
        Set<Entry<String, TreeMap<Integer, Integer>>> entries = index.entrySet();
        for (Entry<String, TreeMap<Integer, Integer>> entry : entries) {
            String term = entry.getKey();
            output += term + "\n";
            Set<Entry<Integer, Integer>> postings = entry.getValue().entrySet();
            for (Entry<Integer, Integer> posting : postings) {
                int doc = posting.getKey();
                Integer post = posting.getValue();
                output += "\t" + doc + ":\t" + post;
                output += "\n";
            }
        }
        return output;
    }
}
