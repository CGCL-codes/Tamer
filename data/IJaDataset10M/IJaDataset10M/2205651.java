package cc.mallet.types;

public class PerLabelFeatureCounts {

    Alphabet dataAlphabet, targetAlphabet;

    FeatureCounts[] fc;

    static boolean countInstances = true;

    private static double[][] calcFeatureCounts(InstanceList ilist) {
        int numClasses = ilist.getTargetAlphabet().size();
        int numFeatures = ilist.getDataAlphabet().size();
        double[][] featureCounts = new double[numClasses][numFeatures];
        for (int i = 0; i < ilist.size(); i++) {
            Instance inst = ilist.get(i);
            if (!(inst.getData() instanceof FeatureVector)) throw new IllegalArgumentException("Currently only handles FeatureVector data");
            FeatureVector fv = (FeatureVector) inst.getData();
            int labelIndex = inst.getLabeling().getBestIndex();
            int fli;
            for (int fl = 0; fl < fv.numLocations(); fl++) {
                fli = fv.indexAtLocation(fl);
                if (countInstances) featureCounts[labelIndex][fli]++; else featureCounts[labelIndex][fli] += fv.valueAtLocation(fl);
            }
        }
        return featureCounts;
    }

    public PerLabelFeatureCounts(InstanceList ilist) {
        dataAlphabet = ilist.getDataAlphabet();
        targetAlphabet = ilist.getTargetAlphabet();
        double[][] counts = calcFeatureCounts(ilist);
        fc = new FeatureCounts[targetAlphabet.size()];
        for (int i = 0; i < fc.length; i++) fc[i] = new FeatureCounts(dataAlphabet, counts[i]);
    }

    public static class Factory implements RankedFeatureVector.PerLabelFactory {

        public Factory() {
        }

        public RankedFeatureVector[] newRankedFeatureVectors(InstanceList ilist) {
            PerLabelFeatureCounts x = new PerLabelFeatureCounts(ilist);
            return x.fc;
        }
    }
}
