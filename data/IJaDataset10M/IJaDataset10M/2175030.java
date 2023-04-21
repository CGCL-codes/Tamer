package guineu.modules.mylly.filter.SimilarityFilter;

import guineu.data.PeakListRow;
import guineu.data.impl.peaklists.SimplePeakListRowGCGC;
import guineu.data.impl.datasets.SimpleGCGCDataset;
import guineu.modules.mylly.filter.NameFilter.AlignmentRowFilter;
import guineu.modules.mylly.datastruct.GCGCDatum;
import java.util.ArrayList;
import java.util.List;

public class Similarity {

    public static final String MAX_SIMILARITY = "maximum similarity";

    public static final String MEAN_SIMILARITY = "mean similarity";

    public static final String REMOVE = "Remove";

    public static final String RENAME = "Rename";

    private double minValue;

    private String mode;

    private String action;

    public Similarity(double minValue, String action, String mode) {
        this.minValue = minValue;
        this.mode = mode;
        this.action = action;
    }

    protected SimpleGCGCDataset actualMap(SimpleGCGCDataset input) {
        List<SimplePeakListRowGCGC> QuantMassOnes = input.getQuantMassAlignments();
        AlignmentRowFilter filterQuantMass = new AlignmentRowFilter(QuantMassOnes);
        SimpleGCGCDataset datasetNoMass = filterQuantMass.actualMap(input);
        List<SimplePeakListRowGCGC> als = new ArrayList<SimplePeakListRowGCGC>();
        for (PeakListRow row : datasetNoMass.getAlignment()) {
            SimplePeakListRowGCGC newRow = (SimplePeakListRowGCGC) row.clone();
            double curVal = 0.0;
            if (MAX_SIMILARITY.equals(mode)) {
                curVal = newRow.getMaxSimilarity();
            } else if (MEAN_SIMILARITY.equals(mode)) {
                curVal = newRow.getMeanSimilarity();
            }
            if (REMOVE.equals(action)) {
                if (curVal >= minValue) {
                    als.add(newRow);
                }
            } else if (RENAME.equals(action)) {
                if (curVal < minValue) {
                    newRow.setName(GCGCDatum.UNKOWN_NAME);
                    newRow.setAllNames("");
                }
                als.add(newRow);
            }
        }
        SimpleGCGCDataset filtered = new SimpleGCGCDataset(datasetNoMass.getColumnNames(), datasetNoMass.getParameters(), datasetNoMass.getAligner());
        filtered.addAll(als);
        for (SimplePeakListRowGCGC row : QuantMassOnes) {
            filtered.addAlignmentRow((SimplePeakListRowGCGC) row.clone());
        }
        return filtered;
    }

    public String getName() {
        return "Filter by similarity";
    }
}
