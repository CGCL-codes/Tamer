package reconcile.drivers;

import static reconcile.Driver.endStage;
import static reconcile.Driver.startStage;
import java.io.IOException;
import org.apache.commons.configuration.ConfigurationException;
import reconcile.FeatureVectorGenerator;
import reconcile.Preprocessor;
import reconcile.SystemConfig;
import reconcile.data.Corpus;
import reconcile.data.Document;

/**
 * @author David Buttler
 * 
 */
public class FeatureGenerator {

    private SystemConfig cfg;

    public static void main(String[] args) {
        try {
            String corpusFile = args[0];
            SystemConfig systemConfig = DriverUtils.configure(args);
            FeatureGenerator trainer = new FeatureGenerator(systemConfig);
            Corpus c = DriverUtils.loadFiles(corpusFile);
            Preprocessor preprocessor = new Preprocessor(systemConfig);
            preprocessor.preprocess(c, false);
            String featureSetName = trainer.generateFeatures(c, false);
            System.out.println("generated features in feature set: " + featureSetName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FeatureGenerator(SystemConfig cfg) {
        this.cfg = cfg;
    }

    public String generateFeatures(Corpus corpus, boolean training) throws IOException {
        return generateFeatures(corpus, training, -1);
    }

    public String generateFeatures(Document doc) throws IOException {
        FeatureVectorGenerator.makeFeatures(doc);
        return cfg.getFeatSetName();
    }

    public String generateFeatures(Corpus corpus, boolean training, int corpusSize) throws IOException {
        boolean generateFeatures = cfg.getGenerateFeatures();
        String featSetName = cfg.getFeatSetName();
        if (generateFeatures) {
            long time = startStage("feature vector generation", "Total of " + corpusSize + " files");
            if (featSetName == null) throw new RuntimeException("Feature set name needs to be specified (parameter FEAT_SET_NAME)");
            FeatureVectorGenerator.makeFeatures(corpus, training);
            endStage("feature vector generation", time);
        }
        return featSetName;
    }
}
