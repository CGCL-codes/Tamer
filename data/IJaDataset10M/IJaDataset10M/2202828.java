package entity.talent;

public class HeldenTalent {

    private int id = 0;

    private int talentId = 0;

    private int heldId = 0;

    private int talentwert = 0;

    private String kompetenz = null;

    public int getId() {
        return (id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTalentId() {
        return (talentId);
    }

    public void setTalentId(int talentId) {
        this.talentId = talentId;
    }

    public int getHeldId() {
        return (heldId);
    }

    public void setHeldId(int heldId) {
        this.heldId = heldId;
    }

    public int getTalentwert() {
        return (talentwert);
    }

    public void setTalentwert(int talentwert) {
        this.talentwert = talentwert;
    }

    public String getKompetenz() {
        return (kompetenz);
    }

    public void setKompetenz(String kompetenz) {
        this.kompetenz = kompetenz;
    }
}
