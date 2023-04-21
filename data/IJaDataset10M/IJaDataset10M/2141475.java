package model;

/**
 *
 * @author ricardol
 */
public class RelevantSegment {

    private Segment segment;

    private Indicator indicator;

    public RelevantSegment(Indicator indicator, Segment segment) {
        this.segment = segment;
        this.indicator = indicator;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }
}
