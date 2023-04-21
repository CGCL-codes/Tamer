package org.proteomecommons.MSExpedite.SignalProcessing;

/**
 *
 * @author takis
 */
public class BaselineSubtractionFilter extends AbstractSpectrumFilter {

    /** Creates a new instance of BaselineSubtractionFilter */
    public BaselineSubtractionFilter() {
    }

    public String getAlias() {
        return "MSE-Baseline-Subtraction-Filter";
    }

    public String getAlgorithmType() {
        return "Baseline Subtraction Filter";
    }

    public void apply() {
        SignalToNoiseAttr snattr = attr.getSignalToNoise();
        attrReflection = snattr;
        double sn = snattr.getRatio();
        int window = snattr.getWindowHalfSize();
        final int win = window * numOfPointsPerDalton;
        final int div = 2 * win;
        NamedAssociations centroid = ScratchPad.getNamedIndexAssociation(ScratchPad.CENTROID);
        NamedAssociations newNamedAssociations = new NamedIndexAssociations(ScratchPad.BASELINE_SUBTRACTED_INTENSITY);
        Association[] assoc = centroid.getAssociations();
        int index = 0;
        float intensity = 0;
        int offset = 0;
        final int spectrumSize = X.length;
        for (int i = 0; i < assoc.length; i++) {
            if (killRequested()) return;
            index = assoc[i].getIndex();
            intensity = 0.0f;
            if (index < win) {
                offset = 0;
            } else if ((index + win) > spectrumSize) {
                offset = index - win - Math.abs(spectrumSize - index - win);
            } else {
                offset = index - win;
            }
            for (int j = index - offset; j < index - offset + div; j++) {
                intensity += Y[j];
            }
            newNamedAssociations.add(new IndexAssociation(index, new Float(intensity / div)));
        }
        ScratchPad.add(newNamedAssociations);
        set(newNamedAssociations);
    }
}
