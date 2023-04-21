package uk.ac.city.soi.everestplus.core;

import org.slaatsoi.prediction.schema.PredictionPolicyType;

/**
 * @author Davide Lorenzoli
 * 
 * @date Apr 29, 2010
 */
public class Prediction {

    public static final double DEFAULT_VALUE = -1d;

    private PredictionPolicyType predictionPolicy;

    private double value;

    private long timestamp;

    private int dataPointSize;

    /**
	 * @param predictionSpecification
	 * @param timestamp
	 * @param value The predicted value, Double.NaN if a prediction couldn't be computed
	 */
    public Prediction(PredictionPolicyType predictionPolicy, double value, long timestamp) {
        this.predictionPolicy = predictionPolicy;
        this.timestamp = timestamp;
        this.value = value;
        this.dataPointSize = -1;
    }

    /**
	 * @return the predictionSpecification
	 */
    public PredictionPolicyType getPredictionSpecification() {
        return predictionPolicy;
    }

    /**
	 * @return the dataPointSize
	 */
    public int getDataPointSize() {
        return dataPointSize;
    }

    /**
	 * @param dataPointSize the dataPointSize to set
	 */
    public void setDataPointSize(int dataPointSize) {
        this.dataPointSize = dataPointSize;
    }

    /**
	 * @return The predicted value, Double.NaN if a prediction couldn't be computed
	 */
    public double getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(double value) {
        this.value = value;
    }

    /**
	 * @return the timestamp
	 */
    public long getTimestamp() {
        return timestamp;
    }

    /**
	 * @param timestamp the timestamp to set
	 */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Prediction{" + "predictionPolicy=" + predictionPolicy + "," + "predictionValue=" + value + "," + "timestamp=" + timestamp + "}";
    }
}
