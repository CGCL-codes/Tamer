package gleam.docservice;

/**
 * Base class for inter-annotator agreement results. This class holds a
 * simple single-number result for an IAA calculation,
 * algorithm-specific subclasses contain the detailed results.
 */
public class IAAResult {

    /**
   * A single-number agreement score for this IAA calculation.
   */
    private float agreement;

    /**
   * The set of label values used in this calculation. May be null if
   * the calculation did not make use of labels.
   */
    private String[] labelValues;

    private IAADetail detail;

    public float getAgreement() {
        return agreement;
    }

    public void setAgreement(float agreement) {
        this.agreement = agreement;
    }

    public String[] getLabelValues() {
        return labelValues;
    }

    public void setLabelValues(String[] labelValues) {
        this.labelValues = labelValues;
    }

    public IAADetail getDetail() {
        return detail;
    }

    public void setDetail(IAADetail detail) {
        this.detail = detail;
    }

    /**
   * Construct an IAA result object with the given agreement score.
   */
    public IAAResult(float agreement, String[] labelValues, IAADetail detail) {
        this.agreement = agreement;
        this.labelValues = labelValues;
        this.detail = detail;
    }

    /**
   * No argument constructor to satisfy stub generator.
   */
    public IAAResult() {
        this(0, null, null);
    }
}
