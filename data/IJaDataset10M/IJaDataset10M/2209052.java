package org.verus.ngl.sl.bprocess.circulation;

/**
 *
 * @author root
 */
public interface Cir_Transaction_Fine {

    public int bm_saveCheckInTransactionFine(Integer libraryId, int ta_id, String overduepaid, String databaseId);
}
