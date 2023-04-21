package com.idna.batchid.service.reporting.accumulators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import com.idna.batchid.util.log.BatchIdLoggerFactoryImpl;

/**
 * 
 * A class to manage the counting of of record request/response states for the ultimate purpose of providing a summary
 * table.
 * 
 * This class uses a Map to hold a {@link StatusSummaryData} per service, and one for the totals column.
 * 
 * 
 * @author gawain.hammond
 * 
 */
public class BatchIdAggregateCounter {

    protected final Logger logger = Logger.getLogger(this.getClass().getName(), new BatchIdLoggerFactoryImpl());

    private Map<String, StatusSummaryData> serviceSummaryStats = new HashMap<String, StatusSummaryData>();

    private Map<List<String>, DatablockSummaryStatus> datablockSummaryStats = new HashMap<List<String>, DatablockSummaryStatus>();

    public BatchIdAggregateCounter() {
    }

    public BatchIdAggregateCounter(String columnName) {
        StatusSummaryData newSummary = StatusSummaryData.createTotalsSummary();
        serviceSummaryStats.put(columnName, newSummary);
    }

    public void incrementServiceStatus(String service, BatchIdSummaryStatus status) {
        StatusSummaryData summary = serviceSummaryStats.get(service);
        if (summary == null) {
            StatusSummaryData newSummary = new StatusSummaryData();
            newSummary.incrementStatus(status);
            serviceSummaryStats.put(service, newSummary);
        } else {
            summary.incrementStatus(status);
        }
    }

    public void incrementDatablockServiceStatus(String service, String datablock, Map<String, String> serviceResponseValues) {
        String datablockType = serviceResponseValues.get(datablock + "-Type");
        incrementDatablockStatus(BatchIdSummaryAccumulator.TOTAL_SUMMARY, datablock, datablockType);
        incrementDatablockStatus(service, datablock, datablockType);
    }

    private void incrementDatablockStatus(String columnName, String datablockName, String subDatablockType) {
        synchronized (datablockSummaryStats) {
            String mainDatablockType = datablockName;
            List<String> key = getKey(columnName, datablockName);
            DatablockSummaryStatus datablockTypeStatus = datablockSummaryStats.get(key);
            if (datablockTypeStatus == null) {
                datablockTypeStatus = new DatablockSummaryStatus(datablockName, columnName);
            }
            datablockTypeStatus.incrementServiceTypeCount(subDatablockType);
            datablockTypeStatus.incrementServiceTypeCount(mainDatablockType);
            datablockSummaryStats.put(key, datablockTypeStatus);
        }
    }

