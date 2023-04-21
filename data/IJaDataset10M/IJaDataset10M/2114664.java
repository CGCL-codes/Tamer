package br.ufrgs.inf.simevaluator.similarity.secondstring;

import br.ufrgs.inf.simevaluator.similarity.SimilarityFunction;
import com.wcohen.ss.AbstractStringDistance;
import com.wcohen.ss.MongeElkan;

public class SecondStringMongeElkan extends SimilarityFunction {

    private static final long serialVersionUID = -9054929583202391151L;

    public SecondStringMongeElkan() {
        super("SecondString", "MongeElkan");
    }

    public float getSimilarity(String query, String target) {
        AbstractStringDistance similarity = new MongeElkan();
        return (float) similarity.score(query, target);
    }
}
