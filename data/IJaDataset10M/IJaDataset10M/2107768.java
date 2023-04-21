package bean.equipment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "equipment_register_input")
@PrimaryKeyJoinColumn(name = "idInput")
public class EquipmentRegisterIn extends EquipmentRegister {

    private Integer idInput;

    private double unitPrice;

    public EquipmentRegisterIn() {
    }

    public EquipmentRegisterIn(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Column(name = "idInput", nullable = false, updatable = false, insertable = false)
    public Integer getIdInput() {
        return idInput;
    }

    public void setIdInput(Integer idInput) {
        this.idInput = idInput;
    }

    @Column(name = "unitPrice")
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Transient
    public double getTotalPrice() {
        return unitPrice * quantity;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
