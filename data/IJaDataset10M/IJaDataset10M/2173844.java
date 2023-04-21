package org.openremote.modeler.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import flexjson.JSON;

/**
 * It represents a switch entity, includes sensor, on command and off command.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "switch")
public class Switch extends BusinessEntity {

    private String name;

    private SwitchCommandOnRef switchCommandOnRef;

    private SwitchCommandOffRef switchCommandOffRef;

    private SwitchSensorRef switchSensorRef;

    private Account account;

    private Device device;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(mappedBy = "offSwitch", cascade = CascadeType.ALL)
    public SwitchCommandOffRef getSwitchCommandOffRef() {
        return switchCommandOffRef;
    }

    public void setSwitchCommandOffRef(SwitchCommandOffRef switchCommandOffRef) {
        this.switchCommandOffRef = switchCommandOffRef;
    }

    @OneToOne(mappedBy = "onSwitch", cascade = CascadeType.ALL)
    public SwitchCommandOnRef getSwitchCommandOnRef() {
        return switchCommandOnRef;
    }

    public void setSwitchCommandOnRef(SwitchCommandOnRef switchCommandOnRef) {
        this.switchCommandOnRef = switchCommandOnRef;
    }

    @OneToOne(mappedBy = "switchToggle", cascade = CascadeType.ALL)
    public SwitchSensorRef getSwitchSensorRef() {
        return switchSensorRef;
    }

    public void setSwitchSensorRef(SwitchSensorRef switchSensorRef) {
        this.switchSensorRef = switchSensorRef;
    }

    @ManyToOne
    @JSON(include = false)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @ManyToOne
    @JSON(include = false)
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Transient
    public String getDisplayName() {
        return getName();
    }

    public int hashCode() {
        return (int) getOid();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Switch other = (Switch) obj;
        if (this.getOid() != other.getOid()) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (switchCommandOffRef == null) {
            if (other.switchCommandOffRef != null) return false;
        } else if (!switchCommandOffRef.equals(other.switchCommandOffRef)) return false;
        if (switchCommandOnRef == null) {
            if (other.switchCommandOnRef != null) return false;
        } else if (!switchCommandOnRef.equals(other.switchCommandOnRef)) return false;
        if (switchSensorRef == null) {
            if (other.switchSensorRef != null) return false;
        } else if (!switchSensorRef.equals(other.switchSensorRef)) return false;
        return true;
    }

    /**
    * Equals without compare oid.
    * Used for rebuilding from template.
    * 
    * @param swh the swh
    * 
    * @return true, if successful
    */
    public boolean equalsWithoutCompareOid(Switch swh) {
        if (name == null) {
            if (swh.name != null) return false;
        } else if (!name.equals(swh.name)) return false;
        if (switchCommandOffRef == null) {
            if (swh.switchCommandOffRef != null) return false;
        } else if (swh.switchCommandOffRef == null || !switchCommandOffRef.equalsWithoutCompareOid(swh.switchCommandOffRef)) return false;
        if (switchCommandOnRef == null) {
            if (swh.switchCommandOnRef != null) return false;
        } else if (swh.switchCommandOnRef == null || !switchCommandOnRef.equalsWithoutCompareOid(swh.switchCommandOnRef)) return false;
        if (switchSensorRef == null) {
            if (swh.switchSensorRef != null) return false;
        } else if (swh.switchSensorRef == null || !switchSensorRef.equalsWithoutCompareOid(swh.switchSensorRef)) return false;
        return true;
    }
}
