package vademecum.classifier.uknow.out;

import vademecum.classifier.uknow.abstractions.SigEntry;
import vademecum.classifier.uknow.data.DoubleSigMatrix;
import vademecum.data.IntegerHashMap;

/**
 * 
 * Just an Encapsulation for the Result created throughout
 * UKnow
 *
 */
public class ResultBean {

    private IntegerHashMap<Integer, SigEntry[]> relevantCharIntervals;

    private IntegerHashMap<Integer, IntegerHashMap<Integer, SigEntry[]>> relevantDiffIntervals;

    private IntegerHashMap<Integer, String> labels;

    private DoubleSigMatrix CharMatrix;

    public ResultBean(IntegerHashMap<Integer, SigEntry[]> relevantCharIntervals, IntegerHashMap<Integer, IntegerHashMap<Integer, SigEntry[]>> relevantDiffIntervals, IntegerHashMap<Integer, String> labels, DoubleSigMatrix CharMatrix) {
        this.relevantCharIntervals = relevantCharIntervals;
        this.relevantDiffIntervals = relevantDiffIntervals;
        this.labels = labels;
        this.CharMatrix = CharMatrix;
    }

    public IntegerHashMap<Integer, String> getLabels() {
        return labels;
    }

    public IntegerHashMap<Integer, SigEntry[]> getRelevantCharIntervals() {
        return relevantCharIntervals;
    }

    public IntegerHashMap<Integer, IntegerHashMap<Integer, SigEntry[]>> getRelevantDiffIntervals() {
        return relevantDiffIntervals;
    }

    public DoubleSigMatrix getCharMatrix() {
        return this.CharMatrix;
    }
}
