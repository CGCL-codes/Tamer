package uk.ac.gla.terrier.matching;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import uk.ac.gla.terrier.matching.dsms.DocumentScoreModifier;
import uk.ac.gla.terrier.matching.models.WeightingModel;
import uk.ac.gla.terrier.matching.tsms.TermScoreModifier;
import uk.ac.gla.terrier.structures.CollectionStatistics;
import uk.ac.gla.terrier.structures.DocumentIndex;
import uk.ac.gla.terrier.structures.Index;
import uk.ac.gla.terrier.structures.InvertedIndex;
import uk.ac.gla.terrier.structures.Lexicon;
import uk.ac.gla.terrier.structures.LexiconEntry;
import uk.ac.gla.terrier.utility.ApplicationSetup;
import uk.ac.gla.terrier.utility.HeapSort;

/**
 * Performs the matching of documents with a query, by
 * first assigning scores to documents for each query term
 * and modifying these scores with the appropriate modifiers.
 * Then, a series of document score modifiers are applied
 * if necessary.
 * 
 * @author Vassilis Plachouras
 * @version $Revision: 1.62 $
 */
public class Matching {

    /** the logger for this class */
    protected static Logger logger = Logger.getRootLogger();

    /**
	 * The default namespace for the term score modifiers that are
	 * specified in the properties file.
	 */
    protected static String tsmNamespace = "uk.ac.gla.terrier.matching.tsms.";

    /**
	 * The default namespace for the document score modifiers that are
	 * specified in the properties file.
	 */
    protected static String dsmNamespace = "uk.ac.gla.terrier.matching.dsms.";

    /** 
	 * The maximum number of documents in the final retrieved set.
	 * It corresponds to the property matching.retrieved_set_size.
	 * The default value is 1000, however, setting the property to 0
	 * will return all matched documents.
	 */
    protected static int RETRIEVED_SET_SIZE;

    /** 
	 * The maximum term frequency that is considered within a 
	 * document. This helps to deal with 'spamming' in documents,
	 * where a term appears suspiciously too many times. The 
	 * corresponding property is <tt>frequency.upper.threshold</tt>
	 * and the default value is 0, meaning that there is no 
	 * threshold. 
	 */
    protected static int FREQUENCY_UPPER_THRESHOLD;

    /**
	 * A property that enables to ignore the terms with a low
	 * IDF. In the match method, we check whether the frequency
	 * of a term in the collection is higher than the number of
	 * documents. If this is true, then by default we don't assign
	 * scores to documents that contain this term. We can change
	 * this default behavious by altering the corresponding property
	 * <tt>ignore.low.idf.terms</tt>, the default value of which is
	 * true.
	 */
    protected static boolean IGNORE_LOW_IDF_TERMS;

    /**
	 * A property that when it is true, it allows matching all documents
	 * to an empty query. In this case the ordering of documents is 
	 * random. More specifically, it is the ordering of documents in 
	 * the document index. The corresponding property is 
	 * <tt>match.empty.query</tt> and the default value is <tt>false</tt>.
	 */
    protected static boolean MATCH_EMPTY_QUERY;

    /** The number of retrieved documents for a query.*/
    protected int numberOfRetrievedDocuments;

    /**
	 * The index used for retrieval. 
	 */
    protected Index index;

    /** The document index used.*/
    protected DocumentIndex docIndex;

    /** The lexicon used.*/
    protected Lexicon lexicon;

    /** The inverted file.*/
    protected InvertedIndex invertedIndex;

    /** The collection statistics */
    protected CollectionStatistics collectionStatistics;

    /** The result set.*/
    protected ResultSet resultSet;

    /**
	 * Contains the term score modifiers to be
	 * applied for a query.
	 */
    protected ArrayList<TermScoreModifier> termModifiers;

    /**
	 * Contains the document score modifiers
	 * to be applied for a query.
	 */
    protected ArrayList<DocumentScoreModifier> documentModifiers;

    /** The weighting model used for retrieval.*/
    protected WeightingModel wmodel;

    protected Matching() {
    }

    /** 
	 * A default constructor that creates 
	 * the CollectionResultSet and initialises
	 * the document and term modifier containers.
	 * @param index the object that encapsulates the basic
	 *        data structures used for retrieval.
	 */
    public Matching(Index index) {
        termModifiers = new ArrayList<TermScoreModifier>();
        documentModifiers = new ArrayList<DocumentScoreModifier>();
        this.index = index;
        this.docIndex = index.getDocumentIndex();
        this.lexicon = index.getLexicon();
        this.invertedIndex = index.getInvertedIndex();
        this.collectionStatistics = index.getCollectionStatistics();
        resultSet = new CollectionResultSet(collectionStatistics.getNumberOfDocuments());
        String defaultTSMS = ApplicationSetup.getProperty("matching.tsms", "");
        String defaultDSMS = ApplicationSetup.getProperty("matching.dsms", "");
        try {
            for (String modifierName : defaultTSMS.split("\\s*,\\s*")) {
                if (modifierName.length() == 0) continue;
                if (modifierName.indexOf('.') == -1) modifierName = tsmNamespace + modifierName;
                addTermScoreModifier((TermScoreModifier) Class.forName(modifierName).newInstance());
            }
            for (String modifierName : defaultDSMS.split("\\s*,\\s*")) {
                if (modifierName.length() == 0) continue;
                if (modifierName.indexOf('.') == -1) modifierName = dsmNamespace + modifierName;
                addDocumentScoreModifier((DocumentScoreModifier) Class.forName(modifierName).newInstance());
            }
        } catch (Exception e) {
            logger.error("Exception while initialising default modifiers. Please check the name of the modifiers in the configuration file.", e);
        }
    }

