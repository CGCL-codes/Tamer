package petproject2.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "states", catalog = "petproject2")
@org.hibernate.annotations.Entity(mutable = false)
public class States implements java.io.Serializable {

    private Integer stateId;

    private String stateName;

    private String stateAbbr;

    public States() {
    }

    public States(String stateName, String stateAbbr) {
        this.stateName = stateName;
        this.stateAbbr = stateAbbr;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "stateId", unique = true, nullable = false)
    public Integer getStateId() {
        return this.stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    @Column(name = "stateAbbr")
    public String getStateAbbr() {
        return this.stateAbbr;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    @Column(name = "stateName")
    public String getStateName() {
        return this.stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String toString() {
        return stateAbbr;
    }
}
