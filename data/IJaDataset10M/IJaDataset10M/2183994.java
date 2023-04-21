package uk.ac.gla.terrier.matching.models.queryexpansion;

import uk.ac.gla.terrier.matching.models.Idf;

/** 
 * This class implements the Bo2 model for query expansion. 
 * See G. Amati's Phd Thesis.
 * @author Gianni Amati, Ben He
 * @version $Revision: 1.18 $
 */
public class Bo2 extends QueryExpansionModel {

    /** A default constructor.*/
    public Bo2() {
        super();
    }

    /**
	 * Returns the name of the model.
	 * @return the name of the model
	 */
    public final String getInfo() {
        if (PARAMETER_FREE) return "Bo2bfree";
        return "Bo2b" + ROCCHIO_BETA;
    }

    /**
     * This method computes the normaliser of parameter-free query expansion.
     * @return The normaliser.
     */
    public final double parameterFreeNormaliser() {
        double f = (maxTermFrequency) * totalDocumentLength / collectionLength;
        return ((maxTermFrequency) * Idf.log((1d + f) / f) + Idf.log(1d + f));
    }

    /**
     * This method computes the normaliser of parameter-free query expansion.
     * @param maxTermFrequency The maximum of the term frequency of the query terms.
     * @param collectionLength The number of tokens in the collections.
     * @param totalDocumentLength The sum of the length of the top-ranked documents.
     * @return The normaliser.
     */
    public final double parameterFreeNormaliser(double maxTermFrequency, double collectionLength, double totalDocumentLength) {
        double f = (maxTermFrequency) * totalDocumentLength / collectionLength;
        return ((maxTermFrequency) * Idf.log((1d + f) / f) + Idf.log(1d + f));
    }

    /** This method implements the query expansion model.
	 *  @param withinDocumentFrequency double The term frequency 
	 *         in the X top-retrieved documents.
	 *  @param termFrequency double The term frequency in the collection.
	 *  @return double The query expansion weight using the Bose-Einstein statistics
	 *  where the mean is given by the Bernoulli process.
	 */
    public final double score(double withinDocumentFrequency, double termFrequency) {
        double f = withinDocumentFrequency * totalDocumentLength / collectionLength;
        return withinDocumentFrequency * Idf.log((1d + f) / f) + Idf.log(1d + f);
    }

    /**
	 * This method implements the query expansion model.
	 * @param withinDocumentFrequency double The term frequency 
	 *        in the X top-retrieved documents.
	 * @param termFrequency double The term frequency in the collection.
	 * @param totalDocumentLength double The sum of length of 
	 *        the X top-retrieved documents.
	 * @param collectionLength double The number of tokens in the whole collection.
	 * @param averageDocumentLength double The average document 
	 *        length in the collection.
	 * @return double The score returned by the implemented model.
	 */
    public final double score(double withinDocumentFrequency, double termFrequency, double totalDocumentLength, double collectionLength, double averageDocumentLength) {
        double f = withinDocumentFrequency * totalDocumentLength / collectionLength;
        return withinDocumentFrequency * Idf.log((1d + f) / f) + Idf.log(1d + f);
    }
}
