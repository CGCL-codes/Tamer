package diet.utils.stringsimilarity;

public interface SimilarityMeasure<X> {

    double similarity(X a, X b);
}
