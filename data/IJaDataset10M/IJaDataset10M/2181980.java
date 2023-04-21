package com.idna.batchid.service.reporting.accumulators;

import java.util.HashMap;
import java.util.Map;
import com.idna.batchid.service.reporting.accumulators.BatchIdSummaryStatus;

/**
 * A class to hold the count of various record request/response states for a 
 * single service/product. 
 * 
 * @author gawain.hammond
 *
 */
public class StatusSummaryData {

    private Map<BatchIdSummaryStatus, Integer> serviceSummaryMap = new HashMap<BatchIdSummaryStatus, Integer>();

    public StatusSummaryData() {
        for (BatchIdSummaryStatus status : BatchIdSummaryStatus.getValidProductSates()) {
            serviceSummaryMap.put(status, new Integer(0));
        }
    }

    /**
	 * <p>
	 * <table border=1 cellspacing=0>
	 * <tr>
	 * <td>{@link BatchIdSummaryStatus#PROCESSED PROCESSED}</td><td>0</td>
	 * </tr>
	 * <tr>
	 * <td>{@link BatchIdSummaryStatus#NOT_PROCESSED NOT_PROCESSED}</td><td>0</td>
	 * </tr>
	 * <tr>
	 * <td>{@link BatchIdSummaryStatus#BAD_DATA BAD_DATA}</td><td>0</td>
	 * </tr>
	 * <tr>
	 * <td>{@link BatchIdSummaryStatus#DUPLICATE DUPLICATE}</td><td>0</td>
	 * </tr>
	 * </table>
	 * @return
	 */
    public static StatusSummaryData createTotalsSummary() {
        StatusSummaryData s = new StatusSummaryData();
        for (BatchIdSummaryStatus status : BatchIdSummaryStatus.values()) {
            s.serviceSummaryMap.put(status, new Integer(0));
        }
        return s;
    }

    /**
	 * Return the summary information related to the specified status e.g. 
	 * <p>
	 * <table border=1 cellspacing=0>
	 * <tr>
	 * <th>status</th><th>value to be obtained</th>
	 * </tr>
	 * <tr>
	 * <td>PROCESSED</td><td>1000</td>
	 * </tr>
	 * <tr>
	 * <td>NOT_PROCESSED</td><td>10</td>
	 * </tr>
	 * </table>
	 * 
	 * @param status
	 * @return
	 */
    public Integer getSummaryForStatus(BatchIdSummaryStatus status) {
        return serviceSummaryMap.get(status);
    }

    /**
	 * Increment the value of the giving status e.g.
	 * <p>
	 * <table border=1 cellspacing=0>
	 * <tr>
	 * <th>status</th><th>value to be incremented</th>
	 * </tr>
	 * <tr>
	 * <td>PROCESSED</td><td>1000</td>
	 * </tr>
	 * <tr>
	 * <td>NOT_PROCESSED</td><td>10</td>
	 * </tr>
	 * </table>
	 * 
	 * @param status
	 */
    public void incrementStatus(BatchIdSummaryStatus status) {
        Integer i = serviceSummaryMap.get(status);
        i++;
        serviceSummaryMap.put(status, i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SummaryData@");
        sb.append(System.identityHashCode(this));
        sb.append(serviceSummaryMap);
        return sb.toString();
    }
}