    /**
	 * Returns the result set.
	 */
    public ResultSet getResultSet() {
        return resultSet;
    }

    /**
	 * Initialises the arrays prior of retrieval. Only the first time it is called,
	 * it will allocate memory for the arrays.
	 */
    protected void initialise() {
        resultSet.initialise();
        RETRIEVED_SET_SIZE = Integer.parseInt(ApplicationSetup.getProperty("matching.retrieved_set_size", "1000"));
        FREQUENCY_UPPER_THRESHOLD = Integer.parseInt(ApplicationSetup.getProperty("frequency.upper.threshold", "0"));
        IGNORE_LOW_IDF_TERMS = Boolean.parseBoolean(ApplicationSetup.getProperty("ignore.low.idf.terms", "true"));
        MATCH_EMPTY_QUERY = Boolean.parseBoolean(ApplicationSetup.getProperty("match.empty.query", "false"));
    }

    /**
	 * Initialises the arrays prior of retrieval, with 
	 * the given scores. Only the first time it is called,
	 * it will allocate memory for the arrays.
	 * @param scs double[] the scores to initialise the result set with.
	 */
    protected void initialise(double[] scs) {
        resultSet.initialise(scs);
        RETRIEVED_SET_SIZE = Integer.parseInt(ApplicationSetup.getProperty("matching.retrieved_set_size", "1000"));
        FREQUENCY_UPPER_THRESHOLD = Integer.parseInt(ApplicationSetup.getProperty("frequency.upper.threshold", "0"));
        IGNORE_LOW_IDF_TERMS = Boolean.parseBoolean(ApplicationSetup.getProperty("ignore.low.idf.terms", "true"));
        MATCH_EMPTY_QUERY = Boolean.parseBoolean(ApplicationSetup.getProperty("match.empty.query", "false"));
    }

    /**
	 * Registers a term score modifier. If more than one modifiers
	 * are registered, then they applied in the order they were registered.
	 * @param termScoreModifier TermScoreModifier the score modifier to be
	 *        applied.
	 */
    public void addTermScoreModifier(TermScoreModifier termScoreModifier) {
        termModifiers.add(termScoreModifier);
    }

    /**
	 * Returns the i-th registered term score modifier.
	 * @return the i-th registered term score modifier.
	 */
    public TermScoreModifier getTermScoreModifier(int i) {
        return (TermScoreModifier) termModifiers.get(i);
    }

    /**
	 * Registers a document score modifier. If more than one modifiers
	 * are registered, then they applied in the order they were registered.
	 * @param documentScoreModifier DocumentScoreModifier the score modifier to be
	 *        applied. 
	 */
    public void addDocumentScoreModifier(DocumentScoreModifier documentScoreModifier) {
        documentModifiers.add(documentScoreModifier);
    }

    /**
	 * Returns the i-th registered document score modifier.
	 * @return the i-th registered document score modifier.
	 */
    public DocumentScoreModifier getDocumentScoreModifier(int i) {
        return (DocumentScoreModifier) documentModifiers.get(i);
    }

    /**
	 * Sets the weihting model used for retrieval.
	 * @param model the weighting model used for retrieval
	 */
    public void setModel(Model model) {
        wmodel = (WeightingModel) model;
    }

    /**
	 * Returns a descriptive string for the retrieval process performed.
	 */
    public String getInfo() {
        return wmodel.getInfo();
    }

