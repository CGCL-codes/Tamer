package org.jdiagnose.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdiagnose.Diagnostic;
import org.jdiagnose.DiagnosticContainer;
import org.jdiagnose.DiagnosticMessage;
import org.jdiagnose.MessageFactory;
import org.jdiagnose.ResultInfo;

/**
 * @author jmccrindle
 */
public class DefaultDiagnosticContainerResult implements DiagnosticContainerResult {

    private DiagnosticContainer container;

    private DiagnosticMessage message;

    private List results = new ArrayList();

    private List containerResults = new ArrayList();

    private int totalDiagnostics = 0;

    private MessageFactory messageFactory = null;

    public DefaultDiagnosticContainerResult(MessageFactory messageFactory, DiagnosticContainer container) {
        this.messageFactory = messageFactory;
        this.container = container;
        for (Iterator diagnosticIterator = container.getDiagnosticContainers().iterator(); diagnosticIterator.hasNext(); ) {
            DiagnosticContainer childContainer = (DiagnosticContainer) diagnosticIterator.next();
            DefaultDiagnosticContainerResult containerResult = new DefaultDiagnosticContainerResult(messageFactory, childContainer);
            containerResults.add(containerResult);
            totalDiagnostics += containerResult.getTotalDiagnostics();
        }
        for (Iterator containerIterator = container.getDiagnostics().iterator(); containerIterator.hasNext(); ) {
            Diagnostic diagnostic = (Diagnostic) containerIterator.next();
            DefaultDiagnosticResult result = new DefaultDiagnosticResult(messageFactory, diagnostic);
            results.add(result);
            totalDiagnostics++;
        }
    }

    public int getTotalDiagnostics() {
        return totalDiagnostics;
    }

    public int getFailedDiagnostics() {
        int failedDiagnostics = 0;
        for (Iterator resultIterator = results.iterator(); resultIterator.hasNext(); ) {
            DiagnosticResult result = (DiagnosticResult) resultIterator.next();
            if (result.getState() == ResultState.FAILED) {
                failedDiagnostics++;
            }
        }
        return failedDiagnostics;
    }

    public int getSuccessfulDiagnostics() {
        int successfulDiagnostics = 0;
        for (Iterator resultIterator = results.iterator(); resultIterator.hasNext(); ) {
            DiagnosticResult result = (DiagnosticResult) resultIterator.next();
            if (result.getState() == ResultState.SUCCEEDED) {
                successfulDiagnostics++;
            }
        }
        return successfulDiagnostics;
    }

    public List getResults() {
        return results;
    }

    public String getName() {
        return container.getName();
    }

    protected ResultState getStateFromList(List resultInfos) {
        ResultState state = ResultState.NOT_STARTED;
        boolean allRunning = false;
        boolean allSucceeded = true;
        for (Iterator resultIterator = resultInfos.iterator(); resultIterator.hasNext(); ) {
            ResultInfo result = (ResultInfo) resultIterator.next();
            if (result.getState() == ResultState.FAILED) {
                return ResultState.FAILED;
            } else if (result.getState() == ResultState.RUNNING) {
                allSucceeded = false;
                allRunning = true;
            } else if (result.getState() == ResultState.SUCCEEDED) {
                allRunning = true;
            } else if (result.getState() == ResultState.NOT_STARTED) {
                allSucceeded = false;
            }
        }
        if (allSucceeded) {
            state = ResultState.SUCCEEDED;
        } else if (allRunning) {
            state = ResultState.RUNNING;
        }
        return state;
    }

    public ResultState getState() {
        ResultState resultsState = getStateFromList(results);
        if (resultsState == ResultState.FAILED) {
            return ResultState.FAILED;
        }
        ResultState containersState = getStateFromList(containerResults);
        if (containersState == ResultState.FAILED) {
            return ResultState.FAILED;
        } else if (resultsState == ResultState.RUNNING || containersState == ResultState.RUNNING) {
            return ResultState.RUNNING;
        } else if (resultsState == ResultState.NOT_STARTED && containersState == ResultState.NOT_STARTED) {
            return ResultState.NOT_STARTED;
        } else if (resultsState == ResultState.SUCCEEDED && containersState == ResultState.SUCCEEDED) {
            return ResultState.SUCCEEDED;
        } else {
            return ResultState.NOT_STARTED;
        }
    }

    public long getDuration() {
        return getFinishTime() - getStartTime();
    }

    protected long getStartTimeFromList(List resultInfos) {
        long startTime = Long.MAX_VALUE;
        for (Iterator resultIterator = resultInfos.iterator(); resultIterator.hasNext(); ) {
            ResultInfo result = (ResultInfo) resultIterator.next();
            long resultStartTime = result.getStartTime();
            if (resultStartTime < startTime) {
                startTime = resultStartTime;
            }
        }
        return startTime;
    }

    protected long getFinishTimeFromList(List resultInfos) {
        long finishTime = 0;
        for (Iterator resultIterator = resultInfos.iterator(); resultIterator.hasNext(); ) {
            ResultInfo result = (ResultInfo) resultIterator.next();
            long resultFinishTime = result.getFinishTime();
            if (resultFinishTime > finishTime) {
                finishTime = resultFinishTime;
            }
        }
        return finishTime;
    }

    public long getStartTime() {
        long resultStartTime = getStartTimeFromList(results);
        long containerStartTime = getStartTimeFromList(containerResults);
        if (resultStartTime == Long.MAX_VALUE && containerStartTime == Long.MAX_VALUE) {
            return 0;
        } else if (resultStartTime == Long.MAX_VALUE) {
            return containerStartTime;
        } else if (containerStartTime == Long.MAX_VALUE) {
            return resultStartTime;
        } else {
            return resultStartTime < containerStartTime ? resultStartTime : containerStartTime;
        }
    }

    public long getFinishTime() {
        long resultFinishTime = getFinishTimeFromList(results);
        long containerFinishTime = getFinishTimeFromList(containerResults);
        if (resultFinishTime == 0) {
            return containerFinishTime;
        } else if (containerFinishTime == 0) {
            return resultFinishTime;
        } else {
            return resultFinishTime > containerFinishTime ? resultFinishTime : containerFinishTime;
        }
    }

    public List getContainerResults() {
        return containerResults;
    }

    public DiagnosticContainer getDiagnosticContainer() {
        return container;
    }

    public DiagnosticMessage getMessage() {
        return message;
    }
}
