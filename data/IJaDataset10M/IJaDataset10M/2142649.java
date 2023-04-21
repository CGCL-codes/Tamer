package apollo.gui.detailviewers.sequencealigner.comparators;

import java.util.Comparator;
import apollo.datamodel.SeqFeatureI;
import apollo.gui.detailviewers.sequencealigner.ReadingFrame;

public class ComparatorSeqFeatureFrame implements Comparator<SeqFeatureI> {

    public int compare(SeqFeatureI o1, SeqFeatureI o2) {
        return Integer.valueOf(ReadingFrame.valueOf(o1.getFrame()).toInt()).compareTo(Integer.valueOf(ReadingFrame.valueOf(o2.getFrame()).toInt()));
    }
}
