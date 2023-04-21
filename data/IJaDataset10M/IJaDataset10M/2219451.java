package hoplugins.feedback.model.training;

public class SkillUpKey {

    private int playerId;

    private int skill;

    private int value;

    public SkillUpKey(int playerId, int skill, int value) {
        super();
        this.playerId = playerId;
        this.skill = skill;
        this.value = value;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
