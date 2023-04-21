package org.personalsmartspace.stdperf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;
import org.personalsmartspace.evaluation.scripts.DelayEvaluationFilter;
import org.personalsmartspace.evaluation.scripts.QuantitativePerformanceResult;
import org.personalsmartspace.log.impl.PersistPerformanceMessage;

public class StreamEvaluationFilter extends DelayEvaluationFilter {

    public StreamEvaluationFilter(String testContext, String component, String opType, int d55TestTableIndex) {
        super(testContext, component, opType, d55TestTableIndex);
    }

    /**
	 * Process the logfile with file name filename and apply it to this Filter.
	 * (Testers do not have to call this method, called by the 
	 * GenericLogParser.ReturnResultsFromLogFile() convenience method.
	 * 
	 * @param filename of the log file
	 * @return A vector with QuantitativePerformanceResults from the log
	 */
    public Vector<QuantitativePerformanceResult> processLogFile(Collection<String> file) {
        Vector<QuantitativePerformanceResult> tmpResults = new Vector<QuantitativePerformanceResult>();
        int lineCounter = 0;
        for (String line : file) {
            lineCounter++;
            PersistPerformanceMessage m = new PersistPerformanceMessage(line);
            if (checkFilterAppliedToPerformanceMessage(m)) {
                String resultName = PersistPerformanceMessage.ReturnPerformanceTypeString(m.getPerformanceType());
                QuantitativePerformanceResult qpResult = new QuantitativePerformanceResult(resultName, Double.parseDouble(m.getValue().trim()));
                tmpResults.add(qpResult);
            }
        }
        System.out.println("Sucessfully filtered PERSIST Logfile!\n");
        return tmpResults;
    }

    /**
	 * Checks whether the message passes the filter
	 * @param performanceMessage the message to check
	 * @return true if message passes the filter 
	 */
    private boolean checkFilterAppliedToPerformanceMessage(PersistPerformanceMessage performanceMessage) {
        if (this.getD55TestTableIndex() == performanceMessage.getD55TestTableIndex() && (this.getOperationType() == null || this.getOperationType().equals(performanceMessage.getOperationType())) && this.getPerformanceType() == performanceMessage.getPerformanceType() && (this.getSourceComponent() == null || this.getSourceComponent().equals(performanceMessage.getSourceComponent())) && (this.getTestContext() == null || this.getTestContext().equals(performanceMessage.getTestContext()))) {
            return true;
        } else {
            return false;
        }
    }
}
