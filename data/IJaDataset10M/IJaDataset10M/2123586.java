package com.tenline.pinecone.platform.model;

import java.util.Date;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Bill
 *
 */
@XmlRootElement
@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Transaction extends Entity {

    /**
	 * Transaction's Type
	 */
    public static final String INCOME = "income";

    public static final String PAYOUT = "payout";

    @Persistent
    private String type;

    @Persistent
    private Date timestamp;

    @Persistent
    private Integer nut = Integer.valueOf(0);

    @Persistent(defaultFetchGroup = "true")
    private Application application;

    /**
	 * 
	 */
    public Transaction() {
    }

    /**
	 * @param type the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param timestamp the timestamp to set
	 */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
	 * @return the timestamp
	 */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
	 * @param nut the nut to set
	 */
    public void setNut(Integer nut) {
        this.nut = nut;
    }

    /**
	 * @return the nut
	 */
    public Integer getNut() {
        return nut;
    }

    /**
	 * @param application the application to set
	 */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
	 * @return the application
	 */
    public Application getApplication() {
        return application;
    }
}
