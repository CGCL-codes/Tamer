package netkit.inference;

import netkit.graph.Graph;
import netkit.classifiers.Classification;
import netkit.classifiers.Estimate;

public interface InferenceMethodListener {

    public void estimate(Estimate e, int[] unknown);

    public void classify(Classification c, int[] unknown);

    public void iterate(Graph g, int[] unknown);
}
