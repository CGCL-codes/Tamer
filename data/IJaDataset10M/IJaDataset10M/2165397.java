package org.jcvi.glk.ctm.status.reporter;

import java.util.Map;
import org.jcvi.glk.ctm.status.FluStatusBin;

/**
 * <code>DefaultStatusReportFilter</code> is an
 * implementation of {@link StatusReportFilter} that will filter out
 * any reports where there are no published samples.
 *
 *
 * @author jsitz
 * @author dkatzel
 */
public class DefaultStatusReportFilter implements StatusReportFilter<FluStatusBin> {

    private static final Integer ZERO = Integer.valueOf(0);

    @Override
    public boolean accept(Map<FluStatusBin, Integer> report) {
        return !report.get(FluStatusBin.PUBLISHED).equals(ZERO);
    }
}
