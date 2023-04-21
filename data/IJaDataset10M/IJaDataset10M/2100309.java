package langnstats.project.lib;

import java.io.Serializable;
import langnstats.project.lib.crossvalidation.TrainTokens;

public interface LanguageModel extends Serializable, Cloneable {

    void train(TrainTokens trainTokens);

    void prepare(WordType[] allWordType);

    double[] predict(WordType wordType);

    String getDescription();

    LanguageModel clone();
}
