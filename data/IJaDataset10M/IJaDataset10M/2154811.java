package de.d3web.we.ci4ke.build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import de.d3web.we.ci4ke.testing.CITestResult;
import de.d3web.we.ci4ke.testing.CITestResult.Type;
import de.d3web.we.ci4ke.util.Pair;

/**
 * An instance of this class holds the result of a ci build
 * 
 * @author Marc-Oliver Ochlast
 */
public final class CIBuildResultset {

    /**
	 * time/date of build execution
	 */
    private final Date buildExecutionDate;

    private final List<Pair<String, CITestResult>> results;

    private long timeSpentForBuild;

    /**
	 * The duration is given in milliseconds.
	 * 
	 * @created 03.02.2012
	 * @return in milliseconds
	 */
    public long getTimeSpentForBuild() {
        return timeSpentForBuild;
    }

    /**
	 * The duration is given in milliseconds.
	 * 
	 * @created 03.02.2012
	 * @param timeSpentForBuild in milliseconds
	 */
    public void setTimeSpentForBuild(long timeSpentForBuild) {
        this.timeSpentForBuild = timeSpentForBuild;
    }

    public CIBuildResultset() {
        super();
        this.buildExecutionDate = new Date();
        this.results = new ArrayList<Pair<String, CITestResult>>();
        this.timeSpentForBuild = 0;
    }

    public Date getBuildExecutionDate() {
        return buildExecutionDate;
    }

    public List<Pair<String, CITestResult>> getResults() {
        return Collections.unmodifiableList(results);
    }

    /**
	 * Computes the overall TestResultType of this resultset, determined by the
	 * "worst" Testresult
	 * 
	 * @created 03.06.2010
	 * @return
	 */
    public Type getOverallResult() {
        Type overallResult = Type.SUCCESSFUL;
        for (Pair<String, CITestResult> resultPair : results) {
            CITestResult testResult = resultPair.getB();
            if (testResult != null && testResult.getType().compareTo(overallResult) > 0) {
                overallResult = testResult.getType();
            }
        }
        return overallResult;
    }

    public String getTestresultMessages() {
        StringBuffer sb = new StringBuffer();
        for (Pair<String, CITestResult> resultPair : results) {
            String testName = resultPair.getA();
            CITestResult testResult = resultPair.getB();
            sb.append(testName + ": ");
            if (testResult.hasMessage()) {
                sb.append(testResult.getMessage());
            } else {
                sb.append("(no resultmessage)");
            }
            sb.append("\n<br/><br/>\n");
        }
        return sb.toString();
    }

    public void addTestResult(String testname, CITestResult testResult) {
        if (testname != null && !testname.isEmpty() && testResult != null) {
            results.add(new Pair<String, CITestResult>(testname, testResult));
        } else {
            throw new IllegalArgumentException("addTestResult() received illegal arguments!");
        }
    }
}
