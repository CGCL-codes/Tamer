package edu.opexcavator.nlp.ir;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import edu.opexcavator.model.DocumentImpl;
import edu.opexcavator.model.Token;
import edu.opexcavator.model.postype.POSType;

/**
 * @author Jesica N. Fera
 *
 */
public interface InformationExtractor {

    /**
	 * Returns a map of extracted tokens and their frequency given a part of speech type
	 * @param prototypePOSType: defines the type of POS to return
	 * @param document
	 * @return map of tokens and their frequency
	 */
    public Map<Token, Double> getFrequencyListedWordsByPOS(POSType prototypePOSType, DocumentImpl document);

    /**
	 * Returns a list of extracted nouns from a document using frequency order.
	 * @param prototypeNoun: defines the type of nouns to return.
	 * @param a document
	 * @return a sorted list of nouns.
	 */
    public List<Token> getFrequentNounsSorted(POSType prototypeNoun, DocumentImpl document);

    /**
	 *  Returns a list of extracted nouns from a document using frequency order, limited to a frequency
	 *  threshold.
	 * @param prototypeNoun
	 * @param document
	 * @param termCountThreshold
	 * @return a sorted list of nouns limited to the given threshold
	 */
    public List<Token> getFrequentNounsSortedLimited(POSType prototypeNoun, DocumentImpl document, double termCountThreshold);

    /**
	 * Returns a list of extracted nouns from a document collection using frequency order, limited to a frequency
	 *  threshold.
	 * @param prototypeNoun
	 * @param documentCollection
	 * @param termCountThreshold
	 * @return
	 */
    public List<Token> getFrequentNounsSortedLimited(POSType prototypeNoun, Collection<DocumentImpl> documentCollection, double termCountThreshold);
}
