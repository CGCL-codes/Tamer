package org.objectwiz.franchisemgmt;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author xym
 */
@Entity
public class Planning implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private Technician technician;

    private Set<Intervention> interventions = new HashSet<Intervention>();

    public Planning() {
    }

    public Planning(Technician technician) {
        this.interventions = new HashSet<Intervention>();
        this.technician = technician;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInterventions(Set<Intervention> interventions) {
        this.interventions = interventions;
    }

    @OneToOne(mappedBy = "planning")
    public Technician getTechnician() {
        return technician;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planning")
    public Set<Intervention> getInterventions() {
        return interventions;
    }

    @Override
    public String toString() {
        return "org.objectwiz.franchisemgmt.Planning[id=" + id + "]";
    }
}
