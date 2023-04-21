package edu.kit.aifb.terrier.concept;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.terrier.matching.models.WeightingModel;
import org.terrier.structures.CollectionStatistics;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.InvertedIndex;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import edu.kit.aifb.concept.IConceptExtractor;
import edu.kit.aifb.concept.IConceptVector;
import edu.kit.aifb.concept.TroveConceptVector;
import edu.kit.aifb.document.IDocument;
import edu.kit.aifb.nlp.ITokenAnalyzer;
import edu.kit.aifb.nlp.ITokenStream;
import edu.kit.aifb.nlp.Language;
import edu.kit.aifb.terrier.tem.ITermEstimateModel;

public class TerrierConceptModelExtractor implements IConceptExtractor {

    static final int MAX_DOC_SCORE_CACHE = 500;

    static Log logger = LogFactory.getLog(TerrierConceptModelExtractor.class);

    private Language language;

    private int maxConceptId;

    private IConceptModel conceptModel;

    private ITermEstimateModel termEstimateModel;

    private Index index;

    private CollectionStatistics collectionStatistics;

    private Lexicon<String> lexicon;

    private InvertedIndex invertedIndex;

    private DocumentIndex docIndex;

    private MetaIndex metaIndex;

    private ITokenAnalyzer tokenAnalyzer;

    private double[][] docScores;

    private double[][] docScoresCache;

    private double[] smoothingWeights;

    private short[] support;

    protected TerrierConceptModelExtractor(Index index, Language language, IConceptModel conceptModel) throws Exception {
        this.index = index;
        collectionStatistics = index.getCollectionStatistics();
        lexicon = index.getLexicon();
        invertedIndex = index.getInvertedIndex();
        docIndex = index.getDocumentIndex();
        metaIndex = index.getMetaIndex();
        this.language = language;
        maxConceptId = index.getDocumentIndex().getNumberOfDocuments();
        logger.info("Setting concept model: " + conceptModel.getClass().getName());
        this.conceptModel = conceptModel;
        conceptModel.setIndex(index);
        termEstimateModel = conceptModel.getTermEstimatModel();
        docScoresCache = new double[MAX_DOC_SCORE_CACHE][];
        support = new short[maxConceptId];
        smoothingWeights = new double[maxConceptId];
        for (int i = 0; i < maxConceptId; i++) {
            smoothingWeights[i] = termEstimateModel.getSmoothingWeight(metaIndex.getItem("docno", i));
        }
    }

    private void reset(int numberOfQueryTerms) {
        Arrays.fill(support, (short) 0);
        docScores = new double[numberOfQueryTerms][];
        for (int i = 0; i < numberOfQueryTerms; i++) {
            if (i >= MAX_DOC_SCORE_CACHE) {
                docScores[i] = new double[maxConceptId];
            } else {
                if (docScoresCache[i] == null) {
                    docScoresCache[i] = new double[maxConceptId];
                } else {
                    Arrays.fill(docScoresCache[i], 0d);
                }
                docScores[i] = docScoresCache[i];
            }
        }
    }

    public IConceptVector extract(IDocument doc) {
        return extract(doc.getName(), doc.getTokens(language));
    }

    public IConceptVector extract(IDocument doc, String... fields) {
        return extract(doc.getName(), doc.getTokens(fields));
    }

    public IConceptVector extract(String docName, ITokenStream queryTokenStream) {
        logger.info("Extracting concepts for document " + docName + ", language=" + language);
        ITokenStream ts = queryTokenStream;
        if (tokenAnalyzer != null) {
            ts = tokenAnalyzer.getAnalyzedTokenStream(ts);
        }
        Map<String, Integer> queryTermFrequencyMap = new HashMap<String, Integer>();
        Map<String, LexiconEntry> queryTermLexiconEntryMap = new HashMap<String, LexiconEntry>();
        while (ts.next()) {
            String token = ts.getToken();
            if (token == null || token.length() == 0) {
                logger.debug("Skipping empty token!");
                continue;
            }
            if (!ts.getLanguage().equals(language)) {
                logger.error("Skipping token, language=" + ts.getLanguage());
                continue;
            }
            if (queryTermFrequencyMap.containsKey(token)) {
                queryTermFrequencyMap.put(token, queryTermFrequencyMap.get(token) + 1);
            } else {
                LexiconEntry lEntry = lexicon.getLexiconEntry(token);
                if (lEntry == null) {
                    logger.info("Term not found: " + token);
                } else {
                    queryTermFrequencyMap.put(token, 1);
                    queryTermLexiconEntryMap.put(token, lEntry);
                }
            }
        }
        int numberOfQueryTerms = queryTermFrequencyMap.size();
        if (numberOfQueryTerms == 0) {
            logger.info("Empty document: " + docName);
            return new TroveConceptVector(docName, maxConceptId);
        }
        String[] queryTerms = new String[numberOfQueryTerms];
        int[] queryTermFrequencies = new int[numberOfQueryTerms];
        LexiconEntry[] lexiconEntries = new LexiconEntry[numberOfQueryTerms];
        double[] queryTermEstimates = new double[numberOfQueryTerms];
        int i = 0;
        for (String queryTerm : queryTermFrequencyMap.keySet()) {
            queryTerms[i] = queryTerm;
            queryTermFrequencies[i] = queryTermFrequencyMap.get(queryTerm);
            lexiconEntries[i] = queryTermLexiconEntryMap.get(queryTerm);
            queryTermEstimates[i] = termEstimateModel.getEstimate(queryTerm);
            i++;
        }
        reset(numberOfQueryTerms);
        WeightingModel wmodel = conceptModel.getWeightingModel();
        wmodel.setCollectionStatistics(collectionStatistics);
        wmodel.setKeyFrequency(1);
        int[][] pointers;
        int activatedConceptCount = 0;
        for (i = 0; i < numberOfQueryTerms; i++) {
            wmodel.setEntryStatistics(lexiconEntries[i]);
            wmodel.prepare();
            pointers = invertedIndex.getDocuments(lexiconEntries[i]);
            for (int j = 0; j < pointers[0].length; j++) {
                int conceptId = pointers[0][j];
                double score = 0;
                try {
                    score = wmodel.score(pointers[1][j], docIndex.getDocumentLength(conceptId));
                } catch (IOException e) {
                    logger.error(e);
                }
                if (score > 0) {
                    docScores[i][conceptId] = score;
                    if (support[conceptId] == 0) {
                        activatedConceptCount++;
                    }
                    support[conceptId]++;
                }
            }
        }
        logger.info("Matched " + activatedConceptCount + " concepts.");
        IConceptVector cv = conceptModel.getConceptVector(docName, queryTerms, queryTermFrequencies, queryTermEstimates, smoothingWeights, docScores, support);
        logger.info("Extracted concept vector with " + cv.count() + " activated concepts.");
        return cv;
    }

    public void setTokenAnalyzer(ITokenAnalyzer tokenAnalyzer) {
        this.tokenAnalyzer = tokenAnalyzer;
    }

    public Index getIndex() {
        return index;
    }
}