    /**
	 * Implements the matching of a query with the documents.
	 * @param queryNumber the identifier of the processed query.
	 * @param queryTerms the query terms to be processed.
	 */
    public void match(String queryNumber, MatchingQueryTerms queryTerms) {
        initialise();
        DocumentScoreModifier[] dsms;
        int NumberOfQueryDSMs = 0;
        dsms = queryTerms.getDocumentScoreModifiers();
        if (dsms != null) NumberOfQueryDSMs = dsms.length;
        TermScoreModifier[] tsms;
        String[] queryTermStrings = queryTerms.getTerms();
        if (MATCH_EMPTY_QUERY && queryTermStrings.length == 0) {
            resultSet.setExactResultSize(collectionStatistics.getNumberOfDocuments());
            resultSet.setResultSize(collectionStatistics.getNumberOfDocuments());
            return;
        }
        int[] docids = resultSet.getDocids();
        double[] scores = resultSet.getScores();
        short[] occurences = resultSet.getOccurrences();
        numberOfRetrievedDocuments = 0;
        int[][] pointers;
        int numOfTermModifiers = termModifiers.size();
        int numOfDocModifiers = documentModifiers.size();
        wmodel.setNumberOfTokens((double) collectionStatistics.getNumberOfTokens());
        wmodel.setNumberOfDocuments((double) collectionStatistics.getNumberOfDocuments());
        wmodel.setAverageDocumentLength((double) collectionStatistics.getAverageDocumentLength());
        wmodel.setNumberOfUniqueTerms((double) collectionStatistics.getNumberOfUniqueTerms());
        wmodel.setNumberOfPointers((double) collectionStatistics.getNumberOfPointers());
        final int queryLength = queryTermStrings.length;
        for (int i = 0; i < queryLength; i++) {
            LexiconEntry lEntry = lexicon.getLexiconEntry(queryTermStrings[i]);
            if (lEntry == null) {
                logger.info("Term Not Found: " + queryTermStrings[i]);
                continue;
            }
            queryTerms.setTermProperty(queryTermStrings[i], lEntry.termId);
            wmodel.setKeyFrequency(queryTerms.getTermWeight(queryTermStrings[i]));
            wmodel.setDocumentFrequency((double) lEntry.n_t);
            wmodel.setTermFrequency((double) lEntry.TF);
            logger.debug((i + 1) + ": " + queryTermStrings[i].trim() + " with " + lEntry.n_t + " documents (TF is " + lEntry.TF + ").");
            if (IGNORE_LOW_IDF_TERMS && docIndex.getNumberOfDocuments() < lEntry.TF) {
                logger.debug("query term " + queryTermStrings[i] + " has low idf - ignored from scoring.");
                continue;
            }
            pointers = invertedIndex.getDocuments(lEntry);
            final int numberOfPointers = pointers[0].length;
            final double[] termScores = new double[numberOfPointers];
            assignScores(termScores, pointers, lEntry, queryTerms.getTermWeight(queryTermStrings[i]));
            for (int t = 0; t < numOfTermModifiers; t++) termModifiers.get(t).modifyScores(termScores, pointers);
            tsms = queryTerms.getTermScoreModifiers(queryTermStrings[i]);
            if (tsms != null) {
                for (int t = 0; t < tsms.length; t++) if (tsms[t] != null) tsms[t].modifyScores(termScores, pointers);
            }
            short mask = 0;
            if (i < 16) mask = (short) (1 << i);
            int docid;
            final int[] pointers0 = pointers[0];
            for (int k = 0; k < numberOfPointers; k++) {
                docid = pointers0[k];
                if ((scores[docid] == 0.0d) && (termScores[k] > 0.0d)) {
                    numberOfRetrievedDocuments++;
                } else if ((scores[docid] > 0.0d) && (termScores[k] < 0.0d)) {
                    numberOfRetrievedDocuments--;
                }
                scores[docid] += termScores[k];
                occurences[docid] |= mask;
            }
        }
        logger.debug("Number of docs with +ve score: " + numberOfRetrievedDocuments);
        int set_size = Math.min(RETRIEVED_SET_SIZE, numberOfRetrievedDocuments);
        if (set_size == 0) set_size = numberOfRetrievedDocuments;
        resultSet.setExactResultSize(numberOfRetrievedDocuments);
        resultSet.setResultSize(set_size);
        HeapSort.descendingHeapSort(scores, docids, occurences, set_size);
        for (int t = NumberOfQueryDSMs - 1; t >= 0; t--) {
            if (dsms[t].modifyScores(index, queryTerms, resultSet)) HeapSort.descendingHeapSort(scores, docids, occurences, resultSet.getResultSize());
        }
        for (int t = 0; t < numOfDocModifiers; t++) {
            if (documentModifiers.get(t).modifyScores(index, queryTerms, resultSet)) HeapSort.descendingHeapSort(scores, docids, occurences, resultSet.getResultSize());
        }
        logger.debug("number of retrieved documents: " + resultSet.getResultSize());
    }

    /** Slight overhead on an aditional method call layer, but makes easier to override behaviour in child classes 
      * Calls assignScores(double[], double[]) by default. */
    protected void assignScores(final double[] scores, final int[][] pointers, LexiconEntry lEntry, double queryTermWeight) {
        assignScores(scores, pointers);
    }

    /**
	 * Assigns scores to documents for a particular term.
	 * @param scores double[] the scores of the documents for the query term.
	 * @param pointers int[][] the pointers read from the inverted file
	 *        for a particular query term.
	 */
    protected void assignScores(final double[] scores, final int[][] pointers) {
        final int[] pointers1 = pointers[0];
        final int[] pointers2 = pointers[1];
        final int numOfPointers = pointers1.length;
        double score;
        if (FREQUENCY_UPPER_THRESHOLD > 0) for (int j = 0; j < numOfPointers; j++) pointers2[j] = pointers2[j] > FREQUENCY_UPPER_THRESHOLD ? FREQUENCY_UPPER_THRESHOLD : pointers2[j];
        for (int j = 0; j < numOfPointers; j++) {
            if ((score = wmodel.score(pointers2[j], docIndex.getDocumentLength(pointers1[j]))) > 0) scores[j] = score;
        }
    }
}
