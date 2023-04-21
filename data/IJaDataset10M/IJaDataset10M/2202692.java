package uk.ac.gla.terrier.matching.tsms;

import uk.ac.gla.terrier.utility.FieldScore;

/**
 * Modifies the scores of a term for a document, given 
 * the tags the term appears in the document. This class
 * implements the TermScoreModifier interface.
 * @author Vassilis Plachouras
 * @version $Revision: 1.10 $.
 */
public class FieldScoreModifier implements TermScoreModifier {

    /** 
	 * Modifies the scores of documents for a particular term, based on 
	 * the fields a term appears in documents.
	 * @param scores double[] the scores of the documents.
	 * @param pointers int[][] the pointers read from the inverted file 
	 *        for a particular query term.
	 * @return the number of documents for which the scores were modified. 
	 */
    public int modifyScores(double[] scores, int[][] pointers) {
        int[] fieldscores = pointers[2];
        final int numOfPointers = fieldscores.length;
        int numOfModifiedDocs = 0;
        int fieldScore;
        for (int j = 0; j < numOfPointers; j++) {
            fieldScore = fieldscores[j];
            if (fieldScore > 0) {
                if (scores[j] != Double.NEGATIVE_INFINITY) numOfModifiedDocs++;
                scores[j] += FieldScore.applyFieldScoreModifier(fieldScore, scores[j]);
            }
        }
        return numOfModifiedDocs;
    }

    public String getName() {
        return "FieldScoreModifier";
    }

    public Object clone() {
        return this;
    }
}
