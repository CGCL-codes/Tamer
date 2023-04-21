package playground.sergioo.FacilitiesGenerator;

import org.matsim.api.core.v01.Id;

public class BadStopException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Id stopId;

    public BadStopException(Id stopId) {
        this.stopId = stopId;
    }

    @Override
    public String getMessage() {
        return "Private bus is not enough for stop " + stopId;
    }
}