    /**
	 * Output array should look like:
	 * <p>
	 * <table border=1 cellspacing=0>
	 * <tr>
	 * <th>Result</th>
	 * <th>Totals</th>
	 * <th>CheckID(LIVE)</th>
	 * <th>ProveID(Live)</th>
	 * </tr>
	 * <tr>
	 * <td>{@link BatchIdSummaryStatus#PROCESSED PROCESSED}</td>
	 * <td>3720</td>
	 * <td>1860</td>
	 * <td>1860</td>
	 * </tr>
	 * <tr>
	 * <td>{@link BatchIdSummaryStatus#NOT_PROCESSED NOT_PROCESSED}</td>
	 * <td>2</td>
	 * <td>0</td>
	 * <td>2</td>
	 * </tr>
	 * <tr>
	 * <td>{@link BatchIdSummaryStatus#BAD_DATA BAD_DATA}</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>{@link BatchIdSummaryStatus#DUPLICATE DUPLICATE}</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>{@link DatablockName#CreditReference CreditReference}</td>
	 * <td>1860</td>
	 * <td>.</td>
	 * <td>.</td>
	 * </tr>
	 * <tr>
	 * <td>{@link DatablockName#CreditReference CreditReference}-Result</td>
	 * <td>1700</td>
	 * <td>0</td>
	 * <td>1700</td>
	 * </tr>
	 * <tr>
	 * <td>{@link DatablockName#CreditReference CreditReference}-NoMatch</td>
	 * <td>50</td>
	 * <td>0</td>
	 * <td>50</td>
	 * </tr>
	 * <tr>
	 * <td>{@link DatablockName#CreditReference CreditReference}-Error</td>
	 * <td>10</td>
	 * <td>0</td>
	 * <td>10</td>
	 * </tr>
	 * <tr>
	 * <td>{@link DatablockName#CreditReference CreditReference}-AddressPicklist</td>
	 * <td>100</td>
	 * <td>0</td>
	 * <td>100</td>
	 * </tr>
	 * </table>
	 * 
	 * @return A two dimensional array rendering data as above
	 */
    public String[][] get2DResultsArray() {
        synchronized (datablockSummaryStats) {
            BatchIdSummaryStatus[] states = BatchIdSummaryStatus.values();
            logger.debug("datablockSummaryStats size: " + datablockSummaryStats.size());
            String[][] results = new String[100][1 + serviceSummaryStats.size()];
            results[0][0] = "Result";
            int rowCounter = 1;
            for (BatchIdSummaryStatus state : states) {
                results[rowCounter][0] = state.toString();
                rowCounter++;
            }
            Iterator<Entry<List<String>, DatablockSummaryStatus>> it = datablockSummaryStats.entrySet().iterator();
            Set<String> rowHeadersInThisColumn = new HashSet<String>();
            while (it.hasNext()) {
                DatablockSummaryStatus datablockSummaryStatus = it.next().getValue();
                for (String datablockType : datablockSummaryStatus.getDatablockTypes()) {
                    String datablockName = datablockSummaryStatus.getDatablockName();
                    String rowHeader = datablockType.equals(datablockName) ? datablockType : datablockName + "-" + datablockType;
                    rowHeadersInThisColumn.add(rowHeader);
                }
            }
            List<String> datablockRowHeaders = new ArrayList<String>(rowHeadersInThisColumn);
            Collections.sort(datablockRowHeaders);
            String lastDatablock = null;
            List<String> datablockRowHeadersWithInsertedLines = new ArrayList<String>(datablockRowHeaders);
            for (String rowHeader : datablockRowHeaders) {
                String currentDatablock = rowHeader.split("-")[0];
                if (!currentDatablock.equalsIgnoreCase(lastDatablock)) {
                    results[rowCounter][0] = "";
                    datablockRowHeadersWithInsertedLines.add(datablockRowHeadersWithInsertedLines.indexOf(currentDatablock), "");
                    rowCounter++;
                }
                results[rowCounter][0] = rowHeader;
                rowCounter++;
                lastDatablock = currentDatablock;
            }
            int serviceColumnCounter = 1;
            for (String servicename : serviceSummaryStats.keySet()) {
                results[0][serviceColumnCounter] = servicename;
                StatusSummaryData summary = serviceSummaryStats.get(servicename);
                int serviceRowCounter = 1;
                for (BatchIdSummaryStatus state : states) {
                    Integer count = summary.getSummaryForStatus(state);
                    if (count != null) {
                        results[serviceRowCounter][serviceColumnCounter] = count.toString();
                    }
                    serviceRowCounter++;
                }
                for (String rowHeader : datablockRowHeadersWithInsertedLines) {
                    String datablockName = rowHeader.split("-")[0];
                    String datablockType = rowHeader.contains("-") ? rowHeader.split("-")[1] : rowHeader;
                    List<String> key = getKey(servicename, datablockName);
                    DatablockSummaryStatus status = datablockSummaryStats.get(key);
                    if (status != null) {
                        int count = status.getServiceTypeCount(servicename, datablockType);
                        results[serviceRowCounter][serviceColumnCounter] = count >= 0 ? String.valueOf(count) : "";
                    } else {
                        results[serviceRowCounter][serviceColumnCounter] = "";
                    }
                    serviceRowCounter++;
                }
                serviceColumnCounter++;
            }
            return trimArray(results, rowCounter, serviceColumnCounter);
        }
    }

    private String[][] trimArray(String[][] results, int rowCounter, int serviceColumnCounter) {
        String[][] anotherArray = new String[rowCounter + 1][serviceColumnCounter];
        for (int row = 0; row < rowCounter; row++) {
            anotherArray[row] = results[row];
        }
        return anotherArray;
    }

    private List<String> getKey(String serviceName, String datablockName) {
        List<String> key = new ArrayList<String>(2);
        key.add(serviceName);
        key.add(datablockName);
        return key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : serviceSummaryStats.keySet()) {
            sb.append(key).append(": ").append(serviceSummaryStats.get(key)).append("\n");
        }
        return sb.toString();
    }

    public String printTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("BatchIdSummary:\n");
        for (String[] row : get2DResultsArray()) {
            for (String cell : row) {
                if (cell != null) {
                    sb.append(" [" + cell + "] \t");
                    if (cell.length() < 3) sb.append("\t");
                } else {
                    sb.append(" [  ] \t\t");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
