package org.proteomecommons.MSExpedite.SignalProcessing;

public class NoiseFilter extends AbstractSpectrumFilter {

    public NoiseFilter() {
        super();
    }

    public synchronized void apply() {
        SignalToNoiseAttr snattr = attr.getSignalToNoise();
        attrReflection = snattr;
        double sn = snattr.getRatio();
        int window = snattr.getWindowHalfSize();
        float x, y;
        float noise[], d;
        int offset = 0;
        final int win = window * numOfPointsPerDalton;
        final int div = 2 * win;
        Integer index;
        int peakIndex;
        final float intensities[] = Y;
        Noise noiseObj = new Noise(intensities);
        Association[] assoc = namedAssociations.getAssociations();
        NamedAssociations newNamedAssociations = new NamedIndexAssociations(ScratchPad.SIGNAL_TO_NOISE);
        final int spectrumSize = X.length;
        final int size = namedAssociations.size();
        for (int i = 0; i < size; i++) {
            if (killRequested()) return;
            peakIndex = assoc[i].getIndex();
            if (peakIndex < win) {
                offset = 0;
            } else if ((peakIndex + win) > spectrumSize) {
                offset = peakIndex - win - Math.abs(spectrumSize - peakIndex - win);
            } else {
                offset = peakIndex - win;
            }
            noise = null;
            try {
                noise = noiseObj.calculate(offset, div);
            } catch (UnableToConvergeException ex) {
                ex.printStackTrace();
            }
            d = Noise.getSignalToNoise(Y[peakIndex], (double) noise[0], (double) noise[1]);
            Float snObj = new Float(d);
            if (d >= sn && !Float.isInfinite(d)) {
                newNamedAssociations.add(new IndexAssociation(peakIndex, snObj));
            } else if (isDebugged()) {
                if (failedAssociations == null) failedAssociations = new NamedIndexAssociations("FAILED SN");
                failedAssociations.add(new IndexAssociation(peakIndex, snObj));
            }
        }
        set(newNamedAssociations);
        ScratchPad.add(newNamedAssociations);
    }

    public String getAlias() {
        return "MSE-Noise Filter";
    }

    public String getAlgorithmType() {
        return "Noise Filter";
    }
}
