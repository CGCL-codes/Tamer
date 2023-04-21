package de.iph.arbeitsgruppenassistent.server.resource.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import de.iph.arbeitsgruppenassistent.server.task.entity.ConcreteTaskStepEntity;

/**
 *
 * @author Andreas Bruns
 */
@Entity
@Table(name = "EMPLOYEE")
public class EmployeeEntity implements Serializable, Comparable<EmployeeEntity> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nativeId;

    private String foreName;

    private String lastName;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private EmployeeContractEntity contractState;

    @OneToMany(mappedBy = "employee")
    @LazyCollection(value = LazyCollectionOption.FALSE)
    private List<EmployeeAbsenceEntity> employeeAbsences;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<QualificationEntity> qualifications;

    @ManyToMany()
    private List<ConcreteTaskStepEntity> concreteTaskSteps;

    private boolean isDeleted = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @LazyToOne(value = LazyToOneOption.FALSE)
    private GroupEntity group;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @LazyToOne(value = LazyToOneOption.FALSE)
    private EmployeeWeightingsEntity weighting;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNativeId() {
        return nativeId;
    }

    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }

    public String getForeName() {
        return foreName;
    }

    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmployeeContractEntity getContractState() {
        return contractState;
    }

    public void setContractState(EmployeeContractEntity contractState) {
        this.contractState = contractState;
    }

    public List<EmployeeAbsenceEntity> getEmployeeAbsences() {
        return employeeAbsences;
    }

    public void setEmployeeAbsences(List<EmployeeAbsenceEntity> employeeAbsences) {
        this.employeeAbsences = employeeAbsences;
    }

    public void setQualifications(List<QualificationEntity> qualifications) {
        this.qualifications = qualifications;
    }

    public List<QualificationEntity> getQualifications() {
        return qualifications;
    }

    public void setConcreteTaskSteps(List<ConcreteTaskStepEntity> concreteTaskSteps) {
        this.concreteTaskSteps = concreteTaskSteps;
    }

    public List<ConcreteTaskStepEntity> getConcreteTaskSteps() {
        return concreteTaskSteps;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public EmployeeWeightingsEntity getWeighting() {
        return weighting;
    }

    public void setWeighting(EmployeeWeightingsEntity weighting) {
        this.weighting = weighting;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof EmployeeEntity)) return false;
        final EmployeeEntity other = (EmployeeEntity) obj;
        if (id != other.id) return false;
        return true;
    }

    @Override
    public String toString() {
        String toReturn = "";
        if (getLastName() != null && !(getLastName().equals(""))) {
            toReturn = toReturn + getLastName();
        }
        if (getForeName() != null && !(getForeName().equals(""))) {
            if (!(toReturn.equals(""))) {
                toReturn = toReturn + ", ";
            }
            toReturn = toReturn + getForeName();
        }
        if (toReturn.equals("")) {
            toReturn = getNativeId() + "";
        }
        return toReturn;
    }

    public int compareTo(EmployeeEntity other) {
        String thisCompareString = this.getLastName() + this.getForeName();
        String otherCompareString = other.getLastName() + other.getForeName();
        return thisCompareString.compareTo(otherCompareString);
    }
}
