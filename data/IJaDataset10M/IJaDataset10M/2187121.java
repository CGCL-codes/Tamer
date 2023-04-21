package playground.sergioo.GTFS2PTSchedule;

import java.util.Date;

public class StopTime {

    private Date arrivalTime;

    private Date departureTime;

    private String stopId;

    /**
	 * @param arrivalTime
	 * @param departureTime
	 * @param stopId
	 */
    public StopTime(Date arrivalTime, Date departureTime, String stopId) {
        super();
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopId = stopId;
    }

    /**
	 * @return the arrivalTime
	 */
    public Date getArrivalTime() {
        return arrivalTime;
    }

    /**
	 * @return the departureTime
	 */
    public Date getDepartureTime() {
        return departureTime;
    }

    /**
	 * @return the stopId
	 */
    public String getStopId() {
        return stopId;
    }

    /**
	 * @param stopId the stopId to set
	 */
    public void setStopId(String stopId) {
        this.stopId = stopId;
    }
}
