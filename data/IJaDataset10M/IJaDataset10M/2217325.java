package uima.taes.interestingness;

import uima.types.StructuralFeature;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

/**
 * @author pon3 Structural feature preference
 */
public class StructuralFeatureScore extends BayesianLearnedFeature {

    protected void train() {
    }

    protected double[] getFeatureVector(int docID, JCas jcas) {
        double[] feature = new double[featureSize];
        FSIndex index = jcas.getJFSIndexRepository().getAnnotationIndex(StructuralFeature.type);
        StructuralFeature sf = (StructuralFeature) index.iterator().next();
        feature[0] = sf.getAverageNumberOfCharactersPerParagraph();
        feature[1] = sf.getAverageNumberofSentencesPerParagraph();
        feature[2] = sf.getAverageNumberOfWordsPerParagraph();
        feature[3] = sf.getHasQuotedContent();
        feature[4] = sf.getNumberOfLines();
        feature[5] = sf.getNumberOfParagraphs();
        feature[6] = sf.getNumberOfSentences();
        return feature;
    }

    public String getParameters() {
        return "";
    }

    protected void initializeFeatureSize() {
        featureSize = 7;
    }
}
