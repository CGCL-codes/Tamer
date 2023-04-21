package de.hpi.eworld.model.db.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Christian Holz
 *
 */
@Entity
@Table(name = "abstractnodes")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_ABSTRACTNODE", sequenceName = "abstractnodes_id_seq")
public abstract class AbstractNode extends EWorldElement {

    protected double longitude;

    protected double latitude;

    protected double altitude;

    /**
	 * id is required by Hibernate for database mapping 
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ABSTRACTNODE")
    private int id;

    protected AbstractNode(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    protected AbstractNode(double latitude, double longitude) {
        this(latitude, longitude, 0);
    }

    /**
	 * This default constructor is required for hibernate
	 * and dragging and dropping 
	 */
    protected AbstractNode() {
    }

    /**
	 * @return
	 */
    public double getLongitude() {
        return longitude;
    }

    /**
	 * @param longitude
	 */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
	 * @return
	 */
    public double getLatitude() {
        return latitude;
    }

    /**
	 * @param latitude
	 */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
	 * @return
	 */
    public double getAltitude() {
        return altitude;
    }

    /**
	 * @param altitude
	 */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    /**
	 * Returns this node's position as a GlobalPosition.
	 * 
	 * @author Martin Beck
	 * @returns Node's position as GlobalPosition.
	 */
    public GlobalPosition getPosition() {
        return new GlobalPosition(latitude, longitude, altitude);
    }

    public String getPointerId() {
        String[] result = this.toString().split("@");
        return result[1];
    }
}
