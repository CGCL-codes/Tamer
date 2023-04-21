package gov.lanl.COAS.odmg;

/**
 * Interface for containing all of the ObservationValue_ objects for COAS
 */
public interface ObservationValue_ {

    /**
	 * Convert ObservationValue_ to XML
	 */
    public String toXML(String indent);

    /**
	 * Convert ObervationValue_ to the COAS ObservationValue Object
	 */
    public org.omg.CORBA.Any[] toObservationValue();

    /**
     * compare to external ObservationValue_
     * @param in
     * @return
     */
    public boolean compare(ObservationValue_ in);
}
