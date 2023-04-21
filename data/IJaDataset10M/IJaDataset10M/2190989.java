package uk.ac.ebi.pride.tools.converter.dao_crux_txt.filters;

import java.util.List;
import java.util.Map;

/**
 * All entries with xcorr rank values less than or equal to threshold will pass the filter.
 * @author Jose A. Dianes
 * @version $Id$
 */
public class XcorrRankFilterCriteria extends FilterCriteria {

    @Override
    public boolean passFilter(Map<String, Integer> header, String[] values) {
        int threshold = (Integer) (this.getThreshold());
        int rank = Integer.parseInt(values[header.get("xcorr rank")]);
        return (rank <= threshold);
    }

    @Override
    public Object getHighestScore(Map<String, Integer> header, List<String> values) {
        if ((values == null) || (values.size() == 0)) return null;
        Integer highest = Integer.MAX_VALUE;
        for (String value : values) {
            String[] columns = value.split("\t");
            highest = Math.min(highest, Integer.parseInt(columns[header.get("xcorr rank")]));
        }
        return highest;
    }
}
