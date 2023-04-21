package uk.ac.shef.wit.textractor.debug;

import org.apache.log4j.Logger;
import uk.ac.shef.wit.textractor.JATRException;
import uk.ac.shef.wit.textractor.core.algorithm.*;
import uk.ac.shef.wit.textractor.core.feature.*;
import uk.ac.shef.wit.textractor.core.npextractor.CandidateTermExtractor;
import uk.ac.shef.wit.textractor.core.npextractor.NounPhraseExtractorOpenNLP;
import uk.ac.shef.wit.textractor.core.npextractor.WordExtractor;
import uk.ac.shef.wit.textractor.io.ResultWriter2File;
import uk.ac.shef.wit.textractor.model.CorpusImpl;
import uk.ac.shef.wit.textractor.model.Term;
import uk.ac.shef.wit.textractor.util.control.Lemmatiser;
import uk.ac.shef.wit.textractor.util.control.StopList;
import uk.ac.shef.wit.textractor.util.counter.TermFreqCounter;
import uk.ac.shef.wit.textractor.util.counter.WordCounter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestWeirdness {

    private Map<Algorithm, AbstractFeatureWrapper> _algregistry = new HashMap<Algorithm, AbstractFeatureWrapper>();

    private static Logger _logger = Logger.getLogger(AlgorithmTester.class);

    public void registerAlgorithm(Algorithm a, AbstractFeatureWrapper f) {
        _algregistry.put(a, f);
    }

    public void execute(GlobalResourceIndex index) throws JATRException, IOException {
        _logger.info("Initializing outputter, loading NP mappings...");
        ResultWriter2File writer = new ResultWriter2File(index);
        if (_algregistry.size() == 0) throw new JATRException("No algorithm registered!");
        _logger.info("Running NP recognition...");
        for (Map.Entry<Algorithm, AbstractFeatureWrapper> en : _algregistry.entrySet()) {
            _logger.info("Running feature store builder and ATR..." + en.getKey().toString());
            Term[] result = en.getKey().execute(en.getValue());
            writer.output(result, en.getKey().toString() + ".txt");
        }
    }

    public static void main(String[] args) throws IOException, JATRException {
        if (args.length < 2) {
            System.out.println("Usage: java TestWeirdness [path_to_corpus] [path_to_reference_corpus_stats]");
        } else {
            System.out.println("Started " + TestWeirdness.class + "at: " + new Date() + "... For detailed progress see log file jatr.log.");
            StopList stop = new StopList(true);
            Lemmatiser lemmatizer = new Lemmatiser();
            CandidateTermExtractor npextractor = new NounPhraseExtractorOpenNLP(stop, lemmatizer);
            CandidateTermExtractor wordextractor = new WordExtractor(stop, lemmatizer);
            TermFreqCounter npcounter = new TermFreqCounter();
            WordCounter wordcounter = new WordCounter();
            GlobalResourceIndexBuilder builder = new GlobalResourceIndexBuilder();
            GlobalResourceIndex termDocIndex = builder.build(new CorpusImpl(args[0]), npextractor);
            GlobalResourceIndex wordDocIndex = builder.build(new CorpusImpl(args[0]), wordextractor);
            FeatureCorpusTermFrequency termCorpusFreq = new FeatureBuilderCorpusTermFrequency(npcounter, wordcounter, lemmatizer).build(termDocIndex);
            FeatureCorpusTermFrequency wordFreq = new FeatureBuilderCorpusTermFrequency(npcounter, wordcounter, lemmatizer).build(wordDocIndex);
            FeatureRefCorpusTermFrequency bncRef = new FeatureBuilderRefCorpusTermFrequency(args[1]).build(null);
            AlgorithmTester tester = new AlgorithmTester();
            tester.registerAlgorithm(new WeirdnessAlgorithm(), new WeirdnessFeatureWrapper(wordFreq, termCorpusFreq, bncRef));
            tester.execute(termDocIndex);
            System.out.println("Ended at: " + new Date());
        }
    }
}
