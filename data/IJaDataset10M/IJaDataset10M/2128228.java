package org.jcvi.glk.ctm.status.reporter;

import java.util.Map;

/**
 * <code>NullStatusReportFilter</code> is a Null Object
 * implementation of {@link StatusReportFilter}, it does not
 * filter anything.
 * @param <E> the enum for the set of statuses.
 *
 * @author jsitz
 * @author dkatzel
 */
public class NullStatusReportFilter<E extends Enum<E>> implements StatusReportFilter<E> {

    @Override
    public boolean accept(Map<E, Integer> report) {
        return true;
    }
}
