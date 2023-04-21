package org.blueoxygen.papaje.location;

import org.blueoxygen.cimande.persistence.PersistenceAware;
import org.blueoxygen.cimande.persistence.PersistenceManager;
import org.blueoxygen.papaje.entity.Location;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author leo
 *
 */
public class LocationForm extends ActionSupport implements PersistenceAware {

    private PersistenceManager manager;

    private Location location = new Location();

    private String id = "";

    private String msg = "";

    private String countryId = "";

    public String execute() {
        if (!"".equals(getId())) {
            location = (Location) manager.getById(Location.class, getId());
        }
        return SUCCESS;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.manager = persistenceManager;
    }

    /**
	 * @return the countryId
	 */
    public String getCountryId() {
        return countryId;
    }

    /**
	 * @param countryId the countryId to set
	 */
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the location
	 */
    public Location getLocation() {
        return location;
    }

    /**
	 * @param location the location to set
	 */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
	 * @return the msg
	 */
    public String getMsg() {
        return msg;
    }

    /**
	 * @param msg the msg to set
	 */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
	 * @return the manager
	 */
    public PersistenceManager getManager() {
        return manager;
    }
}
