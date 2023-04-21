package com.faralam.apptsvc.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "appointment")
public class Appointment {

    @EmbeddedId
    private AppointmentID appointmentID = new AppointmentID();

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "customer_idcustomer", nullable = false, updatable = false, insertable = false)
    private Customer customer;

    public Appointment() {
    }

    public AppointmentID getAppointmentID() {
        return this.appointmentID;
    }

    public void setAppointmentID(AppointmentID pk) {
        this.appointmentID = pk;
    }

    private enum Status {

        Proposed, Approved, RejectedByServiceProvider, RejectedByAppointee, RequestCancellationByAppointee, RequestCancellationByServiceProvider, Cancelled
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = { @JoinColumn(name = "timeSlotAccommodated_seat_idseat", referencedColumnName = "seat_idseat", insertable = false, updatable = false), @JoinColumn(name = "timeSlotAccommodated_seatingDetails", referencedColumnName = "SeatingDetails", insertable = false, updatable = false), @JoinColumn(name = "timeSlotAccommodated_StartTime", referencedColumnName = "StartTime", insertable = false, updatable = false), @JoinColumn(name = "timeSlotAccommodated_EndTime", referencedColumnName = "EndTime", insertable = false, updatable = false), @JoinColumn(name = "timeSlotAccommodated_sA_address_sA_idserviceAccommodator", referencedColumnName = "sA_address_sA_idserviceAccommodator", insertable = false, updatable = false), @JoinColumn(name = "timeSlotAccommodated_sA_address_address_idaddress", referencedColumnName = "sA_address_address_idaddress", insertable = false, updatable = false) })
    private TimeSeatSlot timeSlot;

    public TimeSeatSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSeatSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("   Customer: ");
        buf.append(customer);
        buf.append("\n   TimeSlot: FROM ");
        buf.append(appointmentID.getTimeSlotAccommodated_StartTime().getTime());
        buf.append(" - TO - ");
        buf.append(appointmentID.getTimeSlotAccommodated_EndTime().getTime());
        buf.append(" AT ");
        buf.append(timeSlot);
        return buf.toString();
    }

    @Embeddable
    static class AppointmentID implements Serializable {

        public AppointmentID() {
            super();
        }

        private static final long serialVersionUID = 1L;

        private String customer_idcustomer;

        private Integer timeSlotAccommodated_sA_address_sA_idserviceAccommodator;

        private Integer timeSlotAccommodated_sA_address_address_idaddress;

        public String getCustomer_idcustomer() {
            return customer_idcustomer;
        }

        @Column(name = "timeSlotAccommodated_StartTime")
        @Temporal(TemporalType.TIMESTAMP)
        private Calendar timeSlotAccommodated_StartTime;

        public Calendar getTimeSlotAccommodated_StartTime() {
            return timeSlotAccommodated_StartTime;
        }

        public void setTimeSlotAccommodated_StartTime(Calendar timeSlot_StartTime) {
            this.timeSlotAccommodated_StartTime = timeSlot_StartTime;
        }

        @Column(name = "timeSlotAccommodated_EndTime")
        @Temporal(TemporalType.TIMESTAMP)
        private Calendar timeSlotAccommodated_EndTime;

        public Calendar getTimeSlotAccommodated_EndTime() {
            return timeSlotAccommodated_EndTime;
        }

        public void setTimeSlotAccommodated_EndTime(Calendar timeSlot_EndTime) {
            this.timeSlotAccommodated_EndTime = timeSlot_EndTime;
        }

        public void setCustomer_idcustomer(String customer_idcustomer) {
            this.customer_idcustomer = customer_idcustomer;
        }

        public Integer getTimeSlotAccommodated_sA_address_sA_idserviceAccommodator() {
            return timeSlotAccommodated_sA_address_sA_idserviceAccommodator;
        }

        public void setTimeSlotAccommodated_sE_address_sE_idserviceAccommodated(Integer timeSlot_sP_address_sP_idserviceProvider) {
            this.timeSlotAccommodated_sA_address_sA_idserviceAccommodator = timeSlot_sP_address_sP_idserviceProvider;
        }

        public Integer getTimeSlotAccommodated_serviceAccommodator_address_address_idaddress() {
            return timeSlotAccommodated_sA_address_address_idaddress;
        }

        public void setTimeSlotAccommodated_serviceAccommodator_address_address_idaddress(Integer timeSlotEstablishment_serviceEstablishment_address_address_idaddress) {
            this.timeSlotAccommodated_sA_address_address_idaddress = timeSlotEstablishment_serviceEstablishment_address_address_idaddress;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                AppointmentID that = (AppointmentID) o;
                return this.timeSlotAccommodated_StartTime.equals(that.timeSlotAccommodated_StartTime) && this.customer_idcustomer.equals(that.customer_idcustomer);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return customer_idcustomer.hashCode() + timeSlotAccommodated_StartTime.hashCode();
        }
    }
}
