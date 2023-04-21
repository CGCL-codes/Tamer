package jofc2.model.axis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jofc2.OFC;

public class YAxisLabels extends Label {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6134375829177947590L;

    private Integer steps;

    private List<Object> labels;

    public YAxisLabels() {
    }

    public YAxisLabels(String... labels) {
        addLabels(labels);
    }

    public YAxisLabels(List<String> labels) {
        addLabels(OFC.toArray(labels, String.class));
    }

    public List<Object> getLabels() {
        return labels;
    }

    public YAxisLabels addLabels(String... labels) {
        checkLabels();
        this.labels.addAll(Arrays.asList(labels));
        return this;
    }

    public YAxisLabels addLabels(Label... labels) {
        checkLabels();
        this.labels.addAll(Arrays.asList(labels));
        return this;
    }

    public YAxisLabels addLabels(List<Label> labels) {
        checkLabels();
        this.labels.addAll(labels);
        return this;
    }

    public YAxisLabels setSteps(Integer steps) {
        this.steps = steps;
        return this;
    }

    public Integer getSteps() {
        return steps;
    }

    private synchronized void checkLabels() {
        if (labels == null) labels = new ArrayList<Object>();
    }
}
