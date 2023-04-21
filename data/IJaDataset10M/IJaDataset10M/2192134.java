package elka.wedt.classificator.algorithms;

import java.util.HashSet;
import java.util.Set;
import elka.wedt.classificator.model.document.Document;
import elka.wedt.classificator.model.document.NGram;

public class EuclideanDistance implements IDistanceAlgorithm {

    @Override
    public double computeDistance(Document testingDocument, Document trainingDocument) {
        Set<NGram> nGrams = new HashSet<NGram>();
        nGrams.addAll(testingDocument.getNGramSet());
        nGrams.addAll(trainingDocument.getNGramSet());
        double result = 0.0;
        for (NGram ngram : nGrams) {
            if (trainingDocument.getVectorWithWeights().containsKey(ngram)) {
                if (testingDocument.getVectorWithWeights().containsKey(ngram)) {
                    result += Math.pow(trainingDocument.getVectorWithWeights().get(ngram) - testingDocument.getVectorWithWeights().get(ngram), 2);
                } else {
                    result += Math.pow(trainingDocument.getVectorWithWeights().get(ngram), 2);
                }
            } else {
                result += Math.pow(testingDocument.getVectorWithWeights().get(ngram), 2);
            }
        }
        return Math.sqrt(result);
    }
}
