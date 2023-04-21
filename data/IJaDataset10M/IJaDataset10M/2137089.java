package itjava.model;

public class LabelData {

    public String _labelName;

    public String _labelValue;

    public LabelData(String lblName, String restOfLine) {
        _labelName = lblName;
        _labelValue = restOfLine;
    }
}
