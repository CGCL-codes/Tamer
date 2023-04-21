package net.sf.istcontract.aws.sensor;

/**
 *
 * @author biba
 */
public interface SensorListener {

    /**
     * Method for submitting already marshalled observation reports (i.e. any of ActionReport, PredicateReport or ACLMesagingReport)
     * @param marchalledObservationReport XML document corresponding to a marshalled reports (one of ActionReport, PredicateReport or ACLMesagingReport)
     */
    void submitObservation(String marchalledObservationReport);
}
