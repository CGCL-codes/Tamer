package com.faralam.apptsvc.model;

import java.io.Serializable;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "serviceAccommodator_address")
@AssociationOverrides({ @AssociationOverride(name = "serviceAccommodator", joinColumns = @JoinColumn(name = "serviceAccommodator_idserviceAccommodator", insertable = false, updatable = false)), @AssociationOverride(name = "address", joinColumns = @JoinColumn(name = "address_idaddress", insertable = false, updatable = false)) })
public class ServiceAccommodator_Address implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ServiceAccommodator_AddressID pk;

    public ServiceAccommodator_Address() {
    }

    public ServiceAccommodator_AddressID getPk() {
        return pk;
    }

    public void setPk(ServiceAccommodator_AddressID pk) {
        this.pk = pk;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\n ServiceAccommodatorAddressID: " + pk.toString());
        buf.append("\n ServiceAccommodator: " + getServiceAccommodator());
        buf.append("\n Address: " + getAddress());
        buf.append("\n Hours: " + getTimeSlotPolicy());
        buf.append("\n SeatingPolicy: " + getSeatingPolicy());
        return buf.toString();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_idaddress", insertable = false, updatable = false, nullable = false)
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address addresse) {
        address = addresse;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serviceAccommodator_idserviceAccommodator", insertable = false, updatable = false, nullable = false)
    private ServiceAccommodator serviceAccommodator;

    public ServiceAccommodator getServiceAccommodator() {
        return serviceAccommodator;
    }

    public void setServiceAccommodator(ServiceAccommodator serviceProviderr) {
        serviceAccommodator = serviceProviderr;
    }

    @OneToOne(mappedBy = "serviceAccommodator_Address", fetch = FetchType.LAZY)
    private SeatingPolicy seatingPolicy;

    @OneToOne(mappedBy = "serviceAccommodator_Address", fetch = FetchType.LAZY)
    private TimeSlotPolicy timeSlotPolicy;

    public SeatingPolicy getSeatingPolicy() {
        return seatingPolicy;
    }

    public void setSeatingPolicy(SeatingPolicy seatingPolicy) {
        this.seatingPolicy = seatingPolicy;
    }

    public TimeSlotPolicy getTimeSlotPolicy() {
        return timeSlotPolicy;
    }

    public void setTimeSlotPolicy(TimeSlotPolicy hours) {
        this.timeSlotPolicy = hours;
    }

    @Embeddable
    static class ServiceAccommodator_AddressID implements Serializable {

        private static final long serialVersionUID = 1L;

        public ServiceAccommodator_AddressID() {
        }

        protected Integer serviceAccommodator_idserviceAccommodator;

        @Column(name = "serviceAccommodator_idserviceAccommodator")
        public Integer getServiceAccommodator_idserviceAccommodator() {
            return serviceAccommodator_idserviceAccommodator;
        }

        public void setServiceAccommodator_idserviceAccommodator(Integer serviceAccommodator_idserviceAccommodator) {
            this.serviceAccommodator_idserviceAccommodator = serviceAccommodator_idserviceAccommodator;
        }

        protected Integer address_idaddress;

        @Column(name = "address_idaddress")
        public Integer getAddress_idaddress() {
            return address_idaddress;
        }

        public void setAddress_idaddress(Integer address_idaddress) {
            this.address_idaddress = address_idaddress;
        }

        @Override
        public boolean equals(Object arg0) {
            if (arg0 instanceof ServiceAccommodator_AddressID) {
                ServiceAccommodator_AddressID that = (ServiceAccommodator_AddressID) arg0;
                return this.serviceAccommodator_idserviceAccommodator.equals(that.serviceAccommodator_idserviceAccommodator) && this.address_idaddress.equals(that.address_idaddress);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return address_idaddress.hashCode();
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("\n serviceAccommodator_idserviceAccommodator: " + serviceAccommodator_idserviceAccommodator);
            buf.append("\n address_idaddress: " + address_idaddress);
            return buf.toString();
        }
    }
}
