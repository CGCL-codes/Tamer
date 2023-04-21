package document.lookup;

import java.util.HashMap;

/**
 * @author pon3
 * Language model class
 */
public class LanguageModelSummary {

    protected HashMap termFrequencies;

    protected double documentLength;

    protected double score;

    /**
	 * Constructor
	 * @param termFrequencies term frequency map
	 * @param documentLength document length
	 */
    public LanguageModelSummary(HashMap termFrequencies, double documentLength) {
        this.termFrequencies = termFrequencies;
        this.documentLength = documentLength;
    }

    /**
	 * Finds the P(term|M)
	 * @param term term
	 * @return
	 */
    public double getP_w_given_M(String term) {
        Double freq = (Double) termFrequencies.get(term);
        if (freq == null) return 0; else return freq.doubleValue() / documentLength;
    }

    /**
	 * Update the language model
	 * @param term term 
	 * @param count count to add
	 */
    public void update(String term, double count) {
        Double currentCount = null;
        if (termFrequencies.containsKey(term)) currentCount = (Double) termFrequencies.get(term); else currentCount = new Double(0);
        termFrequencies.put(term, new Double(currentCount.doubleValue() + count));
        documentLength += count;
    }

    /**
	 * @return score of the language model
	 */
    public double getScore() {
        return score;
    }

    /**
	 * @param score score of the language model
	 */
    public void setScore(double score) {
        this.score = score;
    }

    /**
	 * @return term frequency map
	 */
    public HashMap getTermFrequencies() {
        return termFrequencies;
    }
}
