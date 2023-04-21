package pck_tap.Userinterface.rpe.frmRecipe.JPanels.JMaltPanel.jPanelMalt;

public class MaltTableRecord {

    String Code;

    Double ExtractMin;

    Double ExtractMax;

    Integer EfficiencyFactor;

    public MaltTableRecord() {
    }

    public MaltTableRecord(String Code, Double ExtractMin, Double ExtractMax, Integer EfficiencyFactor) {
        this.Code = Code;
        this.ExtractMin = ExtractMin;
        this.ExtractMax = ExtractMax;
        this.EfficiencyFactor = EfficiencyFactor;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getCode() {
        return Code;
    }

    public void setExtractMin(Double ExtractMin) {
        this.ExtractMin = ExtractMin;
    }

    public Double getExtractMin() {
        return ExtractMin;
    }

    public void setExtractMax(Double ExtractMax) {
        this.ExtractMax = ExtractMax;
    }

    public Double getExtractMax() {
        return ExtractMax;
    }

    public void setEfficiencyFactor(Integer EfficiencyFactor) {
        this.EfficiencyFactor = EfficiencyFactor;
    }

    public Integer getEfficiencyFactor() {
        return EfficiencyFactor;
    }
}
