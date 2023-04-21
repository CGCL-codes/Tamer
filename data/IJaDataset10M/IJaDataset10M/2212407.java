package com.tenline.pinecone.platform.model;

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
public class UserRelation extends Entity {

    @Persistent
    private String type;

    @Persistent
    private String userId;

    @Persistent(defaultFetchGroup = "true")
    private User owner;

    /**
	 * 
	 */
    public UserRelation() {
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
	 * @param userId the userId to set
	 */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
	 * @return the userId
	 */
    public String getUserId() {
        return userId;
    }

    /**
	 * @param owner the owner to set
	 */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
	 * @return the owner
	 */
    public User getOwner() {
        return owner;
    }
}
