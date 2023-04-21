package com.qallme.framework.tools.patterns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.qallme.framework.tools.analysis.TreeTaggerQallme;
import eu.fbk.hlt.annotation.TextAnnotator.Language;
import eu.fbk.hlt.common.ConfigurationBrowser;
import eu.fbk.hlt.common.ConfigurationLoader;
import eu.fbk.hlt.common.EDITSException;
import eu.fbk.hlt.common.FileTools;
import eu.fbk.hlt.common.SerializationManager;
import eu.fbk.hlt.common.conffile.Configuration;
import eu.fbk.hlt.common.conffile.Configurations;
import eu.fbk.hlt.edits.EDITS;
import eu.fbk.hlt.edits.engines.EntailmentEngine;
import eu.fbk.hlt.edits.engines.distance.DistanceEntailmentEngine;
import eu.fbk.hlt.edits.etaf.AnnotatedText;
import eu.fbk.hlt.edits.etaf.GroupPattern;
import eu.fbk.hlt.edits.etaf.ProcessedQuestion;
import eu.fbk.hlt.edits.etaf.RelationCluster;
import eu.fbk.hlt.edits.etaf.RelationClusters;

public class PATFAQ {

    /**
	 * @param args
	 * @throws EDITSException
	 */
    public static void main(String[] args) throws Exception {
        boolean useModel = false;
        boolean useStandard = false;
        String editspath = "C:/Users/gcoro/Desktop/AnalisiSemantica/Edits/edits/";
        new EDITS(editspath);
        EDITS.system().loadPlugins();
        EDITS.system().outputStream().setVerbose(1);
        String base = "C:/Users/gcoro/Desktop/AnalisiSemantica/Edits/experiments/iwbank/";
        String data = FileTools.loadString("C:/Users/gcoro/Desktop/AnalisiSemantica/Edits/experiments/faqEnglish/faqEnglish.txt");
        StringTokenizer toker = new StringTokenizer(data, "\n", true);
        List<AnnotatedText> questions = new ArrayList<AnnotatedText>();
        String configurationFile = base + "../../iwbank/conf.xml";
        Configurations conf = SerializationManager.loadConfigurations(configurationFile);
        ConfigurationLoader loader = new ConfigurationLoader();
        loader.processConfiguration(conf);
        TreeTaggerQallme tagger = new TreeTaggerQallme();
        Configuration ttconf = ConfigurationBrowser.getConfById(conf, "tree-tagger").get(0);
        tagger.configure(ttconf);
        tagger.setLanguage(Language.ENGLISH);
        while (toker.hasMoreTokens()) {
            String token = toker.nextToken().trim();
            if (token.length() == 0) continue;
            System.out.println(token);
            ProcessedQuestion q = new ProcessedQuestion();
            q.setString(token);
            questions.add(q);
        }
        tagger.annotate(questions);
        EntailmentEngine engine = null;
        if (useModel) {
            String modelFile = base + "model";
            engine = (EntailmentEngine) SerializationManager.load(modelFile, EDITS.ENTAILMENT_ENGINE, new File(modelFile).getParentFile().getAbsolutePath() + "/");
        } else if (useStandard) {
            configurationFile = base + "../faq/conf-index-verbocean.xml";
            conf = SerializationManager.loadConfigurations(configurationFile);
            loader = new ConfigurationLoader();
            loader.processConfiguration(conf);
            engine = new DistanceEntailmentEngine();
            engine.configure(conf.getModule().get(0));
        } else {
            RelationClusters patterns = new RelationClusters();
            int i = 0;
            for (AnnotatedText question : questions) {
                RelationCluster cq = new RelationCluster();
                cq.setRelation("" + i);
                i++;
                GroupPattern pattern = new GroupPattern();
                pattern.getWord().addAll(question.getWord());
                cq.getPattern().add(pattern);
                patterns.getRelationCluster().add(cq);
            }
            String indexName = "verbocean-simple";
            Configuration confEngine = EntailmentEngineBuilder.build(editspath, patterns, base + "../faqEnglish/" + "idf-file.txt", "lemma", null, indexName);
            engine = new DistanceEntailmentEngine();
            engine.configure(confEngine);
        }
        String query = "I'd like to fix screen brightness";
        ProcessedQuestion q = new ProcessedQuestion();
        q.setString(query);
        tagger.annotate(q);
        System.out.println("******************************");
        System.out.println(query);
        System.out.println("******************************");
        for (AnnotatedText h : questions) {
            double score = engine.calculateEntailmentScore(q, h);
            System.out.println(h.getString() + " " + score);
        }
    }
}
