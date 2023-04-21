package sentiment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.aliasi.classify.ClassifierEvaluator;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

public class PolarityModelBuilder extends ModelBuilder {

    public PolarityModelBuilder() {
        super();
    }

    protected static String[] mCategories = new String[] { "POSITIVE", "NEGATIVE", "NEUTRAL" };

    public static String MODEL_FILE = "resources/Polarity.model";

    public void buildModel(int interval) {
        DynamicLMClassifier mClassifier = new DynamicLMClassifier(mCategories, nGram, bounded);
        double numSentencesInTest = (double) sentences.size() / 5.0;
        for (int i = 0; i < sentences.size(); i++) {
            if (i >= interval * numSentencesInTest && i <= (interval + 1.) * numSentencesInTest) continue;
            Sentence sentence = (Sentence) sentences.get(i);
            if (sentence.isPositive()) {
                mClassifier.train(mCategories[0], sentence.getContent());
            }
            if (sentence.isNegative()) {
                mClassifier.train(mCategories[1], sentence.getContent());
            }
            if (sentence.isNeutral()) {
                mClassifier.train(mCategories[2], sentence.getContent());
            }
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(MODEL_FILE);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            mClassifier.compileTo(objOut);
            objOut.close();
            classifier = (LMClassifier) AbstractExternalizable.compile(mClassifier);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void evaluate(int interval) {
        ClassifierEvaluator evaluator = new ClassifierEvaluator(classifier, mCategories);
        double numSentencesInTest = (double) sentences.size() / 5.0;
        for (int i = 0; i < sentences.size(); i++) {
            if (i < interval * numSentencesInTest || i > (interval + 1.) * numSentencesInTest) continue;
            Sentence sentence = (Sentence) sentences.get(i);
            if (sentence.isPositive()) {
                evaluator.addCase(mCategories[0], sentence.getContent());
            }
            if (sentence.isNegative()) {
                evaluator.addCase(mCategories[1], sentence.getContent());
            }
            if (sentence.isNeutral()) {
                evaluator.addCase(mCategories[2], sentence.getContent());
            }
        }
        System.out.println(evaluator.toString());
    }

    public static void main(String args[]) {
        PolarityModelBuilder mb = new PolarityModelBuilder();
        mb.readDocumentsAndAnnotations();
        for (int i = 0; i < 5; i++) {
            mb.buildModel(i);
            mb.evaluate(i);
        }
    }
}
