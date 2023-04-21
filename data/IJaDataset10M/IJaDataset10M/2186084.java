package at.ac.tuwien.ifs.alviz.align.data;

public class SimilarityInfoBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    protected String correctvalue = null;

    protected ConfidenceBean confidence = null;

    public String getCorrectvalue() {
        return this.correctvalue;
    }

    public void setCorrectvalue(String correctvalue) {
        this.correctvalue = correctvalue;
    }

    public ConfidenceBean getConfidence() {
        return this.confidence;
    }

    public void setConfidence(ConfidenceBean confidence) {
        this.confidence = confidence;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer("        <SimilarityInfo");
        if (this.correctvalue != null) ret.append(" Correctvalue=\"" + this.correctvalue + "\"");
        ret.append(">\n");
        if (this.confidence != null) {
            ret.append(this.confidence.toString());
        }
        ret.append("        </SimilarityInfo>\n");
        return ret.toString();
    }

    public String toXML() {
        return this.toString();
    }
}
