package simis.appLayer.slaAgents.lib.serviceDescriptionDocuments;

public class OfferAllocationPolicy {

    private boolean defined;

    private String matchingrule;

    /**
	 * Constructor
	 */
    public OfferAllocationPolicy(boolean defined, String matchingrule) {
        super();
        this.defined = defined;
        this.matchingrule = matchingrule;
    }

    /**
	 * Returns whether or not the matching function is defined for this
	 * negotiation protocol
	 * 
	 * @return flag indicating whether or not the matching function is defined
	 */
    public boolean isDefined() {
        return defined;
    }

    /**
	 * Returns the matching function
	 * 
	 * @return matching function
	 */
    public String getMatchingrule() {
        return matchingrule;
    }

    /**
	 * Overwritten method
	 * 
	 * @return copy of this offer allocation policy object
	 */
    public OfferAllocationPolicy clone() {
        return new OfferAllocationPolicy(this.defined, this.matchingrule);
    }
}
