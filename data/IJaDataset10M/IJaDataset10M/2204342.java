package net.sourceforge.greenvine.testmodel.data.entities;

import java.io.Serializable;
import java.lang.String;
import java.lang.Integer;
import javax.persistence.*;

@Entity(name = "Desk")
@Table(name = "DBO.TBL_DESK")
public class Desk implements Comparable<Desk>, Serializable {

    private static final long serialVersionUID = -5890027845226945350L;

    /**
    * Identity property
    */
    private Integer deskId;

    /**
    * code property
    */
    private String code;

    /**
    * deskEmployee property
    */
    private Employee deskEmployee;

    /**
    * Default constructor
    */
    public Desk() {
    }

    /**
    * Simple Property constructor
    */
    public Desk(Integer deskId, String code) {
        this.deskId = deskId;
        this.code = code;
    }

    /**
    * Full Property constructor
    */
    public Desk(Integer deskId, String code, Employee deskEmployee) {
        this.deskId = deskId;
        this.code = code;
        this.deskEmployee = deskEmployee;
    }

    /**
    * Accessor for the identity field
    * @returns the value of the identity field
    */
    @Id
    @Column(name = "DESK_ID", nullable = false)
    public Integer getDeskId() {
        return this.deskId;
    }

    /**
    * Mutator for the identity field
    * @param sets the value of the identity field
    */
    public void setDeskId(Integer deskId) {
        this.deskId = deskId;
    }

    /**
    * Accessor for code field
    * returns the value of the code field
    */
    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "CODE", nullable = false)
    public String getCode() {
        return this.code;
    }

    /**
    * Mutator for the code field
    * @param  sets the value of the code field
    */
    public void setCode(String code) {
        this.code = code;
    }

    /**
    * Accessor for deskEmployee property
    * @return the value of the deskEmployee property. 
    */
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Employee.class, optional = true, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(name = "DBO.TBL_DESK_EMPLOYEE", joinColumns = { @JoinColumn(name = "FK_DESK_ID", referencedColumnName = "DESK_ID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "FK_EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID", nullable = false) }, uniqueConstraints = { @UniqueConstraint(columnNames = { "FK_DESK_ID" }), @UniqueConstraint(columnNames = { "FK_EMPLOYEE_ID" }) })
    public Employee getDeskEmployee() {
        return this.deskEmployee;
    }

    /**
    * Mutator for deskEmployee property
    * @param deskEmployee the new value for the deskEmployee property
    */
    public void setDeskEmployee(Employee deskEmployee) {
        this.deskEmployee = deskEmployee;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (!(that instanceof Desk)) return false;
        Desk thatObj = (Desk) that;
        return this.getCode() == null ? thatObj.getCode() == null : this.getCode().equals(thatObj.getCode()) && true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (null == getCode() ? 0 : getCode().hashCode());
        return hash;
    }

    @Override
    public String toString() {
        String str = "Desk:";
        str += ("Identity = " + (null == deskId ? "null" : deskId.toString())) + ", ";
        str += ("code = " + (null == getCode() ? "null" : getCode().toString())) + ", ";
        str += ("deskEmployee = " + (null == getDeskEmployee() ? "null" : getDeskEmployee().toString())) + ", ";
        return str.substring(0, str.lastIndexOf(", "));
    }

    @Override
    public int compareTo(Desk thatObj) {
        int cmp;
        cmp = this.getCode() == null ? (thatObj.getCode() == null ? 0 : -1) : (thatObj.getCode() == null ? 1 : this.getCode().compareTo(thatObj.getCode()));
        if (cmp != 0) return cmp;
        return cmp;
    }
}
