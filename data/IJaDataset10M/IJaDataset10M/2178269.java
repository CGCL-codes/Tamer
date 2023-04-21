package ie.blackoutscout.common.beans.implementations;

import ie.blackoutscout.common.beans.interfaces.IFixtureTeamScout;
import ie.blackoutscout.common.enums.DefenseType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author John
 */
@Entity
public class FixtureTeamScout implements IFixtureTeamScout {

    @Id
    @GeneratedValue(generator = "id_Gen")
    @SequenceGenerator(name = "id_Gen", sequenceName = "id_Seq")
    private Long id;

    private Integer expansive = 0;

    private Integer creative = 0;

    private Integer pickAndGo = 0;

    private Integer driving = 0;

    private Integer kicking = 0;

    private DefenseType defense;

    private String intensity;

    private Long brTimeStamp;

    public Long getBrTimeStamp() {
        return brTimeStamp;
    }

    public void setBrTimeStamp(Long brTimeStamp) {
        this.brTimeStamp = brTimeStamp;
    }

    public Integer getCreative() {
        return creative;
    }

    public void setCreative(Integer creative) {
        this.creative = creative;
    }

    public DefenseType getDefense() {
        return defense;
    }

    public void setDefense(DefenseType defense) {
        this.defense = defense;
    }

    public Integer getDriving() {
        return driving;
    }

    public void setDriving(Integer driving) {
        this.driving = driving;
    }

    public Integer getExpansive() {
        return expansive;
    }

    public void setExpansive(Integer expansive) {
        this.expansive = expansive;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public Integer getKicking() {
        return kicking;
    }

    public void setKicking(Integer kicking) {
        this.kicking = kicking;
    }

    public Integer getPickAndGo() {
        return pickAndGo;
    }

    public void setPickAndGo(Integer pickAndGo) {
        this.pickAndGo = pickAndGo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
