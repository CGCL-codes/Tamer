package benchmarkeditor.core;

import java.util.Comparator;
import benchmarkeditor.util.*;

/**
 * Comparator for BenchmarkElement objects, ordering them by their structural position within a file
 * 
 * @author Charles Schmidt
 *
 */
public class BenchmarkElementStructureComparator implements Comparator<BenchmarkElement> {

    public static final BenchmarkElementStructureComparator INSTANCE = new BenchmarkElementStructureComparator();

    public int compare(BenchmarkElement arg0, BenchmarkElement arg1) {
        if ((arg0 == null) || arg0.isDeleted()) if ((arg1 == null) || (arg1.isDeleted())) return 0; else return 1; else if ((arg1 == null) || (arg1.isDeleted())) return -1;
        return DomUtilities.compareNodePositions(arg0.getXmlNode(), arg1.getXmlNode());
    }
}
