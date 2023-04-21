package at.ac.tuwien.ifs.alviz.config.data;

import java.util.LinkedList;
import java.util.List;

public class AlVizConfigBean {

    protected List<AlignmentAlgorithmBean> alignAlgorithmList = new LinkedList<AlignmentAlgorithmBean>();

    public AlignmentAlgorithmBean[] getAlignmentAlgorithm() {
        return this.alignAlgorithmList.toArray(new AlignmentAlgorithmBean[0]);
    }

    public void setAlignmentAlgorithm(AlignmentAlgorithmBean[] alignAlgorithmList) {
        this.alignAlgorithmList.clear();
        for (AlignmentAlgorithmBean aab : alignAlgorithmList) {
            this.alignAlgorithmList.add(aab);
        }
    }

    public List<AlignmentAlgorithmBean> getAlignmentAlgorithmList() {
        return this.alignAlgorithmList;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<AlVizConfig>\n");
        if (this.alignAlgorithmList != null) {
            for (AlignmentAlgorithmBean aab : alignAlgorithmList) {
                ret.append(aab.toString());
            }
        }
        ret.append("</AlVizConfig>");
        return ret.toString();
    }
}
