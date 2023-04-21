package org.openremote.modeler.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import flexjson.JSON;

/**
 * The Class Device Macro. It's a macro of {@link DeviceCommand}.
 * 
 * @author Dan 2009-7-6
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "device_macro")
public class DeviceMacro extends BusinessEntity {

    /** The device macro items. */
    private List<DeviceMacroItem> deviceMacroItems = new ArrayList<DeviceMacroItem>();

    /** The name. */
    private String name;

    /** The account. */
    private Account account;

    /**
    * Gets the name.
    * 
    * @return the name
    */
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    /**
    * Sets the name.
    * 
    * @param name the new name
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Gets the device macro items. 
    * Here the order of macro items is very important.
    * 
    * @return the device macro items
    */
    @OneToMany(mappedBy = "parentDeviceMacro", cascade = CascadeType.ALL)
    @OrderBy(value = "oid")
    public List<DeviceMacroItem> getDeviceMacroItems() {
        return deviceMacroItems;
    }

    /**
    * Sets the device macro items.
    * 
    * @param deviceMacroItems the new device macro items
    */
    public void setDeviceMacroItems(List<DeviceMacroItem> deviceMacroItems) {
        this.deviceMacroItems = deviceMacroItems;
    }

    /**
    * Gets the account.
    * 
    * @return the account
    */
    @ManyToOne
    @JSON(include = false)
    public Account getAccount() {
        return account;
    }

    /**
    * Sets the account.
    * 
    * @param account the new account
    */
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    @Transient
    public String getDisplayName() {
        return getName();
    }

    @JSON(include = false)
    @Transient
    public Set<DeviceMacro> getSubMacros() {
        Set<DeviceMacro> macros = new HashSet<DeviceMacro>();
        for (DeviceMacroItem item : deviceMacroItems) {
            if (item instanceof DeviceMacroRef) {
                DeviceMacroRef macroRef = (DeviceMacroRef) item;
                DeviceMacro dvcMacro = macroRef.getTargetDeviceMacro();
                macros.add(dvcMacro);
                macros.addAll(dvcMacro.getSubMacros());
            } else if (item instanceof DeviceCommandRef) {
                item.setParentDeviceMacro(this);
            }
        }
        return macros;
    }

    @Override
    public int hashCode() {
        return (int) getOid();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DeviceMacro other = (DeviceMacro) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return this.getOid() == other.getOid();
    }

    public boolean equalsWitoutCompareOid(DeviceMacro other) {
        if (other == null) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (deviceMacroItems == null) {
            if (other.deviceMacroItems != null) return false;
        } else if (!hasSameMacroItems(other.deviceMacroItems)) {
            return false;
        }
        return true;
    }

    private boolean hasSameMacroItems(List<DeviceMacroItem> macroItems) {
        if (macroItems != null && deviceMacroItems != null) {
            if (macroItems.size() != deviceMacroItems.size()) return false;
            for (int i = 0; i < macroItems.size(); i++) {
                if (!deviceMacroItems.get(i).equalsWithoutCompareOid(macroItems.get(i))) return false;
            }
            return true;
        }
        return macroItems == null && deviceMacroItems == null;
    }
}
