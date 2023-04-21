package jmbench.tools.runtime;

import jmbench.tools.OutputError;
import jmbench.tools.TestResults;

/**
 * Measurement of runtime performance.
 *
 * @author Peter Abeles
 */
public class RuntimeMeasurement implements TestResults, Comparable<RuntimeMeasurement> {

    public double opsPerSec;

    public long memoryUsed;

    public OutputError error;

    public RuntimeMeasurement(double opsPerSec, long memoryUsed) {
        this.opsPerSec = opsPerSec;
        this.memoryUsed = memoryUsed;
    }

    public RuntimeMeasurement(double opsPerSec, long memoryUsed, OutputError error) {
        this.opsPerSec = opsPerSec;
        this.memoryUsed = memoryUsed;
        this.error = error;
    }

    public RuntimeMeasurement() {
    }

    public String toString() {
        return "ops/sec = " + opsPerSec;
    }

    public double getOpsPerSec() {
        return opsPerSec;
    }

    public void setOpsPerSec(double opsPerSec) {
        this.opsPerSec = opsPerSec;
    }

    public OutputError getError() {
        return error;
    }

    public void setError(OutputError error) {
        this.error = error;
    }

    public long getMemoryUsed() {
        return memoryUsed;
    }

    public int compareTo(RuntimeMeasurement r) {
        if (r.opsPerSec < opsPerSec) return 1; else if (r.opsPerSec > opsPerSec) return -1;
        return 0;
    }
}
