package org.yafra.rcp.person;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author mwn
 * 
 */
public class MGPerson implements PropertyChangeListener {

    private String firstName;

    private String name;

    private String type;

    private Integer id;

    private String address;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
    }

    public MGPerson() {
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public String getFirstname() {
        return firstName;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setFirstname(String firstName) {
        propertyChangeSupport.firePropertyChange("firstName", this.firstName, this.firstName = firstName);
    }

    public void setType(String type) {
        propertyChangeSupport.firePropertyChange("type", this.type, this.type = type);
    }

    public void setName(String lastName) {
        propertyChangeSupport.firePropertyChange("name", this.name, this.name = lastName);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer guiid) {
        propertyChangeSupport.firePropertyChange("id", this.id, this.id = guiid);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String guiaddress) {
        propertyChangeSupport.firePropertyChange("address", this.address, this.address = guiaddress);
    }

    @Override
    public String toString() {
        return name + " " + firstName;
    }
}
