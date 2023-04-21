package benchmarkeditor.core;

import java.util.Comparator;
import benchmarkeditor.util.*;

public class BenchmarkFieldStructuralComparator implements Comparator<BenchmarkField> {

    public int compare(BenchmarkField arg0, BenchmarkField arg1) {
        if (arg0 == null) if (arg1 == null) return 0; else return 1; else if (arg1 == null) return -1;
        return DomUtilities.compareNodePositions(arg0.getXmlNode(), arg1.getXmlNode());
    }
}
