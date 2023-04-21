package pt.ips.estsetubal.mig.academicCloud.shared.dto.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * This class is the DTO for the <code>DomainEntity</code> entity.
 * 
 * @see pt.ips.estsetubal.mig.academicCloud.server.domain.DomainEntity
 * @author António Casqueiro
 */
public abstract class DomainEntityDTO implements Serializable {

    private static final long serialVersionUID = 2882971322727549752L;

    private String pk;

    private String parentKey;

    /**
	 * When the object was created.
	 */
    private Date createDate;

    /**
	 * Username of the user that created the object.
	 */
    private String createUsername;

    /**
	 * When the object was updated.
	 */
    private Date updateDate;

    /**
	 * Username of the user that updated the object.
	 */
    private String updateUsername;

    /**
	 * Default constructor.
	 */
    public DomainEntityDTO() {
        super();
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUsername() {
        return updateUsername;
    }

    public void setUpdateUsername(String updateUsername) {
        this.updateUsername = updateUsername;
    }
}
