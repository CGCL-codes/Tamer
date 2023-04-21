package com.narirelays.ems.persistence.orm;

/**
 * EquipmentFittingId entity. @author MyEclipse Persistence Tools
 */
public class EquipmentFittingId implements java.io.Serializable {

    private String equipmentId;

    private String fittingId;

    /** default constructor */
    public EquipmentFittingId() {
    }

    /** full constructor */
    public EquipmentFittingId(String equipmentId, String fittingId) {
        this.equipmentId = equipmentId;
        this.fittingId = fittingId;
    }

    public String getEquipmentId() {
        return this.equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getFittingId() {
        return this.fittingId;
    }

    public void setFittingId(String fittingId) {
        this.fittingId = fittingId;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof EquipmentFittingId)) return false;
        EquipmentFittingId castOther = (EquipmentFittingId) other;
        return ((this.getEquipmentId() == castOther.getEquipmentId()) || (this.getEquipmentId() != null && castOther.getEquipmentId() != null && this.getEquipmentId().equals(castOther.getEquipmentId()))) && ((this.getFittingId() == castOther.getFittingId()) || (this.getFittingId() != null && castOther.getFittingId() != null && this.getFittingId().equals(castOther.getFittingId())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getEquipmentId() == null ? 0 : this.getEquipmentId().hashCode());
        result = 37 * result + (getFittingId() == null ? 0 : this.getFittingId().hashCode());
        return result;
    }
}
