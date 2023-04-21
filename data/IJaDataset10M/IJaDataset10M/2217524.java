package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * The service types for a roster entry
 * 
 * @author Michael
 */
public class ServiceType extends AbstractMessage {

    public static final String ID = "serviceType";

    public static final String SERVICETYPE_FREIWILLIG = "Freiwillig";

    public static final String SERVICETYPE_HAUPTAMTLICH = "Hauptamtlich";

    public static final String SERIVCETYPE_ZIVILDIENER = "Zivildiener";

    private int id;

    private String serviceName;

    /**
	 * Default class constructor
	 */
    public ServiceType() {
        super(ID);
        id = -1;
        serviceName = "";
    }

    /**
	 * Default class constructor for a service type
	 */
    public ServiceType(String serviceName) {
        this();
        this.serviceName = serviceName;
    }

    /**
	 * Returns the string based description
	 * 
	 * @return the string description
	 */
    @Override
    public String toString() {
        return id + ":" + serviceName;
    }

    /**
	 * Returns the calculated hash code based on the job id.<br>
	 * Two service types have the same hash code if the id is the same.
	 * 
	 * @return the calculated hash code
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /**
	 * Returns whether the objects are equal or not.<br>
	 * Two service types are equal if, and only if, the service id is the same.
	 * 
	 * @return true if the id is the same otherwise false.
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ServiceType other = (ServiceType) obj;
        if (id != other.id) return false;
        return true;
    }

    /**
	 * Returns the internal unique id of the service type.
	 * 
	 * @return the id the id of the database
	 */
    public int getId() {
        return id;
    }

    /**
	 * Returns the name of the service
	 * 
	 * @return the service name
	 */
    public String getServiceName() {
        return serviceName;
    }

    /**
	 * Sets the unique number of the job.
	 * 
	 * @param id
	 *            the unique id for the job
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * Sets the name of the service
	 * 
	 * @param serviceName
	 *            the serviceName to set
	 */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
