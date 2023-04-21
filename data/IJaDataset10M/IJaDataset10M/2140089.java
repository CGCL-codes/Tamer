package musite.prediction.feature;

import java.io.Serializable;
import java.util.List;
import musite.prediction.feature.knn.SimilarityMatrix;
import musite.prediction.feature.knn.MatrixUtils;
import musite.util.ProteinSequenceUtil;

/**
 *
 * @author LucasYao
 */
public class AminoAcidSubmatrixFeatureExtractor implements FeatureExtractor, Serializable {

    private static final long serialVersionUID = -6507052020284269322L;

    private static class SerializationProxy implements Serializable {

        private static final long serialVersionUID = 5058130718254945376L;

        private String aminoAcidAlphabet;

        private final int windowOffset;

        private final SimilarityMatrix matrix;

        SerializationProxy(AminoAcidSubmatrixFeatureExtractor actualObj) {
            this.aminoAcidAlphabet = actualObj.aminoAcidAlphabet;
            this.windowOffset = actualObj.windowOffset;
            this.matrix = actualObj.matrix;
        }

        private Object readResolve() {
            return new AminoAcidSubmatrixFeatureExtractor(windowOffset, matrix, aminoAcidAlphabet);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.InvalidObjectException {
        throw new java.io.InvalidObjectException("Proxy required");
    }

    private String aminoAcidAlphabet;

    private final int windowOffset;

    private final SimilarityMatrix matrix;

    public AminoAcidSubmatrixFeatureExtractor(final int windowOffset, final SimilarityMatrix matrix) {
        this(windowOffset, matrix, null);
    }

    public AminoAcidSubmatrixFeatureExtractor(final int windowOffset, final SimilarityMatrix matrix, final String aminoAcidAlphabet) {
        if (windowOffset < 0) {
            throw new IllegalArgumentException("Window size cannot be negative.");
        }
        if (aminoAcidAlphabet == null) {
            this.aminoAcidAlphabet = ProteinSequenceUtil.ALPHABET + "*";
        } else {
            this.aminoAcidAlphabet = aminoAcidAlphabet;
        }
        this.windowOffset = windowOffset;
        this.matrix = matrix;
    }

    public void setAminoAcidAlphabet(String aminoAcidAlphabet) {
        if (aminoAcidAlphabet == null) {
            throw new NullPointerException();
        }
        this.aminoAcidAlphabet = aminoAcidAlphabet;
    }

    /**
     * Extract features from protein at site and save to instance
     * @param protein
     * @param site
     * @param savetoInstance
     */
    public List<Double> extract(final Instance instance, final boolean save) {
        if (instance == null) {
            throw new NullPointerException();
        }
        double[][] asciimatrix = MatrixUtils.reindexByASCii(matrix);
        String sequence = InstanceUtil.extractSurroundingSequence(instance, windowOffset, true);
        if (sequence == null) return null;
        StringBuilder sb = new StringBuilder(sequence);
        sb.deleteCharAt(windowOffset);
        List<Double> features = ProteinSequenceUtil.aminoAcidSubmatrix(sb.toString(), aminoAcidAlphabet, asciimatrix);
        if (save) {
            instance.putFeatures(featureTag(), features);
        }
        return features;
    }

    public static final String TAG = "aasubmatrix";

    /**
     *
     * @return
     */
    public String featureTag() {
        return TAG;
    }
}
