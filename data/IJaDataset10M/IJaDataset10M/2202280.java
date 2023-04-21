package pojo;

public class PracticePrincipleValue {

    private int practiceId;

    private int principleValueId;

    private boolean coverageValue;

    public PracticePrincipleValue() {
        super();
    }

    public PracticePrincipleValue(int practiceId, int principleValueId, boolean coverageValue) {
        this.practiceId = practiceId;
        this.principleValueId = principleValueId;
        this.coverageValue = coverageValue;
    }

    public void setPracticeId(int practiceId) {
        this.practiceId = practiceId;
    }

    public int getPracticeId() {
        return practiceId;
    }

    public void setPrincipleValueId(int principleValueId) {
        this.principleValueId = principleValueId;
    }

    public int getPrincipleValueId() {
        return principleValueId;
    }

    public void setCoverageValue(boolean coverageValue) {
        this.coverageValue = coverageValue;
    }

    public boolean isCoverageValue() {
        return coverageValue;
    }
}
