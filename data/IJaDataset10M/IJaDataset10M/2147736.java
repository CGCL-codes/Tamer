package jat.alg.estimators;

import jat.matvec.data.VectorN;
import jat.measurements.ObservationMeasurement;

public interface MeasurementFileModel {

    /**
	 * Returns the H matrix
	 * @param xref VectorN containing the current state
	 * @return H matrix (measurement state relation)
	 */
    public VectorN H(ObservationMeasurement obs, VectorN xref);

    /**
	 * Returns the measurement noise value
	 * @return measurement noise (sigma^2)
	 */
    public double R(ObservationMeasurement obs);

    /**
	 * Returns the predicted measurement based on the current state
	 * @param index measurement index
	 * @param t time of the measurement
	 * @param xref VectorN with the current state at the measurement time
	 */
    public double zPred(ObservationMeasurement obs, int index, double t, VectorN xref);
}
