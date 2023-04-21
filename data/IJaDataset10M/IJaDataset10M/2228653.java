package net.sf.uboss.impl;

import java.util.Date;
import net.sf.uboss.api.BaseItem;

/**
 * @author <a href="mailto:vivek@techartifact.com">Vivek Kumar</a>
 * @version $Id:
 */
public class BaseItemImpl implements BaseItem {

    private static final long serialVersionUID = 1447593352451698863L;

    private String name;

    private boolean enabled;

    private long principalId;

    private long modifiedBy;

    private Date modifiedDate;

    /**
     * @return the modifiedDate
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * @param modifiedDate the modifiedDate to set
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * @return the modifiedBy
     */
    public long getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the principalId
     */
    public long getPrincipalId() {
        return principalId;
    }

    /**
     * @param principalId the principalId to set
     */
    public void setPrincipalId(long principalId) {
        this.principalId = principalId;
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

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
