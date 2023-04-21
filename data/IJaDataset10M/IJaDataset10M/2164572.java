package fr.gfi.gfinet.web.listener.message;

import java.io.Serializable;

/**
 * Stores a Liferay user information.
 * 
 * @author Rub�n Naranjo
 */
public class LiferayOrganizationInfo implements Serializable {

    private String name;

    private Long portalId;

    private Long parentId;

    /**
	 * Organization info constructor.
	 * 
	 * @param name of the Liferay Organization
	 * @param portalId of the Liferay Organization
	 */
    public LiferayOrganizationInfo(String name, Long portalId, Long parentId) {
        this.name = name;
        this.portalId = portalId;
        this.parentId = parentId;
    }

    /**
	 * @see java.langObject#toString
	 */
    @Override
    public String toString() {
        return "[\"" + name + "\", \"" + portalId + "\"]";
    }

    /**
	 * @return the portalId
	 */
    public Long getPortalId() {
        return portalId;
    }

    /**
	 * @param portalId the portalId to set
	 */
    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
