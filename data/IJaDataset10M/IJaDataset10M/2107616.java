package de.dfki.qallme;

import java.io.Serializable;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import net.sf.qallme.gen.ws.InternalServiceFault;
import net.sf.qallme.gen.ws.entailmenttest.AbstractAnnotatedSentence;
import net.sf.qallme.gen.ws.entailmenttest.AnnotatedSentenceId;
import net.sf.qallme.gen.ws.entailmenttest.AnnotatedSentenceRef;
import net.sf.qallme.gen.ws.entailmenttest.EntailmentResults;
import net.sf.qallme.gen.ws.entailmenttest.EntailmentTester;
import net.sf.qallme.gen.ws.entailmenttest.ObjectFactory;
import net.sf.qallme.gen.ws.entailmenttest.Pairs;
import net.sf.qallme.gen.ws.entailmenttest.Probabilities;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.EuclideanDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

/**
 * An web service implementation of the {@link EntailmentTester} web service
 * interface which is simply based on string similarity metrics.
 * 
 * @author Christian Spurk (cspurk@dfki.de)
 * @version SVN $Rev$ by $Author$
 */
@WebService(name = "EntailmentTester", portName = "EntailmentTesterPort", serviceName = "EntailmentTesterWS", targetNamespace = "http://qallme.sf.net/wsdl/entailmenttest.wsdl")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class EntailmentTesterWSProvider implements EntailmentTester {

    /**
	 * an {@link ObjectFactory} instance for creating result objects in this WS
	 * provider
	 */
    private static final ObjectFactory OBJ_FACTORY = new ObjectFactory();

    /**
	 * the lowest entailment probability that makes
	 * {@link #entails(String, String)} return {@code false}
	 */
    private static final float ENTAILMENT_PROBABILITY_THRESHOLD = 0.7f;

    /**
	 * the string similarity metrics which are internally used to calculate
	 * entailment probabilities
	 */
    private AbstractStringMetric[] metrics = null;

    /**
	 * Initializes the class or rather its member variables if necessary.
	 */
    private synchronized void initialize() {
        if (this.metrics == null) this.metrics = new AbstractStringMetric[] { new Levenshtein(), new CosineSimilarity(), new EuclideanDistance(), new MongeElkan() };
    }

    /**
	 * Returns whether one sentence entails another or not for each pair.
	 * 
	 * @param pairs
	 *            pairs of texts and hypotheses for which to calculate the
	 *            entailment result
	 * @return a list of boolean values for each given pair: {@code true} if the
	 *         first sentence of a pair entails the second; {@code false}
	 *         otherwise
	 * @throws InternalServiceFault
	 * @see EntailmentTester#entails(Pairs)
	 */
    @Override
    @WebMethod
    @WebResult(name = "entailmentResults", partName = "entailmentResults")
    public EntailmentResults entails(@WebParam(name = "pairs", partName = "pairs") Pairs pairs) throws InternalServiceFault {
        Probabilities probabilities = getEntailmentProbability(pairs);
        EntailmentResults results = OBJ_FACTORY.createEntailmentResults();
        for (float prob : probabilities.getProbability()) results.getEntails().add(prob > ENTAILMENT_PROBABILITY_THRESHOLD);
        return results;
    }

    /**
	 * Returns the probabilities for whether one sentence entails another in
	 * each given pair.
	 * 
	 * @param pairs
	 *            pairs of texts and hypotheses for which to calculate the
	 *            entailment probability
	 * @return the entailment probabilities for the given sentence pairs
	 * @throws InternalServiceFault
	 * @see EntailmentTester#getEntailmentProbability(Pairs)
	 */
    @Override
    @WebMethod
    @WebResult(name = "probabilities", partName = "probabilities")
    public Probabilities getEntailmentProbability(@WebParam(name = "pairs", partName = "pairs") Pairs pairs) throws InternalServiceFault {
        initialize();
        Probabilities result = OBJ_FACTORY.createProbabilities();
        List<AbstractAnnotatedSentence> sentences = pairs.getSentencePair();
        for (int i = 0; i < sentences.size(); i += 2) result.getProbability().add(calculateEntailmentProbability(sentences.get(i), sentences.get(i + 1)));
        return result;
    }

    /**
	 * Calculates and returns the entailment probability for the given
	 * sentences.
	 * 
	 * @param sentence1
	 *            the entailment text
	 * @param sentence2
	 *            the entailment hypothesis
	 * @return the extracted entailment probability
	 * @throws InternalServiceFault
	 *             if the internally calculated entailment value is not a valid
	 *             probability value
	 */
    private float calculateEntailmentProbability(AbstractAnnotatedSentence sentence1, AbstractAnnotatedSentence sentence2) throws InternalServiceFault {
        float prelResult = 0.0f;
        for (AbstractStringMetric metric : this.metrics) prelResult += metric.getSimilarity(maskEntityPlaceholders(sentence1), maskEntityPlaceholders(sentence2));
        float result = prelResult / this.metrics.length;
        if (result < 0 || result > 1) throw new InternalServiceFault("The web service encountered an internal fault.", "The calculated entailment result is not a valid probability value.");
        return result;
    }

    /**
	 * Masks entity placeholders in the given sentence in order to not confuse
	 * the RTE engine and returns the resulting sentence.
	 * 
	 * @param sentence
	 *            the sentence in which to mask entity placeholders
	 * @return the sentence with masked placeholders
	 */
    private static String maskEntityPlaceholders(AbstractAnnotatedSentence sentence) {
        if (sentence instanceof AnnotatedSentenceRef) sentence = (AnnotatedSentenceId) ((AnnotatedSentenceRef) sentence).getRef();
        StringBuilder result = new StringBuilder();
        for (Serializable s : sentence.getContent()) if (s instanceof String) result.append(s); else result.append("XYZ");
        return result.toString();
    }
}
