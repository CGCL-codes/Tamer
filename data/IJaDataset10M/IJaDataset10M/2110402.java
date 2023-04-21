package se.openprocesslogger.svg.data;

import java.io.Serializable;

public class DataPoint implements Comparable<DataPoint>, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2071503070556115663L;

    public double value;

    public long timestamp;

    public int pointType;

    public DataPoint() {
    }

    ;

    public DataPoint(double value, long timestamp, int pointType) {
        super();
        this.value = value;
        this.timestamp = timestamp;
        this.pointType = pointType;
    }

    @Override
    public int compareTo(DataPoint arg0) {
        if (this.timestamp > arg0.timestamp) return 1;
        if (this.timestamp == arg0.timestamp) return 0;
        return -1;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPointType() {
        return pointType;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
    }
}
