package org.jbpcc.admin.util.jmx.agent.logging;

import java.io.Serializable;
import java.util.List;

/**
 * A data class holding starting and ending file pointer with requested Log statements
 */
public class LogData implements Serializable {

    private long startFilePointer;

    private long endFilePointer;

    private List<String> logStatements;

    public LogData() {
    }

    public LogData(long startFilePointer, long endFilePointer, List<String> logStatements) {
        this.startFilePointer = startFilePointer;
        this.endFilePointer = endFilePointer;
        this.logStatements = logStatements;
    }

    public long getStartFilePointer() {
        return startFilePointer;
    }

    public void setStartFilePointer(long startFilePointer) {
        this.startFilePointer = startFilePointer;
    }

    public long getEndFilePointer() {
        return endFilePointer;
    }

    public void setEndFilePointer(long endFilePointer) {
        this.endFilePointer = endFilePointer;
    }

    public List<String> getLogStatements() {
        return logStatements;
    }

    public void setLogStatements(List<String> logStatements) {
        this.logStatements = logStatements;
    }
}
