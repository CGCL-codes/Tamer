package net.openchrom.chromatogram.msd.peak.support;

public class RawPeak implements IRawPeak {

    private int startScan = 0;

    private int maximumScan = 0;

    private int retentionTimeMaximum = 0;

    private int stopScan = 0;

    public RawPeak(int startScan, int maximumScan, int stopScan) {
        if (startScan < maximumScan && maximumScan < stopScan) {
            this.startScan = startScan;
            this.maximumScan = maximumScan;
            this.stopScan = stopScan;
        }
    }

    @Override
    public int getMaximumScan() {
        return maximumScan;
    }

    @Override
    public int getStartScan() {
        return startScan;
    }

    @Override
    public int getStopScan() {
        return stopScan;
    }

    @Override
    public int getRetentionTimeAtMaximum() {
        return retentionTimeMaximum;
    }

    @Override
    public void setRetentionTimeAtMaximum(int retentionTime) {
        if (retentionTime >= 0) {
            this.retentionTimeMaximum = retentionTime;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        RawPeak otherRawPeak = (RawPeak) other;
        return this.getStartScan() == otherRawPeak.getStartScan() && this.getMaximumScan() == otherRawPeak.getMaximumScan() && this.getStopScan() == otherRawPeak.getStopScan();
    }

    @Override
    public int hashCode() {
        return 7 * Integer.valueOf(startScan).hashCode() + 11 * Integer.valueOf(maximumScan).hashCode() + 13 * Integer.valueOf(stopScan).hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName());
        builder.append("[");
        builder.append("startScan=" + startScan);
        builder.append(",");
        builder.append("maximumScan=" + maximumScan);
        builder.append(",");
        builder.append("stopScan=" + stopScan);
        builder.append("]");
        return builder.toString();
    }
}
