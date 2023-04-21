package org.sulweb.infumon.common.session;

import org.sulweb.infumon.common.db.EventPointDetailRow;

/**
 *
 * @author lucio
 */
public interface RowsFetcher {

    EventPointDetailRow[] fetch(long min, long max);
}
